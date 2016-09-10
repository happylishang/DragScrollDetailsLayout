package com.snail.labaffinity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.snail.labaffinity.R;
import com.snail.labaffinity.adapter.SlideFragmentPagerAdapter;
import com.snail.labaffinity.service.BackGroundService;
import com.snail.labaffinity.view.FlingScrollDetailsLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewPagerlidingLayoutActivity
        extends AppCompatActivity {
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.drag_content)
    FlingScrollDetailsLayout mDragScrollDetailsLayout;
    @BindView(R.id.flag_tips)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_viewpager_sliding);
        ButterKnife.bind(this);
        Intent intent = new Intent(ViewPagerlidingLayoutActivity.this, BackGroundService.class);
        startService(intent);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
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
        mDragScrollDetailsLayout.setOnSlideDetailsListener(new FlingScrollDetailsLayout.OnSlideFinishListener() {
            @Override
            public void onStatueChanged(FlingScrollDetailsLayout.CurrentTargetIndex status) {
                if (status == FlingScrollDetailsLayout.CurrentTargetIndex.UPSTAIRS)
                    mTextView.setText("pull up to show more");
                else
                    mTextView.setText("pull down to top");

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}