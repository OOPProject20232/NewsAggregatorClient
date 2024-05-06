package org.newsaggregator.newsaggregatorclient.downloaders;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class CoinPriceRetriever implements IServerRequest{
    private String endpoint = "v1/coins";
    @Override
    public int sendRequest(boolean isPaged, String cacheFilePath) throws MalformedURLException {
        URL url = URI.create(serverDomain + endpoint).toURL();
        try {
            String cacheFile = cacheFolder + cacheFilePath;
            // Check if file exists
            boolean fileExists = new File(cacheFile).exists();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (fileExists) {
                System.out.println("Cache file exists, sending request with If-Modified-Since header (Coin)");
                connection.setRequestMethod("HEAD");
                connection.setRequestProperty("If-Modified-Since", Files.readAttributes(new File(cacheFile).toPath(), BasicFileAttributes.class).lastModifiedTime().toString());
                int responseCode = connection.getResponseCode();
                if (responseCode == 304) {
                    System.out.println("Server responded with 304 Not Modified, no need to download again (Coin)");
                    return 304;
                }
            }
            else{
                System.out.println("Cache file does not exist, sending request without If-Modified-Since header (Coin)");
                connection.setRequestMethod("GET");
            }
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Server responded with 200 OK, downloading file (Coin)");
                FileOutputStream fileOutputStream;
                try{
                    if (Paths.get(cacheFile).toFile().length() == 0) {
                        fileOutputStream = new FileOutputStream(cacheFile, true);
                    }
                    else {
                        fileOutputStream = new FileOutputStream(cacheFile, false);
                    }
                    InputStream iStream = url.openStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = iStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, length);
                    }
                    fileOutputStream.close();
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return 200;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}