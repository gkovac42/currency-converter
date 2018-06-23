package com.example.goran.currencyconverter.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.goran.currencyconverter.data.model.Currency;


@Database(entities = Currency.class, version = 1, exportSchema = false)
public abstract class CurrencyRoomDatabase extends RoomDatabase {

    public abstract CurrencyDao currencyDao();

    private static CurrencyRoomDatabase instance;

    public static CurrencyRoomDatabase getDatabase(Context context) {
        if (instance == null) {
            synchronized (CurrencyRoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            CurrencyRoomDatabase.class, "currency_db")
                            .build();
                }
            }
        }
        return instance;
    }
}
