package org.newsaggregator.newsaggregatorclient.jsonparsing;

import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.util.CreateJSONCache;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class JSONFileReader {
    private String cacheFileName;
    private final String JSON_FOLDER_PATH = "src/main/resources/json/";

    public JSONFileReader(String cacheFileName) {
        this.cacheFileName = cacheFileName;
    }

    public JSONObject loadJSON() {
        String jsonFilePath = JSON_FOLDER_PATH + cacheFileName;
        System.out.println("Loading JSON file from " + jsonFilePath);
        CreateJSONCache.createFolder(JSON_FOLDER_PATH);
        File dataFile = new File(jsonFilePath);
        Scanner scanner;
        try {
            scanner = new Scanner(dataFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
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

    public String getFolderPath() {
        return JSON_FOLDER_PATH;
    }
}
