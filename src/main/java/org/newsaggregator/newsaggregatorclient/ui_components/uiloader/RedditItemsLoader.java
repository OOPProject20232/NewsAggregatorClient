package org.newsaggregator.newsaggregatorclient.ui_components.uiloader;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.newsaggregator.newsaggregatorclient.datamodel.RedditPostData;
import org.newsaggregator.newsaggregatorclient.ui_components.datacard.RedditCard;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.CategoryTitledPane;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.RedditGroupTitledPane;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.ImageViewDialog;

import java.util.List;

public class RedditItemsLoader extends Task<Void> implements ItemsLoader<RedditPostData>{
    private int limit;
    private int begin;
    private final CategoryTitledPane<RedditCard, RedditPostData> container;
    private HostServices hostServices;

    public RedditItemsLoader(int limit, int begin, HostServices hostServices, CategoryTitledPane<RedditCard, RedditPostData> container){
        this.limit = limit;
        this.begin = begin;
        this.container = (CategoryTitledPane<RedditCard, RedditPostData>) container;
        this.hostServices = hostServices;
    }

    @Override
    public void loadItems(List<RedditPostData> data) {
        if (begin + limit > data.size()) {
            limit = data.size() - begin;
        }
        for (int i = begin; i < begin + limit; i++) {
            RedditPostData redditPostData = data.get(i);
            RedditCard redditCard = new RedditCard(redditPostData);
            redditCard.getTitle().setOnAction(e -> hostServices.showDocument(redditPostData.getUrl()));
            redditCard.getAuthor().setOnAction(e -> hostServices.showDocument(redditPostData.getLinkToAuthor()));
            redditCard.getSubreddit().setOnAction(e -> hostServices.showDocument(redditPostData.getLinkToSub()));
            redditCard.getImageViewLink().setOnAction(e -> {
                try {
                    new ImageViewDialog(redditPostData.getMediaUrl()).show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            System.out.println(redditPostData.getTitle());
            Platform.runLater(() -> {
                redditCard.setText();
                redditCard.setImage();
            });
            container.addItem(redditCard);
        }
    }

    public void setBegin(int begin){
        this.begin = begin;
    }

    public void setLimit(int limit){
        this.limit = limit;
    }

    @Override
    protected Void call() throws Exception {
        return null;
    }
}
