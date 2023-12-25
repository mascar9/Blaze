package com.example.shapeforge;

// PlansAdapter.java
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class PlansAdapter extends RecyclerView.Adapter<PlanViewHolder> {
    private Context context;
    private List<LocalDate> dates;
    private List<String> workouts;
    private LinkedHashMap<LocalDate, String> plans;

    public PlansAdapter(Context context, LinkedHashMap<LocalDate, String> plans) {
        this.context = context;
        this.plans = plans;
        this.dates = new ArrayList<>(plans.keySet());
        this.workouts = new ArrayList<>(plans.values());
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        LocalDate date = dates.get(position);
        String workout = workouts.get(position);

        holder.textDate.setText(date.toString());
        holder.textWorkout.setText("Workout: " + workout);

        holder.btnDelete.setOnClickListener(v -> onDeleteButtonClick(position));
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    private void onDeleteButtonClick(int position) {
        LocalDate dateToRemove = dates.get(position);
        plans.remove(dateToRemove);
        notifyItemRemoved(position);

        GlobalClass.savePlansList(context, plans);

        // You can also perform additional actions when deleting, such as updating UI or storage
        Toast.makeText(context, "Removed from history", Toast.LENGTH_SHORT).show();
    }
}

