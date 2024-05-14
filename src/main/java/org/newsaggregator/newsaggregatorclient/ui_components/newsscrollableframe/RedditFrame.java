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

public class RedditFrame extends GenericFrame {
    private HostServices hostServices;
    private NewsAggregatorClientController mainController;
    private final int limit = 50;
    RedditGroupTitledPane allReddit = new RedditGroupTitledPane("All Reddit");
    BooleanProperty isLoaded = new SimpleBooleanProperty(false);

    public RedditFrame(HostServices hostServices, NewsAggregatorClientController mainController){
        this.hostServices = hostServices;
        this.mainController = mainController;
        this.getItemsContainer().setAlignment(javafx.geometry.Pos.CENTER);
    }

    public void loadReddit(){
//        allReddit.getLoadMoreButton().setOnAction(event -> loadMoreReddit());
        itemsContainer.getChildren().clear();
        itemsContainer.add(allReddit, 0, 0, 2, 2);
        AtomicReference<RedditPostJSONLoader> redditDataLoader = new AtomicReference<>();
        Platform.runLater(() -> {
            redditDataLoader.set(getRedditJSONLoader());
            if (redditDataLoader.get().getJSONString().isEmpty()) {
                System.out.println("Data is empty");
            }
            else {
                List<RedditPostData> data = redditDataLoader.get().getRedditPostsList();
                new Thread(() -> Platform.runLater(() -> {
                    RedditItemsLoader redditItemsLoader = new RedditItemsLoader(20, 0, hostServices, allReddit);
                    redditItemsLoader.loadItems(data);
                })).start();
            }
        });
        isLoaded.set(true);
    }

    public void loadMoreReddit(){
        nextPage();
        AtomicReference<RedditPostJSONLoader> redditDataLoader = new AtomicReference<>();
        Platform.runLater(() -> {
            redditDataLoader.set(getRedditJSONLoader());
            if (redditDataLoader.get().getJSONString().isEmpty()) {
                System.out.println("Data is empty");
            }
            else {
                List<RedditPostData> data = redditDataLoader.get().getRedditPostsList();
                new Thread(() -> Platform.runLater(() -> {
                    RedditItemsLoader redditItemsLoader = new RedditItemsLoader(20, 0, hostServices, allReddit);
                    redditItemsLoader.loadItems(data);
                })).start();
            }
        });
    }

    private RedditPostJSONLoader getRedditJSONLoader(){
        RedditPostJSONLoader redditPostJSONLoader = new RedditPostJSONLoader();
        redditPostJSONLoader.setLimit(limit);
        redditPostJSONLoader.setPageNumber(currentPage);
        redditPostJSONLoader.loadJSON();
        return redditPostJSONLoader;
    }

    public boolean isLoaded(){
        return isLoaded.get();
    }
}
