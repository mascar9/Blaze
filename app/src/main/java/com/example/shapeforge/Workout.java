package com.example.shapeforge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Workout {


    private String workoutName;
    private Map<String, SetRep> exercises;
    //Eventualmente um icon

    public Workout(String workoutName, Map<String, SetRep> exercises){
        this.workoutName = workoutName;
        this.exercises = exercises;

    }

    public Workout() {
    }



    public String getName() {
        return workoutName;
    }

    public int getNrExercises() {
        return exercises.size();
    }

    public Map<String, SetRep> getExercises() {
        return exercises;
    }

    public ArrayList<String> getExercisesNames() {
       Iterator<String> exercisesIt = exercises.keySet().iterator();
       ArrayList<String> firstThreeExercises = new ArrayList<>();
       while (exercisesIt.hasNext()){
           firstThreeExercises.add(exercisesIt.next());
       }

       return firstThreeExercises;
    }


    public String getRepSetsFromExercise(Exercise item) {
        String SetsNReps = "Sets: " + exercises.get(item.getName()).getSet() + " " + "Reps: "+ exercises.get(item.getName()).getRep();
        return SetsNReps;
    }

    public int getReps(String exercise) {

        Iterator<String> exercisesIt = exercises.keySet().iterator();

        while (exercisesIt.hasNext()){
            String exerciseV2 = exercisesIt.next();

            if(exerciseV2.equalsIgnoreCase(exercise)){
                return exercises.get(exerciseV2).getRep();
            }
        }



        return 0;
    }

    public int getSets(String exercise) {
        Iterator<String> exercisesIt = exercises.keySet().iterator();

        while (exercisesIt.hasNext()){
            String exerciseV2 = exercisesIt.next();

            if(exerciseV2.equalsIgnoreCase(exercise)){
                return exercises.get(exerciseV2).getSet();
            }
        }



        return 0;
    }

    public void addExercises(List<String> selectedExercisesV2) {



        for (int k = 0; k < selectedExercisesV2.size(); k++) {
            String exerciseName = selectedExercisesV2.get(k);
            SetRep setRep = new SetRep(3, 6); // You can set the initial set and rep values here
            exercises.put(exerciseName, setRep);
        }

    }

    public void setName(String newWorkoutName) {
        workoutName = newWorkoutName;
    }


    public void removeExercise(String exerciseName) {
        exercises.remove(exerciseName);
    }
}
