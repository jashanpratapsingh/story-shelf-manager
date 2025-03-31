
package com.bookstore.view;

import com.bookstore.controller.AuthController;
import com.bookstore.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * JavaFX view for the login screen.
 */
public class LoginView extends VBox {
    
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private AuthController authController;
    private Stage primaryStage;
    
    /**
     * Constructor.
     * 
     * @param primaryStage The primary stage for this application
     */
    public LoginView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.authController = new AuthController();
        initializeComponents();
    }
    
    /**
     * Initializes the components of the login view.
     */
    private void initializeComponents() {
        this.setSpacing(20);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);
        
        // Create title
        Text titleText = new Text("Welcome to the BookStore App");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        // Create form grid
        GridPane formGrid = new GridPane();
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(20));
        
        // Add username field
        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        formGrid.add(usernameLabel, 0, 0);
        formGrid.add(usernameField, 1, 0);
        
        // Add password field
        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        formGrid.add(passwordLabel, 0, 1);
        formGrid.add(passwordField, 1, 1);
        
        // Add login button
        loginButton = new Button("Login");
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().add(loginButton);
        formGrid.add(buttonBox, 1, 2);
        
        // Add event handler for login button
        loginButton.setOnAction(e -> handleLogin());
        
        // Add components to the main layout
        this.getChildren().addAll(titleText, formGrid);
    }
    
    /**
     * Handles the login button click.
     */
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Error", 
                    "Please enter both username and password");
            return;
        }
        
        boolean loginSuccessful = authController.login(username, password);
        
        if (loginSuccessful) {
            navigateToNextScreen();
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Error", 
                    "Invalid username or password");
        }
    }
    
    /**
     * Navigates to the next screen based on the user's role.
     */
    private void navigateToNextScreen() {
        User currentUser = authController.getCurrentUser();
        if (currentUser == null) {
            System.out.println("Current user is null. Cannot navigate.");
            return;
        }
        
        // Navigate to the appropriate dashboard based on user role
        if (currentUser.getRole() == User.UserRole.OWNER) {
            OwnerDashboardView ownerDashboard = new OwnerDashboardView(primaryStage);
            primaryStage.setScene(new Scene(ownerDashboard, primaryStage.getWidth(), primaryStage.getHeight()));
            primaryStage.setTitle("BookStore - Owner Dashboard");
        } else {
            CustomerDashboardView customerDashboard = new CustomerDashboardView(primaryStage);
            primaryStage.setScene(new Scene(customerDashboard, primaryStage.getWidth(), primaryStage.getHeight()));
            primaryStage.setTitle("BookStore - Customer Dashboard");
        }
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
