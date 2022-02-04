package com.github.CulinaryApp.viewmodels;

import com.github.CulinaryApp.models.CategoryResourceResponse;
import com.github.CulinaryApp.services.GetCategories;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;

public class Categories {
    public static String BASE_URL = "https://www.themealdb.com/api/json/v1/1";
/*
    @Override
    public Call<CategoryResourceResponse> listCategories() {
        // @Provides
        //  @Singleton
        Retrofit rf;
        rf = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();
        rf.create(CategoryResourceResponse.class);
        return CategoryResourceResponse.parseJson(response);
    }

 */
}