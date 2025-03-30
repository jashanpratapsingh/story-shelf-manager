
package com.bookstore.model;

import java.util.UUID;

/**
 * Represents a purchase made by a customer.
 */
public class Purchase {
    private String id;
    private String bookId;
    private String bookTitle;
    private int quantity;
    private double totalPrice;
    private String date;

    /**
     * Default constructor for creating a new purchase with a random ID.
     */
    public Purchase() {
        this.id = UUID.randomUUID().toString();
    }

    /**
     * Constructor for creating a purchase with all fields.
     */
    public Purchase(String id, String bookId, String bookTitle, int quantity, double totalPrice, String date) {
        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.date = date;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id='" + id + '\'' +
                ", bookId='" + bookId + '\'' +
                ", bookTitle='" + bookTitle + '\'' +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", date='" + date + '\'' +
                '}';
    }
}
