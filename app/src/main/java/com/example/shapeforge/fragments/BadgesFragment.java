package com.example.shapeforge.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shapeforge.Badge;
import com.example.shapeforge.BadgesAdapter;
import com.example.shapeforge.Exercise;
import com.example.shapeforge.GlobalClass;
import com.example.shapeforge.R;
import com.example.shapeforge.Workout;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class BadgesFragment extends Fragment {

    private TextView badgesCounterDisplay;

    private int nrUnlockedBadges;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_badges, container, false);

        TextView stat1, stat2, stat3, stat4;

        badgesCounterDisplay = rootView.findViewById(R.id.badgesCounterDisplay);
        nrUnlockedBadges = GlobalClass.badges.size();

        RecyclerView badgesRecyclerView = rootView.findViewById(R.id.badgesRecyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        badgesRecyclerView.setLayoutManager(layoutManager);

        // Create a list of badges (you can add more badges programmatically)
        LinkedHashMap<String, Badge> badges = GlobalClass.getBadgesList(getContext());

        // Set up the RecyclerView adapter
        BadgesAdapter badgesAdapter = new BadgesAdapter(badges);
        badgesRecyclerView.setAdapter(badgesAdapter);

        /*
        List<Badge> badgeList = new ArrayList<>(badges.values());
        for(int k = 0; k < badgeList.size(); k++){
            if(badgeList.get(k).isUnlocked())
                nrUnlockedBadges++;
        }

         */

        List<Badge> badgeList = new ArrayList<>(badges.values());

        String badgesDisplayValue = nrUnlockedBadges + " / " + badgeList.size();
        badgesCounterDisplay.setText(badgesDisplayValue);


        badgesAdapter.setOnBadgeClickListener(new BadgesAdapter.OnBadgeClickListener() {
            @Override
            public void onBadgeClick(Badge badge) {
                showBadgeDetailsDialog(badge);
            }
        });

        return rootView;
    }

    private void showBadgeDetailsDialog(Badge badge) {
        // Inflate the custom dialog layout
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_badge_details, null);

        // Initialize TextViews in the dialog layout
        TextView titleTextView = dialogView.findViewById(R.id.dialogBadgeTitle);
        TextView descriptionTextView = dialogView.findViewById(R.id.dialogBadgeDescription);

        // Set the title and description in the dialog
        titleTextView.setText(badge.getTitle());
        descriptionTextView.setText(badge.getDescription());

        // Create an AlertDialog with the custom layout
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}