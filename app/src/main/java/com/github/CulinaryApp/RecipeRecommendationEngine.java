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


    private int getScoreChange(ArrayList<String> ingredients, ArrayList<String> measurements, ArrayList<String> lifestyles){

        int currScoreChange = 0;

        for(String lifestyle : lifestyles){
            for(int i = 0; i < ingredients.size(); i++) {

                switch (lifestyle) {
                    case "Athletic":
                        currScoreChange += getAthleticScore(ingredients.get(i), measurements.get(i));
                        break;

                    case "Vegan":
                        currScoreChange += getVeganScore(ingredients.get(i), measurements.get(i));
                        break;

                    case "Vegetarian":
                        currScoreChange += getVegetarianScore(ingredients.get(i), measurements.get(i));
                        break;

                    case "Mediterranean":
                        currScoreChange += getMediterraneanScore(ingredients.get(i), measurements.get(i));
                        break;

                    case "Ketogenic":
                        currScoreChange += getKetogenicScore(ingredients.get(i), measurements.get(i));
                        break;

                    case "Flexitarian":
                        currScoreChange += getFlexitarianScore(ingredients.get(i), measurements.get(i));
                        break;
                }

            }

        }

        return currScoreChange;
    }

    //TODO
    private int getAthleticScore(String ingredient, String amount){
        return 0;
    }
    //TODO
    private int getVeganScore(String ingredient, String amount){
        return 0;
    }
    //TODO
    private int getVegetarianScore(String ingredient, String amount){
        return 0;
    }
    //TODO
    private int getMediterraneanScore(String ingredient, String amount){
        return 0;
    }
    //TODO
    private int getKetogenicScore(String ingredient, String amount){
        return 0;
    }
    //TODO
    private int getFlexitarianScore(String ingredient, String amount){
        return 0;
    }


    //TODO Convert measurements into standard unit

    //TODO pass standard unit into non-linear function, altered to reflect differences in desired amount
    /**
     * K affects how fast exponent grows in graph
     * smaller k values represent smaller slops
     * Amt is the amount of an ingredient represented in a standard unit
     * K should be changed based on desired standard amount of that ingredient
     */
    private double Sigmoid(int amt, float k){
        return 2*((1/(1+Math.exp(-k*amt)))-(1/2));
    }
}
