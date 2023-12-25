package com.example.shapeforge;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.shapeforge.fragments.BadgesFragment;
import com.example.shapeforge.fragments.FriendsFrag;
import com.example.shapeforge.fragments.ProfileFrag;

public class MyViewPagerAdapter  extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ProfileFrag();
            case 1:
                return new FriendsFrag();
            case 2:
                return new BadgesFragment();
            default:
                return new ProfileFrag();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
