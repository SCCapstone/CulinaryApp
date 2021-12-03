package com.github.CulinaryApp.views;

import com.bumptech.glide.Glide;
import com.github.CulinaryApp.R;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    /*  This class adapts the layouts for the individual recipes to container layout -
     *    Container for RecyclerView is activity_recipes.xml  */

    private static final String TAG = "RecylcerViewAdapter";

    // Variables needed for RecyclerView
    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private Context mContext;

    /* Initialize the dataset of the Adapter */

    /*
    public RecyclerViewAdapter(ArrayList<String> imageNames, ArrayList<String> images, Context context) {
        mImageNames = imageNames;
        mImages = images;
        mContext = context;
    }
    */

    public RecyclerViewAdapter(RecipesActivity recipesActivity, ArrayList<String> recipeNames, ArrayList<String> recipeImages) {
        mImageNames = recipeNames;
        mImages = recipeImages;
        mContext = recipesActivity;
    }

    /* Create new views (invoked by the layout manager) */
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
        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position)) // load recipeImages of ViewHolder...
                .into(holder.imageOfRecipe);
        // set name of recipe for ViewHolder
        holder.nameOfRecipe.setText(mImageNames.get(position));
        // attach onClickListener to each item in list
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // log recipe name whose image was clicked on
                Log.d(TAG, "onClick: clicked: " + mImages.get(position));
                // display pop up to app screen of recipe name whose img was clicked on
                Toast.makeText(mContext,
                        mImageNames.get(position), Toast.LENGTH_SHORT).show();
               // Intent intent = new Intent(RecyclerViewAdapter.this, RecipeInstructionsActivity.class);
                Intent intent = new Intent(mContext, RecipeInstructionsActivity.class);
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra("Recipe Chosen:", mImageNames.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // return value from size method call on array list
        return mImageNames.size(); // tells adapter how many items are in list
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        /* Provide a reference to the type of view you're using */

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
        public CircleImageView getCircleImageView() {
            return imageOfRecipe;
        }
        public TextView getTextView() {
            return nameOfRecipe;
        }
        public RelativeLayout getParentLayout() {
            return parentLayout;
        }
    }


}