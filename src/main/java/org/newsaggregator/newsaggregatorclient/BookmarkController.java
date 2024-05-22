package org.newsaggregator.newsaggregatorclient;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.newsaggregator.newsaggregatorclient.database.SQLiteJDBC;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.ui_components.datacard.NewsItemCard;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.CategoryTitledPane;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.InfiniteNews;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.NewsCategoryGroupTitledPane;
import org.newsaggregator.newsaggregatorclient.ui_components.uiloader.ArticleItemsLoader;

import java.util.List;

public class BookmarkController {
    @FXML
    VBox bookmarkedGridPane;

    @FXML
    Button reload;

    @FXML
    ScrollPane bookmarkScrollPane;

    HostServices hostServices;
    NewsAggregatorClientController mainController;
    int currentPage = 0;
    int limit = 10;

    public BookmarkController(HostServices hostServices, NewsAggregatorClientController mainController) {
        this.hostServices = hostServices;
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        bookmarkedGridPane.getChildren().clear();
        InfiniteNews newsCategoryGroupTitledPane = new InfiniteNews("");
        bookmarkedGridPane.getChildren().add(newsCategoryGroupTitledPane);
        newsCategoryGroupTitledPane.setPrefWidth(1000);
        Platform.runLater(() -> {
            List<NewsItemData> data = getBookmarkedNews();
            ArticleItemsLoader<CategoryTitledPane> articleItemsLoader = new ArticleItemsLoader<>(10, currentPage * limit, hostServices, newsCategoryGroupTitledPane, mainController);
            articleItemsLoader.loadItems(data);
        });
        reload.setOnAction(e -> {
            currentPage = 0;
            handle(e);
        });
        bookmarkScrollPane.setOnScroll(e -> {
            if (e.getDeltaY() > 0) {
                currentPage--;
            } else {
                currentPage++;
            }
            if (currentPage < 0) {
                currentPage = 0;
            }
            Platform.runLater(() -> {
                List<NewsItemData> data = getBookmarkedNews();
                ArticleItemsLoader<CategoryTitledPane> articleItemsLoader = new ArticleItemsLoader<>(10, currentPage * limit, hostServices, newsCategoryGroupTitledPane, mainController);
                articleItemsLoader.loadItems(data);
            });
        });
    }

    private List<NewsItemData> getBookmarkedNews(){
        SQLiteJDBC db = new SQLiteJDBC();
        List<NewsItemData> data = db.select();
        return data;
    }

    private void handle(ActionEvent e) {
        initialize();
    }
}
