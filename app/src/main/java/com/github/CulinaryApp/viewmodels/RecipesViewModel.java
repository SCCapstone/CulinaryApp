package com.github.CulinaryApp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.github.CulinaryApp.livedata.RecipesLiveData;
import com.github.CulinaryApp.repositories.RecipesRepository;
import com.github.CulinaryApp.models.Recipe;
import com.github.CulinaryApp.views.RecyclerViewAdapter;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RecipesViewModel extends ViewModel {

    MutableLiveData<List<Recipe>> recipeListMutableLiveData;
    RecipesRepository recipesRepository;
    FirebaseFirestore mFirestore;

    public RecipesViewModel(String categorySelected) {
        recipesRepository = new RecipesRepository(categorySelected);
        recipeListMutableLiveData = recipesRepository.getFirestoreRecipesLiveData();
        mFirestore.getInstance();
    }

    public MutableLiveData<List<Recipe>> getRecipesOfACategory()  {
        return recipeListMutableLiveData;
    }
}
