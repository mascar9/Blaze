package com.example.shapeforge;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GlobalClass {

    private static final String SHARED_PREFS_NAME = "shapeforge_shared_prefs";
    private static final String KEY_EXERCISE_LIST = "exercise_list";
    private static final String KEY_MUSCLES_LIST = "muscles_list";
    private static final String KEY_WORKOUT_LIST = "workout_list";

    private static final String KEY_PLANS_LIST = "plans_lists_v3";

    private static final String KEY_BADGES_LIST = "badges_list";

    public static ArrayList<Exercise> exerciseList = new ArrayList<>();

    public static ArrayList<String> musclesList = new ArrayList<>();

    public static List<Workout> workoutList = new ArrayList<>();

    public static LinkedHashMap<LocalDate, String> plansList = new LinkedHashMap<>();

    public static LinkedHashMap<String, Badge> badgeList = new LinkedHashMap<>();

    public static Map<String, Long> PRs = new HashMap<>();

    public static Map<String, Badge> badges = new HashMap<>();

    public static String username;

    public static String name;

    public static User user = new User();








    /**
     *
     * Offline Data
     */

    public static void saveExerciseList(Context context, ArrayList<Exercise> exerciseList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(exerciseList);
        editor.putString(KEY_EXERCISE_LIST, json);
        editor.apply();
    }

    public static ArrayList<Exercise> getExerciseList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_EXERCISE_LIST, null);
        Type type = new TypeToken<ArrayList<Exercise>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static void saveWorkoutList(Context context, ArrayList<Workout> workoutList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(workoutList);
        editor.putString(KEY_WORKOUT_LIST, json);
        editor.apply();
        Log.d("JSON", json);
    }

    public static ArrayList<Workout> getWorkoutList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_WORKOUT_LIST, null);
        Type type = new TypeToken<ArrayList<Workout>>() {}.getType();

        return gson.fromJson(json, type);
    }

    public static void savePlansList(Context context, LinkedHashMap<LocalDate, String> plansList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(plansList);
        editor.putString(KEY_PLANS_LIST, json);
        editor.apply();
        Log.d("JSON", json);
    }

    public static LinkedHashMap<LocalDate, String> getPlansList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
        String json = sharedPreferences.getString(KEY_PLANS_LIST, null);

        if (json == null) {
            return new LinkedHashMap<>(); // Return an empty map if JSON is null
        }

        Type type = new TypeToken<LinkedHashMap<LocalDate, String>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static void saveBadgesList(Context context, LinkedHashMap<String, Badge> badgeList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(badgeList);
        editor.putString(KEY_BADGES_LIST, json);
        editor.apply();
        Log.d("JSON", json);
    }

    public static LinkedHashMap<String, Badge> getBadgesList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new GsonBuilder().create();
        String json = sharedPreferences.getString(KEY_BADGES_LIST, null);

        if (json == null) {
            return new LinkedHashMap<>(); // Return an empty map if JSON is null
        }

        Type type = new TypeToken<LinkedHashMap<String, Badge>>() {}.getType();
        return gson.fromJson(json, type);
    }


}
