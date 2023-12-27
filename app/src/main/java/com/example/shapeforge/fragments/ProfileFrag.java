package com.example.shapeforge.fragments;

import static androidx.fragment.app.FragmentManager.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shapeforge.GlobalClass;
import com.example.shapeforge.ProfileActivity;
import com.example.shapeforge.R;
import com.example.shapeforge.Social.SearchFriendsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;



public class ProfileFrag extends Fragment {

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView nameWelcomeTV, usernameTV, stat3, stat4, WorkoutsNrDisplay;
        ImageButton profileButton;
        AppCompatButton seachFriendBtn;

        nameWelcomeTV = rootView.findViewById(R.id.Name_tv);
        profileButton = rootView.findViewById(R.id.profileBtn);
        usernameTV = rootView.findViewById(R.id.username_profile_et);
        WorkoutsNrDisplay = rootView.findViewById(R.id.workoutNrDisplay);
        seachFriendBtn = rootView.findViewById(R.id.searchFriendButton);

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
        usernameTV.setText("@" + GlobalClass.user.getUsername());


        //WorkoutsNrDisplay.setText("Workouts:\n" + GlobalClass.workoutList.size());


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