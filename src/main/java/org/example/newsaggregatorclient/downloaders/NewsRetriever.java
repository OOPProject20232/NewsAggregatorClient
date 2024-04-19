package org.example.newsaggregatorclient.downloaders;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class NewsRetriever implements IServerRequest{

    private final String serverURLString = "https://newsaggregator-mern.onrender.com/v1/articles";
    private final String cacheFile = cacheFolder + "news.json";
    @Override
    public void sendRequest() throws MalformedURLException {
        try {
            URL url = URI.create(serverURLString).toURL();
            Path cachePath = Paths.get(cacheFile);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (cachePath.toFile().exists()) {
                System.out.println("Cache file exists, sending request with If-Modified-Since header");
                BasicFileAttributes attr = Files.readAttributes(cachePath, BasicFileAttributes.class);
                ZonedDateTime lastModified = attr.lastModifiedTime().toInstant().atZone(ZoneOffset.UTC);
                try {
                    connection.setRequestMethod("HEAD");
                    connection.setRequestProperty("If-Modified-Since", lastModified.toString());
                    System.out.println("If-Modified-Since: " + lastModified.toString());
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 304) {
                        System.out.println("Server responded with 304 Not Modified, no need to download again");
                        return;
                    }
                } catch (IOException e) {
                    System.out.println("Error sending request: " + e.getMessage());
                }
            }
            else{
                System.out.println("Cache file does not exist, sending request without If-Modified-Since header");
                try {
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept", "application/json");
                } catch (IOException e) {
                    System.out.println("Error sending request: " + e.getMessage());
                }
            }
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Server responded with 200 OK, downloading file");
                Files.copy(connection.getInputStream(), cachePath);
                System.out.println("File downloaded successfully!");
            }
            else {
                System.out.println("Server responded with " + responseCode + " " + connection.getResponseMessage());
            }
        }
        catch (IOException e) {
            System.out.println("Error reading file attributes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
