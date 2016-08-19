package com.snail.labaffinity.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.snail.labaffinity.R;

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
