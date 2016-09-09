package com.snail.labaffinity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.snail.labaffinity.R;
import com.snail.labaffinity.service.BackGroundService;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        Intent intent = new Intent(MainActivity.this, BackGroundService.class);
        startService(intent);
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

    @OnClick(R.id.sliding_tabhost)
    void slidingTabhost() {
        Intent intent = new Intent(MainActivity.this, FragmentTabHostSlidingLayoutActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.sliding_viewpager)
    void viewpager() {
        Intent intent = new Intent(MainActivity.this, ViewPagerlidingLayoutActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.sliding_webivew)
    void webview() {
        Intent intent = new Intent(MainActivity.this, DragWebViewActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.sliding_list)
    void list() {
        Intent intent = new Intent(MainActivity.this, DragListViewActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.sliding_webview_list)
    void weblist() {
        Intent intent = new Intent(MainActivity.this, DragWebViewListActivity.class);
        startActivity(intent);
    }

}
