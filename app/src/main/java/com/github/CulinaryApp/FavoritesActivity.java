package com.github.CulinaryApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.compose.ui.text.android.style.PlaceholderSpan;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.CulinaryApp.models.Recipe;
import com.github.CulinaryApp.views.RecipeInstructionsActivity;
import com.github.CulinaryApp.views.RecyclerViewAdapter;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Iterator;
import java.util.LinkedList;


public class FavoritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        //needs to be in onCreate so it doesn't continually repeat add recipes if you return to this activity via the back button
        populateLikedList();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * adds likes into the favorites page
     */
    private void populateLikedList(){
        TextView[] recipeLinks = createLikedRecipeButtons();

        LinearLayoutCompat recipeList = findViewById(R.id.recipeContainer);

        for(TextView recipe : recipeLinks)
            recipeList.addView(recipe);
    }

    /**
     * @return The list of recipes the user has liked in json format String
     */
    private JSONObject getLikesJSON(){
        JSONObject likes = null;
        try {
            likes = new JSONObject(RecipeInstructionsActivity.getLikes(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return likes;
    }

    /**
     * Creates a link to the recipe for each recipe the user has liked
     * @return array of textviews displaying liked recipes
     */
    private TextView[] createLikedRecipeButtons(){
        LinkedList<TextView> recipeLinks = new LinkedList<>();

        JSONObject likes = getLikesJSON();


        for (Iterator<String> it = likes.keys(); it.hasNext(); ) {
            String d = it.next();

            try {
                String name = likes.getJSONObject(d).getString("name");
                String img = likes.getJSONObject(d).getString("image");

                TextView link = new TextView(FavoritesActivity.this);
                link.setText(name);
                link.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));

                int paddingSides = ProfileActivity.getPixelsFromDp(this, 32);
                int paddingVertical = ProfileActivity.getPixelsFromDp(this, 25);
                link.setPadding(paddingSides, paddingVertical, paddingSides, paddingVertical);
                link.setGravity(Gravity.START);
                link.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.transparent)));
                link.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
                link.setTextColor(getResources().getColor(R.color.page_subtext));
//                link.setTypeface(link.getTypeface(), Typeface.BOLD);

                link.setOnClickListener( view -> {
                    Context context = FavoritesActivity.this;

                    Intent startRecipeActivity = new Intent(context, RecipeInstructionsActivity.class);
                    startRecipeActivity.putExtra(RecyclerViewAdapter.KEY_INTENT_EXTRA_RECIPE_ID, d);
                    startRecipeActivity.putExtra(RecyclerViewAdapter.KEY_INTENT_EXTRA_RECIPE_NAME, name);
                    startRecipeActivity.putExtra(RecyclerViewAdapter.KEY_INTENT_EXTRA_RECIPE_IMG, img);
                    context.startActivity(startRecipeActivity);

                });

                //todo put button nav function in here

                recipeLinks.add(link);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return recipeLinks.toArray(new TextView[0]);
    }

}