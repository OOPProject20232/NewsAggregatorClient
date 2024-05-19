package org.newsaggregator.newsaggregatorclient.ui_components.dialogs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.newsaggregator.newsaggregatorclient.NewsAggregatorClientApplication;

import java.io.IOException;

public class ReadingArticle extends Dialog<Void> {
    FXMLLoader loader;
    String url;

    @FXML
    WebView articleWebView;

    public ReadingArticle(String url) throws IOException {
        super();
        this.url = url;
        FXMLLoader loader = new FXMLLoader(ReadingArticle.class.getResource("web_view_with_controllers.fxml"));
        loader.setController(this);
        loader.load();
        getDialogPane().setContent(loader.getRoot());
    }

    @FXML
    public void initialize() throws IOException {
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.setTitle("Reading articles");
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        articleWebView.getEngine().load(url);
    }
}
