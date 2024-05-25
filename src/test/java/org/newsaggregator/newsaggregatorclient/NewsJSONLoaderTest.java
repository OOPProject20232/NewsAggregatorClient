package org.newsaggregator.newsaggregatorclient;

import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.jsonparsing.NewsJSONLoader;

import java.util.List;

public class NewsJSONLoaderTest {
    public static void main(String[] args) {
        NewsJSONLoader newsJSONLoader = new NewsJSONLoader();
        newsJSONLoader.loadJSON();
        System.out.println(newsJSONLoader.getCount());
        List<NewsItemData> newsItemDataList = newsJSONLoader.getDataList(30, 0);
        for (NewsItemData newsItemData : newsItemDataList) {
            System.out.println(newsItemData);
        }
    }
}
