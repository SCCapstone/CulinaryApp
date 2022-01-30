package com.github.CulinaryApp.models;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    Retrofit retrofit = Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api.php/json/v1/1/categories.php")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
