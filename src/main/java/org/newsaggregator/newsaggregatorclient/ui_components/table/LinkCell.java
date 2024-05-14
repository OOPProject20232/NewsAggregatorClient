package org.newsaggregator.newsaggregatorclient.ui_components.table;

import javafx.scene.control.TableCell;

public class LinkCell<T> extends TableCell<T, Void> {
    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
            setText(null);
        } else {
            setGraphic(null);
            setText(getItem().toString());
        }
    }
}
