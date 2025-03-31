
package com.bookstore.view;

import com.bookstore.controller.AuthController;
import com.bookstore.model.Book;
import com.bookstore.model.Customer;
import com.bookstore.model.Purchase;
import com.bookstore.util.DataStore;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * JavaFX view for the customer dashboard.
 */
public class CustomerDashboardView extends BorderPane {
    
    private Stage primaryStage;
    private AuthController authController;
    private Customer currentCustomer;
    private TableView<BookSelectionModel> booksTable;
    private ObservableList<BookSelectionModel> booksData;
    
    /**
     * Constructor.
     * 
     * @param primaryStage The primary stage for this application
     */
    public CustomerDashboardView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.authController = new AuthController();
        initializeCustomer();
        initializeComponents();
        loadBooksData();
    }
    
    /**
     * Model class for book selection in table.
     */
    public static class BookSelectionModel {
        private Book book;
        private SimpleBooleanProperty selected;
        
        public BookSelectionModel(Book book) {
            this.book = book;
            this.selected = new SimpleBooleanProperty(false);
        }
        
        public Book getBook() {
            return book;
        }
        
        public String getTitle() {
            return book.getTitle();
        }
        
        public double getPrice() {
            return book.getPrice();
        }
        
        public boolean isSelected() {
            return selected.get();
        }
        
        public void setSelected(boolean selected) {
            this.selected.set(selected);
        }
        
        public SimpleBooleanProperty selectedProperty() {
            return selected;
        }
    }
    
    /**
     * Initializes the current customer from the authenticated user.
     */
    private void initializeCustomer() {
        if (authController.getCurrentUser() != null) {
            String username = authController.getCurrentUser().getUsername();
            for (Customer customer : DataStore.getInstance().getCustomers()) {
                if (customer.getUsername().equals(username)) {
                    this.currentCustomer = customer;
                    break;
                }
            }
        }
    }
    
    /**
     * Initializes the components of the customer dashboard view.
     */
    private void initializeComponents() {
        this.setPadding(new Insets(20));
        
        // Create welcome header
        VBox headerBox = new VBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(10));
        
        int points = getCustomerPoints();
        String status = getCustomerStatus(points);
        
        Label welcomeLabel = new Label(
            "Welcome " + (currentCustomer != null ? currentCustomer.getName() : "Customer") + 
            ". You have " + points + " points. Your status is " + status
        );
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        headerBox.getChildren().add(welcomeLabel);
        
        // Create table
        booksTable = new TableView<>();
        booksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Create title column
        TableColumn<BookSelectionModel, String> titleColumn = new TableColumn<>("Book Name");
        titleColumn.setCellValueFactory(cellData -> 
                new SimpleStringProperty(cellData.getValue().getTitle()));
        
        // Create price column
        TableColumn<BookSelectionModel, Number> priceColumn = new TableColumn<>("Book Price");
        priceColumn.setCellValueFactory(cellData -> 
                new SimpleDoubleProperty(cellData.getValue().getPrice()));
        
        // Create select column
        TableColumn<BookSelectionModel, Boolean> selectColumn = new TableColumn<>("Select");
        selectColumn.setCellValueFactory(cellData -> 
                cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        
        booksTable.getColumns().addAll(titleColumn, priceColumn, selectColumn);
        
        // Create buttons
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);
        buttonBox.setPadding(new Insets(10));
        
        Button buyButton = new Button("Buy");
        buyButton.setOnAction(e -> handleBuy(false));
        
        Button redeemAndBuyButton = new Button("Redeem points and Buy");
        redeemAndBuyButton.setOnAction(e -> handleBuy(true));
        
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> handleLogout());
        
        buttonBox.getChildren().addAll(buyButton, redeemAndBuyButton, logoutButton);
        
        // Add components to the border pane
        this.setTop(headerBox);
        this.setCenter(booksTable);
        this.setBottom(buttonBox);
    }
    
    /**
     * Gets the current points for the customer.
     */
    private int getCustomerPoints() {
        if (currentCustomer == null || currentCustomer.getPurchaseHistory() == null) {
            return 0;
        }
        
        int totalPoints = 0;
        for (Purchase purchase : currentCustomer.getPurchaseHistory()) {
            totalPoints += (int)(purchase.getPrice() * 10); // 10 points per 1 CAD
        }
        
        return totalPoints;
    }
    
    /**
     * Gets the customer status based on points.
     */
    private String getCustomerStatus(int points) {
        return points >= 1000 ? "Gold" : "Silver";
    }
    
    /**
     * Loads books data from the data store into the table.
     */
    private void loadBooksData() {
        // Get books from data store
        List<Book> books = DataStore.getInstance().getBooks();
        
        // Convert to BookSelectionModel list
        List<BookSelectionModel> bookModels = new ArrayList<>();
        for (Book book : books) {
            bookModels.add(new BookSelectionModel(book));
        }
        
        // Set in table
        booksData = FXCollections.observableArrayList(bookModels);
        booksTable.setItems(booksData);
    }
    
    /**
     * Handles the buy or redeem and buy action.
     */
    private void handleBuy(boolean isRedeem) {
        List<Book> booksToBuy = new ArrayList<>();
        double totalCost = 0;
        
        // Collect selected books
        for (BookSelectionModel model : booksData) {
            if (model.isSelected()) {
                booksToBuy.add(model.getBook());
                totalCost += model.getBook().getPrice();
            }
        }
        
        if (booksToBuy.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Selection Error",
                    "Please select at least one book to buy");
            return;
        }
        
        // Process the purchase
        int currentPoints = getCustomerPoints();
        double finalCost = totalCost;
        
        // Apply points redemption if requested
        if (isRedeem && currentPoints > 0) {
            // Calculate how much can be redeemed (1 CAD per 100 points)
            double redeemAmount = Math.min(currentPoints / 100.0, totalCost);
            finalCost = Math.max(0, totalCost - redeemAmount);
            
            // Clear all points if redemption is applied
            currentPoints = 0;
        }
        
        // Add points earned from this purchase
        int pointsEarned = (int) (finalCost * 10);
        currentPoints += pointsEarned;
        
        // Create purchase records
        if (currentCustomer != null) {
            for (Book book : booksToBuy) {
                Purchase purchase = new Purchase();
                purchase.setBookId(book.getId());
                purchase.setBookTitle(book.getTitle());
                purchase.setQuantity(1);
                purchase.setPrice(book.getPrice());
                purchase.setDate(new Date().toString());
                
                currentCustomer.addPurchase(purchase);
            }
        }
        
        // Navigate to cost screen
        primaryStage.setScene(new Scene(
                new CustomerCostView(primaryStage, finalCost, currentPoints),
                primaryStage.getWidth(), primaryStage.getHeight()));
        primaryStage.setTitle("BookStore - Purchase Complete");
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
