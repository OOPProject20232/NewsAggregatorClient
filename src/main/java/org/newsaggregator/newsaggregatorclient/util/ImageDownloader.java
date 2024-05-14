package org.newsaggregator.newsaggregatorclient.util;

import java.io.FileOutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;

@Deprecated
public class ImageDownloader {
    /**
     * Class này chứa các hàm để download file từ mạng
     */

    public synchronized static void downloadImage(String urlString, String fileName) {
        try {
            URL url = URI.create(urlString).toURL();
            ReadableByteChannel rbc = java.nio.channels.Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("src/main/resources/tmp/img" + fileName);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            System.out.println("Image downloaded successfully!");
        } catch (Exception e) {
            System.out.println("Error downloading file: " + e.getMessage());
        }
    }
}
