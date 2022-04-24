package com.github.CulinaryApp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import java.lang.reflect.Array;
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
    private boolean x_clicked;

    public SearchbarFragment() {
    }

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
        x_clicked = false;

        /**
         * On click listener for x button on side of searchbar basically
         * Removes search if clicked
         */
        search_text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (search_text.getRight() - search_text.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        x_clicked = true;
                        search_text.getText().clear();
                        search_text.clearFocus();
                        ((CategoriesActivity) getContext()).runOnUiThread(() -> clearSearch());
                        hideKeyboard(getActivity());
                        Log.d("x clicked yes", String.valueOf(x_clicked));
                        return true;
                    } else {
                        String temp_currText = search_text.getText().toString();
                        if(temp_currText.equals("")){
                            search_text.setText(" ");
                            search_text.getText().clear();
                        }
                    }
                }

                return false;
            }
        });

        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Log.d("Txt","Before text change");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //If text is detected
                if (x_clicked == false) {
                    if (!s.toString().isEmpty()) {
                        searchEntered = s.toString();
                    } else {
                        searchEntered = "";
                    }
                    new Thread(this::showResults).start();
                }
                else
                    x_clicked = false;
            }

            private void showResults() {
                //Log.d("Str entered", searchEntered);

                //Get list of categories
                Log.d("x clicked", String.valueOf(x_clicked));
                String categories_JSON = apiCall("https://www.themealdb.com/api/json/v1/1/categories.php");
                try {
                    categories = JSONToArray(categories_JSON, "categories", "strCategory");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("CATEGORIES", categories.toString());
                ArrayList<String> categoriesThatMatch = new ArrayList<>();
                ArrayList<String> recipesThatMatch = new ArrayList<>();
                ArrayList<String> idsThatMatch = new ArrayList<>();
                ArrayList<String> imgsThatMatch = new ArrayList<>();
                //Find matching categories
                for (String cat : categories) {
                    if (cat.toLowerCase(Locale.ROOT).contains(searchEntered.toLowerCase(Locale.ROOT))) {
                        categoriesThatMatch.add(cat);
                    }
                }

                //Find matching recipes
                String recipes_URL_start = "https://www.themealdb.com/api/json/v1/1/search.php?s=";
                String recipes_JSON = apiCall(recipes_URL_start + searchEntered);
                try {
                    recipesThatMatch = JSONToArray(recipes_JSON, "meals", "strMeal");
                    idsThatMatch = JSONToArray(recipes_JSON, "meals", "idMeal");
                    imgsThatMatch = JSONToArray(recipes_JSON, "meals", "strMealThumb");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayList<String> finalRecipesThatMatch = recipesThatMatch;
                ArrayList<String> finalIdsThatMatch = idsThatMatch;
                ArrayList<String> finalImgsThatMatch = imgsThatMatch;
                ((CategoriesActivity) getContext()).runOnUiThread(() -> LoadSearch(categoriesThatMatch, finalRecipesThatMatch, finalIdsThatMatch, finalImgsThatMatch));


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
    }

    public void clearSearch() {
        RecyclerView recView = getView().findViewById(R.id.recyclerViewSearchbar);
        recView.setHasFixedSize(true);
        SearchAdapter recAdapter = new SearchAdapter(getContext());
        recView.setAdapter(recAdapter);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        recAdapter.clearAdapter();
    }

    //Sends data to search adapter to display
    public void LoadSearch(ArrayList<String> catList, ArrayList<String> recList, ArrayList<String> idList, ArrayList<String> imgList) {
        ArrayList<String> listToDisplay = new ArrayList<>();
        ArrayList<Boolean> recipeTracker = new ArrayList<>();
        if (!catList.isEmpty()) {
            listToDisplay.add("Categories");
            listToDisplay.addAll(catList);

            recipeTracker.add(false);
            for (String x : catList) {
                recipeTracker.add(false);
            }
        }

        if (!recList.isEmpty()) {
            listToDisplay.add("Recipes");
            listToDisplay.addAll(recList);

            recipeTracker.add(false);
            for (String x : recList) {
                recipeTracker.add(true);
            }
        }

        RecyclerView recView = getView().findViewById(R.id.recyclerViewSearchbar);
        recView.setHasFixedSize(true);
        SearchAdapter recAdapter = new SearchAdapter(getContext(), listToDisplay, recipeTracker, idList, imgList);
        recView.setAdapter(recAdapter);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    //TODO redundant code, move to it's own class at some point
    private String apiCall(String URL_TO_OPEN) {
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
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getJSONObject(i).getString(names));
        }

        return list;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
