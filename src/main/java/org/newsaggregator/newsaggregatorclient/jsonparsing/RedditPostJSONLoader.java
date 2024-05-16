package org.newsaggregator.newsaggregatorclient.jsonparsing;

import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.datamodel.RedditPostData;
import org.newsaggregator.newsaggregatorclient.downloaders.DataReaderFromIS;

import java.util.ArrayList;
import java.util.List;

public class RedditPostJSONLoader implements IJSONLoader{
    private JSONObject jsonObject;
    private int pageNumber;
    private int limit;

    @Override
    public JSONObject loadJSON() {
        if (pageNumber == 0 || limit == 0) {
            throw new IllegalArgumentException("Page number and limit must be set before loading JSON");
        }
        String url = DOMAIN + "v1/posts?limit=" + limit + "&page=" + pageNumber;
        String cacheFileName = "redditPosts" + pageNumber + ".json";
        try{
            jsonObject = DataReaderFromIS.fetchJSONWithCache(url, cacheFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public String getJSONString(){
        try {
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<RedditPostData> getRedditPostsList(int limit, int begin, JSONObject jsonObject){
        List<RedditPostData> redditPostDataList = new ArrayList<>();
        for (int i = begin; i < begin + limit; i++) {
            JSONObject post = jsonObject.getJSONArray("posts").getJSONObject(i);
            RedditPostData redditPostData = new RedditPostData();
            redditPostData.setTitle(post.getString("post_title"));
            redditPostData.setAuthor(post.getString("author"));
            redditPostData.setUrl(post.getString("post_link"));
            redditPostData.setSubreddit(post.getString("website_source"));
            redditPostData.setPostedTime(post.getString("creation_date"));
            redditPostData.setUpvotes(post.getInt("up_votes"));
            redditPostData.setDownvotes(post.getInt("down_votes"));
            redditPostData.setPostContent(post.getString("post_content"));
            try{
                redditPostData.setMediaUrl(post.getString("media_url"));
            } catch (Exception e) {
                redditPostData.setMediaUrl("");
            }
            redditPostDataList.add(redditPostData);
        }
        return redditPostDataList;
    }

    //
//    public List<RedditPostData> getPostsList(){
//
//    }
}
