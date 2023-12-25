package com.example.shapeforge;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.shapeforge.Login_Register.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    ImageView profilePic;

    ImageButton closeButton;

    AppCompatButton saveBtn, signOutBtn;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        profilePic = findViewById(R.id.profile_pic);
        closeButton = findViewById(R.id.closeButton);
        saveBtn = findViewById(R.id.save_profile_button);
        signOutBtn = findViewById(R.id.signOut_button);

        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            // Callback logic here
            if (uri != null) {
                profilePic.setImageURI(uri);
                Log.d("PhotoPicker", "Selected URI: " + uri);
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent closeIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(closeIntent);
                finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent saveIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(saveIntent);
                finish();
            }
        });

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intentSignOutUser = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentSignOutUser);
                finish();
            }
        });


        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Include only one of the following calls to launch(), depending on the types
                // of media that you want to let the user choose from.

                // Launch the photo picker and let the user choose only images.
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());



            }


        });


    }
}