package org.example.newsaggregatorclient.mediator_objects;

public class NewsItemData {
    /**
     * Dữ liệu chứa tin tức tổng quát của một bài báo cần hiển thị
     * Bao gồm tên bài, hình ảnh thumbnail, tác giả, ngày đăng, mô tả, nội dung, link đến bài viết...
     */
    public String category;
    public String title;
    public String author;
    public String description;
    public String url;
    public String urlToImage;
    public String publishedAt;
    public String content;

    public NewsItemData() {

    }

    public NewsItemData(String category, String title, String author, String description, String url, String urlToImage, String publishedAt, String content) {
        this.category = category;
        this.title = title;
        this.author = author;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.content = content;
    }
}
