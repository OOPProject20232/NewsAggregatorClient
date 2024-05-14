package org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes;

import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.ui_components.datacard.HorizontalDataCard;
import org.newsaggregator.newsaggregatorclient.ui_components.datacard.NewsItemCard;

import java.util.ArrayList;

public class NewsCategoryGroupTitledPane extends CategoryTitledPane<NewsItemCard, NewsItemData> {
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

    public NewsCategoryGroupTitledPane(String category) {
        this.setCollapsible(false);
        this.setText(category);
    }

    public void addItem(NewsItemCard data) {
        this.getContainer().getChildren().add(data);
    }

    public ArrayList<NewsItemCard> getItems() {
        ArrayList<NewsItemCard> newsItems = new ArrayList<>();
        for (int i = 0; i < this.getContainer().getChildren().size(); i++) {
            newsItems.add((NewsItemCard) this.getContainer().getChildren().get(i));
        }
        return newsItems;
    }
}
