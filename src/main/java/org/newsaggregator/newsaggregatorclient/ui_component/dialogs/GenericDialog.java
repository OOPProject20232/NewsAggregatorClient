package org.newsaggregator.newsaggregatorclient.ui_component.dialogs;

import javafx.scene.control.Dialog;

public class GenericDialog extends Dialog<Void> {
    /**
     * Class cha của tất cả các dialog trong project
     * Đang cố gắng định dạng CSS cho chúng, nhưng chưa load được file css
     * Rất cáu
     */
    public GenericDialog() {
        getDialogPane().getStyleClass().addAll("dialog", "generic-container");

//        getDialogPane().getStylesheets().add(getClass().getResourceAsStream("assets/css/main.css").toString());
    }
}
