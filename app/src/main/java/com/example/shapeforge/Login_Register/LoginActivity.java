package com.example.shapeforge.Login_Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText email_et;
    private EditText password_et;

    private Button SignInBtn, SignUpBtn, ForgotPasswordBtn;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

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

                    Intent loginSucIntent = new Intent(LoginActivity.this, MainActivity.class);
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
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();


        email_et = findViewById(R.id.et_email);
        password_et = findViewById(R.id.et_password);
        SignInBtn = findViewById(R.id.button_signin);
        SignUpBtn = findViewById(R.id.button_signup);
        progressBar = findViewById(R.id.progress_bar_login);
        ForgotPasswordBtn = findViewById(R.id.button_forgot_password);

        ForgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = String.valueOf(email_et.getText());
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(LoginActivity.this, "Email has been sent to recover password", Toast.LENGTH_LONG).show();
                    mAuth.sendPasswordResetEmail(email);
                }
            }
        });

        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(signUpIntent);
            }
        });

        SignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = String.valueOf(email_et.getText());
                password = String.valueOf(password_et.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://shape-forge-default-rtdb.europe-west1.firebasedatabase.app");
                                        DatabaseReference reference = database.getReference();

                                        ReadAndWriteSnippets snippets = new ReadAndWriteSnippets(reference);

                                        snippets.getUser(user.getUid(), new ReadAndWriteSnippets.OnUserRetrieveListener() {
                                            @Override
                                            public void onUserRetrieved(User user) {
                                                GlobalClass.user = user;
                                                //GlobalClass.PRs = user.getPRs();
                                                //GlobalClass.workoutList = user.getWorkoutList();
                                                //GlobalClass.badges = user.getBadges();
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










                                        Toast.makeText(LoginActivity.this, "Login Successful.",
                                                Toast.LENGTH_SHORT).show();

                                        Intent loginSucIntent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(loginSucIntent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }



            }
        });

    }
}