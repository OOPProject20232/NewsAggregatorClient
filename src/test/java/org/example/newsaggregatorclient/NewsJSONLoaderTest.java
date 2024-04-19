package org.example.newsaggregatorclient;

import org.example.newsaggregatorclient.mediator_objects.NewsJSONLoader;

public class NewsJSONLoaderTest {
    public static void main(String[] args) {
        NewsJSONLoader newsJSONLoader = new NewsJSONLoader();
        newsJSONLoader.loadJSON();
        newsJSONLoader.extractData();
    }
}
