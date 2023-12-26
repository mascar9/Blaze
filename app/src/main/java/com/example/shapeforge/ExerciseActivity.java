package com.example.shapeforge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ExerciseActivity extends AppCompatActivity {

    TextView textViewExercise;
    String exerciseName;
    Exercise exercise;

    private EditText prET;

    private ImageButton closeButton, uploadPrButton;
    private TextView getTextViewExerciseDescription;

    private YouTubePlayer youTubePlayer;
    private YouTubePlayerView youtubePlayerView;


    private FirebaseDatabase database;
    private DatabaseReference reference;

    private ReadAndWriteSnippets snippets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        textViewExercise = findViewById(R.id.exerciseName);
        closeButton = findViewById(R.id.close_exercise_button);
        getTextViewExerciseDescription = findViewById(R.id.exercise_description);
        prET = findViewById(R.id.pr_edit_text);
        uploadPrButton = findViewById(R.id.upload_pr_btn);

        exerciseName = getIntent().getStringExtra("exercise_info");



        database = FirebaseDatabase.getInstance("https://shape-forge-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference();

        snippets = new ReadAndWriteSnippets(reference);


        exercise = null;


        String name = "";
        for (int k = 0; k < GlobalClass.getExerciseList(this).size(); k++){
            name = GlobalClass.getExerciseList(this).get(k).getName();
            if (name.trim().equalsIgnoreCase(exerciseName.trim())) {
                exercise = GlobalClass.getExerciseList(this).get(k);
                break; // Found a match, exit the loop
            }
        }

        Map<String, Long> PRs = GlobalClass.PRs;

        if(PRs.containsKey(exerciseName)){
            prET.setText(PRs.get(exerciseName) + "");
            Log.d("PRs", PRs.get(exerciseName)+"");
        }else{
            prET.setText("0");
        }


        getTextViewExerciseDescription.setText(exercise.getDescription());

        textViewExercise.setText(exerciseName);


        LinkedHashMap<String, Badge> badges = GlobalClass.badgeList;

        uploadPrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = prET.getText().toString().trim(); // Use getText() instead of toString()

                if (!a.isEmpty()) {
                    try {
                        long prValue = Long.parseLong(a);


                        Map<String, Long> PRs = GlobalClass.PRs;

                        PRs.put(exercise.getName(), prValue);

                        snippets.updatePRsForUser(snippets.getUserID(), PRs, new ReadAndWriteSnippets.OnPRsUpdateListener() {
                            @Override
                            public void onPRsUpdateSuccess() {
                                // PRs updated successfully
                                // Handle success case
                            }

                            @Override
                            public void onPRsUpdateError(String error) {
                                // Handle error case
                            }
                        });


                        Toast.makeText(ExerciseActivity.this, "PR recorded", Toast.LENGTH_SHORT).show();

                        Badge b = new Badge();

                        switch (exerciseName){
                            case "Bench Press":
                                if(prValue >= 60) {
                                    b = badges.get("Bench Beast I");
                                    GlobalClass.badges.put(b.getTitle(), b);
                                }
                                else{
                                    GlobalClass.badges.remove("Bench Beast I");
                                }

                                if(prValue >= 80) {
                                    b = badges.get("Bench Beast II");
                                    GlobalClass.badges.put(b.getTitle(), b);
                                }
                                else{
                                    GlobalClass.badges.remove("Bench Beast II");
                                }

                                if(prValue >= 100) {
                                    b = badges.get("Bench Beast III");
                                    GlobalClass.badges.put(b.getTitle(), b);
                                }
                                else {
                                    GlobalClass.badges.remove("Bench Beast III");
                                }

                                if(prValue >= 120) {
                                    b = badges.get("Bench Beast IV");
                                    GlobalClass.badges.put(b.getTitle(), b);
                                }else {
                                    GlobalClass.badges.remove("Bench Beast IV");
                                }

                                if(prValue >= 140) {
                                    b = badges.get("Bench Beast V");
                                    GlobalClass.badges.put(b.getTitle(), b);
                                }else{
                                    GlobalClass.badges.remove("Bench Beast V");
                                }


                                if(prValue >= 160) {
                                    b = badges.get("Bench Beast VI");
                                    GlobalClass.badges.put(b.getTitle(), b);
                                }else {
                                    GlobalClass.badges.remove("Bench Beast VI");
                                }
                        }

                        snippets.updateUserBadges(snippets.getUserID(), GlobalClass.badges, new ReadAndWriteSnippets.OnUserBadgesUpdateListener() {
                            @Override
                            public void onUserBadgesUpdateSuccess() {
                                // Handle success
                            }

                            @Override
                            public void onUserBadgesUpdateError(String error) {
                                // Handle error
                            }
                        });

                        //GlobalClass.saveBadgesList(ExerciseActivity.this, badges);

                    } catch (NumberFormatException e) {
                        // Handle the case where the input is not a valid integer
                    }
                } else {
                    // Handle the case where the input is empty
                }
            }
        });



        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedWorkout = getIntent().getStringExtra("selected_workout");


                if(selectedWorkout == null) {
                    Intent exerciseIntentToExercises = new Intent(ExerciseActivity.this, ExercisesActivity.class);
                    startActivity(exerciseIntentToExercises);
                }else {
                    Intent exerciseIntentToWorkout = new Intent(ExerciseActivity.this, SingleWorkoutActivity.class);
                    exerciseIntentToWorkout.putExtra("workout_info", selectedWorkout);
                    startActivity(exerciseIntentToWorkout);
                }
            }
        });

        //youtubePlayerView = findViewById(R.id.youtube_player_view);



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.destination_exercises);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.destination_badges:
                        Intent badgesIntent = new Intent(ExerciseActivity.this, CalculatorActivity.class);
                        startActivity(badgesIntent);
                        return true;
                    case R.id.destination_workout:
                        Intent workoutIntent = new Intent(ExerciseActivity.this, WorkoutActivity.class);
                        startActivity(workoutIntent);
                        return true;
                    case R.id.destination_plan:
                        Intent planIntent = new Intent(ExerciseActivity.this, PlansActivity.class);
                        startActivity(planIntent);
                        return true;
                    case R.id.destination_home:
                        Intent profileIntent = new Intent(ExerciseActivity.this, MainActivity.class);
                        startActivity(profileIntent);
                        return true;
                    case R.id.destination_exercises:
                        Intent exerciseIntent = new Intent(ExerciseActivity.this, ExercisesActivity.class);
                        startActivity(exerciseIntent);
                        return true;
                    default:
                        return false;
                }
            }
        });


    }

}