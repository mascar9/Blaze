package com.example.shapeforge.Social;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.shapeforge.fragments.BadgesFragment;
import com.example.shapeforge.fragments.FollowersFrag;
import com.example.shapeforge.fragments.FollowingFrag;
import com.example.shapeforge.fragments.FriendsFrag;
import com.example.shapeforge.fragments.ProfileFrag;

public class ViewPagerFollowAdapter extends FragmentStateAdapter {

    public ViewPagerFollowAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FollowersFrag();
            case 1:
                return new FollowingFrag();
            default:
                return new FollowersFrag();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
