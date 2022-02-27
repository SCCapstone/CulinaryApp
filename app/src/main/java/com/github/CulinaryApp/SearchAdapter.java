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

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    ArrayList<String> categories;
    ArrayList<Boolean> recipes; //True is recipe, false if category
    class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView categoryText;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryText = (TextView) itemView.findViewById(R.id.search_result);
        }
    }


    public SearchAdapter(Context context, ArrayList<String> categories, ArrayList<Boolean> recipes){
        this.context = context;
        this.categories = categories;
        this.recipes = recipes;
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

        int paddingDp = 15;
        float density = context.getResources().getDisplayMetrics().density;
        int paddingPixel = (int)(paddingDp * density);

        if(text.equals("Categories") || text.equals("Recipes")) {
            holder.categoryText.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            holder.categoryText.setPadding(paddingPixel,0,0,0);

            holder.categoryText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recipes.get(holder.getAbsoluteAdapterPosition())==false){//Category was clicked
                        Intent newRecipesActivity = new Intent(SearchAdapter.this.context, RecipesActivity.class);
                        newRecipesActivity.putExtra("Category", text);
                        newRecipesActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(newRecipesActivity);
                    } else { //Recipe was clicked
                        //TODO If possible also send category of recipe
                        Intent newRecipesActivity = new Intent(SearchAdapter.this.context, RecipeInstructionsActivity.class);
                        //newRecipesActivity.putExtra("Category", categories[holder.getAbsoluteAdapterPosition()]);
                        newRecipesActivity.putExtra("Recipe", text);
                        newRecipesActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(newRecipesActivity);
                    }
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return categories.size();
    }
}
