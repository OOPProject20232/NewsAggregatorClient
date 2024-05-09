package org.newsaggregator.newsaggregatorclient.downloaders;

import java.net.MalformedURLException;

@Deprecated
public class NewsRetriever implements ICacheDownloader {
    private int pageNumber;
    private int limit;
    private boolean forceDownload;

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


    @Override
    public synchronized int downloadCache(boolean isPaged, String cacheFilePath) throws MalformedURLException {
        String endpoint = "v1/articles";
        GenericRetriever genericRetriever = new GenericRetriever();
        genericRetriever.setPaged(true);
        genericRetriever.setDomain(serverDomain);
        genericRetriever.setPageNumber(pageNumber);
        genericRetriever.setLimit(limit);
        genericRetriever.setForceDownload(forceDownload);
        return genericRetriever.sendRequest(cacheFolder, cacheFilePath, endpoint);
    }

    public void setForceDownload(boolean forceDownload) {
        this.forceDownload = forceDownload;
    }
}
