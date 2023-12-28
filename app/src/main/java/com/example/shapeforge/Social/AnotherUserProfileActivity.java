package com.example.shapeforge.Social;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shapeforge.GlobalClass;
import com.example.shapeforge.R;
import com.example.shapeforge.ReadAndWriteSnippets;
import com.example.shapeforge.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnotherUserProfileActivity extends AppCompatActivity {
    private TextView nameWelcomeTV, usernameTV, followersDisplay, followingDisplay, nrWorkoutsDisplay;

    private String userID;
    private String anotherUserID;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private ReadAndWriteSnippets snippets;

    private User user;

    private Integer nrFollowers;

    private ImageButton exit;

    private AppCompatButton addFriendBtn;

    private List<String> requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_user_profile);

        Intent intent = getIntent();
        if (intent != null) {
            userID = intent.getStringExtra("userID");
            anotherUserID = intent.getStringExtra("anotherUserID");

            // Now you have the values of userID and anotherUserID
            // Use them as needed in your activity
        }

        database = FirebaseDatabase.getInstance("https://shape-forge-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference();

        snippets = new ReadAndWriteSnippets(reference);

        nrFollowers = 0;
        addFriendBtn = findViewById(R.id.addFriendButton);
        nameWelcomeTV = findViewById(R.id.Name_tv);
        usernameTV = findViewById(R.id.username_profile_et);
        followersDisplay = findViewById(R.id.followersTV);
        followingDisplay = findViewById(R.id.followingTV);
        nrWorkoutsDisplay = findViewById(R.id.workoutNrDisplay);
        exit = findViewById(R.id.exitAUPButton);


        snippets.getRequestedToFollowList(userID, new ReadAndWriteSnippets.OnRequestedToFollowListListener() {
            @Override
            public void onRequestedToFollowListRetrieved(List<String> requestedToFollowList) {
                if(requestedToFollowList == null){
                    requests = new ArrayList<>();
                }else {
                    requests = requestedToFollowList;
                    if (requests.contains(anotherUserID)) {
                        addFriendBtn.setText("Request sent");
                        addFriendBtn.setTextColor(getResources().getColor(R.color.light_gray));
                    }
                }
            }

            @Override
            public void onRequestedToFollowListNotFound() {
                // Handle the case where requestedToFollowList is not found
                requests = new ArrayList<>();            }

            @Override
            public void onRequestedToFollowListRetrieveError(String errorMessage) {
                // Handle the error in retrieving requestedToFollowList
                requests = new ArrayList<>();
            }
        });




        if(GlobalClass.anotherUser != null){
            user = GlobalClass.anotherUser;
            nameWelcomeTV.setText(user.getName());
            usernameTV.setText(user.getUsername());



            Map<String, Boolean> followers = user.getFollowers();

            if(followers != null) {
                List<Boolean> followersBools = new ArrayList<>(followers.values());
                for (int k = 0; k < followersBools.size(); k++) {
                    if (followersBools.get(k))
                        nrFollowers++;
                }
                followersDisplay.setText("Followers\n" + nrFollowers);
                followingDisplay.setText("Following\n" +followers.size());
            }else {
                followersDisplay.setText("Followers\n0");
                followingDisplay.setText("Following\n0");
            }

            //nrWorkoutsDisplay.setText(user.getWorkoutList().size());

        }

        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!requests.contains(anotherUserID)) {
                    addFriendBtn.setText("Request sent");
                    addFriendBtn.setTextColor(getResources().getColor(R.color.light_gray));
                    requests.add(anotherUserID);
                    snippets.updateRequestedToFollowList(userID, requests);
                }else{
                    addFriendBtn.setText("Follow");
                    addFriendBtn.setTextColor(getResources().getColor(R.color.apple_white));
                    requests.remove(anotherUserID);
                    snippets.updateRequestedToFollowList(userID, requests);
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AnotherUserProfileActivity.this, SearchFriendsActivity.class);
                startActivity(intent1);
            }
        });


    }


}