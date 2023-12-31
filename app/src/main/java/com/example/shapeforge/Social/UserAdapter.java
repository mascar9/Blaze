package com.example.shapeforge.Social;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shapeforge.GlobalClass;
import com.example.shapeforge.R; // Replace with the actual package name
import com.example.shapeforge.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private String userID;

    private Context context;



    public UserAdapter(List<User> userList, String userID, Context context) {
        this.userList = userList;
        this.userID = userID;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_layout, parent, false);
        return new UserViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    User user = userList.get(position);
                    // Handle the click event with access to the User object
                    holder.handleClick(user, userID);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView usernameTextView, nameTextView;
        private Context context;

        public UserViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            usernameTextView = itemView.findViewById(R.id.userUsernameDisplay);
            nameTextView = itemView.findViewById(R.id.userNameDisplay);



        }

        public void bind(User user) {
            usernameTextView.setText(user.getUsername());
            nameTextView.setText(user.getName());
        }

        private void handleClick(User user, String userID) {

            GlobalClass.anotherUser = user;
            Intent AnotherUserIntent = new Intent(context, AnotherUserProfileActivity.class);
            AnotherUserIntent.putExtra("userID", userID);
            AnotherUserIntent.putExtra("anotherUserID", user.getUsername());
            AnotherUserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(AnotherUserIntent);
        }

    }
}
