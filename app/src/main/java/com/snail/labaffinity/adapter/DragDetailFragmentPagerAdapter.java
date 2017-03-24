package com.snail.labaffinity.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author: hzlishang
 * Data: 16/7/30 下午11:27
 * Des:
 * version:
 */
public abstract class DragDetailFragmentPagerAdapter extends FragmentPagerAdapter {

    private View mCurrentView;

    public DragDetailFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (object instanceof View) {
            mCurrentView = (View) object;
        } else if (object instanceof Fragment) {
            Fragment fragment = (Fragment) object;
            mCurrentView = fragment.getView();
        }
    }
    public View getPrimaryItem() {
        return mCurrentView;
    }
}