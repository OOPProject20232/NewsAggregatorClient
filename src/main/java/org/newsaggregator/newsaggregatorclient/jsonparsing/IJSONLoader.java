package org.newsaggregator.newsaggregatorclient.jsonparsing;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.json.JSONObject;

public interface IJSONLoader {
    /**
     * Interface này chứa các hàm để load dữ liệu từ file JSON
     */
    String DOMAIN = "https://newsaggregator-mern.onrender.com/";
    /**
     * Hàm loadJSON dùng để load dữ liệu từ file JSON
     */
    JSONObject loadJSON();
}
