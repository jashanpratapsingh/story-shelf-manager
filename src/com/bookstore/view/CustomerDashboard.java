
package com.bookstore.view;

import com.bookstore.controller.AuthController;
import com.bookstore.model.Customer;
import com.bookstore.util.DataStore;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard for customers.
 */
public class CustomerDashboard extends JPanel {
    private JFrame parentFrame;
    private AuthController authController;
    
    /**
     * Constructor.
     * 
     * @param parentFrame The parent JFrame for navigation
     */
    public CustomerDashboard(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.authController = new AuthController();
        initializeComponents();
    }
    
    /**
     * Initializes the components of the customer dashboard.
     */
    private void initializeComponents() {
        setLayout(new BorderLayout());
        
        // Get current customer name
        String customerName = "Customer";
        if (authController.getCurrentUser() != null) {
            // Find the customer with matching username
            String username = authController.getCurrentUser().getUsername();
            for (Customer customer : DataStore.getInstance().getCustomers()) {
                if (customer.getUsername().equals(username)) {
                    customerName = customer.getName();
                    break;
                }
            }
        }
        
        // Create header panel with welcome message and logout button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + customerName);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> handleLogout());
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        // Create main content panel with options
        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JButton browseBooksButton = new JButton("Browse Books");
        browseBooksButton.addActionListener(e -> navigateTo("browse-books"));
        
        JButton purchaseHistoryButton = new JButton("Purchase History");
        purchaseHistoryButton.addActionListener(e -> navigateTo("purchase-history"));
        
        contentPanel.add(browseBooksButton);
        contentPanel.add(purchaseHistoryButton);
        
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
