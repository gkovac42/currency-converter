package com.example.goran.currencyconverter.data;

import com.example.goran.currencyconverter.data.remote.ApiManager;
import com.example.goran.currencyconverter.data.remote.model.Currency;
import com.example.goran.currencyconverter.data.util.CurrencyConverter;
import com.example.goran.currencyconverter.di.scope.PerActivity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

@PerActivity
public class DataManagerImpl implements DataManager {

    private ApiManager apiManager;

    @Inject
    public DataManagerImpl(ApiManager apiManager) {
        this.apiManager = apiManager;
    }

    @Override
    public Single<List<Currency>> getCurrencyRates() {
        return apiManager.getCurrencyRates()
                .map(currencies -> {
                    currencies.add(0, new Currency("HRK"));
                    return currencies;
                });
    }

    @Override
    public String convertCurrency(double quantity, Currency fromCurrency, Currency toCurrency) {
        return CurrencyConverter.convert(quantity, fromCurrency, toCurrency);
    }
}
