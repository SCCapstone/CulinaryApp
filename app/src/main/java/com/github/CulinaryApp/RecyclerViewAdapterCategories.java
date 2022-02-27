package com.github.CulinaryApp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.CulinaryApp.views.CategoriesActivity;
import com.github.CulinaryApp.views.LoginActivity;
import com.github.CulinaryApp.views.RecipeInstructionsActivity;
import com.github.CulinaryApp.views.RecipesActivity;

public class RecyclerViewAdapterCategories extends RecyclerView.Adapter<RecyclerViewAdapterCategories.MyViewHolder> {

    private String categories[], recipes1[], recipes2[], recipes3[], recipes4[], images1[], images2[], images3[], images4[];
    private Context context;

    public RecyclerViewAdapterCategories(Context ct, String cat[], String rec1[], String rec2[], String rec3[], String rec4[], String img1[], String img2[], String img3[], String img4[]){
        categories = cat;
        recipes1 = rec1;
        recipes2 = rec2;
        recipes3 = rec3;
        recipes4 = rec4;
        images1 = img1;
        images2 = img2;
        images3 = img3;
        images4 = img4;
        context = ct;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_category, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.categoryT.setText(categories[position]);
        holder.recipeT1.setText(recipes1[position]);
        holder.recipeT2.setText(recipes2[position]);
        holder.recipeT3.setText(recipes3[position]);
        holder.recipeT4.setText(recipes4[position]);
        GlideApp.with(context).load(images1[position]).into(holder.imgI1);
        GlideApp.with(context).load(images2[position]).into(holder.imgI2);
        GlideApp.with(context).load(images3[position]).into(holder.imgI3);
        GlideApp.with(context).load(images4[position]).into(holder.imgI4);

        holder.categoryT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecyclerViewAdapterCategories.this.context, "Detected categories click", Toast.LENGTH_LONG).show();
                Intent newRecipesActivity = new Intent(RecyclerViewAdapterCategories.this.context, RecipesActivity.class);
                newRecipesActivity.putExtra("Category", categories[holder.getAbsoluteAdapterPosition()]);
                newRecipesActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(newRecipesActivity);
            }
        });

        //Suggested Recipe 1
        holder.recipeT1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newRecipesActivity = new Intent(RecyclerViewAdapterCategories.this.context, RecipeInstructionsActivity.class);
                newRecipesActivity.putExtra("Category", recipes1[holder.getAbsoluteAdapterPosition()]);
                newRecipesActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(newRecipesActivity);
            }
        });
        holder.imgI1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newRecipesActivity = new Intent(RecyclerViewAdapterCategories.this.context, RecipeInstructionsActivity.class);
                newRecipesActivity.putExtra("Category", recipes1[holder.getAbsoluteAdapterPosition()]);
                newRecipesActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(newRecipesActivity);
            }
        });

        //Suggested Recipe 2
        holder.recipeT2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newRecipesActivity = new Intent(RecyclerViewAdapterCategories.this.context, RecipeInstructionsActivity.class);
                newRecipesActivity.putExtra("Category", recipes2[holder.getAbsoluteAdapterPosition()]);
                newRecipesActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(newRecipesActivity);
            }
        });
        holder.imgI2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newRecipesActivity = new Intent(RecyclerViewAdapterCategories.this.context, RecipeInstructionsActivity.class);
                newRecipesActivity.putExtra("Category", recipes2[holder.getAbsoluteAdapterPosition()]);
                newRecipesActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(newRecipesActivity);
            }
        });

        //Suggested Recipe 3
        holder.recipeT3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newRecipesActivity = new Intent(RecyclerViewAdapterCategories.this.context, RecipeInstructionsActivity.class);
                newRecipesActivity.putExtra("Category", recipes3[holder.getAbsoluteAdapterPosition()]);
                newRecipesActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(newRecipesActivity);
            }
        });
        holder.imgI3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newRecipesActivity = new Intent(RecyclerViewAdapterCategories.this.context, RecipeInstructionsActivity.class);
                newRecipesActivity.putExtra("Category", recipes3[holder.getAbsoluteAdapterPosition()]);
                newRecipesActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(newRecipesActivity);
            }
        });

        //Suggested Recipe 4
        holder.recipeT4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newRecipesActivity = new Intent(RecyclerViewAdapterCategories.this.context, RecipeInstructionsActivity.class);
                newRecipesActivity.putExtra("Category", recipes4[holder.getAbsoluteAdapterPosition()]);
                newRecipesActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(newRecipesActivity);
            }
        });
        holder.imgI4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newRecipesActivity = new Intent(RecyclerViewAdapterCategories.this.context, RecipeInstructionsActivity.class);
                newRecipesActivity.putExtra("Category", recipes4[holder.getAbsoluteAdapterPosition()]);
                newRecipesActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(newRecipesActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView categoryT;
        TextView recipeT1, recipeT2, recipeT3, recipeT4;
        ImageView imgI1, imgI2, imgI3, imgI4;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryT = itemView.findViewById(R.id.CategoryText);
            recipeT1 = itemView.findViewById(R.id.Recipe1);
            recipeT2 = itemView.findViewById(R.id.Recipe2);
            recipeT3 = itemView.findViewById(R.id.Recipe3);
            recipeT4 = itemView.findViewById(R.id.Recipe4);
            imgI1 = itemView.findViewById(R.id.Recipe_Image1);
            imgI2 = itemView.findViewById(R.id.Recipe_Image2);
            imgI3 = itemView.findViewById(R.id.Recipe_Image3);
            imgI4 = itemView.findViewById(R.id.Recipe_Image4);
        }
    }
}
