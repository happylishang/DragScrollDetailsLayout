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

public class FlingScrollDetailsLayout extends LinearLayout {


    public interface OnSlideFinishListener {
        void onStatueChanged(CurrentTargetIndex status);
    }

    private int mInitialOffSet;
    private ScrollDirection mScrollDirection;

    enum ScrollDirection {
        INVALID,
        VERTICAL,
        HORIZONTAL
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
    private int mUpStairsViewHeight;
    private CustomViewPager mCustomViewPager;

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

    public FlingScrollDetailsLayout(Context context) {
        this(context, null);
    }

    public FlingScrollDetailsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlingScrollDetailsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlingScrollDetailsLayout, defStyleAttr, 0);
        mPercent = a.getFloat(R.styleable.FlingScrollDetailsLayout_percent, DEFAULT_PERCENT);
        mDuration = a.getInt(R.styleable.FlingScrollDetailsLayout_duration, DEFAULT_DURATION);
        mDefaultPanel = a.getInt(R.styleable.FlingScrollDetailsLayout_default_panel, 0);
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
            throw new RuntimeException(" only accept childs more than 1!!");
        }
        mUpstairsView = getChildAt(0);
        mDownstairsView = getChildAt(1);

        if (mDownstairsView instanceof CustomViewPager) {
            mCustomViewPager = (CustomViewPager) mDownstairsView;
        } else if (mDownstairsView instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) mDownstairsView;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (viewGroup.getChildAt(i) instanceof CustomViewPager) {
                    mCustomViewPager = (CustomViewPager) viewGroup.getChildAt(i);
                }
            }
        }
    }

    /**
     * requestDisallowInterceptTouchEvent guarantee DragScrollDetailsLayout intercept event as wish
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                    mVelocityTracker.clear();
                }
                mScrollDirection = ScrollDirection.INVALID;
                mScroller.abortAnimation();
                mInitialOffSet = getScrollY();
                mDownMotionY = ev.getY();
                mDownMotionX = ev.getX();
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                }
                mVelocityTracker.clear();
                mChildHasScrolled = false;
                if (mCustomViewPager != null)
                    mCustomViewPager.setCanScroll(true);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mScrollDirection == ScrollDirection.INVALID) {
                    if (Math.abs(ev.getY() - mDownMotionY) > mTouchSlop) {
                        if (Math.abs(ev.getY() - mDownMotionY) < Math.abs(ev.getX() - mDownMotionX)) {
                            mScrollDirection = ScrollDirection.HORIZONTAL;
                        } else {
                            mScrollDirection = ScrollDirection.VERTICAL;
                            if (mCustomViewPager != null)
                                mCustomViewPager.setCanScroll(false);
                        }
                    }
                }
                if (mScrollDirection == ScrollDirection.VERTICAL) {
                    handlerScroll(ev);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                flingToFinishScroll();
                recycleVelocityTracker();
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void handlerScroll(MotionEvent ev) {

        if (ev.getY() - mDownMotionY < 0) {
            if (getScrollY() <= mUpStairsViewHeight) {
                scrollTo(0, (int) Math.min((mDownMotionY - ev.getY()) + mInitialOffSet, mUpStairsViewHeight));
            }
        } else {
            if (getScrollY() <= 0) {
                mDownMotionY = ev.getY();
                mDownMotionX = ev.getX();
            } else {
                if (canScrollVertically(mDownstairsView, (int) (mDownMotionY - ev.getY()), ev)) {
                    mDownMotionY = ev.getY();
                    mDownMotionX = ev.getX();
                } else {
                    scrollTo(0, (int) (mDownMotionY - ev.getY()) + mInitialOffSet);
                }
            }
        }
        mVelocityTracker.addMovement(ev);
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
        float scrollY = getScrollY();
        mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);
        if (scrollY >= mUpStairsViewHeight || (scrollY == 0 && mVelocityTracker.getYVelocity() > mMiniFlingVelocity)) {
            return;
        }
        float mVelocity = mVelocityTracker.getYVelocity();
        scrollY = mVelocity / 8;
        if (scrollY > 0) scrollY = Math.min(getScrollY(), scrollY);
        else scrollY = Math.max(getScrollY() - mUpStairsViewHeight, scrollY);
        mScroller.startScroll(0, getScrollY(), 0, (int) -scrollY, mDuration);
        postInvalidate();
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
     * 复用已经实现的View，省却了测量布局之类的麻烦,如果只是采用super.onMeasure，
     * LinearLayout会对View的MeasureSpec进行预处理导致获取不到底部View合理的尺寸，
     * super.onMeasure帮助获取LinearLayout的高度，measureChildren帮助确定所有View高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int measureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        mUpstairsView.measure(widthMeasureSpec, measureSpec);
        mUpStairsViewHeight = mUpstairsView.getMeasuredHeight();
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
        View showView = ((SlideFragmentPagerAdapter) viewPager.getAdapter()).getPrimaryItem();
        return showView != null && canScrollVertically(showView, offset, ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

}