package org.newsaggregator.newsaggregatorclient.jsonparsing;

import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.downloaders.DataReaderFromIS;

public class SearchJSONLoader implements IJSONLoader {
    String searchQuery;
    String searchType;
    String isDesc;
    JSONObject jsonObject;
    String isExactOrRegex;
    int limit = 10;
    int page = 1;

    public SearchJSONLoader(String searchQuery, String searchType, String isDesc, String isExactOrRegex) {
        this.searchQuery = searchQuery;
        this.searchType = searchType;
        this.isDesc = isDesc;
        this.isExactOrRegex = isExactOrRegex;
        if (!isExactOrRegex.equals("e") && !isExactOrRegex.equals("r")) {
            throw new IllegalArgumentException("isExactOrRegex must be either 'e' or 'r'");
        }
    }

    @Override
    public void loadJSON() {
        String url = DOMAIN + "v1/articles/search?text=%s&sort=%s&page=%s&limit=%s&opt=%s".formatted(searchQuery, isDesc, page, limit, isExactOrRegex);
        switch (searchType){
            case "articles":
                url = DOMAIN + "v1/articles/search?text=%s&sort=%s&page=%s&limit=%s&opt=%s".formatted(searchQuery, isDesc, page, limit, isExactOrRegex);
                break;
            case "posts":
                url = DOMAIN + "v1/posts/search?text=%s&sort=%s&page=%s&limit=%s&opt=%s".formatted(searchQuery, isDesc, page, limit, isExactOrRegex);
                break;
            case "categories":
                url = DOMAIN + "v1/categories/search?text=%s&sort=%s&page=%s&limit=%s&opt=%s".formatted(searchQuery, isDesc, page, limit, isExactOrRegex);
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

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setIsDesc(String isDesc) {
        this.isDesc = isDesc;
    }
}
