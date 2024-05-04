package org.newsaggregator.newsaggregatorclient.datamodel;

public class CoinPriceData extends GenericData {
    private String coinName;
    private String price;
    private String date;

    public CoinPriceData(){
    }

    public CoinPriceData(String coinName, String price) {
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
        return price;
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
}
