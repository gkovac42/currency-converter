package com.example.goran.currencyconverter.converter;

import com.example.goran.currencyconverter.data.DataManager;
import com.example.goran.currencyconverter.data.DataManagerImpl;
import com.example.goran.currencyconverter.data.remote.model.Currency;
import com.example.goran.currencyconverter.di.scope.PerActivity;

import java.util.List;

import javax.inject.Inject;

@PerActivity
public class ConverterPresenter implements ConverterContract.Presenter, DataManagerImpl.DataListener {

    private DataManager dataManager;
    private ConverterContract.View view;

    @Inject
    public ConverterPresenter(DataManager dataManager, ConverterContract.View view) {
        this.view = view;
        this.dataManager = dataManager;
        this.dataManager.setListener(this);
    }

    @Override
    public void getData() {
        dataManager.getCurrencyRates();
    }

    @Override
    public void onClickSubmit(double quantity, Currency fromCurrency, Currency toCurrency) {
        String result = dataManager.convertCurrency(quantity, fromCurrency, toCurrency);
        view.displayResult(result);
    }

    @Override
    public void onClickClear() {
        view.clearInput();
    }

    @Override
    public void onInputError() {
        view.displayInputError();
    }

    @Override
    public void onDataReady(List<Currency> currencies) {
        view.loadSpinnersData(currencies);
    }

    @Override
    public void onError() {
        view.displayNetworkError();
    }

    @Override
    public void onDestroy() {
        dataManager.cleanUp();
    }
}
