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
public class Edge {
    
    Node a;
    Node b;
    String data;
    double weight;
    
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
}
