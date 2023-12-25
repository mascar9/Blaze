package com.example.shapeforge.Login_Register;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class UsernameAvailabilityChecker {

    private DatabaseReference mDatabase;

    public UsernameAvailabilityChecker(DatabaseReference database) {
        mDatabase = database;
    }

    public void checkUsernameAvailability(String username, final OnUsernameCheckListener listener) {
        DatabaseReference userRef = mDatabase.child("users");

        userRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Username already exists
                    listener.onUsernameExists();
                } else {
                    // Username is available
                    listener.onUsernameAvailable();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                listener.onUsernameCheckError(databaseError.getMessage());
            }
        });
    }

    public interface OnUsernameCheckListener {
        void onUsernameExists();
        void onUsernameAvailable();
        void onUsernameCheckError(String error);
    }
}