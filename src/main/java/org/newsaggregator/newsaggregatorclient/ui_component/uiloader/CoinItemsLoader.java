package org.newsaggregator.newsaggregatorclient.ui_component.uiloader;

import javafx.application.HostServices;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.newsaggregator.newsaggregatorclient.datamodel.CoinPriceData;
import org.newsaggregator.newsaggregatorclient.ui_component.datacard.CoinNewestPriceCard;
import org.newsaggregator.newsaggregatorclient.ui_component.datacard.CoinNewestPriceGroupFrame;
import org.newsaggregator.newsaggregatorclient.util.TimeFormatter;

import java.util.ArrayList;
import java.util.List;

public class CoinItemsLoader implements ItemsLoader {
    List<CoinNewestPriceCard> coinNewestPriceCards = new ArrayList<>();
    private Pane container;
    private HostServices hostServices;
    private CoinNewestPriceGroupFrame coinNewestPriceGroupFrame;

    public CoinItemsLoader() {
    }

    public CoinItemsLoader(Pane container, HostServices hostServices, CoinNewestPriceGroupFrame coinNewestPriceGroupFrame){
        this.container = container;
        this.hostServices = hostServices;
        this.coinNewestPriceGroupFrame = coinNewestPriceGroupFrame;
    }

    public CoinNewestPriceGroupFrame loadItems(List<CoinPriceData> data) {
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
