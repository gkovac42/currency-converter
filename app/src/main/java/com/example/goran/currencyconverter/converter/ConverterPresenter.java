package com.example.goran.currencyconverter.converter;

import com.example.goran.currencyconverter.data.DataRepository;
import com.example.goran.currencyconverter.data.model.Currency;
import com.example.goran.currencyconverter.di.scope.PerActivity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@PerActivity
public class ConverterPresenter implements ConverterContract.Presenter {

    private DataRepository repository;
    private ConverterContract.View view;
    private CompositeDisposable compositeDisposable;

    @Inject
    public ConverterPresenter(DataRepository repository, ConverterContract.View view) {
        this.view = view;
        this.repository = repository;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void getData(boolean hasNetwork) {
        if (hasNetwork) {
            getDataRemote();

        } else {
            getDataLocal();
        }
    }

    private void getDataRemote() {
        repository.getCurrencyRatesRemote()
                .map(currencies -> {
                    repository.saveCurrencies(currencies);
                    return currencies;
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
                        view.loadSpinnersData(currencies);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.displayNetworkError();
                    }
                });
    }

    private void getDataLocal() {
        repository.getCurrencyRatesLocal()
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
                        view.displayDatabaseError();
                    }
                });
    }

    @Override
    public void onClickSubmit(double quantity, Currency fromCurrency, Currency toCurrency) {
        String result = repository.convertCurrency(quantity, fromCurrency, toCurrency);
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
