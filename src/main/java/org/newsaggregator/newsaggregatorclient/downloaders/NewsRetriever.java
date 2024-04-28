package org.newsaggregator.newsaggregatorclient.downloaders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.Scanner;

public class NewsRetriever implements IServerRequest{
    private int pageNumber;
    private int limit;
    private boolean forceDownload;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public int sendRequest(String endpoint, boolean isPaged, String cacheFilePath) throws MalformedURLException {
        try {
            /**
             * Class này chứa các hàm để gửi request đến server
             */
            String serverURLString = serverDomain + "v1/" + endpoint;
            URL url = URI.create(serverURLString).toURL();
            if (isPaged) {
                url = URI.create(serverURLString + "?page=%s&limit=%s".formatted(pageNumber, limit)).toURL();
            }
            String cacheFile = cacheFolder + cacheFilePath;
            Path cachePath = Paths.get(cacheFile);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (forceDownload) {
                System.out.println("Force download enabled, deleting cache file");
                Files.delete(cachePath);
            }
            if (cachePath.toFile().exists() && cachePath.toFile().length() == 0) {
                Files.delete(cachePath);
            }
            if (cachePath.toFile().exists() && cachePath.toFile().length() > 0) {
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
                        return 304;
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
                FileOutputStream fileOutputStream;
                try {
                    if (cachePath.toFile().length() == 0) {
                        fileOutputStream = new FileOutputStream(cacheFile, true);
                    }
                    else {
                        fileOutputStream = new FileOutputStream(cacheFile, false);
                    }
                    InputStream iStream = url.openStream();
                    byte buffer[] = new byte[1024];
                    int length;
                    while ((length = iStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, length);
                    }
                    fileOutputStream.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("File downloaded successfully!");
                return 200;
            }
            else {
                System.out.println("Server responded with " + responseCode + " " + connection.getResponseMessage());
                return responseCode;
            }
        }
        catch (IOException e) {
            System.out.println("Error reading file attributes: " + e.getMessage());
        }
        return 0;
    }

    public void setForceDownload(boolean forceDownload) {
        this.forceDownload = forceDownload;
    }
}
