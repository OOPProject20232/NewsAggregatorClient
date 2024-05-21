package org.newsaggregator.newsaggregatorclient.database;

import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;

import java.util.List;

public class test {
    public static void main(String[] args) {
        SQLiteJDBC db = new SQLiteJDBC();
        NewsItemData item1 = new NewsItemData();
        item1.setGuid("guid1");
        item1.setTitle("title1");
        item1.setAuthor("author1");
        item1.setDescription("description1");
        item1.setArticleDetailedContent("content1");
        item1.setUrl("url1");
        item1.setUrlToImage("urlToImage1");
        item1.setPublishedAt("publishedAt1");
        item1.setPublisher("publisher1");
        item1.setPublisherLogoURL("publisherLogoUrl1");
        item1.setCategory(List.of("category1", "category2"));
//        db.insert(item1);
        NewsItemData item2 = new NewsItemData();
        item2.setGuid("guid2");
        item2.setTitle("title2");
        item2.setAuthor("author2");
        item2.setDescription("description2");
        item2.setArticleDetailedContent("content2");
        item2.setUrl("url2");
        item2.setUrlToImage("urlToImage2");
        item2.setPublishedAt("publishedAt2");
        item2.setPublisher("publisher2");
        item2.setPublisherLogoURL("publisherLogoUrl2");
        item2.setCategory(List.of("category3", "category4"));
//        db.insert(item2);
        db.delete("guid2");
        List<NewsItemData> list = db.select();
        for (NewsItemData item : list) {
            item.printNewsData();
        }
    }
}
