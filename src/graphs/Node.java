/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import java.util.HashMap;

/**
 *
 * @author Hector Ivan Garcia-Hernandez
 */
public class Node {
    
    int id, grad = 0;
    String name, data;
    double x,y;
    boolean visited = false;
    HashMap<Integer, Node> adjacentNodes = new HashMap<>();
    double weight = 0;
    
    public Node() {
        // Construct with no parameter
    }
    
    public Node(int id, String name, String data) {
        this.id = id;
        this.name = name;
        this.data = data;
    }
    
    public Node(int id, String name, double x, double y, String data) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.x = x;
        this.y = y;
    }
    
    public void setAdjacent(Node nodo) {
        adjacentNodes.put(nodo.getId(), nodo);
    }
    
    public HashMap<Integer, Node> getAdjacentNodes() {
        return adjacentNodes;
    }
    
    public void setId(int Id) {
        id = Id;
    }
    
    public int getId() {
        return id;
    }
    
    public void setName(String Name) {
        name = Name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setData(String Data) {
        data = Data;
    }
    
    public String getData() {
        return data;
    }
    
    public void setCoord(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public void updateGrad() {
        this.grad += 1;
    }
    
    public int getGrad() {
        return grad;
    }
}
