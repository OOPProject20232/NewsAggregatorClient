package org.newsaggregator.newsaggregatorclient.ui_component.datacard;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class GenericVerticalCard extends HBox implements IGenericDataCard{
    @Override
    public void setCardStyle() {
        this.getStylesheets().add("org/newsaggregator/newsaggregatorclient/ui_component/datacard/datacard.css");
        this.getStyleClass().add("datacard");
        this.getStyleClass().add("vertical");
    }
}
