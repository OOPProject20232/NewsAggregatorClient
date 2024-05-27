package org.newsaggregator.newsaggregatorclient;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    @FXML
    TextField searchBar;

//    @FXML
//    ComboBox<String> sortByComboBox;

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
        SearchService searchService = new SearchService();
        searchService.reset();
        bookmarkedGridPane.getChildren().clear();
        InfiniteNews newsCategoryGroupTitledPane = new InfiniteNews("");
        bookmarkedGridPane.getChildren().add(newsCategoryGroupTitledPane);
        newsCategoryGroupTitledPane.setPrefWidth(1000);
        Platform.runLater(() -> {
            List<NewsItemData> data = getBookmarkedNews();
            ArticleItemsLoader articleItemsLoader = new ArticleItemsLoader(10, currentPage * limit, hostServices, newsCategoryGroupTitledPane, mainController);
            articleItemsLoader.loadItems(data);
        });
        reload.setOnAction(e -> {
            currentPage = 0;
            searchBar.clear();
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
                ArticleItemsLoader articleItemsLoader = new ArticleItemsLoader(10, currentPage * limit, hostServices, newsCategoryGroupTitledPane, mainController);
                articleItemsLoader.loadItems(data);
            });
        });
        searchBar.setOnKeyPressed(e -> Platform.runLater(searchService::search));
//        sortByComboBox.getItems().clear();
//        sortByComboBox.getItems().addAll("Title", "Publisher", "Date");
    }

    private List<NewsItemData> getBookmarkedNews(){
        SQLiteJDBC db = new SQLiteJDBC();
        List<NewsItemData> data = db.select();
        return data.reversed();
    }

    private void handle(ActionEvent e) {
        initialize();
    }

    private class SearchService extends Service<Void>{

        @Override
        protected Task<Void> createTask() {
            return new Task<>() {
                @Override
                protected Void call() throws Exception {
                    search();
                    return null;
                }
            };
        }

        private void search(){
            String searchText = searchBar.getText();
            if (!searchText.equals("")) {
                SQLiteJDBC db = new SQLiteJDBC();
                List<NewsItemData> resultList = db.search(searchText);
                bookmarkedGridPane.getChildren().clear();
                InfiniteNews pane = new InfiniteNews("Search: ");
                bookmarkedGridPane.getChildren().add(pane);
                currentPage = 0;
                ArticleItemsLoader itemsLoader = new ArticleItemsLoader(
                        limit, currentPage * limit, hostServices, pane, mainController
                );
                itemsLoader.loadItems(resultList);
                System.out.println(resultList.toArray().length);
                bookmarkScrollPane.setOnScroll(e -> {
                    ArticleItemsLoader articleItemsLoader = new ArticleItemsLoader(10, currentPage * limit, hostServices, pane, mainController);
                    articleItemsLoader.loadItems(resultList);
                });
            }
            else initialize();
        }
    }
}
