package org.newsaggregator.newsaggregatorclient.ui_component.datacard;

import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class NewsItemFrame extends GenericHorizontalCard {
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

    Hyperlink hyperlinkTitleObject = new Hyperlink();
    ImageView imageView = new ImageView();
    Label description;
    Label publishedAt;
    Label author;
    Label publisher;



    public NewsItemFrame() {
        imageView.setFitHeight(90);
        imageView.setFitWidth(160);
        this.getChildren().add(imageView);
        VBox newsInfo = new VBox();
        description = new Label();
        description.setWrapText(true);
        publishedAt = new Label();
        author = new Label();
        publisher = new Label();
        newsInfo.getChildren().addAll(hyperlinkTitleObject, description, publishedAt, author, publisher);
        this.getChildren().add(newsInfo);
    }
}
