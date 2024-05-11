package org.newsaggregator.newsaggregatorclient;

import javafx.application.HostServices;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.newsaggregator.newsaggregatorclient.ui_components.datacard.InfiniteNews;

public class RedditFrame extends ScrollPane {
    private HostServices hostServices;
    private NewsAggregatorClientController mainController;
    private int currentRedditPage = 1;
    private final int limit = 50;
    private final GridPane newsContainer = new GridPane();

    public RedditFrame(HostServices hostServices, NewsAggregatorClientController mainController){
        this.hostServices = hostServices;
        this.mainController = mainController;
        AnchorPane container = new AnchorPane();
        this.setContent(container);
        container.getChildren().add(newsContainer);
        AnchorPane.setBottomAnchor(newsContainer, 0.0);
        AnchorPane.setTopAnchor(newsContainer, 0.0);
        AnchorPane.setLeftAnchor(newsContainer, 0.0);
        AnchorPane.setRightAnchor(newsContainer, 0.0);
        setFitToWidth(true);
        setFitToHeight(true);
        newsContainer.getChildren().clear();
        newsContainer.getStyleClass().addAll("generic-transparent-container");
        newsContainer.setHgap(36);
        newsContainer.setVgap(36);
        newsContainer.setPadding(new Insets(48, 48, 48, 48));
        container.getStyleClass().addAll( "generic-transparent-container");
        this.getStyleClass().addAll("generic-transparent-container");
        this.mainController = mainController;
    }
}
