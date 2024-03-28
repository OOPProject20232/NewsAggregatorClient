package org.example.newsaggregatorclient.mediator_objects;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Scanner;

import org.example.newsaggregatorclient.mediator_objects.NewsItemData;

public class DataLoaderFromJSON {

    public DataLoaderFromJSON() {
    }

   public static void loadJSON(String[] args) {
       File dataFile  = new File("src/main/resources/json/data.json");
       ObjectMapper objectMapper = new ObjectMapper();
         try {
              Scanner scanner = new Scanner(dataFile);
              StringBuilder stringBuilder = new StringBuilder();
              while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine());
              }
              String json = stringBuilder.toString();
              System.out.println(json);
              objectMapper.readValue(json, NewsItemData[].class);
         } catch (Exception e) {
              e.printStackTrace();
         }
    }
}
