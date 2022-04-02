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
    private ArrayList<String> tags;

    public Recipe() {

    }

    public Recipe(String name, String image, String id) {
        this.name = name;
        this.image = image;
        this.id = id;

    }

    //Overloaded constructor for ingredients and measurements
    public Recipe(String name, String image, String id, ArrayList<String> ingredients, ArrayList<String> measurements) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.ingredients = ingredients;
        this.measurements = measurements;
    }

    //Overloaded constructor for tags
    public Recipe(String name, String image, String id, ArrayList<String> ingredients, ArrayList<String> measurements, ArrayList<String> tags) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.ingredients = ingredients;
        this.measurements = measurements;
        this.tags = tags;
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

    public void setIngredients(ArrayList<String> ingredients) { this.ingredients = ingredients; }

    public void setMeasurements(ArrayList<String> measurements) { this.measurements = measurements; }

    public void setTags(ArrayList<String> tags) { this.tags = tags; }


    public String getName() {
        return this.name;
    }
    public String getImage() { return this.image; }
    public String getId() { return this.id; }
    public ArrayList<String> getIngredients() { return this.ingredients; }
    public ArrayList<String> getMeasurements() { return this.measurements; }
    public ArrayList<String> getTags() { return this.tags; }

    @Override
    public int hashCode(){
        return this.id.hashCode();
    }

}
