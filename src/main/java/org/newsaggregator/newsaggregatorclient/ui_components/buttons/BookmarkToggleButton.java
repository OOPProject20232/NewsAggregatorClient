package org.newsaggregator.newsaggregatorclient.ui_components.buttons;

import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;

public class BookmarkToggleButton extends ToggleButton {
    public BookmarkToggleButton() {
        super();
        ImageView bookmark = new ImageView("file:src/main/resources/org/newsaggregator/newsaggregatorclient/assets/images/bookmark.png");
        bookmark.setFitHeight(16);
        bookmark.setFitWidth(16);
        ImageView bookmarkSelected = new ImageView("file:src/main/resources/org/newsaggregator/newsaggregatorclient/ui_components/buttons/bookmark-selected.png");
        bookmarkSelected.setFitHeight(16);
        bookmarkSelected.setFitWidth(16);
        this.getStyleClass().add("util-button");
        if (this.isSelected()) {
            this.setGraphic(bookmarkSelected);
        } else {
            this.setGraphic(bookmark);
        }
    }

    public void setSelected(){
        this.setSelected(true);
        ImageView bookmarkSelected = new ImageView("file:src/main/resources/org/newsaggregator/newsaggregatorclient/assets/images/bookmark-selected.png");
        bookmarkSelected.setFitHeight(16);
        bookmarkSelected.setFitWidth(16);
        this.setGraphic(bookmarkSelected);
    }

    public void setUnselected(){
        this.setSelected(false);
        ImageView bookmark = new ImageView("file:src/main/resources/org/newsaggregator/newsaggregatorclient/assets/images/bookmark.png");
        bookmark.setFitHeight(16);
        bookmark.setFitWidth(16);
        this.setGraphic(bookmark);
    }
}
