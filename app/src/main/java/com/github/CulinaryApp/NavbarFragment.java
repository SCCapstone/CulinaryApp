package com.github.CulinaryApp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.CulinaryApp.views.CategoriesActivity;

public class NavbarFragment extends Fragment {

    public NavbarFragment() {
        //todo delete if unnecessary
    }

    public static NavbarFragment newInstance(String param1, String param2) {
        return new NavbarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navbar, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Button clipboardButton = getView().findViewById(R.id.toolbarClip);
        Button searchButton = getView().findViewById(R.id.toolbarSearch);
        Button profileButton = getView().findViewById(R.id.toolbarProfile);
        Button trendingButton = getView().findViewById(R.id.toolbarTrending);

        clipboardButton.setOnClickListener(toggleClipboard);
        profileButton.setOnClickListener(navToProf);
        searchButton.setOnClickListener(navToHome);
//        trendingButton.setOnClickListener(toggleTrending);
    }

    private View.OnClickListener toggleClipboard = dummy -> {

    };

    private View.OnClickListener navToHome = view -> {
        if(getActivity() instanceof CategoriesActivity)
            return;

        Intent goToProfile = new Intent(getContext(), CategoriesActivity.class);
        startActivity(goToProfile);

        getActivity().finish();
    };

    private View.OnClickListener navToProf = view -> {
        if(getActivity() instanceof ProfileActivity)
            return;

        Intent goToProfile = new Intent(getContext(), ProfileActivity.class);
        startActivity(goToProfile);

        getActivity().finish();
    };
}