//I've mostly used healthline.com guides as a guide for these values
//Arbitrarily chosen most of the time as it's somehow faster than the other way

package com.github.CulinaryApp;

import android.util.Log;

import com.github.CulinaryApp.models.Recipe;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

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
            recipeMap.put(recipe, 1000+getScoreChange(recipe.getIngredients(), recipe.getMeasurements(), recipe.getTags(), lifestyles));
        }

        return sortByValue(recipeMap);
    }


    private static int getScoreChange(ArrayList<String> ingredients, ArrayList<String> measurements, ArrayList<String> tags, ArrayList<String> lifestyles){

        int currScoreChange = 0;

        for(String lifestyle : lifestyles){

            //Find score change based on ingredients and tags
            switch (lifestyle) {
                case "Athletic":
                    for(int i = 0; i < ingredients.size(); i++)
                        currScoreChange += getAthleticScore(ingredients.get(i), measurements.get(i));

                    for(String tag : tags)
                        currScoreChange += getAthleticScoreTags(tag);
                    break;

                case "Vegan":
                    for(int i = 0; i < ingredients.size(); i++)
                        currScoreChange += getVeganScore(ingredients.get(i), measurements.get(i));

                    for(String tag : tags)
                        currScoreChange += getVeganScoreTags(tag);
                    break;

                case "Vegetarian":
                    for(int i = 0; i < ingredients.size(); i++)
                        currScoreChange += getVegetarianScore(ingredients.get(i), measurements.get(i));

                    for(String tag : tags)
                        currScoreChange += getVegetarianScoreTags(tag);
                    break;

                case "Mediterranean":
                    for(int i = 0; i < ingredients.size(); i++)
                        currScoreChange += getMediterraneanScore(ingredients.get(i), measurements.get(i));

                    for(String tag : tags)
                        currScoreChange += getMediterraneanScoreTags(tag);
                    break;


                case "Ketogenic":
                    for(int i = 0; i < ingredients.size(); i++)
                        currScoreChange += getKetogenicScore(ingredients.get(i), measurements.get(i));

                    for(String tag : tags)
                        currScoreChange += getKetogenicScoreTags(tag);
                    break;

                case "Flexitarian":
                    for(int i = 0; i < ingredients.size(); i++)
                        currScoreChange += getFlexitarianScore(ingredients.get(i), measurements.get(i));

                    for(String tag : tags)
                        currScoreChange += getFlexitarianScoreTags(tag);
                    break;
            }


        }

        return currScoreChange;
    }


    private static double getAthleticScore(String ingredient, String amount){
        ingredient = ingredient.toLowerCase(Locale.ROOT);

        if(ingredient.contains("chicken")
                || ingredient.contains("protein"))
            return 200;

        else if(ingredient.contains("salmon")
                || ingredient.contains("shrimp")
                || ingredient.contains("trout")
                || ingredient.contains("tuna")
                || ingredient.contains("fish")
                || ingredient.contains("oyster")
                || ingredient.contains("crab"))
            return 150;

        else if(ingredient.contains("beef")
                || ingredient.contains("lamb")
                || ingredient.contains("pork")
                || ingredient.contains("ham")
                || ingredient.contains("turkey"))
            return 100;

        else if(ingredient.contains("nut")
                || ingredient.contains("walnut")
                || ingredient.contains("almond")
                || ingredient.contains("flaxseed")
                || ingredient.contains("pumpkin seed")
                || ingredient.contains("chia seed"))
            return 25;

        else if(ingredient.contains("oil"))
            return 15;

        else if(ingredient.contains("avocado"))
            return 10;

        else if(ingredient.contains("tomato")
                || ingredient.contains("onion")
                || ingredient.contains("pepper"))
            return 10;

        else if(ingredient.contains("sugar")
                || ingredient.contains("cake")
                || ingredient.contains("ice cream")
                || ingredient.contains("icecream")
                || ingredient.contains("candy"))
            return -25;

        else if(ingredient.contains("rice")
                || ingredient.contains("wheat")
                || ingredient.contains("pasta"))
            return 15;

        else if(ingredient.contains("berry")
                || ingredient.contains("berries"))
            return 15;

        else if(ingredient.contains("peas")
                || ingredient.contains("kidney bean")
                || ingredient.contains("lentils")
                || ingredient.contains("chickpea"))
            return 15;

        else if(ingredient.contains("low fat"))
            return 25;

        else if(ingredient.contains("alcohol")
                || ingredient.contains("beer")
                || ingredient.contains("wine")
                || ingredient.contains("liquor"))
            return -50;

        else if(ingredient.contains("syrup")
                || ingredient.contains("sweetener")
                || ingredient.contains("dessert"))
            return -50;


        /**
         * Too complicated for now
        if(Pattern.compile(Pattern.quote("Chicken Breast"), Pattern.CASE_INSENSITIVE).matcher(ingredient).find()){
            if(amount.contains("/")){
                String[] splitString = amount.split("/");
                int numerator = Integer.parseInt(splitString[0]);
                int denominator = Integer.parseInt(splitString[1]);
                return 200*Sigmoid(numerator/denominator, 1.5);
            } else {
                return 200*Sigmoid(Integer.parseInt(amount),1.5);
            }
        }
        else if(Pattern.compile(Pattern.quote("Salmon"), Pattern.CASE_INSENSITIVE).matcher(ingredient).find()) {
            String[] splitString = amount.split(" ");
            if(splitString.length < 2){ //Likely no unit associated
                if(splitString[0].contains("/")){
                    String[] splitStringDash = splitString[0].split("/");
                    int numerator = Integer.parseInt(splitStringDash[0]);
                    int denominator = Integer.parseInt(splitStringDash[1]);
                    return 150*Sigmoid(numerator/denominator, 1.2);
                }
            } else { //Ingredient amount likely has unit

            }
        }**/
        return 0;
    }

    private static double getVeganScore(String ingredient, String amount){
        ingredient = ingredient.toLowerCase(Locale.ROOT);

        if (ingredient.contains("steak")
                || ingredient.contains("ham")
                || ingredient.contains("sausage")
                || ingredient.contains("bacon")
                || ingredient.contains("chicken")
                || ingredient.contains("turkey")
                || !ingredient.contains("substitute")
                || !ingredient.contains("based"))
            return -500;

        else if (ingredient.contains("salmon")
                || ingredient.contains("trout")
                || ingredient.contains("tuna")
                || ingredient.contains("mackerel")
                || ingredient.contains("oyster")
                || ingredient.contains("crab")
                || ingredient.contains("shrimp")
                || !ingredient.contains("substitute")
                || !ingredient.contains("based"))
            return -150;

        else if (ingredient.contains("egg")
                || !ingredient.contains("substitute")
                || !ingredient.contains("based"))
            return -100;

        else if (ingredient.contains("butter")
                || ingredient.contains("cream")
                || !ingredient.contains("substitute")
                || !ingredient.contains("based"))
            return -25;

        else if (ingredient.contains("cheese")
                || ingredient.contains("cheddar")
                || ingredient.contains("goat")
                || ingredient.contains("mozzarella")
                || !ingredient.contains("substitute")
                || !ingredient.contains("based"))
            return -50;

        else if (ingredient.contains("nuts")
                || ingredient.contains("walnut")
                || ingredient.contains("almond")
                || ingredient.contains("flaxseed")
                || ingredient.contains("pumpkin seed")
                || ingredient.contains("chia seed"))
            return 75;


        else if (ingredient.contains("oil"))
            return 15;

        else if (ingredient.contains("avocado"))
            return 50;

        else if (ingredient.contains("tomato")
                || ingredient.contains("onion")
                || ingredient.contains("pepper"))
            return 25;

        else if (ingredient.contains("sugar")
                || ingredient.contains("fruit")
                || ingredient.contains("cake")
                || ingredient.contains("ice cream")
                || ingredient.contains("icecream")
                || ingredient.contains("candy"))
            return 20;

        else if (ingredient.contains("rice")
                || ingredient.contains("wheat")
                || ingredient.contains("pasta")
                || ingredient.contains("cereal"))
            return 75;

        else if (ingredient.contains("berry")
                || ingredient.contains("berries"))
            return 30;

        else if (ingredient.contains("peas")
                || ingredient.contains("kidney bean")
                || ingredient.contains("lentils")
                || ingredient.contains("chickpea"))
            return 50;

        else if (ingredient.contains("low fat"))
            return 20;

        else if (ingredient.contains("alcohol")
                || ingredient.contains("beer")
                || ingredient.contains("wine")
                || ingredient.contains("liquor"))
            return -50;

        else if (ingredient.contains("syrup")
                || ingredient.contains("sweetener")
                || ingredient.contains("dessert"))
            return -15;

        return 0;
    }

    private static double getVegetarianScore(String ingredient, String amount){
        ingredient = ingredient.toLowerCase(Locale.ROOT);

        if (ingredient.contains("steak")
                || ingredient.contains("ham")
                || ingredient.contains("sausage")
                || ingredient.contains("bacon")
                || ingredient.contains("chicken")
                || ingredient.contains("turkey")
                || !ingredient.contains("substitute")
                || !ingredient.contains("based"))
            return -500;

        else if (ingredient.contains("salmon")
                || ingredient.contains("trout")
                || ingredient.contains("tuna")
                || ingredient.contains("mackerel")
                || ingredient.contains("oyster")
                || ingredient.contains("crab")
                || ingredient.contains("shrimp")
                || !ingredient.contains("substitute")
                || !ingredient.contains("based"))
            return -150;

        else if (ingredient.contains("egg")
                || !ingredient.contains("substitute")
                || !ingredient.contains("based"))
            return -100;

        else if (ingredient.contains("butter")
                || ingredient.contains("cream")
                || !ingredient.contains("substitute")
                || !ingredient.contains("based"))
            return -25;

        else if (ingredient.contains("cheese")
                || ingredient.contains("cheddar")
                || ingredient.contains("goat")
                || ingredient.contains("mozzarella")
                || !ingredient.contains("substitute")
                || !ingredient.contains("based"))
            return -50;

        else if (ingredient.contains("nuts")
                || ingredient.contains("walnut")
                || ingredient.contains("almond")
                || ingredient.contains("flaxseed")
                || ingredient.contains("pumpkin seed")
                || ingredient.contains("chia seed"))
            return 75;


        else if (ingredient.contains("oil"))
            return 15;

        else if (ingredient.contains("avocado"))
            return 50;

        else if (ingredient.contains("tomato")
                || ingredient.contains("onion")
                || ingredient.contains("pepper"))
            return 25;

        else if (ingredient.contains("sugar")
                || ingredient.contains("fruit")
                || ingredient.contains("cake")
                || ingredient.contains("ice cream")
                || ingredient.contains("icecream")
                || ingredient.contains("candy"))
            return 20;

        else if (ingredient.contains("rice")
                || ingredient.contains("wheat")
                || ingredient.contains("pasta")
                || ingredient.contains("cereal"))
            return 75;

        else if (ingredient.contains("berry")
                || ingredient.contains("berries"))
            return 30;

        else if (ingredient.contains("peas")
                || ingredient.contains("kidney bean")
                || ingredient.contains("lentils")
                || ingredient.contains("chickpea"))
            return 50;

        else if (ingredient.contains("low fat"))
            return 20;

        else if (ingredient.contains("alcohol")
                || ingredient.contains("beer")
                || ingredient.contains("wine")
                || ingredient.contains("liquor"))
            return -50;

        else if (ingredient.contains("syrup")
                || ingredient.contains("sweetener")
                || ingredient.contains("dessert"))
            return -15;

        return 0;
    }

    private static double getMediterraneanScore(String ingredient, String amount){
        ingredient = ingredient.toLowerCase(Locale.ROOT);

        if (ingredient.contains("steak")
                || ingredient.contains("ham")
                || ingredient.contains("sausage")
                || ingredient.contains("bacon")
                || ingredient.contains("turkey"))
            return -15;

        else if(ingredient.contains("chicken")
        || ingredient.contains("poultry"))
            return 25;

        else if (ingredient.contains("salmon")
                || ingredient.contains("trout")
                || ingredient.contains("tuna")
                || ingredient.contains("mackerel")
                || ingredient.contains("sardine")
                || ingredient.contains("fish")
                || ingredient.contains("shrimp")
                || ingredient.contains("oyster")
                || ingredient.contains("crab"))
            return 250;

        else if (ingredient.contains("egg"))
            return 25;

        else if (ingredient.contains("butter")
                || ingredient.contains("cream"))
            return -25;

        else if (ingredient.contains("cheese")
                || ingredient.contains("cheddar")
                || ingredient.contains("goat")
                || ingredient.contains("mozzarella")
                || ingredient.contains("yogurt"))
            return 15;

        else if (ingredient.contains("nuts")
                || ingredient.contains("walnut")
                || ingredient.contains("almond")
                || ingredient.contains("flaxseed")
                || ingredient.contains("pumpkin seed")
                || ingredient.contains("chia seed"))
            return 25;


        else if (ingredient.contains("oil"))
            return -15;


        else if (ingredient.contains("tomato")
                || ingredient.contains("onion")
                || ingredient.contains("pepper")
                || ingredient.contains("avocado")
                || ingredient.contains("apple")
                || ingredient.contains("banana")
                || ingredient.contains("orange")
                || ingredient.contains("fruit"))
            return 15;

        else if (ingredient.contains("sugar")
                || ingredient.contains("cake")
                || ingredient.contains("ice cream")
                || ingredient.contains("icecream")
                || ingredient.contains("candy"))
            return -25;

        else if (ingredient.contains("rice")
                || ingredient.contains("wheat")
                || ingredient.contains("pasta")
                || ingredient.contains("cereal")
                || ingredient.contains("oats")
                || ingredient.contains("corn")
                || ingredient.contains("rye")
                || ingredient.contains("barley")
                || ingredient.contains("bread"))
            return 50;

        else if (ingredient.contains("berry")
                || ingredient.contains("berries"))
            return 25;

        else if (ingredient.contains("peas")
                || ingredient.contains("kidney bean")
                || ingredient.contains("lentils")
                || ingredient.contains("chickpea"))
            return 15;

        else if (ingredient.contains("low fat"))
            return 10;

        else if (ingredient.contains("alcohol")
                || ingredient.contains("beer")
                || ingredient.contains("wine")
                || ingredient.contains("liquor"))
            return -25;

        else if (ingredient.contains("syrup")
                || ingredient.contains("sweetener")
                || ingredient.contains("dessert"))
            return -15;

        return 0;
    }

    private static double getKetogenicScore(String ingredient, String amount){
        ingredient = ingredient.toLowerCase(Locale.ROOT);

        if (ingredient.contains("steak")
                || ingredient.contains("ham")
                || ingredient.contains("sausage")
                || ingredient.contains("bacon")
                || ingredient.contains("chicken")
                || ingredient.contains("turkey"))
            return 100;

        else if (ingredient.contains("salmon")
                || ingredient.contains("trout")
                || ingredient.contains("tuna")
                || ingredient.contains("mackerel"))
            return 150;

        else if (ingredient.contains("egg"))
            return 50;

        else if (ingredient.contains("butter")
                || ingredient.contains("cream"))
            return 25;

        else if (ingredient.contains("cheese")
                || ingredient.contains("cheddar")
                || ingredient.contains("goat")
                || ingredient.contains("mozzarella"))
            return 10;

        else if (ingredient.contains("nuts")
                || ingredient.contains("walnut")
                || ingredient.contains("almond")
                || ingredient.contains("flaxseed")
                || ingredient.contains("pumpkin seed")
                || ingredient.contains("chia seed"))
            return 25;


        else if (ingredient.contains("oil"))
            return 15;

        else if (ingredient.contains("avocado"))
            return 10;

        else if (ingredient.contains("tomato")
                || ingredient.contains("onion")
                || ingredient.contains("pepper"))
            return 10;

        else if (ingredient.contains("sugar")
                || ingredient.contains("fruit")
                || ingredient.contains("cake")
                || ingredient.contains("ice cream")
                || ingredient.contains("icecream")
                || ingredient.contains("candy"))
            return -25;

        else if (ingredient.contains("rice")
                || ingredient.contains("wheat")
                || ingredient.contains("pasta")
                || ingredient.contains("cereal"))
            return -50;

        else if (ingredient.contains("berry")
                || ingredient.contains("berries"))
            return -10;

        else if (ingredient.contains("peas")
                || ingredient.contains("kidney bean")
                || ingredient.contains("lentils")
                || ingredient.contains("chickpea"))
            return -30;

        else if (ingredient.contains("low fat"))
            return -15;

        else if (ingredient.contains("alcohol")
                || ingredient.contains("beer")
                || ingredient.contains("wine")
                || ingredient.contains("liquor"))
            return -50;

        else if (ingredient.contains("sugar-free")
                || ingredient.contains("syrup")
                || ingredient.contains("sweetener")
                || ingredient.contains("dessert"))
            return -15;


        return 0;
    }

    private static double getFlexitarianScore(String ingredient, String amount){
        ingredient = ingredient.toLowerCase(Locale.ROOT);

        if (ingredient.contains("steak")
                || ingredient.contains("ham")
                || ingredient.contains("sausage")
                || ingredient.contains("bacon")
                || ingredient.contains("turkey"))
            return -15;

        else if(ingredient.contains("chicken")
                || ingredient.contains("poultry"))
            return 10;

        else if (ingredient.contains("salmon")
                || ingredient.contains("trout")
                || ingredient.contains("tuna")
                || ingredient.contains("mackerel")
                || ingredient.contains("sardine")
                || ingredient.contains("fish")
                || ingredient.contains("shrimp")
                || ingredient.contains("oyster")
                || ingredient.contains("crab"))
            return 25;

        else if (ingredient.contains("egg"))
            return 25;

        else if (ingredient.contains("butter")
                || ingredient.contains("cream"))
            return 10;

        else if (ingredient.contains("cheese")
                || ingredient.contains("cheddar")
                || ingredient.contains("goat")
                || ingredient.contains("mozzarella")
                || ingredient.contains("yogurt"))
            return 35;

        else if (ingredient.contains("nuts")
                || ingredient.contains("walnut")
                || ingredient.contains("almond")
                || ingredient.contains("flaxseed")
                || ingredient.contains("pumpkin seed")
                || ingredient.contains("chia seed"))
            return 50;


        else if (ingredient.contains("oil"))
            return 10;


        else if (ingredient.contains("tomato")
                || ingredient.contains("onion")
                || ingredient.contains("pepper")
                || ingredient.contains("avocado")
                || ingredient.contains("apple")
                || ingredient.contains("banana")
                || ingredient.contains("orange")
                || ingredient.contains("fruit"))
            return 15;

        else if (ingredient.contains("sugar")
                || ingredient.contains("cake")
                || ingredient.contains("ice cream")
                || ingredient.contains("icecream")
                || ingredient.contains("candy"))
            return -25;

        else if (ingredient.contains("rice")
                || ingredient.contains("wheat")
                || ingredient.contains("pasta")
                || ingredient.contains("cereal")
                || ingredient.contains("oats")
                || ingredient.contains("corn")
                || ingredient.contains("rye")
                || ingredient.contains("barley")
                || ingredient.contains("bread"))
            return 50;

        else if (ingredient.contains("berry")
                || ingredient.contains("berries"))
            return 25;

        else if (ingredient.contains("peas")
                || ingredient.contains("kidney bean")
                || ingredient.contains("lentils")
                || ingredient.contains("chickpea")
                || ingredient.contains("tofu")
                || ingredient.contains("tempeh")
                || ingredient.contains("legume")
                || ingredient.contains("lentils"))
            return 50;

        else if (ingredient.contains("low fat"))
            return 10;

        else if (ingredient.contains("alcohol")
                || ingredient.contains("beer")
                || ingredient.contains("wine")
                || ingredient.contains("liquor"))
            return -25;

        else if (ingredient.contains("syrup")
                || ingredient.contains("sweetener")
                || ingredient.contains("dessert"))
            return -15;

        return 0;
    }


    private static int getAthleticScoreTags(String tag){
        tag = tag.toLowerCase(Locale.ROOT);

        if(tag.contains("chicken")
                || tag.contains("protein")
                || tag.contains("carb")
                || tag.contains("athlete"))
            return 250;

        else if(tag.contains("meat")
                || tag.contains("beef")
                || tag.contains("lamb")
                || tag.contains("liver"))
            return 100;

        else if(tag.contains("fish")
                || tag.contains("shrimp"))
            return 175;

        else if(tag.contains("egg")
                || tag.contains("dairy"))
            return 50;

        else if(tag.contains("bean")
                || tag.contains("fruit")
                || tag.contains("vegetable"))
            return 25;

        else if(tag.contains("fat") && !tag.contains("low"))
            return -100;

        else if(tag.contains("dessert"))
            return -25;
        return 0;
    }

    private static int getVeganScoreTags(String tag){
        tag = tag.toLowerCase(Locale.ROOT);

        if(tag.contains("fat") && !tag.contains("low")){
            return -250;
        }

        else if(tag.contains("keto")){
            return -100;
        }
        else if(tag.contains("low carb")){
            return 10;
        }
        else if(tag.contains("meat") && !tag.contains("based") && !tag.contains("substitute")){
            return -500;
        }
        else if(tag.contains("protein")){
            return 50;
        }
        else if(tag.contains("fruit")){
            return 50;
        }
        else if(tag.contains("grain")
                || tag.contains("wheat")
                || tag.contains("starch")
                || tag.contains("rice")
                || tag.contains("pasta")
                || tag.contains("cereal")){
            return 75;
        }
        else if(tag.contains("bean")){
            return 50;
        }
        else if(tag.contains("low fat")){
            return 25;
        }
        else if(tag.contains("fish") && !tag.contains("based") && !tag.contains("substitute")){ //Idk if some vegans eat fish hehe
            return -150;
        }
        else if(tag.contains("egg")){
            return -150;
        }
        else if(tag.contains("cheese")
                || tag.contains("dairy")){
            return -250;
        }
        else if(tag.contains("steak")
                || tag.contains("ham")
                || tag.contains("chicken")
                || tag.contains("turkey")
                && !tag.contains("based") && !tag.contains("substitute")){
            return -500;
        }
        else if(tag.contains("dessert")){
            return 10;
        }

        return 0;
    }

    private static int getVegetarianScoreTags(String tag){
        tag = tag.toLowerCase(Locale.ROOT);

        if(tag.contains("fat") && !tag.contains("low")){
            return -100;
        }
        else if(tag.contains("keto")){
            return -50;
        }
        else if(tag.contains("low carb")){
            return 10;
        }
        else if(tag.contains("meat") && !tag.contains("based") && !tag.contains("substitute")){
            return -500;
        }
        else if(tag.contains("protein")){
            return 50;
        }
        else if(tag.contains("fruit")){
            return 50;
        }
        else if(tag.contains("grain")
                || tag.contains("wheat")
                || tag.contains("starch")
                || tag.contains("rice")
                || tag.contains("pasta")
                || tag.contains("cereal")){
            return 75;
        }
        else if(tag.contains("bean")){
            return 50;
        }
        else if(tag.contains("low fat")){
            return 25;
        }
        else if(tag.contains("fish") && !tag.contains("based") && !tag.contains("substitute")){ //Idk if some vegans eat fish hehe
            return -100;
        }
        else if(tag.contains("egg")){
            return -100;
        }
        else if(tag.contains("cheese")
                || tag.contains("dairy")){
            return -250;
        }
        else if(tag.contains("steak")
                || tag.contains("ham")
                || tag.contains("chicken")
                || tag.contains("turkey")
                && !tag.contains("based") && !tag.contains("substitute")){
            return -500;
        }
        else if(tag.contains("dessert")){
            return 10;
        }

        return 0;
    }

    private static int getMediterraneanScoreTags(String tag){
        tag = tag.toLowerCase(Locale.ROOT);

        if(tag.contains("fat") && !tag.contains("low")){
            return -25;
        }

        else if(tag.contains("keto")){
            return -50;
        }
        else if(tag.contains("low carb")){
            return 50;
        }
        else if(tag.contains("meat")){
            return 15;
        }
        else if(tag.contains("protein")){
            return 50;
        }
        else if(tag.contains("fruit")){
            return 50;
        }
        else if(tag.contains("grain")
                || tag.contains("wheat")
                || tag.contains("starch")
                || tag.contains("rice")
                || tag.contains("pasta")
                || tag.contains("cereal")){
            return 15;
        }
        else if(tag.contains("bean")){
            return 25;
        }
        else if(tag.contains("low fat")){
            return 25;
        }
        else if(tag.contains("fish")){
            return 250;
        }
        else if(tag.contains("egg")){
            return 15;
        }
        else if(tag.contains("cheese")){
            return 15;
        }
        else if(tag.contains("steak")
                || tag.contains("ham")
                || tag.contains("chicken")
                || tag.contains("turkey")){
            return -10;
        }
        else if(tag.contains("dessert")){
            return -10;
        }

        return 0;
    }

    private static int getKetogenicScoreTags(String tag){
        tag = tag.toLowerCase(Locale.ROOT);

        if(tag.contains("fat") && !tag.contains("low")){
            return 250;
        }

        else if(tag.contains("keto")){
            return 500;
        }
        else if(tag.contains("low carb")){
            return 100;
        }
        else if(tag.contains("carb")){ //Check for low carb in previous else if
            return -50;
        }
        else if(tag.contains("meat")){
            return 50;
        }
        else if(tag.contains("protein")){
            return 75;
        }
        else if(tag.contains("fruit")){
            return -75;
        }
        else if(tag.contains("grain")
                || tag.contains("wheat")
                || tag.contains("starch")
                || tag.contains("rice")
                || tag.contains("pasta")
                || tag.contains("cereal")){
            return -75;
        }
        else if(tag.contains("bean")){
            return -50;
        }
        else if(tag.contains("low fat")){
            return -500;
        }
        else if(tag.contains("fish")){
            return 100;
        }
        else if(tag.contains("egg")){
            return 50;
        }
        else if(tag.contains("cheese")){
            return 50;
        }
        else if(tag.contains("steak")
                || tag.contains("ham")
                || tag.contains("chicken")
                || tag.contains("turkey")){
            return 75;
        }
        else if(tag.contains("dessert")){
            return -100;
        }

        return 0;
    }


    private static int getFlexitarianScoreTags(String tag){
        tag = tag.toLowerCase(Locale.ROOT);

        if(tag.contains("fat") && !tag.contains("low")){
            return -50;
        }

        else if(tag.contains("keto")){
            return -100;
        }
        else if(tag.contains("low carb")){
            return 50;
        }
        else if(tag.contains("carb")){ //Check for low carb in previous else if
            return -10;
        }
        else if(tag.contains("meat")){
            return 10;
        }
        else if(tag.contains("protein")){
            return 75;
        }
        else if(tag.contains("fruit")){
            return 25;
        }
        else if(tag.contains("grain")
                || tag.contains("wheat")
                || tag.contains("starch")
                || tag.contains("rice")
                || tag.contains("pasta")
                || tag.contains("cereal")){
            return 50;
        }
        else if(tag.contains("bean")
                || tag.contains("tofu")){
            return 75;
        }
        else if(tag.contains("low fat")){
            return 25;
        }
        else if(tag.contains("fish")){
            return 35;
        }
        else if(tag.contains("egg")){
            return 25;
        }
        else if(tag.contains("cheese")){
            return 25;
        }
        else if(tag.contains("steak")
                || tag.contains("ham")
                || tag.contains("chicken")
                || tag.contains("turkey")){
            return -10;
        }
        else if(tag.contains("dessert")){
            return -10;
        }

        return 0;
    }


    //Convert measurements into standard unit for algorithm
    //For now ignored as the mealDB api isn't very standardized in terms of parsing units which makes
    //performing all the checks difficult
    private static double standardMeasureConverter(double amount, String unit){
        unit = unit.toLowerCase(Locale.ROOT);
        switch(unit){

            //Convert liquids to cups
            case "cup":
            case "cups":
                return amount;

            case "teaspoon":
            case "teaspoons":
                return amount*0.028333;

            case "tablespoon":
            case "tablespoons":
                return amount*0.0625;

            case "liter":
            case "liters":
                return amount*4.22675;

            case "milliliter":
            case "milliliters":
                return amount*0.00422675;

            case "fluid ounce":
            case "fluid ounces":
                return amount*0.125;

            case "quart":
            case "quarts":
                return amount*4;

            case "pint":
            case "pints":
                return amount*2;

            //Convert units of mass to pounds
            case "pound":
            case "pounds":
                return amount;

            case "gram":
            case "grams":
                return amount*0.00220462;

            case "kilogram":
            case "kilograms":
                return amount*2.20462;

            case "milligram":
            case "milligrams":
                return amount*2.2046e-6;

            case "stone":
            case "stones":
                return amount*14;

            case "ounce":
            case "ounces":
                return amount*0.625;
        }

        //If unrecognized unit, just return amount
        return amount;
    }

    /**
     * K affects how fast exponent grows in graph
     * smaller k values represent smaller slops
     * Amt is the amount of an ingredient represented in a standard unit
     * K should be changed based on desired standard amount of that ingredient
     */
    private static double Sigmoid(double amt, double k){
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
