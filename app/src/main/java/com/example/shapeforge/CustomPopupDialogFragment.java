package com.example.shapeforge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomPopupDialogFragment extends DialogFragment {

    public static CustomPopupDialogFragment newInstance() {
        return new CustomPopupDialogFragment();
    }

    MyAdapter adapter;

    ArrayList<Exercise> exerciseList = new ArrayList<>();

    ArrayList<String> exercisesNames = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_custom_popup, container, false);

        // Get references to EditText and RecyclerView
        EditText editText = rootView.findViewById(R.id.search_bar_exercises);
        RecyclerView recyclerView = rootView.findViewById(R.id.edit_exercises_list);

        exerciseList = GlobalClass.getExerciseList(getContext());

        for(int k = 0; k < exerciseList.size(); k++){
            exercisesNames.add(exerciseList.get(k).getName());
        }

        adapter = new MyAdapter(exercisesNames, getContext());

        // Set up RecyclerView with adapter and layout manager
        // You can customize this part as per your data and requirements
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return rootView;
    }
}

