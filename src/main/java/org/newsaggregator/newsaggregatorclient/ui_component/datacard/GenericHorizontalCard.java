package org.newsaggregator.newsaggregatorclient.ui_component.datacard;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class GenericHorizontalCard extends HBox implements IGenericDataCard{
    public void GenericHorizontalCard(){

    }

    @Override
    public void setCardStyle(){
        this.getStylesheets().add("org/newsaggregator/newsaggregatorclient/ui_component/datacard/datacard.css");
        this.getStyleClass().add("datacard");
        this.getStyleClass().add("horizontal");
    };
}
