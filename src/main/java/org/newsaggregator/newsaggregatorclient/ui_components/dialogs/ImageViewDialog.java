package org.newsaggregator.newsaggregatorclient.ui_components.dialogs;

import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        stage.sizeToScene();
        ImageView img = new ImageView();
        ScrollPane scrollPane = new ScrollPane(img);
        getDialogPane().setContent(scrollPane);
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Platform.runLater(() -> {
//            stage.getIcons().add(new Image("https://logo.clearbit.com/%s".formatted(URI.create(url).getHost()), true));
            img.setImage(new Image(url, true));
        });
    }
}
