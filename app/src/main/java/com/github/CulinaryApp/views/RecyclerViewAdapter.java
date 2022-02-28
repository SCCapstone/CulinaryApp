package com.github.CulinaryApp.views;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.CulinaryApp.R;
import com.github.CulinaryApp.models.Recipe;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

public class RecyclerViewAdapter extends
        RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    /**
     * Adapter's role is to convert an object at a position int a list row item
     * Apaters require a ViewHolder
     * ViewHolder's role is to provide access to all the view w/in each item row
     */

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends
            RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView recipeImage;
        public TextView recipeName;
      //  public Context context;

        public ViewHolder(View itemView) {
            super(itemView);
            recipeImage = (ImageView) itemView.findViewById(R.id.recipe_image);
            recipeName = (TextView) itemView.findViewById(R.id.recipe_name);
       //     context = context;
            // Setup the click listener
            itemView.setOnClickListener(this);
        }

        // handles the row being clicked on
        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            int position = getAbsoluteAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                Recipe recipe = recipeList.get(position);
                // We can access the data within the views
                Toast.makeText(context, recipe.getName(), Toast.LENGTH_SHORT).show();
                Intent startRecipeActivity = new Intent(context, RecipeInstructionsActivity.class);
                startRecipeActivity.putExtra("User clicked on this recipe: ", recipe.getName());
                context.startActivity(startRecipeActivity);
            }
        }
    }



    private static final String TAG = "RecyclerViewAdapter";

    private List<Recipe> recipeList;

    public RecyclerViewAdapter(List<Recipe> aRecipeList) {
        recipeList = aRecipeList;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout._recipes_list_item, parent, false);
        return new ViewHolder(view);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder CALLED");
        // Get the data model based on position in list
        Recipe aRecipeInList = recipeList.get(position);
        // Set item views based on your views and data model
        Glide.with(holder.itemView.getContext()).load(recipeList.get(position).getImage()).apply(RequestOptions.circleCropTransform()).into(holder.recipeImage);
        holder.recipeName.setText(aRecipeInList.getName());
    }

    @Override
    public int getItemCount() {
        if (recipeList == null)
            return 0;
        return recipeList.size();
    }
}