package org.newsaggregator.newsaggregatorclient.datamodel;

public class CoinPriceData extends GenericData {
    /**
     * Lớp lưu trữ dữ liệu giá của một coin
     * Các trường dữ liệu chính
     * @param coinName Tên của coin, ví dụ: Bitcoin, Ethereum
     * @param price Giá của coin
     * @param date Ngày giá được cập nhật
     */
    private String coinName;
    private String coinSymbol;
    private String price;
    private Float priceChange;
    private String date;
    private String rank;


    public CoinPriceData(){
    }

    public CoinPriceData(String coinName, String price, String date) {
        this.coinName = coinName;
        this.price = price;
        this.date = date;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getPrice() {
        return "$%s".formatted(price);
    }

    public Float getRawPrice() {
        return Float.parseFloat(price);
    }

    public String getFormattedPrice(String currency) {
        return "%s%s".formatted(currency, price);
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "CoinPriceData{date=%s, coinName='%s', price='%s'}".formatted(date, coinName, price);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public String getPriceChangeState() {
        if (priceChange > 0) {
            return "up";
        } else if (priceChange < 0) {
            return "down";
        } else {
            return "same";
        }
    }

    public String getPriceChange() {
        if (priceChange > 0) {
            return "+%s".formatted(priceChange);
        } else if (priceChange < 0.001) {
            return priceChange.toString();
        } else {
            return "-%s".formatted(priceChange);
        }
    }

    public void setPriceChange(Float priceChange) {
        this.priceChange = priceChange;
    }

    public String getLogoURL(){
        return "https://assets.coincap.io/assets/icons/%s@2x.png".formatted(coinSymbol.toLowerCase());
    }
}
