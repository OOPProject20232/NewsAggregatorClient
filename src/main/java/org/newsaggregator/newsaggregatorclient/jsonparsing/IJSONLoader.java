package org.newsaggregator.newsaggregatorclient.jsonparsing;

public interface IJSONLoader {
    /**
     * Interface này chứa các hàm để load dữ liệu từ file JSON
     */
    public final String JSON_FOLDER_PATH = "src/main/resources/json/";
    /**
     * Hàm loadJSON dùng để load dữ liệu từ file JSON
     */
    public void loadJSON();

    public void setCacheFileName(String cacheFileName);
}
