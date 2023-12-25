package com.example.shapeforge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class CalculatorActivity extends AppCompatActivity {

    private TextView result_display_et;

    private SwitchCompat switchCompat;
    private EditText weight_et, reps_et;

    private Button calculate_pr_max_btn;

    private double result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        result = 0;

        switchCompat = findViewById(R.id.weight_metrics_pr);
        weight_et = findViewById(R.id.weight_calculate_pr);
        reps_et = findViewById(R.id.reps_calculate_pr);
        calculate_pr_max_btn = findViewById(R.id.calculate_pr_btn);
        result_display_et = findViewById(R.id.result_pr);
        result_display_et.setVisibility(View.GONE);

        switchCompat.getShowText();

        calculate_pr_max_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(weight_et.getText().toString()) && !TextUtils.isEmpty(reps_et.getText().toString())) {

                    double weight = Integer.parseInt(weight_et.getText().toString());

                    if (switchCompat.isChecked()) {
                        weight = weight * 2.26796185; //Passar a pounds

                        result = weight / (1.0278 - 0.0278 * Integer.parseInt(reps_et.getText().toString())); //Resultado em pounds

                        result = result * 0.45359237;
                        String formattedResult = String.format("%.1f", result);
                        result_display_et.setText("1RM is " + formattedResult + " Kilograms");
                    }else {
                        result = weight / (1.0278 - 0.0278 * Integer.parseInt(reps_et.getText().toString()));
                        String formattedResult = String.format("%.1f", result);
                        result_display_et.setText("1RM is " + formattedResult + " Pounds");
                    }




                    result_display_et.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(CalculatorActivity.this, "Must fill in the values", Toast.LENGTH_SHORT).show();
                }
            }
        });


        getWindow().setWindowAnimations(android.R.style.Animation_Translucent);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.destination_badges);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.destination_badges:
                        //startActivity(new Intent(MainActivity.this, Option1Activity.class));
                        return true;
                    case R.id.destination_workout:
                        Intent workoutIntent = new Intent(CalculatorActivity.this, WorkoutActivity.class);
                        startActivity(workoutIntent);
                        return true;
                    case R.id.destination_plan:
                        Intent planIntent = new Intent(CalculatorActivity.this, PlansActivity.class);
                        startActivity(planIntent);
                        return true;
                    case R.id.destination_home:
                        Intent profileIntent = new Intent(CalculatorActivity.this, MainActivity.class);
                        startActivity(profileIntent);
                        return true;
                    case R.id.destination_exercises:
                        Intent exercisesIntent = new Intent(CalculatorActivity.this, ExercisesActivity.class);
                        startActivity(exercisesIntent);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }


}