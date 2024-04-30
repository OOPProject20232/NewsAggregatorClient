package org.newsaggregator.newsaggregatorclient.jsonparsing;

import org.json.JSONArray;
import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.downloaders.NewsRetriever;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    public void loadJSON() {
        JSONFileLoader jsonFileLoader = new JSONFileLoader(cacheFileName);
        jsonObject = jsonFileLoader.loadJSON();
    }


    public String getJSONString() {
        return jsonString;
    }

//    public List<NewsItemData> getNewsItemDataList(int limit, int begin) {
//    }
}
