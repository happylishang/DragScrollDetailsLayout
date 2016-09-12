package com.snail.labaffinity.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Author: hzlishang
 * Data: 16/9/12 上午9:44
 * Des:
 * version:
 */
public class CustomViewPager extends ViewPager {

    private boolean mCanScroll;

    public CustomViewPager(Context context) {
        this(context, null);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCanScroll(boolean canScroll) {
        mCanScroll = canScroll;
    }

    public boolean isCanScroll() {
        return mCanScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mCanScroll &&super.onInterceptTouchEvent(ev);
    }
}
