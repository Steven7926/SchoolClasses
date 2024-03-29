/* Name: Steven Hudson
 Course: CNT 4714 – Spring 2020
 Assignment title: Project 1 – Event-driven Enterprise Simulation
 Date: Sunday January 26, 2020
*/


import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.swing.JFrame;

public class BookStoreDriver
{
    public static void main(String args[]) throws IOException
    {
        BookStore storeFrame = new BookStore();
        storeFrame.setLocationRelativeTo(null);
        storeFrame.setSize(525, 225);
        storeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        storeFrame.setVisible(true);
        storeFrame.getContentPane().setBackground(Color.DARK_GRAY);
        storeFrame.setResizable(false);
    }
}