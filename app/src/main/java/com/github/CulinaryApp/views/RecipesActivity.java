package com.github.CulinaryApp.views;

import android.os.Bundle;

import com.github.CulinaryApp.R;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class RecipesActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    // this activity recieves data from RecipesViewAdapter so it needs identical data structures
    private ArrayList<String> recipeNames = new ArrayList<>();
    private ArrayList<String> recipeImages = new ArrayList<>();
    private static final String TAG = "RecipesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        Log.d(TAG, "onCreate: started");
        initBitmaps();
        /*
        binding = ActivityRecipes2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_recipes);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
         */

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_recipes);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void initBitmaps() {
        recipeImages.add(String.valueOf(R.drawable.creolepasta));
        recipeNames.add("Alfredo Shrimp Pasta");

        recipeImages.add(String.valueOf(R.drawable.jambalaya));
        recipeNames.add("Jambalaya");

        recipeImages.add(String.valueOf(R.drawable.homemade_creole_seasoning_recipe));
        recipeNames.add("Homemade Creole Seasoning Recipe");

        initRecyclerView();
    }

    // Method to set up recycler view - our container xml
    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: called");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecipesViewAdapter recipesAdapter =
                new RecipesViewAdapter(this, recipeNames, recipeImages);
        recyclerView.setAdapter(recipesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}