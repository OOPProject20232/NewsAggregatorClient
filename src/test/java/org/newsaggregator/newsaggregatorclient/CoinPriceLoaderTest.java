package org.newsaggregator.newsaggregatorclient;

import org.newsaggregator.newsaggregatorclient.jsonparsing.CoinPriceJSONLoader;

public class CoinPriceLoaderTest {
    public static void main(String[] args) {
        CoinPriceJSONLoader coinPriceJSONLoader = new CoinPriceJSONLoader();
        coinPriceJSONLoader.loadJSON();
        coinPriceJSONLoader.getNewestCoinPrices();
        System.out.println("Coin prices by period: 1 day");
        coinPriceJSONLoader.getCoinPricesByPeriod(1);
        System.out.println("Coin prices by period: 7 days");
        coinPriceJSONLoader.getCoinPricesByPeriod(7);
        System.out.println("Coin prices by period: 30 days");
        coinPriceJSONLoader.getCoinPricesByPeriod(30);
    }
}
