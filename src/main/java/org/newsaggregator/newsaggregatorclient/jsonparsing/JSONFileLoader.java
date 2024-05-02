package org.newsaggregator.newsaggregatorclient.jsonparsing;

import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.downloaders.NewsRetriever;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.Scanner;

public class JSONFileLoader {
    private String cacheFileName;
    private final String JSON_FOLDER_PATH = "src/main/resources/json/";

    public JSONFileLoader(String cacheFileName) {
        this.cacheFileName = cacheFileName;
    }

    public JSONObject loadJSON() {
        String jsonFilePath = JSON_FOLDER_PATH + cacheFileName;
        System.out.println("Loading JSON file from " + jsonFilePath);
        File dataFile = new File(jsonFilePath);
        Scanner scanner;
        try {
            scanner = new Scanner(dataFile);
        } catch (FileNotFoundException e) {
            NewsRetriever newsRetriever = new NewsRetriever();
            try {
                newsRetriever.setLimit(50);
                newsRetriever.setPageNumber(1);
                newsRetriever.sendRequest("articles", true, "news.json");
                scanner = new Scanner(dataFile);
            } catch (MalformedURLException | FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
        StringBuilder rawData = new StringBuilder();
        while (scanner.hasNextLine()) {
            rawData.append(scanner.nextLine());
        }
        String jsonString = rawData.toString();
        System.out.println("JSON file loaded successfully!");
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
        }
        catch (Exception e) {
            return null;
        }
        return jsonObject;
    }
}
