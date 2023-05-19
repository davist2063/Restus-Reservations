package service;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.dao.Database;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;
    private static String currentUser = "";
    private static String currentPage = "";
    private static Object data;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        if (Database.isOK()) {
            // Parent root = FXMLLoader.load(getClass().getResource("/view/userView.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/view/loginPage.fxml"));
            primaryStage.setTitle("Restus");
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.centerOnScreen();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            App.primaryStage = primaryStage;
        }
        else {
            System.out.println("There was an error loading the database");
            Platform.exit();
        }
        
    }

    public static void setRoot(String fxml) throws IOException {
        setData(null);
        App.primaryStage.setScene(new Scene(App.loadFXML(fxml)));
        App.primaryStage.centerOnScreen();
    }
    
    public static void setRoot(String fxml, Object data) throws IOException {
        setData(data);
        App.primaryStage.setScene(new Scene(App.loadFXML(fxml)));
        App.primaryStage.centerOnScreen();
    }

    public static void setRoot(String fxml, String page) throws IOException {
        setPage(page);
        App.primaryStage.setScene(new Scene(App.loadFXML(fxml)));
        App.primaryStage.centerOnScreen();
    }

    private static void setPage(String newPage) {
        currentPage = newPage;
    }
    public static String getPage() {
        return currentPage;
    }

    private static void setData(Object newData) {
        data = newData;
    }
    public static Object getData() {
        return data;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void setUser(String newUser) {
        currentUser = newUser;
    }

    public static String getUser() {
        return currentUser;
    }
}