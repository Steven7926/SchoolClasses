/* Name: Steven Hudson
 Course: CNT 4714 – Spring 2020
 Assignment title: Project 3 – Two-Tier Client-Server Application Development With MySQL and JDBC
 Date: Sunday February 21, 2020
*/

import java.awt.*;
import java.io.*;
import java.sql.SQLException;

import javax.swing.JFrame;

public class Driver
{
    public static void main(String args[]) throws IOException, ClassNotFoundException, SQLException
    {
        SQLGUI gui = new SQLGUI();
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        gui.setLayout(layout);
        gui.setSize(760, 680);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setLocationRelativeTo(null);
        gui.setVisible(true);
        gui.getContentPane().setBackground(Color.DARK_GRAY);
        gui.setResizable(false);
    }
}
