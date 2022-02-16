package com.github.CulinaryApp.models;

import com.google.firebase.firestore.DocumentReference;

public class Recipe {


    String name;
    DocumentReference image;

    public Recipe() {

    }

    public void setName(String name) {
        name = name;
    }

    public void setImage(String image) {
        image = image;
    }



    public String getName() {
        return name;
    }
    public String getImage() {
        return image.toString();
    }

}
