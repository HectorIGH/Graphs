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
    
    /**
     * Constructor with no parameters.
     */
    public Edge() {
        // Constructor with no parameters
    }
    
    /**
     * Constructor that sets both nodes of the edge and the data.
     * @param a Node 'a' as a Node object.
     * @param b Node 'b' as a Node object.
     * @param data The edge data as a string.
     */
    public Edge(Node a, Node b, String data) {
        this.a= a;
        this.b = b;
        this.data = data;
    }
    
    /**
     * Constructor that sets both nodes of the edge and the data.
     * @param a Node 'a' as a Node object.
     * @param b Node 'b' as a Node object.
     * @param data The edge data as a string.
     * @param weight The weight of the edge as a double.
     */
    public Edge(Node a, Node b, String data, double weight) {
        this.a= a;
        this.b = b;
        this.data = data;
        this.weight = weight;
    }
    
    /**
     * Sets the nodes of the edge.
     * @param A Node object 'a'.
     * @param B Node object 'b'.
     */
    public void setNodes(Node A, Node B) {
        this.a = A;
        this.b = B;
    }
    
    /**
     * Getter function to retrieve the nodes.
     * @return A HashMap containing both nodes.
     */
    public HashMap<Integer, Node> getNodes() {

        HashMap<Integer, Node> hm = new HashMap<>();
        hm.put(1, a);
        hm.put(2, b);
        return hm;
    }
    
    /**
     * Sets the data.
     * @param Data The edge data as string.
     */
    public void setData(String Data) {
        this.data = Data;
    }
    
    /**
     * Getter function to retrieve the data.
     * @return String with the data.
     */
    public String getData(){
        return data;
    }
    
    /**
     * Setter function for the weight.
     * @param weight Double.
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    /**
     * Getter function of the weight.
     * @return Double.
     */
    public double getWeight() {
        return weight;
    }
    
    // Overriding equals() to compare two Edge objects
    /**
     * See Java documentation.
     * @param o The object to compare
     * @return Boolean that represents if the objects are equal.
     */
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

    /**
     * See Java documentation.
     * @return Integer. The hash code.
     */
    @Override
    public int hashCode() {
        return (int)Double.hashCode(this.weight);
    }

    /**
     * See Java documentation.
     * @param edge Edge object to compare.
     * @return Integer.
     */
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
    
    /**
     * Represents as a string the information encoded in the edge.
     * @return String.
     */
    @Override
    public String toString() {
        return "Edge connects node " + this.a.getId() + " to node " + this.b.getId() + " and it has a weight of: " + this.getWeight() + "\n";
    }
}
