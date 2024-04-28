package org.newsaggregator.newsaggregatorclient.ui_component.dialogs;

import javafx.scene.control.Dialog;

public class GenericDialog extends Dialog<Void> {
    public GenericDialog() {
        getDialogPane().getStyleClass().addAll("dialog", "generic-container");
    }
}
