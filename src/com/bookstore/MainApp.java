
package com.bookstore;

import com.bookstore.util.DataStore;
import com.bookstore.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main entry point for the JavaFX bookstore application.
 */
public class MainApp extends Application {
    
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    
    /**
     * Main method that launches the JavaFX application.
     */
    public static void main(String[] args) {
        // Initialize data store
        DataStore dataStore = DataStore.getInstance();
        dataStore.loadData();
        
        // Register shutdown hook to save data when the application closes
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Saving data...");
            dataStore.saveData();
        }));
        
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("BookStore Management System");
        
        // Set initial view to login screen
        LoginView loginView = new LoginView(primaryStage);
        Scene scene = new Scene(loginView, WIDTH, HEIGHT);
        
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }
    
    @Override
    public void stop() {
        // Ensure data is saved when application closes
        DataStore.getInstance().saveData();
    }
}
