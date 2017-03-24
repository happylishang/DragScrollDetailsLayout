package com.snail.labaffinity.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.snail.labaffinity.R;
import com.snail.labaffinity.adapter.DragDetailFragmentPagerAdapter;

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
    private static final int DEFAULT_DURATION = 300;


    private int mMaxFlingVelocity;
    private int mMiniFlingVelocity;
    private int mDefaultPanel = 0;
    private int mDuration = DEFAULT_DURATION;
    private float mTouchSlop;
    private float mDownMotionY;
    private float mDownMotionX;
    private float mInitialInterceptY;

    public void setPercent(float percent) {
        mPercent = percent;
    }

    private float mPercent = DEFAULT_PERCENT;
    /**
     * flag for listview or scrollview ,if child overscrolled ,do not judge view region 滚过头了，还是可以滚动
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
        mScroller = new Scroller(getContext(), new DecelerateInterpolator());
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
     * requestDisallowInterceptTouchEvent guarantee DragScrollDetailsLayout intercept event as wish
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!mScroller.isFinished()) {
            resetDownPosition(ev);
            return true;
        }
        Log.v("lishang", "" + getScrollY());
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
                resetDownPosition(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                adjustValidDownPoint(ev);
                return checkCanInterceptTouchEvent(ev);
            default:
                break;
        }
        return false;
    }

    private void resetDownPosition(MotionEvent ev) {
        mDownMotionX = ev.getX();
        mDownMotionY = ev.getY();
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.clear();
        mChildHasScrolled = false;
        mInitialInterceptY = (int) ev.getY();
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
        if (!canChildScrollVertically((int) yDiff, ev)) {
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

    /**
     * 拦截之后的拖动
     */
    private void scroll(MotionEvent event) {
        if (mCurrentViewIndex == CurrentTargetIndex.UPSTAIRS) {
            if (getScrollY() <= 0 && event.getY() >= mInitialInterceptY) {
                mInitialInterceptY = (int) event.getY();
            }
            int distance = mInitialInterceptY - event.getY() >= 0 ? (int) (mInitialInterceptY - event.getY()) : 0;
            scrollTo(0, distance);
        } else {
            if (getScrollY() >= mUpstairsView.getMeasuredHeight() && event.getY() <= mInitialInterceptY) {
                mInitialInterceptY = (int) event.getY();
            }
            int distance = event.getY() <= mInitialInterceptY ? mUpstairsView.getMeasuredHeight()
                    : (int) (mInitialInterceptY - event.getY() + mUpstairsView.getMeasuredHeight());
            scrollTo(0, distance);
        }
        mVelocityTracker.addMovement(event);
    }


    /**
     * 清理VelocityTracker
     */

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
        float needFlingDistance = 0;
        if (CurrentTargetIndex.UPSTAIRS == mCurrentViewIndex) {
            if (getScrollY() <= 0) {
                needFlingDistance = 0;
            } else if (getScrollY() <= threshold) {
                if (needFlingToToggleView()) {
                    needFlingDistance = pHeight - getScrollY();
                    mCurrentViewIndex = CurrentTargetIndex.DOWNSTAIRS;
                } else {
                    needFlingDistance = -getScrollY();
                }
            } else {
                needFlingDistance = pHeight - getScrollY();
                mCurrentViewIndex = CurrentTargetIndex.DOWNSTAIRS;
            }
        } else if (CurrentTargetIndex.DOWNSTAIRS == mCurrentViewIndex) {
            if (pHeight <= getScrollY()) {
                needFlingDistance = 0;
            } else if (pHeight - getScrollY() < threshold) {
                if (needFlingToToggleView()) {
                    needFlingDistance = -getScrollY();
                    mCurrentViewIndex = CurrentTargetIndex.UPSTAIRS;
                } else {
                    needFlingDistance = pHeight - getScrollY();
                }
            } else {
                needFlingDistance = -getScrollY();
                mCurrentViewIndex = CurrentTargetIndex.UPSTAIRS;
            }
        }
        mScroller.startScroll(0, getScrollY(), 0, (int) needFlingDistance, mDuration);
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

    protected boolean canChildScrollVertically(int offSet, MotionEvent ev) {
        mCurrentTargetView = getCurrentTargetView();
        return canScrollVertically(mCurrentTargetView, -offSet, ev);
    }

    /***
     * judge is event  is in current view
     * 判断MotionEvent是否处于View上面
     */
    protected boolean isTransformedTouchPointInView(MotionEvent ev, View view) {
        float x = ev.getRawX();
        float y = ev.getRawY();
        int[] rect = new int[2];
        view.getLocationInWindow(rect);
        float localX = x - rect[0];
        float localY = y - rect[1];
        return localX >= 0 && localX < (view.getRight() - view.getLeft())
                && localY >= 0 && localY < (view.getBottom() - view.getTop());
    }

    /***
     * first    can view self  ScrollVertically
     * seconde  if View is ViewPager only judge current page
     * third    if view is viewgroup check it`s children
     */
    private boolean canScrollVertically(View view, int offSet, MotionEvent ev) {

        if (!mChildHasScrolled && !isTransformedTouchPointInView(ev, view)) {
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

    private boolean canViewPagerScrollVertically(ViewPager viewPager, int offset, MotionEvent ev) {

        if(viewPager.getAdapter() instanceof DragDetailFragmentPagerAdapter){
            View showView = ((DragDetailFragmentPagerAdapter) viewPager.getAdapter()).getPrimaryItem();
            return showView != null && canScrollVertically(showView, offset, ev);
        }else {
            return false;
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void scrollToTop() {
        if (mCurrentViewIndex == CurrentTargetIndex.DOWNSTAIRS) {
            mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), mDuration);
            mCurrentViewIndex=CurrentTargetIndex.UPSTAIRS;
            postInvalidate();
        }
        if (mOnSlideDetailsListener != null) {
            mOnSlideDetailsListener.onStatueChanged(mCurrentViewIndex);
        }
    }
}