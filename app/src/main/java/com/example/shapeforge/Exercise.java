package com.example.shapeforge;

import java.util.ArrayList;

public class Exercise {

    private String description, name;


    private int PR;

    private ArrayList<String> targetedMuscles = new ArrayList<>();

    public Exercise (String description, String name, ArrayList<String> targetedMuscles){
        this.description = description;
        this.name = name;
        this.targetedMuscles = targetedMuscles;
        this.PR = 0;
    }

    public Exercise() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getTargetedMuscles() {
        return targetedMuscles;
    }

    public String getTargetedMusclesAsString() {
        StringBuilder builder = new StringBuilder();
        for (String muscle : targetedMuscles) {
            builder.append(muscle);
            builder.append(" / ");
        }
        // Remove the trailing " / " from the last muscle
        if (builder.length() > 3) {
            builder.setLength(builder.length() - 3);
        }
        return builder.toString();
    }

    public int getPR() {
        return PR;
    }

    public void setPR(int newPR) {
        this.PR = newPR;
    }
}
