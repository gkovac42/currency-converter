package com.example.goran.currencyconverter.data;

import com.example.goran.currencyconverter.data.remote.model.Currency;

import java.util.List;

import io.reactivex.Single;

public interface DataManager {

    Single<List<Currency>> getCurrencyRates();

    String convertCurrency(double quantity, Currency fromCurrency, Currency toCurrency);

}
