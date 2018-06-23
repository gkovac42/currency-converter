package com.example.goran.currencyconverter.data.local;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class LocalModule {

    @Provides
    CurrencyRoomDatabase provideCurrencyRoomDatabase(Context context) {
        return CurrencyRoomDatabase.getDatabase(context);
    }
}
