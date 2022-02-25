package com.github.CulinaryApp;

import java.util.ArrayList;

public class LifestyleToCategories {

    /** Generally, more 'important' categories are returned towards the start of the list (GENERALLY)**/

    public static String[] Athletic(){
        String[] categoriesToReturn = {"Beef",
                "Breakfast",
                "Chicken",
                "Goat",
                "Lamb",
                "Miscellaneous",
                "Pasta",
                "Pork",
                "Seafood",
                "Side",
                "Starter"};
        return(categoriesToReturn);
    }

    public static String[] Vegan(){
        String[] categoriesToReturn = {"Vegan",
                "Vegetarian",
                "Pasta",
                "Side",
                "Starter",
                "Breakfast",
                "Miscellaneous"};
        return(categoriesToReturn);
    }

    public static String[] Vegetarian(){
        String[] categoriesToReturn = {"Vegetarian",
                "Vegan",
                "Breakfast",
                "Miscellaneous",
                "Pasta",
                "Side",
                "Starter"};
        return(categoriesToReturn);
    }

    public static String[] Mediterranean(){
        String[] categoriesToReturn = {"Seafood",
                "Vegan",
                "Vegetarian",
                "Chicken",
                "Beef",
                "Breakfast",
                "Goat",
                "Lamb",
                "Miscellaneous",
                "Pork",
                "Side",
                "Starter"};
        return(categoriesToReturn);
    }

    public static String[] Ketogenic(){
        String[] categoriesToReturn = {"Beef",
                "Chicken",
                "Goat",
                "Lamb",
                "Miscellaneous",
                "Pasta",
                "Pork",
                "Seafood",
                "Side"};
        return(categoriesToReturn);
    }

    public static String[] Flexitarian(){
        String[] categoriesToReturn = {"Vegan",
                "Vegetarian",
                "Seafood",
                "Breakfast",
                "Chicken",
                "Beef",
                "Pork",
                "Miscellaneous",
                "Side",
                "Starter"};
        return(categoriesToReturn);
    }

}
