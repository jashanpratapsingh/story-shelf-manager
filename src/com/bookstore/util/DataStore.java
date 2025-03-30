
package com.bookstore.util;

import com.bookstore.model.Book;
import com.bookstore.model.Customer;
import com.bookstore.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages data storage and retrieval for the bookstore application.
 */
public class DataStore {
    private List<Book> books;
    private List<Customer> customers;
    private User currentUser;
    private String currentScreen;

    // Singleton instance
    private static DataStore instance;

    /**
     * Private constructor for singleton pattern.
     */
    private DataStore() {
        this.books = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.currentUser = null;
        this.currentScreen = "login";
    }

    /**
     * Gets the singleton instance of DataStore.
     */
    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    /**
     * Loads data from files.
     */
    public void loadData() {
        FileIO fileIO = new FileIO();
        this.books = fileIO.loadBooks();
        this.customers = fileIO.loadCustomers();
    }

    /**
     * Saves data to files.
     */
    public void saveData() {
        FileIO fileIO = new FileIO();
        fileIO.saveBooks(this.books);
        fileIO.saveCustomers(this.customers);
    }

    /**
     * Authenticates a user.
     */
    public User authenticateUser(String username, String password) {
        // Check for admin credentials
        if (username.equals("admin") && password.equals("admin")) {
            return new User(username, password, User.UserRole.OWNER);
        }
        
        // Check for customer credentials
        for (Customer customer : customers) {
            if (customer.getUsername().equals(username) && 
                customer.getPassword().equals(password)) {
                return new User(username, password, User.UserRole.CUSTOMER);
            }
        }
        
        return null;
    }

    // Getters and setters
    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public String getCurrentScreen() {
        return currentScreen;
    }

    public void setCurrentScreen(String currentScreen) {
        this.currentScreen = currentScreen;
    }
}
