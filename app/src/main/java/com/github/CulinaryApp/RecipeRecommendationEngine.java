package com.github.CulinaryApp;

import com.github.CulinaryApp.models.Recipe;

public class RecipeRecommendationEngine {

    //Object to put into map, stores recipe object and recipe object score
    //This is probably not the recommended way to store values of multiple types
    //into maps but it's the way I thought of so...
    public class RecipeMapObject{
        private int Score;
        private Recipe recipe;

        public RecipeMapObject(){
            this.Score = 1000;
        }

    }
}
