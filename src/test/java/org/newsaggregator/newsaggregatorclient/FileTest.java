package org.newsaggregator.newsaggregatorclient;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;

public class FileTest {
    public static void main(String[] args) throws MalformedURLException {
        String urlString = "https://newsaggregator-mern.onrender.com/v1/categories/articles/search?text=btc&opt=e";
        System.out.println("Sending request to: " + urlString);
        URL url = URI.create(urlString).toURL();
        try{
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
//                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                String inputLine;
//                StringBuilder response = new StringBuilder();
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//                in.close();
                Files.copy(connection.getInputStream(), new File("tmp.json").toPath());
//
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
