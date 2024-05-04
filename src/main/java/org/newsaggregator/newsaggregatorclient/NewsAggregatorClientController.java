package org.newsaggregator.newsaggregatorclient;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.newsaggregator.newsaggregatorclient.downloaders.NewsRetriever;
import org.newsaggregator.newsaggregatorclient.jsonparsing.NewsCategoryJSONLoader;
import org.newsaggregator.newsaggregatorclient.jsonparsing.NewsJSONLoader;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.ui_component.uiloader.ArticleItemsLoader;
import org.newsaggregator.newsaggregatorclient.ui_component.datacard.NewsCategoryGroupTitledPane;
import org.newsaggregator.newsaggregatorclient.util.CreateJSONCache;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class NewsAggregatorClientController {
    /**
     * Controller chính của ứng dụng, chứa các hàm xử lý sự kiện và dữ liệu
     * Được gọi khi ứng dụng được khởi chạy
     */

    @FXML
    private GridPane newsContainer;
    @FXML
    protected SplitPane newsSplitPane;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private Button reloadNews;

    @FXML
    private Tab searchTab;

    @FXML
    private Tab newsTab;

    @FXML
    private Tab marketDataTab;

    @FXML
    private ToggleButton articleTabButton;

    @FXML
    private ToggleButton redditTabButton;

    @FXML
    private TabPane newsTypeTabPane;

    private final HostServices hostServices;

    private int currentPage = 1;
    private final int limit = 50;

    private final String JSON_FOLDER_PẠTH = "src/main/resources/json/";

    public NewsAggregatorClientController(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public void start(){
        newsSplitPane.setDividerPositions(1);
        newsContainer.getChildren().clear();
        reloadNews.setOnAction(event -> reloadNews());
        articleTabButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            ImageView articleIcon = new ImageView();
            articleIcon.setFitHeight(24);
            articleIcon.setFitWidth(24);
            Image iconImageByState = new Image(NewsAggregatorClientController.class.getResourceAsStream("assets/images/news.png"));
            if (newValue) {
                iconImageByState = new Image(NewsAggregatorClientController.class.getResourceAsStream("assets/images/news-black.png"));
            }
            articleIcon.setImage(iconImageByState);
            articleTabButton.setGraphic(articleIcon);
        });
        articleTabButton.setOnAction(event -> {
            newsTypeTabPane.getSelectionModel().select(0);
            if (newsContainer.getChildren().isEmpty()) {
                showAllNewsCategories();
            }
        });
        redditTabButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            ImageView redditIcon = new ImageView();
            redditIcon.setFitHeight(24);
            redditIcon.setFitWidth(24);
            Image iconImageByState = new Image(NewsAggregatorClientController.class.getResourceAsStream("assets/images/reddit.png"));
            if (newValue) {
                iconImageByState = new Image(NewsAggregatorClientController.class.getResourceAsStream("assets/images/reddit-black.png"));
            }
            redditIcon.setImage(iconImageByState);
            redditTabButton.setGraphic(redditIcon);
        });
        redditTabButton.setOnAction(event -> {
            newsTypeTabPane.getSelectionModel().select(1);
        });
        newsTab.setTooltip(new Tooltip("News"));
        searchTab.setOnSelectionChanged(event -> {
            if (searchTab.isSelected()) {
                NewsSearchController newsSearchController = new NewsSearchController();
                FXMLLoader fxmlLoader = new FXMLLoader(NewsAggregatorClientApplication.class.getResource("search.fxml"));
                fxmlLoader.setController(newsSearchController);
                try {
                    searchTab.setContent(fxmlLoader.load());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                newsSearchController.initialize();
            }
        });
        marketDataTab.setOnSelectionChanged(event -> {
            if (marketDataTab.isSelected()) {
                MarketDataController marketDataController = new MarketDataController();
                FXMLLoader fxmlLoader = new FXMLLoader(NewsAggregatorClientApplication.class.getResource("market_data.fxml"));
                fxmlLoader.setController(marketDataController);
                try {
                    marketDataTab.setContent(fxmlLoader.load());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                marketDataController.initialize();
            }
        });

    }

    protected synchronized void showAllNewsCategories() {
        /**
         * Hàm này sẽ hiển thị tất cả các tin tức theo từng danh mục,
         * được gọi khi ứng dụng được khởi chạy
         * Dữ liệu tin tức sẽ được lấy từ database thông qua trung gian
         */
//        newsContainer.getChildren().clear();
        NewsCategoryGroupTitledPane latestNews = new NewsCategoryGroupTitledPane("Latest news");
        NewsCategoryGroupTitledPane bitcoinNews = new NewsCategoryGroupTitledPane("Bitcoin news");
        NewsCategoryGroupTitledPane ethereumNews = new NewsCategoryGroupTitledPane("Ethereum news");
        newsContainer.add(latestNews, 0, 0, 2, 1);
        newsContainer.add(bitcoinNews, 0, 1, 1, 1);
        newsContainer.add(ethereumNews, 1, 1, 1, 1);
        NewsCategoryGroupTitledPane allNews = new NewsCategoryGroupTitledPane("All news");
        newsContainer.add(allNews, 0, 2, 2, 1);
        NewsJSONLoader articleDataLoader = null;
        try {
            articleDataLoader = getNewsJSONLoader();
        }
        catch (Exception ex) {
            CreateJSONCache.createFolder(JSON_FOLDER_PẠTH);
            NewsRetriever newsRetriever = new NewsRetriever();
            newsRetriever.setLimit(50);
            newsRetriever.setPageNumber(1);
            try {
                newsRetriever.sendRequest("articles", true, "news.json");
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
                ArticleItemsLoader articleItemsLoader = new ArticleItemsLoader(20, 0, newsContainer, hostServices, latestNews);
                articleItemsLoader.loadItems(data);
            })).start();
            new Thread(() -> Platform.runLater(() -> {
                ArticleItemsLoader allArticleItemsLoader = new ArticleItemsLoader(50, 0, newsContainer, hostServices, allNews);
                allArticleItemsLoader.loadItems(data);
            })).start();
        }
    }

    private synchronized @NotNull NewsJSONLoader getNewsJSONLoader() {
        NewsJSONLoader articleDataLoader = new NewsJSONLoader();
        articleDataLoader.setCacheFileName("news.json");
        CreateJSONCache.createFolder(JSON_FOLDER_PẠTH);
        articleDataLoader.loadJSON();
        String newsString = articleDataLoader.getJSONString();
        if (newsString.isEmpty()) {
            NewsRetriever newsRetriever = new NewsRetriever();
            newsRetriever.setLimit(50);
            newsRetriever.setPageNumber(1);
            try {
                newsRetriever.sendRequest("articles", true, "news.json");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            articleDataLoader.loadJSON();
//            new Thread(articleDataLoader::loadJSON).start();
        }
        return articleDataLoader;
    }

    private static @NotNull NewsCategoryJSONLoader getNewsCategoryJSONLoader(String category){
        NewsCategoryJSONLoader categoryDataLoader = new NewsCategoryJSONLoader();
        categoryDataLoader.setCacheFileName(category + ".json");
        categoryDataLoader.setCategory(category);
        Thread jsonThread = new Thread(() -> {
            try {
                NewsRetriever newsRetriever = new NewsRetriever();
                newsRetriever.setForceDownload(true);
                try {
                    newsRetriever.sendRequest("v1/categories/articles/search?text=%s".formatted(category), false, category + ".json");
                    categoryDataLoader.loadJSON();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                categoryDataLoader.loadJSON();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        jsonThread.start();
        return categoryDataLoader;
    }

    private void reloadNews() {
        /**
         * Hàm này sẽ load lại tất cả các tin tức hiển thị trên màn hình chính
         * Được gọi khi người dùng click vào nút "tải lại" trên màn hình chính
         * @param event: Sự kiện click chuột vào nút "tải lại"
         */
        new Thread (() -> {
            NewsRetriever articleRetriever = new NewsRetriever();
            articleRetriever.setLimit(50);
            articleRetriever.setPageNumber(1);
            articleRetriever.setForceDownload(false);
            try {
                articleRetriever.sendRequest("articles", true, "news.json");
                Platform.runLater(() -> {
                    newsContainer.getChildren().clear();
                    showAllNewsCategories();
                });
            } catch (MalformedURLException ex) {
                throw new RuntimeException(ex);
            }
        }).start();
    }

    public void resetPage() {
        currentPage = 1;
    }

    public void nextPage() {
        currentPage++;
    }
}