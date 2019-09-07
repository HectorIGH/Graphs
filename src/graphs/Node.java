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
public class Node implements Comparable<Node> {
    
    int id, grad = 0;
    String name, data;
    double x,y;
    boolean visited = false;
    HashMap<Integer, Node> adjacentNodes = new HashMap<>();
    double cost = 0;
    
    /**
     * Empty constructor.
     */
    public Node() {
        // Construct with no parameter
    }
    
    /** 
     * Constructor with parameters.
     * @param id Integer. The Node ID.
     * @param name The node name as string.
     * @param data The node data as string.
     */
    public Node(int id, String name, String data) {
        this.id = id;
        this.name = name;
        this.data = data;
    }
    
    /**
     * Constructor.
     * @param id Node ID as integer.
     * @param name Node name as String.
     * @param x Node x coordinate as double.
     * @param y Node y coordinate as double.
     * @param data Node data as string.
     */
    public Node(int id, String name, double x, double y, String data) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.x = x;
        this.y = y;
    }
    
    /**
     * Adds a node to the adjacent list, whish is a HashMap.
     * @param nodo Node object to add as adjacent.
     */
    public void setAdjacent(Node nodo) {
        adjacentNodes.put(nodo.getId(), nodo);
    }
    
    /**
     * Getter function of the adjacent list.
     * @return HashMap of adjacency.
     */
    public HashMap<Integer, Node> getAdjacentNodes() {
        return adjacentNodes;
    }
    
    /**
     * Setter ID.
     * @param Id Integer.
     */
    public void setId(int Id) {
        id = Id;
    }
    
    /**
     * Getter function of ID.
     * @return Integer.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Setter function of the name.
     * @param Name String.
     */
    public void setName(String Name) {
        name = Name;
    }
    
    /**
     * Getter function for the name.
     * @return String.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Setter function for the data.
     * @param Data String.
     */
    public void setData(String Data) {
        data = Data;
    }
    
    /**
     *  Getter function for the data
     * @return String
     */
    public String getData() {
        return data;
    }
    
    /**
     * Setter function for coordinates.
     * @param x Double. x coordinate. 
     * @param y Double. y coordinate. 
     */
    public void setCoord(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Getter function for x coordinate.
     * @return Double.
     */
    public double getX() {
        return x;
    }
    
    /**
     * Getter function for y coordinate.
     * @return Double.
     */
    public double getY() {
        return y;
    }
    
    /**
     * Increase the grad of the node by 1.
     */
    public void updateGrad() {
        this.grad += 1;
    }
    
    /**
     * Getter function of the grad of the node.
     * @return Integer.
     */
    public int getGrad() {
        return grad;
    }
    
    /**
     * Setter function of the cost for certain algorithms.
     * @param cost Double.
     */
    public void setCost(double cost) {
        this.cost = cost;
    }
    
    /**
     * Getter function of the cost.
     * @return Double.
     */
    public double getCost() {
        return cost;
    }
    
    /**
     * See Java documentation.
     * @param o Object.
     * @return Boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Node nodo = (Node) o;
        return Double.compare(nodo.cost, cost) == 0;
    }

    /**
     * See Java documentation.
     * @return integer
     */
    @Override
    public int hashCode() {
        return (int)Double.hashCode(this.cost);
    }

    /**
     * See Java documentation.
     * @param node Node object.
     * @return Integer.
     */
    @Override
    public int compareTo(Node node) {
        if (this.getCost() > node.getCost()) {
            return 1;
        } else if (this.getCost() < node.getCost()) {
            return -1;
        } else {
            return 0;
        }
    }
    
    /**
     * Represents as a string the information encoded in the node.
     * @return String.
     */
    @Override
    public String toString() {
        return "Node " + this.getId() + " it has a cost: " + this.getCost() + " and it is " + this.visited + " that's been visited.\n";
    }
}
