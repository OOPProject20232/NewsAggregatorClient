package org.newsaggregator.newsaggregatorclient.ui_components.datacard;

import javafx.scene.control.Button;

public class CategoryClickable extends Button {
    public CategoryClickable(){
        this.getStyleClass().add("category-label");
    }

    public CategoryClickable(String categoryString){
        this();
        this.setText(categoryString);
    }
}
