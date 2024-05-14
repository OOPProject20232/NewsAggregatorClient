package org.newsaggregator.newsaggregatorclient.jsonparsing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RedditPostJSONLoaderTest {
    @Test
    void loadJSON() {
        RedditPostJSONLoader redditPostJSONLoader = new RedditPostJSONLoader();
        redditPostJSONLoader.setLimit(10);
        redditPostJSONLoader.setPageNumber(1);
        redditPostJSONLoader.loadJSON();
        assertNotNull(redditPostJSONLoader.getJsonObject());
//        for (int i = 0; i < redditPostJSONLoader.getPostsList().size(); i++) {
//            System.out.println(redditPostJSONLoader.getPostsList().get(i));
//        }
    }
}