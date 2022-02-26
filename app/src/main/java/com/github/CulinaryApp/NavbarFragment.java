package com.github.CulinaryApp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

        profileButton.setOnClickListener(new NavListener(ProfileActivity.class));
        favsButton.setOnClickListener(new NavListener(FavoritesActivity.class));
        homeButtom.setOnClickListener(new NavListener(CategoriesActivity.class));
//        trendingButton.setOnClickListener(toggleTrending);
    }


    public class NavListener <T extends Activity> implements View.OnClickListener {
        private final Class<T> targetActivity;

        public NavListener(Class<T> targetActivity){
            this.targetActivity = targetActivity;
        }

        @Override
        public void onClick(View v) {
            if(getActivity().getClass().getName().equals(targetActivity.getName()))
                return;

            Intent goToActivity = new Intent(getContext(), targetActivity);
            startActivity(goToActivity);

            getActivity().finish();
        }
    }


}