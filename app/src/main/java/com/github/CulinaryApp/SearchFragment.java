package com.github.CulinaryApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.fragment.app.Fragment;

import com.github.CulinaryApp.views.CategoriesActivity;

public class SearchFragment extends Fragment {


    public static SearchFragment newInstance(String param1, String param2) {
        return new SearchFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search,container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
