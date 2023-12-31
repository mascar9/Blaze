package com.example.shapeforge.Social;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.shapeforge.GlobalClass;
import com.example.shapeforge.MainActivity;
import com.example.shapeforge.R;
import com.example.shapeforge.ReadAndWriteSnippets;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    ImageButton exitButton;
    RecyclerView requestsRV;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private ReadAndWriteSnippets snippets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        exitButton = findViewById(R.id.exitNotificationsButton);
        requestsRV = findViewById(R.id.followRequestsRV);

        snippets = GlobalClass.snippets;

        final List<String>[] followRequests = new List[]{new ArrayList<>()};



        snippets.getRequestedToFollowList(snippets.getUserID(), new ReadAndWriteSnippets.OnRequestedToFollowListListener() {
            @Override
            public void onRequestedToFollowListRetrieved(List<String> requestedToFollowList) {
                followRequests[0] = requestedToFollowList;
                //Log.d("Follows", followRequests[0].size()+"");
                NotificationsAdapter notificationsAdapter = new NotificationsAdapter(followRequests[0]);

                // Set layout manager
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                requestsRV.setLayoutManager(layoutManager);

                requestsRV.setAdapter(notificationsAdapter);
            }

            @Override
            public void onRequestedToFollowListNotFound() {
                Log.d("Follows", "not found");

            }

            @Override
            public void onRequestedToFollowListRetrieveError(String errorMessage) {
                Log.d("Follows", errorMessage);

            }
        });



        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exitNotificationsIntent = new Intent(NotificationsActivity.this, MainActivity.class);
                startActivity(exitNotificationsIntent);
            }
        });


    }
}