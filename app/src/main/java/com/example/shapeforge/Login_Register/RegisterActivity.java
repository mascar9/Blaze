package com.example.shapeforge.Login_Register;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shapeforge.GlobalClass;
import com.example.shapeforge.MainActivity;
import com.example.shapeforge.R;
import com.example.shapeforge.ReadAndWriteSnippets;
import com.example.shapeforge.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText email_et, username_et, name_et;
    private EditText password_et, confirm_password_et;
    private FirebaseAuth mAuth;

    private ProgressBar progressBar;

    private Button backBtn, signUpBtn;


    private FirebaseAuth auth;

    private FirebaseUser userDB;

    private FirebaseDatabase database;

    private DatabaseReference reference;

    private String email, username, password, confirm_password, name;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

            FirebaseDatabase database = FirebaseDatabase.getInstance("https://shape-forge-default-rtdb.europe-west1.firebasedatabase.app");
            DatabaseReference reference = database.getReference();

            ReadAndWriteSnippets snippets = new ReadAndWriteSnippets(reference);

            snippets.getUser(currentUser.getUid(), new ReadAndWriteSnippets.OnUserRetrieveListener() {
                @Override
                public void onUserRetrieved(User user) {
                    GlobalClass.user = user;
                    //GlobalClass.PRs = user.getPRs();
                    //GlobalClass.workoutList = user.getWorkoutList();
                    //GlobalClass.badges = user.getBadges();
                    Intent loginSucIntent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(loginSucIntent);
                    finish();
                }

                @Override
                public void onUserNotFound() {
                    // Handle the scenario where the user is not found
                }

                @Override
                public void onUserRetrieveError(String error) {
                    // Handle the error scenario
                }
            });


        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email_et = findViewById(R.id.et_email);
        username_et = findViewById(R.id.et_username);
        password_et = findViewById(R.id.et_password);
        confirm_password_et = findViewById(R.id.et_confirm_password);
        progressBar = findViewById(R.id.progress_bar_reg);
        name_et = findViewById(R.id.et_name);

        signUpBtn = findViewById(R.id.button_signup);




        auth = FirebaseAuth.getInstance();
        userDB = auth.getCurrentUser();

        database = FirebaseDatabase.getInstance("https://shape-forge-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference();

        mAuth = FirebaseAuth.getInstance();

        UsernameAvailabilityChecker checker = new UsernameAvailabilityChecker(reference);


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = String.valueOf(email_et.getText());
                username = String.valueOf(username_et.getText());
                password = String.valueOf(password_et.getText());
                confirm_password = String.valueOf(confirm_password_et.getText());
                name = String.valueOf(name_et.getText());

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(RegisterActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(username)) {
                    Toast.makeText(RegisterActivity.this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                }else if (!password.equals(confirm_password)) {
                    Toast.makeText(RegisterActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                }else{

                    checker.checkUsernameAvailability(username, new UsernameAvailabilityChecker.OnUsernameCheckListener() {
                        @Override
                        public void onUsernameExists() {
                            Toast.makeText(RegisterActivity.this, "username already exists", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onUsernameAvailable() {
                            registerUser();
                        }

                        @Override
                        public void onUsernameCheckError(String error) {
                            // Handle the error (e.g., display an error message)
                        }
                    });
                }
            }
        });

    }

    public void registerUser(){
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {


                            auth = FirebaseAuth.getInstance();
                            userDB = auth.getCurrentUser();

                            ReadAndWriteSnippets readAndWriteSnippets = new ReadAndWriteSnippets(reference);
                            readAndWriteSnippets.writeNewUser(userDB.getUid(), name, username);


                            FirebaseDatabase database = FirebaseDatabase.getInstance("https://shape-forge-default-rtdb.europe-west1.firebasedatabase.app");
                            DatabaseReference reference = database.getReference();

                            ReadAndWriteSnippets snippets = new ReadAndWriteSnippets(reference);

                            snippets.getUser(userDB.getUid(), new ReadAndWriteSnippets.OnUserRetrieveListener() {
                                @Override
                                public void onUserRetrieved(User user) {
                                    GlobalClass.user = user;
                                    //GlobalClass.PRs = user.getPRs();
                                    //GlobalClass.workoutList = user.getWorkoutList();
                                    //GlobalClass.badges = user.getBadges();

                                    Toast.makeText(RegisterActivity.this, "Registration successful",
                                            Toast.LENGTH_SHORT).show();

                                    Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(loginIntent);
                                }

                                @Override
                                public void onUserNotFound() {
                                    // Handle the scenario where the user is not found
                                }

                                @Override
                                public void onUserRetrieveError(String error) {
                                    // Handle the error scenario
                                }
                            });



                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}