package com.example.goran.currencyconverter.di;

import com.example.goran.currencyconverter.converter.ConverterActivity;
import com.example.goran.currencyconverter.di.scope.PerActivity;

import dagger.Subcomponent;

/**
 * Created by Goran on 10.1.2018..
 */

@PerActivity
@Subcomponent (modules = ConverterActivityModule.class)
public interface ConverterActivitySubcomponent {

    void inject(ConverterActivity homeActivity);
}
