package com.ryhma6.maven.steambeater;
import java.io.IOException;

import org.expressme.openid.Association;
import org.expressme.openid.Endpoint;
import org.expressme.openid.OpenIdManager;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.stage.Modality;
import javafx.scene.web.WebView;
import javafx.scene.layout.VBox;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Steambeater");

        initRootLayout();

        showPersonOverview();
    }
    
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            ListView<String> list = new ListView<String>();
            ObservableList<String> items =FXCollections.observableArrayList (
                "Single", "Double", "Suite", "Family App");
            list.setItems(items);
            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showPersonOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/TestView.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            
            // Set person overview into the center of root layout.
            rootLayout.setCenter(personOverview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @FXML protected void handleBrowserButtonAction(ActionEvent event) {
        
        Label secondLabel = new Label("Steam OpenID login");
 
        //StackPane secondaryLayout = new StackPane();
        //secondaryLayout.getChildren().add(secondLabel);
        //Scene secondScene = new Scene(secondaryLayout, 230, 100);

        
        WebView webView = new WebView();
        webView.getEngine().load("http://google.com");
        VBox vBox = new VBox(webView);
        Scene secondScene = new Scene(vBox, 960, 600);


        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Second Stage");
        newWindow.setScene(secondScene);

        // Specifies the modality for new window.
        newWindow.initModality(Modality.WINDOW_MODAL);

        // Specifies the owner Window (parent) for new window
        Node source = (Node) event.getSource();
        Stage parentStage = (Stage)source.getScene().getWindow();
        newWindow.initOwner(parentStage);

        // Set position of second window, related to primary window.
        newWindow.setX(parentStage.getX() + 200);
        newWindow.setY(parentStage.getY() + 100);

        newWindow.show();
        /*
        OpenIdManager manager = new OpenIdManager(); 
        manager.setReturnTo("http://www.openid-example.com/openId"); 
        manager.setRealm("http://*.openid-example.com"); 
        Endpoint endpoint = manager.lookupEndpoint("Google"); 
        Association association = manager.lookupAssociation(endpoint);
        String url = manager.getAuthenticationUrl(endpoint, association); 
        System.out.println("Copy the authentication URL in browser:\n" + url); 
        */
    }
}