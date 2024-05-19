package org.newsaggregator.newsaggregatorclient.ui_components.uiloader;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.newsaggregator.newsaggregatorclient.NewsAggregatorClientApplication;
import org.newsaggregator.newsaggregatorclient.NewsAggregatorClientController;
import org.newsaggregator.newsaggregatorclient.ui_components.datacard.HorizontalDataCard;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.CategoryTitledPane;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.LoadingDialog;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.ReadingArticle;
import org.newsaggregator.newsaggregatorclient.ui_components.newsscrollableframe.ArticlesFrame;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.ui_components.datacard.CategoryClickable;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.NewsCategoryGroupTitledPane;
import org.newsaggregator.newsaggregatorclient.ui_components.datacard.NewsItemCard;

import java.io.IOException;
import java.util.List;

public class ArticleItemsLoader<T>
        extends Task<Void> implements ItemsLoader<NewsItemData>{
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
    T newsCategoryGroupTitledPane;
    NewsAggregatorClientController mainController;
    boolean containingSummary = true;
    boolean containingCategories = true;

    public ArticleItemsLoader(int limit, int begin, HostServices hostServices, T newsCategoryGroupTitledPane, NewsAggregatorClientController mainController) {
        this.limit = limit;
        this.container = container;
        this.hostServices = hostServices;
        this.newsCategoryGroupTitledPane = newsCategoryGroupTitledPane;
        this.begin = begin;
        this.mainController = mainController;
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be greater than 0");
        }
        if (begin < 0) {
            throw new IllegalArgumentException("Begin must be greater than or equal to 0");
        }
//        this.newsCategoryGroupTitledPane.setMaxWidth(800);
    }
    @Override
    protected Void call() {
        updateTitle("Loading...");
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


    public synchronized void loadItems(List<NewsItemData> data) {
        if (begin + limit > data.size()) {
            limit = data.size() - begin;
        }
        Thread textThread = new Thread(() -> {
            for (int countItem = begin; countItem < limit + begin; countItem++) {
                final int count = countItem;
                NewsItemData itemData = data.get(countItem);
                Platform.runLater(() -> {
                    NewsItemCard newsItem = new NewsItemCard(itemData);
                    newsItem.setContainingSummary(containingSummary);
                    newsItem.setContainingCategories(containingCategories);
                    newsItem.setParentWidth(((CategoryTitledPane<?, ?>) newsCategoryGroupTitledPane).getWidth() - 20);
                    newsItem.setText();
                    Platform.runLater(newsItem::setImage);
                    newsItem.setPublisherLogo();
                    newsItem.getArticleHyperlinkTitleObject().setOnAction(
                            event -> {
                                FXMLLoader loader = new FXMLLoader(NewsAggregatorClientApplication.class.getResource("web_view_with_controllers.fxml"));
                                try {
                                    ReadingArticle popup = new ReadingArticle(itemData.getUrl());
                                    popup.initialize();
                                    popup.showAndWait();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );
                    newsItem.getReadMore().setOnAction(
                            event -> hostServices.showDocument(itemData.getUrl())
                    );
                    newsItem.getThumbnailHyperlink().setOnAction(
                            event -> hostServices.showDocument(itemData.getUrl())
                    );
                    for (Node tmp: newsItem.getCategories()){
                        CategoryClickable category = (CategoryClickable) tmp;
                        category.setOnAction((event) -> mainController.setSearchText(category.getText()));
                    }
                    updateProgress(count, limit + begin);
                    CategoryTitledPane<NewsItemCard,?> newsCategoryGroupTitledPane1 = (CategoryTitledPane<NewsItemCard,?>) newsCategoryGroupTitledPane;
                    newsCategoryGroupTitledPane1.addItem(newsItem);
                });
            }
        });
        textThread.start();
    }

    public void setContainingSummary(boolean containingSummary) {
        this.containingSummary = containingSummary;
    }

    public void setContainingCategories(boolean containingCategories) {
        this.containingCategories = containingCategories;
    }
}
