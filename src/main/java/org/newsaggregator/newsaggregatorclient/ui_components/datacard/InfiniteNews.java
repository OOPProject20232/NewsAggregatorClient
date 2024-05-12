package org.newsaggregator.newsaggregatorclient.ui_components.datacard;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class InfiniteNews extends NewsCategoryGroupTitledPane{
    private Button loadMoreButton;
    public InfiniteNews(String category) {
        super(category);
        VBox outerBox = new VBox();
        this.setContent(outerBox);
        outerBox.setAlignment(Pos.TOP_CENTER);
        outerBox.getStyleClass().add("category__layout");
        loadMoreButton = new Button("Load more");
        loadMoreButton.getStyleClass().add("load-more-button");
        outerBox.getChildren().addAll(newsGroupLayout, loadMoreButton);
    }

    public Button getLoadMoreButton() {
        return loadMoreButton;
    }
}
