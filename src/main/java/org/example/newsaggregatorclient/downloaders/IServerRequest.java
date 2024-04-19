package org.example.newsaggregatorclient.downloaders;

import java.net.MalformedURLException;

public interface IServerRequest {
    /**
     * Interface này chứa các hàm để gửi request đến server
     */
    public final String cacheFolder = "src/main/resources/json/";
    void sendRequest() throws MalformedURLException;
}
