package org.newsaggregator.newsaggregatorclient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import org.newsaggregator.newsaggregatorclient.checkers.ConnectionChecker;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.LoadingDialog;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.NoInternetDialog;

import java.io.IOException;
import java.util.Objects;

public class NewsAggregatorClientApplication extends Application {
    private final ConnectionChecker connectionChecker = new ConnectionChecker();

    @Override
    public synchronized void start(Stage stage) throws IOException {
        System.out.println("\u001B[33m"+"Starting application"+ "\u001B[0m");
        FXMLLoader fxmlLoader = new FXMLLoader(NewsAggregatorClientApplication.class.getResource("news_aggregator_client.fxml"));
        NewsAggregatorClientController controller = new NewsAggregatorClientController(this.getHostServices());
        if (!connectionChecker.checkInternetConnection()) {
            NoInternetDialog dialog = new NoInternetDialog();
//            dialog.getDialogPane().getStylesheets().add(this.getClass().getResourceAsStream("/assets/css/dialogs.css").toString());
            stage.setScene(dialog.getDialogPane().getScene());
            stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(Objects.requireNonNull(NewsAggregatorClientApplication.class.getResourceAsStream("assets/images/no-internet.png"))));
            dialog.show();
        } else {
            LoadingDialog loadingDialog = new LoadingDialog();
            loadingDialog.show();
            fxmlLoader.setController(controller);
            Scene scene = new Scene(fxmlLoader.load(), 1200, 1000);
            stage.setTitle("Crypto News Aggregator Client");
            stage.getIcons().add(new Image(Objects.requireNonNull(NewsAggregatorClientApplication.class.getResourceAsStream("assets/images/icon.png"))));
            stage.setScene(scene);
            Stage finalStage = stage;
            controller.start();
            controller.showAllNewsCategories();
            loadingDialog.close();
            finalStage.show();
//            loadingDialog.close();
//            controller.showAllNewsCategories();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}