package com.example.shapeforge;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadAndWriteSnippets {

    private static final String TAG = "ReadAndWriteSnippets";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    public ReadAndWriteSnippets(DatabaseReference database) {
        // [START initialize_database_ref]
        mDatabase = database;
        // [END initialize_database_ref]
    }

    // [START rtdb_write_new_user]
    public void writeNewUser(String userId, String name, String username) {
        List<Workout> workouts = new ArrayList<>();
        User user = new User(name, username, workouts);

        mDatabase.child("users").child(userId).setValue(user);
    }
    // [END rtdb_write_new_user]

    public void writeNewUserWithTaskListeners(String userId, String name, String username) {
        List<Workout> workouts = new ArrayList<>();
        User user = new User(name, username, workouts);

        // [START rtdb_write_new_user_task]
        mDatabase.child("users").child(userId).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        // ...
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                    }
                });
        // [END rtdb_write_new_user_task]
    }

    public void addWorkoutToUser(String userId, Workout workout, final OnWorkoutAddListener listener) {
        DatabaseReference userRef = mDatabase.child("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the existing user data
                    User user = dataSnapshot.getValue(User.class);

                    // Update the workoutList
                    if (user != null) {
                        List<Workout> updatedWorkoutList = user.getWorkoutList();

                        if (updatedWorkoutList == null) {
                            updatedWorkoutList = new ArrayList<>();
                        }

                        updatedWorkoutList.add(workout);

                        // Set the updated workoutList to the user object
                        user.setWorkoutList(updatedWorkoutList);

                        // Write the updated user data back to the database
                        userRef.setValue(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        listener.onWorkoutAdded();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        listener.onWorkoutAddError(e.getMessage());
                                    }
                                });
                    }
                } else {
                    listener.onUserNotFound();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onWorkoutAddError(databaseError.getMessage());
            }
        });
    }

    public String getUserID() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser userDB = auth.getCurrentUser();
        if (userDB != null) {
            return userDB.getUid();
        }else
            return "error";
    }

    public interface OnWorkoutAddListener {
        void onWorkoutAdded();
        void onWorkoutAddError(String error);
        void onUserNotFound();
    }

    public void deleteWorkoutFromUser(String userId, Workout workout, final OnWorkoutDeleteListener listener) {
        DatabaseReference userRef = mDatabase.child("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the existing user data
                    User user = dataSnapshot.getValue(User.class);

                    // Remove the workout from the workoutList
                    if (user != null) {
                        user.removeWorkout(workout);

                        // Write the updated user data back to the database
                        userRef.setValue(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        listener.onWorkoutDeleted();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        listener.onWorkoutDeleteError(e.getMessage());
                                    }
                                });
                    }
                } else {
                    listener.onUserNotFound();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onWorkoutDeleteError(databaseError.getMessage());
            }
        });
    }

    public interface OnWorkoutDeleteListener {
        void onWorkoutDeleted();
        void onWorkoutDeleteError(String error);
        void onUserNotFound();
    }

    public void getWorkoutList(String userId, final OnWorkoutListListener listener) {
        DatabaseReference userRef = mDatabase.child("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the existing user data
                    User user = dataSnapshot.getValue(User.class);

                    if (user != null) {
                        List<Workout> workoutList = user.getWorkoutList();

                        if (workoutList != null) {
                            listener.onWorkoutListRetrieved(workoutList);
                        } else {
                            listener.onWorkoutListNotFound();
                        }
                    }
                } else {
                    listener.onUserNotFound();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onWorkoutListError(databaseError.getMessage());
            }
        });
    }

    public interface OnWorkoutListListener {
        void onWorkoutListRetrieved(List<Workout> workoutList);
        void onWorkoutListNotFound();
        void onUserNotFound();
        void onWorkoutListError(String error);
    }

    public void updatePRsForUser(String userId, Map<String, Long> PRs, final OnPRsUpdateListener listener) {
        DatabaseReference userRef = mDatabase.child("users").child(userId).child("PRs");

        userRef.setValue(PRs)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onPRsUpdateSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onPRsUpdateError(e.getMessage());
                    }
                });
    }

    public void getPRsForUser(String userId, final OnPRsRetrieveListener listener) {
        DatabaseReference userRef = mDatabase.child("users").child(userId).child("PRs");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Long> PRs = (Map<String, Long>) dataSnapshot.getValue();
                    if (PRs != null) {
                        listener.onPRsRetrieved(PRs);
                    } else {
                        listener.onPRsNotFound();
                    }
                } else {
                    listener.onUserNotFound();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onPRsError(databaseError.getMessage());
            }
        });
    }

    public interface OnPRsUpdateListener {
        void onPRsUpdateSuccess();
        void onPRsUpdateError(String error);
    }

    public interface OnPRsRetrieveListener {
        void onPRsRetrieved(Map<String, Long> PRs);
        void onPRsNotFound();
        void onUserNotFound();
        void onPRsError(String error);
    }

    public void getUserInfo(String userId, final OnUserInfoListener listener) {
        DatabaseReference userRef = mDatabase.child("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the existing user data
                    User user = dataSnapshot.getValue(User.class);

                    if (user != null) {
                        String name = user.getName();
                        String username = user.getUsername();

                        listener.onUserInfoRetrieved(name, username);
                    }
                } else {
                    listener.onUserNotFound();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onUserInfoError(databaseError.getMessage());
            }
        });
    }

    public interface OnUserInfoListener {
        void onUserInfoRetrieved(String name, String username);
        void onUserNotFound();
        void onUserInfoError(String error);
    }

    public void getUser(String userId, final OnUserRetrieveListener listener) {
        DatabaseReference userRef = mDatabase.child("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the existing user data
                    User user = dataSnapshot.getValue(User.class);

                    if (user != null) {
                        listener.onUserRetrieved(user);
                    }
                } else {
                    listener.onUserNotFound();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onUserRetrieveError(databaseError.getMessage());
            }
        });
    }

    public interface OnUserRetrieveListener {
        void onUserRetrieved(User user);
        void onUserNotFound();
        void onUserRetrieveError(String error);
    }

    public void updateUserBadges(String userId, Map<String, Badge> badges, final OnUserBadgesUpdateListener listener) {
        DatabaseReference userRef = mDatabase.child("users").child(userId).child("badges");

        userRef.setValue(badges)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onUserBadgesUpdateSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onUserBadgesUpdateError(e.getMessage());
                    }
                });
    }

    // Method to get user badges
    public void getUserBadges(String userId, final OnUserBadgesRetrieveListener listener) {
        DatabaseReference userRef = mDatabase.child("users").child(userId).child("badges");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the existing badges data
                    Map<String, Badge> badges = new HashMap<>();

                    for (DataSnapshot badgeSnapshot : dataSnapshot.getChildren()) {
                        Badge badge = badgeSnapshot.getValue(Badge.class);
                        if (badge != null) {
                            badges.put(badge.getTitle(), badge);
                        }
                    }

                    listener.onUserBadgesRetrieved(badges);
                } else {
                    listener.onUserBadgesNotFound();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onUserBadgesError(databaseError.getMessage());
            }
        });
    }

    // ... (existing interfaces)

    public interface OnUserBadgesUpdateListener {
        void onUserBadgesUpdateSuccess();
        void onUserBadgesUpdateError(String error);
    }

    public interface OnUserBadgesRetrieveListener {
        void onUserBadgesRetrieved(Map<String, Badge> badges);
        void onUserBadgesNotFound();
        void onUserBadgesError(String error);
    }


}
