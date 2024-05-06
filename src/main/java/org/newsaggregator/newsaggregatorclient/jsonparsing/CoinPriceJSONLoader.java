package org.newsaggregator.newsaggregatorclient.jsonparsing;

import org.json.JSONArray;
import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.datamodel.CoinPriceData;
import org.newsaggregator.newsaggregatorclient.util.CreateJSONCache;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.Collections.max;

public class CoinPriceJSONLoader implements IJSONLoader{
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
            JSONObject prices = coin.getJSONObject("prices");
            Integer rank = (Integer) coin.getInt("rank");
            String coinSymbol = coin.getString("symbol");
            String date = max(prices.toMap().keySet());
            LocalDate today = LocalDate.now();
            LocalDateTime todayAt7AM = today.atTime(7, 0, 0);
            todayAt7AM.format(DateTimeFormatter.ISO_DATE_TIME);
            System.out.println("Newest price date: " + todayAt7AM);
            String price = prices.getString(date);
            Float priceFloat = Float.parseFloat(price);
            price = String.format("%.2f", priceFloat);
            CoinPriceData coinPriceData = new CoinPriceData();
            coinPriceData.setCoinName(coinName);
            coinPriceData.setPrice(price);
            coinPriceData.setRank(rank.toString());
            coinPriceData.setCoinSymbol(coinSymbol);
            coinPriceData.setDate(date);
            coinPriceData.setPriceChange(getChangeInPrice(coinPriceData, coinSymbol));
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
            JSONObject prices = coin.getJSONObject("prices");
            String coinSymbol = coin.getString("symbol");
            List<CoinPriceData> coinPriceDataList = new ArrayList<>();
            for (int j = 0; j < period; j++){
//                LocalDate date = LocalDate.now().minusDays(j);
//                LocalDateTime dateAt7AM = date.atTime(7, 0, 0);
//                dateAt7AM.format(DateTimeFormatter.ISO_DATE_TIME);
                String key = max(prices.toMap().keySet());
                String price = prices.getString(key);
                Float priceFloat = Float.parseFloat(price);
                price = String.format("%.2f", priceFloat);
                CoinPriceData coinPriceData = new CoinPriceData();
                coinPriceData.setCoinName(coinName);
                coinPriceData.setPrice(price);
                coinPriceData.setCoinSymbol(coinSymbol);
                coinPriceData.setDate(key);
                coinPriceDataList.add(coinPriceData);
            }
            coinPrices.put(coinSymbol, coinPriceDataList);
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

    public Map<String, String> getCoinPricesBySymbol(String symbol, int period) throws IllegalArgumentException{
        Map<String, String> coinPrices = new HashMap<>();
        JSONArray coinPricesArray = this.coinPrices.getJSONArray("coins");
        for (int i = 0; i < coinPricesArray.length(); i++){
            JSONObject coin = coinPricesArray.getJSONObject(i);
            JSONObject prices = coin.getJSONObject("prices");
            String coinSymbol = coin.getString("symbol");
            if (coinSymbol.equals(symbol)){
                List<String> keys = new ArrayList<>(prices.toMap().keySet());
                keys.sort(Comparator.reverseOrder());
                for (int j = 0; j < period; j++){
                    String key = keys.get(j);
                    String price = prices.getString(key);
                    coinPrices.put(key, price);
                }
            }
        }
        return coinPrices;
    }

    public Float getChangeInPrice(CoinPriceData coinPriceData, String coinSymbol) {
        /**
         * Get the change in price compared to the previous day
         */
        JSONArray coinPricesArray = this.coinPrices.getJSONArray("coins");
        for (int i = 0; i < coinPricesArray.length(); i++){
            JSONObject coin = coinPricesArray.getJSONObject(i);
            String coinName = coin.getString("name");
            JSONObject prices = coin.getJSONObject("prices");
            String symbol = coin.getString("symbol");
            if (symbol.equals(coinSymbol)){
                List<String> keys = new ArrayList<>(prices.toMap().keySet());
                keys.sort(Comparator.reverseOrder());
                String today = keys.get(0);
                String yesterday = keys.get(1);
                float todayPrice = Float.parseFloat(prices.getString(today));
                float yesterdayPrice = Float.parseFloat(prices.getString(yesterday));
                return todayPrice - yesterdayPrice;
            }
        }
        return 0f;
    }

    public String getJSONString() {
        return coinPrices.toString();
    }
}