package com.example.shapeforge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlansActivity extends AppCompatActivity {


    private LocalDate selectedDate;

    private ImageButton setWorkoutPopUpButton;

    private TextView date, plannedWorkout;

    private RecyclerView workout_history_rv, workout_color_rv;

    private PieChart pieChart;

    private Map<LocalDate, String> plans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);

        getWindow().setWindowAnimations(android.R.style.Animation_Translucent);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.destination_plan);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        workout_history_rv = findViewById(R.id.workout_history_rv);

        pieChart = findViewById(R.id.piechart);

        LocalDate currentDate = LocalDate.now();

        workout_color_rv = findViewById(R.id.piechartWorkoutsNames);


        plans = GlobalClass.plansList;


        FirebaseDatabase database;
        DatabaseReference reference;
        database = FirebaseDatabase.getInstance("https://shape-forge-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference();

        ReadAndWriteSnippets snippets = new ReadAndWriteSnippets(reference);
        String userID = snippets.getUserID();

        PlansAdapter adapter = new PlansAdapter(this, snippets);
        workout_history_rv.setLayoutManager(new LinearLayoutManager(this));
        workout_history_rv.addItemDecoration(new CustomDividerItemDecoration(this));
        workout_history_rv.setAdapter(adapter);

        createPieChart();




        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.destination_badges:
                        Intent badgesIntent = new Intent(PlansActivity.this, CalculatorActivity.class);
                        startActivity(badgesIntent);
                        return true;
                    case R.id.destination_workout:
                        Intent workoutIntent = new Intent(PlansActivity.this, WorkoutActivity.class);
                        startActivity(workoutIntent);
                        return true;
                    case R.id.destination_plan:
                        //startActivity(new Intent(MainActivity.this, Option3Activity.class));
                        return true;
                    case R.id.destination_home:
                        Intent profileIntent = new Intent(PlansActivity.this, MainActivity.class);
                        startActivity(profileIntent);
                        return true;
                    case R.id.destination_exercises:
                        Intent exercisesIntent = new Intent(PlansActivity.this, ExercisesActivity.class);
                        startActivity(exercisesIntent);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void createPieChart() {
        Map<String, Integer> workoutsCountings = new HashMap<>();
        Map<String, String> workoutsColors = new HashMap<>();
        List<String> workoutNames = new ArrayList<>();

        Iterator iterator = plans.values().iterator();
        while (iterator.hasNext()){
            String workoutName = iterator.next().toString();
            if(!workoutsCountings.containsKey(workoutName)){
                workoutNames.add(workoutName);
                workoutsCountings.put(workoutName, 1);
                workoutsColors.put(workoutName, getRandomHexColor());
            }else{
                workoutsCountings.put(workoutName, workoutsCountings.get(workoutName) + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : workoutsCountings.entrySet()) {
            String workoutName = entry.getKey();
            int count = entry.getValue();
            String color = workoutsColors.get(workoutName); // Retrieve color for the specific workoutName

            pieChart.addPieSlice(new PieModel(workoutName, count, Color.parseColor(color)));
        }

        pieChart.startAnimation();

        WorkoutColorsAdapter adapterColorsNames = new WorkoutColorsAdapter(this, workoutNames, workoutsColors, workoutsCountings);

        workout_color_rv.setAdapter(adapterColorsNames);

        workout_color_rv.setLayoutManager(new LinearLayoutManager(this));
    }

    public static String getRandomHexColor() {
        // Generate a random 6-digit hexadecimal number
        Random random = new Random();
        int randomInt = random.nextInt(16777215);
        String randomHex = Integer.toHexString(randomInt).toUpperCase();
        // Pad the string to make sure it's always 6 digits
        return "#" + "000000".substring(randomHex.length()) + randomHex;
    }




}