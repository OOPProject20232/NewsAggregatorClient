package org.newsaggregator.newsaggregatorclient.jsonparsing;

import org.json.JSONArray;
import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.downloaders.DataReaderFromIS;

import java.util.ArrayList;
import java.util.List;

public class NewsCategoryJSONLoader implements IJSONLoader{
    private JSONObject jsonObject;
    private String jsonString;
    private String cacheFileName;
    private String category;

    public void setCacheFileName(String cacheFileName) {
        this.cacheFileName = cacheFileName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public synchronized void loadJSON() {
        try {
            cacheFileName = "news_" + category + ".json";
            String urlString = DOMAIN + "v1/categories/articles/search?text=" + category + "&opt=e&page=1&limit=5";
            jsonObject = DataReaderFromIS.fetchJSONWithCache(urlString, cacheFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public JSONObject getJSONObject() {
        return jsonObject;
    }

    public String getJSONString() {
        try {
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public List<NewsItemData> getNewsItemDataList(int limit, int begin) {
        List<NewsItemData> newsItemDataList = new ArrayList<>();
        try {
            JSONArray categories = jsonObject.getJSONArray("categories");
            if (begin + limit > categories.length()) {
                limit = categories.length() - begin;
            }
            for (int i = begin; i < limit; i++) {
                JSONObject categoryObject = categories.getJSONObject(i);
                JSONArray newsItems = categoryObject.getJSONArray("articles");
                for (int j = begin; j < begin + limit; j++) {
                    JSONObject newsItemObject = newsItems.getJSONObject(j);
                    JSON2NewsItemData json2NewsItemData = new JSON2NewsItemData();
                    NewsItemData newsItemData = json2NewsItemData.convert(newsItemObject);
                    newsItemDataList.add(newsItemData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsItemDataList;
    }
}
