package com.github.CulinaryApp.models;

import com.google.firebase.firestore.DocumentReference;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Recipe {
    private String name;
    private String image;
    private String id;
    private ArrayList<String> ingredients;
    private ArrayList<String> measurements;

    public Recipe() {

    }

    public Recipe(String name, String image, String id) {
        this.name = name;
        this.image = image;
        this.id = id;

    }

    //Overloaded constructor for ingredients
    public Recipe(String name, String image, String id, ArrayList<String> ingredients, ArrayList<String> measurements) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.ingredients = ingredients;
        this.measurements = measurements;
    }

    /**
     * converts recipe to JSON, id intentionally excluded bc it makes life easier when interacting w JSON in RecipeInstructions
     * @param currentRecipe
     * @return
     */
    public static JSONObject recipeValuesToJSON(Recipe currentRecipe) {
        HashMap<String, String> recipeMap = new HashMap<>();

        recipeMap.put("image", currentRecipe.image);
        recipeMap.put("name", currentRecipe.name);

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
    public ArrayList<String> getIngredients() { return this.ingredients; }
    public ArrayList<String> getMeasurements() { return this.measurements; }

}
