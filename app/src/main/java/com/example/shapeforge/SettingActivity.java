package com.example.shapeforge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.shapeforge.Login_Register.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends AppCompatActivity {

    ImageButton exitSettingBtn;

    AppCompatButton signOutBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        exitSettingBtn = findViewById(R.id.exitSettingsButton);
        signOutBtn = findViewById(R.id.signOut_button);


        exitSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exitSettingIntent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(exitSettingIntent);
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
    }
}