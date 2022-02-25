package com.github.CulinaryApp.views;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.view.MenuItemCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.CulinaryApp.NavbarFragment;
import com.github.CulinaryApp.ProfileActivity;
import com.github.CulinaryApp.R;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class CategoriesActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private FirebaseAuth mAuth;
    private static final String TAG = "CategoriesPage";

    public void redirectToRecipePage() {
        Intent intentToStartCategoriesPage = new Intent(this, RecipesActivity.class);
        startActivity(intentToStartCategoriesPage);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);



        // TESTING DYNAMIC LOADING
      //  LinearLayout newLayout = (LinearLayout)findViewById(R.id.Category_Layout);
        LinearLayout currView = (LinearLayout)findViewById(R.id.Category_Layout_Holder);

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
                });



        Log.d(TAG, "CATEGORIES_ACTIVITY_CREATED\n");
        /*
         * Michael, not sure what you want the method definition to be like down below where you're
         * gonna put the dynamic page loading functionality so I'm just gonna put this example query here for now
         * that logs all fields for each document our Firebase project's root collection
         */



        FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
        Log.d("FIRESTORE INSTANCE: ", String.valueOf(firestoreDB));

        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // This is more or less example code for how to grab and load both strings and images
        // into text and image views from the firestore db
        TextView firstHeader = findViewById(R.id.Recipe1);
        firstHeader.setText(firestoreDB.collection("CATEGORIES/").document("Beef/").getId().toString());
        ImageView image = findViewById(R.id.Recipe_Image1);
        String categoryName;
        firestoreDB.collection("CATEGORIES/")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot aCategory : task.getResult()) {
                                Log.d("A DOC: ", aCategory.getId() + " => " + aCategory.getData());
                                Log.d(TAG, aCategory.get("image").toString());
                                loadImage(image, (String) aCategory.get("image"));
                                Log.d(TAG, aCategory.get("image").toString() + " was loaded into view");
                            }
                        }
                        else {
                            Log.d("EXCEPTION: ", String.valueOf(task.getException()));
                        }
                    }
                    });
                             /*   if (aCategory.get("image").getClass() == StorageReference.class) {
                                    loadImage(image, (StorageReference) aCategory.get("image"));
                                }
                                else {
                                    loadImage(image, (String) aCategory.get("image"));
                                }
                }
                            }
                              */

        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


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
             //   .apply(RequestOptions.circleCropTransform())
                .into(image);
    }
}
