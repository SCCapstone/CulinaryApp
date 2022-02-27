package com.github.CulinaryApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.compose.ui.text.android.style.PlaceholderSpan;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;

import com.github.CulinaryApp.models.Recipe;
import com.github.CulinaryApp.views.RecipeInstructionsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;


public class FavoritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
    }

    @Override
    protected void onStart() {
        super.onStart();

        populateLikedList();
    }

    private void populateLikedList(){
        Button[] recipeLinks = createLikedRecipeButtons();

        LinearLayoutCompat recipeList = findViewById(R.id.recipeContainer);

        for(Button recipe : recipeLinks)
            recipeList.addView(recipe);
    }

    private JSONObject getLikesJSON(){
        JSONObject likes = null;
        try {
            likes = new JSONObject(RecipeInstructionsActivity.getLikes(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return likes;
    }

    private Button[] createLikedRecipeButtons(){
        LinkedList<Button> recipeLinks = new LinkedList<>();

        JSONObject likes = getLikesJSON();

        //for each liked item create a button w the recipe name
        //this uses a pre existing button so as to copy its styling
        Button template = findViewById(R.id.recipeButtonTemplate);


        for (Iterator<String> it = likes.keys(); it.hasNext(); ) {
            String d = it.next();
            String key = likes.keys().next();

            try {
                String name = likes.getJSONObject(key).getString("name");

                Button link = new Button(FavoritesActivity.this);
                link.setText(name);
                link.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));

                int paddingStart = ProfileActivity.getPixelsFromDp(this, 32);
                int paddingVertical = ProfileActivity.getPixelsFromDp(this, 25);
                link.setPadding(paddingStart, paddingVertical, 0, paddingVertical);
                link.setGravity(Gravity.START);
                link.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                link.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

                //todo put button nav function in here

                recipeLinks.add(link);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return recipeLinks.toArray(new Button[0]);
    }

}