package org.newsaggregator.newsaggregatorclient;

import javafx.util.Callback;

public class HostServicesControllerFactory implements Callback<Class<?>,Object> {
    /**
     * This is used for passing down the HostServices object to the controller
     * without having to pass it through the constructor
     * or interfering with the Application instance
     * suggested by https://stackoverflow.com/a/33100968 (Solution 2)
     **/
    @Override
    public Object call(Class<?> param) {
        return null;
    }
    /**
     * Factory class for creating HostServicesController
     */
}
