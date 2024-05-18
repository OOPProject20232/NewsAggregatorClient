package org.newsaggregator.newsaggregatorclient;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;
import org.newsaggregator.newsaggregatorclient.checkers.ConnectionChecker;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.Error500Dialog;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.LoadingDialog;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.NoInternetDialog;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.SplashScreen;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.FutureTask;

public class NewsAggregatorClientApplication extends Application {
    @Override
    public synchronized void start(final Stage stage) throws IOException {
        System.out.println("\u001B[33m"+"Starting application"+ "\u001B[0m");
        SplashScreen loadingDialog = new SplashScreen();
        FXMLLoader fxmlLoader = new FXMLLoader(NewsAggregatorClientApplication.class.getResource("news_aggregator_client.fxml"));
        NewsAggregatorClientController controller = new NewsAggregatorClientController(this.getHostServices());
        fxmlLoader.setController(controller);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                System.out.println("Called");
                System.out.println("Set");
                int serverCheckResponseCode = 0;
                Scene scene;
                System.out.println("Hi"
                );
                try {
                    scene = new Scene(fxmlLoader.load());
                    serverCheckResponseCode = ConnectionChecker.checkInternetConnection();
                    System.out.println(serverCheckResponseCode);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (serverCheckResponseCode == 0) {
                    failed();
                    loadingDialog.close();
                    NoInternetDialog dialog = new NoInternetDialog();
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.getIcons().add(new Image(Objects.requireNonNull(NewsAggregatorClientApplication.class.getResourceAsStream("assets/images/no-internet.png"))));
                    dialog.showAndWait();
                } else if (serverCheckResponseCode == 500 || serverCheckResponseCode / 100 == 4) {
                    Error500Dialog error500Dialog = new Error500Dialog();
                    error500Dialog.show();
                } else if (serverCheckResponseCode == 200){
                    loadingDialog.close();
                    stage.setMaximized(true);
                    stage.setTitle("Crypto News Aggregator Client");
                    stage.getIcons().add(new Image(Objects.requireNonNull(NewsAggregatorClientApplication.class.getResourceAsStream("assets/images/icon.png"))));
                    stage.setScene(scene);
                    controller.showAllNewsCategories();
                    stage.show();
                    controller.start();
                } else{
                    System.out.println("???");
                }
                return null;
            }
        };
            loadingDialog.show();
        task.run();
        task.setOnFailed((e)->loadingDialog.close());
    }

    public static void main(String[] args) {
        launch();
    }
}