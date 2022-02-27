package com.github.CulinaryApp.views;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.CulinaryApp.NavbarFragment;
import com.github.CulinaryApp.ProfileActivity;
import com.github.CulinaryApp.R;
import com.github.CulinaryApp.RecyclerViewAdapterCategories;
import com.github.CulinaryApp.LifestyleToCategories;
import com.github.CulinaryApp.User;
import com.github.CulinaryApp.SearchAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


public class CategoriesActivity extends AppCompatActivity {
    private Toolbar toolbar;

    //Added by Michael
    EditText search_edit_text;
    SearchAdapter searchAdapter;


    private FirebaseAuth mAuth;
    private static final String TAG = "CategoriesPage";

    private RecyclerView recyclerView;

    public void redirectToRecipePage() {
        Intent intentToStartCategoriesPage = new Intent(this, RecipesActivity.class);
        startActivity(intentToStartCategoriesPage);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        search_edit_text = findViewById(R.id.search_edit_text);

        //Code for toolbar (No Longer Needed)
        //toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //displays home button
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        // TESTING DYNAMIC LOADING
        recyclerView = findViewById(R.id.recyclerView);

        String categories[] = {"Beef"};
        String recipes1[] = {"Recipe 1"};
        String recipes2[] = {"Recipe 2"};
        String recipes3[] = {"Recipe 3"};
        String recipes4[] = {"Recipe 4"};
        String images1[] = {"https://www.themealdb.com//images//category//beef.png"};
        String images2[] = {"https://www.themealdb.com//images//category//beef.png"};
        String images3[] = {"https://www.themealdb.com//images//category//beef.png"};
        String images4[] = {"https://www.themealdb.com//images//category//beef.png"};
        RecyclerViewAdapterCategories recAdapter = new RecyclerViewAdapterCategories(this, categories, recipes1, recipes2, recipes3, recipes4, images1, images2, images3, images4);
        recyclerView.setAdapter(recAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /**
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.fragment_category, null);

        ViewGroup insertPoint = (ViewGroup) findViewById(R.id.Category_Layout_Holder);
        insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ScrollView scrollView = (ScrollView)findViewById(R.id.scroll_view);

        scrollView.getViewTreeObserver()
                .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        if (scrollView.getChildAt(0).getBottom()
                                == (scrollView.getHeight() + scrollView.getScrollY())) {
                            //scroll view is at bottom
                            insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        } else {
                            //scroll view is not at bottom
                        }
                    }
                });**/
        /////////////


        Log.d(TAG, "CATEGORIES_ACTIVITY_CREATED\n");
        /*
         * Michael, not sure what you want the method definition to be like down below where you're
         * gonna put the dynamic page loading functionality so I'm just gonna put this example query here for now
         * that logs all fields for each document our Firebase project's root collection
         */


        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        Log.d("FIRESTORE INSTANCE: ", String.valueOf(firestoreDB));

        ArrayList<String> lifestyles = new ArrayList<>();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users");
        dbRef.child(FirebaseAuth.getInstance().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("Data: ",snapshot.getKey()+", "+snapshot.getValue());
                if(snapshot.getKey().equals("Lifestyle")){
                    ArrayList<String> lifestyles = new ArrayList<String>((ArrayList)snapshot.getValue());
                    ArrayList<String> categories = new ArrayList<String>(getCategories(lifestyles));

                    String[] categoriesArray = new String[categories.size()];
                    categoriesArray = categories.toArray(categoriesArray);
                    String recipes1[] = new String[categoriesArray.length];
                    String recipes2[] = new String[categoriesArray.length];
                    String recipes3[] = new String[categoriesArray.length];
                    String recipes4[] = new String[categoriesArray.length];
                    String images1[] = new String[categoriesArray.length];;
                    String images2[] = new String[categoriesArray.length];;
                    String images3[] = new String[categoriesArray.length];;
                    String images4[] = new String[categoriesArray.length];;

                    //Loop through each category from list in categories collection and get 4 recipes from each
                    for (String cat : categoriesArray){
                        CollectionReference catRef = firestoreDB.collection("CATEGORIES").document(cat).collection("RECIPES");

                        String[] finalCategoriesArray = categoriesArray;
                        catRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int count = 0;
                                    for (QueryDocumentSnapshot aDocInCollection : task.getResult()) {
                                        if(count == 0){
                                            recipes1[arrLength(recipes1)] = (String)aDocInCollection.get("name");
                                            images1[arrLength(images1)] = (String)aDocInCollection.get("image");
                                        }
                                        else if(count == 1){
                                            recipes2[arrLength(recipes2)] = (String)aDocInCollection.get("name");
                                            images2[arrLength(images2)] = (String)aDocInCollection.get("image");
                                        }
                                        else if(count == 2){
                                            recipes3[arrLength(recipes3)] = (String)aDocInCollection.get("name");
                                            images3[arrLength(images3)] = (String)aDocInCollection.get("image");
                                        }
                                        else if(count == 3){
                                            recipes4[arrLength(recipes4)] = (String)aDocInCollection.get("name");
                                            images4[arrLength(images4)] = (String)aDocInCollection.get("image");
                                        }
                                        count += 1;
                                        //Log.d("A DOC: ", "Collection: "+cat+", "+aDocInCollection.getId() + " => " + aDocInCollection.getData() + ", Count: "+count);
                                    }
                                    //If there was less than 4 recipes in the collection, fill with duplicates
                                    if(count < 3){
                                        for(int i = count; i<4; i++){
                                            switch(i) {
                                                case 1:
                                                    recipes2[arrLength(recipes2)] = recipes1[arrLength(recipes1)-1];
                                                    images2[arrLength(images2)] = images1[arrLength(images1)-1];
                                                    break;
                                                case 2:
                                                    recipes3[arrLength(recipes3)] = recipes1[arrLength(recipes1)-1];
                                                    images3[arrLength(images3)] = images1[arrLength(images1)-1];
                                                    break;
                                                case 3:
                                                    recipes4[arrLength(recipes4)] = recipes1[arrLength(recipes1)-1];
                                                    images4[arrLength(images4)] = images1[arrLength(images1)-1];
                                                    break;
                                            }
                                        }
                                    }
                                } else {
                                    Log.d("EXCEPTION: ", String.valueOf(task.getException()));
                                }
                                if(arrLength(recipes1)==recipes1.length){
                                    loadScreen(finalCategoriesArray, recipes1, recipes2, recipes3, recipes4, images1, images2, images3, images4);
                                }
                            }
                        });
                    }

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


        recyclerView = findViewById(R.id.recyclerView);



        mAuth = FirebaseAuth.getInstance();
        //ImageButton imageButton = findViewById(R.id.Recipe_Image1);

        //insert navbar on activity load
        getSupportFragmentManager().beginTransaction().add(R.id.Navbar, NavbarFragment.class, null).commit();

        //Code for toolbar
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        //displays home button
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        //This method is what should send to recipes page
        /**
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToRecipePage();
            }
        });**/

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

    //TODO redirect to recipes pages
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

    //class written by Michael trying to integrate searchable activity
    public ArrayList<String> searchCategories(ArrayList<String> search){
        ArrayList<String> searchCategories = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<String> searchedCategories = searchCategories;
        db.collection("CATEGORIES").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                searchedCategories.add(document.getId().toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return searchedCategories;
    }


    //Class for retrieving categories based on lifestyle preferences
    //This was done by RJ and I have no idea really what I'm doing so feel free to update as you see fit
    public ArrayList<String> getCategories(ArrayList<String> preferences){

        ArrayList<String> categories = new ArrayList<>();

        //Base case no preferences
        if(preferences.isEmpty()){
            //Loops through all categories in collection and adds them to the list
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            ArrayList<String> finalCategories = categories;
            db.collection("CATEGORIES").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for(QueryDocumentSnapshot document : task.getResult()){
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    finalCategories.add(document.getId().toString());
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
            return finalCategories;
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
}
