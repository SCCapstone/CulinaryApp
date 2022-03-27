package com.github.CulinaryApp;

import com.github.CulinaryApp.models.Recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecipeRecommendationEngine {

    //Object to put into map, stores recipe object and recipe object score
    //This is probably not the recommended way to store values of multiple types
    //into maps but it's the way I thought of so...
    public class RecipeMapObject{
        private int score;
        private Recipe recipe;

        public RecipeMapObject(){
            this.score = 1000;
        }

        public RecipeMapObject(Recipe recipe){
            this.score = 1000;
            this.recipe = recipe;
        }

        public void setRecipe(Recipe recipe) {
            this.recipe = recipe;
        }
        public void setScore(int score) { //Realistically should never be used but its here for completions sake
            this.score = score;
        }

        public int getScore() {
            return score;
        }
        public Recipe getRecipe() {
            return recipe;
        }

        //For changing altering the score after the initial value is set
        public void updateScore(int scoreUpdate){
            this.score += scoreUpdate;
        }
    }

    public Map<Recipe, Integer> scoreRecipes(ArrayList<Recipe> recipesList, ArrayList<String> lifestyles){
        Map recipeMap = new HashMap();
        for(Recipe recipe : recipesList){
            recipeMap.put(recipe, 1000+getScoreChange(recipe.getIngredients(), recipe.getMeasurements(), lifestyles));
        }

        return recipeMap;
    }

    //TODO
    private int getScoreChange(ArrayList<String> ingredients, ArrayList<String> measurements, ArrayList<String> lifestyles){

        for(String lifestyle : lifestyles){

            switch (lifestyle){
                case "Athletic":
                    break;

                case "Vegan":
                    break;

                case "Vegetarian":
                    break;

                case "Mediterranean":
                    break;

                case "Ketogenic":
                    break;

                case "Flexitarian":
                    break;
            }

        }

        return 0;
    }

}
