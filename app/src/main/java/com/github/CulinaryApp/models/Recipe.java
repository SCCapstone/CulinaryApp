package com.github.CulinaryApp.models;

import com.google.firebase.firestore.DocumentReference;

public class Recipe {


    String name;
    String image;

    public Recipe() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String image) {
        image = image;
    }



    public String getName() {
        return name;
    }
    public String getImage() { return image; }

}
