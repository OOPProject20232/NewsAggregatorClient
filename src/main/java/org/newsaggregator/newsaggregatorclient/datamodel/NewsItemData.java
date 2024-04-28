package org.newsaggregator.newsaggregatorclient.datamodel;

import java.util.List;

public class NewsItemData extends GenericData{
    /**
     * Dữ liệu chứa tin tức tổng quát của một bài báo cần hiển thị
     * Bao gồm tên bài, hình ảnh thumbnail, tác giả, ngày đăng, mô tả, nội dung, link đến bài viết...
     * @author: Trần Quang Hưng
     *
     */
    public List<String> category;
    public String title;
    public Object author;
    public String description;
    public String articleDetailedContent;
    public String url;
    public String urlToImage;
    public String publishedAt;
    public String content;
    public String publisher;
    public String publisherLogoURL;

    public NewsItemData() {

    }

    public NewsItemData(List<String> category, String title, String author, String description, String url, String urlToImage, String publishedAt, String content, String publisher) {
        this.category = category;
        this.title = title;
        this.author = author;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.content = content;
        this.publisher = publisher;
    }

    public void printNewsData(){
        System.out.println("Category: " + category);
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Description: " + description);
        System.out.println("URL: " + url);
        System.out.println("URL to image: " + urlToImage);
        System.out.println("Published at: " + publishedAt);
        System.out.println("Content: " + content);
    }

    @Override
    public String toString() {
        return  "category=" + category +
                ",\ntitle='" + title + '\'' +
                ",\nauthor=" + author +
                ",\ndescription='" + description + '\'' +
                ",\narticleDetailedContent='" + articleDetailedContent + '\'' +
                ",\nurl='" + url + '\'' +
                ",\nurlToImage='" + urlToImage + '\'' +
                ",\npublishedAt='" + publishedAt + '\'' +
                ",\ncontent='" + content + '\'' +
                ",\npublisher='" + publisher + '\'';
    }
}
