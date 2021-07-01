package com.srp.eways.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.ABResources;
import ir.abmyapp.androidsdk.IABResources;

public class SolidTabBar extends View {

    // Velocity is percent per millisecond
    private static final float INDICATOR_VELOCITY = 0.25f / 100f;

    private static final float TOP_PADDING_RATIO = 0.571f;

    public interface OnTabSelectedListener {

        void onTabSelected(int tab);

    }

    private OnTabSelectedListener mOnTabSelectedListener = null;

    private GestureDetector mGestureDetector;

    private int mUnselectedTabColor;
    private int mIconColor;

    private float mDefaultTextSize;
    private int mHorizontalPadding;
    private int mTabWidth;

    private Paint mTitleSelectedPaint;
    private Paint mTitleUnselectedPaint;
    private Paint mIndicatorPaint;

    private float mIndicatorHeight;

    private List<Tab> mTabs = new ArrayList<>();

    private float mTabTitleY;
    private int mTabIconBottom;
    private int mTabIconSize;

    private int mSelectedTab = -1;

    private float mIndicatorWidth;

    private float mIndicatorTargetPosition = -1;
    private float mIndicatorPosition = -1;

    private RectF mIndicatorLeftRect = new RectF();
    private RectF mIndicatorBodyRect = new RectF();
    private RectF mIndicatorRightRect = new RectF();

    private long mLastDrawTime = 0;

    public SolidTabBar(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public SolidTabBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public SolidTabBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        mGestureDetector = new GestureDetector(context, mOnGestureListener);

        IABResources res = DIMain.getABResources();

        mUnselectedTabColor = res.getColor(R.color.solidtabbar_unselected);
        mIconColor = res.getColor(R.color.solidtabbar_icon);

        int indicatorColor = res.getColor(R.color.solidtabbar_indicator);
        mIndicatorPaint = new Paint();
        mIndicatorPaint.setAntiAlias(true);
        mIndicatorPaint.setStyle(Paint.Style.FILL);
        mIndicatorPaint.setColor(indicatorColor);

        mIndicatorHeight = res.getDimen(R.dimen.solidtabbar_indicator_height);

        int titleColor = res.getColor(R.color.solidtabbar_title);
        mTitleSelectedPaint = new Paint();
        mTitleSelectedPaint.setAntiAlias(true);
        mTitleSelectedPaint.setStyle(Paint.Style.FILL);
        mTitleSelectedPaint.setColor(titleColor);
        mTitleSelectedPaint.setTypeface(ResourcesCompat.getFont(context, R.font.iran_yekan_medium));
        mTitleSelectedPaint.setTextAlign(Paint.Align.CENTER);

        mTitleUnselectedPaint = new Paint(mTitleSelectedPaint);
        mTitleUnselectedPaint.setColor(mUnselectedTabColor);
        mTitleSelectedPaint.setTypeface(ResourcesCompat.getFont(context, R.font.iran_yekan));

        mDefaultTextSize = res.getDimen(R.dimen.solidtabbar_tab_default_textsize);
        mTabIconSize = res.getDimenPixelSize(R.dimen.solidtabbar_icon_size);

        setBackground(new ColorDrawable(res.getColor(R.color.white)));
    }

    public void setTabDefaultTextSize(float textSize) {
        mDefaultTextSize = textSize;

        requestLayout();
    }

    public void setOnTabSelectedListener(OnTabSelectedListener listener) {
        mOnTabSelectedListener = listener;
    }

    public void addTab(String title, Drawable icon) {
        mTabs.add(new Tab(title, icon.mutate()));

        requestLayout();
    }

    public int getSelectedTab() {
        return mSelectedTab;
    }

    public void setSelectedTab(int tab) {
        setSelectedTab(tab, mSelectedTab != -1);
    }

    public void setSelectedTab(int tab, boolean animate) {
        if (mSelectedTab == tab) {
            return;
        }

        if (mSelectedTab != -1) {
            mTabs.get(mSelectedTab).setSelected(false);
        }

        mSelectedTab = tab;

        if (mSelectedTab != -1) {
            mTabs.get(mSelectedTab).setSelected(true);
        }

        mIndicatorTargetPosition = getIndicatorPositionForTab(mSelectedTab);

        if (!animate) {
            mIndicatorPosition = mIndicatorTargetPosition;
        }

        invalidate();
    }

    public void setIndicatorPosition(int tab, float progress) {
        float current = getIndicatorPositionForTab(tab);

        if (progress > 0) {
            float target = getIndicatorPositionForTab(tab + 1);

            mIndicatorTargetPosition = current * (1 - progress) + target * progress;
        }
        else {
            float target = getIndicatorPositionForTab(tab - 1);

            mIndicatorTargetPosition = current * (1 + progress) - target * progress;
        }

        mIndicatorPosition = mIndicatorTargetPosition;

        mLastDrawTime = 0;
        measureIndicator();

        invalidate();
    }

    private float getIndicatorPositionForTab(int tab) {
        if (tab == -1) {
            return -1;
        }

        return mTabs.get(tab).centerX;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = ABResources.get(getContext()).getDimenPixelSize(R.dimen.solidtabbar_height);

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        final int width = getWidth();
        final int height = getHeight();

        IABResources res = ABResources.get(getContext());

        int paddingHorizontal = res.getDimenPixelSize(R.dimen.solidtabbar_padding_horizontal);
        int tabPaddingHorizontal = res.getDimenPixelSize(R.dimen.solidtabbar_tab_padding_horizontal);
        float tabTextMarginTop = res.getDimen(R.dimen.solidtabbar_tab_text_margin_top);

        if (mTabs.isEmpty()) {
            return;
        }

        int availableWidthForEachTab = (width - 2 * paddingHorizontal - (mTabs.size() - 1) * tabPaddingHorizontal) / mTabs.size();
        measureTabTextSize(mDefaultTextSize, availableWidthForEachTab);

        mIndicatorWidth = availableWidthForEachTab;

        mHorizontalPadding = paddingHorizontal;
        mTabWidth = availableWidthForEachTab + tabPaddingHorizontal;

        Paint.FontMetrics fm = mTitleSelectedPaint.getFontMetrics();

        float availableSpace = height - mTabIconSize - fm.bottom + fm.top;
        float paddingTop = availableSpace * TOP_PADDING_RATIO;
        float paddingBottom = availableSpace - paddingTop;

        float titleBottom = height - paddingBottom;
        float titleTop = titleBottom - (fm.bottom - fm.top);

        mTabTitleY = titleBottom - fm.bottom;

        mTabIconBottom = (int) (titleTop - tabTextMarginTop);

        int x = paddingHorizontal + availableWidthForEachTab / 2;

        for (Tab tab: mTabs) {
            tab.measure(x);

            x += availableWidthForEachTab + tabPaddingHorizontal;
        }

        mIndicatorTargetPosition = getIndicatorPositionForTab(mSelectedTab);
        mIndicatorPosition = mIndicatorTargetPosition;
        measureIndicator();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Tab tab: mTabs) {
            tab.draw(canvas);
        }

        if (mIndicatorPosition != -1) {
            drawIndicator(canvas);
        }

        onAnimationFrame();
    }

    private void onAnimationFrame() {
        if (mIndicatorPosition == mIndicatorTargetPosition) {
            return;
        }

        if (mIndicatorTargetPosition == -1) {
            mIndicatorPosition = -1;

            mLastDrawTime = 0;

            measureIndicator();
            invalidate();
            return;
        }

        if (mLastDrawTime == 0) {
            mLastDrawTime = System.currentTimeMillis();

            measureIndicator();
            invalidate();
            return;
        }

        long now = System.currentTimeMillis();
        long passedTime = now - mLastDrawTime;

        mLastDrawTime = now;

        float distance = INDICATOR_VELOCITY * passedTime * getWidth();

        if (mIndicatorPosition > mIndicatorTargetPosition) {
            mIndicatorPosition -= distance;

            if (mIndicatorPosition < mIndicatorTargetPosition) {
                mIndicatorPosition = mIndicatorTargetPosition;
                mLastDrawTime = 0;
            }
        }
        else {
            mIndicatorPosition += distance;

            if (mIndicatorPosition > mIndicatorTargetPosition) {
                mIndicatorPosition = mIndicatorTargetPosition;
                mLastDrawTime = 0;
            }
        }

        measureIndicator();
        invalidate();
    }

    private void drawIndicator(Canvas canvas) {
        canvas.drawArc(mIndicatorLeftRect, 180,90, true, mIndicatorPaint);
        canvas.drawRect(mIndicatorBodyRect, mIndicatorPaint);
        canvas.drawArc(mIndicatorRightRect, 270,90, true, mIndicatorPaint);
    }

    private void measureIndicator() {
        int height = getHeight();

        mIndicatorLeftRect.left = mIndicatorPosition - mIndicatorWidth / 2;
        mIndicatorLeftRect.top = height - mIndicatorHeight;
        mIndicatorLeftRect.right = mIndicatorLeftRect.left + 2 * mIndicatorHeight;
        mIndicatorLeftRect.bottom = height + mIndicatorHeight;

        mIndicatorBodyRect.left = mIndicatorLeftRect.right - mIndicatorHeight;
        mIndicatorBodyRect.top = mIndicatorLeftRect.top;
        mIndicatorBodyRect.right = mIndicatorPosition + mIndicatorWidth / 2 - mIndicatorHeight;
        mIndicatorBodyRect.bottom = height;

        mIndicatorRightRect.left = mIndicatorBodyRect.right - mIndicatorHeight;
        mIndicatorRightRect.top = mIndicatorLeftRect.top;
        mIndicatorRightRect.right = mIndicatorBodyRect.right + mIndicatorHeight;
        mIndicatorRightRect.bottom = mIndicatorLeftRect.bottom;
    }

    private void measureTabTextSize(float defaultTextSize, int width) {
        float textSize = defaultTextSize;

        mTitleSelectedPaint.setTextSize(textSize);

        float requiredWidth = measureRequiredTitleWidth(mTitleSelectedPaint);

        while (requiredWidth > width && textSize > 0) {
            textSize -= 0.5f;

            mTitleSelectedPaint.setTextSize(textSize);

            requiredWidth = measureRequiredTitleWidth(mTitleSelectedPaint);
        }

        mTitleUnselectedPaint.setTextSize(textSize);
    }

    private float measureRequiredTitleWidth(Paint paint) {
        float maxWidth = 0;

        for (Tab tab: mTabs) {
            maxWidth = Math.max(maxWidth, paint.measureText(tab.title));
        }

        return maxWidth;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.OnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) { }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            float x = e.getX();

            int selectedTab = -1;

            x -= mHorizontalPadding;

            if (mTabs.isEmpty()) {
                selectedTab = -1;
            }
            else if (x < 0) {
                selectedTab = 0;
            }
            else {
                for (int i = 0; i < mTabs.size(); i++) {
                    selectedTab = i;

                    if (x < mTabWidth) {
                        break;
                    }

                    x -= mTabWidth;
                }
            }

            if (selectedTab != -1) {
                setSelectedTab(selectedTab, true);

                if (mOnTabSelectedListener != null) {
                    mOnTabSelectedListener.onTabSelected(mSelectedTab);
                }
            }

            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) { }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

    };

    private class Tab {

        private Drawable icon;
        private String title;

        private float centerX;

        private Paint titlePaint;

        Tab(String title, Drawable icon) {
            this.title = title;
            this.icon = icon;

            setSelected(false);
        }

        private void measure(int centerX) {
            icon.setBounds(
                    centerX - mTabIconSize / 2,
                    mTabIconBottom - mTabIconSize,
                    centerX + mTabIconSize / 2,
                    mTabIconBottom);

            this.centerX = centerX;
        }

        private void setSelected(boolean selected) {
            titlePaint = selected ? mTitleSelectedPaint : mTitleUnselectedPaint;
            icon.setColorFilter(selected ? mIconColor : mUnselectedTabColor, PorterDuff.Mode.SRC_IN);
        }

        private void draw(Canvas canvas) {
            icon.draw(canvas);
            canvas.drawText(title, centerX, mTabTitleY, titlePaint);
        }

    }

}
