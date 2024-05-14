package org.newsaggregator.newsaggregatorclient.datamodel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

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
    private double priceChange;
    private String date;
    private String rank;
    private long marketCap;


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
        if (Double.parseDouble(price) > .01) {
            DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            return "$%s".formatted(df.format(Double.parseDouble(price)));
        }
        else{
            return "$%.8f".formatted(Double.parseDouble(price));
        }
    }

    public Float getRawPrice() {
        return Float.parseFloat(price);
    }

    public String getFormattedPrice(String currency) {
        if (Double.parseDouble(price) > .01) {
            DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            return "%s%s".formatted(currency, df.format(Double.parseDouble(price)));
        }
        else{
            return "%s%.8f".formatted(currency, Double.parseDouble(price));
        }
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "CoinPriceData{date=%s, coinName='%s', price='%s', marketCap=%s}".formatted(date, coinName, price, getFormattedMarketCap("$"));
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
        if (priceChange > 0.01) {
            return "up";
        } else if (Math.abs(priceChange) <= 0.01) {
            return "same";
        } else {
            return "down";
        }
    }

    public String getPriceChange() {
        if (priceChange > 0.01) {
            return "+%,.2f".formatted(priceChange);
        } else if (Math.abs(priceChange) <= 0.01) {
            return "0";
        } else {
            return "- %,.2f".formatted(-priceChange);
        }
    }

    public void setPriceChange(Float priceChange) {
        this.priceChange = priceChange;
    }

    public String getLogoURL(){
        return "https://assets.coincap.io/assets/icons/%s@2x.png".formatted(coinSymbol.toLowerCase());
    }

    public long getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(long marketCap) {
        this.marketCap = marketCap;
    }

    public String getFormattedMarketCap(String currency) {
        return "%s%,d".formatted(currency, marketCap);
    }
}
