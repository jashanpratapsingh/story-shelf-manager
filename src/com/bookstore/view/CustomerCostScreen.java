
package com.bookstore.view;

import com.bookstore.controller.AuthController;

import javax.swing.*;
import java.awt.*;

/**
 * Screen displaying the cost after a purchase.
 */
public class CustomerCostScreen extends JPanel {
    private JFrame parentFrame;
    private AuthController authController;
    private double totalCost;
    private int points;
    
    /**
     * Constructor.
     * 
     * @param parentFrame The parent JFrame for navigation
     * @param totalCost The total cost of the purchase
     * @param points The updated points after the purchase
     */
    public CustomerCostScreen(JFrame parentFrame, double totalCost, int points) {
        this.parentFrame = parentFrame;
        this.authController = new AuthController();
        this.totalCost = totalCost;
        this.points = points;
        initializeComponents();
    }
    
    /**
     * Initializes the components of the cost screen.
     */
    private void initializeComponents() {
        setLayout(new BorderLayout());
        
        // Create panel for cost information
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JLabel costLabel = new JLabel("Total Cost: $" + String.format("%.2f", totalCost));
        costLabel.setFont(new Font("Arial", Font.BOLD, 18));
        costLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        String status = points >= 1000 ? "Gold" : "Silver";
        JLabel pointsLabel = new JLabel("Points: " + points + ", Status: " + status);
        pointsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        pointsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Create logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> handleLogout());
        
        // Add components to the panel
        infoPanel.add(costLabel);
        infoPanel.add(pointsLabel);
        infoPanel.add(logoutButton);
        
        // Add the panel to the center of the main panel
        add(infoPanel, BorderLayout.CENTER);
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
}
