
package com.bookstore.view;

import com.bookstore.controller.AuthController;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard for store owners.
 */
public class OwnerDashboard extends JPanel {
    private JFrame parentFrame;
    private AuthController authController;
    
    /**
     * Constructor.
     * 
     * @param parentFrame The parent JFrame for navigation
     */
    public OwnerDashboard(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.authController = new AuthController();
        initializeComponents();
    }
    
    /**
     * Initializes the components of the owner dashboard.
     */
    private void initializeComponents() {
        setLayout(new BorderLayout());
        
        // Create header panel with title and logout button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("BookStore Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> handleLogout());
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        // Create main content panel with options
        JPanel contentPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JButton booksButton = new JButton("Books");
        booksButton.addActionListener(e -> navigateTo("books"));
        
        JButton customersButton = new JButton("Customers");
        customersButton.addActionListener(e -> navigateTo("customers"));
        
        contentPanel.add(booksButton);
        contentPanel.add(customersButton);
        
        // Add components to the main panel
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Handles the logout button click.
     */
    private void handleLogout() {
        authController.logout();
        
        // Navigate back to login screen
        if (parentFrame != null) {
            parentFrame.getContentPane().removeAll();
            parentFrame.getContentPane().add(new LoginScreen(parentFrame));
            parentFrame.setTitle("BookStore Management System");
            parentFrame.revalidate();
            parentFrame.repaint();
        }
    }
    
    /**
     * Navigates to the specified screen.
     * 
     * @param screen The screen to navigate to
     */
    private void navigateTo(String screen) {
        if (parentFrame == null) return;
        
        parentFrame.getContentPane().removeAll();
        
        switch (screen) {
            case "books":
                parentFrame.getContentPane().add(new OwnerBooksScreen(parentFrame));
                parentFrame.setTitle("BookStore - Manage Books");
                break;
            case "customers":
                parentFrame.getContentPane().add(new OwnerCustomersScreen(parentFrame));
                parentFrame.setTitle("BookStore - Manage Customers");
                break;
            default:
                parentFrame.getContentPane().add(new OwnerDashboard(parentFrame));
                parentFrame.setTitle("BookStore - Owner Dashboard");
                break;
        }
        
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}
