package com.example.shapeforge;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class BadgesAdapter extends RecyclerView.Adapter<BadgesAdapter.BadgeViewHolder> {

    private LinkedHashMap<String, Badge> badges;
    private List<Badge> badgesList;


    public BadgesAdapter(LinkedHashMap<String, Badge> badges) {
        this.badges = badges;

        this.badgesList = new ArrayList<>(badges.values());
    }

    public interface OnBadgeClickListener {
        void onBadgeClick(Badge badge);
    }

    private OnBadgeClickListener onBadgeClickListener;

    public void setOnBadgeClickListener(OnBadgeClickListener listener) {
        this.onBadgeClickListener = listener;
    }

    @NonNull
    @Override
    public BadgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.badge_item, parent, false);
        return new BadgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgeViewHolder holder, int position) {
        Badge badge = badgesList.get(position);

        Drawable iconDrawable = badge.isUnlocked()
                ? ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.winner)
                //? ContextCompat.getDrawable(holder.itemView.getContext(), badge.getIconResource())
                : ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.winner_gray);


        holder.badgeIcon.setImageDrawable(iconDrawable);

        holder.badgeTitle.setText(badge.getTitle());

        /*
        if (badge.isUnlocked()) {
            holder.itemView.setBackgroundResource(R.drawable.circle_background_unlocked);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.circle_background_locked);
        }

         */

        if (GlobalClass.badges.containsKey(badge.getTitle())) {
            holder.itemView.setBackgroundResource(R.drawable.circle_background_unlocked);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.circle_background_locked);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBadgeClickListener != null) {
                    onBadgeClickListener.onBadgeClick(badge);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return badgesList.size();
    }

    static class BadgeViewHolder extends RecyclerView.ViewHolder {
        ImageView badgeIcon;
        TextView badgeTitle;



        BadgeViewHolder(@NonNull View itemView) {
            super(itemView);
            badgeIcon = itemView.findViewById(R.id.badgeIcon);
            badgeTitle = itemView.findViewById(R.id.badgeTitle);
        }
    }
}
