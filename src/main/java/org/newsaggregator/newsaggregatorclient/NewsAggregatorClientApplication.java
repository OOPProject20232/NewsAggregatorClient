package org.newsaggregator.newsaggregatorclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.newsaggregator.newsaggregatorclient.checkers.ConnectionChecker;
import org.newsaggregator.newsaggregatorclient.downloaders.NewsRetriever;
import org.newsaggregator.newsaggregatorclient.downloaders.PeriodicNewsRetriever;
import org.newsaggregator.newsaggregatorclient.ui_component.dialogs.NoInternetDialog;

import java.io.IOException;
import java.net.MalformedURLException;

public class NewsAggregatorClientApplication extends Application {
    private NewsAggregatorClientController controller;

    @Override
    public void start(Stage stage) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NewsRetriever newsRetriever = new NewsRetriever();
                try {
                    newsRetriever.sendRequest("articles", true, "news.json");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
//        NewsRetriever newsRetriever = new NewsRetriever();
//        newsRetriever.setForceDownload(true);
//        newsRetriever.setLimit(50);
//        int responseCode =  newsRetriever.sendRequest("articles", true, "news.json");
//        if (responseCode == 500) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText("Server Error");
//            alert.setContentText("Server is not available. Please try again later.");
//            alert.showAndWait();
//            return;
//        }
        FXMLLoader fxmlLoader = new FXMLLoader(NewsAggregatorClientApplication.class.getResource("news_aggregator_client.fxml"));
        controller = new NewsAggregatorClientController(this.getHostServices());
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        ConnectionChecker connectionChecker = new ConnectionChecker();
        if (!connectionChecker.checkInternetConnection()) {
            NoInternetDialog dialog = new NoInternetDialog();
            dialog.showAndWait();
        } else {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    NewsRetriever newsRetriever = new NewsRetriever();
//                    try {
//                        newsRetriever.sendRequest("articles", true, "news.json");
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
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