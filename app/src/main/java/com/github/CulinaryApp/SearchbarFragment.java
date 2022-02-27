package com.github.CulinaryApp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class SearchbarFragment extends Fragment {

    public EditText search_text;
    public RecyclerView recView;

    public SearchbarFragment(){}

    public static SearchbarFragment newInstance(String param1, String param2) {
        return new SearchbarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_searchbar, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        EditText search_text = getView().findViewById(R.id.edit_search);

        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Log.d("Txt","Before text change");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.d("Txt","On Text Change");
                if(!s.toString().isEmpty()){
                    showResults(s.toString());
                } else {
                    ArrayList<String> empty = new ArrayList<>();
                    LoadSearch(empty, empty);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Log.d("Txt","After Text Change");
                /**
                if(!s.toString().isEmpty()){
                    showResults(s.toString());
                }**/
            }
        });
        /**
        Button clipboardButton = getView().findViewById(R.id.toolbarClip);
        Button searchButton = getView().findViewById(R.id.toolbarSearch);
        Button profileButton = getView().findViewById(R.id.toolbarProfile);
        Button trendingButton = getView().findViewById(R.id.toolbarTrending);

        clipboardButton.setOnClickListener(toggleClipboard);
        profileButton.setOnClickListener(navToProf);
        searchButton.setOnClickListener(navToHome);
//        trendingButton.setOnClickListener(toggleTrending);**/
    }

    public void showResults(String s){
        Log.d("Str entered",s);
        if(!s.equals("")) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference catRef = db.collection("CATEGORIES");
            ArrayList<String> catsThatMatch = new ArrayList<>(); //Meow hehe
            ArrayList<String> recsThatMatch = new ArrayList<>();
            //Get categories that match search
            catRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot aDocInCollection : task.getResult()) {
                            String catName = (String) aDocInCollection.get("name");
                            Log.d("Cat searched", catName);
                            if (catName.toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))) {
                                Log.d("Matches", catName);
                                catsThatMatch.add(catName);
                                Log.d("Cat List", catsThatMatch.toString());
                            }

                            //Get Recipes that match
                            CollectionReference recRef = catRef.document(catName).collection("RECIPES");
                            recRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot aDocInDocument : task.getResult()) {
                                            String recName = (String) aDocInDocument.get("name");
                                            if (recName.toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT)))
                                                recsThatMatch.add(recName);
                                        }
                                    } else {
                                        Log.d("EXCEPTION: ", String.valueOf(task.getException()));
                                    }
                                    //Log.d("Recipes that match",recsThatMatch.toString());
                                    //Log.d("Categories that match", catsThatMatch.toString());

                                    //Load into recycle viewer
                                    LoadSearch(catsThatMatch, recsThatMatch);
                                }

                            });
                        }
                    } else {
                        Log.d("EXCEPTION: ", String.valueOf(task.getException()));
                    }
                }

            });
        }
    }

    public void LoadSearch(ArrayList<String> catList, ArrayList<String> recList){
        ArrayList<String> listToDisplay = new ArrayList<>();
        ArrayList<Boolean> recipeTracker = new ArrayList<>();
        if(!catList.isEmpty()) {
            listToDisplay.add("Categories");
            listToDisplay.addAll(catList);

            recipeTracker.add(false);
            for(String x : catList){
                recipeTracker.add(false);
            }
        }

        if (!recList.isEmpty()){
            listToDisplay.add("Recipes");
            listToDisplay.addAll(recList);

            recipeTracker.add(false);
            for(String x : recList){
                recipeTracker.add(true);
            }
        }

        RecyclerView recView = getView().findViewById(R.id.recyclerViewSearchbar);
        recView.setHasFixedSize(true);
        SearchAdapter recAdapter = new SearchAdapter(getContext(), listToDisplay, recipeTracker);
        recView.setAdapter(recAdapter);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
