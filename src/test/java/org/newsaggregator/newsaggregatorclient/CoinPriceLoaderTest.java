package org.newsaggregator.newsaggregatorclient;

import org.newsaggregator.newsaggregatorclient.datamodel.CoinPriceData;
import org.newsaggregator.newsaggregatorclient.jsonparsing.CoinPriceJSONLoader;

import java.net.NoRouteToHostException;

public class CoinPriceLoaderTest {
    public static void main(String[] args) throws NoRouteToHostException {
        CoinPriceData coinPriceData = new CoinPriceData();
        CoinPriceJSONLoader coinPriceJSONLoader = new CoinPriceJSONLoader();
        coinPriceJSONLoader.loadJSON();
        coinPriceData = coinPriceJSONLoader.getDataList().get(0);
        System.out.println(coinPriceData);
    }
}
