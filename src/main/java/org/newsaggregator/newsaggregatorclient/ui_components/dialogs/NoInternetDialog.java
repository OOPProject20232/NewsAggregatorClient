package org.newsaggregator.newsaggregatorclient.ui_components.dialogs;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.newsaggregator.newsaggregatorclient.NewsAggregatorClientApplication;

import java.nio.file.Paths;
import java.util.Objects;

public class NoInternetDialog extends GenericDialog{
    public NoInternetDialog(){
        getDialogPane().getStyleClass().addAll("dialog", "generic-container");
        System.out.println(Paths.get("").toAbsolutePath());
//        getDialogPane().getStylesheets().add(getClass().getResource("/dialog.css").toExternalForm());
        ButtonType close = new ButtonType("Close (and check your Internet connection)", ButtonBar.ButtonData.CANCEL_CLOSE);
        setTitle("No Internet connection");
        setHeaderText("No Internet connection");
        setContentText("This app cannot work without Internet. Please check your Internet connection, then re-open the app");
        getDialogPane().getButtonTypes().add(close);
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(NewsAggregatorClientApplication.class.getResourceAsStream("assets/images/no-internet.png"))));
    }
}
