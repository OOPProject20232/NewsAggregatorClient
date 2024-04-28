package org.newsaggregator.newsaggregatorclient.jsonparsing;

import org.json.JSONArray;
import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.downloaders.NewsRetriever;
import org.newsaggregator.newsaggregatorclient.pojos.NewsItemData;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NewsJSONLoader implements IJSONLoader {
    private JSONObject jsonObject;
    private String jsonString;

    @Override
    public void loadJSON() {
        String JSON_FILE_PATH = JSON_FOLDER_PATH + "news.json";
        System.out.println("Loading JSON file from " + JSON_FILE_PATH);
        File dataFile = new File(JSON_FILE_PATH);
        Scanner scanner;
        try {
            scanner = new Scanner(dataFile);
        } catch (FileNotFoundException e) {
            NewsRetriever newsRetriever = new NewsRetriever();
            try {
                newsRetriever.sendRequest();
                scanner = new Scanner(dataFile);
            } catch (MalformedURLException | FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
        StringBuilder rawData = new StringBuilder();
        while (scanner.hasNextLine()) {
            rawData.append(scanner.nextLine());
        }
        jsonString = rawData.toString();
        System.out.println("JSON file loaded successfully!");
        try {
            jsonObject = new JSONObject(jsonString);
        }
        catch (Exception e) {
            return;
        }
    }

    public int getCount() {
        System.out.println("Getting count from JSON file");
        return jsonObject.getInt("count");
    }

    public String getJSONString() {
        return jsonString;
    }

    public List<NewsItemData> getNewsItemDataList(int limit, int begin) {
        System.out.println("Getting news item data list from JSON file");
        JSONArray newsItems = jsonObject.getJSONArray("articles");
        List<NewsItemData> newsItemDataList = new ArrayList<>();
        for (int i = begin; i < limit + begin; i++) {
            JSONObject newsItemObject = (JSONObject) newsItems.get(i);
            List<Object> categoryListObj = newsItemObject.getJSONArray("categories").toList();
            List<String> categoryList = new ArrayList<>();
            for (Object category : categoryListObj) {
                categoryList.add(category.toString());
            }
            String title = newsItemObject.getString("article_title");
            String author = newsItemObject.getString("author");
            String description = newsItemObject.getString("article_summary");
            String articleDetailedContent = newsItemObject.getString("article_detailed_content");
            String url = newsItemObject.getString("article_link");
            String publisher = newsItemObject.getString("website_source");
            String thumbnailImage;
            try {
                thumbnailImage = newsItemObject.getString("thumbnail_image");
            }
            catch (Exception e) {
                thumbnailImage = "";
            }
            String publishedAt = newsItemObject.getString("creation_date");
            String articleSummary = newsItemObject.getString("article_summary");
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
}
