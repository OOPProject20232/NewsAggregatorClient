package org.newsaggregator.newsaggregatorclient;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import org.newsaggregator.newsaggregatorclient.downloaders.NewsRetriever;
import org.newsaggregator.newsaggregatorclient.jsonparsing.NewsJSONLoader;
import org.newsaggregator.newsaggregatorclient.pojos.NewsItemData;
import org.newsaggregator.newsaggregatorclient.ui_component.ArticleItemsLoader;
import org.newsaggregator.newsaggregatorclient.ui_component.datacard.NewsCategoryGroupTitledPane;

import java.util.List;

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

    private HostServices hostServices;

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
        newsContainer.getChildren().clear();
        NewsJSONLoader dataLoader = new NewsJSONLoader();
        dataLoader.loadJSON();
        String newsString = dataLoader.getJSONString();
        if (newsString == null) {
            NewsRetriever newsRetriever = new NewsRetriever();
            try {
                newsRetriever.sendRequest();
                dataLoader.loadJSON();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            dataLoader.loadJSON();
        }
        int maxItemInARow = 2;
        List<NewsItemData> data = getNewsItemData(dataLoader, 700, 0);

        // This segment is for performance testing only, not for actual use
        // for (int i = 0; i < 6; i++) {
        //      NewsCategoryGroupTitledPane latestNews = new NewsCategoryGroupTitledPane("Latest news");
//            ArticleItemsLoader articleItemsLoader = new ArticleItemsLoader(10,i * 10, newsContainer, hostServices, latestNews);
//            latestNews = articleItemsLoader.articleItemsLoader(data);
//            newsContainer.add(latestNews, i % maxItemInARow, i / maxItemInARow);
//        }
        NewsCategoryGroupTitledPane latestNews = new NewsCategoryGroupTitledPane("Latest news");
        ArticleItemsLoader articleItemsLoader = new ArticleItemsLoader(100,0, newsContainer, hostServices, latestNews);
        latestNews = articleItemsLoader.loadArticleItems(data);
        newsContainer.add(latestNews, 0, 0, 2, 1);
    }

    private static List<NewsItemData> getNewsItemData(NewsJSONLoader loader, int limit, int begin) {
        return loader.getNewsItemDataList(limit, begin);
    }

    private void reloadNews() {
        /**
         * Hàm này sẽ load lại tất cả các tin tức hiển thị trên màn hình chính
         * Được gọi khi người dùng click vào nút "tải lại" trên màn hình chính
         * @param event: Sự kiện click chuột vào nút "tải lại"
         */
        newsContainer.getChildren().clear();
        showAllNewsCategories();
    }

//    public GridPane getNewsContainer() {
//        return newsContainer;
//    }

}