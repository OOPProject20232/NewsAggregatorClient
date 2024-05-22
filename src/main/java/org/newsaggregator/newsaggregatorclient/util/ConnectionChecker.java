package org.newsaggregator.newsaggregatorclient.util;

import java.io.IOException;
import java.net.*;

public class ConnectionChecker {

    public ConnectionChecker(){

    }

    public static int checkInternetConnection() throws IOException{
        try {
            URL testAddress = URI.create("https://www.google.com").toURL();
            HttpURLConnection connection = (HttpURLConnection) testAddress.openConnection();
            connection.connect();
            return connection.getResponseCode();
        }
        catch (IOException e) {
            return 0;
        }
    }
}
