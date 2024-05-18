package org.newsaggregator.newsaggregatorclient.ui_components.dialogs;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.newsaggregator.newsaggregatorclient.NewsAggregatorClientApplication;

import javafx.scene.shape.Rectangle;
import java.nio.file.Paths;
import java.util.Objects;

public class NoInternetDialog extends Dialog<Integer> {
    public NoInternetDialog(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(NewsAggregatorClientApplication.class.getResource("no_internet.fxml"));
            this.setDialogPane(fxmlLoader.load());
            fxmlLoader.setController(this);
            this.setContentText("No Internet");
            Stage stage = (Stage) getDialogPane().getScene().getWindow();
            stage.initStyle(StageStyle.TRANSPARENT);
            getDialogPane().getScene().setFill(Color.TRANSPARENT);
//            DropShadow shadow = new DropShadow();
//            shadow.setColor(Color.BLACK);
//            shadow.setRadius(3);
//            shadow.setSpread(3);
//            this.getDialogPane().setEffect(shadow);
//            Rectangle clip = new Rectangle(this.getWidth(), this.getHeight());
//            clip.setArcWidth(32);
//            clip.setArcHeight(32);
//            getDialogPane().setClip(clip);
            getDialogPane().setStyle("-fx-background-radius: 30px; -fx-border-width: 0;");
            ButtonType buttonType = this.getDialogPane().getButtonTypes().getFirst();
            System.out.println(buttonType.getButtonData());
            stage.setTitle("Cannot connect to server");
            stage.getIcons().add(new Image(Objects.requireNonNull(NewsAggregatorClientApplication.class.getResourceAsStream("assets/images/no-internet.png"))));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
