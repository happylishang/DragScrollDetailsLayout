package com.snail.dragscrolldetailslayout.service;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Author: hzlishang
 * Data: 17-1-19 下午12:41
 * Des:
 * version:
 */

public class MyWebViewClient  extends WebViewClient {

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
         handler.proceed();
    }
}
