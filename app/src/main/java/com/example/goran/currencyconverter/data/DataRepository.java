package com.example.goran.currencyconverter.data;

import com.example.goran.currencyconverter.data.model.Currency;

import java.util.List;

import io.reactivex.Single;

public interface DataRepository {

    Single<List<Currency>> getCurrencyRatesRemote();

    Single<List<Currency>> getCurrencyRatesLocal();

    void saveCurrencies(List<Currency> currencies);

    String convertCurrency(double quantity, Currency fromCurrency, Currency toCurrency);

}
