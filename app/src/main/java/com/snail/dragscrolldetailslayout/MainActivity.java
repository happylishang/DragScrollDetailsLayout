package com.snail.dragscrolldetailslayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.snail.dragscrolldetailslayout.R;
import com.snail.dragscrolldetailslayout.activity.DragListViewActivity;
import com.snail.dragscrolldetailslayout.activity.DragWebViewActivity;
import com.snail.dragscrolldetailslayout.activity.DragWebViewListActivity;
import com.snail.dragscrolldetailslayout.activity.FragmentTabHostSlidingLayoutActivity;
import com.snail.dragscrolldetailslayout.activity.ViewPagerlidingLayoutActivity;
import com.snail.dragscrolldetailslayout.service.BackGroundService;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.sliding_tabhost).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FragmentTabHostSlidingLayoutActivity.class);
            startActivity(intent);
        });


        findViewById(R.id.sliding_viewpager).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewPagerlidingLayoutActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.sliding_webivew).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DragWebViewActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.sliding_list).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DragListViewActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.sliding_webview_list).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DragWebViewListActivity.class);
            startActivity(intent);
        });

    }



}
