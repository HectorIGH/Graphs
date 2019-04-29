/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import java.util.*;

/**
 *
 * @author Hector Ivan Garcia-Hernandez
 */
public class Edge implements Comparable<Edge> {
    
    Node a;
    Node b;
    String data;
    double weight = 1.0;
    
    public Edge() {
        // Constructor with no parameters
    }
    
    public Edge(Node a, Node b, String data) {
        this.a= a;
        this.b = b;
        this.data = data;
    }
    
    public Edge(Node a, Node b, String data, double weight) {
        this.a= a;
        this.b = b;
        this.data = data;
        this.weight = weight;
    }
    
    public void setNodes(Node A, Node B) {
        this.a = A;
        this.b = B;
    }
    
    public HashMap<Integer, Node> getNodes() {

        HashMap<Integer, Node> hm = new HashMap<>();
        hm.put(1, a);
        hm.put(2, b);
        return hm;
    }
    
    public void setData(String Data) {
        this.data = Data;
    }
    
    public String getData(){
        return data;
    }
    
    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    public double getWeight() {
        return weight;
    }
    
    // Overriding equals() to compare two Edge objects
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if(!(o instanceof Edge)) {
            return false;
        }
        Edge ed = (Edge)o;
        return Double.compare(a.getId(), ed.getNodes().get(1).getId()) == 0 && Double.compare(b.getId(), ed.getNodes().get(2).getId()) == 0;
    }

    @Override
    public int hashCode() {
        return (int)Double.hashCode(this.weight);
    }

    @Override
    public int compareTo(Edge edge) {
        if (this.getWeight() > edge.getWeight()) {
            return 1;
        } else if (this.getWeight() < edge.getWeight()) {
            return -1;
        } else {
            return 0;
        }
    }
    
    @Override
    public String toString() {
        return "Edge connects node " + this.a.getId() + " to node " + this.b.getId() + " and it has a weight of: " + this.getWeight() + "\n";
    }
}
