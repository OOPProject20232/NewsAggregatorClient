package org.newsaggregator.newsaggregatorclient.ui_components.dialogs;

import javafx.event.EventHandler;
import javafx.scene.control.Dialog;
import javafx.scene.input.MouseEvent;

public class GenericDialog extends Dialog<Void> {
    /**
     * Class cha của tất cả các dialog trong project
     */
    public GenericDialog() {
        getDialogPane().getStyleClass().addAll("dialog", "generic-container");
        getDialogPane().getStylesheets().add(this.getClass().getResource("dialogs.css").toExternalForm());
    }

}
