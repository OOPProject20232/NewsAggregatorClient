package org.newsaggregator.newsaggregatorclient.ui_component;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class NewsItemFrame extends HBox {
    /**
     * Class này chứa bộ khung để hiển thị một tin tức
     * Bao gồm các thành phần
     * - Tên bài báo
     * - Mô tả bài báo
     * - Ngày đăng bài báo
     * - Tác giả bài báo
     * - Ảnh thumbnail của bài báo
     * - Nút xem chi tiết bài báo: trong app hoặc ở trình duyệt mặc định của máy client
     */
    public NewsItemFrame() {
        ImageView imageView = new ImageView();
        this.getChildren().add(imageView);
        imageView.setImage(null);
    }
}
