package com.example.goran.currencyconverter.data;

import com.example.goran.currencyconverter.data.remote.model.Currency;

public interface DataManager {

    void setListener(DataManagerImpl.DataListener listener);

    void getCurrencyRates();

    String convertCurrency(double quantity, Currency fromCurrency, Currency toCurrency);

    void cleanUp();

}
