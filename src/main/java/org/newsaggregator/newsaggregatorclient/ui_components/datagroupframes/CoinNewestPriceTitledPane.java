package org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes;

import javafx.application.HostServices;
import javafx.scene.control.Label;
import org.controlsfx.control.HyperlinkLabel;
import org.newsaggregator.newsaggregatorclient.datamodel.CoinPriceData;
import org.newsaggregator.newsaggregatorclient.ui_components.datacard.CoinNewestPriceCard;

public class CoinNewestPriceTitledPane extends CategoryTitledPane<CoinNewestPriceCard, CoinPriceData> {
    /**
     * TitledPane chứa giá mới nhất của các loại coin, được tạo bới controller khi nhập dữ liệu từ database
     * Bao gồm
     * - Tên coin
     * - Giá coin
     */
    Label updatedTime = new Label();
    HyperlinkLabel source = new HyperlinkLabel("Data source: [Coinranking API on RapidAPI.com]");
    HostServices hostServices;
//    FlowPane itemsGroupLayout = new FlowPane();
    public CoinNewestPriceTitledPane(HostServices hostServices) {
        this.setText("Daily Price");
//        itemsGroupLayout.setPrefWrapLength(400);
//        itemsGroupLayout.setOrientation(Orientation.VERTICAL);
//        itemsGroupLayout.setMinWidth(300);
        this.hostServices = hostServices;
        itemsGroupLayout.setMaxWidth(400);
        this.setMaxWidth(400);
    }

    public void addUpdatedTime(String time) {
        updatedTime.setText("Updated at: " + time);
        this.itemsGroupLayout.getChildren().add(updatedTime);
        this.itemsGroupLayout.getChildren().add(source);
        source.setOnAction(e -> {
            hostServices.showDocument("https://rapidapi.com/Coinranking/api/coinranking1");
        });
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
