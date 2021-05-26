package com.snail.dragscrolldetailslayout.activity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.snail.dragscrolldetailslayout.R;
import com.snail.dragscrolldetailslayout.service.Constant;
import com.snail.dragscrolldetailslayout.service.MyWebViewClient;
import com.snail.dragscrolldetailslayout.view.DragScrollDetailsLayout;

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
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);//设置缓存模式
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(Constant.URL);
        DragScrollDetailsLayout md= (DragScrollDetailsLayout) findViewById(R.id.drag_lv);
        md.setPercent(0);
    }
}
