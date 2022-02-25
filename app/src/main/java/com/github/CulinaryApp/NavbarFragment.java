package com.github.CulinaryApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.CulinaryApp.views.CategoriesActivity;

public class NavbarFragment extends Fragment {

  /*  public NavbarFragment() {
        //todo delete if unnecessary
    }*/

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

        Button favsButton = getView().findViewById(R.id.toolbarFavs);
        Button homeButtom = getView().findViewById(R.id.toolbarHome);
        Button profileButton = getView().findViewById(R.id.toolbarProfile);
//        Button trendingButton = getView().findViewById(R.id.toolbarTrending);

        profileButton.setOnClickListener(new navListener(ProfileActivity.class));
        favsButton.setOnClickListener(new navListener(FavoritesActivity.class));
        homeButtom.setOnClickListener(new navListener(CategoriesActivity.class));
//        trendingButton.setOnClickListener(toggleTrending);
    }


    private class navListener <T extends Activity> implements View.OnClickListener {
        private final Class<T> targetActivity;

        public navListener (Class<T> targetActivity){
            this.targetActivity = targetActivity;
        }

        @Override
        public void onClick(View v) {
            if(getActivity().getClass().getName().equals(targetActivity.getName()))
                return;

            Intent goToProfile = new Intent(getContext(), targetActivity);
            startActivity(goToProfile);

            getActivity().finish();
        }
    }


}