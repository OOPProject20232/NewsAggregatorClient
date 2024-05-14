package org.newsaggregator.newsaggregatorclient;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.scene.shape.Line;
import org.newsaggregator.newsaggregatorclient.datamodel.CoinPriceData;
import org.newsaggregator.newsaggregatorclient.jsonparsing.CoinPriceJSONLoader;
import org.newsaggregator.newsaggregatorclient.ui_components.chart.CustomCursor;
import org.newsaggregator.newsaggregatorclient.ui_components.chart.LineChartWithCrosshair;
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.LoadingDialog;
import org.newsaggregator.newsaggregatorclient.util.TimeFormatter;

import java.text.DecimalFormat;
import java.util.*;

public class MarketDataController {
    /**
     * Controller của tab dữ liệu thị trường
     */

//    private Map<String, List<CoinPriceData>> coinPriceData;
    private CoinPriceJSONLoader coinPriceJSONLoader = new CoinPriceJSONLoader();
    private final String currency = "$";
    private final String symbol = "BTC";
    private final String[] symbolList = {"BTC"};

    @FXML
    Label chartTitle;

    @FXML
    Label coinPriceLabel;

    LineChartWithCrosshair<String, Number> coinPriceChart;

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

    @FXML
    VBox coinPriceFrame;

    @FXML
    Label priceChangeLabel;

    @FXML
    TableView<CoinData> coinPriceTable;

    @FXML
    TableColumn<CoinData, Hyperlink> coinNameColumn;

    @FXML
    TableColumn<CoinData, String> coinPriceColumn;

    @FXML
    TableColumn<CoinData, String> priceChangeColumn;

    @FXML
    TableColumn<CoinData, String> marketCapColumn;

    NewsAggregatorClientController mainController;

    public MarketDataController() {
    }

    public MarketDataController(CoinPriceJSONLoader coinPriceJSONLoader, NewsAggregatorClientController mainController) {
//        this.coinPriceData = data;
        this.coinPriceJSONLoader = coinPriceJSONLoader;
        this.mainController = mainController;
        coinPriceJSONLoader.setLimit(100);
    }

    public void initialize() {
        // Khởi tạo các giá trị mặc định
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.show();
        coinPriceJSONLoader.setLimit(100);
        Platform.runLater(() -> {
            try{
                coinPriceJSONLoader.getJSONString();
            }
            catch (Exception e){
                coinPriceJSONLoader.loadJSON();
            }
            loadMarketData(7, true, symbolList);
            loadingDialog.close();
        });
        oneWeekButton.setOnAction(event -> {
            LoadingDialog loadingDialog1 = new LoadingDialog();
            loadingDialog1.show();
            loadMarketData(7, true, symbolList);
            loadingDialog1.close();
        });
        oneMonthButton.setOnAction(event -> {
            LoadingDialog loadingDialog1 = new LoadingDialog();
            loadingDialog1.show();
            Platform.runLater(()-> loadMarketData(30, true, symbolList));
            loadingDialog1.close();
        });
        sixMonthsButton.setOnAction(event -> {
            LoadingDialog loadingDialog1 = new LoadingDialog();
            loadingDialog1.show();
            Platform.runLater(()-> loadMarketData(180, true, symbolList));
            loadingDialog1.close();
        });
        ytdButton.setOnAction(event -> {
            Platform.runLater(()-> loadMarketData(365, true, symbolList));
        });
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Price");
        coinPriceChart = new LineChartWithCrosshair<>(xAxis, yAxis, new CustomCursor(new Line(), new Line(), true));
        coinPriceChart.setCreateSymbols(true);
        coinPriceChart.setAnimated(false);
        VBox.setVgrow(coinPriceChart, javafx.scene.layout.Priority.ALWAYS);
        coinPriceFrame.getChildren().clear();
        coinPriceFrame.getChildren().add(coinPriceChart);
        HBox.setHgrow(coinPriceChart, javafx.scene.layout.Priority.ALWAYS);
    }

    public void loadMarketData(int period, boolean clearSeries, String ...symbols) {
        /**
         * Tải dữ liệu thị trường từ JSON
         * @param period: số ngày cần tải dữ liệu
         *              7: 1 tuần
         *              30: 1 tháng
         *              180: 6 tháng
         *              365: 1 năm
         * @param clearSeries: xóa dữ liệu cũ trên biểu đồ hay không
         * @param symbols: danh sách các loại tiền cần tải dữ liệu
         *
         */
        if (clearSeries) {
            coinPriceChart.getData().clear();
        }
        List<CoinPriceData> coinPriceDataList = coinPriceJSONLoader.getNewestCoinPrices();
        double priceChangeByPeriod =  coinPriceJSONLoader.getChangeInPrice(coinPriceDataList.getFirst(), symbol, period);
        for (String symbol : symbols) {
            Map<String, String> dataMap = coinPriceJSONLoader.getCoinPricesBySymbol(symbol, period);
            if (dataMap == null || coinPriceDataList == null) {
                return;
            }
            List<String> dates = new ArrayList<>(dataMap.keySet());
            Collections.sort(dates);
            CoinPriceData newestData = coinPriceDataList.getFirst();
            String priceChanged = newestData.getPriceChange();
            String priceChangeState = newestData.getPriceChangeState();
            System.out.println("Price changed: " + priceChanged);
            priceChangeLabel.getStyleClass().clear();
            priceChangeLabel.getStyleClass().add("price__" + priceChangeState);
            if (priceChangeState.equals("up")) {
                priceChangeLabel.setText("▲" + priceChanged);
            } else if (priceChangeState.equals("down")) {
                priceChangeLabel.setText("▼" + priceChanged);
            } else {
                priceChangeLabel.setText("―");
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
            Platform.runLater(() -> {
                coinPriceChart.getData().add(series);
//                series.getNode().setStyle("-fx-stroke: #000000");
                for (XYChart.Data<String, Number> data : series.getData()) {
                    DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
                    float yValue = data.getYValue().floatValue();
                    String formattedYValue = decimalFormat.format(yValue);
                    if (yValue < .01){
                        formattedYValue = String.format("%s%.8f", currency, yValue);
                    }
                    else{
                        formattedYValue = currency + formattedYValue;
                    }
                    Tooltip tooltip = new Tooltip(data.getXValue() + "\n" + formattedYValue);
                    Tooltip.install(data.getNode(), tooltip);
                    tooltip.setShowDelay(Duration.seconds(.1));
                    if (period > 30) {
                        data.getNode().setStyle("-fx-background-color: transparent");
                        data.getNode().setOnMouseEntered(event -> {
                            data.getNode().setStyle("""
                                    -fx-background-color: white; -fx-border-color: black;
                                    -fx-padding: 5px; -fx-border-radius: 5px;
                                    -fx-border-width: 1px;
                            """);
                        });
                        data.getNode().setOnMouseExited(event -> {
                            data.getNode().setStyle("-fx-background-color: transparent");
                        });
                    }
//                    data.getNode().setStyle("-fx-background-color: transparent");
                }
            });
        }
        coinNameColumn.setCellValueFactory(new PropertyValueFactory<>("coinName"));
        coinPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceChangeColumn.setCellValueFactory(new PropertyValueFactory<>("priceChange"));
        marketCapColumn.setCellValueFactory(new PropertyValueFactory<>("marketCap"));
        Platform.runLater(() -> {
            coinPriceTable.getItems().clear();
            for (CoinPriceData coinPriceData : coinPriceDataList) {
                CoinData coinData = new CoinData(
                        coinPriceData.getCoinSymbol(),
                        coinPriceData.getFormattedPrice(currency),
                        coinPriceData.getPriceChange(),
                        coinPriceData.getFormattedMarketCap(currency)
                );
                coinPriceTable.getItems().add(coinData);
            }
        });
    }

    public void setMainController(NewsAggregatorClientController mainController) {
        this.mainController = mainController;
    }

    public static class CoinData{
        private String coinName;
        private String price;
        private String priceChange;
        private String marketCap;

        public CoinData(String coinName, String price, String priceChange, String marketCap){
            this.coinName = coinName;
            this.price = price;
            this.priceChange = priceChange;
            this.marketCap = marketCap;
        }


        public String getCoinName() {
            return coinName;
        }

        public void setCoinName(String coinName) {
            this.coinName = coinName;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPriceChange() {
            return priceChange;
        }

        public void setPriceChange(String priceChange) {
            this.priceChange = priceChange;
        }

        public String getMarketCap() {
            return marketCap;
        }

        public void setMarketCap(String marketCap) {
            this.marketCap = marketCap;
        }
    }
}
