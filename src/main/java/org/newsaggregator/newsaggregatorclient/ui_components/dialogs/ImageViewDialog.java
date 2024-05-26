package org.newsaggregator.newsaggregatorclient.ui_components.dialogs;

import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.MalformedURLException;
import java.net.URI;

public class ImageViewDialog extends GenericDialog {
    public ImageViewDialog(String url) throws MalformedURLException {
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.initStyle(StageStyle.UNIFIED);
        stage.setTitle(url);
        stage.setResizable(true);
        VBox vBox = new VBox();
        ImageView img = new ImageView();
        Label label = new Label("Press ESC to close the image");
        vBox.getChildren().addAll(label, img);
        ScrollPane scrollPane = new ScrollPane(vBox);
        getDialogPane().setContent(scrollPane);
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        stage.getIcons().add(new Image("https://logo.clearbit.com/%s".formatted(URI.create(url).getHost()), true));
        Platform.runLater(() -> {
            Image tmp = new Image(url, true);
            img.setImage(tmp);
            stage.setWidth(500);
            stage.setHeight(400);
        });
         this.getDialogPane().addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
             if (event.getCode().toString().equals("ESCAPE")) {
                 stage.close();
             }
         });
    }
}
