package org.newsaggregator.newsaggregatorclient;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import org.newsaggregator.newsaggregatorclient.datamodel.CoinPriceData;
import org.newsaggregator.newsaggregatorclient.jsonparsing.CoinPriceJSONLoader;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.CoinNewestPriceGroupTitledPane;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.LoadingDialog;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.NoInternetDialog;
import org.newsaggregator.newsaggregatorclient.ui_components.newsscrollableframe.ArticlesFrame;
import org.newsaggregator.newsaggregatorclient.ui_components.newsscrollableframe.RedditFrame;
import org.newsaggregator.newsaggregatorclient.ui_components.uiloader.CoinNewestPriceItemsLoader;

import java.net.NoRouteToHostException;
import java.util.List;

public class NewsAggregatorClientController {
    /**
     * Controller chính của ứng dụng, chứa các hàm xử lý sự kiện và dữ liệu
     * Được gọi khi ứng dụng được khởi chạy
     */

    // Các thuộc tính có @FXML có tên trùng với các thành phần được đánh dấu fx:id trong file FXML
    @FXML
    private AnchorPane newsArticlesPane;

    @FXML
    private AnchorPane redditAnchorPane;

    @FXML
    @NotNull
    private HBox newsDivider;

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

    @FXML
    private TabPane mainTabPane;

    private final HostServices hostServices;

    private int currentArticlePage = 1;
    private int currentRedditPage = 1;
    private final static int coinLimit = 20;

    private final String JSON_FOLDER_PATH = "src/main/resources/json/";
    private CoinPriceJSONLoader coinPriceJSONLoader;
    private final ArticlesFrame articleScrollPane;
    private NewsSearchController newsSearchController = new NewsSearchController();
    private final RedditFrame redditFrame;

    public NewsAggregatorClientController(HostServices hostServices) {
        this.hostServices = hostServices;
        this.articleScrollPane = new ArticlesFrame(hostServices, this);
        this.redditFrame = new RedditFrame(hostServices, this);
        newsSearchController.setHostServices(hostServices);
        newsSearchController.setMainController(this);
    }

    @FXML
    public synchronized void start(){
        reloadNews.setOnAction(event -> reloadNews());
        AnchorPane.setBottomAnchor(articleScrollPane, 0.0);
        AnchorPane.setTopAnchor(articleScrollPane, 0.0);
        AnchorPane.setLeftAnchor(articleScrollPane, 0.0);
        AnchorPane.setRightAnchor(articleScrollPane, 0.0);
        AnchorPane.setBottomAnchor(redditFrame, 0.0);
        AnchorPane.setTopAnchor(redditFrame, 0.0);
        AnchorPane.setLeftAnchor(redditFrame, 0.0);
        AnchorPane.setRightAnchor(redditFrame, 0.0);
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
            if (!redditFrame.isLoaded()) {
                redditFrame.loadReddit();
            }
        });
        Tooltip newsTooltip = new Tooltip("Latest news");
        newsTooltip.setShowDelay(Duration.millis(10));
        newsTab.setTooltip(newsTooltip);
        searchTab.setOnSelectionChanged(event -> {
            if (searchTab.isSelected()) {
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
        Tooltip searchTooltip = new Tooltip("Search news");
        searchTooltip.setShowDelay(Duration.millis(10));
        searchTab.setTooltip(searchTooltip);
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
        Tooltip marketTooltip = new Tooltip("Market data");
        marketTooltip.setShowDelay(Duration.millis(10));
        marketDataTab.setTooltip(marketTooltip);
        newsArticlesPane.getChildren().add(articleScrollPane);
        redditAnchorPane.getChildren().add(redditFrame);
    }

    protected synchronized void showAllNewsCategories() {
        /**
         * Hàm này sẽ hiển thị tất cả các tin tức theo từng danh mục,
         * được gọi khi ứng dụng được khởi chạy
         * Dữ liệu tin tức sẽ được lấy từ database thông qua trung gian
         */
//        LoadingDialog loadingDialog = new LoadingDialog();
        Platform.runLater(() -> {
            try {
                articleScrollPane.loadArticles();
            } catch (NoRouteToHostException e) {
//                loadingDialog.close();
                NoInternetDialog noInternetDialog = new NoInternetDialog();
                noInternetDialog.show();
            }
//            loadingDialog.close();

        });
        CoinNewestPriceGroupTitledPane coinNewestPriceGroupFrame = new CoinNewestPriceGroupTitledPane();
        coinNewestPriceGroupFrame.getStylesheets().add(NewsAggregatorClientController.class.getResource("assets/css/main.css").toExternalForm());

        coinPriceJSONLoader = getCoinPriceJSONLoader();

        if (coinPriceJSONLoader.getJSONString().isEmpty()) {
            System.out.println("Data is empty");
        }
        else {
            additionalInfoContainer.getChildren().clear();
//            coinNewestPriceGroupFrame.addAllCoins(coinPriceJSONLoader);
            additionalInfoContainer.getChildren().add(coinNewestPriceGroupFrame);
            List<CoinPriceData> coinData = coinPriceJSONLoader.getNewestCoinPrices();
            new Thread(() -> Platform.runLater(() -> {
                System.out.println("\u001B[35m" + "Loading coin items" + "\u001B[0m");
                CoinNewestPriceItemsLoader coinNewestPriceItemsLoader = new CoinNewestPriceItemsLoader(coinNewestPriceGroupFrame, hostServices);
                coinNewestPriceItemsLoader.loadItems(coinData);
//                additionalInfoContainer.getChildren().add(coins);
                System.out.println(coinNewestPriceItemsLoader.getItems());
            })).start();
        }

    }

    private synchronized CoinPriceJSONLoader getCoinPriceJSONLoader() {
        CoinPriceJSONLoader coinPriceJSONLoader = new CoinPriceJSONLoader();
        coinPriceJSONLoader.setLimit(coinLimit);
        coinPriceJSONLoader.loadJSON();
        return coinPriceJSONLoader;
    }

    private void reloadNews() {
        /**
         * Hàm này sẽ load lại tất cả các tin tức hiển thị trên màn hình chính
         * Được gọi khi người dùng click vào nút "tải lại" trên màn hình chính
         * @param event: Sự kiện click chuột vào nút "tải lại"
         */
        SelectionModel<Tab> newsTypeTabs = newsTypeTabPane.getSelectionModel();
        LoadingDialog loadingDialog = new LoadingDialog();
//        loadingDialog.show();
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (newsTypeTabs.getSelectedIndex() == 0) {
                    articleScrollPane.clearAllNews();
                    articleScrollPane.resetPage();
                    try {
                        articleScrollPane.loadArticles();
                    } catch (NoRouteToHostException e) {
                        NoInternetDialog noInternetDialog = new NoInternetDialog();
                        noInternetDialog.show();
                        loadingDialog.close();
                    }
                    showAllNewsCategories();
                    loadingDialog.close();
                }
                else {
                    // Reddit
                    redditFrame.resetPage();
                    redditFrame.loadReddit();
                    loadingDialog.close();
                }
                return null;
            }
        };
        task.setOnRunning(e -> loadingDialog.show());
        task.setOnSucceeded(e -> loadingDialog.hide());
        task.run();

        // New
    }

    public void setSearchText(String text){
        SelectionModel<Tab> mainTabs = mainTabPane.getSelectionModel();
        mainTabs.select(searchTab);
        newsSearchController.insertSearchText(text, "articles", "Newest", "categories", "e");
    }
}