package org.newsaggregator.newsaggregatorclient.ui_component.datacard;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import org.newsaggregator.newsaggregatorclient.datamodel.CoinPriceData;
import org.newsaggregator.newsaggregatorclient.jsonparsing.CoinPriceJSONLoader;

import java.util.List;

public class CoinNewestPriceCard extends HBox implements IGenericDataCard<CoinPriceData>{
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
        this.setSpacing(12);
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
        coinPriceChange = new Label(coinPriceData.getPriceChange());
        coinPriceChange.getStyleClass().add("price__" + changeState);
        if (changeState.equals("up")) {
            coinPriceChange.setText("▲" + coinPriceChange.getText());
        }
        else if (changeState.equals("down")) {
            coinPriceChange.setText("▼" + coinPriceChange.getText());
        }
        else{
            coinPriceChange.setText("―" + coinPriceChange.getText());
        }
        getChildren().addAll(rank, coinSymbol, coinPrice, coinPriceChange);
    }

    @Override
    public void setImage() {
        String imageUrl = coinPriceData.getLogoURL();
        System.out.println(imageUrl);
        Image logo = new Image(imageUrl, true);
        coinImage = new ImageView(logo);
        coinImage.setFitHeight(16);
        coinImage.setFitWidth(16);
        coinSymbol.setGraphic(coinImage);
    }
}
