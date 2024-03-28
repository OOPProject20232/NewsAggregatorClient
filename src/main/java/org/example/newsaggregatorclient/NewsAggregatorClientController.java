package org.example.newsaggregatorclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.web.WebView;
import org.example.newsaggregatorclient.mediator_objects.NewsItemData;
import org.example.newsaggregatorclient.ui_component.NewsCategoryGroupTitledPane;
import org.example.newsaggregatorclient.ui_component.NewsItem;

public class NewsAggregatorClientController {

    @FXML
    private Label welcomeText;
    @FXML
    private WebView articleWebView;
    @FXML
    private FlowPane newsContainer;
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    protected void loadWebsite(ActionEvent event, String url) {
        articleWebView.getEngine().load(url);
    }

    public void showAllNewsCategories() {
        newsContainer.getChildren().clear();
        NewsItemData newsItemData = new NewsItemData("category", "title", "author", "description", "https://www.geeksforgeeks.org/javafx-titledpane-class/", "https://bitcoinist.com/wp-content/uploads/2024/03/Shiba-Inu-16.jpeg?fit=299%2C168", "publishedAt", "content");
        for (int i = 0; i < 10; i++) {
            NewsCategoryGroupTitledPane newsTitledPaneByCategory = new NewsCategoryGroupTitledPane("ABC");
            for (int j = 0; j < 10; j++) {
                NewsItem newsItem = new NewsItem(newsItemData);
                newsTitledPaneByCategory.getContainer().getChildren().add(newsItem);
                newsItem.getArticleHyperlinkObject().setOnAction(event -> {
                    this.loadWebsite(event, newsItemData.url);
                });
            }
            newsContainer.getChildren().add(newsTitledPaneByCategory);
        }
    }
}