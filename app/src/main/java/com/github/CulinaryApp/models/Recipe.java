package com.github.CulinaryApp.models;

public class Recipe {


    String name;
    String imgUrl;

    public void setName(String aName) {
        aName = name;
    }

    public void setImgUrl(String anImgUrl) {
        anImgUrl = imgUrl;
    }

    public Recipe(String aName, String anImgUrl) {
        setName(aName);
        setImgUrl(anImgUrl);
    }

    public String getName() {
        return name;
    }
    public String getImgUrl() {
        return imgUrl;
    }

}
