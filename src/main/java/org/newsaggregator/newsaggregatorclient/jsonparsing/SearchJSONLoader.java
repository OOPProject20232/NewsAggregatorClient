package org.newsaggregator.newsaggregatorclient.jsonparsing;

import org.json.JSONArray;
import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.datamodel.GenericData;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.downloaders.DataReaderFromIS;

import java.util.ArrayList;
import java.util.List;

public class SearchJSONLoader<T extends GenericData> implements IJSONLoader {
    String searchQuery;
    String searchType;
    String isDesc;
    String searchField;
    JSONObject jsonObject;
    String isExactOrRegex="r";
    int limit = 10;
    int page = 1;

    public SearchJSONLoader(String searchQuery, String searchType, String searchField, String isDesc, String isExactOrRegex) {
        this.searchQuery = searchQuery;
        this.searchType = searchType;
        this.searchField = searchField;
        this.isDesc = isDesc;
        this.isExactOrRegex = isExactOrRegex;
//        if (!isExactOrRegex.equals("e") && !isExactOrRegex.equals("r")) {
//            throw new IllegalArgumentException("isExactOrRegex must be either 'e' or 'r'");
//        }
    }

    @Override
    public JSONObject loadJSON() {
        String url = DOMAIN + "v1/articles/search/%s?sort=%s&page=%s&limit=%s&opt=%s".formatted(searchQuery, isDesc, page, limit, isExactOrRegex);
        switch (searchType){
            case "articles":
                if (searchField.equals("all"))
                    url = DOMAIN + "v1/articles/search/%s?sort=%s&page=%s&limit=%s&opt=%s".formatted(searchQuery, isDesc, page, limit, isExactOrRegex);
                else if (searchField.equals("categories")) {
                    url = DOMAIN + "v1/categories/articles/search?text=%s&sort=%s&page=%s&limit=%s&opt=%s".formatted(searchQuery, isDesc, page, limit, isExactOrRegex);
                }
                break;
            case "reddit":
                if (searchField.equals("all"))
                    url = DOMAIN + "v1/reddit/search/%s?sort=%s&page=%s&limit=%s&opt=%s".formatted(searchQuery, isDesc, page, limit, isExactOrRegex);
                else if (searchField.equals("categories")) {
                    url = DOMAIN + "v1/reddit/categories/%s&sort=%s&page=%s&limit=%s&opt=%s".formatted(searchQuery, isDesc, page, limit, isExactOrRegex);
                }
                break;
        }
        try {
            jsonObject = DataReaderFromIS.fetchJSON(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public String toString() {
        return jsonObject.toString();
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setIsDesc(String isDesc) {
        this.isDesc = isDesc;
    }

    public <D extends GenericData> List<D> getNewsItemDataList(int begin, int limit) {
        if (searchType.equals("articles")) {
            System.out.println("\u001B[34m"+"Getting news item data list from JSON file"+ "\u001B[0m");
            JSONArray newsItems = jsonObject.getJSONArray("articles");
            List<NewsItemData> newsItemDataList = new ArrayList<>();
            if (begin >= newsItems.length()) {
                return (List<D>) newsItemDataList;
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
            return (List<D>) newsItemDataList;
        }
        return null;
    }

    public int getCount() {
        try {
            return jsonObject.getInt("count");
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
