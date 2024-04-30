package org.newsaggregator.newsaggregatorclient.jsonparsing;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.downloaders.NewsRetriever;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NewsJSONLoader implements IJSONLoader {
    private JSONObject jsonObject;
    private String jsonString;
    private String cacheFileName;

    @Override
    public void setCacheFileName(String cacheFileName) {
        this.cacheFileName = cacheFileName;
    }

    @Override
    public void loadJSON() {
        JSONFileLoader jsonFileLoader = new JSONFileLoader(cacheFileName);
        jsonObject = jsonFileLoader.loadJSON();
    }

    public int getCount() {
        System.out.println("Getting count from JSON file");
        return jsonObject.getInt("count");
    }

    @NotNull
    public String getJSONString() {
        String result = "";
        try {
            result = jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<NewsItemData> getNewsItemDataList(int limit, int begin) {
        System.out.println("Getting news item data list from JSON file");
        JSONArray newsItems = jsonObject.getJSONArray("articles");
        List<NewsItemData> newsItemDataList = new ArrayList<>();
        if (begin >= newsItems.length()) {
            return newsItemDataList;
        }
        if (begin + limit > newsItems.length()) {
            limit = newsItems.length() - begin;
        }

        System.out.println("Count: " + newsItems.length());
        System.out.println("Begin: " + begin);
        System.out.println("Limit: " + limit);

        for (int i = begin; i < limit + begin; i++) {
            JSONObject newsItemObject = (JSONObject) newsItems.get(i);
            JSON2NewsItemData json2NewsItemData = new JSON2NewsItemData();
            NewsItemData newsItemData = json2NewsItemData.convert(newsItemObject);
            newsItemDataList.add(newsItemData);
        }
        return newsItemDataList;
    }
}
