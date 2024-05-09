package org.newsaggregator.newsaggregatorclient.downloaders;

import java.net.MalformedURLException;

@Deprecated
public class CoinPriceRetriever implements ICacheDownloader {
    private String endpoint = "v1/coins";
    @Override
    public int downloadCache(boolean isPaged, String cacheFilePath) throws MalformedURLException {
        GenericRetriever genericRetriever = new GenericRetriever();
        genericRetriever.setPaged(false);
        genericRetriever.setDomain(serverDomain);
        return genericRetriever.sendRequest(cacheFolder, cacheFilePath, endpoint);
    }
}
