package org.newsaggregator.newsaggregatorclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import org.newsaggregator.newsaggregatorclient.checkers.ConnectionChecker;
import org.newsaggregator.newsaggregatorclient.downloaders.CoinPriceRetriever;
import org.newsaggregator.newsaggregatorclient.downloaders.NewsRetriever;
import org.newsaggregator.newsaggregatorclient.downloaders.NewsRetrieverByCategory;
import org.newsaggregator.newsaggregatorclient.downloaders.PeriodicNewsRetriever;
import org.newsaggregator.newsaggregatorclient.ui_component.dialogs.NoInternetDialog;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class NewsAggregatorClientApplication extends Application {
    private NewsAggregatorClientController controller;

    @Override
    public void init() throws Exception {
        CompletableFuture.runAsync(() -> {
            System.out.println("Starting NewsRetriever");
            NewsRetriever newsRetriever = new NewsRetriever();
            newsRetriever.setForceDownload(true);
            newsRetriever.setLimit(50);
            newsRetriever.setPageNumber(1);
            int response;
            try {
                response = newsRetriever.sendRequest("articles", true, "src/main/resources/json/news.json");
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("NewsRetriever response: " + response);
        });
        CompletableFuture.runAsync(() -> {
            CoinPriceRetriever coinPriceRetriever = new CoinPriceRetriever();
            int response;
            try {
                response = coinPriceRetriever.sendRequest("coins", false, "coins.json");
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("CoinPriceRetriever response: " + response);
        });

    }

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("\u001B[33m"+"Starting application"+ "\u001B[0m");
        FXMLLoader fxmlLoader = new FXMLLoader(NewsAggregatorClientApplication.class.getResource("news_aggregator_client.fxml"));
        controller = new NewsAggregatorClientController(this.getHostServices());
        ConnectionChecker connectionChecker = new ConnectionChecker();
        if (!connectionChecker.checkInternetConnection()) {
            NoInternetDialog dialog = new NoInternetDialog();
            dialog.showAndWait();
        } else {
            fxmlLoader.setController(controller);
            Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
            stage.setTitle("Crypto News Aggregator Client");
            stage.getIcons().add(new Image(Objects.requireNonNull(NewsAggregatorClientApplication.class.getResourceAsStream("assets/images/icon.png"))));
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