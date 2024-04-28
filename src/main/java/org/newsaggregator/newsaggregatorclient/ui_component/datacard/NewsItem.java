package org.newsaggregator.newsaggregatorclient.ui_component.datacard;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;


import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.newsaggregator.newsaggregatorclient.downloaders.FileDownloader;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;

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

    public void loadText(){
        articleHyperlinkTitleObject.setText(newsItemData.title);
        description.setText(newsItemData.description);
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
        publishedAt.setText(formattedDate);
        author.setText(newsItemData.author.toString());
        publisher.setText(newsItemData.publisher);
    }

    public void loadImage(){
        String noImageAvailablePath = "file:///src/main/resources/org/newsaggregator/newsaggregatorclient/assets/images/no-image-available.png";
        Image thumbnail = new Image(noImageAvailablePath, true);
        try {
            System.out.println("Processing image: " + newsItemData.urlToImage);
            File tmpOutputFile = new File("src/main/resources/tmp/img");
            if (newsItemData.urlToImage.endsWith(".webp")) {
                System.out.println("Processing webp image: " + newsItemData.urlToImage);
                try {
                    FileDownloader.fileDownloader(newsItemData.urlToImage, tmpOutputFile.getAbsolutePath());
                    BufferedImage bufferedImage = ImageIO.read(new File("%s/tmp.webp".formatted(tmpOutputFile.getAbsolutePath())));
                    thumbnail = SwingFXUtils.toFXImage(bufferedImage, null);
                } catch (Exception ex) {
                    System.out.println("Error processing webp image: " + ex.getMessage());
                    thumbnail = new Image(noImageAvailablePath, true);
                }
            }
            else {
                thumbnail = new Image(newsItemData.urlToImage, true);
            }
            if (newsItemData.urlToImage.isBlank() || newsItemData.urlToImage.isEmpty()){
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
            Image publisherImage = new Image(newsItemData.publisherLogoURL, true);
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
