
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
        
        JButton manageBooksButton = new JButton("Manage Books");
        manageBooksButton.addActionListener(e -> navigateTo("manage-books"));
        
        JButton manageCustomersButton = new JButton("Manage Customers");
        manageCustomersButton.addActionListener(e -> navigateTo("manage-customers"));
        
        JButton viewStatsButton = new JButton("View Statistics");
        viewStatsButton.addActionListener(e -> navigateTo("view-stats"));
        
        contentPanel.add(manageBooksButton);
        contentPanel.add(manageCustomersButton);
        contentPanel.add(viewStatsButton);
        
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
        // For now, just show a message that this feature is coming soon
        JOptionPane.showMessageDialog(this,
                "This feature will be implemented soon!",
                "Coming Soon",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
