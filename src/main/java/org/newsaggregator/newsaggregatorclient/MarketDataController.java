package org.newsaggregator.newsaggregatorclient;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.scene.shape.Line;
import org.newsaggregator.newsaggregatorclient.datamodel.CoinPriceData;
import org.newsaggregator.newsaggregatorclient.jsonparsing.CoinPriceJSONLoader;
import org.newsaggregator.newsaggregatorclient.ui_components.chart.LineChartWithCrosshair;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.LoadingDialog;
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
    private String[] symbolList = {"BTC"};

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

    @FXML
    VBox marketDataArea;

    NewsAggregatorClientController mainController;

    public MarketDataController() {
    }

    public MarketDataController(CoinPriceJSONLoader coinPriceJSONLoader, NewsAggregatorClientController mainController) {
//        this.coinPriceData = data;
        this.coinPriceJSONLoader = coinPriceJSONLoader;
        this.mainController = mainController;
    }

    public void initialize() {
        // Khởi tạo các giá trị mặc định
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.show();
        coinPriceChart.setCreateSymbols(true);
        coinPriceChart.setAnimated(false);
        // create new hover node, for data presentation
        Platform.runLater(() -> {
            try{
                coinPriceJSONLoader.getJSONString();
            }
            catch (Exception e){
                coinPriceJSONLoader.loadJSON();
            }
            demoLoadMarketData(7, true, symbolList);
            loadingDialog.close();
        });
        oneWeekButton.setOnAction(event -> {
            LoadingDialog loadingDialog1 = new LoadingDialog();
            loadingDialog1.show();
            demoLoadMarketData(7, true, symbolList);
            loadingDialog1.close();
        });
        oneMonthButton.setOnAction(event -> {
            LoadingDialog loadingDialog1 = new LoadingDialog();
            loadingDialog1.show();
            Platform.runLater(()->demoLoadMarketData(30, true, symbolList));
            loadingDialog1.close();
        });
        sixMonthsButton.setOnAction(event -> {
            LoadingDialog loadingDialog1 = new LoadingDialog();
            loadingDialog1.show();
            Platform.runLater(()->demoLoadMarketData(180, true, symbolList));
            loadingDialog1.close();
        });
        ytdButton.setOnAction(event -> {
            Platform.runLater(()->demoLoadMarketData(365, true, symbolList));
        });
        coinPriceChart.setOnMouseEntered(event -> {
            System.out.println("Mouse entered");
            // draw a line for the vertical coordinates
        });
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Price");
        LineChartWithCrosshair lineChartWithCrosshair = new LineChartWithCrosshair(xAxis, yAxis);
        lineChartWithCrosshair.setCreateSymbols(true);
        lineChartWithCrosshair.setAnimated(false);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("BTC");
        series.getData().add(new XYChart.Data<>("2021-01-01", 10000));
        series.getData().add(new XYChart.Data<>("2021-01-02", 11000));
        series.getData().add(new XYChart.Data<>("2021-01-03", 12000));
        series.getData().add(new XYChart.Data<>("2021-01-04", 13000));
        series.getData().add(new XYChart.Data<>("2021-01-05", 14000));
        series.getData().add(new XYChart.Data<>("2021-01-06", 15000));
        series.getData().add(new XYChart.Data<>("2021-01-07", 16000));
        series.getData().add(new XYChart.Data<>("2021-01-08", 17000));
        series.getData().add(new XYChart.Data<>("2021-01-09", 18000));
        series.getData().add(new XYChart.Data<>("2021-01-10", 19000));
        lineChartWithCrosshair.getData().add(series);
        marketDataArea.getChildren().add(lineChartWithCrosshair);
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
            float priceChanged = coinPriceJSONLoader.getChangeInPrice(newestData, currency, period);
            System.out.println("Price changed: " + priceChanged);
            if (Math.abs(priceChanged) < .01){
//                coinPriceLabel.getStyleClass().add("price__same");
                coinPriceLabel.setStyle("-fx-text-fill: black;");
            } else if (priceChanged > 0) {
//                coinPriceLabel.getStyleClass().add("price__up");
                coinPriceLabel.setStyle("-fx-text-fill: green;");
            } else {
//                coinPriceLabel.getStyleClass().add("price__down");
                coinPriceLabel.setStyle("-fx-text-fill: red;");
            }
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
            Platform.runLater(()->{
                    coinPriceChart.getData().add(series);
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        Tooltip tooltip = new Tooltip(data.getXValue() + "\n" + data.getYValue());
                        Tooltip.install(data.getNode(), tooltip);
                        tooltip.setShowDelay(Duration.seconds(.1));
                        data.getNode().setStyle("-fx-background-color: transparent;");
                    }
                }
            );
        }
    }

    public void setMainController(NewsAggregatorClientController mainController) {
        this.mainController = mainController;
    }
}
