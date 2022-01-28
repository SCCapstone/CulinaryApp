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

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileDisplayFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "name";
    private static final String ARG_PARAM2 = "bio";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ProfileDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileDisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileDisplayFragment newInstance(String param1, String param2) {
        ProfileDisplayFragment fragment = new ProfileDisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        displayName.setText(profileValues[0]);
        bio.setText(profileValues[1]);

        Uri[] uris = currentProfileActivity.getUris();
        bgImg.setImageURI(uris[0]);
        profilePic.setImageURI(uris[1]);

    }

    @Override
    public void onStart() {
        super.onStart();

        Button backButton = getView().findViewById(R.id.doneButton);
        backButton.setOnClickListener(quitFragment);
    }

    View.OnClickListener quitFragment = view -> {
        ProfileActivity currentProfileActivity = (ProfileActivity) getActivity();
        FragmentManager manager = ((ProfileActivity) getActivity()).getSupportFragmentManager();

        manager.beginTransaction()
                .remove(manager.findFragmentById(R.id.profileDisplayContainer))
                .commit();
    };
}