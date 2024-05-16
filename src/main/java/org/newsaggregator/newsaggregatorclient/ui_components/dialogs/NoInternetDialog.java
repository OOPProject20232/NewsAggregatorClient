package org.newsaggregator.newsaggregatorclient.ui_components.dialogs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.newsaggregator.newsaggregatorclient.NewsAggregatorClientApplication;

import java.nio.file.Paths;
import java.util.Objects;

public class NoInternetDialog extends Dialog<Void> {
    public NoInternetDialog(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(NewsAggregatorClientApplication.class.getResource("no_internet.fxml"));
//            this.setDialogPane(fxmlLoader.load());
            fxmlLoader.setController(this);
            Scene scene = new Scene(fxmlLoader.load(), 500, 200);
            Stage stage = (Stage) getDialogPane().getScene().getWindow();
            stage.setScene(scene);
            stage.setWidth(500);
            stage.setHeight(200);
            stage.setTitle("No Internet");
            stage.setResizable(false);
            stage.getIcons().add(new Image(Objects.requireNonNull(NewsAggregatorClientApplication.class.getResourceAsStream("assets/images/no-internet.png"))));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
