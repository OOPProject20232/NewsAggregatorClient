package org.newsaggregator.newsaggregatorclient;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.newsaggregator.newsaggregatorclient.datamodel.CoinPriceData;
import org.newsaggregator.newsaggregatorclient.jsonparsing.CoinPriceJSONLoader;
import org.newsaggregator.newsaggregatorclient.util.TimeFormatter;

import java.util.*;

public class MarketDataController {
    /**
     * Controller của tab dữ liệu thị trường
     */

//    private Map<String, List<CoinPriceData>> coinPriceData;
    private CoinPriceJSONLoader coinPriceJSONLoader = new CoinPriceJSONLoader();
    private String currency = "$";
    private String symbol = "BTC";
    private String[] symbolList = {"BTC", "ETH"};

    @FXML
    Label chartTitle;

    @FXML
    Label coinPriceLabel;

    @FXML
    LineChart<String, Number> coinPriceChart;

    @FXML
    ToggleButton oneWeekButton;

    @FXML
    ToggleButton oneMonthButton;

    @FXML
    ToggleButton sixMonthsButton;

    @FXML
    ToggleButton ytdButton;

    public MarketDataController() {
    }

    public MarketDataController(CoinPriceJSONLoader coinPriceJSONLoader) {
//        this.coinPriceData = data;
        this.coinPriceJSONLoader = coinPriceJSONLoader;
    }

    public void initialize() {
        // Khởi tạo các giá trị mặc định
        coinPriceChart.setCreateSymbols(false);
        coinPriceChart.setAnimated(false);
        Platform.runLater(() -> {
            try{
                coinPriceJSONLoader.getJSONString();
            }
            catch (Exception e){
                coinPriceJSONLoader.loadJSON();
            }
            demoLoadMarketData(7, true, symbolList);
            oneWeekButton.setOnAction(event -> {
                demoLoadMarketData(7, true, symbolList);
            });
            oneMonthButton.setOnAction(event -> {
                demoLoadMarketData(30, true, symbolList);
            });
            sixMonthsButton.setOnAction(event -> {
                demoLoadMarketData(180, true, symbolList);
            });
            ytdButton.setOnAction(event -> {
                demoLoadMarketData(365, true, symbolList);
            });
        });

    }

    public void demoLoadMarketData(int period, boolean clearSeries, String ...symbols) {
        // Hàm demo dữ liệu giả cho tab dữ liệu thị trường
        if (clearSeries) {
            coinPriceChart.getData().clear();
        }
        for (String symbol : symbols) {
            Map<String, String> dataMap = coinPriceJSONLoader.getCoinPricesBySymbol(symbol, period);
            List<CoinPriceData> coinPriceDataList = coinPriceJSONLoader.getNewestCoinPrices();
            if (dataMap == null || coinPriceDataList == null) {
                return;
            }
            List<String> dates = new ArrayList<>(dataMap.keySet());
            Collections.sort(dates);
            CoinPriceData newestData = coinPriceDataList.getFirst();
            coinPriceLabel.setText(newestData.getFormattedPrice(currency));
            chartTitle.setText(newestData.getCoinName());
            chartTitle.setGraphic(new ImageView(new Image(newestData.getLogoURL())));
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Date");
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Price");
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(symbol);
            for (String date : dates) {
                System.out.println(date + ": " + dataMap.get(date));
                series.getData().add(new XYChart.Data<>(TimeFormatter.convertISOToDate(date), Float.parseFloat(dataMap.get(date))));
            }
            Platform.runLater(()->coinPriceChart.getData().add(series));
        }
    }
}
