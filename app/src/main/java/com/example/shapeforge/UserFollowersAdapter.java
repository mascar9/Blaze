package com.example.shapeforge;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shapeforge.R;

import java.util.List;

public class UserFollowersAdapter extends RecyclerView.Adapter<UserFollowersAdapter.UserFollowersViewHolder> {

    private List<String> followersList;

    private Context context;

    public UserFollowersAdapter(List<String> followersList, Context context) {
        this.followersList = followersList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserFollowersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_followers_layout, parent, false);
        return new UserFollowersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserFollowersViewHolder holder, int position) {
        String username = followersList.get(position);
        // You may want to retrieve additional user information and set it on the views
        holder.bind(username);
    }

    @Override
    public int getItemCount() {
        return followersList.size();
    }

    static class UserFollowersViewHolder extends RecyclerView.ViewHolder {
        private ImageView userImageView;
        private TextView usernameTextView, nameTextView;

        public UserFollowersViewHolder(@NonNull View itemView) {
            super(itemView);
            userImageView = itemView.findViewById(R.id.imageView3);
            usernameTextView = itemView.findViewById(R.id.userUsernameDisplay);
            nameTextView = itemView.findViewById(R.id.userNameDisplay);
        }

        public void bind(String username) {
            // You can set the user information here
            usernameTextView.setText("@" + username);
            nameTextView.setText("User Full Name"); // Replace with actual user name
            // Load user image using a library like Glide or Picasso
            // Example: Glide.with(itemView.getContext()).load("user_image_url").into(userImageView);
        }
    }
}

