package org.newsaggregator.newsaggregatorclient;

import org.newsaggregator.newsaggregatorclient.jsonparsing.NewsCategoryJSONLoader;

public class NewsCategoriesJSONLoaderTest {
    public static void main(String[] args) {
        NewsCategoryJSONLoader newsCategoryJSONLoader = new NewsCategoryJSONLoader();
        newsCategoryJSONLoader.setCategory("bitcoin");
        newsCategoryJSONLoader.loadJSON();
        System.out.println(newsCategoryJSONLoader.getJSONObject());
    }
}
