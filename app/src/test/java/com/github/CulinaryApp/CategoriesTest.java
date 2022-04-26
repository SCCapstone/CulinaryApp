package com.github.CulinaryApp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.github.CulinaryApp.views.CategoriesActivity;
import com.github.CulinaryApp.views.RegistrationActivity;

import org.json.JSONException;
import org.junit.Test;

public class CategoriesTest {

    //Return true if categories doesn't return an empty list on empty lifestyles
    @Test
    public void categoriesTest_Empty() throws JSONException {
        assertTrue(CategoriesActivity.categoriesTestEmpty());
    }

    //Check all the preferences and makes sure the categories dont return empty
    @Test
    public void categoriesTest_Athletic() throws JSONException {
        assertTrue(CategoriesActivity.categoriesTestAthletic());
    }
    @Test
    public void categoriesTest_Vegan() throws JSONException {
        assertTrue(CategoriesActivity.categoriesTestVegan());
    }
    @Test
    public void categoriesTest_Vegetarian() throws JSONException {
        assertTrue(CategoriesActivity.categoriesTestVegetarian());
    }
    @Test
    public void categoriesTest_Ketogenic() throws JSONException {
        assertTrue(CategoriesActivity.categoriesTestKetogenic());
    }
    @Test
    public void categoriesTest_Flexitarian() throws JSONException {
        assertTrue(CategoriesActivity.categoriesTestFlexitarian());
    }
    @Test
    public void categoriesTest_Mediterranean() throws JSONException {
        assertTrue(CategoriesActivity.categoriesTestMediterranean());
    }
}
