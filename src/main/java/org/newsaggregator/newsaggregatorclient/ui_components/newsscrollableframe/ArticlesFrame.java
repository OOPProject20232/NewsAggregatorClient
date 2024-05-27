package org.newsaggregator.newsaggregatorclient.ui_components.newsscrollableframe;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.NotNull;
import org.newsaggregator.newsaggregatorclient.NewsAggregatorClientController;
import org.newsaggregator.newsaggregatorclient.datamodel.NewsItemData;
import org.newsaggregator.newsaggregatorclient.jsonparsing.NewsCategoryJSONLoader;
import org.newsaggregator.newsaggregatorclient.jsonparsing.NewsJSONLoader;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.InfiniteNews;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.NewsCategoryGroupTitledPane;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.LoadingDialog;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.NoInternetDialog;
import org.newsaggregator.newsaggregatorclient.ui_components.uiloader.ArticleItemsLoader;

import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ArticlesFrame extends GenericFrame {
    private final NewsAggregatorClientController mainController;
    private final int limit = 15;
    private final int chunkSize = 5;
    private static int totalPages;
    private final AtomicReference<Integer> currentChunk = new AtomicReference<>(limit / chunkSize);


//    private final GridPane newsContainer = new GridPane();
    InfiniteNews allNews;

    public ArticlesFrame (HostServices hostServices, NewsAggregatorClientController mainController) {
        super();
        resetPage();
        this.hostServices = hostServices;
        this.mainController = mainController;
        this.setFitToHeight(true);
        this.setPannable(true);
    }

    /**
     * Phần load chính các bài viết
     */
    public void loadArticles() throws NoRouteToHostException, UnknownHostException {
        System.out.println("Loading articles");
        NewsCategoryGroupTitledPane latestNews = new NewsCategoryGroupTitledPane("Latest news");
        NewsCategoryGroupTitledPane bitcoinNews = new NewsCategoryGroupTitledPane("Bitcoin news");
        NewsCategoryGroupTitledPane ethereumNews = new NewsCategoryGroupTitledPane("Ethereum news");
        NewsCategoryGroupTitledPane solanaNews = new NewsCategoryGroupTitledPane("Solana news");
        NewsCategoryGroupTitledPane defiNews = new NewsCategoryGroupTitledPane("DeFI news");
        NewsCategoryGroupTitledPane web3News = new NewsCategoryGroupTitledPane("Web3 news");
        NewsCategoryGroupTitledPane blockchainNews = new NewsCategoryGroupTitledPane("Blockchain news");
        itemsContainer.add(latestNews, 0, 0, 3, 1);
        itemsContainer.add(bitcoinNews, 0, 2, 1, 1);
        itemsContainer.add(ethereumNews, 1, 2, 1, 1);
        itemsContainer.add(solanaNews, 0, 5, 1, 1);
        itemsContainer.add(defiNews, 1, 5, 1, 1);
        itemsContainer.add(web3News, 0, 8, 1, 1);
        itemsContainer.add(blockchainNews, 1, 8, 1, 1);
//        NewsCategoryGroupTitledPane[] newsCategoryGroupTitledPanes = {bitcoinNews, ethereumNews, solanaNews, defiNews, web3News, blockchainNews};
        allNews = new InfiniteNews("All news");
        allNews.getLoadMoreButton().setOnAction(event -> loadMoreArticles());
        itemsContainer.add(allNews, 0, 15, 3, 2);
        AtomicReference<NewsJSONLoader> articleDataLoader = new AtomicReference<>(new NewsJSONLoader());
        Task<Void> task = new Task<>() {
            @Override
            protected void updateProgress(long workDone, long max) {
                super.updateProgress(workDone, max);
                super.updateMessage("Loading %s/%s".formatted(workDone, max));
            }

            @Override
            protected Void call() throws Exception {
                articleDataLoader.set(getNewsJSONLoader());
                AtomicReference<List<NewsItemData>> datalist = new AtomicReference<>();
                new Thread(() -> {
                    articleDataLoader.get().loadJSON();
                    datalist.set(articleDataLoader.get().getDataList(limit, 0));
                }).start();
                Platform.runLater(() -> {
                            new ArticleItemsLoader(
                                    limit,
                                    0,
                                    hostServices,
                                    latestNews,
                                    mainController).loadItems(
                                    datalist.get()
                            );
                            updateProgress(1, 8);
                        }
                );
                AtomicReference<NewsCategoryJSONLoader> newsLoaderBTC = new AtomicReference<>();
                new Thread(() -> newsLoaderBTC.set(getNewsCategoryJSONLoader("bitcoin"))).start();
                Platform.runLater(() -> {
                    getNewsCategory(bitcoinNews, newsLoaderBTC.get());
                    updateProgress(2, 8);
                });
                AtomicReference<NewsCategoryJSONLoader> newsLoaderETH = new AtomicReference<>();
                new Thread(() -> newsLoaderETH.set(getNewsCategoryJSONLoader("ethereum"))).start();
                Platform.runLater(() ->
                {
                    getNewsCategory(ethereumNews, newsLoaderETH.get());
                    updateProgress(3, 8);
                });
                AtomicReference<NewsCategoryJSONLoader> newsLoaderSOL = new AtomicReference<>();
                new Thread(() -> newsLoaderSOL.set(getNewsCategoryJSONLoader("solana"))).start();
                Platform.runLater(() -> {
                    getNewsCategory(solanaNews, newsLoaderSOL.get());
                    updateProgress(4, 8);
                });
                AtomicReference<NewsCategoryJSONLoader> newsLoaderDeFi = new AtomicReference<>();
                new Thread(() -> {
                    newsLoaderDeFi.set(getNewsCategoryJSONLoader("defi"));
                }).start();
                Platform.runLater(() -> {
                    getNewsCategory(defiNews, newsLoaderDeFi.get());
                    updateProgress(5, 8);
                });
                AtomicReference<NewsCategoryJSONLoader> newsLoaderWeb3 = new AtomicReference<>();
                new Thread(() -> {
                    newsLoaderWeb3.set(getNewsCategoryJSONLoader("web3"));
                }).start();
                Platform.runLater(() -> {
                    getNewsCategory(web3News, newsLoaderWeb3.get());
                    updateProgress(6, 8);
                });
                AtomicReference<NewsCategoryJSONLoader> newsLoaderBlockchain = new AtomicReference<>();
                new Thread(() -> {
                    newsLoaderBlockchain.set(getNewsCategoryJSONLoader("blockchain"));
                }).start();
                Platform.runLater(() -> {
                    getNewsCategory(blockchainNews, newsLoaderBlockchain.get());
                    updateProgress(7, 8);
                });
                Platform.runLater(() -> {
                            new ArticleItemsLoader(
                                    limit,
                                    0,
                                    hostServices,
                                    allNews,
                                    mainController).loadItems(
                                    datalist.get()
                            );
                            updateProgress(8, 8);
                        }
                );
                totalPages = articleDataLoader.get().getTotalPages();
                return null;
            }
        };
        Alert alert = createLoadingAlert(task);
        alert.show();
        task.run();
        task.setOnSucceeded(event -> alert.close());
        setOnScroll(event -> {
            if (getVvalue() > .8) {
                loadMoreArticles();
            }
        });
    }

    /**
     * Lấy dữ liệu bài viết từ một category
     * @param pane NewsCategoryGroupTitledPane: Pane chứa bài viết
     * @param newsCategoryJSONLoader NewsCategoryJSONLoader: Loader chứa dữ liệu bài viết
     */
    private synchronized void getNewsCategory(NewsCategoryGroupTitledPane pane, NewsCategoryJSONLoader newsCategoryJSONLoader) {
        try{
            List<NewsItemData> data = newsCategoryJSONLoader.getDataList(5, 0);
            System.out.println("Data size: " + data.size());
                ArticleItemsLoader articleItemsLoader = new ArticleItemsLoader(5, 0, hostServices, pane, mainController);
                articleItemsLoader.setContainingSummary(false);
                articleItemsLoader.setContainingCategories(false);
            Platform.runLater(() -> {
                articleItemsLoader.loadItems(data);
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Lấy dữ liệu bài viết từ trang hiện tại
     * @return NewsJSONLoader: Loader chứa dữ liệu bài viết
     * @throws NoRouteToHostException
     */
    private synchronized @NotNull NewsJSONLoader getNewsJSONLoader() throws NoRouteToHostException {
        NewsJSONLoader articleDataLoader = new NewsJSONLoader();
        articleDataLoader.setPageNumber(currentPage);
        articleDataLoader.setLimit(limit);
        articleDataLoader.loadJSON();
        return articleDataLoader;
    }

    /**
     * Lấy dữ liệu bài viết từ một category
     * @param category String: Tên category
     * @return NewsCategoryJSONLoader: Loader chứa dữ liệu bài viết
     */
    private NewsCategoryJSONLoader getNewsCategoryJSONLoader(String category) {
        NewsCategoryJSONLoader articleDataLoader = new NewsCategoryJSONLoader();
        articleDataLoader.setCategory(category);
        try {
            articleDataLoader.loadJSON();
        }
        catch (NoRouteToHostException e){
            NoInternetDialog noInternetDialog = new NoInternetDialog();
        }
        return articleDataLoader;
    }

    /**
     * Xoá toàn bộ bài viết
     */
    public void clearAllNews() {
        itemsContainer.getChildren().clear();
    }

    /**
     * Tải thêm bài viết từ đoạn tiếp theo, nếu không còn bài viết nào thì chuyển sang trang tiếp theo và tải thêm
     * bài viết từ trang đó
     * @throws NoRouteToHostException
     */
    private synchronized void loadMoreArticles(){
        if (currentChunk.get() * chunkSize >= limit) {
            nextPage();
            currentChunk.set(0);
            if (currentPage > totalPages) {
                previousPage();
                return;
            }
        }
        System.out.println("Current article page: " + currentPage);
        System.out.println("Current chunk: " + currentChunk);
        System.out.println("Current chunk size: " + chunkSize);
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.show();
        System.out.println("Loading more articles");
        try {
            System.out.println("Current article page: " + currentPage);
            NewsJSONLoader articleDataLoader = getNewsJSONLoader();
            Platform.runLater(() -> {
                List<NewsItemData> data = articleDataLoader.getDataList(limit, 0);
                ArticleItemsLoader articleItemsLoader = new ArticleItemsLoader(chunkSize, currentChunk.get() * chunkSize, hostServices, allNews, mainController);
                articleItemsLoader.loadItems(data);
            });
            currentChunk.set(currentChunk.get() + 1);
            loadingDialog.close();
        } catch (ArrayIndexOutOfBoundsException | NoRouteToHostException e){
            // Error, return to old page number
            e.printStackTrace();
            previousPage();
        }
//        mainController.loadingIconOff();
    }

    public void setSearchText(String text){
        mainController.setSearchText(text);
    }

    private Alert createLoadingAlert(Task<?> task) {
        Alert alert = new Alert(Alert.AlertType.NONE);
//        alert.initOwner(owner);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.initStyle(StageStyle.UTILITY);
        alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alert.getDialogPane().lookupButton(ButtonType.OK)
                .disableProperty().bind(task.runningProperty());

        alert.getDialogPane().setHeaderText("Loading...");
        alert.contentTextProperty().bind(task.messageProperty());
        return alert;

    }

}
