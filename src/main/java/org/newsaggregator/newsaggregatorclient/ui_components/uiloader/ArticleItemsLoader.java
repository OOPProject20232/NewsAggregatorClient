package org.newsaggregator.newsaggregatorclient.ui_components.uiloader;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Dialog;
import org.newsaggregator.newsaggregatorclient.NewsAggregatorClientController;
import org.newsaggregator.newsaggregatorclient.database.SQLiteJDBC;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.CategoryTitledPane;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.ImageViewDialog;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.ArticleReader;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.ui_components.buttons.CategoryClickable;
import org.newsaggregator.newsaggregatorclient.ui_components.datacard.NewsItemCard;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class ArticleItemsLoader
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
    protected int begin;
    protected int limit;
    protected HostServices hostServices;
    protected CategoryTitledPane<NewsItemCard, NewsItemData> newsCategoryGroupTitledPane;
    protected NewsAggregatorClientController mainController;
    protected boolean containingSummary = true;
    protected boolean containingCategories = true;

    public ArticleItemsLoader(int limit, int begin, HostServices hostServices, CategoryTitledPane<NewsItemCard, NewsItemData> newsCategoryGroupTitledPane, NewsAggregatorClientController mainController) {
        this.limit = limit;
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
            for (int countItem = begin; countItem < limit + begin; countItem++) {
                NewsItemData itemData = data.get(countItem);

                    NewsItemCard newsItem = new NewsItemCard(itemData);
                    newsItem.setContainingSummary(containingSummary);
                    newsItem.setContainingCategories(containingCategories);
                    newsItem.setText();
                    Platform.runLater(() -> {
                        newsItem.setImage();
                        newsItem.setPublisherLogo();
                    });
                    newsItem.getArticleHyperlinkTitle().setOnAction(
                            (e) -> this.showArticlePopup(newsItem)
                    );
                    newsItem.getReadMore().setOnAction(
                            (e) -> this.showArticlePopup(newsItem)
                    );
                    newsItem.getThumbnailHyperlink().setOnAction(
                            event -> this.showImage(itemData)
                    );
                    newsItem.getExternalLink().setOnAction(
                            event -> hostServices.showDocument(itemData.getUrl())
                    );
                    if (this.checkBookmark(itemData)){
                        newsItem.getBookmarkButton().setSelected();
                    }
                    newsItem.getBookmarkButton().setOnAction((e) -> {
                        if (newsItem.getBookmarkButton().isSelected()){
                            newsItem.getBookmarkButton().setSelected();
                            this.addBookmark(itemData);
                        }
                        else {
                            newsItem.getBookmarkButton().setUnselected();
                            this.removeBookmark(itemData);
                        }
                    });
                    for (Node tmp: newsItem.getCategories()){
                        CategoryClickable category = (CategoryClickable) tmp;
                        category.setOnAction((event) -> mainController.setSearchText(category.getText().replace("#", "")));
                    }
                    updateProgress(countItem, limit + begin);
                    CategoryTitledPane<NewsItemCard,?> newsCategoryGroupTitledPane1 = (CategoryTitledPane<NewsItemCard,?>) newsCategoryGroupTitledPane;
                    newsCategoryGroupTitledPane1.addItem(newsItem);
            }
    }

    public void setContainingSummary(boolean containingSummary) {
        this.containingSummary = containingSummary;
    }

    public void setContainingCategories(boolean containingCategories) {
        this.containingCategories = containingCategories;
    }

    private void showArticlePopup(NewsItemCard itemCard){
        try {
            ArticleReader popup = new ArticleReader(itemCard.getNewsItemData(), hostServices);
            popup.initialize();
            popup.getBookmarkButton().selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (popup.getBookmarkButton().isSelected()){
                    itemCard.getBookmarkButton().setSelected();
                }
                else{
                    itemCard.getBookmarkButton().setUnselected();
                }
            });
            popup.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showImage(NewsItemData itemData){
        ImageViewDialog popup = null;
        try {
            popup = new ImageViewDialog(itemData.getUrlToImage());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        popup.showAndWait();
    }

    private void addBookmark(NewsItemData itemData){
        SQLiteJDBC db = new SQLiteJDBC();
        db.insert(itemData);
    }

    private void removeBookmark(NewsItemData itemData){
        SQLiteJDBC db = new SQLiteJDBC();
        db.delete(itemData.getGuid());
    }

    private boolean checkBookmark(NewsItemData itemData){
        SQLiteJDBC db = new SQLiteJDBC();
        return db.isBookmarked(itemData);
    }
}
