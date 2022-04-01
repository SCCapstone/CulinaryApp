package com.github.CulinaryApp.repositories;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.github.CulinaryApp.models.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RecipesRepository {

    private static final String TAG = "RecipesRepository";

    FirebaseFirestore firestoreDB;
   // CollectionReference rootCategoryCollection; // there are two - CATEGORIES & CATEGORIESBYREGION
   // DocumentReference categoryUserSelected; // dynamic based on user selections from Category Page
   CollectionReference whereAreRecipesForCategoryChosen;

    MutableLiveData<Recipe> recipeMutableLiveData;
    MutableLiveData<List<Recipe>> recipeListMutableLiveData;


    public RecipesRepository(String strCategorySelected) {
        firestoreDB = FirebaseFirestore.getInstance();
        whereAreRecipesForCategoryChosen =
                firestoreDB.collection("CATEGORIES/")
                             .document(strCategorySelected + "/")
                                .collection("RECIPES");
        this.recipeListMutableLiveData = new MutableLiveData<>();
        // define recipe list
        recipeMutableLiveData = new MutableLiveData<>();

    }

    public MutableLiveData<List<Recipe>> getFirestoreRecipesLiveData() {
        Log.i("TAG", "getRecipeListMutableLiveData: ");

        whereAreRecipesForCategoryChosen.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<Recipe> recipeList = new ArrayList<>();
                for (QueryDocumentSnapshot aDocumentSnapshot : value) {
                    Log.d("QueryDocSnapshot:\n",
                            "Document ID: "
                             + aDocumentSnapshot.getId() + "\nFields: "
                                    + aDocumentSnapshot.getData());
                    if (aDocumentSnapshot != null) {
                        recipeList.add(aDocumentSnapshot.toObject(Recipe.class));
                    }
                }
                recipeListMutableLiveData.postValue(recipeList);
            }
        });
        return recipeListMutableLiveData;
    }
}
