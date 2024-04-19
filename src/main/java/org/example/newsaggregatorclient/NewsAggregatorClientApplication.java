package org.example.newsaggregatorclient;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.example.newsaggregatorclient.mediator_objects.ConnectionChecker;
import org.example.newsaggregatorclient.mediator_objects.DataLoaderFromJSON;
import org.example.newsaggregatorclient.mediator_objects.NewsItemData;
import org.example.newsaggregatorclient.ui_component.NewsCategoryGroupTitledPane;
import org.example.newsaggregatorclient.ui_component.NewsItem;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class NewsAggregatorClientApplication extends Application {
    private NewsAggregatorClientController controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(NewsAggregatorClientApplication.class.getResource("news_aggregator_client.fxml"));
        controller = new NewsAggregatorClientController(this.getHostServices());
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
//        scene.getStylesheets().add("src/main/resources/assets/css/main.css");
        ConnectionChecker connectionChecker = new ConnectionChecker();
        if (!connectionChecker.checkInternetConnection()) {
            Dialog dialog = new Dialog();
            ButtonType close = new ButtonType("Close (and check your Internet connection)", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.setHeaderText("No Internet connection");
            dialog.setContentText("This app cannot work without Internet. Please check your Internet connection, then re-open the app");
            dialog.getDialogPane().getButtonTypes().add(close);
            dialog.showAndWait();
        } else {
            stage.setTitle("Crypto News Aggregator Client");
            stage.setScene(scene);
            stage.show();
            controller.start();
            controller.showAllNewsCategories();
        }
    }
    public static void main(String[] args) {
        launch();
    }

}