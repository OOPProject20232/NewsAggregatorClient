package org.newsaggregator.newsaggregatorclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.newsaggregator.newsaggregatorclient.checkers.ConnectionChecker;
import org.newsaggregator.newsaggregatorclient.downloaders.PeriodicNewsRetriever;

import java.io.IOException;

public class NewsAggregatorClientApplication extends Application {
    private NewsAggregatorClientController controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(NewsAggregatorClientApplication.class.getResource("news_aggregator_client.fxml"));
        controller = new NewsAggregatorClientController(this.getHostServices());
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
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
            PeriodicNewsRetriever periodicNewsRetriever = new PeriodicNewsRetriever();
            periodicNewsRetriever.startRetrieving();
            controller.showAllNewsCategories();
        }
    }
    public static void main(String[] args) {
        launch();
    }
}