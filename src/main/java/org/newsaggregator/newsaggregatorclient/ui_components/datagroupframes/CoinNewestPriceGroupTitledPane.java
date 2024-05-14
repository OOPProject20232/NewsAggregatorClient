package org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes;

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.cell.ImageGridCell;
import org.newsaggregator.newsaggregatorclient.datamodel.CoinPriceData;
import org.newsaggregator.newsaggregatorclient.jsonparsing.CoinPriceJSONLoader;
import org.newsaggregator.newsaggregatorclient.ui_components.datacard.CoinNewestPriceCard;

public class CoinNewestPriceGroupTitledPane extends CategoryTitledPane<CoinNewestPriceCard, CoinPriceData> {
    /**
     * TitledPane chứa giá mới nhất của các loại coin, được tạo bới controller khi nhập dữ liệu từ database
     * Bao gồm
     * - Tên coin
     * - Giá coin
     */
    Label updatedTime = new Label();
//    FlowPane itemsGroupLayout = new FlowPane();
    public CoinNewestPriceGroupTitledPane() {
        this.setText("Daily Price");
//        itemsGroupLayout.setPrefWrapLength(400);
//        itemsGroupLayout.setOrientation(Orientation.VERTICAL);
//        itemsGroupLayout.setMinWidth(300);
        itemsGroupLayout.setMaxWidth(400);
        this.setMaxWidth(400);
    }

    public void addUpdatedTime(String time) {
        updatedTime.setText("Updated at: " + time);
        this.itemsGroupLayout.getChildren().add(updatedTime);
    }

//    @Override
//    public void addItem(CoinNewestPriceCard coinNewestPriceCard) {
//        this.itemsGroupLayout.getChildren().add(coinNewestPriceCard);
//    }

//    public void addAllCoins(CoinPriceJSONLoader jsonLoader){
//        int i = 0;
//        for (CoinPriceData coinPriceData : jsonLoader.getNewestCoinPrices()){
//            Label rank = new Label(coinPriceData.getRank());
//            Label name = new Label(coinPriceData.getCoinSymbol());
//            name.setGraphic(new ImageView(coinPriceData.getLogoURL()));
//            Label price = new Label(coinPriceData.getPrice());
//            Label change = new Label(coinPriceData.getPriceChange());
//            change.getStyleClass().add("price__" + coinPriceData.getPriceChangeState());
//            if (coinPriceData.getPriceChangeState().equals("up")) {
//                change.setText("▲" + change.getText());
//            }
//            else if (coinPriceData.getPriceChangeState().equals("down")) {
//                change.setText("▼" + change.getText());
//            }
//            else{
//                change.setText("―");
//            }
//            itemsGroupLayout.add(rank, 0, i);
//            itemsGroupLayout.add(name, 1, i);
//            itemsGroupLayout.add(price, 2, i);
//            itemsGroupLayout.add(change, 3, i);
//            i++;
//        }
//    }
}
