package com.snail.dragscrolldetailslayout.adapter;



import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.snail.dragscrolldetailslayout.activity.FragmentItem1;
import com.snail.dragscrolldetailslayout.activity.FragmentItem2;

/**
 * Author: hzlishang
 * Data: 16/7/30 下午11:27
 * Des:
 * version:
 */
public class SlideFragmentPagerAdapter extends DragDetailFragmentPagerAdapter {
    public SlideFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position % 2 == 0)
            return new FragmentItem2();
        return new FragmentItem1();
    }

    @Override
    public int getCount() {
        return 3;
    }

    private View mCurrentView;

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

    @Override
    public CharSequence getPageTitle(int position) {
        return "Tab"+position;
    }
}