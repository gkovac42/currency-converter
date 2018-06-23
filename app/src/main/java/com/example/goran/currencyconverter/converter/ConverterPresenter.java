package com.example.goran.currencyconverter.converter;

import android.util.Log;

import com.example.goran.currencyconverter.data.DataManager;
import com.example.goran.currencyconverter.data.model.Currency;
import com.example.goran.currencyconverter.di.scope.PerActivity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@PerActivity
public class ConverterPresenter implements ConverterContract.Presenter {

    private DataManager dataManager;
    private ConverterContract.View view;
    private CompositeDisposable compositeDisposable;

    @Inject
    public ConverterPresenter(DataManager dataManager, ConverterContract.View view) {
        this.view = view;
        this.dataManager = dataManager;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getData(boolean hasNetwork) {
        if (hasNetwork) {
            getDataRemote();

        } else {
            getDataLocal();
            Log.e("INFO", "Getting local data.");
        }
    }

    private void getDataRemote() {
        dataManager.getCurrencyRatesRemote()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Currency>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<Currency> currencies) {
                        view.loadSpinnersData(currencies);
                        saveData(currencies);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.displayNetworkError();
                    }
                });
    }

    private void saveData(List<Currency> currencies) {
        compositeDisposable.add(Observable.fromIterable(currencies)
                .subscribeOn(Schedulers.io())
                .subscribe(currency -> {
                            dataManager.saveCurrency(currency);
                            Log.e("SAVED!", currency.getCurrencyCode());
                        },
                        throwable -> view.displayDatabaseError()));
    }

    private void getDataLocal() {
        dataManager.getCurrencyRatesLocal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Currency>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<Currency> currencies) {
                        view.loadSpinnersData(currencies);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("ERROR", e.getMessage());
                        view.displayDatabaseError();
                    }
                });
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
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}
