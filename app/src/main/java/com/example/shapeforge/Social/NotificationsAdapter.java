package com.example.shapeforge.Social;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shapeforge.GlobalClass;
import com.example.shapeforge.R;
import com.example.shapeforge.ReadAndWriteSnippets;

import java.util.ArrayList;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder> {

    private List<String> followRequests;

    public NotificationsAdapter(List<String> followRequests) {
        this.followRequests = followRequests;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        String request = followRequests.get(position);
        holder.bind(request);

        holder.deleteRequestBtn.setOnClickListener(v -> onDeleteButtonClick(position, request));

        holder.acceptRequestBtn.setOnClickListener(v -> onAcceptButtonClick(position, request));


    }

    private void onAcceptButtonClick(int position, String request) {
        Log.d("hereeee", "I was clicked");

        followRequests.remove(request);
        notifyItemRemoved(position);
        GlobalClass.snippets.updateRequestedToFollowList(GlobalClass.userID, followRequests);

        GlobalClass.snippets.getFollowers(GlobalClass.userID, new ReadAndWriteSnippets.OnFollowersRetrieveListener() {
            @Override
            public void onFollowersRetrieved(List<String> followers) {
                    followers.add(request);
                    GlobalClass.snippets.updateFollowers(GlobalClass.userID, followers, new ReadAndWriteSnippets.OnFollowersUpdateListener() {
                        @Override
                        public void onFollowersUpdateSuccess() {
                            Log.d("hereeee", "Sucess");

                        }

                        @Override
                        public void onFollowersUpdateError(String error) {
                            Log.d("hereeee", error);

                        }
                    });
            }

            @Override
            public void onFollowersNotFound() {

                Log.d("hereeee", "NotFound");


                    List<String> newFollowersList = new ArrayList<>();
                    newFollowersList.add(request);
                    GlobalClass.snippets.updateFollowers(GlobalClass.userID, newFollowersList, new ReadAndWriteSnippets.OnFollowersUpdateListener() {
                        @Override
                        public void onFollowersUpdateSuccess() {
                            Log.d("hereeee", "GoodUpdate");

                        }

                        @Override
                        public void onFollowersUpdateError(String error) {
                            Log.d("hereeee", error);
                        }
                    });
            }


            @Override
            public void onUserNotFound() {
                Log.d("hereeee", "userNotFound");
                List<String> newFollowersList = new ArrayList<>();
                newFollowersList.add(request);
                GlobalClass.snippets.updateFollowers(GlobalClass.userID, newFollowersList, new ReadAndWriteSnippets.OnFollowersUpdateListener() {
                    @Override
                    public void onFollowersUpdateSuccess() {
                        Log.d("hereeee", "GoodUpdate");

                    }

                    @Override
                    public void onFollowersUpdateError(String error) {
                        Log.d("hereeee", error);
                    }
                });
            }

            @Override
            public void onFollowersError(String error) {
                Log.d("hereeee", error);
            }
        });
    }

    private void onDeleteButtonClick(int position, String req) {

        followRequests.remove(req);
        notifyItemRemoved(position);
        GlobalClass.snippets.updateRequestedToFollowList(GlobalClass.userID, followRequests);


    }

    @Override
    public int getItemCount() {
        return followRequests.size();
    }


    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private TextView requestTextView;
        private AppCompatButton acceptRequestBtn, deleteRequestBtn;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            requestTextView = itemView.findViewById(R.id.userUsernameDisplay);
            acceptRequestBtn = itemView.findViewById(R.id.acceptRequestButton);
            deleteRequestBtn = itemView.findViewById(R.id.deleteRequestButton);
        }

        public void bind(String request) {
            requestTextView.setText(request + ", has requested to follow you.");

        }
    }
}
