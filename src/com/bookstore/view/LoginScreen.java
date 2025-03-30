
package com.bookstore.view;

import com.bookstore.controller.AuthController;

import javax.swing.*;
import java.awt.*;

/**
 * Login screen for the bookstore application.
 */
public class LoginScreen extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private AuthController authController;
    
    /**
     * Constructor.
     */
    public LoginScreen() {
        this.authController = new AuthController();
        initializeComponents();
    }
    
    /**
     * Initializes the components of the login screen.
     */
    private void initializeComponents() {
        setLayout(new BorderLayout());
        
        // Create the title panel
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Welcome to the BookStore App");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        
        // Create the form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(new JLabel()); // Empty space
        formPanel.add(loginButton);
        
        // Add components to the main panel
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
    }
    
    /**
     * Handles the login button click.
     */
    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both username and password",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean loginSuccessful = authController.login(username, password);
        
        if (loginSuccessful) {
            JOptionPane.showMessageDialog(this,
                    "Login successful!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            // The main application frame would handle screen switching based on the authenticated user
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
