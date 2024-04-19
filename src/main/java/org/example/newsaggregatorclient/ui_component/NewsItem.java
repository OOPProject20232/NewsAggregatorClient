package org.example.newsaggregatorclient.ui_component;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import org.example.newsaggregatorclient.downloaders.FileDownloader;
import org.example.newsaggregatorclient.mediator_objects.NewsItemData;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
//import org.sejda.imageio.*;

import javax.imageio.ImageIO;

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
        this.getStyleClass().add("news-item");
        this.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        Image thumbnail = new Image("file:src/main/resources/assets/images/no-image-available.png");
        try {
            System.out.println("Processing image: " + newsItemData.urlToImage);
            File tmpOutputFile = new File("src/main/resources/tmp/img/tmp.jpg");
            if (newsItemData.urlToImage.endsWith(".webp")) {
                System.out.println("Processing webp image" + newsItemData.urlToImage);
                try {
                    FileDownloader.fileDownloader(newsItemData.urlToImage, tmpOutputFile.getAbsolutePath());
                    BufferedImage bufferedImage = ImageIO.read(new File("%s/tmp.webp".formatted(tmpOutputFile.getAbsolutePath())));
                    thumbnail = SwingFXUtils.toFXImage(bufferedImage, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else {
                thumbnail = new Image(newsItemData.urlToImage);
            }
        }
        catch (Exception e) {
            thumbnail = new Image("file:src/main/resources/assets/images/no-image-available.png");
        }
        finally {
            System.out.println("Thumbnail: " + thumbnail.getUrl());
            int thumbnailHeight = 90;
            int thumbnailWidth = 160;
            Rectangle clip = new Rectangle(
                    thumbnailWidth,
                    thumbnailHeight
            );
            clip.setArcWidth(10);
            clip.setArcHeight(10);
            ImagePattern pattern = new ImagePattern(thumbnail);
            clip.setFill(pattern);

            this.getChildren().add(clip);
            VBox newsInfo = new VBox();
            hyperlinkTitleObject.setText(newsItemData.title);
            Label description = new Label(newsItemData.description);
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = "";
            try{
                Date publishedDate = inputFormat.parse(newsItemData.publishedAt);
                formattedDate = outputFormat.format(publishedDate);
            }
            catch (Exception e){
                System.out.println("Error parsing date: " + e.getMessage());
            }
            Label publishedAt = new Label(formattedDate);
            Label author = new Label(newsItemData.author.toString());
            Label publisher = new Label(newsItemData.publisher);
            newsInfo.getChildren().addAll(hyperlinkTitleObject, description, publishedAt, author, publisher);
            this.getChildren().add(newsInfo);
        }
    }

    public Hyperlink getArticleHyperlinkObject(){
        return this.hyperlinkTitleObject;
    }
    public void loadImages(){

    }
}
