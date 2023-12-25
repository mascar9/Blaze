package com.example.shapeforge;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyAdapterWorkout extends RecyclerView.Adapter<MyAdapterWorkout.ViewHolder> {
    private List<String> data;
    private Context context;

    List<Workout> workouts;




    private MyAdapter.OnClickListener onClickListener;

    public MyAdapterWorkout(Context context, List<String> data, List<Workout> workouts) {
        this.context = context;
        this.data = data;
        this.workouts = workouts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_list_item, parent, false);
        return new ViewHolder(view);
    }

    public void setOnClickListener(MyAdapter.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, String exerciseName);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = data.get(position);
        holder.title.setText(item);

        Drawable backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.circle_shape_v2); // Replace with your desired drawable resource



        Workout workout = workouts.get(0);

        for(int k  = 0; k < workouts.size(); k++){
            if(workouts.get(k).getName().equalsIgnoreCase(item)){
                workout = workouts.get(k);
            }
        }


        holder.workout_letter.setText(String.valueOf(item.charAt(0)));
        Random random = new Random();
        int randomNumber = random.nextInt(4);
        switch (randomNumber){
            case 0:
                //holder.workout_letter.setBackgroundTint(ContextCompat.getColor(context, R.color.red_letter));
                backgroundDrawable.setTint(ContextCompat.getColor(context, R.color.red_letter));
                break;
            case 1:
                backgroundDrawable.setTint(ContextCompat.getColor(context, R.color.blue_letter));
                break;
            case 2:
                backgroundDrawable.setTint(ContextCompat.getColor(context, R.color.green_letter));
                break;
            case 3:
                backgroundDrawable.setTint(ContextCompat.getColor(context, R.color.gold_yellow));
                break;
            default:
                break;
        }

        holder.workout_letter.setBackground(backgroundDrawable);

        int nrExercisesNumber = workout.getNrExercises();

        if(nrExercisesNumber == 1){
            holder.nrExercises.setText(String.valueOf(nrExercisesNumber) + " exercise");
        }else{
            holder.nrExercises.setText(String.valueOf(nrExercisesNumber) +" exercises");
        }

        ArrayList<String> exercises = workout.getExercisesNames();
        if (nrExercisesNumber > 0){
            holder.ex1.setText(exercises.get(0));
        }
        if (nrExercisesNumber > 1){
            holder.ex2.setText(exercises.get(1));
        }
        if (nrExercisesNumber > 2){
            holder.ex3.setText(exercises.get(2));
        }
        if (nrExercisesNumber > 3){
            holder.ex4.setText("And more");
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, nrExercises, ex1, ex2, ex3, ex4, workout_letter;

        public ViewHolder(View itemView) {
            super(itemView);
            workout_letter = itemView.findViewById(R.id.workout_letter);
            title = itemView.findViewById(R.id.workout_list_item_title);
            nrExercises = itemView.findViewById(R.id.workout_list_nrexercises);
            ex1 = itemView.findViewById(R.id.workout_exercise_1);
            ex2 = itemView.findViewById(R.id.workout_exercise_2);
            ex3 = itemView.findViewById(R.id.workout_exercise_3);
            ex4 = itemView.findViewById(R.id.workout_exercise_4);

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

}

