package com.github.CulinaryApp;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    ArrayList<String> categories;
    ArrayList<String> categoryImageList;
    class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;
        TextView categoryText;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = (ImageView) itemView.findViewById(R.id.Recipe_Image1);
            categoryText = (TextView) itemView.findViewById(R.id.Recipe1);
        }
    }


    public SearchAdapter(Context context, ArrayList<String> categories, ArrayList<String> categoryImageList ){
        this.context = context;
        this.categories = categories;
        this.categoryImageList = categoryImageList;
    }
    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_category,parent,false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.categoryText.setText(categories.get(position));

        Glide.with(context).load(categoryImageList.get(position)).asBitmap().placeholder(R.mipmap.ic_launcher_round.into(holder.categoryImage));
    }


    @Override
    public int getItemCount() {
        return categories.size();
    }
}
