package org.newsaggregator.newsaggregatorclient.ui_components.datacard;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
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

    protected Hyperlink articleHyperlinkTitleObject = new Hyperlink();
    protected ImageView thumbnailImageView = new ImageView();
    protected Hyperlink thumbnailHyperlink = new Hyperlink();
    protected Label description;
    protected Hyperlink readMore;
    protected Label publishedAt;
    protected Label author;
    protected Label publisher;
    protected ImageView publisherImageView = new ImageView();
    protected FlowPane categoryFrame = new FlowPane();
    private static final String noImageAvailablePath = "file:src/main/resources/org/newsaggregator/newsaggregatorclient/assets/images/no-image-available.png";
    private NewsItemData newsItemData;
    private boolean containingSummary = true;
    private boolean containingCategories = true;
    private double parentWidth;

    public NewsItemCard() {
        this.setAlignment(Pos.CENTER_LEFT);
        articleHyperlinkTitleObject.getStyleClass().add("article-title");
        thumbnailImageView.setFitHeight(90);
        thumbnailImageView.setFitWidth(160);
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
        description.setAlignment(Pos.BOTTOM_RIGHT);
        description.setMaxHeight(100);
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
        categoryFrame.setHgap(8);
        categoryFrame.setVgap(8);
        categoryFrame.setAlignment(Pos.CENTER_LEFT);
        newsInfo.getChildren().add(categoryFrame);
    }

    public NewsItemCard(NewsItemData newsItemData) {
        this();
        this.newsItemData = newsItemData;
        this.setSpacing(10);
    }

    @Override
    public void setCardStyle() {
        this.getStylesheets().add("file:org/newsaggregator/newsaggregatorclient/ui_components/datacard/datacard.css");
        this.getStyleClass().add("datacard");
        this.getStyleClass().add("horizontal");
    }

    @Override
    public synchronized void setText() {
        articleHyperlinkTitleObject.maxWidthProperty().bind(this.widthProperty().subtract(160));
        articleHyperlinkTitleObject.setWrapText(true);
        articleHyperlinkTitleObject.setText(newsItemData.getTitle());
        if (containingSummary) {
            description.setText(newsItemData.getDescription());
        }
        publishedAt.setText(TimeFormatter.processDateTime(newsItemData.getPublishedAt()));
        author.setText(newsItemData.getAuthor().toString());
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
//        System.out.println("Set image to: " + newsItemData.getTitle());
//        final String noImageAvailablePath = "file:src/main/resources/org/newsaggregator/newsaggregatorclient/assets/images/no-image-available.png";
        try {
//            System.out.println("\u001B[32m"+"Processing image: " + newsItemData.getUrlToImage()+"\u001B[0m");
            String fileName = URI.create(newsItemData.getUrlToImage()).toURL().getFile();
            String tmpCache = fileName.replace("/", "_");
            if (tmpCache.contains("?")) {
                tmpCache = tmpCache.substring(0, tmpCache.lastIndexOf("?"));
            }
            final String cache = tmpCache;

//            System.out.println("File name: " + tmpCache);
            Platform.runLater(()-> {
                Image thumbnail;
                if (fileName.endsWith(".webp")) {
//                    ImageLoader.fileDownloader(newsItemData.getUrlToImage(), cache);
//                    String tmpCachePath = "src/main/resources/tmp/img/" + cache;
//                    File tmpOutputFile = new File(tmpCachePath);
                    System.out.println("\u001B[32m" + "Processing webp image: " + newsItemData.getUrlToImage() + "\u001B[0m");
                    try {
//                        BufferedImage bufferedImage = ImageIO.read(tmpOutputFile);
                        // get buffered image directly from input stream
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
                int thumbnailHeight = 90;
                int thumbnailWidth = 160;
                Rectangle clip = new Rectangle(thumbnailWidth, thumbnailHeight);
                clip.setArcHeight(10);
                clip.setArcWidth(10);
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


    public boolean isContainingSummary() {
        return containingSummary;
    }

    public void setContainingSummary(boolean containingSummary) {
        this.containingSummary = containingSummary;
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

    public void setContainingCategories(boolean containingCategories) {
        this.containingCategories = containingCategories;
    }

    public void setParentWidth(double parentWidth) {
        this.parentWidth = parentWidth;
    }

    public Hyperlink getReadMore() {
        return readMore;
    }


    public Hyperlink getThumbnailHyperlink() {
        return thumbnailHyperlink;
    }

}
