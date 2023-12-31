package com.example.shapeforge.Social;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.shapeforge.GlobalClass;
import com.example.shapeforge.MainActivity;
import com.example.shapeforge.MyViewPagerAdapter;
import com.example.shapeforge.R;
import com.google.android.material.tabs.TabLayout;

public class FollowsFollowingActivity extends AppCompatActivity {

    private ImageButton exitFollowsButton;

    private TextView nameTV;

    private TabLayout tabLayout;

    private ViewPager2 viewPager2;

    private ViewPagerFollowAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follows_following);

        exitFollowsButton = findViewById(R.id.exitFollowsButton);
        nameTV = findViewById(R.id.name_et);
        nameTV.setText(GlobalClass.user.getName());
        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 =findViewById(R.id.view_pager);


        myViewPagerAdapter = new ViewPagerFollowAdapter(this);
        viewPager2.setAdapter(myViewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        exitFollowsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backMainIntent = new Intent(FollowsFollowingActivity.this, MainActivity.class);
                startActivity(backMainIntent);
            }
        });
    }
}