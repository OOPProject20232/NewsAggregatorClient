package org.newsaggregator.newsaggregatorclient;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import org.jetbrains.annotations.NotNull;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.jsonparsing.NewsJSONLoader;
import org.newsaggregator.newsaggregatorclient.ui_components.datacard.InfiniteNews;
import org.newsaggregator.newsaggregatorclient.ui_components.datacard.NewsCategoryGroupTitledPane;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.LoadingDialog;
import org.newsaggregator.newsaggregatorclient.ui_components.uiloader.ArticleItemsLoader;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ArticlesFrame extends GenericFrame {
    private HostServices hostServices;
    private NewsAggregatorClientController mainController;
    private int currentArticlePage = 1;
    private final int limit = 50;
//    private final GridPane newsContainer = new GridPane();
    InfiniteNews allNews;

    public ArticlesFrame (HostServices hostServices, NewsAggregatorClientController mainController) {
        super();
        resetArticlePage();
        this.hostServices = hostServices;
        this.mainController = mainController;
        this.setFitToHeight(true);

    }

    public synchronized void loadArticles() {
        NewsCategoryGroupTitledPane latestNews = new NewsCategoryGroupTitledPane("Latest news");
        NewsCategoryGroupTitledPane bitcoinNews = new NewsCategoryGroupTitledPane("Bitcoin news");
        NewsCategoryGroupTitledPane ethereumNews = new NewsCategoryGroupTitledPane("Ethereum news");
        itemsContainer.add(latestNews, 0, 0, 2, 1);
        itemsContainer.add(bitcoinNews, 0, 1, 1, 1);
        itemsContainer.add(ethereumNews, 1, 1, 1, 1);
        allNews = new InfiniteNews("All news");
        allNews.getLoadMoreButton().setOnAction(event -> loadMoreArticles());
        itemsContainer.add(allNews, 0, 2, 2, 1);
        AtomicReference<NewsJSONLoader> articleDataLoader = new AtomicReference<>();
        Platform.runLater(() -> {
            articleDataLoader.set(getNewsJSONLoader());
            if (articleDataLoader.get().getJSONString().isEmpty()) {
                System.out.println("Data is empty");
            }
            else {
                List<NewsItemData> data = articleDataLoader.get().getNewsItemDataList(limit, 0);
                new Thread(() -> Platform.runLater(() -> {
                    ArticleItemsLoader articleItemsLoader = new ArticleItemsLoader(20, 0, hostServices, latestNews, this);
                    articleItemsLoader.loadItems(data);
                })).start();
                new Thread(() -> Platform.runLater(() -> {
                    ArticleItemsLoader allArticleItemsLoader = new ArticleItemsLoader(50, 0, hostServices, allNews, this);
                    allArticleItemsLoader.loadItems(data);
                })).start();
            }
        });
    }

    private synchronized @NotNull NewsJSONLoader getNewsJSONLoader() {
        NewsJSONLoader articleDataLoader = new NewsJSONLoader();
        articleDataLoader.setPageNumber(currentArticlePage);
        articleDataLoader.setLimit(limit);
        articleDataLoader.loadJSON();
        return articleDataLoader;
    }

    public void clearAllNews() {
        itemsContainer.getChildren().clear();
    }

    private synchronized void loadMoreArticles(){
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.show();
        System.out.println("Loading more articles");
        nextArticlePage();
        try {
            System.out.println("Current article page: " + currentArticlePage);
            NewsJSONLoader articleDataLoader = getNewsJSONLoader();
            Platform.runLater(() -> {
                List<NewsItemData> data = articleDataLoader.getNewsItemDataList(limit, 0);
                ArticleItemsLoader articleItemsLoader = new ArticleItemsLoader(limit, 0, hostServices, allNews, this);
                articleItemsLoader.loadItems(data);
            });
            loadingDialog.close();
        }
        catch (Exception e){
            // Error, return to old page number
            e.printStackTrace();
            currentArticlePage--;
        }
    }

    public void resetArticlePage() {
        currentArticlePage = 1;
    }

    public void nextArticlePage() {
        currentArticlePage+=1;
    }

    public void setSearchText(String text){
        mainController.setSearchText(text);
    }

}
