package org.example.newsaggregatorclient.mediator_objects;

import org.json.JSONObject;
import org.json.JSONString;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class NewsJSONLoader implements IJSONLoader{
    private final String JSON_FILE_PATH = JSON_FOLDER_PATH + "news.json";
    private String jsonString;

    @Override
    public void loadJSON() {
        System.out.println("Loading JSON file from " + JSON_FILE_PATH);
        File dataFile = new File(JSON_FILE_PATH);
        Scanner scanner = null;
        try {
            scanner = new Scanner(dataFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        StringBuilder rawData = new StringBuilder();
        while (scanner.hasNextLine()) {
            rawData.append(scanner.nextLine());
        }
        jsonString = rawData.toString();
        System.out.println("JSON file loaded successfully!");
        System.out.println("JSON data: " + jsonString);
    }

    @Override
    public void extractData() {
        // Extract data from JSON object
        JSONObject jsonObject = new JSONObject(jsonString);
        System.out.println("Extracting data from JSON object");
        String count = String.valueOf(jsonObject.getInt("count"));
        System.out.println("Count: " + count);
    }
}
