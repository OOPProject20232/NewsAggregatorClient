package org.newsaggregator.newsaggregatorclient;

import org.newsaggregator.newsaggregatorclient.pojos.NewsItemData;
import org.newsaggregator.newsaggregatorclient.jsonparsing.NewsJSONLoader;

import java.util.List;

public class NewsJSONLoaderTest {
    public static void main(String[] args) {
        NewsJSONLoader newsJSONLoader = new NewsJSONLoader();
        newsJSONLoader.loadJSON();
        System.out.println(newsJSONLoader.getCount());
        List<NewsItemData> newsItemDataList = newsJSONLoader.getNewsItemDataList(30);
        for (NewsItemData newsItemData : newsItemDataList) {
            System.out.println(newsItemData);
        }
    }
}
