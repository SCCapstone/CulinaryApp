package com.github.CulinaryApp.models;

import com.google.gson.annotations.SerializedName;

public class Category extends Resource {
    @SerializedName("idCategory") // used to map json keys to java properties w/ a diff name
    String id;

    @SerializedName("strCategory")
    String name;

    @SerializedName("strCategoryThumb")
    String imgThumbnail;

    @SerializedName("strCategoryDescription")
    String description;



}
