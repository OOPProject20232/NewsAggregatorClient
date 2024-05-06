package org.newsaggregator.newsaggregatorclient.ui_component.datacard;

import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CoinNewestPriceGroupFrame extends TitledPane {
    /**
     * TitledPane chứa giá mới nhất của các loại coin, được tạo bới controller khi nhập dữ liệu từ database
     * Bao gồm
     * - Tên coin
     * - Giá coin
     */
    VBox coinGroupLayout = new VBox();
    Label updatedTime = new Label();
    public CoinNewestPriceGroupFrame() {
        this.setCollapsible(false);
        this.setText("Daily Coin Price");
        this.coinGroupLayout.getStyleClass().add("category__layout");
        this.getStyleClass().add("category");
//        this.vgrowPolicyProgerty().setPriority(1);
        this.setContent(coinGroupLayout);
    }

    public void addItem(CoinNewestPriceCard coinItem) {
        this.coinGroupLayout.getChildren().add(coinItem);
    }

    public void addUpdatedTime(String time) {
        updatedTime.setText("Updated at: " + time);
        this.coinGroupLayout.getChildren().add(updatedTime);
    }
}
