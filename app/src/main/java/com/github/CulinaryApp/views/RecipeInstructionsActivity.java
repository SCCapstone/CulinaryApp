package com.github.CulinaryApp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.CulinaryApp.R;
import com.github.CulinaryApp.models.Recipe;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class RecipeInstructionsActivity extends AppCompatActivity {

    private static final String KEY_LIKES = "likes";
    private static final String FILENAME_ENCRYPTED_SHARED_PREFS = "secret_shared_prefs";
    private static final String VALUE_DEFAULT_NONE_FOUND = "{}";

    private Recipe currentRecipe;
    private boolean recipeLiked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_instructions);

        ImageButton likeButton = findViewById(R.id.likeButton);
        likeButton.setOnClickListener(toggleLike);

        currentRecipe = null;
        recipeLiked = false;
    }

    @Override
    protected void onStart() {
        super.onStart();

//        Recipe placeHolderRecipe = new Recipe("test2", "111", "022222");
//        Recipe placeHolderRecipe = new Recipe("test1", "000", "111111");
//        this.currentRecipe = placeHolderRecipe; //todo using this as placeholder, this should actually use intents or other means to either:
                                                                // get the id of the recipe user clicked on and build a recipe object
                                                                // OR
                                                                // get a full recipe object thru an Intent. This may require serialization, etc. Somewhat harder, probably saves minimal time

        this.currentRecipe = getCurrentRecipeFromIntent();

        new Thread(this::updateDisplayedRecipe).start();
    }

    private void updateDisplayedRecipe(){
        TextView recipeName = findViewById(R.id.headingRecipeName);
        recipeName.setText(this.currentRecipe.getName());

        String mealJSON = apiCall(this.currentRecipe.getId());

        updateLikeButtonColor();
    }

    private String apiCall(String id){
        //https://www.themealdb.com/api/json/v1/1/lookup.php?i=52772
        final String URL_FIND_BY_ID = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=";

        try {
            HttpsURLConnection connect = (HttpsURLConnection) new URL(URL_FIND_BY_ID+id).openConnection();
            InputStream response = connect.getInputStream();

            return streamToString(response);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "{}";
    }

    private String streamToString(InputStream response) {
        String JSON = "{}";
        try (Scanner scanner = new Scanner(response, StandardCharsets.UTF_8.name())) {
            JSON = scanner.useDelimiter("\\A").next();
        }

        return JSON;
    }

    private Recipe getCurrentRecipeFromIntent(){
        Intent recipeReceived = getIntent();
        if(recipeReceived.getExtras().size() != 3)
            return new Recipe("test1", "000", "111111");

        String name = recipeReceived.getStringExtra(RecyclerViewAdapter.KEY_INTENT_EXTRA_RECIPE_NAME);
        String img = recipeReceived.getStringExtra(RecyclerViewAdapter.KEY_INTENT_EXTRA_RECIPE_IMG);
        String id = recipeReceived.getStringExtra(RecyclerViewAdapter.KEY_INTENT_EXTRA_RECIPE_ID);

        return new Recipe(name, img, id);
    }

    /**
     * Loads old likes as String, converts to JSON, converts the new Like into JSON, appends the new Like, converts the result into a String, and writes it back
     */
    View.OnClickListener toggleLike = view -> {
        final boolean DEBUG_CLEAR_LIKES = false; //for debugging

        //load old likes as string
        SharedPreferences prefs = getSharedPrefs(this);

        String likes = prefs.getString(KEY_LIKES, VALUE_DEFAULT_NONE_FOUND);

        //use old likes and current recipe to generate new likes as string
        if(!this.recipeLiked)
            likes = appendLike(likes, this.getCurrentRecipe());
        else
            likes = removeLike(likes, this.getCurrentRecipe());

        //save likes -with new like appended- as string
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_LIKES, likes);

        editor.apply();

        if(DEBUG_CLEAR_LIKES) { //move around within this method as needed to allow for wiping before loading, after loading but before appending, or after appending but before saving
            editor.clear();
            editor.apply();
        }

        updateLikeButtonColor();
    };

    /**
     * uses value of recipeLiked to determine which color the like button should be
     */
    private void updateLikeButtonColor(){
        this.recipeLiked = isRecipeInLikes(this.currentRecipe);

        ImageButton likeButton = findViewById(R.id.likeButton);
        final int ID_UNLIKED = R.drawable.like_grey_30;
        final int ID_LIKED = R.drawable.like_red_30;

        if(this.recipeLiked)
            likeButton.setImageResource(ID_LIKED);
        else
            likeButton.setImageResource(ID_UNLIKED);

    }

    /**
     * attempts to find if the JSON of all likes includes the current recipe already, meaning the current recipe is liked
     */
    private boolean isRecipeInLikes(Recipe currentRecipe) {
        String likes = getLikes(this);

        //usage of result is unusual here, had to do it bc of what likes.getString returns. It throws an error upon not finding rather than a {} or null, so I kind of forced it to work my way
        String result = "";
        try {

            result = new JSONObject(likes).getJSONObject(currentRecipe.getId()).toString();

        } catch (JSONException e) {
            result = "{}";
        }

        return !result.equals("{}");
    }

    public static String getLikes(Context context){
        return getSharedPrefs(context).getString(KEY_LIKES, VALUE_DEFAULT_NONE_FOUND);
    }

    private static String removeLike(String likes, Recipe currentRecipe) {
        JSONObject likesJSON = null;

        try {

            likesJSON = new JSONObject(likes);
            likesJSON.remove(currentRecipe.getId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return likesJSON.toString();
    }

    private static String appendLike(String oldLikes, Recipe newLike){
        JSONObject likesJSON = null;

        try {
            likesJSON = new JSONObject(oldLikes);

            JSONObject newLikeJSON = Recipe.recipeValuesToJSON(newLike);
            String id = newLike.getId();
            likesJSON.put(id,newLikeJSON);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return likesJSON.toString();
    }


    private void setCurrentRecipe(Recipe currentRecipe){
        this.currentRecipe = currentRecipe;
    }

    private Recipe getCurrentRecipe(){ //todo
        return this.currentRecipe;
    }

    private static SharedPreferences getSharedPrefs(Context context){
        SharedPreferences sharedPreferences = null;
        try {
            MasterKey key = new MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();

            sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    FILENAME_ENCRYPTED_SHARED_PREFS,
                    key,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        return sharedPreferences;
    }


}