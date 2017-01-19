package com.snail.labaffinity.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.snail.labaffinity.R;
import com.snail.labaffinity.service.Constant;
import com.snail.labaffinity.service.MyWebViewClient;
import com.snail.labaffinity.view.DragScrollDetailsLayout;

/**
 * Author: hzlishang
 * Data: 16/8/18 下午8:39
 * Des:
 * version:
 */
public class DragWebViewListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_webview_list);
        WebView webView= (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(Constant.URL);
        DragScrollDetailsLayout md= (DragScrollDetailsLayout) findViewById(R.id.drag_lv);
        md.setPercent(0);
    }
}
