/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

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
    }
    
    private static void showGUI() {
        // Create object Graph
        Graph grafo = new Graph();
        JFrame f = new JFrame("Menu Demo");
        JPanel p = new JPanel(new GridLayout(0,1));
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar jmb = new JMenuBar();

        JButton jbGrafos = new JButton("Make random Graph");
        jbGrafos.setActionCommand("hacerGrafos");
        
        JButton jbLoadGraph = new JButton("Load a Graph from file");
        jbLoadGraph.setActionCommand("leerGrafo");
        
        JButton jbSearchAlg = new JButton("Search Algorithms");
        jbSearchAlg.setActionCommand("searchAlgorithms");
        jbSearchAlg.setEnabled(false);
        
        JButton jbAddWeights = new JButton("Add weight to edges");
        jbAddWeights.setActionCommand("addWeight");
        jbAddWeights.setEnabled(false);
        
        JButton jbDijksAlg = new JButton("Dijkstra's algorithm");
        jbDijksAlg.setActionCommand("dijkstrasalgorithm");
        jbDijksAlg.setEnabled(false);
        
        JButton jbMST = new JButton("Minimum Spanning Tree algorithms");
        jbMST.setActionCommand("MSTalgorithms");
        jbMST.setEnabled(false);
        
        JButton jbFFT = new JButton("Fast Fourier Transform");
        jbFFT.setActionCommand("FFT");
        jbFFT.setEnabled(true);

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
                
                JComboBox<String> comboGraph = new JComboBox<>(models);
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
                        //System.out.println(comboDirig.getSelectedItem());
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
                        //System.out.println(comboConnect.getSelectedItem());
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
                        //System.out.println(JSnodos.getValue());
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
                        //System.out.println(JSedges.getValue());
                    }
                });
                
                JSprobability.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        //System.out.println(JSprobability.getValue());
                    }
                });
                
                JSdistance.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        //System.out.println(JSdistance.getValue());
                    }
                });
                
                JSConNodes.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        //System.out.println(JSConNodes.getValue());
                    }
                });
                
                JSexpectedNodes.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        //System.out.println(JSexpectedNodes.getValue());
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
                    //System.out.println(comboGraph.getSelectedItem() + " " + comboDirig.getSelectedItem() + " " + comboConnect.getSelectedItem() + " " + JSnodos.getValue() + " " + JSedges.getValue() + " " + JSprobability.getValue() + " " + JSdistance.getValue() + " " + JSConNodes.getValue() + " " + JSexpectedNodes.getValue());
                    // Dialog and worker to show a progress bar
                    SwingWorker<Void, Void> worker;
                    final JDialog dialog = new JDialog(f, true);
                    dialog.setUndecorated(true);
                    dialog.setLocationRelativeTo(null);
                    dialog.setLocation(f.getLocation().x + f.getSize().width / 4, f.getLocation().y + f.getSize().height / 4);
                    JProgressBar bar = new JProgressBar();
                    bar.setIndeterminate(true);
                    bar.setStringPainted(true);
                    bar.setBackground(Color.green);
                    bar.setString("Creating. Please wait...");
                    dialog.add(bar);
                    dialog.pack();
                    switch (comboGraph.getSelectedIndex()) {
                        case 0: // Erdös - Rényi model
                            // Create grafo using the Erdös' algorithm
                            // Shows a simple progress bar while executing
                            worker = new SwingWorker<Void, Void>() {
                                @Override
                                protected Void doInBackground() {
                                    grafo.Erdos((int)JSnodos.getValue(), (int)JSedges.getValue(), (int)comboDirig.getSelectedIndex(), (int)comboConnect.getSelectedIndex());
                                    grafo.graphGraph(grafo, comboDirig.getSelectedIndex(), "erd");
                                    return null;
                                }
                                @Override
                                protected void done() {
                                    dialog.dispose();
                                }
                            };
                            worker.execute();
                            dialog.setVisible(true);
                            break;
                        case 1: // Gilbert
                            // Create grafo using the Gilbert's algorithm
                            // Shows a simple progress bar while executing
                            worker = new SwingWorker<Void, Void>() {
                                @Override
                                protected Void doInBackground() {
                                    grafo.Gilbert((int)JSnodos.getValue(), (double)JSprobability.getValue(), (int)comboDirig.getSelectedIndex(), (int)comboConnect.getSelectedIndex());
                                    grafo.graphGraph(grafo, comboDirig.getSelectedIndex(), "gil");
                                    return null;
                                }
                                @Override
                                protected void done() {
                                    dialog.dispose();
                                }
                            };
                            worker.execute();
                            dialog.setVisible(true);
                            break;
                        case 2: // Simple Geographic Model
                            // Create grafo using the Geographic algorithm
                            // Shows a simple progress bar while executing
                            worker = new SwingWorker<Void, Void>() {
                                @Override
                                protected Void doInBackground() {
                                    grafo.Geo((int)JSnodos.getValue(), (double)JSdistance.getValue(), (int)comboDirig.getSelectedIndex(), (int)comboConnect.getSelectedIndex());
                                    grafo.graphGraph(grafo, comboDirig.getSelectedIndex(), "geo");
                                    return null;
                                }
                                @Override
                                protected void done() {
                                    dialog.dispose();
                                }
                            };
                            worker.execute();
                            dialog.setVisible(true);
                            break;
                        case 3: // Modified Barabási - Albert
                            // Create grafo using the Garabasi's algorithm
                            // Shows a simple progress bar while executing
                            worker = new SwingWorker<Void, Void>() {
                                @Override
                                protected Void doInBackground() {
                                    grafo.Barabasi((int)JSnodos.getValue(), (int)JSConNodes.getValue(), (int)JSexpectedNodes.getValue(), (int)comboDirig.getSelectedIndex(), (int)comboConnect.getSelectedIndex());
                                    grafo.graphGraph(grafo, comboDirig.getSelectedIndex(), "bar");
                                    return null;
                                }
                                @Override
                                protected void done() {
                                    dialog.dispose();
                                }
                            };
                            worker.execute();
                            dialog.setVisible(true);
                            break;
                        }
                    jbSearchAlg.setEnabled(true);
                    jbAddWeights.setEnabled(true);
                    jbDijksAlg.setEnabled(true);
                    jbMST.setEnabled(true);
                } else {
                    //System.out.println("Cancelled");
                }
            }
        });
        
        jbLoadGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(null, "Under Construction", "Information", JOptionPane.INFORMATION_MESSAGE);
                grafo.resetNodes(grafo);
                grafo.resetTree(grafo);
                int read = grafo.readGraph(grafo);
                if (read == 1) {
                    jbSearchAlg.setEnabled(true);
                    jbAddWeights.setEnabled(true);
                    jbDijksAlg.setEnabled(true);
                    jbMST.setEnabled(true);
                }
            }
        });
        
        jbSearchAlg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f = new JFrame("");
                JPanel panel = new JPanel(new GridLayout(0, 1));
                f.setLocationRelativeTo(null);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                String[] algos = {"BFS", "Recursive DFS", "Iterative DFS"};
                JComboBox<String> comboAlgo = new JComboBox<>(algos);
                JSpinner JSnodo = new JSpinner(new SpinnerNumberModel(0, 0, grafo.Nodes.size(), 1));
                JButton JBalgo = new JButton("Run the algorithm.");
                panel.add(new JLabel("Select the model to be used."));
                panel.add(comboAlgo);
                panel.add(new JLabel("Select the root node"));
                panel.add(JSnodo);
                panel.add(JBalgo);
                JBalgo.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int alg = comboAlgo.getSelectedIndex();
                        int node = (int)JSnodo.getValue();
                        SwingWorker<Void, Void> worker;
                        final JDialog dialog = new JDialog(f, true);
                        dialog.setUndecorated(true);
                        dialog.setLocationRelativeTo(null);
                        dialog.setLocation(f.getLocation().x + f.getSize().width / 4, f.getLocation().y + f.getSize().height / 4);
                        JProgressBar bar = new JProgressBar();
                        bar.setIndeterminate(true);
                        bar.setStringPainted(true);
                        bar.setBackground(Color.green);
                        bar.setString("Calculating. Please wait...");
                        dialog.add(bar);
                        dialog.pack();
                        switch (alg) {
                            case 0:
                                worker = new SwingWorker<Void, Void>() {
                                    @Override
                                    protected Void doInBackground() {
                                        grafo.BFS(grafo, grafo.Nodes.get(node));
                                        grafo.graphTree(grafo, "BFSTree");
                                        grafo.resetNodes(grafo);
                                        grafo.resetTree(grafo);
                                        return null;
                                    }
                                    @Override
                                    protected void done() {
                                        dialog.dispose();
                                    }
                                };
                                worker.execute();
                                dialog.setVisible(true);
                                break;
                            case 1:
                                worker = new SwingWorker<Void, Void>() {
                                    @Override
                                    protected Void doInBackground() {
                                        grafo.DFS_R(grafo, grafo.Nodes.get(node));
                                        grafo.graphTree(grafo, "DFS_Recursive_Tree");
                                        grafo.resetNodes(grafo);
                                        grafo.resetTree(grafo);
                                        return null;
                                    }
                                    @Override
                                    protected void done() {
                                        dialog.dispose();
                                    }
                                };
                                worker.execute();
                                dialog.setVisible(true);
                                break;
                            case 2:
                                worker = new SwingWorker<Void, Void>() {
                                    @Override
                                    protected Void doInBackground() {
                                        grafo.DFS_I(grafo, grafo.Nodes.get(node));
                                        grafo.graphTree(grafo, "DFS_Iterative_Tree");
                                        grafo.resetNodes(grafo);
                                        grafo.resetTree(grafo);
                                        return null;
                                    }
                                    @Override
                                    protected void done() {
                                        dialog.dispose();
                                    }
                                };
                                worker.execute();
                                dialog.setVisible(true);
                                break;
                        }
                    }
                });
                //int result = JOptionPane.showConfirmDialog(null, panel, "Choose algorithm and set root node",JOptionPane.YES_OPTION, JOptionPane.PLAIN_MESSAGE);
                String[] options = {"OK"};
                JOptionPane.showOptionDialog(null, panel, "Search algorithms", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            }
        });
        
        jbAddWeights.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f = new JFrame("");
                f.setLocationRelativeTo(null);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JPanel panel = new JPanel(new GridLayout(0, 1));
                JSpinner JSminWeight = new JSpinner(new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 0.5));
                JSpinner JSmaxWeight = new JSpinner(new SpinnerNumberModel(10, 0, Integer.MAX_VALUE, 0.5));
                JCheckBox JCBinteger = new JCheckBox("Check if you want only integer weights.", true);
                JButton JBalgo = new JButton("Set weights.");
                panel.add(new JLabel("Set the minimum weight to be randomly applied to the edges."));
                panel.add(JSminWeight);
                panel.add(new JLabel("Set the maximum weight to be randomly applied to the edges."));
                panel.add(JSmaxWeight);
                panel.add(JCBinteger);
                panel.add(JBalgo);
                JBalgo.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        double min = (double)JSminWeight.getValue();
                        double max = (double)JSmaxWeight.getValue();
                        boolean onlyInteger = JCBinteger.isSelected();
                        SwingWorker<Void, Void> worker;
                        final JDialog dialog = new JDialog(f, true);
                        dialog.setUndecorated(true);
                        dialog.setLocationRelativeTo(null);
                        dialog.setLocation(f.getLocation().x + f.getSize().width / 4, f.getLocation().y + f.getSize().height / 4);
                        JProgressBar bar = new JProgressBar();
                        bar.setIndeterminate(true);
                        bar.setStringPainted(true);
                        bar.setBackground(Color.green);
                        bar.setString("Adding weights. Please wait...");
                        dialog.add(bar);
                        dialog.pack();
                        worker = new SwingWorker<Void, Void>() {
                            @Override
                            protected Void doInBackground() {
                                grafo.setEdgeWeights(min, max, onlyInteger);
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
                });
                String[] options = {"OK"};
                JOptionPane.showOptionDialog(null, panel, "Setting weights", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            }
        });
        
        jbDijksAlg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f = new JFrame("");
                f.setLocationRelativeTo(null);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JPanel panel = new JPanel(new GridLayout(0, 1));
                JSpinner JSnodo = new JSpinner(new SpinnerNumberModel(0, 0, grafo.Nodes.size(), 1));
                JButton JBalgo = new JButton("Run the algorithm.");
                panel.add(new JLabel("Select the origin node."));
                panel.add(JSnodo);
                panel.add(JBalgo);
                JBalgo.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int node = (int)JSnodo.getValue();
                        SwingWorker<Void, Void> worker;
                        final JDialog dialog = new JDialog(f, true);
                        dialog.setUndecorated(true);
                        dialog.setLocationRelativeTo(null);
                        dialog.setLocation(f.getLocation().x + f.getSize().width / 4, f.getLocation().y + f.getSize().height / 4);
                        JProgressBar bar = new JProgressBar();
                        bar.setIndeterminate(true);
                        bar.setStringPainted(true);
                        bar.setBackground(Color.green);
                        bar.setString("Calculating. Please wait...");
                        dialog.add(bar);
                        dialog.pack();
                        worker = new SwingWorker<Void, Void>() {
                            @Override
                            protected Void doInBackground() {
                                grafo.Dijkstra(grafo, grafo.Nodes.get(node));
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
                });
                String[] options = {"OK"};
                JOptionPane.showOptionDialog(null, panel, "Dijkstra's algorithm", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            }
        });
        
        jbMST.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f = new JFrame("");
                JPanel panel = new JPanel(new GridLayout(0, 1));
                f.setLocationRelativeTo(null);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                String[] algos = {"Kruskal", "Reverse Delete Algorithm (Inverse Kruskal)", "Prim"};
                JComboBox<String> comboAlgo = new JComboBox<>(algos);
                JSpinner JSnodo = new JSpinner(new SpinnerNumberModel(0, 0, grafo.Nodes.size(), 1));
                JButton JBalgo = new JButton("Run the algorithm.");
                panel.add(new JLabel("Select the model to be used."));
                panel.add(comboAlgo);
                panel.add(new JLabel("Select the root node"));
                panel.add(JSnodo);
                panel.add(JBalgo);
                JBalgo.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int alg = comboAlgo.getSelectedIndex();
                        int node = (int)JSnodo.getValue();
                        SwingWorker<Void, Void> worker;
                        final JDialog dialog = new JDialog(f, true);
                        dialog.setUndecorated(true);
                        dialog.setLocationRelativeTo(null);
                        dialog.setLocation(f.getLocation().x + f.getSize().width / 4, f.getLocation().y + f.getSize().height / 4);
                        JProgressBar bar = new JProgressBar();
                        bar.setIndeterminate(true);
                        bar.setStringPainted(true);
                        bar.setBackground(Color.green);
                        bar.setString("Calculating. Please wait...");
                        dialog.add(bar);
                        dialog.pack();
                        switch (alg) {
                            case 0:
                                worker = new SwingWorker<Void, Void>() {
                                    @Override
                                    protected Void doInBackground() {
                                        grafo.kruskal(grafo);
                                        return null;
                                    }
                                    @Override
                                    protected void done() {
                                        dialog.dispose();
                                    }
                                };
                                worker.execute();
                                dialog.setVisible(true);
                                break;
                            case 1:
                                worker = new SwingWorker<Void, Void>() {
                                    @Override
                                    protected Void doInBackground() {
                                        grafo.inverseKruskal(grafo, grafo.Nodes.get(node));
                                        return null;
                                    }
                                    @Override
                                    protected void done() {
                                        dialog.dispose();
                                    }
                                };
                                worker.execute();
                                dialog.setVisible(true);
                                break;
                            case 2:
                                worker = new SwingWorker<Void, Void>() {
                                    @Override
                                    protected Void doInBackground() {
                                        grafo.prim(grafo, grafo.Nodes.get(node));
                                        return null;
                                    }
                                    @Override
                                    protected void done() {
                                        dialog.dispose();
                                    }
                                };
                                worker.execute();
                                dialog.setVisible(true);
                                break;
                        }
                    }
                });
                //int result = JOptionPane.showConfirmDialog(null, panel, "Choose algorithm and set root node",JOptionPane.YES_OPTION, JOptionPane.PLAIN_MESSAGE);
                String[] options = {"OK"};
                JOptionPane.showOptionDialog(null, panel, "Search algorithms", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            }
        });
        
        jbFFT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f = new JFrame("");
                JPanel panel = new JPanel(new GridLayout(4, 2, 3, 3));
                f.setLocationRelativeTo(null);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                String[] algos = {"Basico", "Mamalon", "Muy Mamalon"};
                JComboBox<String> comboAlgo = new JComboBox<>(algos);
                JButton JBalgo = new JButton("Run the algorithm.");
                panel.add(new JLabel("Select the model to be used."));
                panel.add(comboAlgo);
                panel.add(JBalgo);
                
                JBalgo.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int alg = comboAlgo.getSelectedIndex();
                        SwingWorker<Void, Void> worker;
                        switch (alg) {
                            case 0:
                                f.dispose();
                                worker = new SwingWorker<Void, Void>() {
                                    @Override
                                    protected Void doInBackground() {
                                        JOptionPane.getRootFrame().dispose();
                                        FileNameExtensionFilter filter = new FileNameExtensionFilter("WAV FIles", "wav", "wav");
                                        JFileChooser fc = new JFileChooser();
                                        fc.setDialogTitle("Choose WAV File");
                                        fc.setFileFilter(filter);
                                        fc.setCurrentDirectory(new File(System.getProperty("user.home")));
                                        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                                            File selectedFile = fc.getSelectedFile();
                                            Path path = Paths.get(selectedFile.getAbsolutePath());
                                            try {
                                                grafo.FFT(path);
                                            } catch (IOException | WavFileException ex) {
                                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                        return null;
                                    }
                                    @Override
                                    protected void done() {
                                    }
                                };
                                worker.execute();
                                break;
                            case 1:
                                int n = (int)Math.pow(2, 3);
                                Complex[] x = new Complex[n];
                                        for (int i = 0; i < n; i++) {
                                            x[i] = new Complex(i/1.0, 0.0);
                                        }
                                        System.out.println(Arrays.toString(x));
                                        System.out.println("Directa:");
                                        Complex[] y = grafo.fft(x);
                                        System.out.println("Inversa:");
                                        Complex[] z = grafo.ifft(y);
                                        for (int i = 0; i < n; i++) {
                                            System.out.println(x[i]+" : "+y[i]+" : "+z[i].abs());
                                        }
                                worker = new SwingWorker<Void, Void>() {
                                    @Override
                                    protected Void doInBackground() {
                                        return null;
                                    }
                                    @Override
                                    protected void done() {
                                    }
                                };
                                worker.execute();
                                break;
                            case 2:
                                worker = new SwingWorker<Void, Void>() {
                                    @Override
                                    protected Void doInBackground() {
                                        return null;
                                    }
                                    @Override
                                    protected void done() {
                                    }
                                };
                                worker.execute();
                                break;
                        }
                    }
                });
                //int result = JOptionPane.showConfirmDialog(null, panel, "Choose algorithm and set root node",JOptionPane.YES_OPTION, JOptionPane.PLAIN_MESSAGE);
                String[] options = {"OK"};
                JOptionPane.showOptionDialog(null, panel, "Search algorithms", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            }
        });
        
        p.add(jbGrafos);
        p.add(jbLoadGraph);
        p.add(jbSearchAlg);
        p.add(jbAddWeights);
        p.add(jbDijksAlg);
        p.add(jbMST);
        p.add(jbFFT);
        f.setSize(300, 50 * p.getComponentCount());
        f.add(p);

        f.setJMenuBar(jmb);
        f.setVisible(true);
        f.setLocationRelativeTo(null);
    }
}
