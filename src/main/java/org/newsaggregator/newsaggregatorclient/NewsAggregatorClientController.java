package org.newsaggregator.newsaggregatorclient;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.newsaggregator.newsaggregatorclient.datamodel.CoinPriceData;
import org.newsaggregator.newsaggregatorclient.downloaders.CoinPriceRetriever;
import org.newsaggregator.newsaggregatorclient.downloaders.NewsRetriever;
import org.newsaggregator.newsaggregatorclient.jsonparsing.CoinPriceJSONLoader;
import org.newsaggregator.newsaggregatorclient.jsonparsing.NewsJSONLoader;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.ui_component.datacard.CoinNewestPriceGroupFrame;
import org.newsaggregator.newsaggregatorclient.ui_component.datacard.InfiniteNews;
import org.newsaggregator.newsaggregatorclient.ui_component.uiloader.ArticleItemsLoader;
import org.newsaggregator.newsaggregatorclient.ui_component.datacard.NewsCategoryGroupTitledPane;
import org.newsaggregator.newsaggregatorclient.ui_component.uiloader.CoinItemsLoader;
import org.newsaggregator.newsaggregatorclient.util.CreateJSONCache;

import java.net.MalformedURLException;
import java.util.List;

public class NewsAggregatorClientController {
    /**
     * Controller chính của ứng dụng, chứa các hàm xử lý sự kiện và dữ liệu
     * Được gọi khi ứng dụng được khởi chạy
     */

//    @FXML
//    private GridPane newsContainer;
    // Các thuộc tính có @FXML có tên trùng với các thành phần được đánh dấu fx:id trong file FXML
    @FXML
    private AnchorPane newsArticlesPane;

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

    @FXML
    private VBox additionalInfoContainer;

    private final HostServices hostServices;

    private int currentArticlePage = 1;
    private int currentRedditPage = 1;
    private final int limit = 50;

    private final String JSON_FOLDER_PATH = "src/main/resources/json/";
    private CoinPriceJSONLoader coinPriceJSONLoader;
    private final ArticlesFrame articleScrollPane;

    public NewsAggregatorClientController(HostServices hostServices) {
        this.hostServices = hostServices;
        this.articleScrollPane = new ArticlesFrame(hostServices);
    }

    public void start(){
        newsSplitPane.setDividerPositions(1);
        reloadNews.setOnAction(event -> reloadNews());
        newsArticlesPane.getChildren().add(articleScrollPane);
        AnchorPane.setBottomAnchor(articleScrollPane, 0.0);
        AnchorPane.setTopAnchor(articleScrollPane, 0.0);
        AnchorPane.setLeftAnchor(articleScrollPane, 0.0);
        AnchorPane.setRightAnchor(articleScrollPane, 0.0);
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
        resetArticlePage();
        resetRedditPage();
        articleScrollPane.loadArticles();
        CoinNewestPriceGroupFrame coinNewestPriceGroupFrame = new CoinNewestPriceGroupFrame();
        coinNewestPriceGroupFrame.getStylesheets().add(NewsAggregatorClientController.class.getResource("assets/css/main.css").toExternalForm());
        additionalInfoContainer.getChildren().add(coinNewestPriceGroupFrame);

        try {
            coinPriceJSONLoader = getCoinPriceJSONLoader();
        }
        catch (Exception ex) {
            CreateJSONCache.createFolder(JSON_FOLDER_PATH);
            CoinPriceRetriever coinPriceRetriever = new CoinPriceRetriever();
            try {
                coinPriceRetriever.downloadCache(true, "coins.json");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            coinPriceJSONLoader = getCoinPriceJSONLoader();
        }

        if (coinPriceJSONLoader.getJSONString().isEmpty()) {
            System.out.println("Data is empty");
        }
        else {
            additionalInfoContainer.getChildren().clear();
            List<CoinPriceData> coinData = coinPriceJSONLoader.getNewestCoinPrices();
            new Thread(() -> Platform.runLater(() -> {
                System.out.println("\u001B[35m" + "Loading coin items" + "\u001B[0m");
                CoinItemsLoader coinItemsLoader = new CoinItemsLoader(coinNewestPriceGroupFrame, hostServices);
                CoinNewestPriceGroupFrame coins =  coinItemsLoader.loadItems(coinData);
                additionalInfoContainer.getChildren().add(coins);
                System.out.println(coinItemsLoader.getItems());
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

    private synchronized CoinPriceJSONLoader getCoinPriceJSONLoader() {
        CoinPriceJSONLoader coinPriceJSONLoader = new CoinPriceJSONLoader();
        coinPriceJSONLoader.setCacheFileName("coins.json");
        CreateJSONCache.createFolder(JSON_FOLDER_PATH);
        coinPriceJSONLoader.loadJSON();
        String coinString = coinPriceJSONLoader.getJSONString();
        if (coinString.isEmpty()) {
            CoinPriceRetriever coinPriceRetriever = new CoinPriceRetriever();
            try {
                coinPriceRetriever.downloadCache(true, "coins.json");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            coinPriceJSONLoader.loadJSON();
        }
        return coinPriceJSONLoader;
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
            articleRetriever.setForceDownload(true);
            CoinPriceRetriever coinPriceRetriever = new CoinPriceRetriever();
            try {
                articleRetriever.downloadCache(true, "news.json");
                coinPriceRetriever.downloadCache(false, "coins.json");
                Platform.runLater(() -> {
                    articleScrollPane.clearAllNews();
                    showAllNewsCategories();
                });
            } catch (MalformedURLException ex) {
                throw new RuntimeException(ex);
            }
        }).start();
    }
}