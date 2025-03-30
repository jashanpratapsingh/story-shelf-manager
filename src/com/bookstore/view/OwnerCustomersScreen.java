
package com.bookstore.view;

import com.bookstore.model.Customer;
import com.bookstore.util.DataStore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Screen for managing customers in the bookstore.
 */
public class OwnerCustomersScreen extends JPanel {
    private JFrame parentFrame;
    private DefaultTableModel tableModel;
    private JTable customersTable;
    private JTextField usernameField;
    private JTextField passwordField;
    
    /**
     * Constructor.
     * 
     * @param parentFrame The parent JFrame for navigation
     */
    public OwnerCustomersScreen(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        initializeComponents();
        loadCustomersData();
    }
    
    /**
     * Initializes the components of the customers management screen.
     */
    private void initializeComponents() {
        setLayout(new BorderLayout());
        
        // Top part - Customers table
        tableModel = new DefaultTableModel(new Object[]{"Username", "Password", "Points"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        customersTable = new JTable(tableModel);
        customersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(customersTable);
        
        // Middle part - Add customer form
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JTextField(20);
        
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> handleAddCustomer());
        
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(new JLabel()); // Empty space
        formPanel.add(addButton);
        
        // Bottom part - Delete and Back buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> handleDeleteCustomer());
        
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> navigateBack());
        
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        
        // Add all parts to the main panel
        add(tableScrollPane, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Loads customers data from the data store into the table.
     */
    private void loadCustomersData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get customers from data store
        List<Customer> customers = DataStore.getInstance().getCustomers();
        
        // Add customers to table
        for (Customer customer : customers) {
            int points = calculateTotalPoints(customer);
            tableModel.addRow(new Object[]{customer.getUsername(), customer.getPassword(), points});
        }
    }
    
    /**
     * Calculates the total points for a customer based on their purchase history.
     */
    private int calculateTotalPoints(Customer customer) {
        // For simplicity, we'll just return the count of purchases for now
        return customer.getPurchaseHistory() != null ? customer.getPurchaseHistory().size() * 100 : 0;
    }
    
    /**
     * Handles adding a new customer.
     */
    private void handleAddCustomer() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both username and password",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if customer with same username already exists
        List<Customer> customers = DataStore.getInstance().getCustomers();
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username)) {
                JOptionPane.showMessageDialog(this,
                        "A customer with this username already exists",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Create and add the new customer
        Customer newCustomer = new Customer();
        newCustomer.setUsername(username);
        newCustomer.setPassword(password);
        newCustomer.setName(username); // Using username as name for simplicity
        newCustomer.setPurchaseHistory(new ArrayList<>());
        
        // Add to data store
        customers.add(newCustomer);
        
        // Update table
        tableModel.addRow(new Object[]{username, password, 0});
        
        // Clear input fields
        usernameField.setText("");
        passwordField.setText("");
        
        JOptionPane.showMessageDialog(this,
                "Customer added successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Handles deleting a customer.
     */
    private void handleDeleteCustomer() {
        int selectedRow = customersTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a customer to delete",
                    "Selection Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String username = (String) tableModel.getValueAt(selectedRow, 0);
        
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the customer: " + username + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);
        
        if (confirmation == JOptionPane.YES_OPTION) {
            // Remove from data store
            List<Customer> customers = DataStore.getInstance().getCustomers();
            for (int i = 0; i < customers.size(); i++) {
                if (customers.get(i).getUsername().equals(username)) {
                    customers.remove(i);
                    break;
                }
            }
            
            // Remove from table
            tableModel.removeRow(selectedRow);
            
            JOptionPane.showMessageDialog(this,
                    "Customer deleted successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Navigates back to the owner dashboard.
     */
    private void navigateBack() {
        if (parentFrame != null) {
            parentFrame.getContentPane().removeAll();
            parentFrame.getContentPane().add(new OwnerDashboard(parentFrame));
            parentFrame.revalidate();
            parentFrame.repaint();
        }
    }
}
