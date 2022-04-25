package com.github.CulinaryApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.CulinaryApp.views.RecipeInstructionsActivity;
import com.github.CulinaryApp.views.RecipesActivity;
import com.github.CulinaryApp.views.RecyclerViewAdapter;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    ArrayList<String> categories;
    ArrayList<Boolean> recipes; //True is recipe, false if category
    ArrayList<String> ids;
    ArrayList<String> imgs;

    class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView categoryText;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryText = (TextView) itemView.findViewById(R.id.search_result);
        }
    }


    public SearchAdapter(Context context, ArrayList<String> categories, ArrayList<Boolean> recipes, ArrayList<String> ids, ArrayList<String> imgs){
        this.context = context;
        this.categories = categories;
        this.recipes = recipes;
        this.ids = ids;
        this.imgs = imgs;
    }

    public SearchAdapter(Context context){

    }

    public void clearAdapter(){
        if(categories != null) {
            categories.clear();
        }
        if(recipes != null) {
            recipes.clear();
        }
        if(ids != null) {
            ids.clear();
        }
        if(imgs != null) {
            imgs.clear();
        }
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_search_result,parent,false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        String text = categories.get(position);
        holder.categoryText.setText(categories.get(position));

        //Padding from left of screen
        int paddingDp = 15;
        float density = context.getResources().getDisplayMetrics().density;
        int paddingPixel = (int)(paddingDp * density);

        //Padding from top and bottom of screen
        int padding_top_and_bottom_Dp = 5;
        int padding_top_and_bottom_Pixel = (int)(padding_top_and_bottom_Dp * density);

        if(text.equals("Categories") || text.equals("Recipes")) {
            holder.categoryText.setTypeface(Typeface.DEFAULT_BOLD);
            holder.categoryText.setPadding(0,0,0,0);
        } else {
            holder.categoryText.setPadding(paddingPixel,padding_top_and_bottom_Pixel,0,padding_top_and_bottom_Pixel);
            holder.categoryText.setTypeface(Typeface.DEFAULT);
            holder.categoryText.setOnClickListener(v -> {
                if(!recipes.get(holder.getAbsoluteAdapterPosition())){
                    //Category was clicked
                    Intent newRecipesActivity = new Intent(SearchAdapter.this.context, RecipesActivity.class);
                    newRecipesActivity.putExtra("Category", text);
                    newRecipesActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(newRecipesActivity);
                } else {
                    //Recipe was clicked
                    Intent newRecipesActivity = new Intent(SearchAdapter.this.context, RecipeInstructionsActivity.class);
                    //newRecipesActivity.putExtra("Category", categories[holder.getAbsoluteAdapterPosition()]);
                    newRecipesActivity.putExtra(RecyclerViewAdapter.KEY_INTENT_EXTRA_RECIPE_ID, ids.get(position-(categories.size()-ids.size())));
                    newRecipesActivity.putExtra(RecyclerViewAdapter.KEY_INTENT_EXTRA_RECIPE_NAME, text);
                    newRecipesActivity.putExtra(RecyclerViewAdapter.KEY_INTENT_EXTRA_RECIPE_IMG, imgs.get(position-(categories.size()-imgs.size())));


                    newRecipesActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(newRecipesActivity);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        if(categories != null) {
            return categories.size();
        }
        else
            return 0;
    }
}
