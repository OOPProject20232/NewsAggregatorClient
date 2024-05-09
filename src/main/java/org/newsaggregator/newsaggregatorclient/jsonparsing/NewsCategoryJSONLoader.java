package org.newsaggregator.newsaggregatorclient.jsonparsing;

import org.json.JSONObject;

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

    }


    public String getJSONString() {
        return jsonString;
    }

//    public List<NewsItemData> getNewsItemDataList(int limit, int begin) {
//    }
}
