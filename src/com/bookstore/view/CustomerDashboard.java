
package com.bookstore.view;

import com.bookstore.controller.AuthController;
import com.bookstore.model.Book;
import com.bookstore.model.Customer;
import com.bookstore.model.Purchase;
import com.bookstore.util.DataStore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Dashboard for customers.
 */
public class CustomerDashboard extends JPanel {
    private JFrame parentFrame;
    private AuthController authController;
    private Customer currentCustomer;
    private DefaultTableModel tableModel;
    private JTable booksTable;
    private List<Book> availableBooks;
    private List<Boolean> selectedBooks;
    
    /**
     * Constructor.
     * 
     * @param parentFrame The parent JFrame for navigation
     */
    public CustomerDashboard(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.authController = new AuthController();
        initializeCustomer();
        initializeComponents();
        loadBooksData();
    }
    
    /**
     * Initializes the current customer from the authenticated user.
     */
    private void initializeCustomer() {
        if (authController.getCurrentUser() != null) {
            String username = authController.getCurrentUser().getUsername();
            for (Customer customer : DataStore.getInstance().getCustomers()) {
                if (customer.getUsername().equals(username)) {
                    this.currentCustomer = customer;
                    break;
                }
            }
        }
    }
    
    /**
     * Initializes the components of the customer dashboard.
     */
    private void initializeComponents() {
        setLayout(new BorderLayout());
        
        // Top part - Welcome message with points and status
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        int points = getCustomerPoints();
        String status = getCustomerStatus(points);
        
        JLabel welcomeLabel = new JLabel(
            "Welcome " + (currentCustomer != null ? currentCustomer.getName() : "Customer") + 
            ". You have " + points + " points. Your status is " + status
        );
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(welcomeLabel);
        
        // Middle part - Books table with checkboxes
        String[] columnNames = {"Book Name", "Book Price", "Select"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Boolean.class : String.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Only checkbox column is editable
            }
        };
        
        booksTable = new JTable(tableModel);
        booksTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        booksTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        booksTable.getColumnModel().getColumn(2).setPreferredWidth(50);
        
        JScrollPane tableScrollPane = new JScrollPane(booksTable);
        
        // Bottom part - Buy, Redeem and Logout buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton buyButton = new JButton("Buy");
        buyButton.addActionListener(e -> handleBuy(false));
        
        JButton redeemAndBuyButton = new JButton("Redeem points and Buy");
        redeemAndBuyButton.addActionListener(e -> handleBuy(true));
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> handleLogout());
        
        buttonPanel.add(buyButton);
        buttonPanel.add(redeemAndBuyButton);
        buttonPanel.add(logoutButton);
        
        // Add all parts to the main panel
        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Gets the current points for the customer.
     */
    private int getCustomerPoints() {
        if (currentCustomer == null || currentCustomer.getPurchaseHistory() == null) {
            return 0;
        }
        
        int totalPoints = 0;
        for (Purchase purchase : currentCustomer.getPurchaseHistory()) {
            totalPoints += (int)(purchase.getPrice() * 10); // 10 points per 1 CAD
        }
        
        return totalPoints;
    }
    
    /**
     * Gets the customer status based on points.
     */
    private String getCustomerStatus(int points) {
        return points >= 1000 ? "Gold" : "Silver";
    }
    
    /**
     * Loads books data from the data store into the table.
     */
    private void loadBooksData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get books from data store
        availableBooks = DataStore.getInstance().getBooks();
        selectedBooks = new ArrayList<>(availableBooks.size());
        
        // Add books to table
        for (Book book : availableBooks) {
            tableModel.addRow(new Object[]{book.getTitle(), book.getPrice(), false});
            selectedBooks.add(false);
        }
        
        // Add listener to update selectedBooks array when checkboxes are changed
        tableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            
            if (column == 2 && row >= 0 && row < selectedBooks.size()) {
                Boolean isSelected = (Boolean) tableModel.getValueAt(row, column);
                selectedBooks.set(row, isSelected);
            }
        });
    }
    
    /**
     * Handles the buy or redeem and buy action.
     */
    private void handleBuy(boolean isRedeem) {
        List<Book> booksToBuy = new ArrayList<>();
        double totalCost = 0;
        
        // Collect selected books
        for (int i = 0; i < selectedBooks.size(); i++) {
            if (selectedBooks.get(i) && i < availableBooks.size()) {
                booksToBuy.add(availableBooks.get(i));
                totalCost += availableBooks.get(i).getPrice();
            }
        }
        
        if (booksToBuy.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please select at least one book to buy",
                    "Selection Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Process the purchase
        int currentPoints = getCustomerPoints();
        double finalCost = totalCost;
        
        // Apply points redemption if requested
        if (isRedeem && currentPoints > 0) {
            // Calculate how much can be redeemed (1 CAD per 100 points)
            double redeemAmount = Math.min(currentPoints / 100.0, totalCost);
            finalCost = Math.max(0, totalCost - redeemAmount);
            
            // Clear all points if redemption is applied
            currentPoints = 0;
        }
        
        // Add points earned from this purchase
        int pointsEarned = (int) (finalCost * 10);
        currentPoints += pointsEarned;
        
        // Create purchase records
        if (currentCustomer != null) {
            for (Book book : booksToBuy) {
                Purchase purchase = new Purchase();
                purchase.setId(UUID.randomUUID().toString());
                purchase.setBookId(book.getId());
                purchase.setBookTitle(book.getTitle());
                purchase.setPrice(book.getPrice());
                purchase.setQuantity(1);
                purchase.setDate(new Date().toString());
                
                currentCustomer.addPurchase(purchase);
            }
        }
        
        // Navigate to cost screen
        if (parentFrame != null) {
            parentFrame.getContentPane().removeAll();
            parentFrame.getContentPane().add(
                new CustomerCostScreen(parentFrame, finalCost, currentPoints)
            );
            parentFrame.setTitle("BookStore - Purchase Complete");
            parentFrame.revalidate();
            parentFrame.repaint();
        }
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
