package org.newsaggregator.newsaggregatorclient.ui_component.uiloader;

import javafx.application.HostServices;
import javafx.scene.layout.Pane;
import org.newsaggregator.newsaggregatorclient.datamodel.CoinPriceData;
import org.newsaggregator.newsaggregatorclient.ui_component.datacard.CoinNewestPriceCard;
import org.newsaggregator.newsaggregatorclient.ui_component.datacard.CoinNewestPriceGroupFrame;
import org.newsaggregator.newsaggregatorclient.util.TimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class CoinItemsLoader implements ItemsLoader {
    /**
     *
     */
    List<CoinNewestPriceCard> coinNewestPriceCards = new ArrayList<>();
    HostServices hostServices;
    Pane container;

    public CoinItemsLoader(CoinNewestPriceGroupFrame container, HostServices hostServices) {
    }

    public CoinNewestPriceGroupFrame loadItems(List<CoinPriceData> data) {
        CoinNewestPriceGroupFrame coinNewestPriceGroupFrame = new CoinNewestPriceGroupFrame();
        for (CoinPriceData coinPriceData : data) {
            CoinNewestPriceCard coinNewestPriceCard = new CoinNewestPriceCard(coinPriceData);
            coinNewestPriceCard.setText();
            coinNewestPriceCard.setImage();
            coinNewestPriceGroupFrame.addItem(coinNewestPriceCard);
            coinNewestPriceCards.add(coinNewestPriceCard);
        }
        coinNewestPriceGroupFrame.addUpdatedTime(TimeFormatter.convertISOToNormal(data.getFirst().getDate()));
        return coinNewestPriceGroupFrame;
    }

    public List<CoinNewestPriceCard> getItems() {
        return coinNewestPriceCards;
    }
}
