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

    void setId(String anId) {
        this.id = anId;
    }
    void setName(String aName) {
        this.name = aName;
    }
    void setImgThumbnail(String imgThumbnail) {
        this.imgThumbnail = imgThumbnail;
    }
    void setDescription(String aDescription) {
        this.description = description;
    }

    String getId() {
        return this.id;
    }
    String getName() {
        return this.name;
    }
    String getImgThumbnail() {
        return this.imgThumbnail;
    }
    String getDescription() {
        return this.description;
    }
    public Category(String anId, String aName, String anImgThumbnail, String aDescription) {
        setId(anId);
        setName(aName);
        setImgThumbnail(anImgThumbnail);
        setDescription(aDescription);
    }
}
