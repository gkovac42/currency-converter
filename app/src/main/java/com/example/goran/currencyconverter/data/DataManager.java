package com.example.goran.currencyconverter.data;

import com.example.goran.currencyconverter.data.model.Currency;

import java.util.List;

import io.reactivex.Single;

public interface DataManager {

    Single<List<Currency>> getCurrencyRatesRemote();

    Single<List<Currency>> getCurrencyRatesLocal();

    void saveCurrency(Currency currency);

    String convertCurrency(double quantity, Currency fromCurrency, Currency toCurrency);

}
