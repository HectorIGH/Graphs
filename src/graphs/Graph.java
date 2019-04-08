/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**
 *
 * @author Hector Ivan Garcia-Hernandez
 */
public class Graph {
    
    HashMap<Integer, Node> Nodes = new HashMap<>();
    HashMap<Integer, Edge> Edges = new HashMap<>();
    HashMap<Integer, Edge> treeEdges = new HashMap<>();
    int loadedGraphDirected = 0;
    
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
        if (m > n*(n+1)/2) {
            return null;
        }
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
        return this;
    }
    
    public Graph Gilbert(int n, double p, int dir, int cic) {
        if (p > 1) {
            return null;
        }
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
        return this;
    }
    
    public Graph Geo(int n, double r, int dir, int cic) {
        if (r > 1) {
            return null;
        }
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
        return this;
    }
    
    public Graph Barabasi(int n, int d, double g, int dir, int cic) {
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
                    out.println("   \"" + A.getId() + "," + A.getGrad() + "\"--\"" + B.getId() + "," + B.getGrad() + "\"");
                    out.flush();
                } else { // Directed graph
                    out.println("   \"" + A.getId() + "," + A.getGrad() + "\"->\"" + B.getId() + "," + B.getGrad() + "\"");
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
    
    public Graph readGraph(Graph grafo) {
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
                                    currentLine = line.replace("\"", "").trim().split("--");
                                }
                                if (line.contains("->")) {
                                    dir = 1;
                                    currentLine = line.replace("\"", "").trim().split("->");
                                }
                                String[] nodeL = currentLine[0].split(",| ");
                                String[] nodeR = currentLine[1].split(",| ");
                                int izq = Integer.parseInt(nodeL[0]);
                                if (nodes < izq) {
                                    nodes = izq;
                                }
                                int der = Integer.parseInt(nodeR[0]);
                                if (nodes < der) {
                                    nodes = der;
                                }
                            }
                        }
                        createNodes(nodes + 1);
                        for (String line : lines) {
                            if(line.contains("--") || line.contains("->")) {
                                String[] currentLine = {""};
                                if (line.contains("--")) {
                                    dir = 0;
                                    currentLine = line.replace("\"", "").trim().split("--");
                                }
                                if (line.contains("->")) {
                                    dir = 1;
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
        }        
        return this;
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
    }
    
    public void Dijkstra(Graph grafo, Node origin) {
        // TO-DO Dijkstra's algorithm
    }
}
