package com.github.CulinaryApp.models;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealDB {

    @GET("/json/v1/1/categories.php")
    Call<CategoryResourceResponse> listCategories();

}
