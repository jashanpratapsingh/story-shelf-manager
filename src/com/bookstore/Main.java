
package com.bookstore;

import com.bookstore.util.DataStore;
import com.bookstore.view.LoginScreen;

import javax.swing.*;
import java.awt.*;

/**
 * Main entry point for the bookstore application.
 */
public class Main {
    
    /**
     * Main method.
     */
    public static void main(String[] args) {
        // Initialize data store
        DataStore dataStore = DataStore.getInstance();
        dataStore.loadData();
        
        // Set up the look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and show the GUI
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("BookStore Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            
            // Set the initial screen to the login screen and pass the frame for navigation
            frame.getContentPane().add(new LoginScreen(frame));
            
            frame.setVisible(true);
            
            // Add shutdown hook to save data when the application closes
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Saving data...");
                dataStore.saveData();
            }));
        });
    }
}
