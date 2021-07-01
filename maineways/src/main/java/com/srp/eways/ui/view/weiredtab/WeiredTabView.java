package com.srp.eways.ui.view.weiredtab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;

import ir.abmyapp.androidsdk.IABResources;

public class WeiredTabView extends View {

    public interface WeiredTabListener {
        void onWeiredTabSelected(int tabIndex);

        void onWeiredTabReselected(int tabIndex);
    }

    private static final int TAB_COUNT = 3;
    private static final long MAXIMUM_TAB_SELECTION_DURATION = 300;

    private GestureDetector mGestureDetector;

    private Paint mSelectedTabBackgroundPaint;
    private Paint mUnSelectedTabBackgroundPaint;

    private Paint mSelectedTabTitlePaint;
    private Paint mUnSelectedTabTitlePaint;

    private Drawable[] mTabIcons;
    private String[] mTabTitles;

    private int mTabSelectedColor;
    private int mTabUnSelectedColor;

    private int mSelectedTabBackgroundColor;
    private int mSelectedTabBackgroundStrokeColor;

    private int mSelectedTabStrokeWidth;

    private int mTabTitleSelectedColor;
    private int mTabTitleUnSelectedColor;

    private int mTabIconSize;
    private int mTabIconBackgroundRadius;

    private int mCenterCircleVisibleHeight;

    private int mSelectedTabTitleHeight;

    private int mTabTitleMarginTop;
    private int mTabTitleMarginBottom;

    private Rect[] mTabTitleBounds;

    private Rect mTitleTextBounds = new Rect();

    private int mCurrentTabIndex;

    private float mSelectionMovementSpeed;
    private long mLastDrawTime = 0;

    private float mTargetFocusPosition;
    private float mCurrentFocusedPosition = -1;

    private WeiredTabBackground mBackground;
    private int mBackColor;

    private WeiredTabListener mListener;

    public WeiredTabView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public WeiredTabView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public WeiredTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        mGestureDetector = new GestureDetector(context, mOnGestureListener);

        IABResources abResources = DIMain.getABResources();

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        int widthPixels = displayMetrics.widthPixels;

        mTabTitleMarginTop = (int) (((float) 6 / 600) * widthPixels * displayMetrics.density);
        mTabTitleMarginBottom = (int) (((float) 8 / 600) * widthPixels * displayMetrics.density);

        int selectedTabTitleColor = abResources.getColor(R.color.weiredtab_tabtitle_selected);
        int unselectedTabTitleColor = abResources.getColor(R.color.weiredtab_tabtitle_unselected);

        Typeface mSelectedTabTitleFont = ResourcesCompat.getFont(context, R.font.iran_yekan_medium);
        Typeface mUnSelectedTabTitleFont = ResourcesCompat.getFont(context, R.font.iran_yekan);

        float tabTitleTextSize = abResources.getDimen(R.dimen.weiredtab_tabtitle_textsize);

        mTabSelectedColor = abResources.getColor(R.color.bottomnav_tab_selected_color);
        mTabUnSelectedColor = abResources.getColor(R.color.bottomnav_tab_unselected_color);
        mTabTitleSelectedColor = abResources.getColor(R.color.bottomnav_tab_title_selected_color);
        mTabTitleUnSelectedColor = mTabUnSelectedColor;

        mSelectedTabBackgroundColor = abResources.getColor(R.color.weiredtab_tabbackground_selected);
        mSelectedTabBackgroundStrokeColor = abResources.getColor(R.color.weiredtab_tabbackground_selected_stroke);

        mBackColor = abResources.getColor(R.color.background);

        mSelectedTabBackgroundPaint = new Paint();
        mSelectedTabBackgroundPaint.setAntiAlias(true);
        mSelectedTabBackgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mSelectedTabBackgroundPaint.setColor(mSelectedTabBackgroundColor);

        mSelectedTabStrokeWidth = abResources.getDimenPixelSize(R.dimen.weiredtab_strokewidth);

        mUnSelectedTabBackgroundPaint = new Paint();
        mUnSelectedTabBackgroundPaint.setAntiAlias(true);
        mUnSelectedTabBackgroundPaint.setStyle(Paint.Style.FILL);
        mUnSelectedTabBackgroundPaint.setColor(abResources.getColor(R.color.weiredtab_tabbackground_unselected));

        mSelectedTabTitlePaint = new Paint();
        mSelectedTabTitlePaint.setAntiAlias(true);
        mSelectedTabTitlePaint.setStyle(Paint.Style.FILL);
        mSelectedTabTitlePaint.setColor(selectedTabTitleColor);
        mSelectedTabTitlePaint.setTextSize(tabTitleTextSize);
        mSelectedTabTitlePaint.setTypeface(mSelectedTabTitleFont);

        mUnSelectedTabTitlePaint = new Paint();
        mUnSelectedTabTitlePaint.setAntiAlias(true);
        mUnSelectedTabTitlePaint.setStyle(Paint.Style.FILL);
        mUnSelectedTabTitlePaint.setColor(unselectedTabTitleColor);
        mUnSelectedTabTitlePaint.setTextSize(tabTitleTextSize);
        mUnSelectedTabTitlePaint.setTypeface(mUnSelectedTabTitleFont);

        mTabIcons = new Drawable[TAB_COUNT];
        mTabTitles = new String[TAB_COUNT];

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WeiredTabView, defStyleAttr, defStyleAttr);

        for (int i = 0; i < a.length(); ++i) {
            int index = a.getIndex(i);

            if (index == R.styleable.WeiredTabView_weiredtab_icon1) {
                mTabIcons[0] = AppCompatResources.getDrawable(context, a.getResourceId(R.styleable.WeiredTabView_weiredtab_icon1, 0)).mutate();
            } else if (index == R.styleable.WeiredTabView_weiredtab_icon2) {
                mTabIcons[1] = AppCompatResources.getDrawable(context, a.getResourceId(R.styleable.WeiredTabView_weiredtab_icon2, 0)).mutate();
            } else if (index == R.styleable.WeiredTabView_weiredtab_icon3) {
                mTabIcons[2] = AppCompatResources.getDrawable(context, a.getResourceId(R.styleable.WeiredTabView_weiredtab_icon3, 0)).mutate();
            } else if (index == R.styleable.WeiredTabView_weiredtab_title1) {
                mTabTitles[0] = a.getString(R.styleable.WeiredTabView_weiredtab_title1);
            } else if (index == R.styleable.WeiredTabView_weiredtab_title2) {
                mTabTitles[1] = a.getString(R.styleable.WeiredTabView_weiredtab_title2);
            } else if (index == R.styleable.WeiredTabView_weiredtab_title3) {
                mTabTitles[2] = a.getString(R.styleable.WeiredTabView_weiredtab_title3);
            }
        }
        a.recycle();

        mTabTitleBounds = new Rect[TAB_COUNT];
        for (int i = 0; i < TAB_COUNT; ++i) {
            mTabTitleBounds[i] = new Rect();
        }

        for (int i = 0; i < mTabIcons.length; i++) {
            mTabIcons[i].setColorFilter(abResources.getColor(R.color.weiredtab_tab_icon_color), PorterDuff.Mode.SRC_IN);
        }

        Paint.FontMetrics fontMetrics = mSelectedTabTitlePaint.getFontMetrics();
        mSelectedTabTitleHeight = (int) (fontMetrics.descent - fontMetrics.ascent);

        int centerCircleRadius = (int) (((float) 75 / 600) * widthPixels);
        int sideCircleRadius = (int) (((float) 50 / 600) * widthPixels);

        mTabIconBackgroundRadius = (int) (((float) 50 / 600) * widthPixels);

//        mTabIconSize = (int)(((float)16 / 600) * widthPixels * displayMetrics.density);
        mTabIconSize = mTabIconBackgroundRadius * 2;

        mCenterCircleVisibleHeight = (int) (((float) 66 / 600) * widthPixels);

        mBackground = new WeiredTabBackground(context, centerCircleRadius, sideCircleRadius, mCenterCircleVisibleHeight, mTabIconBackgroundRadius, Color.parseColor("#82ac26"));
//        setBackground(mBackground);
        setBackgroundColor(mBackColor);
//        mBackground.setCallback(this);

        setSelectedTab(2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRect(-100, 0, view.getWidth() + 100, view.getHeight());
                }
            });
        }
    }

    public void setFillColor(int fillColor) {
        mBackground.setFillColor(fillColor);
    }

    public void setWeiredTabListener(WeiredTabListener listener) {
        mListener = listener;
    }

    public void setSelectedTab(int tab) {
        if (tab < 0 || tab >= mTabIcons.length) {
            throw new IllegalArgumentException("Invalid tab position: " + tab);
        }

        mCurrentTabIndex = tab;

        measureTargetFocusPosition();

        mLastDrawTime = System.currentTimeMillis();

        invalidate();
    }

    private void measureTargetFocusPosition() {
        if (getWidth() > 0) {
            float tabAreaWidth = (float) getWidth() / mTabIcons.length;

            mTargetFocusPosition = tabAreaWidth / 2 + tabAreaWidth * mCurrentTabIndex;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height;

        mSelectedTabTitlePaint.getTextBounds(mTabTitles[0], 0, mTabTitles[0].length(), mTitleTextBounds);

        height = mTabIconBackgroundRadius + mCenterCircleVisibleHeight + mTabTitleMarginTop + mTitleTextBounds.height() + mTabTitleMarginBottom;

        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = getWidth();
        int height = getHeight();

//        mBackground.setBounds(0, mTabIconBackgroundRadius, width, mTabIconBackgroundRadius + mCenterCircleVisibleHeight);

        measureTargetFocusPosition();

        measureTabIconBounds();

        mSelectionMovementSpeed = (width - (float) width / mTabIcons.length) / MAXIMUM_TAB_SELECTION_DURATION;

        mBackground.setBounds(0, 0, width, height);

        invalidate();
    }

    private void measureTabIconBounds() {
        final int width = getWidth();
        final int height = getHeight();

        int tabAreaWidth = width / mTabIcons.length;

        int tabLeft = (tabAreaWidth - mTabIconSize) / 2;
        int tabTitleLeft = 0;

//        int tabTop = (int)(mTabIconBackgroundRadius - mTabIconSize / 2);
        int tabTop = 0;
        int tabBottom = tabTop + mTabIconSize;

        for (int i = 0; i < mTabIcons.length; i++) {
            mTabIcons[i].setBounds(tabLeft, tabTop, tabLeft + mTabIconSize, tabBottom);
            mTabTitleBounds[i].set(tabTitleLeft, mTabIconBackgroundRadius + mCenterCircleVisibleHeight + mTabTitleMarginTop, tabTitleLeft + tabAreaWidth, mTabIconBackgroundRadius + mCenterCircleVisibleHeight + mTabTitleMarginTop + mSelectedTabTitleHeight);

            tabLeft += tabAreaWidth;
            tabTitleLeft += tabAreaWidth;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        measureCurrentFocusedPosition();

        mBackground.draw(canvas);

        for (int i = 0; i < mTabIcons.length; ++i) {
            canvas.drawCircle(mTabIcons[i].getBounds().centerX(), mTabIconBackgroundRadius + mSelectedTabStrokeWidth, mTabIconBackgroundRadius, mCurrentTabIndex == i ? mSelectedTabBackgroundPaint : mUnSelectedTabBackgroundPaint);

            if (mCurrentTabIndex == i) {
                mSelectedTabBackgroundPaint.setStyle(Paint.Style.STROKE);
                mSelectedTabBackgroundPaint.setColor(mSelectedTabBackgroundStrokeColor);
                mSelectedTabBackgroundPaint.setStrokeWidth(mSelectedTabStrokeWidth);

                canvas.drawCircle(mTabIcons[i].getBounds().centerX(), mTabIconBackgroundRadius + mSelectedTabStrokeWidth, mTabIconBackgroundRadius, mSelectedTabBackgroundPaint);

                mSelectedTabBackgroundPaint.setStyle(Paint.Style.FILL);
                mSelectedTabBackgroundPaint.setColor(mSelectedTabBackgroundColor);
                mSelectedTabBackgroundPaint.setStrokeWidth(0);
            }

            mTabIcons[i].draw(canvas);
        }

        for (int i = 0; i < mTabTitleBounds.length; ++i) {
            float textWidth = getTextWidth(i);
            canvas.drawText(mTabTitles[i], mTabTitleBounds[i].centerX() - textWidth / 2, mTabTitleBounds[i].centerY(), mCurrentTabIndex == i ? mSelectedTabTitlePaint : mUnSelectedTabTitlePaint);
        }

        if (mTargetFocusPosition != mCurrentFocusedPosition) {
            invalidate();
        }

        mLastDrawTime = System.currentTimeMillis();
    }

    private float getTextWidth(int tabIndex) {
        Paint paint = mCurrentTabIndex == tabIndex ? mSelectedTabTitlePaint : mUnSelectedTabTitlePaint;

        return paint.measureText(mTabTitles[tabIndex]);
    }

    private void measureCurrentFocusedPosition() {
        if (mCurrentFocusedPosition == -1) {
            mCurrentFocusedPosition = mTargetFocusPosition;
        } else {
            long now = System.currentTimeMillis();
            long passedTime = now - mLastDrawTime;
            float displacement = mSelectionMovementSpeed * passedTime;

            if (displacement > Math.abs(mCurrentFocusedPosition - mTargetFocusPosition)) {
                mCurrentFocusedPosition = mTargetFocusPosition;
            } else if (mCurrentFocusedPosition < mTargetFocusPosition) {
                mCurrentFocusedPosition += displacement;
            } else {
                mCurrentFocusedPosition -= displacement;
            }
        }

        mBackground.setPosition(mCurrentFocusedPosition);

        measureTabIconBounds();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            final float width = getWidth();

            float xPercentage = e.getX() / width;

            float tabPercentage = xPercentage * mTabIcons.length;

            final int selectedTab = (int) Math.floor(tabPercentage);

            if (mCurrentTabIndex == selectedTab) {
                mListener.onWeiredTabReselected(selectedTab);
            } else {
                setSelectedTab(selectedTab);
                mListener.onWeiredTabSelected(selectedTab);
            }


            return true;
        }

    };

}
