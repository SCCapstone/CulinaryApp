package com.github.CulinaryApp;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileDisplayFragment extends Fragment {

    public ProfileDisplayFragment() {
        // Required empty public constructor
    }

    public static ProfileDisplayFragment newInstance() {
        return new ProfileDisplayFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_display, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProfileActivity currentProfileActivity = (ProfileActivity) getActivity();
        String[] profileValues = currentProfileActivity.getProfileStrings();

        TextView displayName = getView().findViewById(R.id.displayName);

        TextView bio = getView().findViewById(R.id.bio);
        ImageView bgImg = getView().findViewById(R.id.backgroundImg);
        CircleImageView profilePic = getView().findViewById(R.id.profilePicture);

        StorageReference[] refs = currentProfileActivity.getRefs();

        currentProfileActivity.loadImg(refs[0], bgImg);
        currentProfileActivity.loadImg(refs[1], profilePic);

        displayName.setText(profileValues[0]);
        bio.setText(profileValues[1]);

    }

    @Override
    public void onStart() {
        super.onStart();

        Button backButton = getView().findViewById(R.id.doneButton);
        backButton.setOnClickListener(quitFragment);
    }

    View.OnClickListener quitFragment = view -> {
        FragmentManager manager = getActivity().getSupportFragmentManager();

        manager.beginTransaction()
                .remove(manager.findFragmentById(R.id.profileDisplayContainer))
                .commit();
    };
}