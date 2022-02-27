package com.github.CulinaryApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
