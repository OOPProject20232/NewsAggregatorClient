package org.newsaggregator.newsaggregatorclient.jsonparsing;

import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.datamodel.RedditPostData;
import org.newsaggregator.newsaggregatorclient.downloaders.DataReaderFromIS;
import java.util.List;

public class RedditPostJSONLoader implements IJSONLoader{
    private JSONObject jsonObject;
    private int pageNumber;
    private int limit;

    @Override
    public void loadJSON() {
        String url = DOMAIN + "v1/posts";
        try{
            jsonObject = DataReaderFromIS.fetchJSON(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    //
//    public List<RedditPostData> getPostsList(){
//
//    }
}
