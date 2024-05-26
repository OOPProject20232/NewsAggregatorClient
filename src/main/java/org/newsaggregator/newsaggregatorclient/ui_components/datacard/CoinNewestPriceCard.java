package org.newsaggregator.newsaggregatorclient.ui_components.datacard;

import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.newsaggregator.newsaggregatorclient.datamodel.CoinPriceData;

/**
 * Thẻ thông tin về 1 đồng coin hiện trên bảng giá mới nhất ở trang chính
 * Kế thừa từ HorizontalDataCard
 * @see HorizontalDataCard
 * Sử dụng 1 lớp mở rộng của CoinPriceData để lấy thông tin của đồng coin
 * @see CoinPriceData
 * Bao gồm các thành phần:
 * - Hình ảnh của đồng coin
 * - Tên đồng coin
 * - Giá của đồng coin
 * - Biến động giá của đồng coin
 */
public class CoinNewestPriceCard extends HorizontalDataCard<CoinPriceData>{
    private final CoinPriceData coinPriceData;
    private Label rank;
    private Label coinName;
    private Label coinSymbol;
    private Label coinPrice;
    private Label coinPriceChange;
    private ImageView coinImage = new ImageView();


    public CoinNewestPriceCard(CoinPriceData coinPriceData) {
        this.coinPriceData = coinPriceData;
        setCardStyle();
    }
//
//    @Override
//    public void setCardStyle() {
//
//    }

    @Override
    public void setText() {
        coinName = new Label(coinPriceData.getCoinName());
        // coinName.set
        coinPrice = new Label(coinPriceData.getPrice());
        coinPrice.getStyleClass().add("price");
        rank = new Label(coinPriceData.getRank());
        coinSymbol = new Label(coinPriceData.getCoinSymbol());
        String changeState = coinPriceData.getPriceChangeState();
        coinPriceChange = new Label(coinPriceData.getPriceChangePercentage());
        coinPriceChange.getStyleClass().add("price__" + changeState);
        if (changeState.equals("up")) {
            coinPriceChange.setText("▲ " + coinPriceChange.getText());
        }
        else if (changeState.equals("down")) {
            coinPriceChange.setText("▼ " + coinPriceChange.getText());
        }
        else{
            coinPriceChange.setText("―");
        }
        HBox priceBox = new HBox();
        priceBox.setAlignment(Pos.CENTER_RIGHT);
        priceBox.setSpacing(12);
        HBox.setHgrow(priceBox, javafx.scene.layout.Priority.ALWAYS);
        HBox priceChangeBox = new HBox();
        priceChangeBox.setAlignment(Pos.CENTER_LEFT);
        priceChangeBox.setMinWidth(100);
        priceChangeBox.setMaxWidth(100);
//        HBox.setHgrow(priceChangeBox, javafx.scene.layout.Priority.ALWAYS);
        priceChangeBox.getChildren().add(coinPriceChange);
        priceBox.getChildren().addAll(coinPrice, priceChangeBox);
        getChildren().addAll(coinSymbol, priceBox);
    }

    @Override
    public synchronized void setImage() {
        String imageUrl = coinPriceData.getLogoURL();
        System.out.println(imageUrl);
        Image logo = new Image(imageUrl, true);
        coinImage = new ImageView(logo);
        coinImage.setCache(true);
        coinImage.setCacheHint(CacheHint.SPEED);
        coinImage.setFitHeight(16);
        coinImage.setFitWidth(16);
        coinSymbol.setGraphic(coinImage);
    }
}
