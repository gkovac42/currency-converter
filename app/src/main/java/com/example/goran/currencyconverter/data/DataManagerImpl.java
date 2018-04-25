package com.example.goran.currencyconverter.data;

import com.example.goran.currencyconverter.data.remote.ApiManager;
import com.example.goran.currencyconverter.data.remote.model.Currency;
import com.example.goran.currencyconverter.data.util.CurrencyConverter;
import com.example.goran.currencyconverter.di.scope.PerActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
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
        Observable<List<Currency>> observable = apiManager.getCurrencyRates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Disposable disposable = observable.subscribe(
                currencies -> {
                    Currency hrk = new Currency("HRK");
                    List<Currency> currenciesWithHrk = new ArrayList<>();
                    currenciesWithHrk.add(hrk);
                    currenciesWithHrk.addAll(currencies);
                    listener.onDataReady(currenciesWithHrk);
                },

                throwable -> listener.onError());

        compositeDisposable.add(disposable);
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
