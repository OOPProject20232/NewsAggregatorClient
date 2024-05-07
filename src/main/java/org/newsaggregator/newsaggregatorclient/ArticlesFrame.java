package org.newsaggregator.newsaggregatorclient;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import org.controlsfx.control.spreadsheet.Grid;
import org.jetbrains.annotations.NotNull;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.downloaders.NewsRetriever;
import org.newsaggregator.newsaggregatorclient.jsonparsing.NewsJSONLoader;
import org.newsaggregator.newsaggregatorclient.ui_component.datacard.CoinNewestPriceGroupFrame;
import org.newsaggregator.newsaggregatorclient.ui_component.datacard.InfiniteNews;
import org.newsaggregator.newsaggregatorclient.ui_component.datacard.NewsCategoryGroupTitledPane;
import org.newsaggregator.newsaggregatorclient.ui_component.datacard.NewsItemCard;
import org.newsaggregator.newsaggregatorclient.ui_component.uiloader.ArticleItemsLoader;
import org.newsaggregator.newsaggregatorclient.util.CreateJSONCache;

import java.net.MalformedURLException;
import java.util.List;

import static java.lang.Long.MAX_VALUE;

public class ArticlesFrame extends ScrollPane {
    private HostServices hostServices;

    private int currentArticlePage = 1;
    private final int limit = 50;
    private GridPane newsContainer = new GridPane();
    InfiniteNews allNews;

    private final String JSON_FOLDER_PATH = "src/main/resources/json/";

    public ArticlesFrame (HostServices hostServices) {
        resetArticlePage();
        this.hostServices = hostServices;
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
    }

    public void loadArticles() {
        NewsCategoryGroupTitledPane latestNews = new NewsCategoryGroupTitledPane("Latest news");
        NewsCategoryGroupTitledPane bitcoinNews = new NewsCategoryGroupTitledPane("Bitcoin news");
        NewsCategoryGroupTitledPane ethereumNews = new NewsCategoryGroupTitledPane("Ethereum news");
        newsContainer.add(latestNews, 0, 0, 2, 1);
        newsContainer.add(bitcoinNews, 0, 1, 1, 1);
        newsContainer.add(ethereumNews, 1, 1, 1, 1);
        allNews = new InfiniteNews("All news");
        allNews.getLoadMoreButton().setOnAction(event -> loadMoreArticles());
        newsContainer.add(allNews, 0, 2, 2, 1);
        NewsJSONLoader articleDataLoader = null;
        try {
            articleDataLoader = getNewsJSONLoader();
        }
        catch (Exception ex) {
            CreateJSONCache.createFolder(JSON_FOLDER_PATH);
            NewsRetriever newsRetriever = new NewsRetriever();
            newsRetriever.setLimit(50);
            newsRetriever.setPageNumber(currentArticlePage);
            try {
                newsRetriever.downloadCache(true, "news.json");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            articleDataLoader = getNewsJSONLoader();
        }
        if (articleDataLoader.getJSONString().isEmpty()) {
            System.out.println("Data is empty");
        }
        else {
            List<NewsItemData> data = articleDataLoader.getNewsItemDataList(limit, 0);
            new Thread(() -> Platform.runLater(() -> {
                ArticleItemsLoader articleItemsLoader = new ArticleItemsLoader(20, 0, hostServices, latestNews);
                articleItemsLoader.loadItems(data);
            })).start();
            new Thread(() -> Platform.runLater(() -> {
                ArticleItemsLoader allArticleItemsLoader = new ArticleItemsLoader(50, 0, hostServices, allNews);
                allArticleItemsLoader.loadItems(data);
            })).start();
        }
    }

    private synchronized @NotNull NewsJSONLoader getNewsJSONLoader() {
        NewsJSONLoader articleDataLoader = new NewsJSONLoader();
        articleDataLoader.setCacheFileName("news.json");
        CreateJSONCache.createFolder(JSON_FOLDER_PATH);
        articleDataLoader.loadJSON();
        String newsString = articleDataLoader.getJSONString();
        if (newsString.isEmpty()) {
            NewsRetriever newsRetriever = new NewsRetriever();
            newsRetriever.setLimit(50);
            newsRetriever.setPageNumber(1);
            try {
                newsRetriever.downloadCache(true, "news.json");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            articleDataLoader.loadJSON();
//            new Thread(articleDataLoader::loadJSON).start();
        }
        return articleDataLoader;
    }

    public void clearAllNews() {
        newsContainer.getChildren().clear();
    }

    private void loadMoreArticles(){
        System.out.println("Loading more articles");
        nextArticlePage();
        System.out.println("Current article page: " + currentArticlePage);
        NewsRetriever articleRetriever = new NewsRetriever();
        articleRetriever.setLimit(50);
        articleRetriever.setPageNumber(currentArticlePage);
        articleRetriever.setForceDownload(true);
        try {
            articleRetriever.downloadCache(true, "news.json");
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
        NewsJSONLoader articleDataLoader = getNewsJSONLoader();
        List<NewsItemData> data = articleDataLoader.getNewsItemDataList(limit, 0);
        new Thread(() -> Platform.runLater(() -> {
            for (NewsItemData newsItemData : data) {
                NewsItemCard newsItem = new NewsItemCard(newsItemData);
                newsItem.setText();
                newsItem.getArticleHyperlinkTitleObject().setOnAction(
                        event -> hostServices.showDocument(newsItemData.getUrl())
                );
                allNews.addItem(newsItem);
                new Thread(() -> Platform.runLater(newsItem::setImage)).start();
            }
        })).start();
    }

    public void resetArticlePage() {
        currentArticlePage = 1;
    }

    public void nextArticlePage() {
        currentArticlePage+=1;
    }

}
