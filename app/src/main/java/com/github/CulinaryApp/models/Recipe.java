package com.github.CulinaryApp.models;

import com.github.CulinaryApp.views.RecipeInstructionsActivity;
import com.google.firebase.firestore.DocumentReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Recipe {
    /**
     * Recipe class is the data model for recipes
     * The Recipes page contains a list of Recipe objects.
     * The Recipes detail page contains one recipe and uses an overloaded constructor for Recipe
     * Additionally, this class deals with recipe parsing from mealdb.
     */
    private String name;
    private String image;
    private String id;
    private ArrayList<String> ingredients;
    private ArrayList<String> measurements;
    private ArrayList<String> tags;
    private String instructionsString, ingredsString;
    public static final String URL_BASE = "https://www.themealdb.com/meal/";

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

    @Override
    public String toString() {
        return "Recipe: " + name + "\n" +
                URL_BASE + id + "\n" +
                "\n\nIngredients:\n" + ingredsString +
                "\n\nDirections:\n" + instructionsString;
    }

    public String[] getRecipeContentsById(){
        final String KEY_INSTRUCTIONS="strInstructions", KEY_AMOUNTS="strMeasure", KEY_INGREDIENTS="strIngredient";
        final int NUM_MAX_INGREDIENTS = 20;

        String mealJSONRaw = RecipeInstructionsActivity.apiCall(this.getId(), RecipeInstructionsActivity.URL_FIND_BY_ID);

//        String instructions = null;
        StringBuilder ingreds = new StringBuilder();

        try {
            JSONObject parsedRecipe = new JSONObject(mealJSONRaw);

            instructionsString = RecipeInstructionsActivity.getItemFromJSON(parsedRecipe, KEY_INSTRUCTIONS).toString();

            for(int i=1;i<=NUM_MAX_INGREDIENTS;i++) {
                String amt = RecipeInstructionsActivity.getItemFromJSON(parsedRecipe, KEY_AMOUNTS + i).toString();
                String ingred = RecipeInstructionsActivity.getItemFromJSON(parsedRecipe, KEY_INGREDIENTS + i).toString();

                if(ingred.equals("") || amt.equals("") || ingred.equals("null") || amt.equals("null"))
                    continue;

                ingreds.append(amt).append(" ")
                        .append(ingred).append("\n");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ingredsString = ingreds.toString();
        return new String[] {instructionsString, ingredsString};
    }

    /**
     * converts recipe to JSON, currentRecipe.id is intentionally excluded bc it makes life easier when interacting w JSON in RecipeInstructions
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
