package org.newsaggregator.newsaggregatorclient;

import org.newsaggregator.newsaggregatorclient.jsonparsing.CoinPriceLoader;

public class CoinPriceLoaderTest {
    public static void main(String[] args) {
        CoinPriceLoader coinPriceLoader = new CoinPriceLoader();
        coinPriceLoader.setCacheFileName("coins.json");
        coinPriceLoader.loadJSON();
        coinPriceLoader.getNewestCoinPrices();
        System.out.println("Coin prices by period: 1 day");
        coinPriceLoader.getCoinPricesByPeriod(1);
        System.out.println("Coin prices by period: 7 days");
        coinPriceLoader.getCoinPricesByPeriod(7);
        System.out.println("Coin prices by period: 30 days");
        coinPriceLoader.getCoinPricesByPeriod(30);
    }
}
