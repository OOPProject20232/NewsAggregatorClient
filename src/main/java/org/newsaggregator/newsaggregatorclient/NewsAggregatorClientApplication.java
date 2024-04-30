package org.newsaggregator.newsaggregatorclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import org.newsaggregator.newsaggregatorclient.checkers.ConnectionChecker;
import org.newsaggregator.newsaggregatorclient.downloaders.NewsRetriever;
import org.newsaggregator.newsaggregatorclient.downloaders.NewsRetrieverByCategory;
import org.newsaggregator.newsaggregatorclient.downloaders.PeriodicNewsRetriever;
import org.newsaggregator.newsaggregatorclient.ui_component.dialogs.NoInternetDialog;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

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
            NoInternetDialog dialog = new NoInternetDialog();
            dialog.showAndWait();
        } else {
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
        AtomicReference<NewsRetriever> newsRetriever = new AtomicReference<>();
        new Thread(() -> {
            newsRetriever.set(new NewsRetriever());
            newsRetriever.get().setForceDownload(true);
            newsRetriever.get().setLimit(50);
            newsRetriever.get().setPageNumber(1);
            try {
                newsRetriever.get().sendRequest("articles", true, "news.json");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }).start();
//        new Thread(() -> {
//            NewsRetriever newsRetriever = new NewsRetriever();
//            newsRetriever.setForceDownload(true);
//            try {
//                newsRetriever.sendRequest("v1/categories/articles?search=bitcoin", false, "bitcoin.json");
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }
//        }).start();
//        new Thread(() -> {
//            NewsRetriever newsRetriever = new NewsRetriever();
//            newsRetriever.setForceDownload(true);
//            try {
//                newsRetriever.sendRequest("v1/categories/articles?search=ethereum",false, "ethereum.json");
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }
//        }).start();

        launch();
    }
}