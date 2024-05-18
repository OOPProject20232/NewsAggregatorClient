package org.newsaggregator.newsaggregatorclient.ui_components.dialogs;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.newsaggregator.newsaggregatorclient.NewsAggregatorClientApplication;

import java.util.Objects;

public class SplashScreen extends Alert {
    FXMLLoader fxmlLoader = new FXMLLoader(NewsAggregatorClientApplication.class.getResource("splashscreen.fxml"));
    public SplashScreen() {
        super(AlertType.NONE);
        try {
            this.getDialogPane().setContent(fxmlLoader.load());
            Stage stage = (Stage) getDialogPane().getScene().getWindow();
            stage.setWidth(300);
            stage.setHeight(450);
            stage.setIconified(false);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setAlwaysOnTop(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ButtonType close = new ButtonType("Close", ButtonType.CANCEL.getButtonData());
        getDialogPane().getButtonTypes().add(close);
        getDialogPane().lookupButton(close).setVisible(false);
        getDialogPane().lookupButton(close).setStyle("-fx-pref-height: 0");
        getDialogPane().setContentText("Loading...");
    }
}
