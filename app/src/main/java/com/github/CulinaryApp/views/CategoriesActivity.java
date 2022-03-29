package com.github.CulinaryApp.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.CulinaryApp.R;
import com.github.CulinaryApp.RecyclerViewAdapterCategories;
import com.github.CulinaryApp.LifestyleToCategories;
import com.github.CulinaryApp.RecipeRecommendationEngine;
import com.github.CulinaryApp.models.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;


public class CategoriesActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private FirebaseAuth mAuth;
    private static final String TAG = "CategoriesPage";

    private RecyclerView recyclerView;

    private ArrayList<String> categories = new ArrayList<>();

    public void redirectToRecipePage() {
        Intent intentToStartCategoriesPage = new Intent(this, RecipesActivity.class);
        startActivity(intentToStartCategoriesPage);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        /** Figure out how to add padding so part of the screen isn't cut off
        View v = findViewById(R.id.recyclerView);
        LayoutInflater li = getLayoutInflater();
        final View textEntrySB = li.inflate(R.layout.fragment_searchbar, null);
        final View homeButton = li.inflate(R.layout.fragment_navbar, null);
        v.setPadding(0, textEntrySB.findViewById(R.id.edit_search).getHeight(),0,homeButton.findViewById(R.id.toolbarSearch).getHeight());
         **/
        Log.d(TAG, "CATEGORIES_ACTIVITY_CREATED\n");
        /*
         * Michael, not sure what you want the method definition to be like down below where you're
         * gonna put the dynamic page loading functionality so I'm just gonna put this example query here for now
         * that logs all fields for each document our Firebase project's root collection
         */

        recyclerView = findViewById(R.id.recyclerView);


        mAuth = FirebaseAuth.getInstance();
        //ImageButton imageButton = findViewById(R.id.Recipe_Image1);

        //insert navbar on activity load
        // getSupportFragmentManager().beginTransaction().add(R.id.Navbar, NavbarFragment.class, null).commit();
        //getSupportFragmentManager().beginTransaction().add(R.id.Searchbar, SearchbarFragment.class, null).commit();

        //Code for toolbar
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        //displays home button
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onStart(){
        super.onStart();

        new Thread(this::updateDisplay).start();

    }

    private void updateDisplay(){
        final ArrayList<String>[] lifestyles = new ArrayList[]{new ArrayList<>()};

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users");
        dbRef.child(FirebaseAuth.getInstance().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("Data: ", snapshot.getKey() + ", " + snapshot.getValue());
                if (snapshot.getKey().equals("Lifestyle")) {
                    lifestyles[0] = (ArrayList) snapshot.getValue();
                    try {
                        categories = getCategories(lifestyles[0]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new Thread(this::setScreen).start();

                }
            }

            private void setScreen () {
                Log.d("CATEGORIES", categories.toString());
                final String url_cat_start = "https://www.themealdb.com/api/json/v1/1/filter.php?c=";
                final String url_id_lookup_start = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=";
                ArrayList<String> mealIDs = new ArrayList<String>();

                String recipes1[] = new String[categories.size()];
                String recipes2[] = new String[categories.size()];
                String recipes3[] = new String[categories.size()];
                String recipes4[] = new String[categories.size()];
                String images1[] = new String[categories.size()];
                String images2[] = new String[categories.size()];
                String images3[] = new String[categories.size()];
                String images4[] = new String[categories.size()];

                //Loop through all categories and grab 4 meals out of each
                int counter = 0;
                for (String cat : categories) {
                    String category_JSON = apiCall(url_cat_start + cat);
                    //TODO * THA PLAN BABY
                    //TODO * For each category get a list of meal ids  DONE
                    //TODO * For each mealID search up that specific meal, create recipe object with results  DONE
                    //TODO * Map each recipe id to its object and its score DONE
                    //TODO * Pass recipe object into /MAGIC RECIPE SCORER ALGORITHM/ DONE
                    //TODO * Update scores -> Display 4 highest scoring recipes DONECa
                    //TODO * Actually design magic scoring alogirthm

                    try {
                        //Get a list of meal IDs
                        mealIDs = JSONToArray(category_JSON, "meals", "idMeal");


                        //Search each meal -> create recipe object
                        ArrayList<Recipe> recipes_list = new ArrayList<Recipe>();
                        for (String id : mealIDs) {
                            String meal_JSON = apiCall(url_id_lookup_start + id);
                            //There should only be a single result for each as we're looking up by ID
                            String name = JSONToArray(meal_JSON, "meals", "strMeal").get(0);
                            String image = JSONToArray(meal_JSON, "meals", "strMealThumb").get(0);
                            ArrayList<String> ingredients = getListFromMealDB(meal_JSON, "strIngredient");
                            ArrayList<String> measurements = getListFromMealDB(meal_JSON, "strMeasure");
                            recipes_list.add(new Recipe(name, image, id, ingredients, measurements));
                        }
                        Log.d("CATEGORIES", "Recipes list for " + cat + " created");

                        //Pass recipes into scoring algorithm
                        //Values are sorted largest score first already when returned
                        Map<Recipe, Integer> recipesMap = new HashMap();
                        recipesMap = RecipeRecommendationEngine.scoreRecipes(recipes_list, lifestyles[0]);

                        int iterator = 0;
                        loop: for(Map.Entry<Recipe, Integer> entry : recipesMap.entrySet()){
                            switch(iterator){
                                case 0:
                                    recipes1[counter] = entry.getKey().getName();
                                    images1[counter] = entry.getKey().getImage();
                                    break;
                                case 1:
                                    recipes2[counter] = entry.getKey().getName();
                                    images2[counter] = entry.getKey().getImage();
                                    break;
                                case 2:
                                    recipes3[counter] = entry.getKey().getName();
                                    images3[counter] = entry.getKey().getImage();
                                    break;
                                case 3:
                                    recipes4[counter] = entry.getKey().getName();
                                    images4[counter] = entry.getKey().getImage();
                                    break;
                                case 4:
                                    break loop;
                            }
                            iterator++;
                        }
                        exit_loop: ;
                        //Check if there was a total of 4 recipes in category
                        //If not, repeat 1st recipe as many times as necessary
                        switch(iterator){
                            case 1:
                                Map.Entry<Recipe,Integer> entry = recipesMap.entrySet().iterator().next();
                                recipes2[counter] = entry.getKey().getName();
                                recipes3[counter] = entry.getKey().getName();
                                recipes4[counter] = entry.getKey().getName();
                                images2[counter] = entry.getKey().getImage();
                                images3[counter] = entry.getKey().getImage();
                                images4[counter] = entry.getKey().getImage();
                                break;
                            case 2: //Java doesn't handle the declaration of entry in multiple cases well, hence the renaming
                                Map.Entry<Recipe,Integer> entry2 = recipesMap.entrySet().iterator().next();
                                recipes3[counter] = entry2.getKey().getName();
                                recipes4[counter] = entry2.getKey().getName();
                                images3[counter] = entry2.getKey().getImage();
                                images4[counter] = entry2.getKey().getImage();
                                break;
                            case 3:
                                Map.Entry<Recipe,Integer> entry3 = recipesMap.entrySet().iterator().next();
                                recipes4[counter] = entry3.getKey().getName();
                                images4[counter] = entry3.getKey().getImage();
                                break;
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                     counter++;
                }
                //Load screen with data once arrays are populated
                Log.d("PROGRESS", "Attempting to load categories screen");
                String[] categoriesArray = new String[categories.size()];
                categoriesArray = categories.toArray(categoriesArray);
                String[] finalCategoriesArray = categories.toArray(categoriesArray);
                runOnUiThread(() -> loadScreen(finalCategoriesArray, recipes1, recipes2, recipes3, recipes4, images1, images2, images3, images4));

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.actionBarProfile:
//                Intent goToProfile = new Intent(this, ProfileActivity.class);
//                startActivity(goToProfile);

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return true;
    }
    /*
    This is where the dynamic page loading functionality will go
    //need a way to diferentiate between users
    Query userSuggestion = databaseReference.child("recipes").child(User ID definer).orderByChild("Suggested");
    */

    //TODO redirect to recipes pagesW
    public void redirectToRecipe(View view){

    }

    public void loadImage(ImageView image, StorageReference ref) {
        Glide.with(this /* context */)
                .load(ref)
                .into(image);
    }
    public void loadImage(ImageView image, String url) {
        Glide.with(this /* context */)
                .load(url)
                .into(image);
    }

    //Class for retrieving categories based on lifestyle preferences
    //This was done by RJ and I have no idea really what I'm doing so feel free to update as you see fit
    public ArrayList<String> getCategories(ArrayList<String> preferences) throws JSONException {

        ArrayList<String> categories = new ArrayList<>();

        //Base case no preferences
        if(preferences.isEmpty()){
            Log.d("Get Categories","Preference list is empty");
            String categories_JSON = apiCall("https://www.themealdb.com/api/json/v1/1/categories.php");
            categories = JSONToArray(categories_JSON,"categories","strCategory");
        }

        //Loop through preferences and add return the most limiting list of categories
        for(int i = 0; i<preferences.size(); i++){
            String pref = preferences.get(i);
            switch(pref){
                case "Athletic":
                    if(categories.isEmpty() || LifestyleToCategories.Athletic().length < categories.size())
                        categories = new ArrayList<String>(Arrays.asList(LifestyleToCategories.Athletic()));
                    break;
                case "Vegan":
                    if(categories.isEmpty() || LifestyleToCategories.Vegan().length < categories.size())
                        categories = new ArrayList<String>(Arrays.asList(LifestyleToCategories.Vegan()));
                    break;
                case "Vegetarian":
                    if(categories.isEmpty() || LifestyleToCategories.Vegetarian().length < categories.size())
                        categories = new ArrayList<String>(Arrays.asList(LifestyleToCategories.Vegetarian()));
                    break;
                case "Mediterranean":
                    if(categories.isEmpty() || LifestyleToCategories.Mediterranean().length < categories.size())
                        categories = new ArrayList<String>(Arrays.asList(LifestyleToCategories.Mediterranean()));
                    break;
                case "Ketogenic":
                    if(categories.isEmpty() || LifestyleToCategories.Ketogenic().length < categories.size())
                        categories = new ArrayList<String>(Arrays.asList(LifestyleToCategories.Ketogenic()));
                    break;
                case "Flexitarian":
                    if(categories.isEmpty() || LifestyleToCategories.Flexitarian().length < categories.size())
                        categories = new ArrayList<String>(Arrays.asList(LifestyleToCategories.Flexitarian()));
                    break;
            }
        }
        //Log.d("Return",categories.toString());
        return categories;

    }

    //Returns amount of array in use
    public static int arrLength(Object[] array){
        int counter = 0;
        for (int i = 0; i < array.length; i ++) {
            if (array[i] != null)
                counter++;
        }
        return counter;
    }

    public void loadScreen(String[] categoriesArray, String[] recipes1, String[] recipes2, String[] recipes3, String[] recipes4, String[] images1,String[] images2, String[] images3, String[] images4){
        recyclerView = findViewById(R.id.recyclerView);
        RecyclerViewAdapterCategories recAdapter = new RecyclerViewAdapterCategories(getApplicationContext(), categoriesArray, recipes1, recipes2, recipes3, recipes4, images1, images2, images3, images4);
        recyclerView.setAdapter(recAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

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

    /**
     * Ingredients list in MealDB goes out 20 spots everytime (strIngredient1 - strIngredient1)
     * If there is no value it'll either be empty or list as "null"
     * We want to filter those out and return the array
     * Start is for start of string value to look for (strIngredient or strMeasure)
     **/
    private ArrayList<String> getListFromMealDB(String meals_JSON, String start){
        ArrayList<String> listOfElems = new ArrayList<String>();
        for(int i = 1; i <= 20; i++){
            try {
                String elem = JSONToArray(meals_JSON, "meals", start+String.valueOf(i)).get(0);
                if(!elem.isEmpty() && !elem.equalsIgnoreCase("null")){
                    listOfElems.add(elem);
                } else if (start.equals("strMeasure")){
                    listOfElems.add("None"); //Some ingredients list are shorter than recipes list
                    // ^ Not a great workaround but it works for now I guess
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return listOfElems;
    }

    /**
    private ArrayList<String> getLifeStyles(){
        final ArrayList<String>[] lifestyles = new ArrayList[]{new ArrayList<String>()};

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users");
        dbRef.child(FirebaseAuth.getInstance().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("Data: ", snapshot.getKey() + ", " + snapshot.getValue());
                if (snapshot.getKey().equals("Lifestyle")) {
                    lifestyles[0] = (ArrayList) snapshot.getValue();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }**/
}