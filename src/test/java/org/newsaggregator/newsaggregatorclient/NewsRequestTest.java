package org.newsaggregator.newsaggregatorclient;

import org.newsaggregator.newsaggregatorclient.downloaders.NewsRetriever;

public class NewsRequestTest {
    public static void main(String[] args) {
        NewsRetriever newsRequest = new NewsRetriever();
        try {
            newsRequest.sendRequest();
        } catch (Exception e) {
            System.out.println("Error sending request: " + e.getMessage());
        }
    }
}
