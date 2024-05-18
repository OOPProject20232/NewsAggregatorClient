package org.newsaggregator.newsaggregatorclient.util;

import java.text.DecimalFormat;

public class NumberFormatter {
    public static String formatCurrencyValue(int number, String currency) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return currency + formatter.format(number);
    }
    
    public static String formatCurrencyValue(double number, String currency) {
        return "%s%,f".formatted(currency, number);
    }
    
    public static String formatPercentageValue(double number) {
        number = number * 100;
        if (number < .01){
            DecimalFormat formatter = new DecimalFormat("0.########");
            return formatter.format(number) + "%";
        } else {
            DecimalFormat formatter = new DecimalFormat("#,##0.00");
            return formatter.format(number) + "%";
        }
    }
}
