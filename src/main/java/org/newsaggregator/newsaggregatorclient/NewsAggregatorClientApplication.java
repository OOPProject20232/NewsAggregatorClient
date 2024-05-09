package org.newsaggregator.newsaggregatorclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import org.newsaggregator.newsaggregatorclient.checkers.ConnectionChecker;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.NoInternetDialog;

import java.io.IOException;
import java.util.Objects;

public class NewsAggregatorClientApplication extends Application {
    private ConnectionChecker connectionChecker = new ConnectionChecker();

//    @Override
//    public synchronized void init() throws Exception {
//        if (connectionChecker.checkInternetConnection()){
//                System.out.println("Starting NewsRetriever");
//                NewsRetriever newsRetriever = new NewsRetriever();
//                newsRetriever.setForceDownload(true);
//                newsRetriever.setLimit(50);
//                newsRetriever.setPageNumber(1);
//                int response;
//                try {
//                    response = newsRetriever.downloadCache( true, "news.json");
//                } catch (MalformedURLException e) {
//                    throw new RuntimeException(e);
//                }
//                System.out.println("NewsRetriever response: " + response);
//                CoinPriceRetriever coinPriceRetriever = new CoinPriceRetriever();
//                try {
//                    response = coinPriceRetriever.downloadCache(false, "coins.json");
//                } catch (MalformedURLException e) {
//                    throw new RuntimeException(e);
//                }
//                System.out.println("CoinPriceRetriever response: " + response);
//        }
//    }

    @Override
    public synchronized void start(Stage stage) throws IOException {
        System.out.println("\u001B[33m"+"Starting application"+ "\u001B[0m");
        FXMLLoader fxmlLoader = new FXMLLoader(NewsAggregatorClientApplication.class.getResource("news_aggregator_client.fxml"));
        NewsAggregatorClientController controller = new NewsAggregatorClientController(this.getHostServices());
        stage.setScene(null);
        if (!connectionChecker.checkInternetConnection()) {
            NoInternetDialog dialog = new NoInternetDialog();
            stage.setScene(dialog.getDialogPane().getScene());
            stage.getIcons().add(new Image(Objects.requireNonNull(NewsAggregatorClientApplication.class.getResourceAsStream("assets/images/no-internet.png"))));            stage.show();
        } else {
            fxmlLoader.setController(controller);
            Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
            stage.setTitle("Crypto News Aggregator Client");
            stage.getIcons().add(new Image(Objects.requireNonNull(NewsAggregatorClientApplication.class.getResourceAsStream("assets/images/icon.png"))));
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