package com.github.CulinaryApp.livedata;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.CulinaryApp.models.Recipe;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class RecipesLiveData
        extends LiveData<List<Recipe>>
          implements EventListener<QueryDocumentSnapshot> {

    private static final String TAG = "In RecipesLiveData.java";

    private CollectionReference subCollectionReference; // from repository class
    private List<Recipe> tempRecipeList = new ArrayList<>();
    public MutableLiveData<List<Recipe>> recipeList = new MutableLiveData<>();

    public RecipesLiveData(CollectionReference subCollectionReference) {
        this.subCollectionReference = subCollectionReference;

    }


    public boolean invalidQuery(QueryDocumentSnapshot queryDocSnapshot) {
        return queryDocSnapshot == null && queryDocSnapshot.exists();
    }

    // listen for firestore updates & respond accordingly
    @Override public void
        onEvent(@Nullable QueryDocumentSnapshot queryDocSnapshot,
                  @Nullable FirebaseFirestoreException firetoreException) {

        if (invalidQuery(queryDocSnapshot)) {
            Log.d(TAG, "Query Snapshot Error. Exception thrown from Firestore:\n");
            Log.d(TAG, firetoreException.toString());
        }
        else {
            Map<String, Object> recipeListItems = queryDocSnapshot.getData();
            // empty temporary list to prevent duplicates
            tempRecipeList.clear();

            // create mapping to returned docs
            // where keys are firestore field names, values are field values (as an Object)
            for (Map.Entry<String, Object> entry : recipeListItems.entrySet()) {
                String name = "";
                String imgUrl = "";
                if (entry.getKey() == "name")
                    name = entry.getValue().toString();
                if (entry.getKey() == "imgUrl")
                    imgUrl = entry.getValue().toString();
                Recipe recipeFromFirestore = new Recipe();
                tempRecipeList.add(recipeFromFirestore);
            }
            // setting the mutable live data w/ array list of live data returned from our query snapshot
            recipeList.setValue(tempRecipeList);

        }

    }
}
