package com.github.CulinaryApp;

import android.util.Log;

import com.github.CulinaryApp.models.Recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RecipeRecommendationEngine {

    /**
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
    }**/

    //Creates a map of recipe to its score, 1000 as a base score is chosen more or less arbitrarily
    public static Map<Recipe, Integer> scoreRecipes(ArrayList<Recipe> recipesList, ArrayList<String> lifestyles){
        Map recipeMap = new HashMap();
        for(Recipe recipe : recipesList){
            //Log.d("RECIPE ID",recipe.getId());
            recipeMap.put(recipe, 1000+getScoreChange(recipe.getIngredients(), recipe.getMeasurements(), lifestyles));
        }

        return sortByValue(recipeMap);
    }


    private static int getScoreChange(ArrayList<String> ingredients, ArrayList<String> measurements, ArrayList<String> lifestyles){

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
    private static int getAthleticScore(String ingredient, String amount){
        return 0;
    }
    //TODO
    private static int getVeganScore(String ingredient, String amount){
        return 0;
    }
    //TODO
    private static int getVegetarianScore(String ingredient, String amount){
        return 0;
    }
    //TODO
    private static int getMediterraneanScore(String ingredient, String amount){
        return 0;
    }
    //TODO
    private static int getKetogenicScore(String ingredient, String amount){
        return 0;
    }
    //TODO
    private static int getFlexitarianScore(String ingredient, String amount){
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
    private static double Sigmoid(int amt, float k){
        return 2*((1/(1+Math.exp(-k*amt)))-(1/2));
    }


    //Sorts hashmap by key value, larger values first
    //Slight variation on code found here: https://mkyong.com/java/how-to-sort-a-map-in-java/ because I'm lazy tbh
    private static Map<Recipe, Integer> sortByValue(Map<Recipe, Integer> unsortMap) {

        // 1. Convert Map to List of Map
        List<Map.Entry<Recipe, Integer>> list =
                new LinkedList<Map.Entry<Recipe, Integer>>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        Collections.sort(list, new Comparator<Map.Entry<Recipe, Integer>>() {
            public int compare(Map.Entry<Recipe, Integer> o1,
                               Map.Entry<Recipe, Integer> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<Recipe, Integer> sortedMap = new LinkedHashMap<Recipe, Integer>();
        for (Map.Entry<Recipe, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

}
