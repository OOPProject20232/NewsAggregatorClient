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
        String jsonFilePath = JSON_FOLDER_PATH + cacheFileName;
        System.out.println("Loading JSON file from " + jsonFilePath);
        File dataFile = new File(jsonFilePath);
        Scanner scanner;
        try {
            scanner = new Scanner(dataFile);
        } catch (FileNotFoundException e) {
            NewsRetriever newsRetriever = new NewsRetriever();
            try {
                newsRetriever.sendRequest("v1/categories/articles/search?text=%s".formatted(category), false, jsonFilePath);
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


    public String getJSONString() {
        return jsonString;
    }

    public List<NewsItemData> getNewsItemDataList(int limit, int begin) {
        System.out.println("Getting news item data list from JSON file");
        JSONArray newsItems = jsonObject.getJSONArray("articles");
        List<NewsItemData> newsItemDataList = new ArrayList<>();
        if (begin >= newsItems.length()) {
            return newsItemDataList;
        }
        if (begin + limit > newsItems.length()) {
            limit = newsItems.length() - begin;
        }

        System.out.println("Count: " + newsItems.length());
        System.out.println("Begin: " + begin);
        System.out.println("Limit: " + limit);

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
            JSONObject publisherJSONObject = newsItemObject.getJSONObject("publisher");
            String publisher = publisherJSONObject.getString("name");
            String publisherLogo = publisherJSONObject.getString("logo");
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
            newsItemData.publisherLogoURL = publisherLogo;
            newsItemDataList.add(newsItemData);
        }
        return newsItemDataList;
    }
}