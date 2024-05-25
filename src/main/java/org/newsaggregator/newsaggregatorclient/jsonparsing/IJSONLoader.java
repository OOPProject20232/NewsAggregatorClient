package org.newsaggregator.newsaggregatorclient.jsonparsing;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.datamodel.GenericData;

import java.util.List;

public interface IJSONLoader<T extends GenericData>{
    /**
     * Interface này chứa các hàm để load dữ liệu từ file JSON
     */
    String DOMAIN = "https://newsaggregator-mern.onrender.com/api/";
    /**
     * Hàm loadJSON dùng để load dữ liệu từ file JSON
     */
    JSONObject loadJSON();

    void setJSONObj(JSONObject jsonObject);

    default int getTotalPages(){
        return loadJSON().getInt("totalPages");
    }

    List<T> getDataList(int limit, int begin);
}
