package com.github.CulinaryApp.services;

import retrofit2.http.GET;
import retrofit2.Call;
import com.github.CulinaryApp.models.CategoryResourceResponse;

public interface GetCategories {

    @GET("/categories.php")
    Call<CategoryResourceResponse> listCategories();
}
