package org.newsaggregator.newsaggregatorclient;

import org.newsaggregator.newsaggregatorclient.downloaders.SearchResultRetriever;

public class SearchTest {
    public static void main(String[] args) {
        SearchResultRetriever retriever = new SearchResultRetriever("bitcoin", true);
        retriever.downloadCache(false, "search.json");
    }
}
