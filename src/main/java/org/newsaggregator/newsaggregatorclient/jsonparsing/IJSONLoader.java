package org.newsaggregator.newsaggregatorclient.jsonparsing;

public interface IJSONLoader {
    /**
     * Interface này chứa các hàm để load dữ liệu từ file JSON
     */
    public final String DOMAIN = "https://newsaggregator-mern.onrender.com/";
    /**
     * Hàm loadJSON dùng để load dữ liệu từ file JSON
     */
    public void loadJSON();
}
