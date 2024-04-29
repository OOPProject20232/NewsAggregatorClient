package org.newsaggregator.newsaggregatorclient.ui_component.dialogs;

import javafx.css.Style;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import java.nio.file.Paths;

public class NoInternetDialog extends GenericDialog{
    public NoInternetDialog(){
        getDialogPane().getStyleClass().addAll("dialog", "generic-container");
        System.out.println(Paths.get("").toAbsolutePath());
//        getDialogPane().getStylesheets().add(getClass().getResource("/dialog.css").toExternalForm());
        ButtonType close = new ButtonType("Close (and check your Internet connection)", ButtonBar.ButtonData.CANCEL_CLOSE);
        setHeaderText("No Internet connection");
        setContentText("This app cannot work without Internet. Please check your Internet connection, then re-open the app");
        getDialogPane().getButtonTypes().add(close);
    }
}
