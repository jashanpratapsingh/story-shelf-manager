
package com.bookstore.view;

import com.bookstore.model.Book;
import com.bookstore.util.DataStore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Screen for managing books in the bookstore.
 */
public class OwnerBooksScreen extends JPanel {
    private JFrame parentFrame;
    private DefaultTableModel tableModel;
    private JTable booksTable;
    private JTextField nameField;
    private JTextField priceField;
    
    /**
     * Constructor.
     * 
     * @param parentFrame The parent JFrame for navigation
     */
    public OwnerBooksScreen(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        initializeComponents();
        loadBooksData();
    }
    
    /**
     * Initializes the components of the books management screen.
     */
    private void initializeComponents() {
        setLayout(new BorderLayout());
        
        // Top part - Books table
        tableModel = new DefaultTableModel(new Object[]{"Book Name", "Book Price"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        booksTable = new JTable(tableModel);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScrollPane = new JScrollPane(booksTable);
        
        // Middle part - Add book form
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);
        
        JLabel priceLabel = new JLabel("Price:");
        priceField = new JTextField(20);
        
        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> handleAddBook());
        
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(priceLabel);
        formPanel.add(priceField);
        formPanel.add(new JLabel()); // Empty space
        formPanel.add(addButton);
        
        // Bottom part - Delete and Back buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> handleDeleteBook());
        
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
     * Loads books data from the data store into the table.
     */
    private void loadBooksData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get books from data store
        List<Book> books = DataStore.getInstance().getBooks();
        
        // Add books to table
        for (Book book : books) {
            tableModel.addRow(new Object[]{book.getTitle(), book.getPrice()});
        }
    }
    
    /**
     * Handles adding a new book.
     */
    private void handleAddBook() {
        String name = nameField.getText().trim();
        String priceText = priceField.getText().trim();
        
        if (name.isEmpty() || priceText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter both name and price",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double price;
        try {
            price = Double.parseDouble(priceText);
            if (price <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid positive price",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if book with same name already exists
        List<Book> books = DataStore.getInstance().getBooks();
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(name)) {
                JOptionPane.showMessageDialog(this,
                        "A book with this name already exists",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        // Create and add the new book
        Book newBook = new Book();
        newBook.setTitle(name);
        newBook.setPrice(price);
        newBook.setAuthor(""); // Not required as per the specification
        newBook.setQuantity(1); // As per the specification, only one copy is allowed
        
        // Add to data store
        books.add(newBook);
        
        // Update table
        tableModel.addRow(new Object[]{name, price});
        
        // Clear input fields
        nameField.setText("");
        priceField.setText("");
        
        JOptionPane.showMessageDialog(this,
                "Book added successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Handles deleting a book.
     */
    private void handleDeleteBook() {
        int selectedRow = booksTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a book to delete",
                    "Selection Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String bookName = (String) tableModel.getValueAt(selectedRow, 0);
        
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the book: " + bookName + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);
        
        if (confirmation == JOptionPane.YES_OPTION) {
            // Remove from data store
            List<Book> books = DataStore.getInstance().getBooks();
            for (int i = 0; i < books.size(); i++) {
                if (books.get(i).getTitle().equals(bookName)) {
                    books.remove(i);
                    break;
                }
            }
            
            // Remove from table
            tableModel.removeRow(selectedRow);
            
            JOptionPane.showMessageDialog(this,
                    "Book deleted successfully",
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
