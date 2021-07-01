package com.srp.ewayspanel.ui.view.bottomnavigation;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import com.srp.eways.ui.view.ShadowLayout;
import com.srp.eways.ui.view.charge.PhoneAndOperatorsView;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.appservice.AppService;
import com.srp.ewayspanel.ui.view.landing.AppServiceItemView;

public class BottomNavigationView extends View {

    public static int TAB_COUNT = 5;
    private static final long MAXIMUM_TAB_SELECTION_DURATION = 500;

    public interface BottomNavigationClickListener {
        void onTabClicked(int tabIndex);

        void onTabReselected(int tabIndex);
    }

    private GestureDetector mGestureDetector;

    private Paint mTitlePaint;
    private Paint mSelectionCirclePaint;

    private Drawable[] mTabIcons;
    private Drawable[] mTabSelectedIcons;

    private String[] mTabTitles;

    private int mTabIconSize;

    private int mSidePadding;

    private float mSelectionCircleY;
    private float mSelectionCircleRadius;

    private Rect[] mTabTitleBounds;

    private int mTabSelectedColor;
    private int mTabUnSelectedColor;

    private Typeface mSelectedTabTypeFace;
    private Typeface mUnSelectedTabTypeFace;

    private Path mSelectionCirclePath = new Path();

    private int mTabTitleSelectedColor;
    private int mTabTitleUnSelectedColor;

    private float mTabTitleDeltaY;

    private int mCurrentTabIndex;

    private float mSelectionMovementSpeed;
    private long mLastDrawTime = 0;

    private float mTargetFocusPosition;
    private float mCurrentFocusedPosition = -1;

    private BottomNavigationBackground mBackground;

    private BottomNavigationClickListener mBottomNavigationClickListener;

    public BottomNavigationView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public BottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public BottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    public void setBottomNavigationClickListener(BottomNavigationClickListener bottomNavigationClickListener) {
        mBottomNavigationClickListener = bottomNavigationClickListener;
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        mGestureDetector = new GestureDetector(context, mOnGestureListener);

        Resources resources = context.getResources();

        mTabIconSize = resources.getDimensionPixelSize(R.dimen.bottomnav_sideicon_size);
        mSidePadding = resources.getDimensionPixelSize(R.dimen.bottomnav_padding_left);

        mSelectionCircleRadius = resources.getDimension(R.dimen.bottomnav_centericon_size) / 2;

        mTabSelectedColor = resources.getColor(R.color.bottomnav_tab_selected_color);
        mTabUnSelectedColor = resources.getColor(R.color.bottomnav_tab_unselected_color);
        mTabTitleSelectedColor = resources.getColor(R.color.bottomnav_tab_title_selected_color);
        mTabTitleUnSelectedColor = mTabUnSelectedColor;

        mUnSelectedTabTypeFace = ResourcesCompat.getFont(context, R.font.iran_yekan);
        mSelectedTabTypeFace = ResourcesCompat.getFont(context, R.font.iran_yekan_medium);

        mSelectionCirclePaint = new Paint();
        mSelectionCirclePaint.setAntiAlias(true);
        mSelectionCirclePaint.setStyle(Paint.Style.STROKE);
        mSelectionCirclePaint.setStrokeWidth(0.5f);
        mSelectionCirclePaint.setColor(resources.getColor(R.color.bottomnav_centericon_background));

        mTitlePaint = new Paint();
        mTitlePaint.setAntiAlias(true);
        mTitlePaint.setColor(mTabTitleUnSelectedColor);
        mTitlePaint.setTextSize(resources.getDimension(R.dimen.bottomnav_tab_title_font_size));
        mTitlePaint.setTypeface(mUnSelectedTabTypeFace);
        mTitlePaint.setTextAlign(Paint.Align.CENTER);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomNavigationView, defStyleAttr, defStyleAttr);

        mTabIcons = new Drawable[TAB_COUNT];
        mTabSelectedIcons = new Drawable[TAB_COUNT];
        mTabTitles = new String[TAB_COUNT];

        mTabTitleBounds = new Rect[TAB_COUNT];
        for (int i = 0; i < TAB_COUNT; ++i) {
            mTabTitleBounds[i] = new Rect();
        }

        if (a.hasValue(R.styleable.BottomNavigationView_tab_icon1)) {
            int icon0 = a.getResourceId(R.styleable.BottomNavigationView_tab_icon1, 0);
            int iconSelected0 = a.getResourceId(R.styleable.BottomNavigationView_tab_selected_icon1, 0);
            mTabIcons[0] = VectorDrawableCompat.create(context.getResources(), icon0, null).mutate();
            mTabSelectedIcons[0] = VectorDrawableCompat.create(context.getResources(), iconSelected0, null).mutate();
        }

        if (a.hasValue(R.styleable.BottomNavigationView_tab_icon2)) {
            mTabIcons[1] = AppCompatResources.getDrawable(context, a.getResourceId(R.styleable.BottomNavigationView_tab_icon2, 0)).mutate();
            mTabSelectedIcons[1] = AppCompatResources.getDrawable(context, a.getResourceId(R.styleable.BottomNavigationView_tab_selected_icon2, 0)).mutate();
        }

        if (a.hasValue(R.styleable.BottomNavigationView_tab_icon3)) {
            mTabIcons[2] = AppCompatResources.getDrawable(context, a.getResourceId(R.styleable.BottomNavigationView_tab_icon3, 0)).mutate();
            mTabSelectedIcons[2] = AppCompatResources.getDrawable(context, a.getResourceId(R.styleable.BottomNavigationView_tab_selected_icon3, 0)).mutate();
        }

        if (a.hasValue(R.styleable.BottomNavigationView_tab_icon4)) {
            mTabIcons[3] = AppCompatResources.getDrawable(context, a.getResourceId(R.styleable.BottomNavigationView_tab_icon4, 0)).mutate();
            mTabSelectedIcons[3] = AppCompatResources.getDrawable(context, a.getResourceId(R.styleable.BottomNavigationView_tab_selected_icon4, 0)).mutate();
        }

        if (a.hasValue(R.styleable.BottomNavigationView_tab_icon5)) {
            mTabIcons[4] = AppCompatResources.getDrawable(context, a.getResourceId(R.styleable.BottomNavigationView_tab_icon5, 0)).mutate();
            mTabSelectedIcons[4] = AppCompatResources.getDrawable(context, a.getResourceId(R.styleable.BottomNavigationView_tab_selected_icon5, 0)).mutate();
        }

        if (a.hasValue(R.styleable.BottomNavigationView_tab_title1)) {
            mTabTitles[0] = a.getString(R.styleable.BottomNavigationView_tab_title1);
        }

        if (a.hasValue(R.styleable.BottomNavigationView_tab_title2)) {
            mTabTitles[1] = a.getString(R.styleable.BottomNavigationView_tab_title2);
        }

        if (a.hasValue(R.styleable.BottomNavigationView_tab_title3)) {
            mTabTitles[2] = a.getString(R.styleable.BottomNavigationView_tab_title3);
        }

        if (a.hasValue(R.styleable.BottomNavigationView_tab_title4)) {
            mTabTitles[3] = a.getString(R.styleable.BottomNavigationView_tab_title4);
        }

        if (a.hasValue(R.styleable.BottomNavigationView_tab_title5)) {
            mTabTitles[4] = a.getString(R.styleable.BottomNavigationView_tab_title5);
        }

        a.recycle();

        mBackground = new BottomNavigationBackground(context);
        setBackground(mBackground);

        setSelectedTab(mTabIcons.length / 2);
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
            int width = getWidth() - (2 * mSidePadding);
            float tabAreaWidth = (float) width / mTabIcons.length;

            mTargetFocusPosition = mSidePadding + tabAreaWidth / 2 + tabAreaWidth * mCurrentTabIndex;
        }
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);

        mSelectionCircleRadius = (int) (((float) 100 / 255) * getHeight());

        float focusedTabBackgroundMarginTop = ((float) 5 / 256 * getHeight());
        mSelectionCircleY = focusedTabBackgroundMarginTop + mSelectionCircleRadius / 2;

        measureTargetFocusPosition();

        final float width = getWidth() - (2 * mSidePadding);

        measureTabIconBounds();

        mSelectionMovementSpeed = (width - width / mTabIcons.length) / MAXIMUM_TAB_SELECTION_DURATION;

        invalidate();
    }

    private void measureTabIconBounds() {
        final int width = getWidth() - (2 * mSidePadding);
        final int height = getHeight();

        int tabAreaWidth = width / mTabIcons.length;

        int tabLeft = mSidePadding + (tabAreaWidth - mTabIconSize) / 2;

        int tabBottom = (int) (mSelectionCircleY + mSelectionCircleRadius);
        int tabTop = tabBottom - mTabIconSize;

        for (int i = 0; i < mTabIcons.length; i++) {
            float distanceRatio = 1 - Math.min(Math.abs(tabAreaWidth * i + tabAreaWidth / 2 - mCurrentFocusedPosition) / (tabAreaWidth / 2), 1);

            int tabYOffset = (int) -(distanceRatio * (mSelectionCircleRadius - mTabIconSize / 2));

            mTabIcons[i].setBounds(tabLeft, tabTop + tabYOffset, tabLeft + mTabIconSize, tabBottom + tabYOffset);

            if (mCurrentTabIndex == i) {
                updateTabTitlePaintInfo(true);
            } else {
                updateTabTitlePaintInfo(false);
            }
            mTabTitleBounds[i].set(tabLeft, tabBottom, tabLeft + tabAreaWidth, (int) (height));
            mTabSelectedIcons[i].setBounds(mTabIcons[i].getBounds());

            tabLeft += tabAreaWidth;
        }
    }

    private void updateTabTitlePaintInfo(boolean isTabSelected) {
        mTitlePaint.setColor(isTabSelected ? mTabTitleSelectedColor : mTabTitleUnSelectedColor);
        mTitlePaint.setTypeface(isTabSelected ? mSelectedTabTypeFace : mUnSelectedTabTypeFace);

        Paint.FontMetrics fm = mTitlePaint.getFontMetrics();
        mTabTitleDeltaY = (fm.descent + fm.ascent) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        measureCurrentFocusedPosition();

        for (Drawable tabIcon : mTabIcons) {
            tabIcon.draw(canvas);
        }

        for (int i = 0; i < mTabTitleBounds.length; ++i) {
            updateTabTitlePaintInfo(mCurrentTabIndex == i);
            canvas.drawText(mTabTitles[i], mTabTitleBounds[i].left + (float) mTabIconSize / 2, mTabTitleBounds[i].centerY() - mTabTitleDeltaY, mTitlePaint);
        }

        Drawable background = DI.getABResources().getDrawable(R.drawable.bottomnavigation_selected_item_background);
        background.setBounds((int) (mCurrentFocusedPosition - (1.6 * mSelectionCircleRadius)),
                (int) (mSelectionCircleY - (1.5 * mSelectionCircleRadius)),
                (int) (mCurrentFocusedPosition + (1.5 * mSelectionCircleRadius)),
                (int) (mSelectionCircleY + (1.6 * mSelectionCircleRadius)));

        background.draw(canvas);

        canvas.save();
        canvas.clipPath(mSelectionCirclePath);

        for (Drawable tabIcon : mTabSelectedIcons) {
            tabIcon.draw(canvas);
        }

        canvas.restore();

        if (mTargetFocusPosition != mCurrentFocusedPosition) {
            invalidate();
        }

        mLastDrawTime = System.currentTimeMillis();
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

        mSelectionCirclePath.reset();
        mSelectionCirclePath.addCircle(mCurrentFocusedPosition, mSelectionCircleY, mSelectionCircleRadius, Path.Direction.CW);

        measureTabIconBounds();
    }

    public int getCurrentTabIndex() {
        return mCurrentTabIndex;
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
            final int width = getWidth();

            float xPercentage = e.getX() / width;

            float tabPercentage = xPercentage * mTabIcons.length;

            final int selectedTab = (int) Math.floor(tabPercentage);

            if (mCurrentTabIndex == selectedTab) {
                mBottomNavigationClickListener.onTabReselected(selectedTab);
            } else {
                setSelectedTab(selectedTab);
                mBottomNavigationClickListener.onTabClicked(selectedTab);
            }


            return true;
        }

    };

}
