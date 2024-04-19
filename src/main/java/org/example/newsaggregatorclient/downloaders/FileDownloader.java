package org.example.newsaggregatorclient.downloaders;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class FileDownloader {
    /**
     * Class này chứa các hàm để download file từ mạng
     * Hiện tại chưa cần dùng đến
     */

    public static void fileDownloader(String urlString, String fileName) {
        try {
            URL url = URI.create(urlString).toURL();
            InputStream in = url.openStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int n;

            while ((n = in.read(buffer)) > 0) {
                out.write(buffer, 0, n);
            }

            out.close();
            in.close();

            byte[] imageBytes = out.toByteArray();

            // Write downloaded bytes to a file
            FileOutputStream fos = new FileOutputStream(fileName + ".webp");
            fos.write(imageBytes);
            fos.close();

            System.out.println("WebP image downloaded successfully!");
        } catch (Exception e) {
            System.out.println("Error downloading file: " + e.getMessage());
        }
    }
}
