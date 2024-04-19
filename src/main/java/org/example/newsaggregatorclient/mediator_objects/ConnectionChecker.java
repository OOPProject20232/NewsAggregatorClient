package org.example.newsaggregatorclient.mediator_objects;

import java.io.IOException;
import java.net.*;

public class ConnectionChecker {

    public ConnectionChecker(){

    }

    public boolean checkInternetConnection() {
        try {
            URL testAddress = URI.create("https://www.google.com").toURL();
            URLConnection connection = testAddress.openConnection();
            connection.connect();
            return true;
        }
        catch (MalformedURLException e) {
            return false;
        }
        catch (IOException e) {
            return false;
        }
    }
}
