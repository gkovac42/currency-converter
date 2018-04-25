package com.example.goran.currencyconverter.data.remote;

import com.example.goran.currencyconverter.data.remote.model.Currency;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class ApiManager {

    private HnbApiService apiService;

    @Inject
    public ApiManager(HnbApiService apiService) {
        this.apiService = apiService;
    }

    public Observable<List<Currency>> getCurrencyRates() {
        return apiService.getCurrencyRates();
    }

}
