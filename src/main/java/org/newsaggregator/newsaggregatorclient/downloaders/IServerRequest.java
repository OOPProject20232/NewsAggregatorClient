package org.newsaggregator.newsaggregatorclient.downloaders;

import java.net.MalformedURLException;

public interface IServerRequest {
    /**
     * Interface này chứa các hàm để gửi request đến server
     */
    String serverDomain = "https://newsaggregator-mern.onrender.com/";
    String cacheFolder = "src/main/resources/json/";
    int sendRequest(boolean isPaged, String cacheFilePath) throws MalformedURLException;
}
