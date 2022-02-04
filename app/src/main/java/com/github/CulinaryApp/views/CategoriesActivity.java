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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.view.MenuItemCompat;

import com.github.CulinaryApp.ProfileActivity;
import com.github.CulinaryApp.R;
import com.github.CulinaryApp.models.CategoryResourceResponse;
import com.github.CulinaryApp.services.GetCategories;
import com.github.CulinaryApp.viewmodels.Categories;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

//@AndroidEntryPoint
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
/*
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

 */
        OkHttpClient httpClient = new OkHttpClient();
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(Categories.BASE_URL).newBuilder();
        String url = urlBuilder.build().toString();

        Request aRequest = new Request.Builder()
                .url(url)
                .build();
        Response response = new Response.Builder()
                .request(aRequest)
                .build();
        Boolean imConnected = response.isSuccessful();
        if (imConnected)
            Log.d(TAG, "CONNECTED");
        else
            Log.d(TAG, "NOT CONNECTED");
        response.body().close();
       // Call call = CategoryResourceResponse.parseJson()
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionBarProfile:
                Intent goToProfile = new Intent(this, ProfileActivity.class);
                startActivity(goToProfile);

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

}