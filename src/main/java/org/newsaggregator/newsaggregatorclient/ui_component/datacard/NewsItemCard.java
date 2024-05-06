package org.newsaggregator.newsaggregatorclient.ui_component.datacard;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.util.FileDownloader;
import org.newsaggregator.newsaggregatorclient.util.TimeFormatter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;

public class NewsItemCard extends HBox implements IGenericDataCard<NewsItemData>{
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
    private final String noImageAvailablePath = "file:src/main/resources/org/newsaggregator/newsaggregatorclient/assets/images/no-image-available.png";
    private NewsItemData newsItemData;

    public NewsItemCard() {
        this.setAlignment(Pos.CENTER_LEFT);
        articleHyperlinkTitleObject.setWrapText(true);
        articleHyperlinkTitleObject.getStyleClass().add("article-title");
        thumbnailImageView.setFitHeight(90);
        thumbnailImageView.setFitWidth(160);
        this.getChildren().add(thumbnailImageView);
        VBox newsInfo = new VBox();
        newsInfo.setSpacing(5);
        newsInfo.setAlignment(Pos.CENTER_LEFT);
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
        Label separator = new Label("⋅");
        separator.setStyle("-fx-font-weight: bold;");
        publisherFrame.getChildren().addAll(publishedAt, separator, author);
        newsInfo.getChildren().addAll(publisher, articleHyperlinkTitleObject, description, publisherFrame);
        this.getChildren().add(newsInfo);
    }

    public NewsItemCard(NewsItemData newsItemData) {
        this();
        this.newsItemData = newsItemData;
        this.setSpacing(10);
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

    @Override
    public void setCardStyle() {
        this.getStylesheets().add("org/newsaggregator/newsaggregatorclient/ui_component/datacard/datacard.css");
        this.getStyleClass().add("datacard");
        this.getStyleClass().add("horizontal");
    }

    @Override
    public synchronized void setText() {
        articleHyperlinkTitleObject.setText(newsItemData.getTitle());
        description.setText(newsItemData.getDescription());
        publishedAt.setText(TimeFormatter.processDateTime(newsItemData.getPublishedAt()));
        author.setText(newsItemData.getAuthor().toString());
        publisher.setText(newsItemData.getPublisher());
    }

    @Override
    public synchronized void setImage() {
        String noImageAvailablePath = "file:src/main/resources/org/newsaggregator/newsaggregatorclient/assets/images/no-image-available.png";
        Image thumbnail = new Image(noImageAvailablePath, true);
        try {
            System.out.println("\u001B[32m"+"Processing image: " + newsItemData.getUrlToImage()+"\u001B[0m");
            String fileName = URI.create(newsItemData.getUrlToImage()).toURL().getFile();
            String cache = fileName.replace("/", "_");
            if (cache.contains("?")) {
                cache = cache.substring(0, cache.indexOf("?"));
            }
            System.out.println("File name: " + fileName);
            if (fileName.endsWith(".webp")) {
                String tmpCachePath = "src/main/resources/tmp/img" + cache;
                if (!new File(tmpCachePath).exists()) {
                    new File(tmpCachePath).delete();
                    FileDownloader.fileDownloader(newsItemData.getUrlToImage(), cache);
                }
                File tmpOutputFile = new File(tmpCachePath);
                System.out.println("\u001B[32m"+"Processing webp image: " + tmpCachePath+"\u001B[0m");
                try {
                    BufferedImage bufferedImage = ImageIO.read(tmpOutputFile);
                    thumbnail = SwingFXUtils.toFXImage(bufferedImage, null);
                } catch (Exception ex) {
                    System.out.println("\u001B[31m"+"Error processing webp image: " + ex.getMessage()+"\u001B[0m");
                    thumbnail = new Image(noImageAvailablePath, true);
                }
            } else {
                if (!new File("src/main/resources/tmp/img" + cache).exists()) {
                    FileDownloader.fileDownloader(newsItemData.getUrlToImage(), cache);
                }
                thumbnail = new Image("file:src/main/resources/tmp/img" + cache, true);
            }
            if (newsItemData.getUrlToImage().isBlank() || newsItemData.getUrlToImage().isEmpty()){
                thumbnail = new Image(noImageAvailablePath, true);

            }
        }
        catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
            thumbnail = new Image(noImageAvailablePath, true);
        }
        finally {
            int thumbnailHeight = 90;
            int thumbnailWidth = 160;
            Rectangle clip = new Rectangle(thumbnailWidth, thumbnailHeight);
            clip.setArcHeight(10);
            clip.setArcWidth(10);
            thumbnailImageView.setImage(thumbnail);
            thumbnailImageView.setClip(clip);
        }

        // Load publisher logo
        try{
            Image publisherImage = new Image(newsItemData.getPublisherLogoURL(), true);
            ImageView publisherLogo = new ImageView(publisherImage);
            publisherLogo.setFitHeight(16);
            publisherLogo.setFitWidth(16);
            publisher.setGraphic(publisherLogo);
        }
        catch (Exception e) {
            System.out.println("Error loading publisher logo: " + e.getMessage());
        }
    }
}
