package com.example.goran.currencyconverter.di;

import com.example.goran.currencyconverter.converter.ConverterActivity;
import com.example.goran.currencyconverter.di.scope.PerActivity;

import dagger.Subcomponent;


@PerActivity
@Subcomponent (modules = ConverterActivityModule.class)
public interface ConverterActivitySubcomponent {

    void inject(ConverterActivity converterActivity);
}
