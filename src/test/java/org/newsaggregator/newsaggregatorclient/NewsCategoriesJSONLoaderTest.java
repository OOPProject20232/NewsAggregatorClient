package org.newsaggregator.newsaggregatorclient;

import org.newsaggregator.newsaggregatorclient.jsonparsing.NewsCategoryJSONLoader;

import java.net.NoRouteToHostException;

public class NewsCategoriesJSONLoaderTest {
    public static void main(String[] args) throws NoRouteToHostException {
        NewsCategoryJSONLoader newsCategoryJSONLoader = new NewsCategoryJSONLoader();
        newsCategoryJSONLoader.setCategory("bitcoin");
        newsCategoryJSONLoader.loadJSON();
        System.out.println(newsCategoryJSONLoader.getJSONObject());
    }
}
