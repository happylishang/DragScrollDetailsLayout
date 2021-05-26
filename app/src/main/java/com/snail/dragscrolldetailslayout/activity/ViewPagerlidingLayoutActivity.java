package com.snail.dragscrolldetailslayout.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.snail.dragscrolldetailslayout.R;
import com.snail.dragscrolldetailslayout.adapter.SlideFragmentPagerAdapter;
import com.snail.dragscrolldetailslayout.service.BackGroundService;
import com.snail.dragscrolldetailslayout.view.DragScrollDetailsLayout;


public class ViewPagerlidingLayoutActivity
        extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_viewpager_sliding);
        Intent intent = new Intent(ViewPagerlidingLayoutActivity.this, BackGroundService.class);
        startService(intent);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        ViewPager viewPager = ((ViewPager) findViewById(R.id.viewpager));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setScrollPosition(position, 0, false);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(new SlideFragmentPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        DragScrollDetailsLayout mDragScrollDetailsLayout = findViewById(R.id.drag_content);
        mDragScrollDetailsLayout.setOnSlideDetailsListener(new DragScrollDetailsLayout.OnSlideFinishListener() {
            @Override
            public void onStatueChanged(DragScrollDetailsLayout.CurrentTargetIndex status) {
                TextView mTextview = findViewById(R.id.flag_tips);
                if (status == DragScrollDetailsLayout.CurrentTargetIndex.UPSTAIRS)
                    mTextview.setText("pull up to show more");
                else
                    mTextview.setText("pull down to top");

            }
        });
    }

}