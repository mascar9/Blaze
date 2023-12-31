package com.example.shapeforge.Social;

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

    }

    private void onDeleteButtonClick(int position, String req) {

        followRequests.remove(req);
        notifyItemRemoved(position);
        GlobalClass.snippets.updateRequestedToFollowList(GlobalClass.userID, followRequests);

        /*
        GlobalClass.snippets.getRequestedToFollowList(GlobalClass.userID, new ReadAndWriteSnippets.OnRequestedToFollowListListener() {
            @Override
            public void onRequestedToFollowListRetrieved(List<String> requestedToFollowList) {
                requestedToFollowList.remove(request);
                GlobalClass.snippets.updateRequestedToFollowList(GlobalClass.userID, requestedToFollowList);
            }

            @Override
            public void onRequestedToFollowListNotFound() {
                // Handle the case where requestedToFollowList is not found
            }

            @Override
            public void onRequestedToFollowListRetrieveError(String errorMessage) {
                // Handle the error in retrieving requestedToFollowList

            }
        });

         */

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
