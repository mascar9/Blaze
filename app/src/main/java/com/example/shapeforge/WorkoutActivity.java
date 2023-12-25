package com.example.shapeforge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class WorkoutActivity extends AppCompatActivity {

    private RecyclerView workout_list_recycler;
    private MyAdapterWorkout adapter;

    private ArrayList<String> workoutsNames;

    private FloatingActionButton createWorkoutButton;

    private List<Workout> workouts;

    FirebaseDatabase database;
    DatabaseReference reference;

    ReadAndWriteSnippets snippets;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        database = FirebaseDatabase.getInstance("https://shape-forge-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference();

        snippets = new ReadAndWriteSnippets(reference);
        userID = snippets.getUserID();

        snippets.getWorkoutList(userID, new ReadAndWriteSnippets.OnWorkoutListListener() {
                    @Override
                    public void onWorkoutListRetrieved(List<Workout> workoutList) {

                        // Handle successful retrieval of the workout list
                        Log.d("problemz", "worked + size: " + workoutList.size());
                        workouts = workoutList;

                        workout_list_recycler = findViewById(R.id.workouts_list);

                        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing); // Define the desired spacing in pixels
                        ItemSpacingDecoration itemDecoration = new ItemSpacingDecoration(spacingInPixels);
                        workout_list_recycler.removeItemDecoration(itemDecoration);
                        workout_list_recycler.addItemDecoration(itemDecoration);

                        workoutsNames = new ArrayList<>();

                        for(int k = 0; k < workouts.size(); k++){
                            workoutsNames.add(workouts.get(k).getName());
                        }

                        adapter = new MyAdapterWorkout(getApplicationContext(), workoutsNames, workouts);

                        workout_list_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        workout_list_recycler.setAdapter(adapter);


                        adapter.setOnClickListener(new MyAdapter.OnClickListener() {
                            @Override
                            public void onClick(int position, String exerciseName) {
                                Intent intent = new Intent(WorkoutActivity.this, SingleWorkoutActivity.class);
                                intent.putExtra("workout_info", exerciseName);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onWorkoutListNotFound() {
                    }

                    @Override
                    public void onUserNotFound() {

                    }

                    @Override
                    public void onWorkoutListError(String error) {
                    }
                });


        createWorkoutButton = findViewById(R.id.create_workout_button);

        createWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createWorkoutIntent = new Intent(WorkoutActivity.this, CreateOrEditWorkout.class);
                startActivity(createWorkoutIntent);
            }
        });




        /*
        workout_list_recycler = findViewById(R.id.workouts_list);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing); // Define the desired spacing in pixels
        ItemSpacingDecoration itemDecoration = new ItemSpacingDecoration(spacingInPixels);
        workout_list_recycler.removeItemDecoration(itemDecoration);
        workout_list_recycler.addItemDecoration(itemDecoration);

        workoutsNames = new ArrayList<>();

        for(int k = 0; k < workouts.size(); k++){
            workoutsNames.add(workouts.get(k).getName());
        }

        adapter = new MyAdapterWorkout(this, workoutsNames, workouts);

        workout_list_recycler.setLayoutManager(new LinearLayoutManager(this));
        workout_list_recycler.setAdapter(adapter);

adapter.setOnClickListener(new MyAdapter.OnClickListener() {
                            @Override
                            public void onClick(int position, String exerciseName) {
                                Intent intent = new Intent(WorkoutActivity.this, SingleWorkoutActivity.class);
                                intent.putExtra("workout_info", exerciseName);
                                startActivity(intent);
                            }
                        });
         */

        getWindow().setWindowAnimations(android.R.style.Animation_Translucent);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.destination_workout);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.destination_badges:
                        Intent badgesIntent = new Intent(WorkoutActivity.this, CalculatorActivity.class);
                        startActivity(badgesIntent);
                        return true;
                    case R.id.destination_workout:
                        return true;
                    case R.id.destination_plan:
                        Intent planIntent = new Intent(WorkoutActivity.this, PlansActivity.class);
                        startActivity(planIntent);
                        return true;
                    case R.id.destination_home:
                        Intent profileIntent = new Intent(WorkoutActivity.this, MainActivity.class);
                        startActivity(profileIntent);
                        return true;
                    case R.id.destination_exercises:
                        Intent exercisesIntent = new Intent(WorkoutActivity.this, ExercisesActivity.class);
                        startActivity(exercisesIntent);
                        return true;
                    default:
                        return false;
                }
            }
        });



    }
}