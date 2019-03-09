/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;

/**
 *
 * @author Hector Ivan Garcia-Hernandez
 */

public class Main{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                showGUI();
            }
        });
        
        //Variables to create graphs
        int n = 10, m = 10, dir = 0, cic = 0, d = 5;
        double p = 0.5, r = 0.5, g = 10;
        
        // Create object Graph
        //Graph grafo = new Graph();
        // Create grafo using the Erdös' algorithm
        //grafo.Erdos(n, m, dir, cic);
        // Create grafo using the Gilbert's algorithm
        //grafo.Gilbert(n, p, dir, cic);
        // Create grafo using the Geographic algorithm
        //grafo.Geo(n, r, dir, cic);
        // Create grafo using the Garabasi's algorithm
        //grafo.Barabasi(n, d, g, dir, cic);
        //grafo.graphGraph(grafo, dir, "bar");
        
    }
    
    private static void showGUI() {
        JFrame f = new JFrame("Menu Demo");
        JPanel p = new JPanel(new GridLayout(4,1));
        f.setSize(420, 400);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar jmb = new JMenuBar();

        JButton jbGrafos = new JButton("Make random Graph");
        jbGrafos.setActionCommand("hacerGrafos");

        JMenu jmHelp = new JMenu("Help");
        JMenuItem jmiAbout = new JMenuItem("About");
        jmHelp.add(jmiAbout);
        jmb.add(jmHelp);

        jmiAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Developed by Héctor I. García-Hernández", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        jbGrafos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] models = {"Erdös - Rényi", "Gilbert", "Simple Geographic Model", "Modified Barabási - Albert"};
                String[] dirig = {"No directed", "Directed"};
                String[] connect = {"No self connections", "Self connections"};
                
                JComboBox<String> comboGraph = new JComboBox<>(models);;
                JComboBox<String> comboDirig = new JComboBox<>(dirig);
                JComboBox<String> comboConnect = new JComboBox<>(connect);
                JSpinner JSnodos = new JSpinner(new SpinnerNumberModel(100, 0, 100000, 1));
                int n = (int)JSnodos.getValue();
                JSpinner JSedges = new JSpinner(new SpinnerNumberModel(10, 0, n * (n - 1) / 2, 1));
                JSpinner JSprobability = new JSpinner(new SpinnerNumberModel(0.5, 0, 1, 0.001));
                JSpinner JSdistance = new JSpinner(new SpinnerNumberModel(0.5, 0, java.lang.Math.sqrt(2), 0.001));
                JSpinner JSConNodes = new JSpinner(new SpinnerNumberModel(5, 0, (int)JSnodos.getValue(), 1));
                JSpinner JSexpectedNodes = new JSpinner(new SpinnerNumberModel(9, 1, 100000, 1));
                
                JSnodos.setEnabled(true);
                JSedges.setEnabled(true);
                JSprobability.setEnabled(false);
                JSdistance.setEnabled(false);
                JSConNodes.setEnabled(false);
                JSexpectedNodes.setEnabled(false);
                
                comboGraph.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        switch (comboGraph.getSelectedIndex()) {
                            case 0: // Erdös - Rényi model
                                JSnodos.setEnabled(true);
                                JSedges.setEnabled(true);
                                JSprobability.setEnabled(false);
                                JSdistance.setEnabled(false);
                                JSConNodes.setEnabled(false);
                                JSexpectedNodes.setEnabled(false);
                                break;
                            case 1: // Gilbert
                                JSnodos.setEnabled(true);
                                JSedges.setEnabled(false);
                                JSprobability.setEnabled(true);
                                JSdistance.setEnabled(false);
                                JSConNodes.setEnabled(false);
                                JSexpectedNodes.setEnabled(false);
                                break;
                            case 2: // Simple Geographic Model
                                JSnodos.setEnabled(true);
                                JSedges.setEnabled(false);
                                JSprobability.setEnabled(false);
                                JSdistance.setEnabled(true);
                                JSConNodes.setEnabled(false);
                                JSexpectedNodes.setEnabled(false);
                                break;
                            case 3: // Modified Barabási - Albert
                                JSnodos.setEnabled(true);
                                JSedges.setEnabled(false);
                                JSprobability.setEnabled(false);
                                JSdistance.setEnabled(false);
                                JSConNodes.setEnabled(true);
                                JSexpectedNodes.setEnabled(true);
                                break;
                        }
                    }
                });
                
                comboDirig.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(comboDirig.getSelectedItem());
                        int n = (int)JSnodos.getValue();
                        if (comboDirig.getSelectedIndex() == 0 && comboConnect.getSelectedIndex() == 0){
                            JSedges.setModel(new SpinnerNumberModel((int)JSedges.getValue(), 0, n * (n - 1) /2, 1));
                        }
                        if (comboDirig.getSelectedIndex() == 0 && comboConnect.getSelectedIndex() == 1){
                            JSedges.setModel(new SpinnerNumberModel((int)JSedges.getValue(), 0, n * (n + 1) / 2, 1));
                        }
                        if (comboDirig.getSelectedIndex() == 1 && comboConnect.getSelectedIndex() == 0){
                            JSedges.setModel(new SpinnerNumberModel((int)JSedges.getValue(), 0, n * (n - 1), 1));
                        }
                        if (comboDirig.getSelectedIndex() == 1 && comboConnect.getSelectedIndex() == 1){
                            JSedges.setModel(new SpinnerNumberModel((int)JSedges.getValue(), 0, n * n, 1));
                        }
                    }
                });
                
                comboConnect.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(comboConnect.getSelectedItem());
                        int n = (int)JSnodos.getValue();
                        if (comboDirig.getSelectedIndex() == 0 && comboConnect.getSelectedIndex() == 0){
                            JSedges.setModel(new SpinnerNumberModel((int)JSedges.getValue(), 0, n * (n - 1) /2, 1));
                        }
                        if (comboDirig.getSelectedIndex() == 0 && comboConnect.getSelectedIndex() == 1){
                            JSedges.setModel(new SpinnerNumberModel((int)JSedges.getValue(), 0, n * (n + 1) / 2, 1));
                        }
                        if (comboDirig.getSelectedIndex() == 1 && comboConnect.getSelectedIndex() == 0){
                            JSedges.setModel(new SpinnerNumberModel((int)JSedges.getValue(), 0, n * (n - 1), 1));
                        }
                        if (comboDirig.getSelectedIndex() == 1 && comboConnect.getSelectedIndex() == 1){
                            JSedges.setModel(new SpinnerNumberModel((int)JSedges.getValue(), 0, n * n, 1));
                        }
                    }
                });
                
                JSnodos.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        System.out.println(JSnodos.getValue());
                        int n = (int)JSnodos.getValue();
                        if (comboDirig.getSelectedIndex() == 0 && comboConnect.getSelectedIndex() == 0){
                            JSedges.setModel(new SpinnerNumberModel((int)JSedges.getValue(), 0, n * (n - 1) /2, 1));
                        }
                        if (comboDirig.getSelectedIndex() == 0 && comboConnect.getSelectedIndex() == 1){
                            JSedges.setModel(new SpinnerNumberModel((int)JSedges.getValue(), 0, n * (n + 1) / 2, 1));
                        }
                        if (comboDirig.getSelectedIndex() == 1 && comboConnect.getSelectedIndex() == 0){
                            JSedges.setModel(new SpinnerNumberModel((int)JSedges.getValue(), 0, n * (n - 1), 1));
                        }
                        if (comboDirig.getSelectedIndex() == 1 && comboConnect.getSelectedIndex() == 1){
                            JSedges.setModel(new SpinnerNumberModel((int)JSedges.getValue(), 0, n * n, 1));
                        }
                    }
                });
                
                JSedges.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        System.out.println(JSedges.getValue());
                    }
                });
                
                JSprobability.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        System.out.println(JSprobability.getValue());
                    }
                });
                
                JSdistance.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        System.out.println(JSdistance.getValue());
                    }
                });
                
                JSConNodes.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        System.out.println(JSConNodes.getValue());
                    }
                });
                
                JSexpectedNodes.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        System.out.println(JSexpectedNodes.getValue());
                    }
                });
                
                JPanel panel = new JPanel(new GridLayout(0, 1));
                
                panel.add(new JLabel("Select the model to be used."));
                panel.add(comboGraph);
                panel.add(new JLabel("Select if the graph will be directed or not."));
                panel.add(comboDirig);
                panel.add(new JLabel("Allows or not a node to be self-connected."));
                panel.add(comboConnect);
                panel.add(new JLabel("Select the number of nodes."));
                panel.add(JSnodos);
                panel.add(new JLabel("Select the number of edges. Used in Erdös - Rényi."));
                panel.add(JSedges);
                panel.add(new JLabel("Probability. Used in Gilbert's model."));
                panel.add(JSprobability);
                panel.add(new JLabel("Distance. Used in the Simple Geographic Model."));
                panel.add(JSdistance);
                panel.add(new JLabel("Number of first fully-connected nodes. Used in Barabási - Albert."));
                panel.add(JSConNodes);
                panel.add(new JLabel("Node grad expected. Used in Barabási - Albert."));
                panel.add(JSexpectedNodes);
                
                int result = JOptionPane.showConfirmDialog(null, panel, "Choose method and set parameters",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    System.out.println(comboGraph.getSelectedItem() + " " + comboDirig.getSelectedItem() + " " + comboConnect.getSelectedItem() + " " + JSnodos.getValue() + " " + JSedges.getValue() + " " + JSprobability.getValue() + " " + JSdistance.getValue() + " " + JSConNodes.getValue() + " " + JSexpectedNodes.getValue());
                    // Create object Graph
                    Graph grafo = new Graph();
                    switch (comboGraph.getSelectedIndex()) {
                        case 0: // Erdös - Rényi model
                            // Create grafo using the Erdös' algorithm
                            grafo.Erdos((int)JSnodos.getValue(), (int)JSedges.getValue(), (int)comboDirig.getSelectedIndex(), (int)comboConnect.getSelectedIndex());
                            grafo.graphGraph(grafo, comboDirig.getSelectedIndex(), "erd");
                            System.out.println(grafo.getNodesGraph().get(0).getAdjacentNodes());
                            break;
                        case 1: // Gilbert
                            // Create grafo using the Gilbert's algorithm
                            grafo.Gilbert((int)JSnodos.getValue(), (double)JSprobability.getValue(), (int)comboDirig.getSelectedIndex(), (int)comboConnect.getSelectedIndex());
                            grafo.graphGraph(grafo, comboDirig.getSelectedIndex(), "gil");
                            break;
                        case 2: // Simple Geographic Model
                            // Create grafo using the Geographic algorithm
                            grafo.Geo((int)JSnodos.getValue(), (double)JSdistance.getValue(), (int)comboDirig.getSelectedIndex(), (int)comboConnect.getSelectedIndex());
                            grafo.graphGraph(grafo, comboDirig.getSelectedIndex(), "geo");
                            break;
                        case 3: // Modified Barabási - Albert
                            // Create grafo using the Garabasi's algorithm
                            grafo.Barabasi((int)JSnodos.getValue(), (int)JSConNodes.getValue(), (int)JSexpectedNodes.getValue(), (int)comboDirig.getSelectedIndex(), (int)comboConnect.getSelectedIndex());
                            grafo.graphGraph(grafo, comboDirig.getSelectedIndex(), "bar");
                            break;
                        }
                } else {
                    System.out.println("Cancelled");
                }
            }
        });
        
        p.add(jbGrafos);
        f.add(p);

        f.setJMenuBar(jmb);
        f.setVisible(true);
    }
}
