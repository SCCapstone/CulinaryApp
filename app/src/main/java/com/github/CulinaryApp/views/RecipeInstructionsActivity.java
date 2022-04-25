package com.github.CulinaryApp.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.CulinaryApp.FavoritesActivity;
import com.github.CulinaryApp.R;
import com.github.CulinaryApp.models.Recipe;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class RecipeInstructionsActivity extends AppCompatActivity {
    private static final String KEY_LIKES = "likes";
    private static final String FILENAME_ENCRYPTED_SHARED_PREFS = "secret_shared_prefs";
    private static final String VALUE_DEFAULT_NONE_FOUND = "{}";
    public static String URL_FIND_BY_ID = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=";

    private Recipe currentRecipe;
    private boolean recipeLiked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_instructions);

        findViewById(R.id.likeButton).setOnClickListener(toggleLike);
        findViewById(R.id.shareButton).setOnClickListener(shareListener);

        //default values for instance variables
        currentRecipe = null;
        recipeLiked = false;
    }

    @Override
    protected void onStart() {
        super.onStart();


        //retrieves the values of the recipe the user clicked
        this.currentRecipe = getCurrentRecipeFromIntent();

        //asyc call to get and set the recipe's instructions, img, and ingredients using mealdb api
        new Thread(this::updateDisplayedRecipe).start();
    }

    //Forms a bitmap object from any .png stored on the web
    private Bitmap getBmpFromURL(String url){
        try {
            InputStream bitmapStream = new URL(url).openConnection().getInputStream();
            return BitmapFactory.decodeStream(bitmapStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //starts android's sharing interface. shares the current recipe in string form
    View.OnClickListener shareListener = view -> {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, currentRecipe.toString());
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    };

    //retrieves an item from a JSON array using a default key to get the array and a given key to get the item
    //index is 0 because structure wise there is only ever one array named meals
    public static Object getItemFromJSON(JSONObject recipe, String key) throws JSONException{
        final String KEY_OBJECT = "meals";
        final int INDEX = 0;

        return recipe.getJSONArray(KEY_OBJECT).getJSONObject(INDEX).get(key);

    }

    //Takes parallel lists of texts and views, must be the same size, and iteratively displays one text onto every Textview by index
    private void setRecipeContentTexts(String[] texts, TextView... views){
        for(int i=0;i<views.length;i++) {
            int index = i;
            //this method runs within an async thread, meaning it cannot make changes to what is displayed. runOnUiThread method allows access to layout edits
            runOnUiThread(() -> views[index].setText(texts[index]));
        }
    }

    /**
     * replaces the default values from before a recipe has loaded with the values of the intended recipe
     */
    private void updateDisplayedRecipe(){
        TextView recipeName = findViewById(R.id.headingRecipeName);
        recipeName.setText(this.currentRecipe.getName());

        ImageView recipeImg = findViewById(R.id.recipeInstrImg);

        //getBmpFromURL is a network call, so it must be off the main thread
        //however, that bmp is immediately used to update layout, so a uiThread call is required within this thread to avoid unnecessary complication
        new Thread( () -> {
            Bitmap bmp = getBmpFromURL(currentRecipe.getImage());
            runOnUiThread(() -> recipeImg.setImageBitmap(bmp));
        }).start();

        setRecipeContentTexts(currentRecipe.getRecipeContentsById(), findViewById(R.id.instructions), findViewById(R.id.ingredients));

        updateLikeButtonColor();
    }

    /**
     * sends an HTTP GET request to the mealdb api
     * api endpoint: lookup meal by id
     * @param value the key of the api endpoint. either id or name
     * @param url the base url defining the type of lookup
     * @return the result of the request as a JSON formatted string
     */
    public static String apiCall(String value, String url){
        //example https://www.themealdb.com/api/json/v1/1/lookup.php?i=52772

        try {
            HttpsURLConnection connect = (HttpsURLConnection) new URL(url+value).openConnection();
            InputStream response = connect.getInputStream();

            return streamToString(response);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "{}";
    }

    /**
     * converts an inputstream into a string
     * @param response the inputstream resulting from an HTTPsUrlConnection
     * @return the JSON response of an API call in string form
     */
    private static String streamToString(InputStream response) {
        String JsonResponse = "{}";
        try (Scanner scanner = new Scanner(response, StandardCharsets.UTF_8.name())) {
            JsonResponse = scanner.useDelimiter("\\A").next();
        }

        return JsonResponse;
    }

    /**
     * Retrieves the individual details of the recipe the user clicked on either on the categories page or the recipes outer page
     * @return the new Recipe constructed from the Intent send to this activity
     */
    private Recipe getCurrentRecipeFromIntent(){
        Intent recipeReceived = getIntent();
        if(recipeReceived.getExtras() == null || recipeReceived.getExtras().size() != 3)
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

    /**
     * Gets the likes from sharedprefs
     * @param context
     * @return the recipes a user liked
     */
    public static String getLikes(Context context){
        return getSharedPrefs(context).getString(KEY_LIKES, VALUE_DEFAULT_NONE_FOUND);
    }

    /**
     * removes a recipe from the list of likes
     * @param likes likes as JSON format string
     * @param currentRecipe recipe being displayed
     * @return likes as JSON format string without the recipe that was unliked
     */
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

    /**
     * adds a recipe to the list of likes
     * @param oldLikes the list previous to pressing the like button
     * @param newLike the recipe to add to the list
     * @return the list of likes including the new recipe as a JSON format string
     */
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

    /**
     * returns the recipe being displayed
     * @return the recipe the user clicked on
     */
    private Recipe getCurrentRecipe(){ //todo
        return this.currentRecipe;
    }

    /**
     * returns the sharedpreferences
     * @param context
     * @return sharedprefs object
     */
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