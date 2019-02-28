/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import java.util.*;
import java.io.*;

/**
 *
 * @author Hector Ivan Garcia-Hernandez
 */

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        //Variables to create graphs
        int n = 100, m = 10, dir = 0, cic = 0, d = 5;
        double p = 0.5, r = 0.5, g = 10;
        
        // Create object Graph
        Graph grafo = new Graph();
        // Create grafo using the Erdös' algorithm
        //grafo.Erdos(n, m, dir, cic);
        // Create grafo using the Gilbert's algorithm
        //grafo.Gilbert(n, p, dir, cic);
        // Create grafo using the Geographic algorithm
        //grafo.Geo(n, r, dir, cic);
        // Create grafo using the Garabasi's algorithm
        grafo.Barabasi(n, d, g, dir, cic);
        grafo.graphGraph(grafo, dir, "bar");
        
        
        
        //Node nodoA = new Node(12,"A","Nodo A");
        //Node nodoB = new Node(21,"B","Nodo B");
        //Edge ed = new Edge(nodoA, nodoB, "Une nodo A y B");
        //HashMap<Integer, Node> hm = ed.getNodes();
        /*HashMap<Integer, Node> NG = grafo.getNodesGraph(); // HAshMap to retrieve nodes
        HashMap<Integer, Edge> EG = grafo.getEdgesGraph(); // HashMap to retrieve edges
        Set set = NG.entrySet();
        Iterator i = set.iterator();
        while(i.hasNext()) {
            Map.Entry mentry = (Map.Entry)i.next();
            System.out.print("Key is: " + mentry.getKey() + " & Value is: ");
            Node a = (Node)mentry.getValue();
            System.out.println(a.getId()+ " " + a.getName() + " " + a.getData());
        }
        System.out.println("El número de aristas es: " + EG.size());
        try (FileWriter fw = new FileWriter("C:\\temp\\graph.gv");
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
            System.out.print("Key is: " + mentry.getKey() + " & connects: ");
            Edge edge = (Edge)mentry.getValue();
            HashMap<Integer, Node> anode = edge.getNodes();
            Node A = anode.get(1);
            Node B = anode.get(2);
            System.out.println("node " + A.getId() + " & node " + B.getId());
            if (dir == 0) { // Not directed graph
                out.println("   " + A.getId() + "--" + B.getId());
                out.flush();
            } else { // Directed graph
                out.println("   " + A.getId() + "->" + B.getId());
                out.flush();
            }
        }
        out.println("}");
        out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
    
}
