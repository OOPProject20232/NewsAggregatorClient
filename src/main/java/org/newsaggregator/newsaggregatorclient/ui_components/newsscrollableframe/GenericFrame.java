package org.newsaggregator.newsaggregatorclient.ui_components.newsscrollableframe;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class GenericFrame extends ScrollPane {
    protected final GridPane itemsContainer = new GridPane();
    protected final String JSON_FOLDER_PATH = "src/main/resources/json/";
    public GenericFrame(){
        AnchorPane container = new AnchorPane();
        this.setContent(container);
        container.getChildren().add(itemsContainer);
        AnchorPane.setBottomAnchor(itemsContainer, 0.0);
        AnchorPane.setTopAnchor(itemsContainer, 0.0);
        AnchorPane.setLeftAnchor(itemsContainer, 0.0);
        AnchorPane.setRightAnchor(itemsContainer, 0.0);
        setFitToWidth(true);
        setFitToHeight(true);
        itemsContainer.getChildren().clear();
        itemsContainer.getStyleClass().addAll("generic-transparent-container");
        itemsContainer.setHgap(36);
        itemsContainer.setVgap(36);
        itemsContainer.setPadding(new Insets(48, 48, 48, 48));
        itemsContainer.setAlignment(Pos.BASELINE_CENTER);
//        itemsContainer.setGridLinesVisible(true);
        container.getStyleClass().addAll( "generic-transparent-container");
        this.getStyleClass().addAll("generic-transparent-container");
    }

    public GridPane getItemsContainer() {
        return itemsContainer;
    }
}
