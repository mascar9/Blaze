package com.example.shapeforge;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class WorkoutColorsAdapter extends RecyclerView.Adapter<WorkoutColorsAdapter.ViewHolder> {

    private Context context;
    private List<String> workoutNames;
    private Map<String, String> workoutsColors;

    private Map<String, Integer> workoutNumbers;


    public WorkoutColorsAdapter(Context context, List<String> workoutNames, Map<String, String> workoutsColors, Map<String, Integer> workoutNumbers) {
        this.context = context;
        this.workoutNames = workoutNames;
        this.workoutsColors = workoutsColors;
        this.workoutNumbers = workoutNumbers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_workout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String workoutName = workoutNames.get(position);
        String color = workoutsColors.get(workoutName);

        holder.tvWorkoutName.setText(workoutName + ": " + workoutNumbers.get(workoutName));
        holder.viewColor.setBackgroundColor(Color.parseColor(color));
    }

    @Override
    public int getItemCount() {
        return workoutNames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvWorkoutName;
        View viewColor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWorkoutName = itemView.findViewById(R.id.tvWorkoutName);
            viewColor = itemView.findViewById(R.id.viewColor);
        }
    }
}
