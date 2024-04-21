package org.newsaggregator.newsaggregatorclient.ui_component;

import javafx.application.HostServices;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import org.newsaggregator.newsaggregatorclient.pojos.NewsItemData;
import org.newsaggregator.newsaggregatorclient.jsonparsing.NewsJSONLoader;

import java.util.List;

public class LoadNewsItems extends Task<Void> {
    /**
     * Class này chứa các hàm để load các tin tức từ các nguồn khác nhau
     * lên trên GUI
     */
    public LoadNewsItems(int limit, Pane container, HostServices hostServices) {
        loadNewsItems(limit, container, hostServices);
    }
    @Override
    protected Void call() throws Exception {
        updateMessage("Loading news items");
        return null;
    }

    protected void succeeded() {
        super.succeeded();
        updateMessage("News items loaded");
    }

    protected void failed() {
        super.failed();
        updateMessage("Failed to load news items");
    }

    public void loadNewsItems(int limit, Pane container, HostServices hostServices) {
        NewsJSONLoader dataLoader = new NewsJSONLoader();
        dataLoader.loadJSON();
        List<NewsItemData> data = dataLoader.getNewsItemDataList(limit);
        NewsCategoryGroupTitledPane newsCategoryGroupTitledPane = new NewsCategoryGroupTitledPane("Latest news");
        for (int countItem = 0; countItem < limit; countItem++) {
            System.out.println("Adding news item " + (countItem));
            NewsItemData item = data.get(countItem);
            System.out.println(item.title);
            NewsItem newsItem = new NewsItem(item);
            newsItem.getArticleHyperlinkObject().setOnAction(
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            hostServices.showDocument(item.url);
                        }
                    }
            );
            newsCategoryGroupTitledPane.addNewsItem(newsItem);
        }
        container.getChildren().add(newsCategoryGroupTitledPane);
    }
}
