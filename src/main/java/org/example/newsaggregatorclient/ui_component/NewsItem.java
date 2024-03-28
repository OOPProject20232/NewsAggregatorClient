package org.example.newsaggregatorclient.ui_component;

import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.newsaggregatorclient.mediator_objects.NewsItemData;

public class NewsItem extends HBox {
    /**
     * Đây là thành phần nhỏ nhất của phần hiện tin, bao gồm các thông tin cơ bản của một tin tức
     * Bao gồm
     * - Ảnh thumbnail của tin tức
     * - Tiêu đề tin tức
     * - Tóm tắt tin tức
     * - Ngày đăng tin tức
     * - Tác giả tin tức
     * - Nút xem tin tức (nút "chi tiết" hoặc link ẩn trên tiêu đề tin tức)
     * @param newsItemData: Dữ liệu tin tức cần hiển thị, @see NewsItemData, dữ liệu này được truyền vào từ controller
     */
    private NewsItemData newsItemData;

    Hyperlink hyperlinkTitleObject = new Hyperlink();

    public NewsItem(NewsItemData newsItemData) {
        this.newsItemData = newsItemData;

        this.setStyle(
            """
            -fx-padding: 10;
            -fx-border-color: black;
            -fx-border-width: 1;
            -fx-border-radius: 5;
            -fx-background-color: #f0f0f0;
            """
        );
        try {
            Image thumbnail = new Image(newsItemData.urlToImage);
            javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(thumbnail);
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            imageView.getStyleClass().add("news-thumbnail");
            this.getChildren().add(imageView);

            VBox newsInfo = new VBox();
            Hyperlink title = new Hyperlink(newsItemData.title);
            Label description = new Label(newsItemData.description);
            newsInfo.getChildren().addAll(title, description);
            this.getChildren().add(newsInfo);

        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public Hyperlink getArticleHyperlinkObject(){
        return this.hyperlinkTitleObject;
    }
}
