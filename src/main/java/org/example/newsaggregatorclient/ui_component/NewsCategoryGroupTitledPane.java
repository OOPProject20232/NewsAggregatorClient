package org.example.newsaggregatorclient.ui_component;

import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import org.example.newsaggregatorclient.mediator_objects.NewsItemData;

public class NewsCategoryGroupTitledPane extends TitledPane {
    /**
     * TitledPane chứa các tin tức theo từng danh mục, được tạo bới controller khi nhập dữ liệu từ database
     * Bao gồm
     * - Ảnh thumbnail của tin tức
     * - Tiêu đề tin tức
     * - Tóm tắt tin tức
     * - Ngày đăng tin tức
     * - Tác giả tin tức
     * - Nút xem tin tức (nút "chi tiết" hoặc link ẩn trên tiêu đề tin tức)
     */
    private NewsItemData newsItemData;
    @FXML
    private WebView articleWebView;
    @FXML
    private final VBox newsGroupLayout = new VBox();

    public NewsCategoryGroupTitledPane(String category) {
        this.setCollapsible(false);
        this.setText(category);
        this.setContent(this.newsGroupLayout);
        this.getStyleClass().add("category");
    }

    public VBox getContainer() {
        return this.newsGroupLayout;
    }
}
