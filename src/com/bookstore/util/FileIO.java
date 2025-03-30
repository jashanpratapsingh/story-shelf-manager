
package com.bookstore.util;

import com.bookstore.model.Book;
import com.bookstore.model.Customer;
import com.bookstore.model.Purchase;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles file input/output operations for the bookstore application.
 */
public class FileIO {
    private static final String BOOKS_FILE = "data/books.txt";
    private static final String CUSTOMERS_FILE = "data/customers.txt";

    /**
     * Loads books from file.
     */
    public List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String id = parts[0];
                    String title = parts[1];
                    String author = parts[2];
                    double price = Double.parseDouble(parts[3]);
                    int quantity = Integer.parseInt(parts[4]);
                    
                    books.add(new Book(id, title, author, price, quantity));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading books: " + e.getMessage());
        }
        
        return books;
    }

    /**
     * Saves books to file.
     */
    public void saveBooks(List<Book> books) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
            for (Book book : books) {
                writer.write(String.format("%s,%s,%s,%.2f,%d\n",
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPrice(),
                        book.getQuantity()));
            }
        } catch (IOException e) {
            System.err.println("Error saving books: " + e.getMessage());
        }
    }

    /**
     * Loads customers from file.
     */
    public List<Customer> loadCustomers() {
        List<Customer> customers = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMERS_FILE))) {
            String line;
            Customer currentCustomer = null;
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("CUSTOMER:")) {
                    if (currentCustomer != null) {
                        customers.add(currentCustomer);
                    }
                    
                    String[] parts = line.substring(9).split(",");
                    if (parts.length == 3) {
                        String id = parts[0];
                        String username = parts[1];
                        String name = parts[2];
                        
                        currentCustomer = new Customer();
                        currentCustomer.setId(id);
                        currentCustomer.setUsername(username);
                        currentCustomer.setName(name);
                    }
                } else if (line.startsWith("PASSWORD:") && currentCustomer != null) {
                    currentCustomer.setPassword(line.substring(9));
                } else if (line.startsWith("PURCHASE:") && currentCustomer != null) {
                    String[] parts = line.substring(9).split(",");
                    if (parts.length == 6) {
                        String id = parts[0];
                        String bookId = parts[1];
                        String bookTitle = parts[2];
                        int quantity = Integer.parseInt(parts[3]);
                        double totalPrice = Double.parseDouble(parts[4]);
                        String date = parts[5];
                        
                        Purchase purchase = new Purchase(id, bookId, bookTitle, quantity, totalPrice, date);
                        currentCustomer.addPurchase(purchase);
                    }
                }
            }
            
            if (currentCustomer != null) {
                customers.add(currentCustomer);
            }
        } catch (IOException e) {
            System.err.println("Error loading customers: " + e.getMessage());
        }
        
        return customers;
    }

    /**
     * Saves customers to file.
     */
    public void saveCustomers(List<Customer> customers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Customer customer : customers) {
                writer.write(String.format("CUSTOMER:%s,%s,%s\n",
                        customer.getId(),
                        customer.getUsername(),
                        customer.getName()));
                
                writer.write(String.format("PASSWORD:%s\n", customer.getPassword()));
                
                for (Purchase purchase : customer.getPurchaseHistory()) {
                    writer.write(String.format("PURCHASE:%s,%s,%s,%d,%.2f,%s\n",
                            purchase.getId(),
                            purchase.getBookId(),
                            purchase.getBookTitle(),
                            purchase.getQuantity(),
                            purchase.getTotalPrice(),
                            purchase.getDate()));
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving customers: " + e.getMessage());
        }
    }
}
