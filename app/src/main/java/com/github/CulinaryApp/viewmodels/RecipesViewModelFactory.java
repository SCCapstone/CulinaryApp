package com.github.CulinaryApp.viewmodels;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.firestore.DocumentReference;

public class RecipesViewModelFactory implements ViewModelProvider.Factory {

   // private Application app;
    private String categorySelected;
    public RecipesViewModelFactory(String categorySelected)
    {
      //  this.app = app;
        this.categorySelected = categorySelected;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new RecipesViewModel(categorySelected);
    }
}
