package org.newsaggregator.newsaggregatorclient;

import org.newsaggregator.newsaggregatorclient.downloaders.CoinPriceRetriever;

public class CoinRetrieverTest {
    public static void main(String[] args) {
        CoinPriceRetriever coinRetriever = new CoinPriceRetriever();
        try{
            coinRetriever.sendRequest(false, "coins.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
