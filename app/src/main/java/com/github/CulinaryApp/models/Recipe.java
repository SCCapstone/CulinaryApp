package com.github.CulinaryApp.models;

import com.google.firebase.firestore.DocumentReference;

public class Recipe {


    String name;
    String image;
    String id;

    public Recipe() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String image) {
        this.image = image;
    }

    public void setId(String id) { this.id = id; }


    public String getName() {
        return this.name;
    }
    public String getImage() { return this.image; }
    public String getId() { return this.id; }

}
