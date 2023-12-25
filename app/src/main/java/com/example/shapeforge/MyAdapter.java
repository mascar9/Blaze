package com.example.shapeforge;

import android.app.assist.AssistStructure;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<String> data;

    private Context context;

    private OnClickListener onClickListener;

    public MyAdapter(List<String> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_list_item, parent, false);
        return new ViewHolder(view);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, String exerciseName);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView button;
        TextView description;
        public ViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.exercise_list_item_title);
            description = itemView.findViewById(R.id.exercise_list_item_desc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            String exerciseName = data.get(position);
                            onClickListener.onClick(position, exerciseName);
                        }
                    }
                }
            });
        }
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = data.get(position);
        holder.button.setText(item);

        // Get the corresponding Exercise object based on the position
        Exercise exercise = GlobalClass.getExerciseList(context).get(0);;


        for(int k  = 0; k < GlobalClass.getExerciseList(context).size(); k++){
            if(GlobalClass.getExerciseList(context).get(k).getName().equalsIgnoreCase(item)){
                exercise = GlobalClass.getExerciseList(context).get(k);
            }
        }

        // Set the targeted muscles in the exercise_list_item_desc TextView
        String muscles = exercise.getTargetedMusclesAsString();
        holder.description.setText(muscles);
    }

    public void setData(List<String> newData) {
        data = newData;
        notifyDataSetChanged();
    }



}

