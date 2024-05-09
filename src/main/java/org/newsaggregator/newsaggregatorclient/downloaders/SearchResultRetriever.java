package org.newsaggregator.newsaggregatorclient.downloaders;

import org.newsaggregator.newsaggregatorclient.datamodel.GenericData;
import org.newsaggregator.newsaggregatorclient.util.CreateJSONCache;

@Deprecated
public class SearchResultRetriever implements ICacheDownloader{
    private String searchText;
    private boolean isDesc;

    public SearchResultRetriever() {

    }

    public SearchResultRetriever(String searchText, boolean isDesc){
        this.searchText = searchText;
        this.isDesc = isDesc;
    }

    public void setSearchText(String searchText){
        this.searchText = searchText;
    }

    @Override
    public int downloadCache(boolean isPaged, String cacheFilePath){
        String sortKeyword;
        if (isDesc) sortKeyword = "desc";
        else sortKeyword = "asc";
        String endpoint = "v1/articles/search?text=%s&sort=%s".formatted(searchText, sortKeyword);
        GenericRetriever retriever = new GenericRetriever();
        retriever.setDomain(serverDomain);
        retriever.setPaged(false);
        retriever.setForceDownload(true);
        return retriever.sendRequest(cacheFolder, cacheFilePath, endpoint);
    }
}
