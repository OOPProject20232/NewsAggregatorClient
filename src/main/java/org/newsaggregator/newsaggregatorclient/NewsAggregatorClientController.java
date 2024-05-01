package org.newsaggregator.newsaggregatorclient;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import org.jetbrains.annotations.NotNull;
import org.newsaggregator.newsaggregatorclient.downloaders.NewsRetriever;
import org.newsaggregator.newsaggregatorclient.jsonparsing.NewsCategoryJSONLoader;
import org.newsaggregator.newsaggregatorclient.jsonparsing.NewsJSONLoader;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.ui_component.uiloader.ArticleItemsLoader;
import org.newsaggregator.newsaggregatorclient.ui_component.datacard.NewsCategoryGroupTitledPane;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class NewsAggregatorClientController {
    /**
     * Controller chính của ứng dụng, chứa các hàm xử lý sự kiện và dữ liệu
     * Được gọi khi ứng dụng được khởi chạy
     */

    @FXML
    private WebView articleWebView;
    @FXML
    private GridPane newsContainer;
    @FXML
    protected SplitPane newsSplitPane;
    @FXML
    private Label addressBar;

    @FXML
    private VBox articleViewer;

    @FXML
    private Button reloadButton;

    @FXML
    private Button closeButton;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private Button reloadNews;

    @FXML
    private Tab searchTab;

    @FXML
    private Tab marketDataTab;

    private final HostServices hostServices;

    private int currentPage = 1;
    private final int limit = 50;

    public NewsAggregatorClientController(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public void start(){
        newsSplitPane.setDividerPositions(1);
        articleViewer.setVisible(false);
        reloadButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    reloadWebsite();
                }
            }
        );

        closeButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    closeWebsite();
                }
            }
        );
        newsContainer.getChildren().clear();
        reloadNews.setOnAction(new EventHandler<ActionEvent>() {
               @Override
               public void handle(ActionEvent event) {
                   reloadNews();
               }
            }
        );
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

    void loadWebsite(String url) {
        /**
         * Hàm này sẽ load bài báo hiển thị trên màn hình chính lên WebView
         * Được gọi khi người dùng click vào một tin tức
         * @param event: Sự kiện click chuột vào một tin tức
         * @param url: Đường dẫn website cần load
         */
        System.out.println("Loading website: " + url);
        newsSplitPane.setDividerPositions(0.5);
        articleWebView.getEngine().load(url);
        addressBar.setText(url);
        articleViewer.setVisible(true);
    }

    private void reloadWebsite() {
        /**
         * Hàm này sẽ load lại trang web đang hiển thị trên WebView
         * Được gọi khi người dùng click vào nút "tải lại" trên WebView
         * @param event: Sự kiện click chuột vào nút "tải lại"
         */
        articleWebView.getEngine().reload();
    }

    protected void closeWebsite(){
        /**
         * Hàm này sẽ đóng trang web đang hiển thị trên WebView
         * Được gọi khi người dùng click vào nút "đóng" trên WebView
         */
        articleWebView.getEngine().load(null);
        addressBar.setText("");
        newsSplitPane.setDividerPositions(1);
        articleViewer.setVisible(false);
    }

    protected void showAllNewsCategories() {
        /**
         * Hàm này sẽ hiển thị tất cả các tin tức theo từng danh mục,
         * được gọi khi ứng dụng được khởi chạy
         * Dữ liệu tin tức sẽ được lấy từ database thông qua trung gian
         */
//        newsContainer.getChildren().clear();
        NewsCategoryGroupTitledPane latestNews = new NewsCategoryGroupTitledPane("Latest news");
        NewsCategoryGroupTitledPane bitcoinNews = new NewsCategoryGroupTitledPane("Bitcoin news");
        NewsCategoryGroupTitledPane ethereumNews = new NewsCategoryGroupTitledPane("Ethereum news");
        NewsCategoryGroupTitledPane redditNews = new NewsCategoryGroupTitledPane("Post from Reddit");
        newsContainer.add(latestNews, 0, 0, 2, 1);
        newsContainer.add(bitcoinNews, 0, 1, 1, 1);
        newsContainer.add(ethereumNews, 1, 1, 1, 1);
        newsContainer.add(redditNews, 2, 1, 1, 3);
        NewsCategoryGroupTitledPane allNews = new NewsCategoryGroupTitledPane("All news");
        newsContainer.add(allNews, 0, 2, 2, 1);
        NewsJSONLoader articleDataLoader = getNewsJSONLoader();
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

    private static @NotNull NewsJSONLoader getNewsJSONLoader() {
        NewsJSONLoader articleDataLoader = new NewsJSONLoader();
        articleDataLoader.setCacheFileName("news.json");
        Thread jsonThread = new Thread(() -> {
            try {
                articleDataLoader.loadJSON();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        jsonThread.start();
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
            new Thread(articleDataLoader::loadJSON).start();
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
            NewsRetriever articleRetriver = new NewsRetriever();
            articleRetriver.setLimit(50);
            articleRetriver.setPageNumber(1);
            try {
                articleRetriver.sendRequest("articles", true, "news.json");
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