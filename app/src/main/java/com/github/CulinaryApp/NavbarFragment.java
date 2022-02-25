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

        Button clipboardFavs = getView().findViewById(R.id.toolbarFavs);
        Button homeButtom = getView().findViewById(R.id.toolbarHome);
        Button profileButton = getView().findViewById(R.id.toolbarProfile);
//        Button trendingButton = getView().findViewById(R.id.toolbarTrending);

        clipboardFavs.setOnClickListener(toggleFavs);
        profileButton.setOnClickListener(new navListener(ProfileActivity.class));
        homeButtom.setOnClickListener(new navListener(CategoriesActivity.class));
//        trendingButton.setOnClickListener(toggleTrending);
    }

    View.OnClickListener toggleFavs = view -> {
        Activity currentActivity = getActivity();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        FragmentContainerView favsContainer = (FragmentContainerView) inflater.inflate(R.layout.layout_navbar_favs_cont, currentActivity.findViewById(R.id.favsFragmentHolder));
        currentActivity.addContentView(favsContainer, new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));

        FragmentManager manager = getActivity().getSupportFragmentManager();
        int visibility = favsContainer.getVisibility();

        if(visibility==View.GONE){
            manager.beginTransaction()
                    .add(R.id.favsFragmentHolder, TrendingFragment.class, null)
                    .commit();

            favsContainer.setVisibility(View.VISIBLE);
            favsContainer.requestFocus();
        } else if (visibility == View.VISIBLE){
            manager.beginTransaction()
                    .remove(manager.findFragmentById(R.id.favsFragmentHolder))
                    .commit();

            favsContainer.setVisibility(View.GONE);

        }
    };

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

//        =
//    } view -> {
//        if(getActivity() instanceof CategoriesActivity)
//            return;
//
//        Intent goToProfile = new Intent(getContext(), CategoriesActivity.class);
//        startActivity(goToProfile);
//
//        getActivity().finish();
//    };

//    private View.OnClickListener navToProf = view -> {
//        if(getActivity() instanceof ProfileActivity)
//            return;
//
//        Intent goToProfile = new Intent(getContext(), ProfileActivity.class);
//        startActivity(goToProfile);
//
//        getActivity().finish();
//    };
}