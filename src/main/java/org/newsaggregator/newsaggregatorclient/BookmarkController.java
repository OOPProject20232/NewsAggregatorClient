package org.newsaggregator.newsaggregatorclient;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import org.newsaggregator.newsaggregatorclient.database.SQLiteJDBC;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.ui_components.datacard.NewsItemCard;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.CategoryTitledPane;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.NewsCategoryGroupTitledPane;
import org.newsaggregator.newsaggregatorclient.ui_components.uiloader.ArticleItemsLoader;

import java.util.List;

public class BookmarkController {
    @FXML
    GridPane bookmarkedGridPane;

    HostServices hostServices;

    public BookmarkController(HostServices hostServices) {

    }

    @FXML
    public void initialize() {
        NewsCategoryGroupTitledPane newsCategoryGroupTitledPane = new NewsCategoryGroupTitledPane("");
        bookmarkedGridPane.getChildren().add(newsCategoryGroupTitledPane);
        newsCategoryGroupTitledPane.setPrefWidth(1000);
        List<NewsItemData> data = getBookmarkedNews();
        ArticleItemsLoader<CategoryTitledPane> articleItemsLoader = new ArticleItemsLoader<>(10, 0, hostServices, newsCategoryGroupTitledPane, null);
        articleItemsLoader.loadItems(data);
    }

    private List<NewsItemData> getBookmarkedNews(){
        SQLiteJDBC db = new SQLiteJDBC();
        List<NewsItemData> data = db.select();
        return data;
    }
}
