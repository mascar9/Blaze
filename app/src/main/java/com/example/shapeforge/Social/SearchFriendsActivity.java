package com.example.shapeforge.Social;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.shapeforge.MainActivity;
import com.example.shapeforge.R;
import com.example.shapeforge.ReadAndWriteSnippets;
import com.example.shapeforge.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SearchFriendsActivity extends AppCompatActivity {

    private ImageButton closeButton;

    private EditText searchUsersET;

    private RecyclerView usersRV;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private ReadAndWriteSnippets snippets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);

        closeButton = findViewById(R.id.closeSFButton);
        searchUsersET = findViewById(R.id.searchUsersET);
        usersRV =  findViewById(R.id.usersRV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this); // Replace 'this' with your activity or context
        usersRV.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance("https://shape-forge-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference();

        snippets = new ReadAndWriteSnippets(reference);


        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(SearchFriendsActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });


        searchUsersET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Unused
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = charSequence.toString().trim();

                // Perform a search in the Firebase database
                snippets.searchUsers(query, new ReadAndWriteSnippets.OnUserSearchListener() {
                    @Override
                    public void onUserSearchResult(List<User> userList) {

                        UserAdapter userAdapter = new UserAdapter(userList, snippets.getUserID(), getApplicationContext());
                        usersRV.setAdapter(userAdapter);
                    }

                    @Override
                    public void onUserSearchError(String error) {
                        // Handle the error
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Unused
            }
        });


    }
}