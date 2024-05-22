package org.newsaggregator.newsaggregatorclient.ui_components.uiloader;

import javafx.application.HostServices;
import org.newsaggregator.newsaggregatorclient.datamodel.CoinPriceData;
import org.newsaggregator.newsaggregatorclient.ui_components.datacard.CoinNewestPriceCard;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.CoinNewestPriceTitledPane;
import org.newsaggregator.newsaggregatorclient.util.TimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class CoinNewestPriceItemsLoader implements ItemsLoader<CoinPriceData> {
    /**
     *
     */
    List<CoinNewestPriceCard> coinNewestPriceCards = new ArrayList<>();
    HostServices hostServices;
    CoinNewestPriceTitledPane container;

    public CoinNewestPriceItemsLoader(CoinNewestPriceTitledPane container, HostServices hostServices) {
        this.hostServices = hostServices;
        this.container = container;
    }

    public void loadItems(List<CoinPriceData> data) {
//        CoinNewestPriceTitledPane coinNewestPriceGroupFrame = new CoinNewestPriceTitledPane();
        for (CoinPriceData coinPriceData : data) {
            CoinNewestPriceCard coinNewestPriceCard = new CoinNewestPriceCard(coinPriceData);
            coinNewestPriceCard.setText();
            coinNewestPriceCard.setImage();
            container.addItem(coinNewestPriceCard);
            coinNewestPriceCards.add(coinNewestPriceCard);
        }
        container.addUpdatedTime(TimeFormatter.convertISOToNormal(data.getFirst().getDate()));
    }

    public List<CoinNewestPriceCard> getItems() {
        return coinNewestPriceCards;
    }
}
