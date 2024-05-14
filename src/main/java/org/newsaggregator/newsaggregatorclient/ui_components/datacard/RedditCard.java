package org.newsaggregator.newsaggregatorclient.ui_components.datacard;

import javafx.application.Platform;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import org.newsaggregator.newsaggregatorclient.datamodel.RedditPostData;

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

    public RedditCard(RedditPostData redditPostData){
        this.redditPostData = redditPostData;
        this.setCardStyle();
        HBox voteBox = new HBox();
        voteBox.getChildren().addAll(upvoteCount, downvoteCount);
        getChildren().addAll(subreddit, thumbnail, title, content , author, voteBox);
    }

    @Override
    public void setCardStyle() {
        this.getStyleClass().add("datacard");
        title.setWrapText(true);
        title.setPrefWidth(500);
        content.setPrefWidth(500);
        content.setWrapText(true);
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
        try {
            Platform.runLater(() -> {
                thumbnail.setImage(new Image(redditPostData.getMediaUrl(), true));
            });
        } catch (Exception e) {
            e.printStackTrace();
            thumbnail.setImage(new Image(noThumbnail, true));
        }
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


}
