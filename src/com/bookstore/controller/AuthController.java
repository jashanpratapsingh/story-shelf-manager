
package com.bookstore.controller;

import com.bookstore.model.User;
import com.bookstore.util.DataStore;

/**
 * Controller for authentication-related operations.
 */
public class AuthController {
    private DataStore dataStore;
    
    /**
     * Constructor.
     */
    public AuthController() {
        this.dataStore = DataStore.getInstance();
    }
    
    /**
     * Authenticates a user and updates the current user in the data store.
     * 
     * @param username The username to authenticate
     * @param password The password to authenticate
     * @return true if authentication was successful, false otherwise
     */
    public boolean login(String username, String password) {
        User authenticatedUser = dataStore.authenticateUser(username, password);
        
        if (authenticatedUser != null) {
            dataStore.setCurrentUser(authenticatedUser);
            
            // Set appropriate screen based on user role
            if (authenticatedUser.getRole() == User.UserRole.OWNER) {
                dataStore.setCurrentScreen("owner-dashboard");
            } else {
                dataStore.setCurrentScreen("customer-dashboard");
            }
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Logs out the current user.
     */
    public void logout() {
        dataStore.setCurrentUser(null);
        dataStore.setCurrentScreen("login");
    }
    
    /**
     * Gets the current user.
     */
    public User getCurrentUser() {
        return dataStore.getCurrentUser();
    }
    
    /**
     * Checks if the current user is an owner.
     */
    public boolean isOwner() {
        User currentUser = dataStore.getCurrentUser();
        return currentUser != null && currentUser.getRole() == User.UserRole.OWNER;
    }
    
    /**
     * Checks if the current user is a customer.
     */
    public boolean isCustomer() {
        User currentUser = dataStore.getCurrentUser();
        return currentUser != null && currentUser.getRole() == User.UserRole.CUSTOMER;
    }
}
