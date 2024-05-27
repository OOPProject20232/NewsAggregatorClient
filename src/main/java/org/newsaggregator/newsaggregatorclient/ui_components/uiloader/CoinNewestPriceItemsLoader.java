package org.newsaggregator.newsaggregatorclient.ui_components.uiloader;

import javafx.application.HostServices;
import org.newsaggregator.newsaggregatorclient.datamodel.CoinPriceData;
import org.newsaggregator.newsaggregatorclient.ui_components.datacard.CoinNewestPriceCard;
import org.newsaggregator.newsaggregatorclient.ui_components.datagroupframes.CoinNewestPriceTitledPane;
import org.newsaggregator.newsaggregatorclient.util.TimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     Class này chứa các hàm để load các giá mới nhất của các đồng coin lên trên GUI
 * </p>
 *
 * <p>
 * Các đối tượng gọi tới lớp này sẽ truyền một tham số là một danh sách các giá mới nhất, sau đó
 * sẽ tạo các card chứa thông tin giá và tên của coin
 * </p>
 */
public class CoinNewestPriceItemsLoader implements ItemsLoader<CoinPriceData> {
    private List<CoinNewestPriceCard> coinNewestPriceCards = new ArrayList<>();
    private final HostServices hostServices;
    private CoinNewestPriceTitledPane pane;

    public CoinNewestPriceItemsLoader(CoinNewestPriceTitledPane pane, HostServices hostServices) {
        this.hostServices = hostServices;
        this.pane = pane;
    }

    @Override
    public void loadItems(List<CoinPriceData> data) {
//        CoinNewestPriceTitledPane coinNewestPriceGroupFrame = new CoinNewestPriceTitledPane();
        for (CoinPriceData coinPriceData : data) {
            CoinNewestPriceCard coinNewestPriceCard = new CoinNewestPriceCard(coinPriceData);
            coinNewestPriceCard.setText();
            coinNewestPriceCard.setImage();
            pane.addItem(coinNewestPriceCard);
            coinNewestPriceCards.add(coinNewestPriceCard);
        }
        pane.addUpdatedTime(TimeFormatter.convertISOToNormal(data.getFirst().getDate()));
    }

    public List<CoinNewestPriceCard> getItems() {
        return coinNewestPriceCards;
    }
}
