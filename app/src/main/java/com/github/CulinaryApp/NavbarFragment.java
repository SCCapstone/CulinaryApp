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

    public NavbarFragment() {
        //todo delete if unnecessary
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
        Button homeButtom = getView().findViewById(R.id.toolbarHome);
        Button profileButton = getView().findViewById(R.id.toolbarProfile);
        Button trendingButton = getView().findViewById(R.id.toolbarTrending);

        clipboardButton.setOnClickListener(toggleClipboard);
        profileButton.setOnClickListener(new navListener(ProfileActivity.class));
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

    private View.OnClickListener toggleClipboard = dummy -> {

    };

    private  View.OnClickListener navListener = view -> {

    };


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

    private View.OnClickListener navToProf = view -> {
        if(getActivity() instanceof ProfileActivity)
            return;

        Intent goToProfile = new Intent(getContext(), ProfileActivity.class);
        startActivity(goToProfile);

        getActivity().finish();
    };
}