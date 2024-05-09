package org.newsaggregator.newsaggregatorclient.downloaders;

import java.net.MalformedURLException;

@Deprecated
public interface ICacheDownloader {
    /**
     * Interface này chứa các hàm để gửi request đến server
     */
    String serverDomain = "https://newsaggregator-mern.onrender.com/";
    String cacheFolder = "src/main/resources/json/";
    int downloadCache(boolean isPaged, String cacheFilePath) throws MalformedURLException;
}
