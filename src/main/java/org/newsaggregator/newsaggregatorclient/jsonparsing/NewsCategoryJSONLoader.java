package org.newsaggregator.newsaggregatorclient.jsonparsing;

import org.json.JSONArray;
import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.downloaders.DataReaderFromIS;

import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class NewsCategoryJSONLoader implements IJSONLoader<NewsItemData>{
    private JSONObject newsCategoryJsonObject;
    private String category;

    @Override
    public synchronized JSONObject loadJSON() throws NoRouteToHostException {
        try {
            String cacheFileName = "news_" + category + ".json";
            String urlString = DOMAIN + "v1/articles/categories/" + category + "?page=1&limit=5";
            newsCategoryJsonObject = DataReaderFromIS.fetchJSONWithCache(urlString, cacheFileName);
        } catch (NoRouteToHostException e) {
            throw new NoRouteToHostException("No Internet");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsCategoryJsonObject;
    }

    @Override
    public void setJSONObj(JSONObject jsonObject) {
        this.newsCategoryJsonObject = jsonObject;
    }


    public JSONObject getJSONObject() {
        return newsCategoryJsonObject;
    }

    public String getJSONString() {
        try {
            return newsCategoryJsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public List<NewsItemData> getDataList(int limit, int begin) {
        List<NewsItemData> newsItemDataList = new ArrayList<>();
        try {
            JSONArray categories = newsCategoryJsonObject.getJSONArray("categories");
//            if (begin + limit > categories.length()) {
//                limit = categories.length() - begin;
//            }
            for (int i = begin; i < categories.length(); i++) {
                try{
                    JSONObject categoryObject = categories.getJSONObject(i);
                    System.out.println(categoryObject.toMap());
                    JSONArray newsItems = categoryObject.getJSONArray("articles");
                    for (int j = 0; j < newsItems.length(); j++) {
                        JSONObject newsItemObject = newsItems.getJSONObject(j);
                        JSON2NewsItemData json2NewsItemData = new JSON2NewsItemData();
                        NewsItemData newsItemData = json2NewsItemData.convert(newsItemObject);
                        newsItemDataList.add(newsItemData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsItemDataList;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
