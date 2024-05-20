package org.newsaggregator.newsaggregatorclient.ui_components.dialogs;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.newsaggregator.newsaggregatorclient.NewsAggregatorClientApplication;

import java.io.IOException;
import java.net.*;

public class ReadingArticle extends GenericDialog {
    FXMLLoader loader;
    String url;

    @FXML
    WebView articleWebView;

    @FXML
    Button reloadButton;

    public ReadingArticle(String url) throws IOException {
        super();
        this.url = url;
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
    }

    private void loadArticle(){
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Block all redirections
        doc.select("a").attr("href", "#");
        String title = doc.select("title").getFirst().text();
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.setTitle(title);
        stage.getIcons().add(new Image("https://logo.clearbit.com/%s".formatted(URI.create(url).getHost())));
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
