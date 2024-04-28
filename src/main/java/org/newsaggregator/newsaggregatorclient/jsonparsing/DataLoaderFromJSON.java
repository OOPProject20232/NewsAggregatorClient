package org.newsaggregator.newsaggregatorclient.jsonparsing;

import java.io.File;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;

@Deprecated
public class DataLoaderFromJSON {
    /**
     * Class này dùng để load dữ liệu từ file JSON
     * Dữ liệu được load sẽ được chuyển thành một Map<String, List<NewsItemData>>
     * Trong đó key của Map là tên của một category, value là một List chứa các NewsItemData
     * @see NewsItemData
     * @deprecated Lớp này được thay thế bởi NewsJSONLoader
     */

    public DataLoaderFromJSON() {
    }
    public List<NewsItemData> loadJSON() {
        try {
            File dataFile = new File("src/main/resources/json/data.json");
            Scanner scanner = new Scanner(dataFile);
            StringBuilder rawData = new StringBuilder();
            while (scanner.hasNextLine()) {
                rawData.append(scanner.nextLine());
            }
            String jsonString = rawData.toString();
            JSONArray jsonArray = new JSONArray(jsonString);
            List<NewsItemData> newsItemDataList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                List<Object> categoryListObj = jsonObject.getJSONArray("category").toList();
                List<String> categoryList = new ArrayList<>();
                for (Object category : categoryListObj) {
                    categoryList.add(category.toString());
                }
                String title = jsonObject.getString("article_title");
                String author = jsonObject.getString("author");
                String description = jsonObject.getString("article_summary");
                String articleDetailedContent = jsonObject.getString("article_detailed_content");
                String url = jsonObject.getString("article_link");
                String publisher = jsonObject.getString("website_source");
                String thumbnailImage = "";
                try {
                    thumbnailImage = jsonObject.getString("thumbnail_image");
                }
                catch (Exception e) {
                    thumbnailImage = "";
                }
                String publishedAt = jsonObject.getString("creation_date");
                String articleSummary = jsonObject.getString("article_summary");
                NewsItemData newsItemData = new NewsItemData();
                newsItemData.category = categoryList;
                newsItemData.title = title;
                newsItemData.author = author;
                newsItemData.description = description;
                newsItemData.articleDetailedContent = articleDetailedContent;
                newsItemData.url = url;
                newsItemData.urlToImage = thumbnailImage;
                newsItemData.publishedAt = publishedAt;
                newsItemData.content = articleSummary;
                newsItemData.publisher = publisher;
                newsItemDataList.add(newsItemData);
            }
            return newsItemDataList;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
