package org.newsaggregator.newsaggregatorclient.ui_component.datacard;

import javafx.scene.layout.HBox;
import org.newsaggregator.newsaggregatorclient.datamodel.CoinPriceData;
import org.newsaggregator.newsaggregatorclient.jsonparsing.CoinPriceLoader;

import java.util.List;

public class CoinNewestPriceFrame extends HBox implements IGenericDataCard<CoinPriceData>{
    private CoinPriceData coinPriceData;
    private List<CoinPriceData> coinPriceDataList;

    public CoinNewestPriceFrame(CoinPriceData coinPriceData) {
        this.coinPriceData = coinPriceData;
    }

    @Override
    public void setCardStyle() {
        this.getStyleClass().add("coin-card");
    }

    @Override
    public void setText() {

    }

    @Override
    public void setImage() {

    }

    private void loadCoinPriceData() {
        // Load coin price data
        CoinPriceLoader coinPriceLoader = new CoinPriceLoader();
        coinPriceLoader.setCacheFileName("coins.json");
        coinPriceLoader.loadJSON();
    }
}
