package org.newsaggregator.newsaggregatorclient.ui_components.datacard;

import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.newsaggregator.newsaggregatorclient.datamodel.CoinPriceData;

public class CoinNewestPriceCard extends HorizontalDataCard<CoinPriceData>{
    private CoinPriceData coinPriceData;
    Label rank;
    Label coinName;
    Label coinSymbol;
    Label coinPrice;
    Label coinPriceChange;
    ImageView coinImage = new ImageView();


    public CoinNewestPriceCard(CoinPriceData coinPriceData) {
        this.coinPriceData = coinPriceData;
        setCardStyle();
    }

    @Override
    public void setCardStyle() {
//        this.setSpacing(SPACING);
        this.getStylesheets().add(STYLESHEET);
//        this.setMaxWidth(100);
//        this.getStylesheets().add(this.getClass().getResourceAsStream("assets/css/coin-newest-price-frame.css").toString());
    }

    @Override
    public void setText() {
        coinName = new Label(coinPriceData.getCoinName());
        // coinName.set
        coinPrice = new Label(coinPriceData.getPrice());
        coinPrice.getStyleClass().add("price");
        rank = new Label(coinPriceData.getRank());
        coinSymbol = new Label(coinPriceData.getCoinSymbol());
        String changeState = coinPriceData.getPriceChangeState();
        coinPriceChange = new Label(coinPriceData.getPriceChangePercentage());
        coinPriceChange.getStyleClass().add("price__" + changeState);
        if (changeState.equals("up")) {
            coinPriceChange.setText("▲ " + coinPriceChange.getText());
        }
        else if (changeState.equals("down")) {
            coinPriceChange.setText("▼ " + coinPriceChange.getText());
        }
        else{
            coinPriceChange.setText("―");
        }
        HBox priceBox = new HBox();
        priceBox.setAlignment(Pos.CENTER_RIGHT);
        priceBox.setSpacing(12);
        HBox.setHgrow(priceBox, javafx.scene.layout.Priority.ALWAYS);
        HBox priceChangeBox = new HBox();
        priceChangeBox.setAlignment(Pos.CENTER_LEFT);
        priceChangeBox.setMinWidth(100);
        priceChangeBox.setMaxWidth(100);
//        HBox.setHgrow(priceChangeBox, javafx.scene.layout.Priority.ALWAYS);
        priceChangeBox.getChildren().add(coinPriceChange);
        priceBox.getChildren().addAll(coinPrice, priceChangeBox);
        getChildren().addAll(coinSymbol, priceBox);
    }

    @Override
    public synchronized void setImage() {
        String imageUrl = coinPriceData.getLogoURL();
        System.out.println(imageUrl);
        Image logo = new Image(imageUrl, true);
        coinImage = new ImageView(logo);
        coinImage.setCache(true);
        coinImage.setCacheHint(CacheHint.SPEED);
        coinImage.setFitHeight(16);
        coinImage.setFitWidth(16);
        coinSymbol.setGraphic(coinImage);
    }
}
