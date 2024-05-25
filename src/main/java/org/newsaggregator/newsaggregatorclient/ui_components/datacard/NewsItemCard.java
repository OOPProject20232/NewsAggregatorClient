package org.newsaggregator.newsaggregatorclient.ui_components.datacard;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.ui_components.buttons.BookmarkToggleButton;
import org.newsaggregator.newsaggregatorclient.ui_components.buttons.CategoryClickable;
import org.newsaggregator.newsaggregatorclient.util.TimeFormatter;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;

public class NewsItemCard extends HorizontalDataCard<NewsItemData> {
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

    private final Hyperlink articleHyperlinkTitle = new Hyperlink();
    private final ImageView thumbnailImageView = new ImageView();
    private final Hyperlink thumbnailHyperlink = new Hyperlink();
    private final Label description;
    private final Hyperlink readMore;
    private final Label publishedAt;
    private Label author;
    private Label publisher;
    private final FlowPane categoryFrame = new FlowPane();
    private final Button copyButton = new Button("");
    private final BookmarkToggleButton bookmarkButton = new BookmarkToggleButton();
    private final Button externalLink = new Button();
    private static final String noImageAvailablePath
            = "file:src/main/resources/org/newsaggregator/newsaggregatorclient/assets/images/no-image-available.png";
    private NewsItemData newsItemData;
    private boolean containingSummary = true;
    private boolean containingCategories = true;
    private final int IMAGE_HEIGHT = 108;
    private final int IMAGE_WIDTH = 192;

    public NewsItemCard() {
        this.setAlignment(Pos.TOP_LEFT);
//        this.setStyle("-fx-border-width: 0 0 1 0; -fx-border-color: -fx-outline-color-variant; -fx-padding: 0 0 12px 0");
        articleHyperlinkTitle.getStyleClass().add("article-title");
        thumbnailImageView.setFitWidth(IMAGE_WIDTH);
        thumbnailImageView.setFitHeight(IMAGE_HEIGHT);
        thumbnailHyperlink.setGraphic(thumbnailImageView);
        this.getChildren().add(thumbnailHyperlink);
        VBox newsInfo = new VBox();
        newsInfo.setSpacing(5);
        newsInfo.setAlignment(Pos.CENTER_LEFT);
        description = new Label();
        description.setWrapText(true);
        readMore = new Hyperlink("Read more");
        description.setGraphic(readMore);
        description.setContentDisplay(ContentDisplay.RIGHT);
        description.setTextAlignment(TextAlignment.LEFT);
        description.setAlignment(Pos.BOTTOM_RIGHT);
        description.setMaxHeight(100);
        publishedAt = new Label();
        author = new Label();
        HBox publisherFrame = new HBox();
        publisherFrame.setSpacing(5);
        publisher = new Label();
        ImageView publisherImageView = new ImageView();
        publisherImageView.setFitHeight(16);
        publisherImageView.setFitWidth(16);
        publisher.setGraphic(publisherImageView);
        Label separator = new Label("⋅");
        separator.setStyle("-fx-font-weight: bold;");
        publisherFrame.getChildren().addAll(publishedAt, separator, author);
        newsInfo.getChildren().addAll(publisher, articleHyperlinkTitle, description, publisherFrame);
        this.getChildren().add(newsInfo);
        categoryFrame.setHgap(8);
        categoryFrame.setVgap(8);
        categoryFrame.setAlignment(Pos.CENTER_LEFT);
        newsInfo.getChildren().add(categoryFrame);
        HBox utilityFrame = new HBox();
        utilityFrame.setSpacing(8);
        ImageView copyIcon = new ImageView(new Image("file:src/main/resources/org/newsaggregator/newsaggregatorclient/assets/images/link.png"));
        copyIcon.setFitHeight(16);
        copyIcon.setFitWidth(16);
        copyButton.setGraphic(copyIcon);
        copyButton.getStyleClass().add("util-button");
        utilityFrame.getChildren().add(copyButton);
        utilityFrame.getChildren().add(bookmarkButton);
        externalLink.getStyleClass().add("util-button");
        ImageView externalIcon = new ImageView("file:src/main/resources/org/newsaggregator/newsaggregatorclient/assets/images/external-link.png");
        externalIcon.setFitWidth(16);
        externalIcon.setFitHeight(16);
        externalLink.setGraphic(externalIcon);
        utilityFrame.getChildren().add(externalLink);
        newsInfo.getChildren().add(utilityFrame);
    }

    public NewsItemCard(NewsItemData newsItemData) {
        this();
        this.newsItemData = newsItemData;
        this.setSpacing(10);
        Tooltip copyTooltip = new Tooltip("Copy link");
        copyTooltip.setShowDelay(javafx.util.Duration.millis(10));
        copyButton.setTooltip(copyTooltip);
        copyButton.setOnMouseClicked(event -> {
            System.out.println("Copy button clicked");
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(newsItemData.getUrl());
            clipboard.setContent(clipboardContent);
            copyTooltip.setText("Link copied!");
            copyButton.setTooltip(copyTooltip);
//            copyTooltip.show(copyButton, event.getScreenX() , event.getScreenY());
            copyButton.setText("Copied!");
            copyButton.setGraphic(null);
        });
        copyButton.setOnMouseExited(event -> {
//            copyTooltip.hide();
            copyButton.setText("");
            ImageView copyIcon = new ImageView(new Image("file:src/main/resources/org/newsaggregator/newsaggregatorclient/assets/images/link.png"));
            copyIcon.setFitHeight(16);
            copyIcon.setFitWidth(16);
            copyButton.setGraphic(copyIcon);
            copyButton.setTooltip(new Tooltip("Copy link"));
        });
        Tooltip bookmarkTooltip = new Tooltip("Save this to Bookmark");
        bookmarkTooltip.setShowDelay(Duration.millis(10));
        bookmarkButton.setTooltip(bookmarkTooltip);
        Tooltip externalTooltip = new Tooltip("Open in external browser");
        externalTooltip.setShowDelay(Duration.millis(10));
        externalLink.setTooltip(externalTooltip);
    }

    @Override
    public void setCardStyle() {
        this.getStyleClass().add("datacard");
        this.getStyleClass().add("horizontal");
    }

    @Override
    public void setText() {
        articleHyperlinkTitle.maxWidthProperty().bind(this.widthProperty().subtract(160));
        articleHyperlinkTitle.setWrapText(true);
        articleHyperlinkTitle.setText(newsItemData.getTitle());
        if (containingSummary) {
            description.setText(newsItemData.getDescription());
        }
        try {
            publishedAt.setText(TimeFormatter.processDateTime(newsItemData.getPublishedAt()));
        }
        catch (Exception e) {
            System.out.println("Error processing date time: " + e.getMessage());
        }
        author.setText(newsItemData.getAuthor());
        publisher.setText(newsItemData.getPublisher());
        if (containingCategories) {
            for (String category : newsItemData.getCategory()) {
                CategoryClickable categoryLabel = new CategoryClickable(category);
                categoryFrame.getChildren().add(categoryLabel);
            }
        }
    }

    @Override
    public synchronized void setImage() {
        try {
            String fileName = URI.create(newsItemData.getUrlToImage()).toURL().getFile();

            Platform.runLater(()-> {
                Image thumbnail;
                if (fileName.endsWith(".webp")) {
                    System.out.println("\u001B[32m" + "Processing webp image: " + newsItemData.getUrlToImage() + "\u001B[0m");
                    try {
                        HttpURLConnection connection = (HttpURLConnection) URI.create(newsItemData.getUrlToImage()).toURL().openConnection();
                        connection.setRequestMethod("GET");
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        byte[] buffer = new byte[65534];
                        int length;
                        while ((length = in.read()) > 0) {
                            out.write(buffer, 0, length);
                        }
                        in.close();
                        byte[] bytes = out.toByteArray();
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                        ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream);
                        BufferedImage bufferedImage = ImageIO.read(imageInputStream);
                        try {
                            thumbnail = SwingFXUtils.toFXImage(bufferedImage, null);
                        } catch (Exception ex) {
                            thumbnail = new Image(noImageAvailablePath, true);
                            System.out.println("\u001B[31m" + "Error processing webp image: " + ex.getMessage() + "\u001B[0m");
                        }
                    } catch (Exception ex) {
                        thumbnail = new Image(noImageAvailablePath, true);
                        System.out.println("\u001B[31m" + "Error processing webp image: " + ex.getMessage() + "\u001B[0m");
                    }
                } else {
//                    thumbnail = new Image("file:src/main/resources/tmp/img" + cache, true);
                    thumbnail = new Image(newsItemData.getUrlToImage(), true);
                }
                if (newsItemData.getUrlToImage().isBlank() || newsItemData.getUrlToImage().isEmpty()) {
                    thumbnail = new Image(noImageAvailablePath, true);
                }
                Rectangle clip = new Rectangle(IMAGE_WIDTH, IMAGE_HEIGHT);
                clip.setArcHeight(24);
                clip.setArcWidth(24);
                thumbnailImageView.setImage(thumbnail);
                thumbnailImageView.setClip(clip);
            });
        }
        catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
        }
    }

    public synchronized void setPublisherLogo(){
        // Load publisher logo
        try{
            Platform.runLater(() -> {
                Image publisherImage = new Image(newsItemData.getPublisherLogoURL(), true);
                ImageView publisherLogo = new ImageView(publisherImage);
                publisherLogo.setCache(true);
                publisherLogo.setCacheHint(CacheHint.SPEED);
                publisherLogo.setFitHeight(16);
                publisherLogo.setFitWidth(16);
                publisher.setGraphic(publisherLogo);
            });
        }
        catch (Exception e) {
            System.out.println("Error loading publisher logo: " + e.getMessage());
        }
    }

    public List<Node> getCategories(){
        return categoryFrame.getChildren();
    }


    public void setContainingSummary(boolean containingSummary) {
        this.containingSummary = containingSummary;
    }

    public Hyperlink getArticleHyperlinkTitle() {
        return articleHyperlinkTitle;
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

    public void setContainingCategories(boolean containingCategories) {
        this.containingCategories = containingCategories;
    }

    public Hyperlink getReadMore() {
        return readMore;
    }


    public Hyperlink getThumbnailHyperlink() {
        return thumbnailHyperlink;
    }


    public Button getExternalLink() {
        return externalLink;
    }

    public BookmarkToggleButton getBookmarkButton() {
        return bookmarkButton;
    }
}
