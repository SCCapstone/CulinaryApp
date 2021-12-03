package com.github.CulinaryApp.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.CulinaryApp.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.BreakIterator;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipesViewAdapter extends RecyclerView.Adapter<RecipesViewAdapter.ViewHolder> {

    /*  This class adapts the layouts for the individual recipes to container layout -
     *    Container for RecyclerView is activity_recipes.xml  */

    private static final String TAG = "RecylcerViewAdapter";

    // Variables needed for RecyclerView
    private ArrayList<String> recipeNames = new ArrayList<>();
    private ArrayList<String> recipeImages = new ArrayList<>();
    private Context contextRecipesAdapter;


    public RecipesViewAdapter(Context contextRecipesAdapter, ArrayList<String> recipeNames, ArrayList<String> recipeImages) {
        contextRecipesAdapter = contextRecipesAdapter;
        recipeNames = recipeNames;
        recipeImages = recipeImages;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // this method "inflates" the View
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;  // this recycles the ViewHolder
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // This method is gonna change based on what our layouts look like
        Log.d(TAG, "onBindViewHolder: called"); // method is called everytime an item is added to list
        // get images
        Glide.with(contextRecipesAdapter)
                .asBitmap()
                .load(recipeImages.get(position)) // load recipeImages of ViewHolder...
                .into(holder.imageOfRecipe);
        // set name of recipe for ViewHolder
        holder.nameOfRecipe.setText(recipeNames.get(position));
        // attach onClickListener to each item in list
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // log recipe name whose image was clicked on
                Log.d(TAG, "onClick: clicked: " + recipeImages.get(position));
                // display pop up to app screen of recipe name whose img was clicked on
                Toast.makeText(contextRecipesAdapter,
                        recipeImages.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        // return value from size method call on array list
        return recipeImages.size(); // tells adapter how many items are in list
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //public BreakIterator nameOfRecipe;

        CircleImageView imageOfRecipe;
        TextView nameOfRecipe;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            // attach widgets to their xml id's
            imageOfRecipe = itemView.findViewById(R.id.recipe_image);
            nameOfRecipe = itemView.findViewById(R.id.recipe_name);
            parentLayout = itemView.findViewById(R.id.individual_recipe_item_layout);
        }
    }
}