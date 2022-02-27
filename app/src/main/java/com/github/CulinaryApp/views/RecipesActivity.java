package com.github.CulinaryApp.views;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.github.CulinaryApp.NavbarFragment;
import com.github.CulinaryApp.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviderKt;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.github.CulinaryApp.models.Recipe;
import com.github.CulinaryApp.viewmodels.RecipesViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

public class RecipesActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private static final String TAG = "RecipesActivity";
    // this activity recieves data from RecipesViewAdapter so it needs identical data structures

    // View component vars
    private RecipesViewModel recipesViewModel;
    private RecyclerView recipesRecyclerView;
    private RecyclerViewAdapter recipesViewAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        Log.d(TAG, "onCreate: started");

        // RJ Code for testing
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("Category");
            Log.d("Category passed",value);
            //The key argument here must match that used in the other activity
        }
        //End of my code


       // getSupportActionBar().setTitle("A Category's Recipes");

        // define viewmodel
        // ViewModelProvider(Context context), get method parameter is the ViewModel pertaining to...
        // ... ViewModel class you want this View to be modelled after
        recipesViewModel = new ViewModelProvider(this).get(RecipesViewModel.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.show();

        // define recycler using container elm in activity_recipes layout file
        recipesRecyclerView = findViewById(R.id.recycler_view_container);
        // Listen for changes & instantiate new LiveData Obj each time ViewModel is updated...
        // ... using ViewModelProvider class member that has async (callback/listener?) that
        // ... attaches to ViewModel's getter

        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipesRecyclerView.setHasFixedSize(true);

        // get recipes through viewModel observer class getRecipesOfACategory()
        recipesViewModel.getRecipesOfACategory().observe(this, recipeList -> {
           progressDialog.dismiss();
           recipesViewAdapter = new RecyclerViewAdapter(recipeList);
           recipesRecyclerView.setAdapter(recipesViewAdapter);
           recipesViewAdapter.notifyDataSetChanged();
        });

        // insert navbar
        getSupportFragmentManager().beginTransaction().add(R.id.Navbar, NavbarFragment.class, null).commit();

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_recipes);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


}