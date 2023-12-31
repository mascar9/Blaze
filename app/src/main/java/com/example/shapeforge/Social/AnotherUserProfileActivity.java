package com.example.shapeforge.Social;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.shapeforge.GlobalClass;
import com.example.shapeforge.R;
import com.example.shapeforge.ReadAndWriteSnippets;
import com.example.shapeforge.User;
import com.example.shapeforge.UserFollowersAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AnotherUserProfileActivity extends AppCompatActivity {
    private TextView nameWelcomeTV, usernameTV, followersDisplay, followingDisplay, nrWorkoutsDisplay;

    private String userID;
    private String anotherUserUsername;

    private String anotherUserID;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private ReadAndWriteSnippets snippets;

    private User user;

    private Integer nrFollowers;

    private ImageButton exit;

    private AppCompatButton addFriendBtn;

    private List<String> requests;

    private List<String> userFollowers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_user_profile);

        Intent intent = getIntent();
        if (intent != null) {
            userID = intent.getStringExtra("userID");
            anotherUserUsername = intent.getStringExtra("anotherUserID");

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
        exit = findViewById(R.id.exitNotificationsButton);

        requests = new ArrayList<>();
        userFollowers = new ArrayList<>();

        snippets.getUserIdByUsername(anotherUserUsername, new ReadAndWriteSnippets.OnUserIdRetrieveListener() {
            @Override
            public void onUserIdRetrieved(String userId) {
                // Handle the user ID retrieval
                anotherUserID = userId;

                snippets.getRequestedToFollowList(anotherUserID, new ReadAndWriteSnippets.OnRequestedToFollowListListener() {
                    @Override
                    public void onRequestedToFollowListRetrieved(List<String> requestedToFollowList) {
                        if(requestedToFollowList == null){
                            requests = new ArrayList<>();
                        }else {
                            requests = requestedToFollowList;
                            if (requests.contains(GlobalClass.user.getUsername())) {
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


                //TODO trocar isto por getFollowing, nao getFollowers. Seu otario do crlh
                GlobalClass.snippets.getFollowers(GlobalClass.userID, new ReadAndWriteSnippets.OnFollowersRetrieveListener() {
                    @Override
                    public void onFollowersRetrieved(List<String> followers) {
                        userFollowers = followers;

                        if (followers.contains(anotherUserUsername)){
                            addFriendBtn.setText("Following");
                        }
                    }

                    @Override
                    public void onFollowersNotFound() {
                        // Handle the case where no followers are found
                        userFollowers = new ArrayList<>();
                    }

                    @Override
                    public void onUserNotFound() {
                        userFollowers = new ArrayList<>();
                    }

                    @Override
                    public void onFollowersError(String error) {
                        // Handle the error case when retrieving followers fails
                    }
                });
            }

            @Override
            public void onUserIdNotFound() {
                // Handle the case where no user with the given username was found
                Log.d("UserId", "User not found");
            }

            @Override
            public void onUserIdRetrieveError(String error) {
                // Handle the error case
                Log.e("UserId", "Error retrieving user ID: " + error);
            }
        });









        if(GlobalClass.anotherUser != null){
            user = GlobalClass.anotherUser;
            nameWelcomeTV.setText(user.getName());
            usernameTV.setText(user.getUsername());


            followersDisplay.setText("Followers\n0");
            followingDisplay.setText("Following\n0");
            //nrWorkoutsDisplay.setText(user.getWorkoutList().size());

        }


        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!requests.contains(GlobalClass.user.getUsername())) {
                    addFriendBtn.setText("Request sent");
                    addFriendBtn.setTextColor(getResources().getColor(R.color.light_gray));
                    requests.add(GlobalClass.user.getUsername());
                    snippets.updateRequestedToFollowList(anotherUserID, requests);
                }
                /*
                else if(userFollowers.contains(anotherUserUsername)){
                    addFriendBtn.setText("Follow");
                    addFriendBtn.setTextColor(getResources().getColor(R.color.apple_white));
                    userFollowers.remove(anotherUserUsername);
                    GlobalClass.snippets.updateFollowers(GlobalClass.userID, userFollowers, new ReadAndWriteSnippets.OnFollowersUpdateListener() {
                        @Override
                        public void onFollowersUpdateSuccess() {

                        }

                        @Override
                        public void onFollowersUpdateError(String error) {
                        }
                    });
                }

                 */
                else{
                    addFriendBtn.setText("Follow");
                    addFriendBtn.setTextColor(getResources().getColor(R.color.apple_white));
                    requests.remove(GlobalClass.user.getUsername());
                    snippets.updateRequestedToFollowList(anotherUserID, requests);
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