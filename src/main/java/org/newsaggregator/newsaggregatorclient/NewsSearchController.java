package org.newsaggregator.newsaggregatorclient;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.json.JSONObject;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.datamodel.RedditPostData;
import org.newsaggregator.newsaggregatorclient.jsonparsing.NewsCategoryJSONLoader;
import org.newsaggregator.newsaggregatorclient.jsonparsing.NewsJSONLoader;
import org.newsaggregator.newsaggregatorclient.jsonparsing.RedditPostJSONLoader;
import org.newsaggregator.newsaggregatorclient.jsonparsing.SearchJSONLoader;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.CategoryTitledPane;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.InfiniteNews;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.RedditGroupTitledPane;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.LoadingDialog;
import org.newsaggregator.newsaggregatorclient.ui_components.uiloader.ArticleItemsLoader;
import org.newsaggregator.newsaggregatorclient.ui_components.uiloader.RedditItemsLoader;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class NewsSearchController{
    /**
     * Controller của tab tìm kiếm tin tức
     */
    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private RadioButton articlesToggleButton;

    @FXML
    private RadioButton redditToggleButton;

    @FXML
    private ComboBox<String> searchFieldComboBox;

    @FXML
    private VBox searchVBox;

    @FXML
    private ScrollPane searchScrollPane;

    @FXML
    private ToggleButton exactSearchToggleButton;

    private String searchType = "articles";
    private String searchOrder = "Newest";
    private String searchField = "all";
    private String isExactOrRegex = "r";
    private int page = 1;
    private int limit = 10;

    private SearchJSONLoader<NewsItemData> searchNewsJSONLoader;
    private SearchJSONLoader<RedditPostData> searchRedditJSONLoader;

    private HostServices hostServices;
    private NewsAggregatorClientController mainController;
    private int chunkSize = 10;
    private AtomicReference<Integer> currentChunk = new AtomicReference<>(limit/chunkSize);


    public void initialize() {
        /**
         * Khởi tạo các giá trị mặc định
         * - Disable nút tìm kiếm khi ô tìm kiếm trống hoặc chỉ chứa khoảng trắng
         * - Xử lý sự kiện tìm kiếm khi nhấn Enter hoặc click vào nút tìm kiếm
         * - Focus vào ô tìm kiếm
         */
        searchButton.setDisable(true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchButton.setDisable(newValue.trim().isEmpty());
        });
        searchTextField.setOnKeyPressed(event -> {
            autocomplete();
        });
        searchTextField.setOnAction(event -> {
            if (!searchTextField.getText().isEmpty())
                search();
        });
        searchButton.setOnAction(event -> search());
        searchTextField.requestFocus();
        searchFieldComboBox.getItems().clear();
        searchFieldComboBox.getItems().addAll("all", "categories");
        searchFieldComboBox.onActionProperty().set(event -> {
            searchField = searchFieldComboBox.getValue();
        });
        searchFieldComboBox.setValue("all");
        articlesToggleButton.getStyleClass().remove("radio-button");
        articlesToggleButton.setOnAction(event -> {
            if (articlesToggleButton.isSelected())
                searchType = "articles";
            System.out.println(searchType);
        });
        redditToggleButton.getStyleClass().remove("radio-button");
//        redditToggleButton.setOnAction(event -> {
//            if (redditToggleButton.isSelected())
//                searchType = "reddit";
//            System.out.println(searchType);
//        });
        redditToggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                searchType = "reddit";
                exactSearchToggleButton.setDisable(true);
                searchFieldComboBox.setValue("all");
                searchFieldComboBox.setDisable(true);
            }
        });
//        exactSearchToggleButton.setOnAction(event -> {
//            if (exactSearchToggleButton.isSelected()){
//                isExactOrRegex = "e";
//                exactSearchToggleButton.setText("✓ Exact search");
//            }
//            else{
//                isExactOrRegex = "r";
//                exactSearchToggleButton.setText("Exact search");
//            }
//            System.out.println(isExactOrRegex);
//        });
        exactSearchToggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                exactSearchToggleButton.setText("✓ Exact search");
                isExactOrRegex = "e";
            }
            else{
                exactSearchToggleButton.setText("Exact search");
                isExactOrRegex = "r";
            }
        });
    }

    private String getSearchQuery(){
        String result = searchTextField.getText();
        if (result == null){
            return "";
        }
        result = result.replace("?", "%3F")
                .replace("\\", "%5C")
                .replace("/", "%2F")
                .replace("=", "%3D")
                .replace("&", "%26")
                .replace(":", "%3A")
                .replace(";", "%3B")
                .replace("#", "%23")
                .replace("%", "%25")
                .replace("+", "%2B")
                .replace("!", "%21")
                .replace("@", "%40")
                .replace("$", "%24")
                .replace("^", "%5E")
                .replace("*", "%2A")
                .replace("(", "%28")
                .replace(")", "%29+")
                .replace("[", "%5B")
                .replace("]", "%5D")
                .replace("{", "%7B")
                .replace("}", "%7D")
                .replace("|", "%7C")
                .replace("'", "%27")
                .replace("\"", "%22")
                .replace("`", "%60")
                .replace("~", "%7E")
                .replace("<", "%3C")
                .replace(">", "%3E")
                .replace(",", "%2C")
                .replace(".", "%2E")
                .replace("_", "%5F")
                .replace("-", "%2D")
                .replace(" ", "%20");

        return result;
    }

    private void search() {
        // Hàm xử lý sự kiện tìm kiếm tin tức
        String isDesc;
        resetPage();
        searchVBox.getChildren().clear();
        String searchText = getSearchQuery();
        System.out.println("Searching for: " + searchText);
        System.out.println("Search field: " + searchField);
        System.out.println("Search Order" + searchOrder);
        System.out.println("Exact search: " + isExactOrRegex);
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.show();
        if (searchType == null) {
            searchType = "articles";
        }
        if (searchOrder == null) {
            searchOrder = "Newest";
        }
        if(searchOrder.equals("Newest")){
            isDesc = "desc";
        }
        else{
            isDesc = "asc";
        }
        if (searchField == null) {
            searchField = "all";
        }
        try {
            if (searchType.equals("articles")) {
                // Load articles
                    InfiniteNews infiniteNews = new InfiniteNews("Search result");
                    infiniteNews.setMaxWidth(1200);
                    searchVBox.getChildren().add(infiniteNews);
                    searchVBox.setAlignment(Pos.CENTER);
                    SearchJSONLoader<NewsItemData> searchJSONLoader = new SearchJSONLoader<>(searchText, searchType, searchField, isDesc, isExactOrRegex);
                    searchJSONLoader.setLimit(limit);
                    searchJSONLoader.setPage(page);
                    JSONObject obj = searchJSONLoader.loadJSON();
                    System.out.println(obj.toMap());
                    int count;
                    try{
                        count = obj.getInt("count");
                    } catch (Exception e){
                        count = 0;
                    }
                    infiniteNews.addOtherItems(
                        new Label("Search results for: " + searchTextField.getText()),
                        new Label("Found " + count + " results")
                    );
                    loadNewsToFrame(infiniteNews, obj, chunkSize, 0);
                    infiniteNews.getLoadMoreButton().setOnAction(event -> loadMoreNews(infiniteNews));
                    searchScrollPane.setOnScroll(event -> {
                        if (searchScrollPane.getVvalue() > .8) {
//                            loadMoreNews(infiniteNews);
                            loadMore(infiniteNews);
                        }
                    });

            } else if (searchType.equals("reddit")) {
                // Load reddit posts
                try {
                    SearchJSONLoader<RedditPostData> searchJSONLoader = new SearchJSONLoader<>(searchText, "reddit", searchField, isDesc, isExactOrRegex);
                    JSONObject obj = searchJSONLoader.loadJSON();
                    RedditGroupTitledPane redditGroupTitledPane = new RedditGroupTitledPane("Search Results");
                    loadRedditToFrame(redditGroupTitledPane, obj);
                    searchVBox.getChildren().add(redditGroupTitledPane);
                    searchScrollPane.setOnScroll(event -> {
                        if (searchScrollPane.getVvalue() > .8){
//                            loadMoreReddit(redditGroupTitledPane);
                            loadMore(redditGroupTitledPane);
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            loadingDialog.close();
        }
        catch (Exception e){
            e.printStackTrace();
            searchVBox.getChildren().clear();
            CategoryTitledPane categoryTitledPane = new CategoryTitledPane("No results found");
            searchVBox.getChildren().add(categoryTitledPane);
            loadingDialog.close();
        }
    }

    private void loadNewsToFrame(InfiniteNews infiniteNews, JSONObject obj, int limit, int begin){
        if (searchField.equals("all")) {
            NewsJSONLoader newsJSONLoader = new NewsJSONLoader();
            newsJSONLoader.setJSONObj(obj);
            List<NewsItemData> list = newsJSONLoader.getDataList(limit, begin);
            ArticleItemsLoader articleItemsLoader = new ArticleItemsLoader(limit, begin, hostServices, infiniteNews, mainController);
            articleItemsLoader.loadItems(list);
            currentChunk.set(currentChunk.get() + 1);
        }
        else{
            NewsCategoryJSONLoader newsJSONLoader = new NewsCategoryJSONLoader();
            newsJSONLoader.setJSONObj(obj);
            List<NewsItemData> list = newsJSONLoader.getDataList(limit, begin);
            System.out.println(list);
            ArticleItemsLoader articleItemsLoader = new ArticleItemsLoader(limit, begin, hostServices, infiniteNews, mainController);
            articleItemsLoader.loadItems(list);
            currentChunk.set(currentChunk.get() + 1);
        }
    }

    private void nextPage(){
        page++;
    }


    private void loadMoreNews(InfiniteNews infiniteNews){
        // Hàm xử lý sự kiện load thêm tin tức
        System.out.println("Loading more news");
//        nextPage();
        if (currentChunk.get() * chunkSize >= limit){
            nextPage();
            currentChunk.set(0);
        }
        if (articlesToggleButton.isSelected())
            Platform.runLater(() -> {
                SearchJSONLoader<NewsItemData> searchJSONLoader = new SearchJSONLoader<>(getSearchQuery(), searchType, searchField, searchOrder, isExactOrRegex);
                searchJSONLoader.setPage(page);
                JSONObject obj = searchJSONLoader.loadJSON();
                loadNewsToFrame(infiniteNews, obj, chunkSize, currentChunk.get() * chunkSize);
                currentChunk.set(currentChunk.get() + 1);
            });
    }

    private void loadMoreReddit(RedditGroupTitledPane redditGroupTitledPane){
        Platform.runLater(() -> {
            SearchJSONLoader<RedditPostData> searchJSONLoader = new SearchJSONLoader<>(getSearchQuery(), "posts", searchField, searchOrder, isExactOrRegex);
            searchJSONLoader.setPage(page);
            JSONObject obj = searchJSONLoader.loadJSON();
            loadRedditToFrame(redditGroupTitledPane, obj);
        });
    }

    private void loadRedditToFrame(RedditGroupTitledPane redditGroupTitledPane, JSONObject obj){
        RedditPostJSONLoader redditPostJSONLoader = new RedditPostJSONLoader();
        List<RedditPostData> list = redditPostJSONLoader.getDataList(10, 0);
        RedditItemsLoader redditItemsLoader = new RedditItemsLoader(10, 0, hostServices, redditGroupTitledPane);
        redditItemsLoader.loadItems(list);
    }

    private<T extends CategoryTitledPane> void loadMore(T frame){
        if (frame instanceof InfiniteNews){
            loadMoreNews((InfiniteNews) frame);
        } else if (frame instanceof RedditGroupTitledPane) {
            loadMoreReddit((RedditGroupTitledPane) frame);

        }
    }

    private void autocomplete() {
        // Hàm xử lý sự kiện autocomplete
        System.out.println("Autocompleting for: " + searchTextField.getText());
//        if (!searchTextField.getText().isEmpty()){
//            try {
//                JSONObject obj = DataReaderFromIS.fetchJSONWithCache(
//                        "https://newsaggregator-mern.onrender.com/api/v1/articles/autocomplete/" + searchTextField.getText(),
//                        "%s.json".formatted(searchTextField.getText())
//                );
//
//            } catch (MalformedURLException e) {
//                throw new RuntimeException(e);
//            } catch (NoRouteToHostException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }

    public void insertSearchText(String searchKeyword, String searchType, String searchOrder, String searchField, String isExactOrRegex){
        searchTextField.setText(searchKeyword);
        this.searchType = searchType;
        this.isExactOrRegex = isExactOrRegex;
        this.searchOrder = searchOrder;
        this.searchField = searchField;
        this.searchFieldComboBox.setValue(searchField);
        if (searchType.equals("articles")){
            articlesToggleButton.setSelected(true);
        }
        else if (searchType.equals("reddit")){
            redditToggleButton.setSelected(true);
        }
        if (isExactOrRegex.equals("e")){
            exactSearchToggleButton.setSelected(true);
        }
        search();
    }


    public HostServices getHostServices() {
        return hostServices;
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    public void setMainController(NewsAggregatorClientController mainController) {
        this.mainController = mainController;
    }

    public void resetPage(){
        page = 1;
    }
}
