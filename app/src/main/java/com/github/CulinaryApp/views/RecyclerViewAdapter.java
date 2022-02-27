package com.github.CulinaryApp.views;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.CulinaryApp.R;
import com.github.CulinaryApp.models.Recipe;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private List<Recipe> recipeList;

    public RecyclerViewAdapter(List<Recipe> aRecipeList) {
        recipeList = aRecipeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Log.d(TAG, "onBindViewHolder CALLED");
        holder.recipeName.setText(recipeList.get(position).getName());
       // holder.recipeImgUrl.setText(recipeList.get(position).getImgUrl());
        Glide.with(holder.itemView.getContext()).load(recipeList.get(position).getImage()).apply(RequestOptions.circleCropTransform()).into(holder.recipeImgUrl);
       holder.view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Log.d(TAG, "clicked on " + recipeList.get(position).getName());
           }
       });
    }

    @Override
    public int getItemCount() {
        if (recipeList == null)
            return 0;
        return recipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView recipeName;
        ImageView recipeImgUrl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            recipeName = view.findViewById(R.id.recipe_name);
            recipeImgUrl = view.findViewById(R.id.recipe_image);
        }
    }
    }