
package com.bookstore.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a customer of the bookstore.
 */
public class Customer {
    private String id;
    private String username;
    private String password;
    private String name;
    private List<Purchase> purchaseHistory;

    /**
     * Default constructor for creating a new customer with a random ID.
     */
    public Customer() {
        this.id = UUID.randomUUID().toString();
        this.purchaseHistory = new ArrayList<>();
    }

    /**
     * Constructor for creating a customer with all fields.
     */
    public Customer(String id, String username, String password, String name, List<Purchase> purchaseHistory) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.purchaseHistory = purchaseHistory != null ? purchaseHistory : new ArrayList<>();
    }

    /**
     * Adds a purchase to the customer's purchase history.
     */
    public void addPurchase(Purchase purchase) {
        if (this.purchaseHistory == null) {
            this.purchaseHistory = new ArrayList<>();
        }
        this.purchaseHistory.add(purchase);
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Purchase> getPurchaseHistory() {
        return purchaseHistory;
    }

    public void setPurchaseHistory(List<Purchase> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", purchaseHistory=" + purchaseHistory.size() + " items" +
                '}';
    }
}
