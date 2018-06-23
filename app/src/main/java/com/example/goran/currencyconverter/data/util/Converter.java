package com.example.goran.currencyconverter.data.util;

import com.example.goran.currencyconverter.data.model.Currency;

import java.text.DecimalFormat;

public class Converter {

    public static String convert(Double quantity, Currency fromCurrency, Currency toCurrency) {

        double result = 0;

        if (fromCurrency.isHRK() && toCurrency.isHRK()) {
            result = quantity;

        } else if (fromCurrency.isHRK()) {
            result = quantity / toCurrency.getMedianRateAsDouble() * toCurrency.getUnitValue();

        } else if (toCurrency.isHRK()) {
            result = quantity * fromCurrency.getMedianRateAsDouble() / fromCurrency.getUnitValue();
        }

        DecimalFormat df = new DecimalFormat("#0.00");

        return quantity + " " + fromCurrency + " = " + df.format(result) + " " + toCurrency;
    }
}
