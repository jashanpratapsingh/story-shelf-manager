
package com.bookstore.view;

import com.bookstore.controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * JavaFX view for displaying the cost after a purchase.
 */
public class CustomerCostView extends VBox {
    
    private Stage primaryStage;
    private AuthController authController;
    private double totalCost;
    private int points;
    
    /**
     * Constructor.
     * 
     * @param primaryStage The primary stage for this application
     * @param totalCost The total cost of the purchase
     * @param points The updated points after the purchase
     */
    public CustomerCostView(Stage primaryStage, double totalCost, int points) {
        this.primaryStage = primaryStage;
        this.authController = new AuthController();
        this.totalCost = totalCost;
        this.points = points;
        initializeComponents();
    }
    
    /**
     * Initializes the components of the customer cost view.
     */
    private void initializeComponents() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setPadding(new Insets(50));
        
        // Create total cost label
        Label costLabel = new Label("Total Cost: $" + String.format("%.2f", totalCost));
        costLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Create points and status label
        String status = points >= 1000 ? "Gold" : "Silver";
        Label pointsLabel = new Label("Points: " + points + ", Status: " + status);
        pointsLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        
        // Create logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> handleLogout());
        
        // Add components to the layout
        this.getChildren().addAll(costLabel, pointsLabel, logoutButton);
    }
    
    /**
     * Handles the logout button click.
     */
    private void handleLogout() {
        authController.logout();
        
        // Navigate back to login screen
        primaryStage.setScene(new Scene(new LoginView(primaryStage),
                primaryStage.getWidth(), primaryStage.getHeight()));
        primaryStage.setTitle("BookStore Management System");
    }
    
    /**
     * Shows an alert dialog with the given parameters.
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
