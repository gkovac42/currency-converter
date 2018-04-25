package com.example.goran.currencyconverter.data.remote;

import com.example.goran.currencyconverter.data.remote.model.Currency;
import com.example.goran.currencyconverter.util.Constants;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface HnbApiService {

    @GET(Constants.API_RATES_ENDPOINT)
    Observable<List<Currency>> getCurrencyRates();
}
