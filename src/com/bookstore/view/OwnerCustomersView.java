
package com.bookstore.view;

import com.bookstore.model.Customer;
import com.bookstore.util.DataStore;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * JavaFX view for managing customers in the bookstore.
 */
public class OwnerCustomersView extends BorderPane {
    
    private Stage primaryStage;
    private TableView<Customer> customersTable;
    private ObservableList<Customer> customersData;
    private TextField usernameField;
    private TextField passwordField;
    
    /**
     * Constructor.
     * 
     * @param primaryStage The primary stage for this application
     */
    public OwnerCustomersView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeComponents();
        loadCustomersData();
    }
    
    /**
     * Initializes the components of the owner customers view.
     */
    private void initializeComponents() {
        this.setPadding(new Insets(20));
        
        // Create table
        customersTable = new TableView<>();
        customersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Create username column
        TableColumn<Customer, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getUsername()));
        
        // Create password column
        TableColumn<Customer, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getPassword()));
        
        // Create points column
        TableColumn<Customer, Number> pointsColumn = new TableColumn<>("Points");
        pointsColumn.setCellValueFactory(cellData -> 
                new SimpleIntegerProperty(calculateTotalPoints(cellData.getValue())));
        
        customersTable.getColumns().addAll(usernameColumn, passwordColumn, pointsColumn);
        
        // Create form for adding customers
        GridPane formGrid = new GridPane();
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(20));
        
        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        formGrid.add(usernameLabel, 0, 0);
        formGrid.add(usernameField, 1, 0);
        
        Label passwordLabel = new Label("Password:");
        passwordField = new TextField();
        formGrid.add(passwordLabel, 0, 1);
        formGrid.add(passwordField, 1, 1);
        
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> handleAddCustomer());
        formGrid.add(addButton, 1, 2);
        
        // Create buttons for bottom
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);
        buttonBox.setPadding(new Insets(10));
        
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> handleDeleteCustomer());
        
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> navigateBack());
        
        buttonBox.getChildren().addAll(deleteButton, backButton);
        
        // Add components to the border pane
        this.setTop(customersTable);
        this.setCenter(formGrid);
        this.setBottom(buttonBox);
    }
    
    /**
     * Loads customers data from the data store into the table.
     */
    private void loadCustomersData() {
        // Get customers from data store
        List<Customer> customers = DataStore.getInstance().getCustomers();
        
        // Convert to observable list and set in table
        customersData = FXCollections.observableArrayList(customers);
        customersTable.setItems(customersData);
    }
    
    /**
     * Calculates the total points for a customer based on their purchase history.
     */
    private int calculateTotalPoints(Customer customer) {
        // For simplicity, we'll just return the count of purchases for now
        return customer.getPurchaseHistory() != null ? customer.getPurchaseHistory().size() * 100 : 0;
    }
    
    /**
     * Handles adding a new customer.
     */
    private void handleAddCustomer() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", 
                    "Please enter both username and password");
            return;
        }
        
        // Check if customer with same username already exists
        for (Customer customer : customersData) {
            if (customer.getUsername().equals(username)) {
                showAlert(Alert.AlertType.ERROR, "Input Error", 
                        "A customer with this username already exists");
                return;
            }
        }
        
        // Create and add the new customer
        Customer newCustomer = new Customer();
        newCustomer.setUsername(username);
        newCustomer.setPassword(password);
        newCustomer.setName(username); // Using username as name for simplicity
        newCustomer.setPurchaseHistory(new ArrayList<>());
        
        // Add to data store
        DataStore.getInstance().getCustomers().add(newCustomer);
        
        // Add to table
        customersData.add(newCustomer);
        
        // Clear input fields
        usernameField.clear();
        passwordField.clear();
        
        showAlert(Alert.AlertType.INFORMATION, "Success", 
                "Customer added successfully");
    }
    
    /**
     * Handles deleting a customer.
     */
    private void handleDeleteCustomer() {
        Customer selectedCustomer = customersTable.getSelectionModel().getSelectedItem();
        
        if (selectedCustomer == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", 
                    "Please select a customer to delete");
            return;
        }
        
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Are you sure you want to delete the customer: " + 
                selectedCustomer.getUsername() + "?");
        
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Remove from data store
                DataStore.getInstance().getCustomers().remove(selectedCustomer);
                
                // Remove from table
                customersData.remove(selectedCustomer);
                
                showAlert(Alert.AlertType.INFORMATION, "Success", 
                        "Customer deleted successfully");
            }
        });
    }
    
    /**
     * Navigates back to the owner dashboard.
     */
    private void navigateBack() {
        primaryStage.setScene(new Scene(new OwnerDashboardView(primaryStage), 
                primaryStage.getWidth(), primaryStage.getHeight()));
        primaryStage.setTitle("BookStore - Owner Dashboard");
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
