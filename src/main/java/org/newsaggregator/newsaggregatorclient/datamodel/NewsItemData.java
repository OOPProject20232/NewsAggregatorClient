package org.newsaggregator.newsaggregatorclient.datamodel;

import java.util.List;

public class NewsItemData extends GenericData{
    /**
     * Dữ liệu chứa tin tức tổng quát của một bài báo cần hiển thị
     * Bao gồm tên bài, hình ảnh thumbnail, tác giả, ngày đăng, mô tả, nội dung, link đến bài viết...
     * @author: Trần Quang Hưng
     *
     */
    private List<String> category;
    private String title;
    private String author;
    private String description;
    private String articleDetailedContent;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private String content;
    private String publisher;
    private String publisherLogoURL;

    public NewsItemData() {

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
                ",\npublisher='" + publisher + '\'' +
                ",\npublisherLogoURL='" + publisherLogoURL + '\'';
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisherLogoURL() {
        return publisherLogoURL;
    }

    public void setPublisherLogoURL(String publisherLogoURL) {
        this.publisherLogoURL = publisherLogoURL;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArticleDetailedContent() {
        return articleDetailedContent;
    }

    public void setArticleDetailedContent(String articleDetailedContent) {
        this.articleDetailedContent = articleDetailedContent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
