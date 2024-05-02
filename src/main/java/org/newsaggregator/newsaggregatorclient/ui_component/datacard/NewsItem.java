package org.newsaggregator.newsaggregatorclient.ui_component.datacard;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;


import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.newsaggregator.newsaggregatorclient.downloaders.FileDownloader;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.util.TimeFormatter;

import javax.imageio.ImageIO;
//import com.twelvemonkeys.imageio.plugins.webp.*;


public class NewsItem extends NewsItemFrame{
    /**
     * Nạp dữ liệu tin tức vào khung tin tức
     * thông qua việc truyền một cấu trúc dữ liệu tin tức NewsItemData vào khung tin tức
     * Phần text sẽ được tải trước lên UI, sau đó các hình ảnh thumbnail sẽ được tải lên
     * trên luồng riêng
     */
    private final NewsItemData newsItemData;

    public NewsItem(NewsItemData newsItemData) {
        this.newsItemData = newsItemData;
        this.setSpacing(10);
    }



    public synchronized void loadText(){
        articleHyperlinkTitleObject.setText(newsItemData.getTitle());
        description.setText(newsItemData.getDescription());
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = "";
//        try{
//            Date publishedDate = inputFormat.parse(newsItemData.getPublishedAt());
//            formattedDate = outputFormat.format(publishedDate);
//        }
//        catch (Exception e){
//            System.out.println("Error parsing date: " + e.getMessage());
//        }
        publishedAt.setText(TimeFormatter.processDateTime(newsItemData.getPublishedAt()));
        author.setText(newsItemData.getAuthor().toString());
        publisher.setText(newsItemData.getPublisher());
    }

    public synchronized void loadImage(){
        String noImageAvailablePath = "file:src/main/resources/org/newsaggregator/newsaggregatorclient/assets/images/no-image-available.png";
        Image thumbnail = new Image(noImageAvailablePath, true);
        try {
            System.out.println("\u001B[32m"+"Processing image: " + newsItemData.getUrlToImage()+"\u001B[0m");
            String fileName = URI.create(newsItemData.getUrlToImage()).toURL().getFile();
            System.out.println("File name: " + fileName);
            if (fileName.endsWith(".webp")) {
                String tmpCachePath = "src/main/resources/tmp/img" + fileName.replace("/", "_");
                File tmpOutputFile = new File(tmpCachePath);
                System.out.println("\u001B[32m"+"Processing webp image: " + tmpCachePath+"\u001B[0m");
                try {
                    FileDownloader.fileDownloader(newsItemData.getUrlToImage(), fileName.replace("/", "_"));
                    BufferedImage bufferedImage = ImageIO.read(tmpOutputFile);
                    thumbnail = SwingFXUtils.toFXImage(bufferedImage, null);
                } catch (Exception ex) {
                    System.out.println("\u001B[31m"+"Error processing webp image: " + ex.getMessage()+"\u001B[0m");
                    thumbnail = new Image(noImageAvailablePath, true);
                }
            }
            else {
                thumbnail = new Image(newsItemData.getUrlToImage(), true);
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

    public Hyperlink getArticleHyperlinkObject(){
        return this.articleHyperlinkTitleObject;
    }
}
