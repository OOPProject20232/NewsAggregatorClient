package org.newsaggregator.newsaggregatorclient.jsonparsing;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;

import java.util.ArrayList;
import java.util.List;

public class JSON2NewsItemData {
    /**
     * Class này là 1 tiện ích chuyển đổi từ 1 JSONObject có định dạng 1 article (không phải post) sang NewsItemData
     */
    public NewsItemData convert( JSONObject newsItemObject) {
        NewsItemData newsItemData = new NewsItemData();
        List<Object> categoryListObj = newsItemObject.getJSONArray("categories").toList();
        List<String> categoryList = new ArrayList<>();
        for (Object category : categoryListObj) {
            categoryList.add(category.toString());
        }
        newsItemData.setCategory(categoryList);
        newsItemData.setTitle(getSimpleField(newsItemObject,"article_title"));
        newsItemData.setAuthor(getSimpleField(newsItemObject, "author"));
        newsItemData.setDescription(getSimpleField(newsItemObject, "article_summary"));
        newsItemData.setArticleDetailedContent(getSimpleField(newsItemObject, "article_detailed_content"));
        newsItemData.setUrl(getSimpleField(newsItemObject, "article_link"));
        JSONObject publisherJSONObject = getComplexField(newsItemObject, "publisher");
        newsItemData.setPublisher(getSimpleField(publisherJSONObject,"name"));
        newsItemData.setPublisherLogoURL(getSimpleField(publisherJSONObject, "logo"));
        String thumbnailImage;
        try {
            thumbnailImage = newsItemObject.getString("thumbnail_image");
        }
        catch (Exception e) {
            thumbnailImage = "";
        }
        newsItemData.setUrlToImage(thumbnailImage);
        try{
            newsItemData.setPublishedAt(newsItemObject.getString("creation_date"));
        }
        catch (Exception e) {
            newsItemData.setPublishedAt("");
        }
        return newsItemData;
    }

    private String getSimpleField(JSONObject jsonObject, String field) {
        try {
            return jsonObject.getString(field);
        }
        catch (Exception e) {
            return null;
        }
    }

    private JSONObject getComplexField(JSONObject jsonObject, String field) {
        try {
            return jsonObject.getJSONObject(field);
        }
        catch (Exception e) {
            return null;
        }
    }
}
