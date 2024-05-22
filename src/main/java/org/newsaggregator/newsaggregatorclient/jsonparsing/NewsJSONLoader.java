package org.newsaggregator.newsaggregatorclient.jsonparsing;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.downloaders.DataReaderFromIS;

import java.net.NoRouteToHostException;
import java.util.ArrayList;
import java.util.List;

public class NewsJSONLoader implements IJSONLoader {
    private JSONObject jsonObject;
    private String jsonString;
    private String cacheFileName;
    int pageNumber;
    int limit;
    List<NewsItemData> newsItemDataList = new ArrayList<>();

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public JSONObject getJsonObject(){
        return jsonObject;
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
//            jsonObject = DataReaderFromIS.fetchJSON(url);
            jsonObject = DataReaderFromIS.fetchJSONWithCache(url, cacheFileName);

        }
        catch (NoRouteToHostException e){

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Get the count of news items from JSON file
     * @return The count of news items
     */
    public int getCount() {
        System.out.println("Getting count from JSON file");
        return jsonObject.getInt("count");
    }

    @NotNull
    public synchronized String getJSONString() {
        String result = "";
        try {
            result = jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Get news item data list from JSON file
     * @param limit: The number of news items to get
     * @param begin: The index of the first news item to get
     * @param jsonObject: The JSON object to get news items from
     * @return List of news item data
     */
    public synchronized List<NewsItemData> getNewsItemDataList(int limit, int begin, JSONObject jsonObject) {
        if (jsonObject == null) {
            jsonObject = this.jsonObject;
        }
        if (this.jsonObject == null && jsonObject == null) {
            return newsItemDataList;
        }
        System.out.println("\u001B[34m"+"Getting news item data list from JSON file"+ "\u001B[0m");
        JSONArray newsItems = jsonObject.getJSONArray("articles");
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

    public synchronized List<NewsItemData> getNewsItemDataList(int limit, int begin){
        return getNewsItemDataList(limit, begin, null);
    }
}
