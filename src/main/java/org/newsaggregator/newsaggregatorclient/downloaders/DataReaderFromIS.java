package org.newsaggregator.newsaggregatorclient.downloaders;

import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.NoInternetDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class to fetch JSON data from the internet
 * with or without caching
 */
public class DataReaderFromIS {
    /**
     * Using HttpURLConnection to fetch JSON data from the internet
     * @param urlString: The URL of the JSON data
     *
     */
    public static JSONObject fetchJSON(String urlString) throws MalformedURLException {
        System.out.println("Sending request to: " + urlString);
        URL url = URI.create(urlString).toURL();
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(5000);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return new JSONObject(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Fetch JSON data from the internet and cache it
     * @param urlString: The URL of the JSON data
     */
    public static JSONObject fetchJSONWithCache(String urlString, String fileName) throws MalformedURLException, NoRouteToHostException {
        //If folder not exists, create it
        Path folderPath = Path.of("src/main/resources/json/");
        if (!Files.exists(folderPath)) {
            try {
                Files.createDirectory(folderPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Path cachePath = Path.of(folderPath + "/" + fileName);
        try {
            if (Files.exists(cachePath)) {
                int responseCode = request(urlString, cachePath);
                System.out.println("Response code: " + responseCode);
                if (responseCode == 304) {
                    System.out.println("Cache is up to date");
                    return new JSONObject(Files.readString(cachePath));
                }
            }
            JSONObject jsonObject = fetchJSON(urlString);
            if (Files.exists(cachePath)) {
                Files.delete(cachePath);
            }
            Files.write(cachePath, jsonObject.toString().getBytes());
            return jsonObject;
        } catch (NoRouteToHostException e){
            NoInternetDialog noInternetDialog = new NoInternetDialog();
            noInternetDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void fetchFileWithCache(String urlString, String cacheFile, String cacheFolder) throws NoRouteToHostException {
        //If folder not exists, create it
        Path folderPath = Path.of(cacheFolder);
        if (!Files.exists(folderPath)) {
            try {
                Files.createDirectory(folderPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Path cachePath = Path.of(cacheFile);
        try {
            if (Files.exists(cachePath)) {
                int responseCode = request(urlString, cachePath);
                if (responseCode == 304) {
                    System.out.println("Cache is up to date");
                    return;
                }
            }
            HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "image");
            connection.setConnectTimeout(5000);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                Files.write(cachePath, new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine().getBytes());
            }
        }
        catch (NoRouteToHostException e){
            throw new NoRouteToHostException("No Internet");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int request(String urlString, Path cachePath) throws IOException {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("If-Modified-Since", Files.getLastModifiedTime(cachePath).toString());
            connection.setConnectTimeout(5000);
            connection.connect();
            int responseCode = connection.getResponseCode();
            return responseCode;
        }
        catch (NoRouteToHostException e){
            throw new NoRouteToHostException("No Internet connection");
        }
    }
}
