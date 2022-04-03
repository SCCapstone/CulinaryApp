package com.github.CulinaryApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.CulinaryApp.views.CategoriesActivity;
import com.github.CulinaryApp.views.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class NavbarFragment extends Fragment{

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
        Button logoutButton  = getView().findViewById(R.id.toolbarlogOut);

        profileButton.setOnClickListener(new NavListener(ProfileActivity.class));
        favsButton.setOnClickListener(new NavListener(FavoritesActivity.class));
        homeButtom.setOnClickListener(new NavListener(CategoriesActivity.class));
//        trendingButton.setOnClickListener(toggleTrending);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                FirebaseAuth.getInstance().signOut();
                                goToLogin();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
                builder.setTitle("Are you sure you'd like to logout?");
                builder.setPositiveButton("Yes",dialogClickListener)
                        .setNegativeButton("No",dialogClickListener).show();

            }

            private void goToLogin() {
                Intent switchToLogin = new Intent(getContext(), LoginActivity.class);
                startActivity(switchToLogin);
            }
        });


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