package org.newsaggregator.newsaggregatorclient.jsonparsing;

import org.json.JSONArray;
import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.downloaders.NewsRetriever;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NewsJSONLoader implements IJSONLoader {
    private JSONObject jsonObject;
    private String jsonString;
    private String cacheFileName;

    @Override
    public void setCacheFileName(String cacheFileName) {
        this.cacheFileName = cacheFileName;
    }

    @Override
    public void loadJSON() {
        String JSON_FILE_PATH = JSON_FOLDER_PATH + cacheFileName;
        System.out.println("Loading JSON file from " + JSON_FILE_PATH);
        File dataFile = new File(JSON_FILE_PATH);
        Scanner scanner;
        try {
            scanner = new Scanner(dataFile);
        } catch (FileNotFoundException e) {
            NewsRetriever newsRetriever = new NewsRetriever();
            try {
                newsRetriever.sendRequest("articles", true, "news.json");
                newsRetriever.setLimit(50);
                newsRetriever.setPageNumber(1);
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
            String title = getSimpleField( newsItemObject,"article_title");
            String author = getSimpleField(newsItemObject, "author");
            String description = getSimpleField(newsItemObject, "article_summary");
            String articleDetailedContent = getSimpleField(newsItemObject, "article_detailed_content");
            String url = getSimpleField(newsItemObject, "article_link");
            JSONObject publisherJSONObject = getComplexField(newsItemObject, "publisher");
            String publisher = getSimpleField(publisherJSONObject,"name");
            String publisherLogo = getSimpleField(publisherJSONObject, "logo");
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
