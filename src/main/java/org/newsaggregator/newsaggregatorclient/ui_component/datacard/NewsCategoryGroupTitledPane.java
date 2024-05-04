package org.newsaggregator.newsaggregatorclient.ui_component.datacard;

import com.almasb.fxgl.core.collection.Array;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

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

    private final VBox newsGroupLayout = new VBox();

    public NewsCategoryGroupTitledPane(String category) {
        this.setCollapsible(false);
        this.setText(category);
        this.setContent(this.newsGroupLayout);
        this.newsGroupLayout.getStyleClass().add("category__layout");
        this.getStyleClass().add("category");
    }

    public VBox getContainer() {
        return this.newsGroupLayout;
    }
    public void addNewsItem(NewsItemFrame newsItem) {
        this.newsGroupLayout.getChildren().add(newsItem);
    }
    public Array<NewsItemFrame> getNewsItems() {
        Array<NewsItemFrame> newsItems = new Array<>();
        for (int i = 0; i < this.newsGroupLayout.getChildren().size(); i++) {
            newsItems.add((NewsItemFrame) this.newsGroupLayout.getChildren().get(i));
        }
        return newsItems;
    }
}
