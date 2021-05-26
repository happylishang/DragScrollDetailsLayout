package com.snail.dragscrolldetailslayout.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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