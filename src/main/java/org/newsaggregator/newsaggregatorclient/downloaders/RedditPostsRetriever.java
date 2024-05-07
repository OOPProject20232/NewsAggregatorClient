package org.newsaggregator.newsaggregatorclient.downloaders;

import java.net.MalformedURLException;

public class RedditPostsRetriever implements IServerRequest{
    private int limit;
    private int pageNumber;

    @Override
    public synchronized int downloadCache(boolean isPaged, String cacheFilePath) throws MalformedURLException {
        String endpoint = "v1/posts";
        GenericRetriever genericRetriever = new GenericRetriever();
        genericRetriever.setPaged(true);
        genericRetriever.setDomain(serverDomain);
        genericRetriever.setPageNumber(pageNumber);
        genericRetriever.setLimit(limit);
        return genericRetriever.sendRequest(cacheFolder + cacheFilePath, cacheFilePath, endpoint);
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
