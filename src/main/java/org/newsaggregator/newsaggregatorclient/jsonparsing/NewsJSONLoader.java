package org.newsaggregator.newsaggregatorclient.jsonparsing;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.downloaders.DataReaderFromIS;

import java.net.NoRouteToHostException;
import java.util.ArrayList;
import java.util.List;

public class NewsJSONLoader implements IJSONLoader<NewsItemData> {
    private JSONObject newsJsonObject;
    private int pageNumber;
    private int limit;
    private final List<NewsItemData> newsItemDataList = new ArrayList<>();

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public JSONObject getNewsJsonObject(){
        return newsJsonObject;
    }

    /**
     * Load JSON file from the given URL
     * @return The JSON object loaded from the URL
     */
    @Override
    public synchronized JSONObject loadJSON() {
        String url = DOMAIN + "v1/articles?page=" + pageNumber + "&limit=" + limit;
        String cacheFileName = "news" + pageNumber + ".json";
        try{
//            newsJsonObject = DataReaderFromIS.fetchJSON(url);
            newsJsonObject = DataReaderFromIS.fetchJSONWithCache(url, cacheFileName);

        }
        catch (NoRouteToHostException e){

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return newsJsonObject;
    }

    @Override
    public void setJSONObj(JSONObject jsonObject) {
        this.newsJsonObject = jsonObject;
    }

    /**
     * Get the count of news items from JSON file
     * @return The count of news items
     */
    public int getCount() {
        System.out.println("Getting count from JSON file");
        return newsJsonObject.getInt("count");
    }

    @NotNull
    public synchronized String getJSONString() {
        String result = "";
        try {
            result = newsJsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Get news item data list from JSON file
     * @param limit: The number of news items to get
     * @param begin: The index of the first news item to get
     * @return List of news item data
     */
    @Override
    public synchronized List<NewsItemData> getDataList(int limit, int begin) {
        if (this.newsJsonObject == null) {
            return newsItemDataList;
        }
        System.out.println("\u001B[34m"+"Getting news item data list from JSON file"+ "\u001B[0m");
        JSONArray newsItems = newsJsonObject.getJSONArray("articles");
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
            System.out.println("Added news item: " + newsItemData);
        }
        return newsItemDataList;
    }
}
