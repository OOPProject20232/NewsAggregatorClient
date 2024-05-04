package org.newsaggregator.newsaggregatorclient.downloaders;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

public class CoinPriceRetriever implements IServerRequest{
    @Override
    public int sendRequest(String endpoint, boolean isPaged, String cacheFilePath) throws MalformedURLException {
        URL url = URI.create(serverDomain + "v1/" + endpoint).toURL();
        try {
            String cacheFile = cacheFolder + cacheFilePath;
            // Check if file exists
            boolean fileExists = new File(cacheFile).exists();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (fileExists) {
                System.out.println("Cache file exists, sending request with If-Modified-Since header");
                connection.setRequestMethod("HEAD");
                connection.setRequestProperty("If-Modified-Since", Files.readAttributes(new File(cacheFile).toPath(), BasicFileAttributes.class).lastModifiedTime().toString());
                int responseCode = connection.getResponseCode();
                if (responseCode == 304) {
                    System.out.println("Server responded with 304 Not Modified, no need to download again");
                    return 304;
                }
            }
            else{
                System.out.println("Cache file does not exist, sending request without If-Modified-Since header");
                connection.setRequestMethod("GET");
            }
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Server responded with 200 OK, downloading file");
                File cache = new File(cacheFile);
                if (cache.exists()){
                    cache.delete();
                }
                Files.copy(connection.getInputStream(), cache.toPath());
                return 200;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
