package org.newsaggregator.newsaggregatorclient.ui_components.dialogs;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.newsaggregator.newsaggregatorclient.NewsAggregatorClientApplication;

import java.io.IOException;
import java.net.*;

public class ReadingArticle extends GenericDialog {
    FXMLLoader loader;
    String url;
    HostServices hostServices;

    @FXML
    WebView articleWebView;

    @FXML
    Button reloadButton;

    @FXML
    Label addressBar;

    @FXML
    Button copyLinkButton;

    @FXML
    Button externalButton;

    public ReadingArticle(String url, HostServices hostServices) throws IOException {
        super();
        this.url = url;
        this.hostServices = hostServices;
        setResizable(true);
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        FXMLLoader loader = new FXMLLoader(NewsAggregatorClientApplication.class.getResource("web_view_with_controllers.fxml"));
        loader.setController(this);
        loader.load();
        getDialogPane().setContent(loader.getRoot());
        reloadButton.setOnAction(e -> reload());
    }

    @FXML
    public void initialize() throws IOException {
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.setTitle("Reading articles");
        Platform.runLater(this::loadArticle);
        addressBar.setText(url);
        Tooltip copyLinkTooltip = new Tooltip("Copy link to clipboard");
        copyLinkTooltip.setShowDelay(Duration.millis(10));
        ImageView copyLinkImage = new ImageView("file:src/main/resources/org/newsaggregator/newsaggregatorclient/assets/images/link.png");
        copyLinkImage.setFitWidth(16);
        copyLinkImage.setFitHeight(16);
        copyLinkButton.setTooltip(copyLinkTooltip);
        copyLinkButton.setOnMouseClicked(e -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(url);
            clipboard.setContent(content);
            copyLinkButton.setText("Copied!");
            copyLinkButton.setGraphic(null);
        });
        copyLinkButton.setOnMouseExited(e -> {
            copyLinkButton.setGraphic(copyLinkImage);
            copyLinkButton.setText("");
            copyLinkTooltip.hide();
        });
        Tooltip externalButtonTooltip = new Tooltip("Open in browser");
        externalButtonTooltip.setShowDelay(Duration.millis(10));
        externalButton.setTooltip(externalButtonTooltip);
        externalButton.setOnAction(e -> {
            hostServices.showDocument(url);
        });
    }

    private void loadArticle(){
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
            doc.select("a").attr("href", "#");
            String title = doc.select("title").getFirst().text();
            Stage stage = (Stage) getDialogPane().getScene().getWindow();
            stage.setTitle(title);
            stage.getIcons().add(new Image("https://logo.clearbit.com/%s".formatted(URI.create(url).getHost())));
        } catch (HttpStatusException e) {
            doc = Jsoup.parse(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Block all redirections
        articleWebView.getEngine().loadContent(doc.toString());
        System.out.println(articleWebView.getEngine().getUserAgent());
    }

    private void reload(){
        ((Stage)getDialogPane().getScene().getWindow()).setTitle("Reloading...");
        LoadingDialog dialog = new LoadingDialog();
        dialog.show();
        loadArticle();
        dialog.close();
    }
}
