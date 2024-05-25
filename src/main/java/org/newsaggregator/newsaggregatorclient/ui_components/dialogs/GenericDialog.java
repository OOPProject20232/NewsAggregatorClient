package org.newsaggregator.newsaggregatorclient.ui_components.dialogs;

import javafx.event.EventHandler;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.newsaggregator.newsaggregatorclient.NewsAggregatorClientApplication;

import java.awt.dnd.DragGestureListener;
import java.util.Objects;

public class GenericDialog extends Dialog<Void> {
    /**
     * Class cha của tất cả các dialog trong project
     */
    public GenericDialog() {
        getDialogPane().getStyleClass().addAll("dialog", "generic-transparent-container");
//
        getDialogPane().getStylesheets().add(
                NewsAggregatorClientApplication.class.getResourceAsStream("assets/css/dialogs.css").toString()
        );
        getDialogPane().getStylesheets().add(
                NewsAggregatorClientApplication.class.getResource("assets/css/main.css").toExternalForm());
    }

}
