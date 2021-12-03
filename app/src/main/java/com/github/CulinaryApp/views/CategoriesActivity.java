package com.github.CulinaryApp.views;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.github.CulinaryApp.R;
import com.google.firebase.auth.FirebaseAuth;

public class CategoriesActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private static final String TAG = "CategoriesPage";

    public void navigateToRecipePage() {
        Intent intentToStartCategoriesPage = new Intent(this, RecipesActivity.class);
        startActivity(intentToStartCategoriesPage);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Activity Created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        mAuth = FirebaseAuth.getInstance();
        ImageButton imageButton = findViewById(R.id.creolePastaButton);


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
                navigateToRecipePage();
            }
        });
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

}