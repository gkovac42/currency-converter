package com.example.goran.currencyconverter.data.remote;

import com.example.goran.currencyconverter.data.model.Currency;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public class ApiManager {

    private HnbApiService apiService;

    @Inject
    public ApiManager(HnbApiService apiService) {
        this.apiService = apiService;
    }

    public Single<List<Currency>> getCurrencyRates() {
        return apiService.getCurrencyRates();
    }
}
