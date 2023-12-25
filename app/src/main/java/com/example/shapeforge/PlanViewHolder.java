package com.example.shapeforge;

// PlanViewHolder.java
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlanViewHolder extends RecyclerView.ViewHolder {
    public TextView textDate;
    public TextView textWorkout;
    public ImageButton btnDelete;

    public PlanViewHolder(@NonNull View itemView) {
        super(itemView);
        textDate = itemView.findViewById(R.id.textDate);
        textWorkout = itemView.findViewById(R.id.textWorkout);
        btnDelete = itemView.findViewById(R.id.btnDelete);
    }
}

