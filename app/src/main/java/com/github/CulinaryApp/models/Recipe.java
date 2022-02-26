package com.github.CulinaryApp.models;

import com.google.firebase.firestore.DocumentReference;

import org.json.JSONObject;

import java.util.HashMap;

public class Recipe {

    private String name;
    private String image;
    private String id;

    public Recipe() {

    }

    public static JSONObject recipeToJSON(Recipe currentRecipe) {
        HashMap<String, String> recipeMap = new HashMap<>();

        recipeMap.put("name", currentRecipe.name);
        recipeMap.put("image", currentRecipe.image);
        recipeMap.put("id", currentRecipe.id);

        return new JSONObject(recipeMap);
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
