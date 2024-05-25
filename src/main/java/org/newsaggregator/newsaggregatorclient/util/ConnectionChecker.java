package org.newsaggregator.newsaggregatorclient.util;

import org.newsaggregator.newsaggregatorclient.downloaders.DataReaderFromIS;

import java.io.IOException;
import java.net.*;

public class ConnectionChecker {

    public ConnectionChecker(){

    }

    public static int checkInternetConnection() throws IOException{
        try {
            URL testAddress = URI.create("https://newsaggregator-mern.onrender.com/api/v1/articles?page=1&limit=10").toURL();
            HttpURLConnection connection = (HttpURLConnection) testAddress.openConnection();
            connection.setConnectTimeout(5000);
            connection.connect();
            return connection.getResponseCode();
        }
        catch (IOException e) {
            return 0;
        }
    }
}
