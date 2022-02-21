package com.github.CulinaryApp.views;

import android.content.ClipData;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.view.MenuItemCompat;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


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


        super.onCreate(savedInstanceState); //todo should this be happening after anything? I'm pretty sure this should go before
        setContentView(R.layout.activity_categories);

        redirectToRecipePage();
        mAuth = FirebaseAuth.getInstance();
        ImageButton imageButton = findViewById(R.id.creolePastaButton);

        //insert navbar on activity load
        getSupportFragmentManager().beginTransaction().add(R.id.Navbar, NavbarFragment.class, null).commit();

        //Code for toolbar
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        //displays home button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //This method is what should send to recipes page
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToRecipePage();
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
}
