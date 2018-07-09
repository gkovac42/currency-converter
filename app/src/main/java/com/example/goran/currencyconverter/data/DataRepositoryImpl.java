package com.example.goran.currencyconverter.data;

import com.example.goran.currencyconverter.data.local.CurrencyRoomDatabase;
import com.example.goran.currencyconverter.data.model.Currency;
import com.example.goran.currencyconverter.data.remote.ApiManager;
import com.example.goran.currencyconverter.data.util.Converter;
import com.example.goran.currencyconverter.di.scope.PerActivity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

@PerActivity
public class DataRepositoryImpl implements DataRepository {

    private ApiManager apiManager;
    private CurrencyRoomDatabase database;

    @Inject
    public DataRepositoryImpl(ApiManager apiManager, CurrencyRoomDatabase database) {
        this.apiManager = apiManager;
        this.database = database;
    }

    @Override
    public Single<List<Currency>> getCurrencyRatesRemote() {
        return apiManager.getCurrencyRates()
                .map(currencies -> {
                    currencies.add(0, new Currency("HRK"));
                    return currencies;
                });
    }

    @Override
    public Single<List<Currency>> getCurrencyRatesLocal() {
        return database.currencyDao().getAll();
    }

    @Override
    public void saveCurrencies(List<Currency> currencies) {
        database.currencyDao().insertAll(currencies);
    }

    @Override
    public String convertCurrency(double quantity, Currency fromCurrency, Currency toCurrency) {
        return Converter.convert(quantity, fromCurrency, toCurrency);
    }
}
