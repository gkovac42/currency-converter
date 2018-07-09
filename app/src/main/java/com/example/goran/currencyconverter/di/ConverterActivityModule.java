package com.example.goran.currencyconverter.di;

import com.example.goran.currencyconverter.converter.ConverterActivity;
import com.example.goran.currencyconverter.converter.ConverterContract;
import com.example.goran.currencyconverter.converter.ConverterPresenter;
import com.example.goran.currencyconverter.data.DataRepository;
import com.example.goran.currencyconverter.data.DataRepositoryImpl;
import com.example.goran.currencyconverter.di.scope.PerActivity;

import dagger.Module;
import dagger.Provides;


@Module
public class ConverterActivityModule {

    private ConverterActivity converterActivity;

    public ConverterActivityModule(ConverterActivity converterActivity) {
        this.converterActivity = converterActivity;
    }

    @Provides
    @PerActivity
    ConverterContract.View provideView() {
        return converterActivity;
    }

    @Provides
    @PerActivity
    ConverterContract.Presenter providePresenter(ConverterPresenter presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    DataRepository provideLoginInteractor(DataRepositoryImpl dataManager) {
        return dataManager;
    }
}
