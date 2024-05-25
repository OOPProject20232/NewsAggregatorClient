package org.newsaggregator.newsaggregatorclient.ui_components.dialogs;

import javafx.scene.control.Alert;
import org.newsaggregator.newsaggregatorclient.NewsAggregatorClientApplication;

public class GenericAlert extends Alert {
    public GenericAlert(AlertType alertType, String title, String headerText, String contentText) {
        super(alertType);
        this.setTitle(title);
        this.setHeaderText(headerText);
        this.setContentText(contentText);
        getDialogPane().getStylesheets().add(
                NewsAggregatorClientApplication.class.getResource("assets/css/main.css").toExternalForm()
        );
        getDialogPane().getStylesheets().add(
                NewsAggregatorClientApplication.class.getResource("assets/css/dialogs.css").toExternalForm());
        getDialogPane().getStyleClass().addAll("dialog", "generic-transparent-container");
    }
}
