package org.newsaggregator.newsaggregatorclient.downloaders;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class CoinPriceRetriever implements IServerRequest{
    private String endpoint = "v1/coins";
    @Override
    public int downloadCache(boolean isPaged, String cacheFilePath) throws MalformedURLException {
        GenericRetriever genericRetriever = new GenericRetriever();
        genericRetriever.setPaged(false);
        genericRetriever.setDomain(serverDomain);
        return genericRetriever.sendRequest(cacheFolder, cacheFilePath, endpoint);
    }
}
