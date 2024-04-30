package org.newsaggregator.newsaggregatorclient.jsonparsing;

import java.io.File;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;

@Deprecated
public class DataLoaderFromJSON {
    /**
     * Class này dùng để load dữ liệu từ file JSON
     * Dữ liệu được load sẽ được chuyển thành một Map<String, List<NewsItemData>>
     * Trong đó key của Map là tên của một category, value là một List chứa các NewsItemData
     * @see NewsItemData
     * @deprecated Lớp này được thay thế bởi NewsJSONLoader
     */

    public DataLoaderFromJSON() {
    }
    public List<NewsItemData> loadJSON() {
        try {
            File dataFile = new File("src/main/resources/json/data.json");
            Scanner scanner = new Scanner(dataFile);
            StringBuilder rawData = new StringBuilder();
            while (scanner.hasNextLine()) {
                rawData.append(scanner.nextLine());
            }
            String jsonString = rawData.toString();
            JSONArray jsonArray = new JSONArray(jsonString);
            List<NewsItemData> newsItemDataList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSON2NewsItemData json2NewsItemData = new JSON2NewsItemData();
                NewsItemData newsItemData = json2NewsItemData.convert(jsonObject);
                newsItemDataList.add(newsItemData);
            }
            return newsItemDataList;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
