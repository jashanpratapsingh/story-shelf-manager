
package com.bookstore.view;

import com.bookstore.model.Book;
import com.bookstore.util.DataStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;

/**
 * JavaFX view for managing books in the bookstore.
 */
public class OwnerBooksView extends BorderPane {
    
    private Stage primaryStage;
    private TableView<Book> booksTable;
    private ObservableList<Book> booksData;
    private TextField nameField;
    private TextField priceField;
    
    /**
     * Constructor.
     * 
     * @param primaryStage The primary stage for this application
     */
    public OwnerBooksView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeComponents();
        loadBooksData();
    }
    
    /**
     * Initializes the components of the owner books view.
     */
    private void initializeComponents() {
        this.setPadding(new Insets(20));
        
        // Create table
        booksTable = new TableView<>();
        booksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Create title column
        TableColumn<Book, String> titleColumn = new TableColumn<>("Book Name");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        
        // Create price column
        TableColumn<Book, Double> priceColumn = new TableColumn<>("Book Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        booksTable.getColumns().addAll(titleColumn, priceColumn);
        
        // Create form for adding books
        GridPane formGrid = new GridPane();
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(20));
        
        Label nameLabel = new Label("Name:");
        nameField = new TextField();
        formGrid.add(nameLabel, 0, 0);
        formGrid.add(nameField, 1, 0);
        
        Label priceLabel = new Label("Price:");
        priceField = new TextField();
        formGrid.add(priceLabel, 0, 1);
        formGrid.add(priceField, 1, 1);
        
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> handleAddBook());
        formGrid.add(addButton, 1, 2);
        
        // Create buttons for bottom
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);
        buttonBox.setPadding(new Insets(10));
        
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> handleDeleteBook());
        
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> navigateBack());
        
        buttonBox.getChildren().addAll(deleteButton, backButton);
        
        // Add components to the border pane
        this.setTop(booksTable);
        this.setCenter(formGrid);
        this.setBottom(buttonBox);
    }
    
    /**
     * Loads books data from the data store into the table.
     */
    private void loadBooksData() {
        // Get books from data store
        List<Book> books = DataStore.getInstance().getBooks();
        
        // Convert to observable list and set in table
        booksData = FXCollections.observableArrayList(books);
        booksTable.setItems(booksData);
    }
    
    /**
     * Handles adding a new book.
     */
    private void handleAddBook() {
        String name = nameField.getText().trim();
        String priceText = priceField.getText().trim();
        
        if (name.isEmpty() || priceText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", 
                    "Please enter both name and price");
            return;
        }
        
        double price;
        try {
            price = Double.parseDouble(priceText);
            if (price <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", 
                    "Please enter a valid positive price");
            return;
        }
        
        // Check if book with same name already exists
        for (Book book : booksData) {
            if (book.getTitle().equalsIgnoreCase(name)) {
                showAlert(Alert.AlertType.ERROR, "Input Error", 
                        "A book with this name already exists");
                return;
            }
        }
        
        // Create and add the new book
        Book newBook = new Book();
        newBook.setTitle(name);
        newBook.setPrice(price);
        newBook.setAuthor(""); // Not required as per the specification
        newBook.setQuantity(1); // As per the specification, only one copy is allowed
        
        // Add to data store
        DataStore.getInstance().getBooks().add(newBook);
        
        // Add to table
        booksData.add(newBook);
        
        // Clear input fields
        nameField.clear();
        priceField.clear();
        
        showAlert(Alert.AlertType.INFORMATION, "Success", 
                "Book added successfully");
    }
    
    /**
     * Handles deleting a book.
     */
    private void handleDeleteBook() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        
        if (selectedBook == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", 
                    "Please select a book to delete");
            return;
        }
        
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Are you sure you want to delete the book: " + 
                selectedBook.getTitle() + "?");
        
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Remove from data store
                DataStore.getInstance().getBooks().remove(selectedBook);
                
                // Remove from table
                booksData.remove(selectedBook);
                
                showAlert(Alert.AlertType.INFORMATION, "Success", 
                        "Book deleted successfully");
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
