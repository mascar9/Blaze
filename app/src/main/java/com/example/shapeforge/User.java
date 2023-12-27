package com.example.shapeforge;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String name;
    private String username;
    private String email;

    private List<Workout> workouts;

    private Map<String, Long> PRs;

    private Map<String, Badge> badges;

    private Map<LocalDate, String> plansList;

    private Map<String, Boolean> followers;



    public User(String name, String username, List<Workout> workouts){

        this.name = name;
        this.username = username;
        this.workouts = workouts;
        this.followers = new HashMap<>();
        this.badges = new HashMap<>();
        this.plansList = new HashMap<>();

    }

    public User(){

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Workout> getWorkoutList() {
        return workouts;
    }

    public void setWorkoutList(List<Workout> updatedWorkoutList) {
        this.workouts = updatedWorkoutList;
    }

    public void removeWorkout(Workout workout) {
        if (workouts != null) {
            for (int k = 0; k < workouts.size(); k++){
                if(workout.getName().equals(workouts.get(k).getName())){
                    workouts.remove(workouts.get(k));
                }
            }
        }
    }

    public Map<String, Long> getPRs() {
        return PRs;
    }

    public void setPRs(Map<String, Long> PRs) {
        this.PRs = PRs;
    }

    public Map<String, Badge> getBadges() {
        return badges;
    }

    public void setBadges(Map<String, Badge> badges) {
        this.badges = badges;
    }
}
