package org.newsaggregator.newsaggregatorclient;

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
import org.newsaggregator.newsaggregatorclient.downloaders.PeriodicNewsRetriever;
import org.newsaggregator.newsaggregatorclient.ui_component.LoadNewsItems;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        LoadNewsItems loadNewsItems = new LoadNewsItems(30, newsContainer, hostServices);
        try (ExecutorService executorService = Executors.newFixedThreadPool(4)) {
            executorService.execute(loadNewsItems);
        }
    }
}