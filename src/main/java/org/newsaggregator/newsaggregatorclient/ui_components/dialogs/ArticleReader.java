package org.newsaggregator.newsaggregatorclient.ui_components.dialogs;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
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
import org.newsaggregator.newsaggregatorclient.database.SQLiteJDBC;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;

import java.io.IOException;
import java.net.*;

public class ArticleReader extends Dialog<Void> {
    private final String url;
    private final HostServices hostServices;

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

    @FXML
    ToggleButton bookmarkButton;

    private NewsItemData newsItemData;

    public ArticleReader(String url, HostServices hostServices) throws IOException {
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

    public ArticleReader(NewsItemData newsItemData, HostServices hostServices) throws IOException {
        this(newsItemData.getUrl(), hostServices);
        this.newsItemData = newsItemData;
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
        if (hostServices == null && externalButton != null){
            externalButton.setDisable(true);
        }
        if (newsItemData == null && bookmarkButton != null){
            bookmarkButton.setDisable(true);
        }
        else {
            assert bookmarkButton != null;
            bookmarkButton.setDisable(false);
            bookmarkButton.setOnAction(e -> {
                if (bookmarkButton.isSelected()) {
                    bookmark();
                } else {
                    removeBookmark();
                }
            });
        }
        this.getDialogPane().addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().toString().equals("ESCAPE")) {
                stage.close();
            }
        });
        SQLiteJDBC db = new SQLiteJDBC();
        if (newsItemData != null && db.isBookmarked(newsItemData)){
            bookmarkButton.setSelected(true);
            ImageView bookmarkedIcon = new ImageView("file:src/main/resources/org/newsaggregator/newsaggregatorclient/assets/images/bookmark-selected.png");
            bookmarkedIcon.setFitHeight(16);
            bookmarkedIcon.setFitWidth(16);
            bookmarkButton.setGraphic(bookmarkedIcon);
        }
    }

    private void loadArticle(){
        Document doc = null;
        try {
            doc = Jsoup.connect(url)
                       .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36 Edg/126.0.0.0")
                       .timeout(5000)
                       .get();
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

    private void bookmark(){
        SQLiteJDBC db = new SQLiteJDBC();
        db.insert(newsItemData);
        ImageView bookmarkedIcon = new ImageView("file:src/main/resources/org/newsaggregator/newsaggregatorclient/assets/images/bookmark-selected.png");
        bookmarkedIcon.setFitHeight(16);
        bookmarkedIcon.setFitWidth(16);
        bookmarkButton.setGraphic(bookmarkedIcon);
    }

    private void removeBookmark(){
        SQLiteJDBC db = new SQLiteJDBC();
        db.delete(newsItemData.getGuid());
        ImageView bookmarkIcon = new ImageView("file:src/main/resources/org/newsaggregator/newsaggregatorclient/assets/images/bookmark.png");
        bookmarkIcon.setFitHeight(16);
        bookmarkIcon.setFitWidth(16);
        bookmarkButton.setGraphic(bookmarkIcon);
    }

    public ToggleButton getBookmarkButton() {
        return bookmarkButton;
    }
}
