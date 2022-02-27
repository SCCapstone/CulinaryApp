package com.github.CulinaryApp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<String> categories;
    class SearchViewAdapter extends RecyclerView.ViewHolder( {
        public SearchViewAdapter(@NonNull View itemView) {
            super(itemView);
        }
    }

    )
    public SearchAdapter(Context context, ArrayList<String> categories ){
        this.context = context;
        this.categories = categories;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
