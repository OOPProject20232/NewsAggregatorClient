package org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes;

import javafx.scene.layout.FlowPane;
import org.newsaggregator.newsaggregatorclient.datamodel.RedditPostData;
import org.newsaggregator.newsaggregatorclient.ui_components.datacard.RedditCard;

public class RedditGroupTitledPane extends CategoryTitledPane<RedditCard, RedditPostData> {
//    private final VBox container = new VBox();
//    private final FlowPane container = new FlowPane();
    public RedditGroupTitledPane() {
        super();
        this.setContent(getContainer());
//        container.setHgap(24);
//        container.setVgap(24);
//        container.getStyleClass().add("category--invisible");
        this.getStyleClass().clear();
        this.getContainer().getStyleClass().clear();
        this.getStyleClass().add("category--invisible");
        this.getContainer().getStyleClass().add("category--invisible");
    }

    public RedditGroupTitledPane(String title){
        this();
        setText(title);
    }

//    @Override
//    public void addItem(RedditCard dataCard){
//        container.getChildren().add(dataCard);
//    }
}
