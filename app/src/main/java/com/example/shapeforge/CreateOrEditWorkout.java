package com.example.shapeforge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CreateOrEditWorkout extends AppCompatActivity {

    EditText workoutNameET;
    Button addExerciseButton;
    RecyclerView exerciseRecycler;

    AppCompatButton saveButton, deleteButton;

    MyAdapterEditExerciseRepsSets adapter;

    private MyAdapterSelectExercises selectorAdapter;
    private String selectedWorkout;

    private Workout workout;

    private List<Workout> workouts;

    ArrayList<Exercise> exerciseList = GlobalClass.exerciseList;

    ArrayList<String> exercisesNames = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference reference;

    ReadAndWriteSnippets snippets;

    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_edit_workout);

        workoutNameET = findViewById(R.id.workout_name);
        addExerciseButton = findViewById(R.id.add_button);
        exerciseRecycler = findViewById(R.id.edit_exercises_list);
        saveButton = findViewById(R.id.save_editWorkout);


        selectedWorkout = getIntent().getStringExtra("edit_workout");

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing); // Define the desired spacing in pixels
        ItemSpacingDecoration itemDecoration = new ItemSpacingDecoration(spacingInPixels);
        exerciseRecycler.removeItemDecoration(itemDecoration);
        exerciseRecycler.addItemDecoration(itemDecoration);



        database = FirebaseDatabase.getInstance("https://shape-forge-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference();

        snippets = new ReadAndWriteSnippets(reference);
        userID = snippets.getUserID();

        workouts = new ArrayList<>();


        snippets.getWorkoutList(userID, new ReadAndWriteSnippets.OnWorkoutListListener() {
            @Override
            public void onWorkoutListRetrieved(List<Workout> workoutList) {
                // Handle successful retrieval of the workout list
                workouts = workoutList;

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


        if(selectedWorkout != null) {
            workout = null;

            if(workouts.size() == 0){
                workouts  = GlobalClass.workoutList;
            }

            for (int k = 0; k < workouts.size(); k++) {
                if (workouts.get(k).getName().equalsIgnoreCase(selectedWorkout)) {
                    workout = workouts.get(k);
                }
            }
            exercisesNames = workout.getExercisesNames();
            adapter = new MyAdapterEditExerciseRepsSets(workout.getExercisesNames(), workout, getApplicationContext());
            workoutNameET.setText(workout.getName());
            exerciseRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            exerciseRecycler.setAdapter(adapter);

        }else{
            Map<String, SetRep> exercises = new LinkedHashMap<>();

            workout = new Workout("name", exercises);

            adapter = new MyAdapterEditExerciseRepsSets(workout.getExercisesNames(), workout, getApplicationContext());

            exerciseRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            exerciseRecycler.setAdapter(adapter);
        }


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newWorkoutName = workoutNameET.getText().toString();
                if (!newWorkoutName.isBlank()) {

                    /*
                    TODO Fazer funcionalidade de editar workout

                    ArrayList<Workout> workoutList = GlobalClass.getWorkoutList(CreateOrEditWorkout.this);
                    for (int k = 0; k < workoutList.size(); k++) {
                        if (workoutList.get(k).getName().equalsIgnoreCase(workout.getName())) {
                            workoutList.remove(k);
                        }
                    }
                    workout.setName(newWorkoutName);
                    workoutList.add(0, workout);


                     */
                    workout.setName(newWorkoutName);
                    GlobalClass.workoutList.add(workout);



                    snippets.addWorkoutToUser(userID, workout, new ReadAndWriteSnippets.OnWorkoutAddListener() {
                        @Override
                        public void onWorkoutAdded() {
                            Toast.makeText(CreateOrEditWorkout.this, "Changes were saved", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onWorkoutAddError(String error) {
                            Toast.makeText(CreateOrEditWorkout.this, "Error", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onUserNotFound() {
                            Toast.makeText(CreateOrEditWorkout.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    });

                    //GlobalClass.saveWorkoutList(CreateOrEditWorkout.this, workoutList);
                    Intent exerciseIntentToWorkout = new Intent(CreateOrEditWorkout.this, SingleWorkoutActivity.class);
                    exerciseIntentToWorkout.putExtra("workout_info", newWorkoutName);
                    startActivity(exerciseIntentToWorkout);
                }

            }
        });

        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = getLayoutInflater().inflate(R.layout.fragment_custom_popup, null);

                RecyclerView exerciseRecyclerSelector;
                EditText search_bar_exercises;
                AppCompatButton addButton;

                search_bar_exercises = popupView.findViewById(R.id.search_bar_exercises);

                addButton = popupView.findViewById(R.id.add_button);

                exerciseRecyclerSelector = popupView.findViewById(R.id.recyclerViewSelectExercises);


                ArrayList<Exercise> exerciseList = GlobalClass.getExerciseList(getApplicationContext());
                ArrayList<String> exercisesNames = new ArrayList<>();

                for(int k = 0; k < exerciseList.size(); k++){
                    exercisesNames.add(exerciseList.get(k).getName());
                }


                selectorAdapter = new MyAdapterSelectExercises(exercisesNames, CreateOrEditWorkout.this);
                exerciseRecyclerSelector.setAdapter(selectorAdapter);
                exerciseRecyclerSelector.setLayoutManager(new LinearLayoutManager(CreateOrEditWorkout.this));

                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<String> selectedExercises = new ArrayList<>();


                        for (int i = 0; i < selectorAdapter.getItemCount(); i++) {
                            MyAdapterSelectExercises.ViewHolder viewHolder = (MyAdapterSelectExercises.ViewHolder) exerciseRecyclerSelector.findViewHolderForAdapterPosition(i);
                            if (viewHolder != null && viewHolder.checkBox.isChecked()) {
                                String selectedItem = selectorAdapter.getItem(i);
                                selectorAdapter.addItemToSelection(selectedItem);
                                selectedExercises.add(selectedItem);
                            }
                        }


                        workout.addExercises(selectedExercises);
                        adapter.setData(workout.getExercisesNames());
                        //GlobalClass.saveWorkoutList(CreateOrEditWorkout.this, GlobalClass.getWorkoutList(CreateOrEditWorkout.this));
                        adapter.notifyDataSetChanged();

                        /*
                        recreate();
                        */
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }


                    }
                });

                search_bar_exercises.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        filterData(s.toString());
                    }

                    private void filterData(String query) {
                        List<String> filteredData = new ArrayList<>();

                        for (Exercise exercise : exerciseList) {
                            String exerciseName = exercise.getName();

                            // Filter based on search query
                            if (exerciseName.toLowerCase().contains(query.toLowerCase())) {
                                // Filter based on selected muscles

                                    filteredData.add(exerciseName);

                            }
                        }

                        selectorAdapter.setData(filteredData);
                    }

                });

                //Display of PopUp



                // Set a background drawable for the popup window
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // Set focusable to true to allow interaction with the popup window
                popupWindow.setFocusable(true);

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int screenWidth = displayMetrics.widthPixels;

                // Set the width of the popup window to match the screen width
                popupWindow.setWidth(screenWidth);

                // Set the animation style for the popup window
                popupWindow.setAnimationStyle(R.style.PopupAnimation);

                // Show the popup window at the center of the activity
                popupWindow.showAtLocation(findViewById(R.id.add_button), Gravity.BOTTOM, 0, 0);

            }
        });
    }





}

