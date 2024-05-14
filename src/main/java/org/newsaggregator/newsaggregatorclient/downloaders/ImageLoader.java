package org.newsaggregator.newsaggregatorclient.downloaders;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

public class ImageLoader {
    private static String noImageAvailablePath = "file:src/main/resources/org/newsaggregator/newsaggregatorclient/assets/images/no-image-available.png";

    public static synchronized Image downloadImage(String url, String cacheFile) throws MalformedURLException, NoRouteToHostException {
        Image image = new Image(noImageAvailablePath);
        Path folder = Paths.get("src/main/resources/tmp/img/");
        if (!folder.toFile().exists()) {
            folder.toFile().mkdirs();
        }
        Path path = Paths.get(cacheFile);
        String ext = FilenameUtils.getExtension(cacheFile);
        if (path.toFile().exists()) {
            if (ext.equals("webp")) {
                try {
                    BufferedImage bufferedImage = ImageIO.read(path.toFile());
                    image = SwingFXUtils.toFXImage(bufferedImage, null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return image;
            }
            else {
                image = new Image(path.toUri().toString());
                return image;
            }
        }
        else {
            try {
                if (cacheFile.endsWith(".webp")) {
                    System.out.println("\u001B[32m" + "Processing webp image: " + url + "\u001B[0m");
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestMethod("GET");
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buffer = new byte[65534];
                    int length;
                    while ((length = in.read()) > 0) {
                        out.write(buffer, 0, length);
                    }
                    in.close();
                    byte[] bytes = out.toByteArray();
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                    ImageInputStream imageInputStream = ImageIO.createImageInputStream(byteArrayInputStream);
                    BufferedImage bufferedImage = ImageIO.read(imageInputStream);
                    try {
                        image = SwingFXUtils.toFXImage(bufferedImage, null);
                    } catch (Exception ex) {
                        image = new Image(noImageAvailablePath, true);
                        System.out.println("\u001B[31m" + "Error processing webp image: " + ex.getMessage() + "\u001B[0m");
                    }
                } else {
                    image = new Image(url, true);
                }
            } catch (Exception ex) {
                image = new Image(noImageAvailablePath, true);
                System.out.println("\u001B[31m" + "Error processing webp image: " + ex.getMessage() + "\u001B[0m");
            }
            System.out.println("\u001B[32m" + "Downloading image: " + url + "\u001B[0m");
            System.out.println("\u001B[32m" + "Saving image to: " + path.toString() + "\u001B[0m");
            DataReaderFromIS.fetchFileWithCache(url, path.toString(), folder.toString());
        }
        return image;
    }
}
