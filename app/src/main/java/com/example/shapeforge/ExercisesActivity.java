package com.example.shapeforge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ExercisesActivity extends AppCompatActivity {

    private RecyclerView exercise_list_recycler;
    private MyAdapter adapter;

    private EditText search_bar;

    private ArrayList<Exercise> originalData;

    private ArrayList<String> exercisesNames = new ArrayList<>();

    private ImageButton filterButton, exitFilterButton;


    private List<String> selectedMuscles = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        getWindow().setWindowAnimations(android.R.style.Animation_Translucent);

        search_bar = findViewById(R.id.search_bar_exercises);

        exercise_list_recycler = findViewById(R.id.exercises_list);

        filterButton = findViewById(R.id.filter_exercises_button);




        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing); // Define the desired spacing in pixels
        ItemSpacingDecoration itemDecoration = new ItemSpacingDecoration(spacingInPixels);
        exercise_list_recycler.removeItemDecoration(itemDecoration);
        exercise_list_recycler.addItemDecoration(itemDecoration);


        originalData = new ArrayList<>();
        //originalData.addAll(GlobalClass.exerciseList);
        originalData.addAll(GlobalClass.getExerciseList(this));



        for(int k = 0; k < originalData.size(); k++){
            exercisesNames.add(originalData.get(k).getName());
        }

        adapter = new MyAdapter(exercisesNames, this);

        exercise_list_recycler.setLayoutManager(new LinearLayoutManager(this));
        exercise_list_recycler.addItemDecoration(new CustomDividerItemDecoration(this));
        exercise_list_recycler.setAdapter(adapter);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterPopup();
            }
        });

        adapter.setOnClickListener(new MyAdapter.OnClickListener() {
            @Override
            public void onClick(int position, String exerciseName) {
                Intent intent = new Intent(ExercisesActivity.this, ExerciseActivity.class);
                intent.putExtra("exercise_info", exerciseName);
                startActivity(intent);
            }
        });

        
        search_bar.addTextChangedListener(new TextWatcher() {
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
        });




        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.destination_exercises);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.destination_badges:
                        Intent badgesIntent = new Intent(ExercisesActivity.this, CalculatorActivity.class);
                        startActivity(badgesIntent);
                        return true;
                    case R.id.destination_workout:
                        Intent workoutIntent = new Intent(ExercisesActivity.this, WorkoutActivity.class);
                        startActivity(workoutIntent);
                        return true;
                    case R.id.destination_plan:
                        Intent planIntent = new Intent(ExercisesActivity.this, PlansActivity.class);
                        startActivity(planIntent);
                        return true;
                    case R.id.destination_home:
                        Intent profileIntent = new Intent(ExercisesActivity.this, MainActivity.class);
                        startActivity(profileIntent);
                        return true;
                    case R.id.destination_exercises:
                        //startActivity(new Intent(MainActivity.this, Option3Activity.class));
                        return true;
                    default:
                        return false;
                }
            }
        });
    }


    private void showFilterPopup() {
        View popupView = getLayoutInflater().inflate(R.layout.checkbox_exercises, null);

        exitFilterButton = popupView.findViewById(R.id.exit_exercises_filter_button);


        CheckBox checkboxQuadriceps = popupView.findViewById(R.id.checkbox_quadriceps);
        CheckBox checkboxHamstrings = popupView.findViewById(R.id.checkbox_hamstrings);
        CheckBox checkboxGlutes = popupView.findViewById(R.id.checkbox_glutes);
        CheckBox checkboxCalves = popupView.findViewById(R.id.checkbox_calves);
        CheckBox checkboxChest = popupView.findViewById(R.id.checkbox_chest);
        CheckBox checkboxShoulders = popupView.findViewById(R.id.checkbox_shoulders);
        CheckBox checkboxTriceps = popupView.findViewById(R.id.checkbox_triceps);
        CheckBox checkboxLowerBack = popupView.findViewById(R.id.checkbox_lower_back);
        CheckBox checkboxUpperBack = popupView.findViewById(R.id.checkbox_upper_back);
        CheckBox checkboxBack = popupView.findViewById(R.id.checkbox_back);
        CheckBox checkboxBiceps = popupView.findViewById(R.id.checkbox_biceps);
        CheckBox checkboxAbdominals = popupView.findViewById(R.id.checkbox_abdominals);
        CheckBox checkboxObliques = popupView.findViewById(R.id.checkbox_obliques);
        CheckBox checkboxForearms = popupView.findViewById(R.id.checkbox_forearms);
        CheckBox checkboxUpperChest = popupView.findViewById(R.id.checkbox_upper_chest);
        CheckBox checkboxLowerChest = popupView.findViewById(R.id.checkbox_lower_chest);

        // Add more CheckBox views

        // Set the initial checked state based on selectedMuscles list

        checkboxQuadriceps.setChecked(selectedMuscles.contains("Quadriceps"));
        checkboxHamstrings.setChecked(selectedMuscles.contains("Hamstrings"));
        checkboxGlutes.setChecked(selectedMuscles.contains("Glutes"));
        checkboxCalves.setChecked(selectedMuscles.contains("Calves"));
        checkboxChest.setChecked(selectedMuscles.contains("Chest"));
        checkboxShoulders.setChecked(selectedMuscles.contains("Shoulders"));
        checkboxTriceps.setChecked(selectedMuscles.contains("Triceps"));
        checkboxLowerBack.setChecked(selectedMuscles.contains("Lower back"));
        checkboxBack.setChecked(selectedMuscles.contains("Back"));
        checkboxUpperBack.setChecked(selectedMuscles.contains("Upper back"));
        checkboxBack.setChecked(selectedMuscles.contains("Back"));
        checkboxAbdominals.setChecked(selectedMuscles.contains("Abdominals"));
        checkboxObliques.setChecked(selectedMuscles.contains("Obliques"));
        checkboxForearms.setChecked(selectedMuscles.contains("Forearms"));
        checkboxUpperChest.setChecked(selectedMuscles.contains("Upper Chest"));
        checkboxLowerChest.setChecked(selectedMuscles.contains("Lower Chest"));
        // Set the checked state for other CheckBox views



        checkboxQuadriceps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedMuscles("Quadriceps", isChecked);
            }
        });

        checkboxHamstrings.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedMuscles("Hamstrings", isChecked);
            }
        });

        checkboxGlutes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedMuscles("Glutes", isChecked);
            }
        });

        checkboxCalves.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedMuscles("Calves", isChecked);
            }
        });

        checkboxChest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedMuscles("Chest", isChecked);
            }
        });

        checkboxShoulders.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedMuscles("Shoulders", isChecked);
            }
        });

        checkboxTriceps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedMuscles("Triceps", isChecked);
            }
        });

        checkboxLowerBack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedMuscles("Lower back", isChecked);
            }
        });

        checkboxUpperBack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedMuscles("Upper back", isChecked);
            }
        });

        checkboxBack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedMuscles("Back", isChecked);
            }
        });

        checkboxBiceps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedMuscles("Biceps", isChecked);
            }
        });

        checkboxAbdominals.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedMuscles("Abdominals", isChecked);
            }
        });

        checkboxObliques.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedMuscles("Obliques", isChecked);
            }
        });

        checkboxForearms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedMuscles("Forearms", isChecked);
            }
        });

        checkboxUpperChest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedMuscles("Upper Chest", isChecked);
            }
        });

        checkboxLowerChest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateSelectedMuscles("Lower Chest", isChecked);
            }
        });

        // Add checked change listeners for other CheckBox views

        // Create the PopupWindow
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

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
        popupWindow.showAtLocation(findViewById(R.id.filter_exercises_button), Gravity.BOTTOM, 0, 0);

        // Show the popup window at the center of the activity
        //popupWindow.showAtLocation(findViewById(R.id.filter_exercises_button), Gravity.CENTER, 0, 0);


        exitFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss(); // Dismiss the PopupWindow
            }
        });
    }


    private void updateSelectedMuscles(String muscle, boolean isChecked) {
        if (isChecked) {
            selectedMuscles.add(muscle);
        } else {
            selectedMuscles.remove(muscle);
        }
        filterData(search_bar.getText().toString());
    }


    private void filterData(String query) {
        List<String> filteredData = new ArrayList<>();

        for (Exercise exercise : originalData) {
            String exerciseName = exercise.getName();

            // Filter based on search query
            if (exerciseName.toLowerCase().contains(query.toLowerCase())) {
                // Filter based on selected muscles
                if (selectedMuscles.isEmpty() || exercise.getTargetedMuscles().containsAll(selectedMuscles)) {
                    filteredData.add(exerciseName);
                }
            }
        }

        adapter.setData(filteredData);
    }


    private boolean exerciseContainsSelectedMuscle(String exercise) {
        // Check if the exercise contains any of the selected muscles
        for (String muscle : selectedMuscles) {
            if (exercise.contains(muscle)) {
                return true;
            }
        }
        return false;
    }

}