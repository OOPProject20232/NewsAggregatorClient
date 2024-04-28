package org.newsaggregator.newsaggregatorclient.ui_component;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import org.newsaggregator.newsaggregatorclient.pojos.NewsItemData;
import org.newsaggregator.newsaggregatorclient.ui_component.datacard.NewsCategoryGroupTitledPane;
import org.newsaggregator.newsaggregatorclient.ui_component.datacard.NewsItem;

import java.util.List;

public class ArticleItemsLoader extends Task<Void>{
    private int begin;
    /**
     * Class này chứa các hàm để load các tin tức từ các nguồn khác nhau
     * lên trên GUI
     */
    int limit;
    Pane container;
    HostServices hostServices;
    NewsCategoryGroupTitledPane newsCategoryGroupTitledPane;

    public ArticleItemsLoader(int limit, int begin, Pane container, HostServices hostServices, NewsCategoryGroupTitledPane newsCategoryGroupTitledPane) {
        this.limit = limit;
        this.container = container;
        this.hostServices = hostServices;
        this.newsCategoryGroupTitledPane = newsCategoryGroupTitledPane;
        this.begin = begin;
//        this.newsCategoryGroupTitledPane.setMaxWidth(800);
    }
    @Override
    protected Void call() throws Exception {
        updateMessage("Loading news items");
        return null;
    }

    protected void succeeded() {
        super.succeeded();
        updateMessage("News items loaded");
        System.out.println("News items loaded");
    }

    protected void failed() {
        super.failed();
        updateMessage("Failed to load news items");
    }

    @Override
    protected void updateProgress(long workDone, long max) {
        super.updateProgress(workDone, max);
        System.out.println("Loading: " + workDone + "/" + max);
    }

    public NewsCategoryGroupTitledPane loadArticleItems(List<NewsItemData> data) {
        for (int countItem = begin; countItem < limit + begin; countItem++) {
//            System.out.println("Adding news item " + (countItem));
            NewsItemData itemData = data.get(countItem);
//            System.out.println(itemData.title);
            NewsItem newsItem = new NewsItem(itemData);
            newsItem.loadText();
            newsItem.getArticleHyperlinkObject().setOnAction(
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            hostServices.showDocument(itemData.url);
                        }
                    }
            );
            updateProgress(countItem, limit + begin);
            newsCategoryGroupTitledPane.addNewsItem(newsItem);
        }
        Thread imageThread = new Thread(() -> {
            Platform.runLater(() -> {
                for (NewsItem newsItem : newsCategoryGroupTitledPane.getNewsItems()) {
                    newsItem.loadImage();
                }
            });
        });
        imageThread.start();
        return newsCategoryGroupTitledPane;
    }
}
