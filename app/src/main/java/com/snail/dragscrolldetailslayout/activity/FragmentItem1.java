package com.snail.dragscrolldetailslayout.activity;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.snail.dragscrolldetailslayout.R;
import com.snail.dragscrolldetailslayout.service.Constant;
import com.snail.dragscrolldetailslayout.service.MyWebViewClient;

/**
 * Created by netease on 16-7-25.
 */
public class FragmentItem1 extends Fragment {

    private WebView webView;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.item_webview, container, false);
            webView = (WebView) rootView.findViewById(R.id.webview);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new MyWebViewClient());
            webView.setWebChromeClient(new WebChromeClient());
            webView.loadUrl(Constant.URL);
        } else {
            ViewGroup v = ((ViewGroup) (rootView.getParent()));
            if (v != null) {
                v.removeView(rootView);
            }
        }
        return rootView;
    }

}
