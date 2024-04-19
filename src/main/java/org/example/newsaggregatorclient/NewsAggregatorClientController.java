package org.example.newsaggregatorclient;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Window;
import org.example.newsaggregatorclient.downloaders.PeriodicNewsRetriever;
import org.example.newsaggregatorclient.mediator_objects.DataLoaderFromJSON;
import org.example.newsaggregatorclient.mediator_objects.NewsItemData;
import org.example.newsaggregatorclient.ui_component.NewsCategoryGroupTitledPane;
import org.example.newsaggregatorclient.ui_component.NewsItem;

import java.util.List;

public class NewsAggregatorClientController {
    /**
     * Controller chính của ứng dụng, chứa các hàm xử lý sự kiện và dữ liệu
     * Được gọi khi ứng dụng được khởi chạy
     */

    @FXML
    private WebView articleWebView;
    @FXML
    private FlowPane newsContainer;
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

    public FlowPane getNewsContainer() {
        return newsContainer;
    }

    protected void showAllNewsCategories() {
        /**
         * Hàm này sẽ hiển thị tất cả các tin tức theo từng danh mục,
         * được gọi khi ứng dụng được khởi chạy
         * Dữ liệu tin tức sẽ được lấy từ database thông qua trung gian
         */
        newsContainer.getChildren().clear();
        PeriodicNewsRetriever periodicNewsRetriever = new PeriodicNewsRetriever();
        periodicNewsRetriever.startRetrieving();
        Thread thread = new Thread(() -> {
            DataLoaderFromJSON dataLoader = new DataLoaderFromJSON();
            List<NewsItemData> data = dataLoader.loadJSON();
            NewsCategoryGroupTitledPane newsCategoryGroupTitledPane = new NewsCategoryGroupTitledPane("Latest news");
            final int maxNewsCount = 20;
            for (int countItem = 0; countItem < maxNewsCount; countItem++) {
                System.out.println("Adding news item " + (countItem));
                NewsItemData item = data.get(countItem);
                System.out.println(item.title);
                NewsItem newsItem = new NewsItem(item);
                newsItem.getArticleHyperlinkObject().setOnAction(
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                hostServices.showDocument(item.url);
                            }
                        }
                );
                newsCategoryGroupTitledPane.addNewsItem(newsItem);
            }
            getNewsContainer().getChildren().add(newsCategoryGroupTitledPane);
        });
        thread.start();
    }

}