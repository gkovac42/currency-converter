package com.example.goran.currencyconverter.converter;

import com.example.goran.currencyconverter.data.remote.model.Currency;

import java.util.List;

public interface ConverterContract {

    interface View {

        void loadSpinnersData(List<Currency> currencies);

        void displayResult(String result);

        void displayNetworkError();

        void displayInputError();


    }

    interface Presenter {

        void getData();

        void onClickSubmit(double quantity, Currency fromCurrency, Currency toCurrency);

        void onInputError();

        void onDestroy();
    }
}
