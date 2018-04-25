package com.example.goran.currencyconverter;

import android.app.Application;

import com.example.goran.currencyconverter.di.AppComponent;
import com.example.goran.currencyconverter.di.AppModule;
import com.example.goran.currencyconverter.di.DaggerAppComponent;
import com.example.goran.currencyconverter.data.remote.RemoteModule;


public class BaseApplication extends Application {

    private AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .remoteModule(new RemoteModule())
                .build();

        appComponent.inject(this);
    }
}
