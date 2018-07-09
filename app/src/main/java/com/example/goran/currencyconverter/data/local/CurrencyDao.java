package com.example.goran.currencyconverter.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.goran.currencyconverter.data.model.Currency;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface CurrencyDao {

    @Query("SELECT * FROM currency_table")
    Single<List<Currency>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Currency> currencies);
}
