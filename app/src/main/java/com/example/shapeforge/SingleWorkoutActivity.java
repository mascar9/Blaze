package com.example.shapeforge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SingleWorkoutActivity extends AppCompatActivity {

    private RecyclerView exercise_list_recycler;

    private MyAdapterForWorkoutExercises adapter;

    private ArrayList<Exercise> originalData;

    private ArrayList<String> exercisesNames = new ArrayList<>();

    private String workoutName;

    private ImageButton closeButton, addWorkoutHistoryBtn;

    private AppCompatButton editButton, deleteButton;

    private Workout workout;

    private TextView workoutTitle;

    private List<Workout> workouts;

    FirebaseDatabase database;
    DatabaseReference reference;

    ReadAndWriteSnippets snippets;

    String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_workout);

        Map<LocalDate, String> plans = GlobalClass.plansList;

        FirebaseDatabase database;
        DatabaseReference reference;
        database = FirebaseDatabase.getInstance("https://shape-forge-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference();

        ReadAndWriteSnippets snippets = new ReadAndWriteSnippets(reference);
        String userID = snippets.getUserID();

        editButton = findViewById(R.id.workout_edit_button);
        closeButton = findViewById(R.id.closeButton);
        exercise_list_recycler = findViewById(R.id.exercises_list);
        workoutTitle = findViewById(R.id.workout_name);
        deleteButton = findViewById(R.id.delete_editWorkout);
        addWorkoutHistoryBtn = findViewById(R.id.addWorkoutHistoryBtn);

        workoutName = getIntent().getStringExtra("workout_info");

        //database = FirebaseDatabase.getInstance("https://shape-forge-default-rtdb.europe-west1.firebasedatabase.app");
        //reference = database.getReference();

        //snippets = new ReadAndWriteSnippets(reference);
        //userID = snippets.getUserID();

        workouts = GlobalClass.workoutList;


        workout = null;
        for (int k = 0; k < workouts.size(); k++) {
            String name = workouts.get(k).getName();
            if (name.trim().equalsIgnoreCase(workoutName.trim())) {
                workout = workouts.get(k);
                break;
            }
        }

        exercisesNames = workout.getExercisesNames();

        workoutTitle.setText(workoutName);

        adapter = new MyAdapterForWorkoutExercises(exercisesNames, workout, getApplicationContext());
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing); // Define the desired spacing in pixels
        ItemSpacingDecoration itemDecoration = new ItemSpacingDecoration(spacingInPixels);
        exercise_list_recycler.removeItemDecoration(itemDecoration);
        exercise_list_recycler.addItemDecoration(itemDecoration);
        exercise_list_recycler.addItemDecoration(new CustomDividerItemDecoration(this));
        exercise_list_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        exercise_list_recycler.setAdapter(adapter);


        adapter.setOnClickListener(new MyAdapterForWorkoutExercises.OnClickListener() {
            @Override
            public void onClick(int position, String exerciseName) {
                Intent intent = new Intent(SingleWorkoutActivity.this, ExerciseActivity.class);
                intent.putExtra("exercise_info", exerciseName);
                intent.putExtra("selected_workout", workoutName);
                startActivity(intent);
            }
        });



        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingleWorkoutActivity.this, WorkoutActivity.class);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                for (int k = 0; k < workouts.size(); k++){
                    if(workouts.get(k).getName().equalsIgnoreCase(workout.getName())){
                        workouts.remove(k);
                    }
                }

                //GlobalClass.saveWorkoutList(SingleWorkoutActivity.this, workoutList);
                //TODO Editar workout


                snippets.deleteWorkoutFromUser(userID, workout, new ReadAndWriteSnippets.OnWorkoutDeleteListener() {
                    @Override
                    public void onWorkoutDeleted() {
                        Toast.makeText(SingleWorkoutActivity.this, "Workout has been deleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onWorkoutDeleteError(String error) {
                        Toast.makeText(SingleWorkoutActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUserNotFound() {
                        // Handle case where user is not found
                    }
                });

                Intent exerciseIntentToWorkout = new Intent(SingleWorkoutActivity.this, WorkoutActivity.class);
                startActivity(exerciseIntentToWorkout);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingleWorkoutActivity.this, CreateOrEditWorkout.class);
                intent.putExtra("edit_workout", workoutName);
                startActivity(intent);
            }
        });

        addWorkoutHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalClass.plansList.put(LocalDate.now(), workoutName);

                snippets.updateUserPlans(userID, GlobalClass.plansList, new ReadAndWriteSnippets.OnUserPlansUpdateListener() {
                    @Override
                    public void onUserPlansUpdateSuccess() {
                        Toast.makeText(getBaseContext(), "Workout was recorded", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUserPlansUpdateError(String error) {
                        Toast.makeText(getBaseContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }

}