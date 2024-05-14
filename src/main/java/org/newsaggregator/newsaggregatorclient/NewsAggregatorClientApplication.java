package org.newsaggregator.newsaggregatorclient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import org.newsaggregator.newsaggregatorclient.checkers.ConnectionChecker;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.Error500Dialog;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.LoadingDialog;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.NoInternetDialog;

import java.io.IOException;
import java.util.Objects;

public class NewsAggregatorClientApplication extends Application {
    @Override
    public synchronized void start(Stage stage) throws IOException {
        System.out.println("\u001B[33m"+"Starting application"+ "\u001B[0m");
        FXMLLoader fxmlLoader = new FXMLLoader(NewsAggregatorClientApplication.class.getResource("news_aggregator_client.fxml"));
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.show();
        NewsAggregatorClientController controller = new NewsAggregatorClientController(this.getHostServices());
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load());
        int serverCheckResponseCode = ConnectionChecker.checkInternetConnection();
        if (serverCheckResponseCode == 0) {
            loadingDialog.close();
            NoInternetDialog dialog = new NoInternetDialog();
//            dialog.getDialogPane().getStylesheets().add(this.getClass().getResourceAsStream("/assets/css/dialogs.css").toString());
            stage.setScene(dialog.getDialogPane().getScene());
            stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(Objects.requireNonNull(NewsAggregatorClientApplication.class.getResourceAsStream("assets/images/no-internet.png"))));
            dialog.show();
        } else if (serverCheckResponseCode == 500 || serverCheckResponseCode / 100 == 4) {
            loadingDialog.close();
            Error500Dialog error500Dialog = new Error500Dialog();
            error500Dialog.show();
        } else if (serverCheckResponseCode == 200){
            stage.setMaximized(true);
            stage.setTitle("Crypto News Aggregator Client");
            stage.getIcons().add(new Image(Objects.requireNonNull(NewsAggregatorClientApplication.class.getResourceAsStream("assets/images/icon.png"))));
            stage.setScene(scene);
            controller.start();
            controller.showAllNewsCategories();
            loadingDialog.close();
            stage.show();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}