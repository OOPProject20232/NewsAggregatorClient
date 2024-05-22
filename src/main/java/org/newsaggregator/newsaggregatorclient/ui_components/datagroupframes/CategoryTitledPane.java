package org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import org.newsaggregator.newsaggregatorclient.datamodel.GenericData;
import org.newsaggregator.newsaggregatorclient.ui_components.datacard.NewsItemCard;

public class CategoryTitledPane<T, D extends GenericData> extends TitledPane {
    final VBox itemsGroupLayout = new VBox();


    public CategoryTitledPane() {
        this.setCollapsible(false);
        this.setContent(this.itemsGroupLayout);
        this.itemsGroupLayout.getStyleClass().add("category__layout");
        this.getStyleClass().add("category");
        AnchorPane container = new AnchorPane();
        this.setContent(container);
        container.getChildren().add(itemsGroupLayout);
        AnchorPane.setBottomAnchor(itemsGroupLayout, 0.0);
        AnchorPane.setTopAnchor(itemsGroupLayout, 0.0);
        AnchorPane.setLeftAnchor(itemsGroupLayout, 0.0);
        AnchorPane.setRightAnchor(itemsGroupLayout, 0.0);
        itemsGroupLayout.setSpacing(24);
    }

    public CategoryTitledPane(String title){
        this();
        setText(title);
    }

    public void addItem(T dataCard){
        itemsGroupLayout.getChildren().add((Node) dataCard);
    }

    public VBox getContainer(){
        return itemsGroupLayout;
    }
}
