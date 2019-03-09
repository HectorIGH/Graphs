/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Hector Ivan Garcia-Hernandez
 */
public class Graph {
    
    HashMap<Integer, Node> Nodes = new HashMap<>();
    HashMap<Integer, Edge> Edges = new HashMap<>();
    
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
    
    public int createEdge(int id, Node a, Node b, String data) {
        Edge edge = new Edge(a, b, data);
        Edges.put(id, edge);
        a.updateGrad();
        b.updateGrad();
        a.setAdjacent(b);
        b.setAdjacent(a);
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
                createEdge(j, a, b, "Connects node " + a.getId()+" and node " + b.getId()); // Creates edge
                create = 1; // Reset condition
                //System.out.println("Estoy creando aristas.");
            }
            j++;
        }
        return new Graph();
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
                        createEdge(NE, a, b, "Connects node " + a.getId() +" and node " + b.getId());
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
                createEdge(NE, a, b, "Connects node " + a.getId() +" and node " + b.getId());
            }
        }
        //System.out.println(Edges.size());
        return new Graph();
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
                        createEdge(NE, a, b, "Connects node " + a.getId() +" and node " + b.getId());
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
                createEdge(NE, a, b, "Connects node " + a.getId() +" and node " + b.getId());
            }
        }
        //System.out.println(Edges.size());
        return new Graph();
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
                //pr = 1 - (double)Nodes.get(j).getGrad() / (double)(Edges.size() + g);
                //pr = 1 - (double)(Edges.size()) / (double)(Nodes.get(j).getGrad() + g + Edges.size());
                if (i < d) {
                    pr = 1;
                }
                //System.out.println(p + "," + pr);
                if (pr > p) {
                    Node A = Nodes.get(Nodes.size() - 1);
                    Node B = Nodes.get(j);
                    createEdge(Ne, A, B, "Connects node " + A.getId() + " and node " + B.getId());
                    Ne++;
                }
            }
        }
        return new Graph();
    }
    
    public HashMap<Integer, Node> getNodesGraph() {
        return Nodes;
    }
    
    public HashMap<Integer, Edge> getEdgesGraph() {
        return Edges;
    }
    
    public int graphGraph(Graph grapho, int dir, String name) {
        //System.out.println("The graph will be saved in: C:\\temp\\"+name+".gv");
        HashMap<Integer, Edge> EG = grapho.getEdgesGraph();
        try (FileWriter fw = new FileWriter("C:\\temp\\"+name+".gv");
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
        JOptionPane.showMessageDialog(null, "The graph will be saved in: C:\\temp\\"+name+".gv", "Graph Created", JOptionPane.INFORMATION_MESSAGE);
        return 1;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "The graph couldn't be saved in: C:\\temp\\"+name+".gv", "Graph Not Created", JOptionPane.INFORMATION_MESSAGE);
            return 0;
        }
    }
}
