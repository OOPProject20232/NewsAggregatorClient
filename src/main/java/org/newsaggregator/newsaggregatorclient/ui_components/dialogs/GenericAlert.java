package org.newsaggregator.newsaggregatorclient.ui_components.dialogs;

import javafx.scene.control.Alert;

public class GenericAlert extends Alert {
    public GenericAlert(AlertType alertType, String title, String headerText, String contentText) {
        super(alertType);
        this.setTitle(title);
        this.setHeaderText(headerText);
        this.setContentText(contentText);
//        getDialogPane().getStylesheets().add(
//                "src/main/resources/org/newsaggregator/newsaggregatorclient/assets/css/dialogs.css"
//        );
        getDialogPane().getStyleClass().addAll("dialog", "generic-container");
    }
}
