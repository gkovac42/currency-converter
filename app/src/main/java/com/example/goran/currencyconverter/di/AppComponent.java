package com.example.goran.currencyconverter.di;

import com.example.goran.currencyconverter.BaseApplication;
import com.example.goran.currencyconverter.data.local.LocalModule;
import com.example.goran.currencyconverter.data.remote.RemoteModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, RemoteModule.class, LocalModule.class})
public interface AppComponent {

    void inject(BaseApplication baseApplication);

    ConverterActivitySubcomponent converterActivitySubcomponent(ConverterActivityModule converterActivityModule);

}
