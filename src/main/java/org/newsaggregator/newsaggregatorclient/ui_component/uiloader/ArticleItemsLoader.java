package org.newsaggregator.newsaggregatorclient.ui_component.uiloader;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.layout.Pane;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.ui_component.datacard.NewsCategoryGroupTitledPane;
import org.newsaggregator.newsaggregatorclient.ui_component.datacard.NewsItemCard;

import java.util.List;

public class ArticleItemsLoader extends Task<Void> implements ItemsLoader{
    /**
     * <p>
     *     Class này chứa các hàm để load các tin tức từ các nguồn khác nhau
     *      lên trên GUI
     * </p>
     *
     * <p>
     * Các đối tượng gọi tới lớp này sẽ truyền một tham số là một danh sách các tin tức, sau đó
     * sẽ tạo 2 luồng để tải tin lên, một luồng để tải text và một luồng để tải hình ảnh
     * </p>
     */
    int begin;
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
    protected Void call() {
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

    public synchronized NewsCategoryGroupTitledPane loadItems(List<NewsItemData> data) {
        if (begin + limit > data.size()) {
            limit = data.size() - begin;
        }
        Thread textThread = new Thread(() -> Platform.runLater(() -> {
            for (int countItem = begin; countItem < limit + begin; countItem++) {
                NewsItemData itemData = data.get(countItem);
                NewsItemCard newsItem = new NewsItemCard(itemData);
                newsItem.setText();
                newsItem.getArticleHyperlinkTitleObject().setOnAction(
                        event -> hostServices.showDocument(itemData.getUrl())
                );
                updateProgress(countItem, limit + begin);
                newsCategoryGroupTitledPane.addItem(newsItem);
            }
        }));
        textThread.start();
        Thread imageThread = new Thread(() -> Platform.runLater(() -> {
            for (NewsItemCard newsItem : newsCategoryGroupTitledPane.getItems()) {
                newsItem.setImage();
            }
        }));
        imageThread.start();
        return newsCategoryGroupTitledPane;
    }
}
