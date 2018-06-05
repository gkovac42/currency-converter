package com.example.goran.currencyconverter.data;

import com.example.goran.currencyconverter.data.remote.ApiManager;
import com.example.goran.currencyconverter.data.remote.model.Currency;
import com.example.goran.currencyconverter.data.util.CurrencyConverter;
import com.example.goran.currencyconverter.di.scope.PerActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@PerActivity
public class DataManagerImpl implements DataManager {

    private ApiManager apiManager;
    private DataListener listener;
    private CompositeDisposable compositeDisposable;

    @Inject
    public DataManagerImpl(ApiManager apiManager) {
        this.apiManager = apiManager;
        this.compositeDisposable = new CompositeDisposable();
    }

    public interface DataListener {

        void onDataReady(List<Currency> rates);

        void onError();
    }

    public void setListener(DataListener listener) {
        this.listener = listener;
    }

    @Override
    public void getCurrencyRates() {
        Single<List<Currency>> single = apiManager.getCurrencyRates();
        single.map(currencies -> {
            List<Currency> currenciesWithHrk = new ArrayList<>();
            currenciesWithHrk.add(new Currency("HRK"));
            currenciesWithHrk.addAll(currencies);
            return currenciesWithHrk;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Currency>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<Currency> currencies) {
                        listener.onDataReady(currencies);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError();
                    }
                });
    }

    @Override
    public String convertCurrency(double quantity, Currency fromCurrency, Currency toCurrency) {
        return CurrencyConverter.convert(quantity, fromCurrency, toCurrency);
    }

    @Override
    public void cleanUp() {
        if (listener != null) {
            listener = null;
        }
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}
