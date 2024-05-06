package org.newsaggregator.newsaggregatorclient.util;

import java.io.File;

public class CreateJSONCache {
    public static void createFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        else {
            System.out.println("Folder already exists");
        }
    }
}

