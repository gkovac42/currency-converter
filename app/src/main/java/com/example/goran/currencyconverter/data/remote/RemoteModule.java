package com.example.goran.currencyconverter.data.remote;

import android.content.Context;

import com.example.goran.currencyconverter.util.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RemoteModule {

    private static final int CACHE_SIZE = 10 * 1024 * 1024;

    @Provides
    @Singleton
    HnbApiService provideHnbApiService(Retrofit retrofit) {
        return retrofit.create(HnbApiService.class);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(Constants.API_BASE)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
        return new OkHttpClient.Builder()
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    Cache provideCache(Context context) {
        return new Cache(context.getCacheDir(), CACHE_SIZE);
    }
}
