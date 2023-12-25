package com.example.shapeforge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterEditExerciseRepsSets extends RecyclerView.Adapter<MyAdapterEditExerciseRepsSets.ViewHolder> {
    private List<String> data;

    private Workout workout;

    private Context context;

    private OnClickListener onClickListener;

    public MyAdapterEditExerciseRepsSets(List<String> data, Workout workout, Context context) {
        this.data = data;
        this.workout = workout;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_list_item_setsreps, parent, false);
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
        TextView exerciseName;
        TextView description;

        EditText sets, reps;

        ImageButton deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            exerciseName = itemView.findViewById(R.id.exercise_list_item_title);
            description = itemView.findViewById(R.id.exercise_list_item_desc);
            sets = itemView.findViewById(R.id.sets);
            reps = itemView.findViewById(R.id.reps);
            deleteButton = itemView.findViewById(R.id.exercise_deleteButton);




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
        holder.exerciseName.setText(item);

        // Get the corresponding Exercise object based on the position
        Exercise exercise = GlobalClass.getExerciseList(context).get(0);;


        for(int k  = 0; k < GlobalClass.getExerciseList(context).size(); k++){
            if(GlobalClass.getExerciseList(context).get(k).getName().equalsIgnoreCase(item)){
                exercise = GlobalClass.getExerciseList(context).get(k);
            }
        }

        holder.reps.setText(String.valueOf(workout.getReps(item)));
        holder.sets.setText(String.valueOf(workout.getSets(item)));

        String muscles = exercise.getTargetedMusclesAsString();
        holder.description.setText(muscles);


        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    String exerciseName = data.get(position);
                    data.remove(position);
                    workout.getExercisesNames().remove(exerciseName); // Remove from the workout

                    workout.removeExercise(exerciseName);
                    notifyItemRemoved(position);



                    /*
                    ArrayList<Workout> workoutList = GlobalClass.getWorkoutList(context);
                    for (int k = 0; k < workoutList.size(); k++) {
                        if (workoutList.get(k).getName().equalsIgnoreCase(workout.getName())) {
                            workoutList.remove(k);
                        }
                    }
                    workoutList.add(0, workout);


                     */
                    //GlobalClass.saveWorkoutList(context, workoutList);
                    //TODO Editar Workout

                }
            }
        });
    }

    public void setData(List<String> newData) {
        data = newData;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemChecked(String item, boolean isChecked);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }




}

