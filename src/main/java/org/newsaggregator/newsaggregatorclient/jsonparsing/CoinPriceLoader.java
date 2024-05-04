package org.newsaggregator.newsaggregatorclient.jsonparsing;

import javafx.scene.control.ColorPicker;
import org.json.JSONArray;
import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.datamodel.CoinPriceData;
import org.newsaggregator.newsaggregatorclient.util.CreateJSONCache;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

import static java.util.Collections.max;

public class CoinPriceLoader implements IJSONLoader{
    private String filePath;
    JSONObject coinPrices;
    @Override
    public void loadJSON() {
        JSONFileReader jsonFileReader = new JSONFileReader(filePath);
        try{
            coinPrices = jsonFileReader.loadJSON();
        } catch (Exception e) {
            CreateJSONCache.createFolder(jsonFileReader.getFolderPath());
            coinPrices = jsonFileReader.loadJSON();
        }
    }

    @Override
    public void setCacheFileName(String cacheFileName) {
        this.filePath = cacheFileName;
    }

    public List<CoinPriceData> getNewestCoinPrices() throws IllegalArgumentException{
        List<CoinPriceData> coinPrices = new ArrayList<>();
        JSONArray coinPricesArray = this.coinPrices.getJSONArray("coins");
        for (int i = 0; i < coinPricesArray.length(); i++) {
            JSONObject coin = coinPricesArray.getJSONObject(i);
            String coinName = coin.getString("name");
            JSONArray prices = coin.getJSONArray("prices");
            JSONObject newestPrice = prices.getJSONObject(0);
            String key = max(newestPrice.toMap().keySet());
            LocalDate today = LocalDate.now();
            LocalDateTime todayAt7AM = today.atTime(7, 0, 0);
            todayAt7AM.format(DateTimeFormatter.ISO_DATE_TIME);
            System.out.println("Newest price date: " + todayAt7AM);
            String price = newestPrice.getString(key);
            CoinPriceData coinPriceData = new CoinPriceData();
            coinPriceData.setCoinName(coinName);
            coinPriceData.setPrice(price);
            coinPrices.add(coinPriceData);
        }
        // Print all values in coinPrices
        for (CoinPriceData coinPriceData : coinPrices) {
            System.out.println(coinPriceData);
        }
        return coinPrices;
    }

    public Map<String, List<CoinPriceData>> getCoinPricesByPeriod(int period) throws IllegalArgumentException{
        Map<String, List<CoinPriceData>> coinPrices = new HashMap<>();
        JSONArray coinPricesArray = this.coinPrices.getJSONArray("coins");
        for (int i = 0; i < coinPricesArray.length(); i++){
            JSONObject coin = coinPricesArray.getJSONObject(i);
            String coinName = coin.getString("name");
            JSONArray pricesArray = coin.getJSONArray("prices");
            JSONObject prices = pricesArray.getJSONObject(0);
            List<CoinPriceData> coinPriceDataList = new ArrayList<>();
            for (int j = 0; j < period; j++){
                LocalDate date = LocalDate.now().minusDays(j);
                LocalDateTime dateAt7AM = date.atTime(7, 0, 0);
                dateAt7AM.format(DateTimeFormatter.ISO_DATE_TIME);
                String price = prices.getString(dateAt7AM.toString() + ":00Z");
                CoinPriceData coinPriceData = new CoinPriceData();
                coinPriceData.setCoinName(coinName);
                coinPriceData.setPrice(price);
                coinPriceDataList.add(coinPriceData);
            }
            coinPrices.put(coinName, coinPriceDataList);
        }
        // Print all values in coinPrices
        for (Map.Entry<String, List<CoinPriceData>> entry : coinPrices.entrySet()) {
            System.out.println("Coin: " + entry.getKey());
            for (CoinPriceData coinPriceData : entry.getValue()) {
                System.out.println(coinPriceData);
            }
        }
        return coinPrices;
    }
}
