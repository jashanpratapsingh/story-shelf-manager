
package com.bookstore.model;

/**
 * Represents a user of the bookstore application.
 */
public class User {
    private String username;
    private String password;
    private UserRole role;

    /**
     * Enum representing the possible roles for a user.
     */
    public enum UserRole {
        OWNER,
        CUSTOMER
    }

    /**
     * Default constructor.
     */
    public User() {
    }

    /**
     * Constructor for creating a user with all fields.
     */
    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters and setters
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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", role=" + role +
                '}';
    }
}
