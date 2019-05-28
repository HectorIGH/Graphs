/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import javafx.util.Pair;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYSeries;

/**
 *
 * @author Hector Ivan Garcia-Hernandez
 */
public class Graph {

    HashMap<Integer, Node> Nodes = new HashMap<>();
    HashMap<Integer, Edge> Edges = new HashMap<>();
    HashMap<Integer, Edge> treeEdges = new HashMap<>();
    int loadedGraphDirected = 0;
    int dirigido = 0;
    int reachableNodes = 0;
    String WAVpath = "";
    boolean loadWAV = false;
    WavFile wavFile = null;

    public Graph() {
        //Constructor with no parameters
    }

    public int createNode(int id, String name, String data) {
        Node nodo = new Node(id, name, data);
        Nodes.put(id, nodo);
        return 0;
    }

    public int createNodeXY(int id, String name, double x, double y, String data) {
        Node nodo = new Node(id, name, x, y, data);
        Nodes.put(id, nodo);
        return 0;
    }

    public int createEdge(int id, Node a, Node b, String data, int dir) {
        Edge edge = new Edge(a, b, data);
        Edges.put(id, edge);
        a.updateGrad();
        b.updateGrad();
        if (dir == 0) {
            a.setAdjacent(b);
            b.setAdjacent(a);
        } else {
            a.setAdjacent(b);
        }
        return 0;
    }

    public int createTreeEdge(int id, Node a, Node b, String data) {
        Edge edge = new Edge(a, b, data);
        treeEdges.put(id, edge);
        return 0;
    }

    public int getGradNode(Node node) {
        return node.getGrad();
    }

    public void createNodes(int n) {
        int i;
        for (i = 0; i < n; i++) { // Creates n nodes
            createNode(i, "Node " + i, "No data");
        }
    }
    
    public void sieveNodes() {
        Set<Integer> reachable = new HashSet<>();
        Set set = Edges.entrySet();
        Iterator i = set.iterator();
        while(i.hasNext()) {
            Map.Entry mentry = (Map.Entry)i.next();
            Edge edge = (Edge)mentry.getValue();
            reachable.add(edge.getNodes().get(1).getId());
            reachable.add(edge.getNodes().get(2).getId());
        }
        this.reachableNodes = reachable.size();
    }
    
    public void createNodesFromFile(Set<Integer> nodesFromFile) {
        for (int index : nodesFromFile) {
            createNode(index, "Node " + index, "No data");
            //System.out.println(index);
        }
    }

    public void createNodesXY(int n) {
        int i;
        double x,y;
        Random rand = new Random();
        for (i = 0; i <n; i++) { // Creates n nodes with coordinates inside a box size one
            x = rand.nextFloat();
            y = rand.nextFloat();
            createNodeXY(i, "Node " + i, x, y, "No data");
        }
    }

    public Graph Erdos(int n, int m, int dir, int cic) {
        this.Nodes.clear();
        this.Edges.clear();
        Random rand = new Random();
        int j = 0, indexA, indexB, create = 1;
        createNodes(n);

        //for (j = 0; j < m; j++) { // Creates m edges
        while(Edges.size() < m) {
            indexA = rand.nextInt(n); // Choose at random node A
            indexB = rand.nextInt(n); // Choose at random node B
            if (cic == 0) { // Loops not allowed in graph
                while (indexA == indexB) { // We cycle until node B differs from node A
                    indexB = rand.nextInt(n);
                }
            }
            Node a = Nodes.get(indexA); // Retrieve node A
            Node b = Nodes.get(indexB); // Retrieve node B
            Set set = Edges.entrySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()) { // We iterate over the existing edges
                Map.Entry mentry = (Map.Entry)iterator.next();
                mentry.getKey();
                Edge edge = (Edge)mentry.getValue();
                HashMap<Integer, Node> edgeNodes = edge.getNodes();
                Node A = edgeNodes.get(1); // Node a from the edge
                Node B = edgeNodes.get(2); // Node b from the edge
                create = 1;
                if ((a == A && b == B) || (a == B && b == A && dir == 0)) { // The edge already exists
                    create = 0; // Since exists, we don't create it again
                    break;
                }
            }
            if (create != 0) {
                createEdge(j, a, b, "Connects node " + a.getId()+" and node " + b.getId(), dir); // Creates edge
                create = 1; // Reset condition
                //System.out.println("Estoy creando aristas.");
            }
            j++;
        }
        this.sieveNodes();
        return this;
    }

    public Graph Gilbert(int n, double p, int dir, int cic) {
        this.Nodes.clear();
        this.Edges.clear();
        Random rand = new Random();
        int i, j, NE = 0, create = 1;
        double probability;
        createNodes(n);

        for(i = 0; i < n; i++) { // We iterate as long as there are nodes left
            for(j = 0; j < n; j++){
                if (cic == 0 && (i == j)) { // Loops not allowed
                    continue;
                }
                //rand.setSeed(j);
                probability = rand.nextFloat(); // Generates a random probability
                if (probability > p) { // Generate an edge
                    Node a = Nodes.get(i);
                    Node b = Nodes.get(j);
                    Set set = Edges.entrySet();
                    Iterator iterator = set.iterator();
                    while(iterator.hasNext()) { // We iterate over the existing edges
                        Map.Entry mentry = (Map.Entry)iterator.next();
                        mentry.getKey();
                        Edge edge = (Edge)mentry.getValue();
                        HashMap<Integer, Node> edgeNodes = edge.getNodes();
                        Node A = edgeNodes.get(1); // Node a from the edge
                        Node B = edgeNodes.get(2); // Node b from the edge
                        create = 1;
                        if ((a == A && b == B) || (a == B && b == A && dir == 0)) { // The edge already exists
                            create = 0; // Since exists, we don't create it again
                            break;
                        }
                    }
                    if(create != 0) {
                        createEdge(NE, a, b, "Connects node " + a.getId() +" and node " + b.getId(), dir);
                        NE++;
                        create = 1;
                    }
                }
            }
        }
        probability = rand.nextFloat();
        if (probability > p) {
            Node a = Nodes.get(Nodes.size() - 1);
            Node b = Nodes.get(0);
            Set set = Edges.entrySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()) { // We iterate over the existing edges
                Map.Entry mentry = (Map.Entry)iterator.next();
                mentry.getKey();
                Edge edge = (Edge)mentry.getValue();
                HashMap<Integer, Node> edgeNodes = edge.getNodes();
                Node A = edgeNodes.get(1); // Node a from the edge
                Node B = edgeNodes.get(2); // Node b from the edge
                create = 1;
                if ((a == A && b == B) || (a == B && b == A && dir == 0)) { // The edge already exists
                    create = 0; // Since exists, we don't create it again
                    break;
                }
            }
            if(create != 0) {
                createEdge(NE, a, b, "Connects node " + a.getId() +" and node " + b.getId(), dir);
            }
        }
        //System.out.println(Edges.size());
        this.sieveNodes();
        return this;
    }

    public Graph Geo(int n, double r, int dir, int cic) {
        this.Nodes.clear();
        this.Edges.clear();
        int i, j, NE = 0, create = 1;
        createNodesXY(n);

        List index = new ArrayList();
        List jndex = new ArrayList();
        for (int k = 0; k < n; k++) {
            index.add(k);
            jndex.add(k);
        }
        Collections.shuffle(index);
        Collections.shuffle(jndex);
        int I, J;

        for(i = 0; i < n; i++) { // We iterate as long as there are nodes left
            I = (int)index.get(i);
            for(j = 0; j < n; j++){
                J = (int)jndex.get(j);
                if (cic == 0 && (I == J)) { // Loops not allowed
                    continue;
                }
                Node a = Nodes.get(I);
                Node b = Nodes.get(J);
                double x1 = a.getX();
                double y1 = a.getY();
                double x2 = b.getX();
                double y2 = b.getY();
                double d = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
                //System.out.println(d + ",("+I+", "+J+")"+"-"+NE);
                if (d <= r) { // Generate an edge
                    Set set = Edges.entrySet();
                    Iterator iterator = set.iterator();
                    while(iterator.hasNext()) { // We iterate over the existing edges
                        Map.Entry mentry = (Map.Entry)iterator.next();
                        mentry.getKey();
                        Edge edge = (Edge)mentry.getValue();
                        HashMap<Integer, Node> edgeNodes = edge.getNodes();
                        Node A = edgeNodes.get(1); // Node a from the edge
                        Node B = edgeNodes.get(2); // Node b from the edge
                        create = 1;
                        if ((a == A && b == B) || (a == B && b == A && dir == 0)) { // The edge already exists
                            create = 0; // Since exists, we don't create it again
                            break;
                        }
                    }
                    if(create != 0) {
                        createEdge(NE, a, b, "Connects node " + a.getId() +" and node " + b.getId(), dir);
                        NE++;
                        create = 1;
                    }
                }
            }
        }
        Node a = Nodes.get(Nodes.size() - 1);
        Node b = Nodes.get(0);
        double x1 = a.getX();
        double y1 = a.getY();
        double x2 = b.getX();
        double y2 = b.getY();
        double d = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        if (d <= r) {
            Set set = Edges.entrySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()) { // We iterate over the existing edges
                Map.Entry mentry = (Map.Entry)iterator.next();
                mentry.getKey();
                Edge edge = (Edge)mentry.getValue();
                HashMap<Integer, Node> edgeNodes = edge.getNodes();
                Node A = edgeNodes.get(1); // Node a from the edge
                Node B = edgeNodes.get(2); // Node b from the edge
                create = 1;
                if ((a == A && b == B) || (a == B && b == A && dir == 0)) { // The edge already exists
                    create = 0; // Since exists, we don't create it again
                    break;
                }
            }
            if(create != 0) {
                createEdge(NE, a, b, "Connects node " + a.getId() +" and node " + b.getId(), dir);
            }
        }
        //System.out.println(Edges.size());
        this.sieveNodes();
        return this;
    }

    public Graph Barabasi(int n, int d, double g, int dir, int cic) {
        this.Nodes.clear();
        this.Edges.clear();
        int Ne = 0;
        double p = 0, pr;
        Random rand = new Random();
        for (int i = 0; i < n; i++) { // Iterate d times to create n nodes
            createNode(i, "Node " + i, "No data"); // Creates a node
            for (int j = 0; j < Nodes.size() - (1 - cic); j++) { // Iterate over the existing nodes to connect them
                // The (1 - cic) part controls whether we allow or not a self-loop
                p = rand.nextFloat(); // Random probability
                pr = 1 - Nodes.get(j).getGrad() / (double)(g); // Get a value proportional to the grad of the node
                if (i < d) {
                    pr = 1;
                }
                if (pr > p) {
                    Node A = Nodes.get(Nodes.size() - 1);
                    Node B = Nodes.get(j);
                    createEdge(Ne, A, B, "Connects node " + A.getId() + " and node " + B.getId(), dir);
                    Ne++;
                }
            }
        }
        this.sieveNodes();
        return this;
    }

    public HashMap<Integer, Node> getNodesGraph() {
        return Nodes;
    }

    public HashMap<Integer, Edge> getEdgesGraph() {
        return Edges;
    }

    public int graphGraph(Graph grapho, int dir, String name) {
        //System.out.println("The graph will be saved in: C:\\temp\\"+name+".gv");
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(name));
        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            name = fc.getCurrentDirectory().toString() + "\\" + fc.getSelectedFile().getName();
            HashMap<Integer, Edge> EG = grapho.getEdgesGraph();
            try (FileWriter fw = new FileWriter(name+".gv");
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw)) {
                if (dir == 0) { // Not directed graph
                    out.println("strict graph{");
                    out.flush();
                } else { // Directed graph
                    out.println("strict digraph{");
                    out.flush();
                }

            Set setE = EG.entrySet();
            Iterator it = setE.iterator();
            while(it.hasNext()) {
                Map.Entry mentry = (Map.Entry)it.next();
                //System.out.print("Key is: " + mentry.getKey() + " & connects: ");
                Edge edge = (Edge)mentry.getValue();
                HashMap<Integer, Node> anode = edge.getNodes();
                Node A = anode.get(1);
                Node B = anode.get(2);
                //System.out.println("node " + A.getId() + " & node " + B.getId());
                if (dir == 0) { // Not directed graph
                    //out.println("   \"" + A.getId() + "," + A.getGrad() + "\"--\"" + B.getId() + "," + B.getGrad() + "\"");
                    out.println("   \"" + A.getId() + "\"--\"" + B.getId() + "\"");
                    out.flush();
                } else { // Directed graph
                    //out.println("   \"" + A.getId() + "," + A.getGrad() + "\"->\"" + B.getId() + "," + B.getGrad() + "\"");
                    out.println("   \"" + A.getId() + "\"->\"" + B.getId() + "\"");
                    out.flush();
                }
            }
            out.println("}");
            out.close();
            JOptionPane.showMessageDialog(null, "The graph will be saved in: " + name + ".gv", "Graph Created", JOptionPane.INFORMATION_MESSAGE);
            return 1;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "The graph couldn't be saved in: " + name + ".gv", "Graph Not Created", JOptionPane.INFORMATION_MESSAGE);
                return 0;
            }
        } else {
            JOptionPane.showMessageDialog(null, "The graph was not saved.", "Graph Not Created", JOptionPane.INFORMATION_MESSAGE);
        }
        return 1;
    }

    public HashMap<Integer, Edge> getEdgesTree() {
        return treeEdges;
    }

    public int graphTree(Graph grafo, String name) {
        //System.out.println("The graph will be saved in: C:\\temp\\"+name+".gv");
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(name));
        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            name = fc.getCurrentDirectory().toString() + "\\" + fc.getSelectedFile().getName();
            HashMap<Integer, Edge> EG = grafo.getEdgesTree();
            try (FileWriter fw = new FileWriter(name+".gv");
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
                out.println("strict graph{");
                out.flush();

                Set setE = EG.entrySet();
                Iterator it = setE.iterator();
                while(it.hasNext()) {
                    Map.Entry mentry = (Map.Entry)it.next();
                    //System.out.print("Key is: " + mentry.getKey() + " & connects: ");
                    Edge edge = (Edge)mentry.getValue();
                    HashMap<Integer, Node> anode = edge.getNodes();
                    Node A = anode.get(1);
                    Node B = anode.get(2);
                    out.println("   \"" + A.getId() + "\"--\"" + B.getId() + "\"");
                    out.flush();
                }
                out.println("}");
                out.close();
                JOptionPane.showMessageDialog(null, "The graph will be saved in: " + name + ".gv", "Graph Created", JOptionPane.INFORMATION_MESSAGE);
                return 1;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "The graph couldn't be saved in: " + name + ".gv", "Graph Not Created", JOptionPane.INFORMATION_MESSAGE);
                return 0;
            }
        } else {
            JOptionPane.showMessageDialog(null, "The graph was not saved.", "Graph Not Created", JOptionPane.INFORMATION_MESSAGE);
        }
        return 1;
    }

    public int readGraph(Graph grafo) {
        Set<Integer> nodesFromFile = new HashSet<>();
        grafo.Nodes.clear();
        grafo.Edges.clear();
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(System.getProperty("user.home")));
        JFrame f = new JFrame("");
        JPanel p = new JPanel(new GridLayout(0,1));
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            SwingWorker<Void, Void> worker;
            final JDialog dialog = new JDialog(f, true);
            dialog.setUndecorated(true);
            dialog.setLocationRelativeTo(null);
            dialog.setLocation(f.getLocation().x + f.getSize().width / 4, f.getLocation().y + f.getSize().height / 4);
            JProgressBar bar = new JProgressBar();
            bar.setIndeterminate(true);
            bar.setStringPainted(true);
            bar.setBackground(Color.green);
            bar.setString("Loading. Please wait...");
            dialog.add(bar);
            dialog.pack();
            worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    int dir = 0;
                    int edges = 0;
                    int nodes = 0;
                    File selectedFile = fc.getSelectedFile();
                    Path path = Paths.get(selectedFile.getAbsolutePath());
                    //System.out.println("Selected file: " + path.toString());
                    List<String> lines = Collections.emptyList();
                    try {
                        lines = Files.readAllLines(path, StandardCharsets.UTF_8);
                        for (String line : lines) {
                            if (line.contains("--") || line.contains("->")) {
                                String[] currentLine = {""};
                                if (line.contains("--")) {
                                    dir = 0;
                                    grafo.dirigido = 0;
                                    currentLine = line.replace("\"", "").trim().split("--");
                                }
                                if (line.contains("->")) {
                                    dir = 1;
                                    grafo.dirigido = 1;
                                    currentLine = line.replace("\"", "").trim().split("->");
                                }
                                String[] nodeL = currentLine[0].split(",| ");
                                String[] nodeR = currentLine[1].split(",| ");
                                int izq = Integer.parseInt(nodeL[0]);
                                //if (nodes < izq) {
                                //    nodes = izq;
                                //}
                                int der = Integer.parseInt(nodeR[0]);
                                //if (nodes < der) {
                                //    nodes = der;
                                //}
                                nodesFromFile.add(izq);
                                nodesFromFile.add(der);
                            }
                        }
                        //createNodes(nodes + 1);
                        createNodesFromFile(nodesFromFile);
                        grafo.reachableNodes = nodesFromFile.size();
                        for (String line : lines) {
                            if(line.contains("--") || line.contains("->")) {
                                String[] currentLine = {""};
                                if (line.contains("--")) {
                                    dir = 0;
                                    grafo.dirigido = 0;
                                    currentLine = line.replace("\"", "").trim().split("--");
                                }
                                if (line.contains("->")) {
                                    dir = 1;
                                    grafo.dirigido = 1;
                                    currentLine = line.replace("\"", "").trim().split("->");
                                }
                                String[] nodeL = currentLine[0].split(",| ");
                                String[] nodeR = currentLine[1].split(",| ");
                                int izq = Integer.parseInt(nodeL[0]);
                                int der = Integer.parseInt(nodeR[0]);
                                createEdge(edges, grafo.Nodes.get(izq), grafo.Nodes.get(der), "Connects node " + izq + " and node " + der, dir);
                                edges++;
                            }
                        }
                        for(String line : lines) {
                            if(line.contains("label")) {
                                String[] currentLine = {""};
                                if(line.contains("--")) {
                                    dir = 0;
                                    currentLine = line.replace("\"", "").trim().split("--|=");
                                }
                                if(line.contains("->")) {
                                    dir = 1;
                                    currentLine = line.replace("\"", "").trim().split("->|=");
                                }
                                String[] nodeL = currentLine[0].split(",| ");
                                int izq = Integer.parseInt(nodeL[0]);
                                String[] nodeR = currentLine[1].split(",| ");
                                int der = Integer.parseInt(nodeR[0]);
                                String[] weight = currentLine[2].trim().split(",| |\\[|\\]");
                                double weigh = Double.parseDouble(weight[0]);
                                Edge edge = new Edge(grafo.Nodes.get(izq), grafo.Nodes.get(der), "Connects node " + izq + " and node " + der, dir);
                                Set set = grafo.Edges.entrySet();
                                Iterator it = set.iterator();
                                while(it.hasNext()) {
                                    Map.Entry mentry = (Map.Entry)it.next();
                                    Edge edgeCompara = (Edge)mentry.getValue();
                                    if(edge.equals(edgeCompara)) {
                                        edgeCompara.setWeight(weigh);
                                    }
                                }
                            }
                        }
                        //System.out.println(Edges.size() +":" + Nodes.size());
                    } catch(IOException e) {
                        JOptionPane.showMessageDialog(null, "The file could not be loaded.", "File not loaded", JOptionPane.INFORMATION_MESSAGE);
                        return null;
                    }
                    return null;
                }
                @Override
                protected void done() {
                    dialog.dispose();
                }
            };
            worker.execute();
            dialog.setVisible(true);
            return 1;
        }
        return 0;
    }

    public void resetNodes(Graph grafo) {
        Set set = grafo.getNodesGraph().entrySet();
        Iterator it = set.iterator();
        while(it.hasNext()) {
            Map.Entry mentry = (Map.Entry)it.next();
            Node node = (Node)mentry.getValue();
            node.visited = false;
        }
    }

    public void resetTree(Graph grafo) {
        grafo.treeEdges.clear();
    }

    public void BFS(Graph grafo, Node root) {
        Deque<Node> myQ = new LinkedList<>();
        myQ.add(root);
        int adding = 0;
        while(!myQ.isEmpty()) {
            Node current = myQ.pollFirst();
            current.visited = true;
            HashMap<Integer, Node> neighbors = current.getAdjacentNodes();
            Set set = neighbors.entrySet();
            Iterator it = set.iterator();
            while(it.hasNext()) {
                Map.Entry mentry = (Map.Entry)it.next();
                Node neighbor = (Node)mentry.getValue();
                if (!neighbor.visited) {
                    neighbor.visited = true;
                    myQ.addLast(neighbor);
                    createTreeEdge(adding, current, neighbor, "");
                    adding++;
                }
            }
        }
    }

    public int DFS_R(Graph grafo, Node root) {
        HashMap<Integer, Node> neighbors = root.getAdjacentNodes();
        root.visited = true;
        Set set = neighbors.entrySet();
        Iterator it = set.iterator();
        int id = -1;
        Node neighbor = new Node();
        while (it.hasNext()) {
            Map.Entry mentry = (Map.Entry)it.next();
            neighbor = (Node)mentry.getValue();
            if (neighbor != null && !neighbor.visited) {
                id = DFS_R(grafo, neighbor);
                if (id != root.getId()) {
                    createTreeEdge(id, root, grafo.getNodesGraph().get(id), "");
                }
            }
        }
        return root.getId();
    }

    public void DFS_I(Graph grafo, Node root){
        Stack<Node> stack = new Stack<>();
        Deque<Node> nodosIndex = new LinkedList<>();
        stack.add(root);
        root.visited = true;
        int adding = 0;
        while (!stack.isEmpty()) {
            Node current = stack.pop();
            nodosIndex.add(current);
            HashMap<Integer, Node> neighbors = current.getAdjacentNodes();
            Set set = neighbors.entrySet();
            Iterator it = set.iterator();
            while (it.hasNext()) {
                Map.Entry mentry = (Map.Entry)it.next();
                Node neighbor = (Node)mentry.getValue();
                if (neighbor != null && !neighbor.visited) {
                    stack.add(neighbor);
                    neighbor.visited = true;
                }
            }
        }
        Deque<Node> reverseDeque = new LinkedList<>();
        while (!nodosIndex.isEmpty()) {
            Node current = nodosIndex.poll();
            //reverseDeque.add(current);
            reverseDeque.addFirst(current);
            if (!nodosIndex.isEmpty()){
                if (current.adjacentNodes.containsValue(nodosIndex.peek())) {
                    System.out.println(current.getId()+"->"+nodosIndex.peek().getId());
                    createTreeEdge(adding, current, nodosIndex.peek(), "");
                    adding++;
                } else {
                    Iterator it = reverseDeque.iterator();
                    it.next();
                    while (it.hasNext()) {
                        Node previousNode = (Node)it.next();
                        if (previousNode.adjacentNodes.containsValue(nodosIndex.peek())) {
                            System.out.println(previousNode.getId()+"->"+nodosIndex.peek().getId());
                            createTreeEdge(adding, previousNode, nodosIndex.peek(), "");
                            adding++;
                            break;
                        }
                    }
                }
            }
        }
    }

    public void setEdgeWeights(double min, double max, boolean onlyInteger) {
        Set set = Edges.entrySet();
        Iterator it = set.iterator();
        while(it.hasNext()) {
            Map.Entry mentry = (Map.Entry)it.next();
            Edge edge = (Edge)mentry.getValue();
            if(onlyInteger) {
                edge.weight = min + (int)(Math.random() * ((max - min) + 1));
            } else {
                edge.weight = min + (Math.random() * ((max - min) + 1));
            }
        }
        graphWeightedGraph();
    }

    public int graphWeightedGraph() {
        //System.out.println("The graph will be saved in: C:\\temp\\"+name+".gv");
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String name = fc.getCurrentDirectory().toString() + "\\" + fc.getSelectedFile().getName();
            try (FileWriter fw = new FileWriter(name+".gv");
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw)) {
                if (dirigido == 0) { // Not directed graph
                    out.println("strict graph{");
                    out.flush();
                } else { // Directed graph
                    out.println("strict digraph{");
                    out.flush();
                }

            Set setE = Edges.entrySet();
            Iterator it = setE.iterator();
            while(it.hasNext()) {
                Map.Entry mentry = (Map.Entry)it.next();
                Edge edge = (Edge)mentry.getValue();
                HashMap<Integer, Node> anode = edge.getNodes();
                Node A = anode.get(1);
                Node B = anode.get(2);
                if (dirigido == 0) { // Not directed graph
                    out.println("   \"" + A.getId() + "\"--\"" + B.getId() + "\" [label = \"" + edge.getWeight() + "\"]");
                    out.flush();
                } else { // Directed graph
                    out.println("   \"" + A.getId() + "\"->\"" + B.getId() + "\" [label = \"" + edge.getWeight() + "\"]");
                    out.flush();
                }
            }
            out.println("}");
            out.close();
            JOptionPane.showMessageDialog(null, "The graph will be saved in: " + name + ".gv", "Graph Created", JOptionPane.INFORMATION_MESSAGE);
            return 1;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "The graph couldn't be saved in: " + name + ".gv", "Graph Not Created", JOptionPane.INFORMATION_MESSAGE);
                return 0;
            }
        } else {
            JOptionPane.showMessageDialog(null, "The graph was not saved.", "Graph Not Created", JOptionPane.INFORMATION_MESSAGE);
        }
        return 1;
    }

    public void Dijkstra(Graph grafo, Node s) {
        int key = -1;
        Edge edge = new Edge();
        Edge edgeO = new Edge();
        Set<Integer> settled = new HashSet<>();
        PriorityQueue<Node> pq = new PriorityQueue<>();
        // Set source node dist as 0
        // HashMap for keeping track of predecessors
        HashMap<Integer, Integer> prev = new HashMap<>();
        prev.put(s.getId(), Integer.MIN_VALUE);
        // HashMap for keeping track of distances
        HashMap<Integer, Double> dist = new HashMap<>();
        dist.put(s.getId(), 0.0);
        Set set = Nodes.entrySet();
        Iterator it = set.iterator();
        while(it.hasNext()) {
            Map.Entry mentry = (Map.Entry)it.next();
            Node nodo = (Node)mentry.getValue();
            // Set nodes not source cost as infinite
            if (nodo.getId() != s.getId()) {
                dist.put(nodo.getId(), Double.MAX_VALUE);
            }
        }
        pq.add(s);
        //while(settled.size() != Nodes.size()) { //No jala esta condición (???)
        while(!pq.isEmpty()) {
            Node u = pq.remove();
            settled.add(u.getId());
            double edgeDistance = -1.0;
            double newDistance = -1.0;
            Set set1 = u.adjacentNodes.entrySet();
            //set1.removeAll(pq);
            Iterator ite = set1.iterator();
            while(ite.hasNext()) {
                Map.Entry mentry = (Map.Entry)ite.next();
                Node v = (Node)mentry.getValue();
                //System.out.println(prev.containsKey(v.getId()));
                //System.out.print(v.getId());
                if(!settled.contains(v.getId())) {
                    edge = new Edge(u, v, "Connects node " + u.getId() + " and node " + v.getId());
                    edgeO = new Edge(v, u, "Connects node " + v.getId() + " and node " + u.getId());
                    Set setedge = Edges.entrySet();
                    Iterator iteratorEdge = setedge.iterator();
                    while(iteratorEdge.hasNext()) {
                        Map.Entry mentryEdge = (Map.Entry)iteratorEdge.next();
                        Edge edgeCompara = (Edge)mentryEdge.getValue();
                        if (edge.equals(edgeCompara)) {
                            key = (int)mentryEdge.getKey();
                            edgeDistance = edgeCompara.getWeight();
                            break;
                        }
                        if (edgeO.equals(edgeCompara)) {
                            key = (int)mentryEdge.getKey();
                            edgeDistance = edgeCompara.getWeight();
                            break;
                        }
                    }
                    //edgeDistance = v.getCost();
                    newDistance = dist.get(u.getId()) + edgeDistance;
                    if(newDistance <= dist.get(v.getId())) {
                        dist.put(v.getId(), newDistance);
                        v.setCost(newDistance);
                        prev.put(v.getId(), u.getId());
                    }
                    pq.add(v);
                }
            }
        }
        graphDijkstra(grafo, "Dijkstra", prev, dist);
        /*Set se = Edges.entrySet();
        Iterator i = se.iterator();
        while(i.hasNext()) {
            Map.Entry mentr = (Map.Entry)i.next();
            Edge e = (Edge)mentr.getValue();
            System.out.println("Edge: "+e.a.getId()+" -> "+e.b.getId() + "eith weight: "+e.getWeight());
        }
        Set sett = dist.entrySet();
        Iterator itt = sett.iterator();
        while(itt.hasNext()) {
            Map.Entry mentry = (Map.Entry)itt.next();
            System.out.println("Nodo: " + mentry.getKey() + " Distance: " + mentry.getValue());
        }
        Set zet = prev.entrySet();
        Iterator j = zet.iterator();
        while(j.hasNext()) {
            Map.Entry entry = (Map.Entry)j.next();
            int a = (int)entry.getKey();
            int b = (int)entry.getValue();
            System.out.println("Nodo: " + a +" -> "+b);
        }*/
    }

    public int graphDijkstra(Graph grafo, String name, HashMap<Integer, Integer> prev, HashMap<Integer, Double> dist) {
        //System.out.println("The graph will be saved in: C:\\temp\\"+name+".gv");
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(name));
        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            name = fc.getCurrentDirectory().toString() + "\\" + fc.getSelectedFile().getName();
            HashMap<Integer, Edge> EG = grafo.getEdgesGraph();
            try (FileWriter fw = new FileWriter(name+".gv");
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw)) {
                if (dirigido == 0) { // Not directed graph
                    out.println("strict graph{");
                    out.flush();
                } else { // Directed graph
                    out.println("strict digraph{");
                    out.flush();
                }
            Set setPrev = prev.entrySet();
            Iterator itPrev = setPrev.iterator();
            while(itPrev.hasNext()) {
                Map.Entry mentry = (Map.Entry)itPrev.next();
                int hijo = (int)mentry.getKey();
                int padre = (int)mentry.getValue();
                if (Integer.MIN_VALUE == padre) {
                    continue;
                }
                Edge edge = new Edge(Nodes.get(padre), Nodes.get(hijo), "Connects node " + padre + " and node " + hijo);
                Edge edgeO = new Edge(Nodes.get(hijo), Nodes.get(padre), "Connects node " + hijo + " and node " + padre);
                //System.out.println(padre+":"+hijo);
                double edgeWeight = 0.0;
                Set setE = EG.entrySet();
                Iterator it = setE.iterator();
                while(it.hasNext()) {
                    Map.Entry entryEdge = (Map.Entry)it.next();
                    Edge enGrafo = (Edge)entryEdge.getValue();
                    if(edge.equals(enGrafo)) {
                        edgeWeight = enGrafo.getWeight();
                        break;
                    }
                    if(edgeO.equals(enGrafo)) {
                        edgeWeight = enGrafo.getWeight();
                        break;
                    }
                }
                if (dirigido == 0) { // Not directed graph
                    out.println("   \"" + padre + ",(" + dist.get(padre) + ")\"--\"" + hijo + ",(" + dist.get(hijo) + ")\" [label = \"" + edgeWeight + "\"]");
                    out.flush();
                } else { // Directed graph
                    out.println("   \"" + padre + ",(" + dist.get(padre) + ")\"->\"" + hijo + ",(" + dist.get(hijo) + ")\" [label = \"" + edgeWeight + "\"]");
                    out.flush();
                }
            }
            out.println("}");
            out.close();
            JOptionPane.showMessageDialog(null, "The calculated graph will be saved in: " + name + ".gv", "Graph Created", JOptionPane.INFORMATION_MESSAGE);
            return 1;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "The calculated graph couldn't be saved in: " + name + ".gv", "Graph Not Created", JOptionPane.INFORMATION_MESSAGE);
                return 0;
            }
        } else {
            JOptionPane.showMessageDialog(null, "The calculated graph was not saved.", "Graph Not Created", JOptionPane.INFORMATION_MESSAGE);
        }
        return 1;
    }
    
    public int graphMST(Graph grafo, String name, HashMap<Integer, Edge> mst) {
        //System.out.println("The graph will be saved in: C:\\temp\\"+name+".gv");
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(name));
        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            name = fc.getCurrentDirectory().toString() + "\\" + fc.getSelectedFile().getName();
            try (FileWriter fw = new FileWriter(name+".gv");
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter out = new PrintWriter(bw)) {
                if (dirigido == 0) { // Not directed graph
                    out.println("strict graph{");
                    out.flush();
                } else { // Directed graph
                    out.println("strict digraph{");
                    out.flush();
                }
            Set setPrev = mst.entrySet();
            Iterator itPrev = setPrev.iterator();
            double weight = 0.0;
            while(itPrev.hasNext()) {
                Map.Entry mentry = (Map.Entry)itPrev.next();
                Edge edge = (Edge)mentry.getValue();
                if (dirigido == 0) { // Not directed graph
                    weight += edge.getWeight();
                    out.println("   \"" + edge.a.getId() + "\"--\"" + edge.b.getId() + "\" [label = \"" + edge.getWeight() + "\"]");
                    out.flush();
                } else { // Directed graph
                    weight += edge.getWeight();
                    out.println("   \"" + edge.a.getId() + "\"->\"" + edge.b.getId() + "\" [label = \"" + edge.getWeight() + "\"]");
                    out.flush();
                }
            }
            out.println("labelloc=\"t\"\nlabel=\"MST's weight="+weight+"\"");
            out.println("}");
            out.close();
            JOptionPane.showMessageDialog(null, "The calculated graph will be saved in: " + name + ".gv", "Graph Created", JOptionPane.INFORMATION_MESSAGE);
            return 1;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "The calculated graph couldn't be saved in: " + name + ".gv", "Graph Not Created", JOptionPane.INFORMATION_MESSAGE);
                return 0;
            }
        } else {
            JOptionPane.showMessageDialog(null, "The calculated graph was not saved.", "Graph Not Created", JOptionPane.INFORMATION_MESSAGE);
        }
        return 1;
    }
    
    public void kruskal(Graph grafo) {
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        HashMap<Integer, Integer> prev = new HashMap<>();
        HashMap<Integer, Edge> mst = new HashMap<>();
        Set set = Edges.entrySet();
        Iterator it = set.iterator();
        // Add all edges to priority queue
        while (it.hasNext()) {
            Map.Entry mentry = (Map.Entry)it.next();
            Edge edge = (Edge)mentry.getValue();
            pq.add(edge);
        }
        // Create list of parents for each node. Initially each nodes is its parent
        Set nodeSet = Nodes.entrySet();
        Iterator nodesIterator = nodeSet.iterator();
        while(nodesIterator.hasNext()) {
            Map.Entry mentry = (Map.Entry)nodesIterator.next();
            Node node = (Node)mentry.getValue();
            prev.put(node.getId(), node.getId());
        }
        // Process nodes-1 edges
        int index = 0;
        //while(index < grafo.reachableNodes - 1) {
        while(!pq.isEmpty()) {
            Edge edge = pq.poll();
            // Check if adding this edge creates a cycle
            int first = find(prev, edge.a.getId());
            int second = find(prev, edge.b.getId());
            if (first == second) {
                // We ignore since it would create a cycle
                System.out.println("No avanzo weeee" + grafo.reachableNodes+" : "+mst.size());
                Set zet = mst.entrySet();
                Iterator ite = zet.iterator();
                while(ite.hasNext()) {
                    Map.Entry menty = (Map.Entry)ite.next();
                    Edge edje = (Edge)menty.getValue();
                    System.out.println(edje.toString());
                }
            } else {
                // Add it to our final result
                mst.put(index, edge);
                index++;
                union(prev, first, second);
            }
        }
        // Print MST
        this.graphMST(grafo, "MST using Kruskal", mst);
        /*System.out.println("Minimum Spanning Tree: "+mst.size());
        Set zet = mst.entrySet();
        Iterator ite = zet.iterator();
        while(ite.hasNext()) {
            Map.Entry menty = (Map.Entry)ite.next();
            Edge edje = (Edge)menty.getValue();
            System.out.println(edje.toString());
        }*/
    }
    
    // Auxiliar functions for Kruskal
    public int find(HashMap<Integer, Integer> prev, int idNode) {
        // Chain of parent pointers from x upwards through the tree
        // until an element is reached whose parent is itself
        if(prev.get(idNode) != idNode) {
            System.out.println("Stuck_find");
            return find(prev, prev.get(idNode));
        }
        return idNode;
    }
    
    // Auxiliar function for Kruskal
    public void union(HashMap<Integer, Integer> prev, int x, int y) {
        int x_parent = find(prev, x);
        int y_parent = find(prev, y);
        // Make x as parent of y
        System.out.println("Stuck_Union");
        prev.put(y_parent, x_parent);
    }
    
    public boolean inverseKruskal(Graph grafo, Node s) {
        grafo.DFS_R(grafo, s);
        if(!grafo.checkAllVisited()){
            JOptionPane.showMessageDialog(null, "This method does not support not connected graphs.", "Error.", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        PriorityQueue<Edge> pq = new PriorityQueue<>(Collections.reverseOrder());
        HashMap<Integer, Edge> mst = new HashMap<>();
        Set set = Edges.entrySet();
        Iterator it = set.iterator();
        // Add all edges to priority queue
        while (it.hasNext()) {
            Map.Entry mentry = (Map.Entry)it.next();
            Edge edge = (Edge)mentry.getValue();
            pq.add(edge);
        }
        int index = 0;
        while(!pq.isEmpty()) {
            grafo.resetNodes(grafo);
            Edge edge = pq.poll();
            Node a = edge.a;
            Node b = edge.b;
            b.adjacentNodes.remove(a.getId());
            a.adjacentNodes.remove(b.getId());
            // Check if removing the edge disconnects the graph
            grafo.DFS_R(grafo, a);
            boolean test = checkAllVisited();
            if (test) {
            } else {
                // Add edge to our final result
                mst.put(index, edge);
                index++;
                b.adjacentNodes.put(a.getId(), a);
                a.adjacentNodes.put(b.getId(), b);
            }
        }
        // Print MST
        this.graphMST(grafo, "MST using Reverse Kruskal", mst);
        return true;
    }
    
    public boolean checkAllVisited() {
        Set set = Nodes.entrySet();
        Iterator ite = set.iterator();
        while(ite.hasNext()) {
            Map.Entry mentry = (Map.Entry)ite.next();
            Node node = (Node)mentry.getValue();
            if(node.visited) {
            } else {
                return false;
            }
        }
        this.resetNodes(this);
        return true;
    }
    
    public boolean prim(Graph grafo, Node s) {
        grafo.DFS_R(grafo, s);
        if(!grafo.checkAllVisited()){
            JOptionPane.showMessageDialog(null, "This method does not support not connected graphs.", "Error.", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        HashMap<Integer, Edge> mst = new HashMap<>();
        int index = 0;
        Edge edge = new Edge();
        Edge edgeO = new Edge();
        Set<Integer> settled = new HashSet<>();
        PriorityQueue<Node> pq = new PriorityQueue<>();
        // Set source node dist as 0
        // HashMap for keeping track of predecessors
        HashMap<Integer, Integer> prev = new HashMap<>();
        prev.put(s.getId(), Integer.MIN_VALUE);
        // HashMap for keeping track of distances
        HashMap<Integer, Double> dist = new HashMap<>();
        dist.put(s.getId(), 0.0);
        Set set = Nodes.entrySet();
        Iterator it = set.iterator();
        while(it.hasNext()) {
            Map.Entry mentry = (Map.Entry)it.next();
            Node nodo = (Node)mentry.getValue();
            if (nodo.getId() != s.getId()) {
                dist.put(nodo.getId(), Double.MAX_VALUE);
            }
        }
        pq.add(s);
        while(!pq.isEmpty()) {
            Node u = pq.remove();
            settled.add(u.getId());
            double edgeDistance = -1.0;
            Set set1 = u.adjacentNodes.entrySet();
            Iterator ite = set1.iterator();
            while(ite.hasNext()) {
                Edge edgeCompara = new Edge();
                Map.Entry mentry = (Map.Entry)ite.next();
                Node v = (Node)mentry.getValue();
                if(!settled.contains(v.getId())) {
                    edge = new Edge(u, v, "Connects node " + u.getId() + " and node " + v.getId());
                    edgeO = new Edge(v, u, "Connects node " + v.getId() + " and node " + u.getId());
                    Set setedge = Edges.entrySet();
                    Iterator iteratorEdge = setedge.iterator();
                    while(iteratorEdge.hasNext()) {
                        Map.Entry mentryEdge = (Map.Entry)iteratorEdge.next();
                        edgeCompara = (Edge)mentryEdge.getValue();
                        if (edge.equals(edgeCompara) || edgeO.equals(edgeCompara)) {
                            edgeDistance = edgeCompara.getWeight();
                            break;
                        }
                    }
                    if(edgeDistance < dist.get(v.getId())) {
                        dist.put(v.getId(), edgeDistance);
                        v.setCost(edgeDistance);
                        prev.put(v.getId(), u.getId());
                        mst.put(index, edgeCompara);
                        index++;
                    }
                    pq.add(v);
                }
            }
        }
        this.graphMST(grafo, "MST using Prim's Algorithm", mst);
        return true;
    }
    
    double[] originalWAVBuffer = null;
    Complex[] original = null;
    Complex[] transformed = null;
    Complex[] inversed = null;
    double[] realTransformed = null;
    boolean running = true;
    double phase = 0;
    double frames = 0;
    double rate = 0;
    int index = 0;
    double minWAV = Double.MAX_VALUE;
    double maxWAV = Double.MIN_VALUE;
    double max = Double.MIN_VALUE;
    Clip clip = null;
    public void FFT(Path path) throws IOException, WavFileException {
        originalWAVBuffer = null;
        running = true;
        original = null;
        transformed = null;
        inversed = null;
        realTransformed = null;
        phase = 0;
        frames = 0;
        rate = 0;
        index = 0;
        minWAV = Double.MAX_VALUE;
        maxWAV = Double.MIN_VALUE;
        /////////////////////// Loading WAV file ///////////////////////////
        /////////////////////// First thing to do ///////////////////////////
        try {
            wavFile = WavFile.openWavFile(new File(path.toString()));
            wavFile.display();
            frames = wavFile.getNumFrames();
            rate = wavFile.getSampleRate();
        } catch (IOException | WavFileException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error.", JOptionPane.ERROR_MESSAGE);
        }
        //////////////////////////////////////////////////////
        
        ///////////////////////To show a progress bar while reading wav and fft and ifft//////////////////
        SwingWorker<Void, Void> worker;
        JFrame frame = new JFrame();
        final JDialog dialog = new JDialog(frame, true);
        dialog.setUndecorated(true);
        dialog.setLocationRelativeTo(null);
        Dimension SS = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation((int)(SS.getWidth() - dialog.getWidth()) / 2, (int)((SS.getHeight() - dialog.getHeight()) / 2));
        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);
        bar.setStringPainted(true);
        bar.setBackground(Color.green);
        bar.setString("Loading file and making magic. Please wait...");
        dialog.add(bar);
        dialog.pack();
        worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    getWavData(wavFile);
                } catch (IOException|WavFileException ex) {
                }
                return null;
            }
            @Override
            protected void done() {
                dialog.dispose();
                frame.dispose();
            }
        };
        worker.execute();
        dialog.setVisible(true);
        frame.setVisible(false);
        //getWavData(wavFile);
        double[][] initdata = getWavDataByChunks((int)rate);
        double[][] initdata2 = {{0.0}, {0.0}};
        double[][] initdata3 = getInverseDataByChunks((int)rate);
 
        // Create Charts
        final XYChart chart = QuickChart.getChart("Original Data", "Time", "Wav", "wav", initdata[0], initdata[1]);
        
        final XYChart chart2 = QuickChart.getChart("FFT", "Frequency", "Magnitude", "fft", initdata2[0], initdata2[1]);
        
        final XYChart chart3 = QuickChart.getChart("Recovered Data", "Time", "Recovered Wav", "ifft wav", initdata3[0], initdata3[1]);
        
        chart.getStyler().setYAxisMax(maxWAV);
        chart.getStyler().setYAxisMin(minWAV);
        chart2.getStyler().setSeriesColors(new Color[] {Color.RED});
        chart2.getStyler().setYAxisMax(max);
        chart3.getStyler().setYAxisMax(maxWAV);
        chart3.getStyler().setYAxisMin(minWAV);
        
        //////////////////////////////// Constructing GUI ///////////////////////////
        JFrame f = new JFrame("Fast Fourier Transform");
        JPanel panel = new XChartPanel<>(chart);
        JPanel panel2 = new XChartPanel<>(chart2);
        JPanel panel3 = new XChartPanel<>(chart3);
        JPanel pB = new JPanel();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(panel);
        mainPanel.add(panel2);
        mainPanel.add(panel3);
        mainPanel.add(pB);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //JButton JBFFT = new JButton("Perform FFT.");
        //pB.add(JBFFT);
        JButton JBSaveWAV = new JButton("Save generated .wav.");
        pB.add(JBSaveWAV);
        f.setSize(1000, 150 * mainPanel.getComponentCount());
        f.add(mainPanel);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        f.setLocation((int)((screenSize.getWidth() - f.getWidth())/2), (int)((screenSize.getHeight() - f.getHeight())/2));
        f.setVisible(true);
        JBSaveWAV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String name = "";
                    JFileChooser fc = new JFileChooser();
                    fc.setSelectedFile(new File("retrieved_sample"));
                    if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        name = fc.getCurrentDirectory().toString() + "\\" + fc.getSelectedFile().getName();
                        int sampleRate = (int)rate;    // Samples per second
                        double duration = frames/sampleRate;     // Seconds

                        // Calculate the number of frames required for specified duration
                        long numFrames = (long)(duration * sampleRate);
                        // Create a wav file with the name specified as the first argument
                        WavFile wavFile = WavFile.newWavFile(new File(name+".wav"), 1, numFrames, 16, sampleRate);

                        // Create a buffer of 100 frames
                        double[][] buffer = new double[1][(int)numFrames];

                        // Fill the buffer, one tone per channel
                        for (int s=0 ; s<numFrames ; s++) {
                            buffer[0][s] = inversed[s].abs();
                        }
                        // Write the buffer
                        wavFile.writeFrames(buffer, (int)numFrames);
                        // Close the wavFile
                        wavFile.close();
                    } else {
                        JOptionPane.showMessageDialog(null, "The file will not be saved", "Not saved.", JOptionPane.INFORMATION_MESSAGE);
                    }
                }catch (IOException | WavFileException exc) {
                    JOptionPane.showMessageDialog(null, "The file could not be saved", "Error.", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        clip = null;
        try{
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(path.toString())));
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException exc){
            JOptionPane.showMessageDialog(null, "The file could not be played.", "Error.", JOptionPane.ERROR_MESSAGE);
        }
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                running = false;
                index = (int)frames + 10;
                clip.stop();
                try {
                    wavFile.close();
                } catch (IOException ex) {
                    //Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        //f.setVisible(true);
        /////////////////////////////////////////////////////////////////

        // Show it
        //final SwingWrapper<XYChart> sw = new SwingWrapper<XYChart>(chart4);
        //sw.displayChart();
        
        clip.start();

        while (index < frames) {
            try {
                Thread.sleep(900);
            } catch (InterruptedException ex) {
                //Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
            }
            //System.out.println("Refresco en index: "+index/(int)rate);
            final double[][] data = getWavDataByChunks((int)rate);
            final double[][] data2 = getRealIFFT(index, index + (int)rate);
            final double[][] data3 = getInverseDataByChunks((int)rate);
            
            chart.updateXYSeries("wav", data[0], data[1], null);
            chart2.updateXYSeries("fft", data2[0], data2[1], null);
            max = Arrays.stream(data2[1]).max().getAsDouble();
            chart2.getStyler().setYAxisMax(max);
            chart3.updateXYSeries("ifft wav", data3[0], data3[1], null);
            
            
            //sw.repaintChart();
            mainPanel.repaint();
            f.repaint();
            index += (int)rate;
        }
        wavFile.close();
    }
    
    public void FFTStatic(Path path) throws IOException, WavFileException {
        originalWAVBuffer = null;
        running = true;
        original = null;
        transformed = null;
        inversed = null;
        realTransformed = null;
        phase = 0;
        frames = 0;
        rate = 0;
        index = 0;
        minWAV = Double.MAX_VALUE;
        maxWAV = Double.MIN_VALUE;
        /////////////////////// Loading WAV file ///////////////////////////
        /////////////////////// First thing to do ///////////////////////////
        try {
            wavFile = WavFile.openWavFile(new File(path.toString()));
            wavFile.display();
            frames = wavFile.getNumFrames();
            rate = wavFile.getSampleRate();
        } catch (IOException | WavFileException ex) {
            JOptionPane.showMessageDialog(null, ex, "Error.", JOptionPane.ERROR_MESSAGE);
        }
        //////////////////////////////////////////////////////
        
        ///////////////////////To show a progress bar while reading wav and fft and ifft//////////////////
        SwingWorker<Void, Void> worker;
        JFrame frame = new JFrame();
        final JDialog dialog = new JDialog(frame, true);
        dialog.setUndecorated(true);
        dialog.setLocationRelativeTo(null);
        Dimension SS = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation((int)(SS.getWidth() - dialog.getWidth()) / 2, (int)((SS.getHeight() - dialog.getHeight()) / 2));
        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);
        bar.setStringPainted(true);
        bar.setBackground(Color.green);
        bar.setString("Loading file and making magic. Please wait...");
        dialog.add(bar);
        dialog.pack();
        worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                try {
                    getWavData(wavFile);
                    wavFile.close();
                } catch (IOException|WavFileException ex) {
                }
                return null;
            }
            @Override
            protected void done() {
                dialog.dispose();
                frame.dispose();
            }
        };
        worker.execute();
        dialog.setVisible(true);
        frame.setVisible(false);
        //getWavData(wavFile);
        double[][] initdata = getWavDataByChunks((int)frames);
        double[][] initdata2 = getRealIFFT(0, (int)frames);
        double[][] initdata3 = getInverseDataByChunks((int)frames);
 
        // Create Charts
        final XYChart chart = QuickChart.getChart("Original Data", "Time", "Wav", "wav", initdata[0], initdata[1]);
        
        final XYChart chart2 = QuickChart.getChart("FFT", "Frequency", "Magnitude", "fft", initdata2[0], initdata2[1]);
        
        final XYChart chart3 = QuickChart.getChart("Recovered Data", "Time", "Recovered Wav", "ifft wav", initdata3[0], initdata3[1]);
        
        chart.getStyler().setYAxisMax(maxWAV);
        chart.getStyler().setYAxisMin(minWAV);
        chart2.getStyler().setSeriesColors(new Color[] {Color.RED});
        chart2.getStyler().setYAxisMax(max);
        chart3.getStyler().setYAxisMax(maxWAV);
        chart3.getStyler().setYAxisMin(minWAV);
        
        //////////////////////////////// Constructing GUI ///////////////////////////
        JFrame f = new JFrame("Fast Fourier Transform");
        JPanel panel = new XChartPanel<>(chart);
        JPanel panel2 = new XChartPanel<>(chart2);
        JPanel panel3 = new XChartPanel<>(chart3);
        JPanel pB = new JPanel();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(panel);
        mainPanel.add(panel2);
        mainPanel.add(panel3);
        mainPanel.add(pB);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JButton JBSaveWAV = new JButton("Save generated .wav.");
        pB.add(JBSaveWAV);
        f.setSize(1000, 150 * mainPanel.getComponentCount());
        f.add(mainPanel);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        f.setLocation((int)((screenSize.getWidth() - f.getWidth())/2), (int)((screenSize.getHeight() - f.getHeight())/2));
        f.setVisible(true);
        System.out.println("Visible finished");
        JBSaveWAV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String name = "";
                    JFileChooser fc = new JFileChooser();
                    fc.setSelectedFile(new File("retrieved_sample"));
                    if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        name = fc.getCurrentDirectory().toString() + "\\" + fc.getSelectedFile().getName();
                        int sampleRate = (int)rate;    // Samples per second
                        double duration = frames/sampleRate;     // Seconds

                        // Calculate the number of frames required for specified duration
                        long numFrames = (long)(duration * sampleRate);
                        // Create a wav file with the name specified as the first argument
                        WavFile wavFile = WavFile.newWavFile(new File(name+".wav"), 1, numFrames, 16, sampleRate);

                        // Create a buffer of 100 frames
                        double[][] buffer = new double[1][(int)numFrames];

                        // Fill the buffer, one tone per channel
                        for (int s=0 ; s<numFrames ; s++) {
                            buffer[0][s] = inversed[s].abs();
                        }
                        // Write the buffer
                        wavFile.writeFrames(buffer, (int)numFrames);
                        // Close the wavFile
                        wavFile.close();
                    } else {
                        JOptionPane.showMessageDialog(null, "The file will not be saved", "Not saved.", JOptionPane.INFORMATION_MESSAGE);
                    }
                }catch (IOException | WavFileException exc) {
                    JOptionPane.showMessageDialog(null, "The file could not be saved", "Error.", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private double[][] getWavData(WavFile wavFile) throws IOException, WavFileException {
        int numChannels = wavFile.getNumChannels();
        double numFrames = wavFile.getNumFrames();
        double power = Math.log10(numFrames * numChannels) / Math.log10(2);
        power = (power % 1 != 0) ? Math.ceil(power) : power;
        int size = (int)Math.pow(2, power);
        originalWAVBuffer = new double[size];
        double[] xData = new double[size];
        original = new Complex[size];
        transformed = new Complex[size];
        inversed = new Complex[size];
        int framesRead;
        do {
            framesRead = wavFile.readFrames(originalWAVBuffer, (int)numFrames);
            for (int s= 0; s < size; s++) {
                xData[s] = s;
                original[s] = new Complex(originalWAVBuffer[s], 0.0);
            }
            /*for (int s = 0; s < framesRead * numChannels; s++) {
                if (originalWAVBuffer[s] > maxWAV) {
                    maxWAV = originalWAVBuffer[s];
                }
                if (originalWAVBuffer[s] < minWAV) {
                    minWAV = originalWAVBuffer[s];
                }
            }*/
        } while (framesRead != 0);
        maxWAV = Arrays.stream(originalWAVBuffer).max().getAsDouble();
        minWAV = Arrays.stream(originalWAVBuffer).min().getAsDouble();
        transformed = fft(original);
        realTransformed = IntStream.range(0, (int)frames).mapToDouble(i -> transformed[i].abs()).toArray();
        max = Arrays.stream(realTransformed).max().getAsDouble();
        inversed = ifft(transformed);
        //for (int i = 0; i < size; i++) {
        //    System.out.println(/*originalWAVBuffer[i]+" : "+*/transformed[i]/*+" : "+inversed[i].abs()*Math.signum(originalWAVBuffer[i])*/);
        //}
        return new double[][] { xData, originalWAVBuffer };
    }
    
    private double[][] getWavDataByChunks(int sizeOfChunk) {
        if(originalWAVBuffer.length < index + sizeOfChunk) {
            sizeOfChunk = originalWAVBuffer.length - index;
        }
        
        /*double[] xData = new double[sizeOfChunk];
        double[] yData = new double[sizeOfChunk];
        
        for (int i = 0; i < sizeOfChunk; i ++) {
            xData[i] = (i + index)/rate;
            yData[i] = originalWAVBuffer[index + i];
        }*/
        double[] xData = IntStream.range(0, sizeOfChunk).mapToDouble(i -> (i + index)/rate).toArray();
        double[] yData = IntStream.range(0, sizeOfChunk).mapToDouble(i -> originalWAVBuffer[index + i]).toArray();
        return new double[][] { xData, yData };
    }
    
    private double[][] getInverseDataByChunks(int sizeOfChunk) {
        if(originalWAVBuffer.length < index + sizeOfChunk) {
            sizeOfChunk = originalWAVBuffer.length - index;
        }
        
        double[] xData = IntStream.range(0, sizeOfChunk).mapToDouble(i -> (i + index)/rate).toArray();
        double[] yData = IntStream.range(0, sizeOfChunk).mapToDouble(i -> inversed[index + i].abs() * Math.signum(originalWAVBuffer[i])).toArray();
        return new double[][] { xData, yData };
    }
    
    private double[][] getRealIFFT(int init, int end) {
        if (frames < end) {
            end = (int)frames;
        }

        double[] xData = IntStream.range(0, end - init).map(a -> a).asDoubleStream().toArray();
        double[] yData = IntStream.range(0, end - init).mapToDouble(i -> realTransformed[i + init]).toArray();
        return new double[][] { xData, yData };
    }
    
    public Complex[] fft(Complex[] x) {
        int n = x.length;
        if (n == 1) {
            return new Complex[] {x[0]};
        }
        
        // Even terms
        int len = (n % 2 != 0) ? (n + 1) / 2 : n / 2;
        Complex[] even = new Complex[len];
        for (int k = 0; k < len; k++) {
            even[k] = x[2 * k];
        }
        Complex[] e = fft(even);
        
        // Odd terms
        len = (n % 2 != 0) ? (n - 1) / 2 : n / 2;
        Complex[] odd = new Complex[len];
        for (int k = 0; k < len; k++) {
            odd[k] = x[2 * k + 1];
        }
        Complex[] o = fft(odd);
        
        // Combine
        len = (n % 2 != 0) ? (n - 1) / 2 : n / 2;
        Complex[] y = new Complex[n];
        for (int k = 0; k < n/2; k ++) {
            double kth = -2 * k * Math.PI / n;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k] = e[k].plus(wk.times(o[k]));
            y[k + n/2] = e[k].minus(wk.times(o[k]));
        }
        
        return y;
    }
    
    public Complex[] ifft(Complex[] x) {
        int n = x.length;
        Complex[] y = new Complex[n];
        
        // Take conjugate
        for (int i = 0; i < n; i++) {
            y[i] = x[i].conjugate();
        }
        
        // Compute forward FFT
        y = fft(y);
        
        // Take conjugate again
        for (int i = 0; i < n; i++) {
            y[i] = y[i].conjugate();
        }
        
        // Divide by n
        for (int i = 0; i < n; i++) {
            y[i] = y[i].scale(1.0 / n);
        }
        return y;
    }
    
    double yMax = 100;
    double yMin = -100;
    double xMax = 100;
    double xMin = -100;
    double factorX = 20.0;
    double factorY = 16.0;
    ArrayList<Double> xData = new ArrayList<>();
    ArrayList<Double> yData = new ArrayList<>();
    HashMap<Double, Double> coXY = new HashMap<>();
    public void SLS() {
        double[] x = {0};
        double[] y = {0};
        xData.clear();
        yData.clear();
        coXY.clear();
        yMax = 100;
        yMin = -100;
        xMax = 100;
        xMin = -100;
        factorX = 20.0;
        factorY = 16.0;
        final XYChart chart1 = QuickChart.getChart("SLS", "X", "Y", "SLS", x, y);
        final XYChart chart = new XYChart(chart1.getWidth(), chart1.getHeight());
        chart.setTitle("SLS");
        chart.setXAxisTitle("X");
        chart.setYAxisTitle("Y");
        chart.addSeries("Data", x, y);
        chart.addSeries("LS", new double[] {0}, new double[] {0});
        chart.addSeries("SLS", new double[] {0}, new double[] {0});

        chart.getStyler().setMarkerSize(2);

        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        
        chart.getStyler().setYAxisMax(yMax);
        chart.getStyler().setYAxisMin(yMin);
        chart.getStyler().setXAxisMax(xMax);
        chart.getStyler().setXAxisMin(xMin);
        chart.getStyler().setMarkerSize(20);
        chart.getStyler().setChartBackgroundColor(Color.CYAN);
        chart.getStyler().setPlotMargin(0);
        chart.getStyler().setToolTipsEnabled(true);
        
        //chart.getStyler().setToolTipsAlwaysVisible(true);
        
        //////////////////////////////// Constructing GUI ///////////////////////////
        JFrame f = new JFrame("Segmented Least Square");
        JPanel panel = new XChartPanel<>(chart);
        JPanel pB = new JPanel();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(panel);
        mainPanel.add(pB);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JButton jbClear = new JButton("Clear points.");
        pB.add(jbClear);
        JButton jbZin = new JButton("Zoom in.");
        pB.add(jbZin);
        JButton jbZout = new JButton("Zoom out.");
        pB.add(jbZout);
        pB.add(new JLabel("Zoom percentage:"));
        JSpinner JSPercent = new JSpinner(new SpinnerNumberModel(0.1, 0.0, 1.0, 0.1));
        Component spinnerEditor = JSPercent.getEditor();
        JFormattedTextField jftf = ((JSpinner.DefaultEditor) spinnerEditor).getTextField();
        jftf.setColumns(2);
        pB.add(JSPercent);
        pB.add(new JLabel("C value:"));
        JSpinner JSC = new JSpinner(new SpinnerNumberModel(100.0, 0.05, 1000000.0, 100.0));
        pB.add(JSC);
        JButton jbSLS = new JButton("SLS.");
        pB.add(jbSLS);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        pB.setMaximumSize(new Dimension((int)screenSize.getWidth() - 100, 30));
        pB.setLayout(new GridLayout(1, pB.getComponentCount()));
        f.setSize((int)screenSize.getWidth() - 100, (int)screenSize.getHeight() - 100);
        panel.setSize(new Dimension(f.getWidth(), f.getHeight() - jbZin.getHeight()));
        f.add(mainPanel);
        f.setLocation((int)((screenSize.getWidth() - f.getWidth())/2), (int)((screenSize.getHeight() - f.getHeight())/2));
        f.setVisible(true);
        
        jbClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                coXY.clear();
                xData.clear();
                yData.clear();
                chart.removeSeries("Data");
                chart.removeSeries("LS");
                chart.removeSeries("SLS");
                chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
                chart.setTitle("SLS");
                chart.setXAxisTitle("X");
                chart.setYAxisTitle("Y");
                chart.addSeries("Data", new double[] {0}, new double[] {0});
                chart.addSeries("LS", new double[] {0}, new double[] {0});
                chart.addSeries("SLS", new double[] {0}, new double[] {0});
                chart.updateXYSeries("Data", new double[] {0}, new double[] {0}, null);
                chart.updateXYSeries("LS", new double[] {0}, new double[] {0}, null);
                chart.updateXYSeries("SLS", new double[] {0}, new double[] {0}, null);
                panel.repaint();
                f.repaint();
            }
        });
        
        jbZin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double per = 1.0 - (double)JSPercent.getValue();
                yMax = chart.getStyler().getYAxisMax() * per;
                yMin = chart.getStyler().getYAxisMin() * per;
                xMax = chart.getStyler().getXAxisMax() * per;
                xMin = chart.getStyler().getXAxisMin() * per;
                factorX = factorX * per;
                factorY = factorY * per;
                chart.getStyler().setYAxisMax(yMax);
                chart.getStyler().setYAxisMin(yMin);
                chart.getStyler().setXAxisMax(xMax);
                chart.getStyler().setXAxisMin(xMin);
                panel.repaint();
            }
        });
        
        jbZout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double per = 1.0 + (double)JSPercent.getValue();
                yMax = chart.getStyler().getYAxisMax() * per;
                yMin = chart.getStyler().getYAxisMin() * per;
                xMax = chart.getStyler().getXAxisMax() * per;
                xMin = chart.getStyler().getXAxisMin() * per;
                factorX = factorX * per;
                factorY = factorY * per;
                chart.getStyler().setYAxisMax(yMax);
                chart.getStyler().setYAxisMin(yMin);
                chart.getStyler().setXAxisMax(xMax);
                chart.getStyler().setXAxisMin(xMin);
                panel.repaint();
            }
        });
        
        panel.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                double xBorderL = 16.0;
                double xBorderC = (xMax < 100) ? 95.0 + 58.0 : 95.0 + 51.0;
                double yBorderU = 8.0;
                double yBorderC = 30.0 + 46.0;
                double x = (e.getX() - chart.getWidth() / 2 + xBorderL) * ((xMax * 2 + factorX) / (chart.getWidth() - xBorderC));
                double y = (chart.getHeight() / 2 - e.getY() - yBorderU) * ((yMax * 2 + factorY) / (chart.getHeight() - yBorderC));
                coXY.put(x, y);
                System.out.println(e.getY()+":"+chart.getHeight());
                //xData.add(x);
                //yData.add(y);
                
                xData.clear();
                yData.clear();
                // Orders the HashMap of coordinates
                Map<Double, Double> orderCoo = new TreeMap<>(coXY);
                xData.addAll(orderCoo.keySet());
                yData.addAll(orderCoo.values());
                
                chart.updateXYSeries("Data", xData, yData, null);
                chart.updateXYSeries("LS", new double[] {0}, new double[] {0}, null);
                chart.updateXYSeries("SLS", new double[] {0}, new double[] {0}, null);
                f.repaint();
                //System.out.println("^"+e.getX()+":"+e.getY());
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        
        jbSLS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(coXY.size() < 2) {
                    JOptionPane.showMessageDialog(null, "At least two points are needed.", "Error.", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                double c = (double)JSC.getValue();
                xData.clear();
                yData.clear();
                // Orders the HashMap of coordinates
                Map<Double, Double> orderCoo = new TreeMap<>(coXY);
                // Populates the ArrayList in order
                Set set = orderCoo.entrySet();
                Iterator it = set.iterator();
                Map.Entry mentry = (Map.Entry)it.next();
                double equis = (double)mentry.getKey();
                double ye = (double)mentry.getValue();
                xData.add(equis);
                yData.add(ye);
                    
                xData.addAll(orderCoo.keySet());
                yData.addAll(orderCoo.values());
                
                int n = xData.size();
                double[][] A = new double[xData.size()][yData.size()];
                double[][] B = new double[xData.size()][yData.size()];
                double[][] E = new double[xData.size()][yData.size()];
                // Populates the matrices for A, B, and Error
                for (int i = 1; i < n; i++) {
                    for (int j = i + 1; j < n; j++) {
                        // Copy the chunks required
                        double[] copyx = IntStream.range(i, j+1).mapToDouble(k -> xData.get(k)).toArray();
                        double[] copyy = IntStream.range(i, j+1).mapToDouble(k -> yData.get(k)).toArray();
                        ArrayList<Double> copyX = DoubleStream.of(copyx).boxed().collect(Collectors.toCollection(ArrayList::new));
                        ArrayList<Double> copyY = DoubleStream.of(copyy).boxed().collect(Collectors.toCollection(ArrayList::new));
                        Pair coefficients = Coef(copyX, copyY);
                        double error = ErrorXY(copyX, copyY, (double)coefficients.getKey(), (double)coefficients.getValue());
                        A[i][j] = (double)coefficients.getKey();
                        B[i][j] = (double)coefficients.getValue();
                        E[i][j] = error;
                    }
                }
                /*for (int i = 0; i < n ; i++) {
                    for (int j = 0; j < n ; j++) {
                        System.out.print(A[i][j]+" ");
                    }
                    System.out.println();
                }
                for (int i = 0; i < n ; i++) {
                    for (int j = 0; j < n ; j++) {
                        System.out.print(B[i][j]+" ");
                    }
                    System.out.println();
                }*/
                double[] OPT = new double[n];
                int[] retIndex = new int[n];
                OPT[0] = 0;
                retIndex[0] = 0;
                for (int j = 1; j < n; j++) {
                    OPT[j] = E[1][j] + c;
                    retIndex[j] = 1;
                    for (int i = 2; i <= j; i++) {
                        if(OPT[i-1] + E[i][j] + c < OPT[j]) {
                            OPT[j] = OPT[i - 1] + E[i][j] + c;
                            retIndex[j] = i + 1;
                        }
                    }
                }
                //System.out.println(Arrays.toString(OPT));
                //System.out.println(Arrays.toString(retIndex));
                
                Pair coe = Coef(xData, yData);
                //double error = ErrorXY(xData, yData, (double)coe.getKey(), (double)coe.getValue());
                double[] x_ls = {xData.get(0), xData.get(xData.size()-1)};
                double[] y_ls = {(double)coe.getKey()*xData.get(0) + (double)coe.getValue(), (double)coe.getKey()*xData.get(xData.size() - 1) + (double)coe.getValue()};
                ArrayList<Double> x_sls = new ArrayList<>();
                ArrayList<Double> y_sls = new ArrayList<>();
                HashMap<Double, Double> sls_co = new HashMap<>();
                
                int currInd = n - 1;
                while (currInd > 0) {
                    int nextInd = retIndex[currInd];
                    if (nextInd == currInd) {
                        //System.out.println("IF:     nextInd: "+nextInd+ ", currInd: "+currInd);
                        //System.out.println(xData.get(currInd - 1)+","+yData.get(currInd - 1));
                        //System.out.println(xData.get(currInd)+","+yData.get(currInd));
                        sls_co.put(xData.get(currInd - 1), yData.get(currInd - 1));
                        sls_co.put(xData.get(currInd), yData.get(currInd));
                    } else {
                        //System.out.println("ELSE:     nextInd: "+nextInd+ ", currInd: "+currInd);
                        //System.out.println("ELSE:     A: "+A[nextInd][currInd]+ ", B: "+B[nextInd][currInd]);
                        double x1 = xData.get(nextInd);
                        double y1 = x1 * A[nextInd][currInd] + B[nextInd][currInd];
                        double x2 = xData.get(currInd);
                        double y2 = x2 * A[nextInd][currInd] + B[nextInd][currInd];
                        //System.out.println(x1+","+y1);
                        //System.out.println(x2+","+y2);
                        sls_co.put(x1, y1);
                        sls_co.put(x2, y2);
                    }
                    currInd = nextInd - 1;
                    //System.out.println("AFTER:     nextInd: "+nextInd+ ", currInd: "+currInd);
                }
                
                Map<Double, Double> orderCooSLS = new TreeMap<>(sls_co);
                // Populates the ArrayList in order
                x_sls.addAll(orderCooSLS.keySet());
                y_sls.addAll(orderCooSLS.values());
                
                chart.removeSeries("Data");
                chart.removeSeries("LS");
                chart.removeSeries("SLS");
                chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
                chart.setTitle("SLS");
                chart.setXAxisTitle("X");
                chart.setYAxisTitle("Y");
                chart.addSeries("Data", xData, yData);
                chart.addSeries("LS", xData, yData);
                chart.addSeries("SLS", xData, yData);
                chart.updateXYSeries("Data", xData, yData, null);
                chart.updateXYSeries("LS", x_ls, y_ls, null);
                chart.updateXYSeries("SLS", x_sls, y_sls, null);
                chart.getStyler().setSeriesColors(new Color[] {Color.BLUE, Color.MAGENTA, Color.ORANGE});
                panel.repaint();
                f.repaint();
            }
        });
    }
    
    public Pair Coef(ArrayList<Double> x, ArrayList<Double> y) {
        if (x.size() != y.size()) {
            return new Pair(0.0, 0.0);
            //return 0.0;
        }
        int n = x.size();
        double sum_xy = IntStream.range(0, x.size()).mapToDouble(i -> x.get(i) * y.get(i)).sum();
        double sum_x = x.stream().mapToDouble(a -> a).sum();
        double sum_y = y.stream().mapToDouble(a -> a).sum();
        double sum_xx = x.stream().mapToDouble(a -> a*a).sum();
        double a = (n *  sum_xy - sum_x * sum_y)/(n * sum_xx - sum_x * sum_x);
        double b = (sum_y - a * sum_x)/n;
        return new Pair(a, b);
    }
    
    public double ErrorXY(ArrayList<Double> x, ArrayList<Double> y, double a, double b) {
        return IntStream.range(0, x.size()).mapToDouble(i -> Math.pow(y.get(i) - a * x.get(i) - b, 2)).sum();
    }
}