package com.github.CulinaryApp.dependencyinjection;

import javax.inject.Singleton;

import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.HiltAndroidApp;

import dagger.hilt.components.SingletonComponent;
import okhttp3.HttpUrl;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@HiltAndroidApp
@InstallIn(SingletonComponent.class)
public class Application {

    String BASE_URL = "https://www.themealdb.com/api/json/v1/1";
    @Provides
    @Singleton
    public Retrofit provideRetrofit() {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
