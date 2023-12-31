package com.example.shapeforge.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shapeforge.GlobalClass;
import com.example.shapeforge.R;
import com.example.shapeforge.ReadAndWriteSnippets;
import com.example.shapeforge.Social.UserAdapter;
import com.example.shapeforge.UserFollowersAdapter;

import java.util.List;

public class FollowersFrag extends Fragment {

    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_followers, container, false);


        recyclerView = rootView.findViewById(R.id.followersRV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        GlobalClass.snippets.getFollowers(GlobalClass.userID, new ReadAndWriteSnippets.OnFollowersRetrieveListener() {
            @Override
            public void onFollowersRetrieved(List<String> followers) {
                UserFollowersAdapter userAdapter = new UserFollowersAdapter(followers, getContext());
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onFollowersNotFound() {
                // Handle the case where no followers are found
            }

            @Override
            public void onUserNotFound() {
                // Handle the case where the user is not found
            }

            @Override
            public void onFollowersError(String error) {
                // Handle the error case when retrieving followers fails
            }
        });











        return rootView;
    }
}