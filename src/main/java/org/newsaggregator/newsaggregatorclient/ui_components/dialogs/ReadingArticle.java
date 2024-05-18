package org.newsaggregator.newsaggregatorclient.ui_components.dialogs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.newsaggregator.newsaggregatorclient.NewsAggregatorClientApplication;

import java.io.IOException;

public class ReadingArticle extends Dialog<Void> {
    @FXML
    WebView articleWebView;

    public ReadingArticle(String url) throws IOException {
        super();
        Stage stage = (Stage) getDialogPane().getScene().getWindow();
        stage.setTitle("Reading articles");
        FXMLLoader loader = new FXMLLoader(NewsAggregatorClientApplication.class.getResource("web_view_with_controllers.fxml"));
        getDialogPane().setContent(loader.load());
        loader.setController(this);
        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        articleWebView.getEngine().load(url);
    }
}
