package com.github.CulinaryApp.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.CulinaryApp.GlideApp;
import com.github.CulinaryApp.NavbarFragment;
import com.github.CulinaryApp.R;
import com.github.CulinaryApp.RecyclerViewAdapterCategories;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;


public class CategoriesActivity extends AppCompatActivity {
    private Toolbar toolbar;

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
        firestoreDB.collection("CATEGORIES/")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot aDocInCollection : task.getResult()) {
                                Log.d("A DOC: ", aDocInCollection.getId() + " => " + aDocInCollection.getData());
                        }
                    } else {
                       Log.d("EXCEPTION: ", String.valueOf(task.getException()));
                    }
                }
                });

        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // This is more or less example code for how to grab and load both strings and images
        // into text and image views from the firestore db
        /**
        TextView firstHeader = findViewById(R.id.Recipe1);
        firstHeader.setText(firestoreDB.collection("CATEGORIES/").document("Beef").getId());
        ImageView image = findViewById(R.id.Recipe_Image1);
        DocumentReference docRef = firestoreDB.collection("CATEGORIES/").document("Beef/").collection("RECIPES").document("Beef And Oyster Pie");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if(document.get("image").getClass() == StorageReference.class)
                            loadImage(image, (StorageReference) document.get("image"));
                        else
                            loadImage(image, (String) document.get("image"));
                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });**/
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        redirectToRecipePage();
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
        GlideApp.with(this /* context */)
                .load(ref)
                .into(image);
    }
    public void loadImage(ImageView image, String url) {
        GlideApp.with(this /* context */)
                .load(url)
                .into(image);
    }
}
