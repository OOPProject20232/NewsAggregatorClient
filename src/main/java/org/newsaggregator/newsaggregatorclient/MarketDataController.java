package org.newsaggregator.newsaggregatorclient;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import org.newsaggregator.newsaggregatorclient.ui_components.dialogs.NoInternetDialog;
import org.newsaggregator.newsaggregatorclient.util.NumberFormatter;
import org.newsaggregator.newsaggregatorclient.util.TimeFormatter;

import java.net.NoRouteToHostException;
import java.util.*;

public class MarketDataController {
    /**
     * Controller của tab dữ liệu thị trường
     */

//    private Map<String, List<CoinPriceData>> coinPriceData;
    private CoinPriceJSONLoader coinPriceJSONLoader = new CoinPriceJSONLoader();
    private final String currency = "$";
    private String symbol = "BTC";
    private final String[] symbolList = {"BTC"};

    @FXML
    private Label chartTitle;

    @FXML
    private Label coinPriceLabel;

    private LineChartWithCrosshair<String, Number> coinPriceChart;

    @FXML
    private ToggleButton oneWeekButton;

    @FXML
    private ToggleButton oneMonthButton;

    @FXML
    private ToggleButton sixMonthsButton;

    @FXML
    private ToggleButton ytdButton;

    @FXML
    private VBox coinPriceFrame;

    @FXML
    private Label priceChangeLabel;

    @FXML
    private TableView<CoinData> coinPriceTable;

    @FXML
    private TableColumn<CoinData, Hyperlink> coinNameColumn;

    @FXML
    private TableColumn<CoinData, String> coinPriceColumn;

    @FXML
    private TableColumn<CoinData, String> priceChangeColumn;

    @FXML
    private TableColumn<CoinData, String> marketCapColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private Hyperlink sourceLink;

    private NewsAggregatorClientController mainController;

    private HostServices hostServices;

    public MarketDataController() {
    }

    public MarketDataController(CoinPriceJSONLoader coinPriceJSONLoader, NewsAggregatorClientController mainController){
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
//        loadMarketData(7, true, symbol);
        Platform.runLater(() -> {
            try{
                coinPriceJSONLoader.loadJSON();
            }
            catch (NoRouteToHostException e){
                NoInternetDialog noInternetDialog = new NoInternetDialog();
                noInternetDialog.show();
            }
            loadMarketData(7);
            loadingDialog.close();
        });
        oneWeekButton.setOnAction(event -> {
            LoadingDialog loadingDialog1 = new LoadingDialog();
            loadingDialog1.show();
            loadMarketData(7);
            loadingDialog1.close();
        });
        oneMonthButton.setOnAction(event -> {
            LoadingDialog loadingDialog1 = new LoadingDialog();
            loadingDialog1.show();
            loadMarketData(30);
//            Platform.runLater(()-> loadChartData(30, true, symbol));
            loadingDialog1.close();
        });
        sixMonthsButton.setOnAction(event -> {
            LoadingDialog loadingDialog1 = new LoadingDialog();
            loadingDialog1.show();
//            Platform.runLater(()-> loadChartData(180, true, symbol));
            loadMarketData(180);
            loadingDialog1.close();
        });
        ytdButton.setOnAction(event -> {
//            Platform.runLater(()-> loadChartData(365, true, symbol));
            LoadingDialog loadingDialog1 = new LoadingDialog();
            loadingDialog1.show();
            loadMarketData(365);
            loadingDialog1.close();
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
        sourceLink.setOnAction(event ->
                hostServices.showDocument("https://rapidapi.com/Coinranking/api/coinranking1"));
    }

    public void loadMarketData(int period) {
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
        List<CoinPriceData> coinPriceDataList = coinPriceJSONLoader.getDataList();
//        double priceChangeByPeriod =  coinPriceJSONLoader.getChangeInPrice(coinPriceDataList.getFirst(), symbol, period);
        loadChartData(period, true, symbol);
//        for (String symbol : symbols) {
        coinNameColumn.setCellValueFactory(new PropertyValueFactory<>("coinName"));
        coinPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceChangeColumn.setCellValueFactory(new PropertyValueFactory<>("priceChange"));
        marketCapColumn.setCellValueFactory(new PropertyValueFactory<>("marketCap"));
        ObservableList<CoinData> coinDataList = FXCollections.observableArrayList();
        for (CoinPriceData coinPriceData : coinPriceDataList) {
            double priceChangeByPeriod = coinPriceJSONLoader.getChangeInPrice(coinPriceData.getCoinSymbol(), period);
            double percentChange = priceChangeByPeriod / (coinPriceData.getRawPrice() - priceChangeByPeriod);
            String formattedPercentChange = NumberFormatter.formatPercentageValue(percentChange);
            CoinData coinData = new CoinData(
                    "%s (%s)".formatted(coinPriceData.getCoinSymbol(), coinPriceData.getCoinName()),
                    coinPriceData.getFormattedPrice(currency),
                    percentChange > 0 ? "▲" + formattedPercentChange : percentChange < 0 ? "▼" + formattedPercentChange : "―",
                    coinPriceData.getFormattedMarketCap(currency)
            );
            coinDataList.add(coinData);
        }
        FilteredList<CoinData> filteredData = new FilteredList<>(coinDataList, p -> true);
        coinPriceTable.setItems(filteredData);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(coinData -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (coinData.getCoinName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        coinPriceTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                CoinData coinData = coinPriceTable.getSelectionModel().getSelectedItem();
                symbol = coinData.getCoinName().split(" ")[0];
                LoadingDialog loadingDialog = new LoadingDialog();
                loadingDialog.show();
                Platform.runLater(() -> {
                    loadChartData(period, true, coinData.getCoinName().split(" ")[0]);
                    loadingDialog.close();
                });
            }
        });
    }

    public void loadChartData(int period, boolean clearSeries, String coinSymbol) {
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
        System.out.println(coinSymbol);
        if (clearSeries) {
            coinPriceChart.getData().clear();
        }
//        List<CoinPriceData> coinPriceDataList = coinPriceJSONLoader.getNewestCoinPrices();
        double priceChangeByPeriod = coinPriceJSONLoader.getChangeInPrice(coinSymbol, period);
        Map<String, String> dataMap = coinPriceJSONLoader.getCoinPricesBySymbol(coinSymbol, period);
        if (dataMap == null) {
            return;
        }
        List<String> dates = new ArrayList<>(dataMap.keySet());
        Collections.sort(dates);
        CoinPriceData newestData = coinPriceJSONLoader.getNewestCoinPriceByCoin(coinSymbol);
//            String priceChangeState = newestData.getPriceChangeState();
        String priceChangeState = priceChangeByPeriod > 0 ? "up" : priceChangeByPeriod < 0 ? "down" : "same";
        System.out.println("Price changed: " + NumberFormatter.formatPercentageValue(priceChangeByPeriod / (newestData.getRawPrice() - priceChangeByPeriod)));
        priceChangeLabel.getStyleClass().clear();
        priceChangeLabel.getStyleClass().add("price__" + priceChangeState);
        if (priceChangeState.equals("up")) {
            priceChangeLabel.setText("▲" + NumberFormatter.formatPercentageValue(priceChangeByPeriod / (newestData.getRawPrice() - priceChangeByPeriod)));
        } else if (priceChangeState.equals("down")) {
            priceChangeLabel.setText("▼" + NumberFormatter.formatPercentageValue(priceChangeByPeriod / (newestData.getRawPrice() - priceChangeByPeriod)));
        } else {
            priceChangeLabel.setText("―");
        }
        System.out.println(newestData);
        coinPriceLabel.setText(newestData.getFormattedPrice(currency));
        chartTitle.setText(coinSymbol);
        chartTitle.setGraphic(new ImageView(new Image(newestData.getLogoURL())));
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Price");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(coinSymbol);
        for (String date : dates) {
            System.out.println(date + ": " + dataMap.get(date));
            series.getData().add(new XYChart.Data<>(TimeFormatter.convertISOToDate(date), Float.parseFloat(dataMap.get(date))));
        }
            coinPriceChart.getData().add(series);
            for (XYChart.Data<String, Number> data : series.getData()) {
                String formattedYValue = NumberFormatter.formatCurrencyValue(data.getYValue().floatValue(), currency);
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
            }
    }

    public void setHostServices(HostServices hostServices){
        this.hostServices = hostServices;
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
