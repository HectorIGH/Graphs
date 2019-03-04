/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Hector Ivan Garcia-Hernandez
 */

public class Main implements ActionListener{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new Main();
        
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
        
    }
    
    Main() {
        JFrame f = new JFrame("Menu Demo");
        f.setSize(220, 200);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar jmb = new JMenuBar();

        JMenu jmFile = new JMenu("File");
        JMenuItem jmiOpen = new JMenuItem("Open");
        JMenuItem jmiClose = new JMenuItem("Close");
        JMenuItem jmiSave = new JMenuItem("Save");
        JMenuItem jmiExit = new JMenuItem("Exit");
        JButton jb = new JButton("Boton");
        f.add(jb);
        jmFile.add(jmiOpen);
        jmFile.add(jmiClose);
        jmFile.add(jmiSave);
        jmFile.addSeparator();
        jmFile.add(jmiExit);
        jmb.add(jmFile);

        JMenu jmOptions = new JMenu("Options");
        JMenu a = new JMenu("A");
        JMenuItem b = new JMenuItem("B");
        JMenuItem c = new JMenuItem("C");
        JMenuItem d = new JMenuItem("D");
        a.add(b);
        a.add(c);
        a.add(d);
        jmOptions.add(a);

        JMenu e = new JMenu("E");
        e.add(new JMenuItem("F"));
        e.add(new JMenuItem("G"));
        jmOptions.add(e);

        jmb.add(jmOptions);

        JMenu jmHelp = new JMenu("Help");
        JMenuItem jmiAbout = new JMenuItem("About");
        jmHelp.add(jmiAbout);
        jmb.add(jmHelp);

        jmiOpen.addActionListener(this);
        jmiClose.addActionListener(this);
        jmiSave.addActionListener(this);
        jmiExit.addActionListener(this);
        b.addActionListener(this);
        c.addActionListener(this);
        d.addActionListener(this);
        jmiAbout.addActionListener(this);
        jb.addActionListener(this);

        f.setJMenuBar(jmb);
        f.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        String comStr = ae.getActionCommand();
        System.out.println(comStr + " Selected");
    }
    
}
