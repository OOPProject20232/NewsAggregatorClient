package org.newsaggregator.newsaggregatorclient.ui_components.dialogs;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class Error500Dialog extends GenericDialog{
    public Error500Dialog(){
        getDialogPane().getStyleClass().addAll("dialog", "generic-container");
        ButtonType close = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        setHeaderText("Server error");
        setContentText("The server encountered an error. Please try again later");
        getDialogPane().getButtonTypes().add(close);
    }
}
