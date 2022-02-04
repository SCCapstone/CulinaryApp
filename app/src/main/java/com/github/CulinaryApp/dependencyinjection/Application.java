package com.github.CulinaryApp.dependencyinjection;

import javax.inject.Singleton;

import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.HiltAndroidApp;

import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import okhttp3.*;
import retrofit2.converter.gson.GsonConverterFactory;
import com.github.CulinaryApp.models.Category;
import com.github.CulinaryApp.models.CategoryResourceResponse;
import com.github.CulinaryApp.services.GetCategories;
/*
@HiltAndroidApp
// @InstallIn(SingletonComponent.class)
@InstallIn(ActivityComponent.class)
public class Application {

    String BASE_URL = "https://www.themealdb.com/api/json/v1/1";
    @Provides
    @Singleton
    public Retrofit provideRetrofit() {
        Retrofit rf;
        rf = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();
        return rf;
    }

}
*/