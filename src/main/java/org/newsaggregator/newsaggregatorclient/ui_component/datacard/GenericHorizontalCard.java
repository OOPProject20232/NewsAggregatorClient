package org.newsaggregator.newsaggregatorclient.ui_component.datacard;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.newsaggregator.newsaggregatorclient.datamodel.GenericData;

public class GenericHorizontalCard extends HBox implements IGenericDataCard<GenericData> {

    @Override
    public void setCardStyle(){
        this.getStylesheets().add("org/newsaggregator/newsaggregatorclient/ui_component/datacard/datacard.css");
        this.getStyleClass().add("datacard");
        this.getStyleClass().add("horizontal");
    }

    @Override
    public void setText() {

    }

    @Override
    public void setImage() {

    }
}
