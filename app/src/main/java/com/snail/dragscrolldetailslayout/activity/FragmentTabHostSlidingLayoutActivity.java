package com.snail.dragscrolldetailslayout.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTabHost;

import com.snail.dragscrolldetailslayout.R;
import com.snail.dragscrolldetailslayout.view.DragScrollDetailsLayout;


public class FragmentTabHostSlidingLayoutActivity extends AppCompatActivity {
    FragmentTabHost fragmentTabHost;
    private Class[] mHomeFragments={FragmentItem2.class,FragmentItem1.class,FragmentItem2.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_tabhost_sliding);
        fragmentTabHost = (FragmentTabHost) findViewById(R.id.tablayout);
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.content);
        for(int i=0;i<mHomeFragments.length;i++){
            TabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec("" + i).setIndicator(""+i);
            fragmentTabHost.addTab(tabSpec, mHomeFragments[i], null);
        }

        findViewById(R.id.up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DragScrollDetailsLayout) findViewById(R.id.detail)).scrollToTop();
            }
        });
    }


}
