package com.github.CulinaryApp.models;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class CategoryResourceResponse {
    /**
     * This class maps the "categories" key of JSON response to a list of Category objects
     */
    @SerializedName("categories")
    List<Category> categoryList;

    public CategoryResourceResponse() {
        categoryList = new ArrayList<Category>();
    }

    public static CategoryResourceResponse parseJson(String response) {
        Gson gsonBuilder = new GsonBuilder().create();
        CategoryResourceResponse responseObject = gsonBuilder.fromJson(response, CategoryResourceResponse.class);
        return responseObject;
    }
}
