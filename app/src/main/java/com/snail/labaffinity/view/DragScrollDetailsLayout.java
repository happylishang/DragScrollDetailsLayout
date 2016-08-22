package com.snail.labaffinity.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.snail.labaffinity.R;
import com.snail.labaffinity.adapter.SlideFragmentPagerAdapter;

/**
 * 1 ViewPager+TabLayout as content
 * 2 FragmentTabHost+Fragment as content
 * 3 bug :can not assertain which View is inTouch
 */

public class DragScrollDetailsLayout extends LinearLayout {


    public interface OnSlideFinishListener {
        void onStatueChanged(CurrentTargetIndex status);
    }

    public enum CurrentTargetIndex {
        UPSTAIRS,
        DOWNSTAIRS;

        public static CurrentTargetIndex valueOf(int index) {
            return 1 == index ? DOWNSTAIRS : UPSTAIRS;
        }
    }

    private static final float DEFAULT_PERCENT = 0.3f;
    private static final int DEFAULT_DURATION = 400;


    private int mMaxFlingVelocity;
    private int mMiniFlingVelocity;
    private int mDefaultPanel = 0;
    private int mDuration = DEFAULT_DURATION;
    private float mTouchSlop;
    private float mDownMotionY;
    private float mDownMotionX;
    private float mInitialInterceptY;
    private float mPercent = DEFAULT_PERCENT;
    /**
     * flag for listview or scrollview ,if child overscrolled ,do not judge view region
     */
    private boolean mChildHasScrolled;

    private View mUpstairsView;
    private View mDownstairsView;
    private View mCurrentTargetView;

    private Scroller mScroller;

    private VelocityTracker mVelocityTracker;
    private OnSlideFinishListener mOnSlideDetailsListener;
    private CurrentTargetIndex mCurrentViewIndex = CurrentTargetIndex.UPSTAIRS;

    public DragScrollDetailsLayout(Context context) {
        this(context, null);
    }

    public DragScrollDetailsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragScrollDetailsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DragScrollDetailsLayout, defStyleAttr, 0);
        mPercent = a.getFloat(R.styleable.DragScrollDetailsLayout_percent, DEFAULT_PERCENT);
        mDuration = a.getInt(R.styleable.DragScrollDetailsLayout_duration, DEFAULT_DURATION);
        mDefaultPanel = a.getInt(R.styleable.DragScrollDetailsLayout_default_panel, 0);
        a.recycle();
        mScroller = new Scroller(getContext());
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mMaxFlingVelocity = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
        mMiniFlingVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        setOrientation(VERTICAL);
    }

    public void setOnSlideDetailsListener(OnSlideFinishListener listener) {
        this.mOnSlideDetailsListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        final int childCount = getChildCount();
        if (1 >= childCount) {
            throw new RuntimeException("SlideDetailsLayout only accept childs more than 1!!");
        }
        mUpstairsView = getChildAt(0);
        mDownstairsView = getChildAt(1);
    }

    /**
     * requestDisallowInterceptTouchEvent guarantee intercept event as DragScrollDetailsLayout wish
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        requestDisallowInterceptTouchEvent(false);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * intercept rules：
     * 1. The vertical displacement is larger than the horizontal displacement;
     * 2. Panel stauts is UPSTAIRS：  slide up
     * 3. Panel status is DOWNSTAIRS：slide down
     * 4. child can requestDisallowInterceptTouchEvent();
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mDownMotionX = ev.getX();
                mDownMotionY = ev.getY();
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                }
                mVelocityTracker.clear();
                mChildHasScrolled=false;
                break;
            case MotionEvent.ACTION_MOVE:
                adjustValidDownPoint(ev);
                return checkCanInterceptTouchEvent(ev);
            default:
                break;
        }
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                flingToFinishScroll();
                recycleVelocityTracker();
                break;
            case MotionEvent.ACTION_MOVE:
                scroll(ev);
                break;
            default:
                break;
        }
        return true;
    }


    private boolean checkCanInterceptTouchEvent(MotionEvent ev) {
        final float xDiff = ev.getX() - mDownMotionX;
        final float yDiff = ev.getY() - mDownMotionY;
        if (!canChildScrollVertically((int) yDiff,ev)) {
            mInitialInterceptY = (int) ev.getY();
            if (Math.abs(yDiff) > mTouchSlop && Math.abs(yDiff) >= Math.abs(xDiff)
                    && !(mCurrentViewIndex == CurrentTargetIndex.UPSTAIRS && yDiff > 0
                    || mCurrentViewIndex == CurrentTargetIndex.DOWNSTAIRS && yDiff < 0)) {
                return true;
            }
        }
        return false;
    }



    private void adjustValidDownPoint(MotionEvent event) {
        if (mCurrentViewIndex == CurrentTargetIndex.UPSTAIRS && event.getY() > mDownMotionY
                || mCurrentViewIndex == CurrentTargetIndex.DOWNSTAIRS && event.getY() < mDownMotionY) {
            mDownMotionX = event.getX();
            mDownMotionY = event.getY();
        }
    }

    private void scroll(MotionEvent event) {
        if (mCurrentViewIndex == CurrentTargetIndex.UPSTAIRS) {
            if (getScrollY() <= 0 && event.getY() > mInitialInterceptY) {
                mInitialInterceptY = (int) event.getY();
            }
            scrollTo(0, (int) (mInitialInterceptY - event.getY()));
        } else {
            if (getScrollY() >= mUpstairsView.getMeasuredHeight() && event.getY() < mInitialInterceptY) {
                mInitialInterceptY = (int) event.getY();
            }
            scrollTo(0, (int) (mInitialInterceptY - event.getY() + mUpstairsView.getMeasuredHeight()));
        }
        mVelocityTracker.addMovement(event);
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * if speed is enough even though offset is not enough go
     */

    private void flingToFinishScroll() {

        final int pHeight = mUpstairsView.getMeasuredHeight();
        final int threshold = (int) (pHeight * mPercent);
        float scrollY = getScrollY();
        if (CurrentTargetIndex.UPSTAIRS == mCurrentViewIndex) {
            if (scrollY <= 0) {
                scrollY = 0;
            } else if (scrollY <= threshold) {
                if (needFlingToToggleView()) {
                    scrollY = pHeight - getScrollY();
                    mCurrentViewIndex = CurrentTargetIndex.DOWNSTAIRS;
                } else
                    scrollY = -getScrollY();
            } else {
                scrollY = pHeight - getScrollY();
                mCurrentViewIndex = CurrentTargetIndex.DOWNSTAIRS;
            }
        } else if (CurrentTargetIndex.DOWNSTAIRS == mCurrentViewIndex) {
            if (pHeight - scrollY <= threshold) {
                if (needFlingToToggleView()) {
                    scrollY = -getScrollY();
                    mCurrentViewIndex = CurrentTargetIndex.UPSTAIRS;
                } else
                    scrollY = pHeight - scrollY;
            } else if (scrollY < pHeight) {
                scrollY = -getScrollY();
                mCurrentViewIndex = CurrentTargetIndex.UPSTAIRS;
            }
        }
        mScroller.startScroll(0, getScrollY(), 0, (int) scrollY, mDuration);
        if (mOnSlideDetailsListener != null) {
            mOnSlideDetailsListener.onStatueChanged(mCurrentViewIndex);
        }
        postInvalidate();
    }


    private boolean needFlingToToggleView() {
        mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);
        if (mCurrentViewIndex == CurrentTargetIndex.UPSTAIRS) {
            if (-mVelocityTracker.getYVelocity() > mMiniFlingVelocity) {
                return true;
            }
        } else {
            if (mVelocityTracker.getYVelocity() > mMiniFlingVelocity) {
                return true;
            }
        }
        return false;
    }

    private View getCurrentTargetView() {
        return mCurrentViewIndex == CurrentTargetIndex.UPSTAIRS
                ? mUpstairsView : mDownstairsView;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }

    /***
     * 复用已经实现的View，省却了测量布局之类的麻烦
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }

    protected boolean canChildScrollVertically(int offSet,MotionEvent ev) {
        mCurrentTargetView = getCurrentTargetView();
        return canScrollVertically(mCurrentTargetView, -offSet, ev);
    }

    /***
     * judge is event  is in current view
     */
    protected boolean isTransformedTouchPointInView(MotionEvent ev, View child, ViewGroup parent) {
        float x = ev.getRawX();
        float y = ev.getRawY();
        int[] rect = new int[2];
        child.getLocationInWindow(rect);
        float localX = x - rect[0];
        float localY = y - rect[1];
        return localX >= 0 && localX < (child.getRight() - child.getLeft())
                && localY >= 0 && localY < (child.getBottom() - child.getTop());
    }

    /***
     * first    can view self  ScrollVertically
     * seconde  if View is ViewPager only judge current page
     * third    if view is viewgroup check it`s children
     */
    private boolean canScrollVertically(View view, int offSet, MotionEvent ev) {

        if (!mChildHasScrolled && !isTransformedTouchPointInView(ev, view, (ViewGroup) view.getParent())) {
            return false;
        }
        if (ViewCompat.canScrollVertically(view, offSet)) {
            mChildHasScrolled = true;
            return true;
        }
        if (view instanceof ViewPager) {
            return canViewPagerScrollVertically((ViewPager) view, offSet, ev);
        }
        if (view instanceof ViewGroup) {
            ViewGroup vGroup = (ViewGroup) view;
            for (int i = 0; i < vGroup.getChildCount(); i++) {
                if (canScrollVertically(vGroup.getChildAt(i), offSet, ev)) {
                    mChildHasScrolled = true;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canViewPagerScrollVertically(ViewPager viewPager, int offset,MotionEvent ev) {
        View showView = ((SlideFragmentPagerAdapter) viewPager.getAdapter()).getPrimaryItem();
        return showView != null && canScrollVertically(showView, offset, ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

}