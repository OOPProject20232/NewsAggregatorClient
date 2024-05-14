package org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import static javafx.geometry.Pos.CENTER;

public class InfiniteNews extends NewsCategoryGroupTitledPane {
    private Button loadMoreButton;
    public InfiniteNews(String category) {
        super(category);
        VBox outerBox = new VBox();
        this.setContent(outerBox);
        outerBox.getStyleClass().add("category__layout");
        getStyleClass().add("category__layout");
        loadMoreButton = new Button("Load more");
        loadMoreButton.getStyleClass().add("load-more-button");
        outerBox.setAlignment(CENTER);
        outerBox.getChildren().addAll(getContainer(), loadMoreButton);
    }

    public Button getLoadMoreButton() {
        return loadMoreButton;
    }
}
