package org.newsaggregator.newsaggregatorclient.jsonparsing;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;

import java.util.ArrayList;
import java.util.List;

/**
 * Class này là 1 tiện ích chuyển đổi từ 1 JSONObject có định dạng 1 article (không phải post) sang NewsItemData
 */
public class JSON2NewsItemData {
    /**
     * Chuyển đổi từ JSONObject sang NewsItemData
     * @param newsItemObject: JSONObject cần chuyển đổi
     * @return NewsItemData: NewsItemData sau khi chuyển đổi
     */
    public NewsItemData convert( JSONObject newsItemObject) {
        NewsItemData newsItemData = new NewsItemData();
        List<Object> categoryListObj = newsItemObject.getJSONArray("categories").toList();
        List<String> categoryList = new ArrayList<>();
        for (Object category : categoryListObj) {
            categoryList.add(category.toString());
        }
        newsItemData.setGuid(getSimpleField(newsItemObject, "guid"));
        newsItemData.setCategory(categoryList);
        try {
            newsItemData.setTitle(getSimpleField(newsItemObject,"article_title"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    /**
     * Lấy giá trị của 1 field trong JSONObject
     * @param jsonObject: JSONObject cần lấy giá trị
     * @param field: Tên field cần lấy giá trị
     * @return String: Giá trị của field
     */
    private String getSimpleField(JSONObject jsonObject, String field) {
        try {
            return jsonObject.getString(field);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Lấy giá trị của 1 nested field trong JSONObject
     * @param jsonObject: JSONObject cần lấy giá trị
     * @param field: Tên nested field cần lấy giá trị
     * @return JSONObject: Giá trị của nested field
     */
    private JSONObject getComplexField(JSONObject jsonObject, String field) {
        try {
            return jsonObject.getJSONObject(field);
        }
        catch (Exception e) {
            return null;
        }
    }
}
