package com.github.CulinaryApp.repositories;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.github.CulinaryApp.models.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RecipesRepository {

    private static final String TAG = "RecipesRepository";

   FirebaseFirestore firestoreDB;
   CollectionReference rootCategoryCollection; // there are two - CATEGORIES & CATEGORIESBYREGION
   DocumentReference categoryUserSelected; // dynamic based on user selections from Category Page
   CollectionReference recipesSubCollection;  // RECIPES

    MutableLiveData<Recipe> recipeMutableLiveData;
    MutableLiveData<List<Recipe>> recipeListMutableLiveData;

    public CollectionReference getCategoryTypeSelection() {
        // hard-coded for now
        return firestoreDB.collection("CATEGORIES/");
    }
   public DocumentReference getCategorySelection() {
       // hard-coded for now
       return rootCategoryCollection.document("Beef/");
   }


   public RecipesRepository() {
       firestoreDB = FirebaseFirestore.getInstance();
       rootCategoryCollection = getCategoryTypeSelection();
       categoryUserSelected = getCategorySelection();
       recipesSubCollection = categoryUserSelected.collection("RECIPES/");
       recipeMutableLiveData = new MutableLiveData<>();

   }

    public MutableLiveData<List<Recipe>> getFirestoreRecipesLiveData() {
        Log.i("TAG", "getFirestoreRecipesLiveData:");

       recipesSubCollection
               .get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       List<Recipe> recipeList = new ArrayList<>();
                       Recipe item;
                       if (task.isSuccessful()) {
                           String name ="";
                           String downloadUrl="";
                           for (QueryDocumentSnapshot document : task.getResult()) {
                               Log.d(TAG, document.getId() + " => " + document.getData());
                               if (document != null)
                                   recipeList.add(document.toObject(Recipe.class));
                         /*      if (document.getId().toString() == "name")
                                   name = document.getData().toString();
                               if (document.getId().toString() == "downloadUrl")
                                   downloadUrl = document.getId().toString();

                               if (!(name == "" || downloadUrl == "")) */

                           }
                           recipeListMutableLiveData.postValue(recipeList);
                       }
                       else {
                           Log.d(TAG, "Error getting documents: " + task.getException());
                       }
                   }
               });
       return recipeListMutableLiveData;
    }
}
