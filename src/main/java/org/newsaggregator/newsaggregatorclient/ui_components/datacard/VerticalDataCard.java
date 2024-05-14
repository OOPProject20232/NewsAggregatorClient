package org.newsaggregator.newsaggregatorclient.ui_components.datacard;

import javafx.scene.layout.VBox;
import org.newsaggregator.newsaggregatorclient.datamodel.GenericData;

public abstract class VerticalDataCard<T extends GenericData> extends VBox {
    public void setCardStyle() {
        this.getStyleClass().add("datacard");
        this.getStylesheets().add("org/newsaggregator/newsaggregatorclient/assets/css/datacard.css");
        setSpacing(12);
    };

    public abstract void setText();

    public abstract void setImage();
}
