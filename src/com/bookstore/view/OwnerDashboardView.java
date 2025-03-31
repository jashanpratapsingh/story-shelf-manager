
package com.bookstore.view;

import com.bookstore.controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * JavaFX view for the owner dashboard.
 */
public class OwnerDashboardView extends BorderPane {
    
    private Stage primaryStage;
    private AuthController authController;
    
    /**
     * Constructor.
     * 
     * @param primaryStage The primary stage for this application
     */
    public OwnerDashboardView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.authController = new AuthController();
        initializeComponents();
    }
    
    /**
     * Initializes the components of the owner dashboard view.
     */
    private void initializeComponents() {
        this.setPadding(new Insets(20));
        
        // Create header
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setSpacing(20);
        
        Label titleLabel = new Label("BookStore Admin Dashboard");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> handleLogout());
        
        headerBox.getChildren().addAll(titleLabel, logoutButton);
        
        // Create content with buttons
        VBox contentBox = new VBox();
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setSpacing(20);
        contentBox.setPadding(new Insets(50));
        
        Button booksButton = new Button("Books");
        booksButton.setPrefWidth(200);
        booksButton.setOnAction(e -> navigateTo("books"));
        
        Button customersButton = new Button("Customers");
        customersButton.setPrefWidth(200);
        customersButton.setOnAction(e -> navigateTo("customers"));
        
        contentBox.getChildren().addAll(booksButton, customersButton);
        
        // Add components to the border pane
        this.setTop(headerBox);
        this.setCenter(contentBox);
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
     * Navigates to the specified screen.
     * 
     * @param screen The screen to navigate to
     */
    private void navigateTo(String screen) {
        switch (screen) {
            case "books":
                primaryStage.setScene(new Scene(new OwnerBooksView(primaryStage), 
                        primaryStage.getWidth(), primaryStage.getHeight()));
                primaryStage.setTitle("BookStore - Manage Books");
                break;
            case "customers":
                primaryStage.setScene(new Scene(new OwnerCustomersView(primaryStage), 
                        primaryStage.getWidth(), primaryStage.getHeight()));
                primaryStage.setTitle("BookStore - Manage Customers");
                break;
            default:
                primaryStage.setScene(new Scene(new OwnerDashboardView(primaryStage), 
                        primaryStage.getWidth(), primaryStage.getHeight()));
                primaryStage.setTitle("BookStore - Owner Dashboard");
                break;
        }
    }
}
