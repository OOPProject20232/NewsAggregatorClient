package org.newsaggregator.newsaggregatorclient.ui_components.datacard;

import javafx.application.Platform;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.newsaggregator.newsaggregatorclient.datamodel.RedditPostData;


import static java.lang.Long.MAX_VALUE;
import static javafx.scene.CacheHint.SPEED;

/**
 * Class RedditCard chứa các thành phần của một card hiển thị thông tin của một bài viết trên Reddit
 * kế thừa từ HorizontalDataCard
 * @see HorizontalDataCard
 * Sử dụng RedditPostData để lấy thông tin của bài viết
 * @see RedditPostData
 */
public class RedditCard extends HorizontalDataCard<RedditPostData> {
    private final ImageView thumbnail = new ImageView();
    private final Hyperlink title = new Hyperlink();
    private final Hyperlink author = new Hyperlink();
    private final Hyperlink subreddit = new Hyperlink();
    private final Label upvoteCount = new Label();
    private final Label downvoteCount = new Label();
    private final Label content = new Label();
    private final RedditPostData redditPostData;
    private final Hyperlink imageViewLink = new Hyperlink();

    public RedditCard(RedditPostData redditPostData){
        this.redditPostData = redditPostData;
        imageViewLink.setGraphic(thumbnail);
        this.setCardStyle();
        HBox voteBox = new HBox();
        voteBox.getChildren().addAll(upvoteCount, downvoteCount);
        VBox contentBox = new VBox();
        contentBox.getChildren().addAll(subreddit, title, imageViewLink, content, author, voteBox);
        VBox.setVgrow(contentBox, Priority.ALWAYS);
        getChildren().add(contentBox);
    }

    @Override
    public void setCardStyle() {
        this.getStyleClass().add("datacard");
        title.setWrapText(true);
        title.setPrefWidth(500);
        content.setPrefWidth(500);
        content.setWrapText(true);
        this.setStyle("-fx-border-width: 0 0 1 0; -fx-border-color: -fx-outline-color-variant; -fx-padding: 0 0 12px 0");
        this.setMaxHeight(600);
        this.setPrefHeight(MAX_VALUE);
    }

    @Override
    public void setText() {
        this.subreddit.setText(redditPostData.getSubreddit());
        this.title.setText(redditPostData.getTitle());
        this.author.setText(redditPostData.getAuthor());
        this.upvoteCount.setText("▲ " + redditPostData.getUpvotes());
        this.downvoteCount.setText("▼ " + redditPostData.getDownvotes());
        this.content.setText(redditPostData.getPostContent());
    }

    @Override
    public void setImage() {
        String noThumbnail = "file:src/main/resources/assets/images/no-image-available.png";
        Platform.runLater(() -> {
            try{
                Image image = new Image(redditPostData.getMediaUrl(), true);
                thumbnail.setFitWidth(320);
                Rectangle clip = new Rectangle(320, 180);
                thumbnail.setClip(clip);
                thumbnail.setPreserveRatio(true);
                thumbnail.setSmooth(true);
                thumbnail.setFitHeight(180);
                thumbnail.setImage(image);
                thumbnail.setCache(true);
                thumbnail.setCacheHint(SPEED);
            } catch (Exception e) {
                thumbnail.setImage(new Image(noThumbnail));
            }
        });
    }

    public Hyperlink getTitle() {
        return title;
    }

    public Hyperlink getAuthor() {
        return author;
    }

    public Hyperlink getSubreddit() {
        return subreddit;
    }

    public Label getUpvoteCount() {
        return upvoteCount;
    }

    public Label getDownvoteCount() {
        return downvoteCount;
    }

    public Label getContent() {
        return content;
    }

    public Hyperlink getImageViewLink() {
        return imageViewLink;
    }
}
