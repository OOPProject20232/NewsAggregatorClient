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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ArticlesFrame extends GenericFrame {
    private HostServices hostServices;
    private NewsAggregatorClientController mainController;
    private final int limit = 30;
    private final int chunkSize = 10;
    private AtomicReference<Integer> currentChunk = new AtomicReference<>(30/10);

//    private final GridPane newsContainer = new GridPane();
    InfiniteNews allNews;

    public ArticlesFrame (HostServices hostServices, NewsAggregatorClientController mainController) {
        super();
        resetPage();
        this.hostServices = hostServices;
        this.mainController = mainController;
        this.setFitToHeight(true);
        this.setPannable(true);
    }

    public synchronized void loadArticles() throws NoRouteToHostException {
        NewsCategoryGroupTitledPane latestNews = new NewsCategoryGroupTitledPane("Latest news");
        NewsCategoryGroupTitledPane bitcoinNews = new NewsCategoryGroupTitledPane("Bitcoin news");
        NewsCategoryGroupTitledPane ethereumNews = new NewsCategoryGroupTitledPane("Ethereum news");
        NewsCategoryGroupTitledPane solanaNews = new NewsCategoryGroupTitledPane("Solana news");
        NewsCategoryGroupTitledPane defiNews = new NewsCategoryGroupTitledPane("DeFI news");
        NewsCategoryGroupTitledPane web3News = new NewsCategoryGroupTitledPane("Web3 news");
        NewsCategoryGroupTitledPane blockchainNews = new NewsCategoryGroupTitledPane("Blockchain news");
        itemsContainer.add(latestNews, 0, 0, 3, 2);
        itemsContainer.add(bitcoinNews, 0, 2, 1, 2);
        itemsContainer.add(ethereumNews, 1, 2, 1, 2);
        itemsContainer.add(solanaNews, 0, 5, 1, 2);
        itemsContainer.add(defiNews, 1, 5, 1, 2);
        itemsContainer.add(web3News, 0, 8, 1, 2);
        itemsContainer.add(blockchainNews, 1, 8, 1, 2);
        NewsCategoryGroupTitledPane[] newsCategoryGroupTitledPanes = {bitcoinNews, ethereumNews, solanaNews, defiNews, web3News, blockchainNews};
        allNews = new InfiniteNews("All news");
        allNews.getLoadMoreButton().setOnAction(event -> loadMoreArticles());
        itemsContainer.add(allNews, 0, 15, 3, 2);
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
                List<NewsItemData> data = articleDataLoader.get().getNewsItemDataList(limit, 0, articleDataLoader.get().getJsonObject());
                new Thread(() -> Platform.runLater(() -> {
                    ArticleItemsLoader<NewsCategoryGroupTitledPane> articleItemsLoader = new ArticleItemsLoader<>(10, 0, hostServices, latestNews, mainController);
                    articleItemsLoader.loadItems(data);
                })).start();
                new Thread(() -> Platform.runLater(() -> {
                    ArticleItemsLoader<NewsCategoryGroupTitledPane> allArticleItemsLoader = new ArticleItemsLoader<>(30, 0, hostServices, allNews, mainController);
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
        new Thread(() -> Platform.runLater(() -> {
            newsCategoryJSONLoader.set(getNewsCategoryJSONLoader("solana"));
            getNewsCategory(solanaNews, newsCategoryJSONLoader);
        })).start();
        new Thread(() -> Platform.runLater(() -> {
            newsCategoryJSONLoader.set(getNewsCategoryJSONLoader("defi"));
            getNewsCategory(defiNews, newsCategoryJSONLoader);
        })).start();
        new Thread(() -> Platform.runLater(() -> {
            newsCategoryJSONLoader.set(getNewsCategoryJSONLoader("web3"));
            getNewsCategory(web3News, newsCategoryJSONLoader);
        })).start();
        new Thread(() -> Platform.runLater(() -> {
            newsCategoryJSONLoader.set(getNewsCategoryJSONLoader("blockchain"));
            getNewsCategory(blockchainNews, newsCategoryJSONLoader);
        })).start();
        setOnScroll(event -> {
            if (getVvalue() > .4) {
                loadMoreArticles();
            }
        });
//        setOnScrollFinished(event -> {
//            if (getVvalue() >= .5) {
//                loadMoreArticles();
//            }
//        });
    }

    private synchronized void getNewsCategory(NewsCategoryGroupTitledPane ethereumNews, AtomicReference<NewsCategoryJSONLoader> newsCategoryJSONLoader) {
        if (newsCategoryJSONLoader.get().getJSONString().isEmpty()) {
            System.out.println("Data is empty");
        }
        else {
            List<NewsItemData> data = newsCategoryJSONLoader.get().getNewsItemDataList(5, 0, newsCategoryJSONLoader.get().getJSONObject());
            System.out.println("Data size: " + data.size());
                ArticleItemsLoader<NewsCategoryGroupTitledPane> articleItemsLoader = new ArticleItemsLoader<>(5, 0, hostServices, ethereumNews, mainController);
                articleItemsLoader.setContainingSummary(false);
                articleItemsLoader.setContainingCategories(false);
            Platform.runLater(() -> {
                articleItemsLoader.loadItems(data);
            });
        }
    }

    private synchronized @NotNull NewsJSONLoader getNewsJSONLoader() throws NoRouteToHostException {
        NewsJSONLoader articleDataLoader = new NewsJSONLoader();
        articleDataLoader.setPageNumber(currentPage);
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
        if (currentChunk.get() * chunkSize >= limit) {
            nextPage();
            currentChunk.set(0);
        }
        System.out.println("Current article page: " + currentPage);
        System.out.println("Current chunk: " + currentChunk);
        System.out.println("Current chunk size: " + chunkSize);
//        LoadingDialog loadingDialog = new LoadingDialog();
//        loadingDialog.show();
        System.out.println("Loading more articles");
        try {
            System.out.println("Current article page: " + currentPage);
            NewsJSONLoader articleDataLoader = getNewsJSONLoader();
            Platform.runLater(() -> {
                List<NewsItemData> data = articleDataLoader.getNewsItemDataList(limit, 0, articleDataLoader.getJsonObject());
                ArticleItemsLoader<InfiniteNews> articleItemsLoader = new ArticleItemsLoader<>(chunkSize, currentChunk.get() * chunkSize, hostServices, allNews, mainController);
                articleItemsLoader.loadItems(data);
            });
            currentChunk.set(currentChunk.get() + 1);
//            loadingDialog.close();
        }
        catch (NoRouteToHostException e){

        }
        catch (ArrayIndexOutOfBoundsException e){
            // Error, return to old page number
            e.printStackTrace();
            previousPage();
        }
    }

    public void setSearchText(String text){
        mainController.setSearchText(text);
    }

}
