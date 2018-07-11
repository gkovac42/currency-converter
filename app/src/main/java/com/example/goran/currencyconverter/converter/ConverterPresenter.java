package com.example.goran.currencyconverter.converter;

import com.example.goran.currencyconverter.data.DataRepository;
import com.example.goran.currencyconverter.data.model.Currency;
import com.example.goran.currencyconverter.di.scope.PerActivity;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
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
        compositeDisposable.add(repository.getCurrencyRatesRemote()
                .doOnSuccess(currencies -> repository.saveCurrencies(currencies))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currencies -> view.loadSpinnersData(currencies),
                        throwable -> view.displayNetworkError()
                ));
    }

    private void getDataLocal() {
        compositeDisposable.add(repository.getCurrencyRatesLocal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currencies -> view.loadSpinnersData(currencies),
                        throwable -> view.displayDatabaseError()
                ));
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
