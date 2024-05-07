package org.newsaggregator.newsaggregatorclient;

import org.newsaggregator.newsaggregatorclient.downloaders.NewsRetriever;

public class NewsRequestTest {
    public static void main(String[] args) {
        NewsRetriever newsRequest = new NewsRetriever();
        newsRequest.setLimit(50);
        newsRequest.setPageNumber(1);
        try {
            newsRequest.downloadCache(true, "news.json");
        } catch (Exception e) {
            System.out.println("Error sending request: " + e.getMessage());
        }
    }
}
