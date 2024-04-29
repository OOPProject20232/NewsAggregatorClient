package org.newsaggregator.newsaggregatorclient.ui_component.datacard;

import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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

    protected Hyperlink articleHyperlinkTitleObject = new Hyperlink();
    protected ImageView thumbnailImageView = new ImageView();
    protected Label description;
    protected Label publishedAt;
    protected Label author;
    protected Label publisher;
    protected ImageView publisherImageView = new ImageView();

    public NewsItemFrame() {
        thumbnailImageView.setFitHeight(90);
        thumbnailImageView.setFitWidth(160);
        this.getChildren().add(thumbnailImageView);
        VBox newsInfo = new VBox();
        description = new Label();
        description.setWrapText(true);
        publishedAt = new Label();
        author = new Label();
        HBox publisherFrame = new HBox();
        publisherFrame.setSpacing(5);
        publisher = new Label();
        publisherImageView.setFitHeight(16);
        publisherImageView.setFitWidth(16);
        publisher.setGraphic(publisherImageView);
        Label separator = new Label(" ⋅ ");
        publisherFrame.getChildren().addAll(author, separator, publisher);
        newsInfo.getChildren().addAll(articleHyperlinkTitleObject, description, publishedAt, publisherFrame);
        this.getChildren().add(newsInfo);
    }

    public Hyperlink getArticleHyperlinkTitleObject() {
        return articleHyperlinkTitleObject;
    }

    public void setArticleHyperlinkTitleObject(Hyperlink articleHyperlinkTitleObject) {
        this.articleHyperlinkTitleObject = articleHyperlinkTitleObject;
    }

    public ImageView getThumbnailImageView() {
        return thumbnailImageView;
    }

    public void setImageView(ImageView imageView) {
        this.thumbnailImageView = imageView;
    }

    public Label getDescription() {
        return description;
    }

    public void setDescription(Label description) {
        this.description = description;
    }

    public Label getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Label publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Label getAuthor() {
        return author;
    }

    public void setAuthor(Label author) {
        this.author = author;
    }

    public Label getPublisher() {
        return publisher;
    }

    public void setPublisher(Label publisher) {
        this.publisher = publisher;
    }
}
