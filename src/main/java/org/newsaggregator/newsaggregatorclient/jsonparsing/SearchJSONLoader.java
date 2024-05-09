package org.newsaggregator.newsaggregatorclient.jsonparsing;

import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.downloaders.DataReaderFromIS;

public class SearchJSONLoader implements IJSONLoader {
    String searchQuery;
    String searchType;
    String isDesc;
    JSONObject jsonObject;

    public SearchJSONLoader(String searchQuery, String searchType, String isDesc) {
        this.searchQuery = searchQuery;
        this.searchType = searchType;
        this.isDesc = isDesc;
    }

    @Override
    public void loadJSON() {
        String url = DOMAIN + "v1/articles/search?text=%s&sort=%s".formatted(searchQuery, isDesc);
        switch (searchType){
            case "articles":
                url = DOMAIN + "v1/articles/search?text=%s&sort=%s".formatted(searchQuery, isDesc);
                break;
            case "posts":
                url = DOMAIN + "v1/posts/search?text=%s&sort=%s".formatted(searchQuery, isDesc);
                break;
            case "categories":
                url = DOMAIN + "v1/categories/search?text=%s&sort=%s".formatted(searchQuery, isDesc);
                break;
        }
        try {
            jsonObject = DataReaderFromIS.fetchJSON(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "SearchJSONLoader{" +
                "searchQuery='" + searchQuery + '\'' +
                ", searchType='" + searchType + '\'' +
                ", isDesc='" + isDesc + '\'' +
                ", jsonObject=" + jsonObject +
                '}';
    }
}
