package org.newsaggregator.newsaggregatorclient.downloaders;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;

public class FileDownloader {
    /**
     * Class này chứa các hàm để download file từ mạng
     * Hiện tại chưa cần dùng đến
     */

    public static void fileDownloader(String urlString, String fileName) {
        try {
            URL url = URI.create(urlString).toURL();
            ReadableByteChannel rbc = java.nio.channels.Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("src/main/resources/tmp/img" + fileName);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
//            InputStream in = url.openStream();
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int n;
//
//            while ((n = in.read(buffer)) > 0) {
//                out.write(buffer, 0, n);
//            }
//
//            out.close();
//            in.close();
//
//            byte[] imageBytes = out.toByteArray();
//
//            // Write downloaded bytes to a file
//            File tmpOutputFile = new File("src/main/resources/tmp/img" + fileName);
//            FileOutputStream fos = new FileOutputStream(tmpOutputFile.getAbsoluteFile());
//            fos.write(imageBytes);
//            fos.close();

            System.out.println("WebP image downloaded successfully!");
        } catch (Exception e) {
            System.out.println("Error downloading file: " + e.getMessage());
        }
    }
}
