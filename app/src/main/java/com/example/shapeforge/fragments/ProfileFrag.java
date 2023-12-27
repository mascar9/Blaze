package com.example.shapeforge.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.shapeforge.GlobalClass;
import com.example.shapeforge.R;
import com.example.shapeforge.Social.SearchFriendsActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ProfileFrag extends Fragment {

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView nameWelcomeTV, usernameTV, followersDisplay, followingDisplay, WorkoutsNrDisplay;
        ImageButton profileButton;
        AppCompatButton seachFriendBtn;
        int nrFollowers = 0;

        nameWelcomeTV = rootView.findViewById(R.id.Name_tv);
        profileButton = rootView.findViewById(R.id.profileBtn);
        usernameTV = rootView.findViewById(R.id.username_profile_et);
        WorkoutsNrDisplay = rootView.findViewById(R.id.workoutNrDisplay);
        seachFriendBtn = rootView.findViewById(R.id.searchFriendButton);
        followersDisplay = rootView.findViewById(R.id.followersTV);
        followingDisplay = rootView.findViewById(R.id.followingTV);


        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            // Callback logic here
            if (uri != null) {
                profileButton.setImageURI(uri);
                Log.d("PhotoPicker", "Selected URI: " + uri);
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });

        nameWelcomeTV.setText(GlobalClass.user.getName());
        usernameTV.setText(GlobalClass.user.getUsername());


        WorkoutsNrDisplay.setText(GlobalClass.workoutList.size() + "\nWorkouts");
        Map<String, Boolean> followers = GlobalClass.user.getFollowers();

        if(followers != null) {
            List<Boolean> followersBools = new ArrayList<>(followers.values());
            for (int k = 0; k < followersBools.size(); k++) {
                if (followersBools.get(k))
                    nrFollowers++;
            }
            followersDisplay.setText(nrFollowers + "\nFollowers");
            followingDisplay.setText(followers.size() + "\nFollowing");
        }else {
            followersDisplay.setText("0\n Followers");
            followingDisplay.setText("0\nFollowing");
        }

        seachFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchFriendIntent = new Intent(getContext(), SearchFriendsActivity.class);
                startActivity(searchFriendIntent);
            }
        });


        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });


        return rootView;
    }
}