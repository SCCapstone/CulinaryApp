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

import com.github.CulinaryApp.views.CategoriesActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class SearchbarFragment extends Fragment {

    public EditText search_text;
    public RecyclerView recView;
    private String searchEntered = "";
    private ArrayList<String> categories = new ArrayList<>();

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
                    searchEntered = s.toString();
                    new Thread(this::showResults).start();
                } else {
                    ArrayList<String> empty = new ArrayList<>();
                    LoadSearch(empty, empty);
                }
            }

            private void showResults() {
                Log.d("Str entered",searchEntered);
                if(!searchEntered.equals("")) {
                    //Get list of categories
                    //TODO this is not really efficient to do everytime, though I'm presently unsure how to store these and retrieve them later
                    String categories_JSON = apiCall("https://www.themealdb.com/api/json/v1/1/categories.php");
                    try{
                    categories = JSONToArray(categories_JSON, "categories","strCategory");
                    } catch(JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("CATEGORIES",categories.toString());
                    ArrayList<String> categoriesThatMatch = new ArrayList<>();
                    ArrayList<String> recipesThatMatch = new ArrayList<>();
                    //Find matching categories
                    for(String cat : categories){
                        if(cat.toLowerCase(Locale.ROOT).contains(searchEntered.toLowerCase(Locale.ROOT))){
                            categoriesThatMatch.add(cat);
                        }
                    }

                    //Find matching recipes
                    String recipes_URL_start = "https://www.themealdb.com/api/json/v1/1/search.php?s=";
                    String recipes_JSON = apiCall(recipes_URL_start+searchEntered);
                    try{
                        recipesThatMatch = JSONToArray(recipes_JSON,"meals","strMeal");
                    } catch(JSONException e) {
                        e.printStackTrace();
                    }

                    ArrayList<String> finalRecipesThatMatch = recipesThatMatch;
                    ((CategoriesActivity)getContext()).runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            LoadSearch(categoriesThatMatch, finalRecipesThatMatch);
                        }
                    });
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

    //TODO redundant code, move to it's own class at some point
    private String apiCall(String URL_TO_OPEN){
        try {
            HttpsURLConnection connect = (HttpsURLConnection) new URL(URL_TO_OPEN).openConnection();
            InputStream response = connect.getInputStream();

            String stream = streamToString(response);

            return stream;


        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String streamToString(InputStream response) {
        String JSON = "{}";
        try (Scanner scanner = new Scanner(response, StandardCharsets.UTF_8.name())) {
            JSON = scanner.useDelimiter("\\A").next();
        }

        return JSON;

    }

    private ArrayList<String> JSONToArray(String JSON, String type, String names) throws JSONException {
        JSONObject obj = new JSONObject(JSON);

        ArrayList<String> list = new ArrayList<String>();
        JSONArray array = obj.getJSONArray(type);
        for(int i = 0 ; i < array.length() ; i++){
            list.add(array.getJSONObject(i).getString(names));
        }

        return list;
    }

}
