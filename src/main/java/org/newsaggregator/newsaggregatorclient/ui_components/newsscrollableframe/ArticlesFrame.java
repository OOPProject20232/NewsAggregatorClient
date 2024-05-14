package org.newsaggregator.newsaggregatorclient.ui_components.newsscrollableframe;

import javafx.application.HostServices;
import javafx.application.Platform;
import org.jetbrains.annotations.NotNull;
import org.newsaggregator.newsaggregatorclient.NewsAggregatorClientController;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.jsonparsing.NewsCategoryJSONLoader;
import org.newsaggregator.newsaggregatorclient.jsonparsing.NewsJSONLoader;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.InfiniteNews;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.NewsCategoryGroupTitledPane;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.LoadingDialog;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.NoInternetDialog;
import org.newsaggregator.newsaggregatorclient.ui_components.uiloader.ArticleItemsLoader;

import java.net.NoRouteToHostException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ArticlesFrame extends GenericFrame {
    private HostServices hostServices;
    private NewsAggregatorClientController mainController;
    private int currentArticlePage = 1;
    private final int limit = 30;
//    private final GridPane newsContainer = new GridPane();
    InfiniteNews allNews;

    public ArticlesFrame (HostServices hostServices, NewsAggregatorClientController mainController) {
        super();
        resetArticlePage();
        this.hostServices = hostServices;
        this.mainController = mainController;
        this.setFitToHeight(true);
        this.setPannable(true);
    }

    public synchronized void loadArticles() throws NoRouteToHostException {
        NewsCategoryGroupTitledPane latestNews = new NewsCategoryGroupTitledPane("Latest news");
        NewsCategoryGroupTitledPane bitcoinNews = new NewsCategoryGroupTitledPane("Bitcoin news");
        NewsCategoryGroupTitledPane ethereumNews = new NewsCategoryGroupTitledPane("Ethereum news");
        itemsContainer.add(latestNews, 0, 0, 2, 2);
        itemsContainer.add(bitcoinNews, 0, 2, 1, 2);
        itemsContainer.add(ethereumNews, 1, 2, 1, 2);
        allNews = new InfiniteNews("All news");
        allNews.getLoadMoreButton().setOnAction(event -> loadMoreArticles());
        itemsContainer.add(allNews, 0, 6, 2, 2);
        AtomicReference<NewsJSONLoader> articleDataLoader = new AtomicReference<>();
        AtomicReference<NewsCategoryJSONLoader> newsCategoryJSONLoader = new AtomicReference<>();
        new Thread(() -> Platform.runLater(() -> {
            try {
                articleDataLoader.set(getNewsJSONLoader());
            } catch (NoRouteToHostException e) {
                NoInternetDialog noInternetDialog = new NoInternetDialog();
                noInternetDialog.show();
            }
            if (articleDataLoader.get().getJSONString().isEmpty()) {
                System.out.println("Data is empty");
            }
            else {
                List<NewsItemData> data = articleDataLoader.get().getNewsItemDataList(limit, 0);
                new Thread(() -> Platform.runLater(() -> {
                    ArticleItemsLoader articleItemsLoader = new ArticleItemsLoader(10, 0, hostServices, latestNews, this);
                    articleItemsLoader.loadItems(data);
                })).start();
                new Thread(() -> Platform.runLater(() -> {
                    ArticleItemsLoader allArticleItemsLoader = new ArticleItemsLoader(30, 0, hostServices, allNews, this);
                    allArticleItemsLoader.loadItems(data);
                })).start();
            }
        })).start();
        new Thread(() -> Platform.runLater(() -> {
            newsCategoryJSONLoader.set(getNewsCategoryJSONLoader("bitcoin"));
            getNewsCategory(bitcoinNews, newsCategoryJSONLoader);
        })).start();
        new Thread(() -> Platform.runLater(() -> {
            newsCategoryJSONLoader.set(getNewsCategoryJSONLoader("ethereum"));
            getNewsCategory(ethereumNews, newsCategoryJSONLoader);
        })).start();
    }

    private synchronized void getNewsCategory(NewsCategoryGroupTitledPane ethereumNews, AtomicReference<NewsCategoryJSONLoader> newsCategoryJSONLoader) {
        if (newsCategoryJSONLoader.get().getJSONString().isEmpty()) {
            System.out.println("Data is empty");
        }
        else {
            List<NewsItemData> data = newsCategoryJSONLoader.get().getNewsItemDataList(limit, 0);
                ArticleItemsLoader<NewsCategoryGroupTitledPane> articleItemsLoader = new ArticleItemsLoader(5, 0, hostServices, ethereumNews, this);
                articleItemsLoader.setContainingSummary(false);
                articleItemsLoader.setContainingCategories(false);
            Platform.runLater(() -> {
                articleItemsLoader.loadItems(data);
            });
        }
    }

    private synchronized @NotNull NewsJSONLoader getNewsJSONLoader() throws NoRouteToHostException {
        NewsJSONLoader articleDataLoader = new NewsJSONLoader();
        articleDataLoader.setPageNumber(currentArticlePage);
        articleDataLoader.setLimit(limit);
        articleDataLoader.loadJSON();
        return articleDataLoader;
    }

    private synchronized NewsCategoryJSONLoader getNewsCategoryJSONLoader(String category) {
        NewsCategoryJSONLoader articleDataLoader = new NewsCategoryJSONLoader();
        articleDataLoader.setCategory(category);
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
        catch (NoRouteToHostException e){

        }
        catch (Exception e){
            // Error, return to old page number
            e.printStackTrace();
            currentArticlePage--;
        }
    }

    public void resetArticlePage() {
        currentArticlePage = 2;
    }

    public void nextArticlePage() {
        currentArticlePage+=1;
    }

    public void setSearchText(String text){
        mainController.setSearchText(text);
    }

}
