package org.newsaggregator.newsaggregatorclient.ui_components.newsscrollableframe;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.newsaggregator.newsaggregatorclient.NewsAggregatorClientController;
import org.newsaggregator.newsaggregatorclient.datamodel.RedditPostData;
import org.newsaggregator.newsaggregatorclient.jsonparsing.RedditPostJSONLoader;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.RedditGroupTitledPane;
import org.newsaggregator.newsaggregatorclient.ui_components.uiloader.RedditItemsLoader;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>Khung scrollpane dùng để load và đọc tin Reddit,
 * được tách ra từ controller chính để đảm bảo code dễ đọc</p>
 * Gọi loadReddit() để đọc tin
 */
public class RedditFrame extends GenericFrame {
    private final NewsAggregatorClientController mainController;
    private final RedditGroupTitledPane allReddit = new RedditGroupTitledPane("All Reddit");
    private final BooleanProperty isLoaded = new SimpleBooleanProperty(false);

    public RedditFrame(HostServices hostServices, NewsAggregatorClientController mainController){
        this.hostServices = hostServices;
        this.mainController = mainController;
        this.itemsContainer.setAlignment(javafx.geometry.Pos.CENTER);
        setOnScroll(event -> {
            if (getVvalue() > 0.8) {
                loadMoreReddit();
            }
        });
    }

    public void loadReddit(){
//        allReddit.getLoadMoreButton().setOnAction(event -> loadMoreReddit());
        itemsContainer.getChildren().clear();
        itemsContainer.add(allReddit, 0, 0, 2, 2);
        insertToFrame();
        isLoaded.set(true);
    }

    private void insertToFrame() {
        AtomicReference<RedditPostJSONLoader> redditDataLoader = new AtomicReference<>();
        Platform.runLater(() -> {
            redditDataLoader.set(getRedditJSONLoader());
            if (redditDataLoader.get().getJSONString().isEmpty()) {
                System.out.println("Data is empty");
            }
            else {
                List<RedditPostData> data = redditDataLoader.get().getDataList(20, 0);
                new Thread(() -> Platform.runLater(() -> {
                    RedditItemsLoader redditItemsLoader = new RedditItemsLoader(20, 0, hostServices, allReddit);
                    redditItemsLoader.loadItems(data);
                })).start();
            }
        });
    }

    private void loadMoreReddit(){
        nextPage();
        insertToFrame();
    }

    private RedditPostJSONLoader getRedditJSONLoader(){
        RedditPostJSONLoader redditPostJSONLoader = new RedditPostJSONLoader();
        int limit = 50;
        redditPostJSONLoader.setLimit(limit);
        redditPostJSONLoader.setPageNumber(currentPage);
        redditPostJSONLoader.loadJSON();
        return redditPostJSONLoader;
    }

    public boolean isLoaded(){
        return isLoaded.get();
    }
}
