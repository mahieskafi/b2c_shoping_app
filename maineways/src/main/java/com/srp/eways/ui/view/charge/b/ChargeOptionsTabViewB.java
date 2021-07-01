package com.srp.eways.ui.view.charge.b;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.core.content.res.ResourcesCompat;

import com.srp.eways.R;
import com.srp.eways.ui.view.charge.TabOptionsView;

import java.util.List;

import ir.abmyapp.androidsdk.ABResources;
import ir.abmyapp.androidsdk.IABResources;

public class ChargeOptionsTabViewB extends TabOptionsView {

    private static final long MAXIMUM_TAB_SELECTION_DURATION = 200;

    private int mTabViewPaddingTop;
    private int mTabViewPaddingBottom;

    private int mTabIconHeight;
    private int mTabIconWidth;

    private int mTabTitleMarginRight;

    private int mPaddingLeft;

    private int mTabColorSelected;
    private int mTabColorUnSelected;

    private Typeface mTabTitleTypeFaceSelected;
    private Typeface mTabTitleTypeFaceUnSelected;

    private Drawable[] mTabDrawablesUnSelected;
    private Drawable[] mTabDrawablesSelected;
    private String[] mTabTitles;

    private Rect[] mTabTitlesBounds;

    private GradientDrawable mTabIndicatorDrawable;

    private int mTabIndicatorMarginTop;

    private int mTabCount;

    private Paint mTabTitlePaint;

    private float mTabTitleDeltaY;

    private int mCurrentTabIndex;

    private GradientDrawable mBackgroundDrawable;

    private ChargeOptionsTabViewListener mListener;

    private float mTargetFocusPosition;
    private float mCurrentFocusedPosition = -1;

    private float mTabIndicatorMovementSpeed;
    private long mLastDrawTime = 0;

    private List<TabItem> mTabItems;

    public ChargeOptionsTabViewB(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public ChargeOptionsTabViewB(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public ChargeOptionsTabViewB(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        IABResources abResources = ABResources.get(context);

        float tabBarRadius = abResources.getDimen(R.dimen.chargeoptionstabview_background_cornerradius_b);
        float tabBarElevation = abResources.getDimen(R.dimen.chargeoptionstabview_cardelevation_b);

        //getting resources
        setRadius(tabBarRadius);
        setCardElevation(tabBarElevation);

        mPaddingLeft = abResources.getDimenPixelSize(R.dimen.chargeoptionstabview_b_contentpadding_left);

        mTabViewPaddingTop = abResources.getDimenPixelSize(R.dimen.chargeoptionstabview_contentpadding_top);
        mTabViewPaddingBottom = abResources.getDimenPixelSize(R.dimen.chargeoptionstabview_contentpadding_bottom);

        mTabTitleMarginRight = abResources.getDimenPixelSize(R.dimen.chargeoptionstabview_tabtitle_marginright);

        mTabIconHeight = abResources.getDimenPixelSize(R.dimen.chargeoptionstabview_tabicon_height);
        mTabIconWidth = abResources.getDimenPixelSize(R.dimen.chargeoptionstabview_tabicon_width);

        float tabTitleFontSize = abResources.getDimen(R.dimen.chargeoptionstabview_tabtitle_size);

        mTabColorSelected = abResources.getColor(R.color.chargeoptionstabview_tabtitle_color_selected_b);
        mTabColorUnSelected = abResources.getColor(R.color.chargeoptionstabview_tabtitle_color_unselected_b);

        mTabTitleTypeFaceSelected = ResourcesCompat.getFont(context, R.font.iran_yekan);
        mTabTitleTypeFaceUnSelected = ResourcesCompat.getFont(context, R.font.iran_yekan);

        mTabTitlePaint = new Paint();
        mTabTitlePaint.setAntiAlias(true);
        mTabTitlePaint.setTypeface(mTabTitleTypeFaceUnSelected);
        mTabTitlePaint.setTextSize(tabTitleFontSize);

        updateTabTitlePaintInfo(false);

        mBackgroundDrawable = new GradientDrawable();
        mBackgroundDrawable.setColor(abResources.getColor(R.color.chargeoptionstabview_background_color_b));
        mBackgroundDrawable.setCornerRadius(abResources.getDimen(R.dimen.chargeoptionstabview_background_cornerradius_b));

        setBackground(mBackgroundDrawable);

        mTabIndicatorDrawable = new GradientDrawable();
        mTabIndicatorDrawable.setColor(abResources.getColor(R.color.chargeoptionstabview_tabindicator_b));

        mTabIndicatorMarginTop = abResources.getDimenPixelSize(R.dimen.chargeoptionstabview_tabindicator_margintop_b);
    }

    @Override
    public void setOnChargeTabClickListener(ChargeOptionsTabViewListener listener) {
        mListener = listener;
    }

    @Override
    public void setTabBackgroundColor(int color) {
        mBackgroundDrawable.setColor(color);

        invalidate();
    }

    public void setUnselectedTabColor(int color) {
        mTabColorUnSelected = color;
    }

    @Override
    public void setTabItems(List<TabItem> tabItems) {
        if (mTabItems != null && mTabItems.size() != tabItems.size()) {//Todo:change this
            mTargetFocusPosition = 0;
            mCurrentFocusedPosition = -1;
        }

        mTabItems = tabItems;
        mTabCount = mTabItems.size();

        mTabTitles = new String[mTabCount];
        mTabDrawablesSelected = new Drawable[mTabCount];
        mTabDrawablesUnSelected = new Drawable[mTabCount];

        IABResources abResources = ABResources.get(getContext());

        //set TabTitles and tabIcons
        mTabTitlesBounds = new Rect[mTabCount];
        for (int i = 0; i < mTabCount; ++i) {
            mTabTitles[i] = tabItems.get(i).title;

            Drawable unSelectedTabDrawable = abResources.getDrawable(mTabItems.get(i).icon).mutate();
            unSelectedTabDrawable.setColorFilter(mTabColorUnSelected, PorterDuff.Mode.SRC_IN);

            mTabDrawablesUnSelected[i] = unSelectedTabDrawable;

            Drawable selectedTabDrawable = abResources.getDrawable(mTabItems.get(i).icon).mutate();
            selectedTabDrawable.setColorFilter(mTabColorSelected, PorterDuff.Mode.SRC_IN);

            mTabDrawablesSelected[i] = selectedTabDrawable;

            mTabTitlesBounds[i] = new Rect();
        }

        requestLayout();
    }

    @Override
    public void setSelectedTab(int tab) {
        if (tab < 0 || tab >= mTabCount) {
            throw new IllegalArgumentException("Invalid tab position: " + tab);
        }

        mCurrentTabIndex = tab;

        measureTargetFocusPosition();

        mLastDrawTime = System.currentTimeMillis();

        invalidate();
    }

    @Override
    public void setSelectedTabAndNotify(int tab) {
        setSelectedTab(tab);

        mListener.onChargeTabClicked(tab, mTabItems.get(tab));
    }

    private void measureTargetFocusPosition() {
        float width = getWidth() - 2 * mPaddingLeft;

        if (width > 0) {
            float tabAreaWidth = width / mTabTitles.length;

            mTargetFocusPosition = mPaddingLeft + width - (mCurrentTabIndex * tabAreaWidth);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = mTabViewPaddingTop + mTabIconHeight + mTabViewPaddingBottom;

        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        float width = getWidth() - 2 * mPaddingLeft;
        int height = getHeight();

        measureTargetFocusPosition();

        measureTabBounds();

        mTabIndicatorMovementSpeed = (width - width / mTabTitles.length) / MAXIMUM_TAB_SELECTION_DURATION;

        invalidate();
    }

    private void measureTabBounds() {
        int width = getWidth() - 2 * mPaddingLeft;
        int height = getHeight();

        int tabAreaWidth = width / mTabCount;
        int tabCenterX = mPaddingLeft + width - tabAreaWidth / 2;

        for (int i = 0; i < mTabCount; ++i) {
//            int tabIconWidth = mTabDrawablesUnSelected[i].getIntrinsicWidth();
            int tabIconWidth = mTabIconWidth;
            float tabTitleMeasuredWidth = mTabTitlePaint.measureText(mTabTitles[i]);

            Rect textBounds = new Rect();
            mTabTitlePaint.getTextBounds(mTabTitles[i], 0, mTabTitles[i].length(), textBounds);
            float tabTitleMeasuredHeight = textBounds.height();

            int tabMeasuredWidth = (int)(tabIconWidth + mTabTitleMarginRight + tabTitleMeasuredWidth);

            int right = tabCenterX + (tabMeasuredWidth / 2);
            int left = right - tabIconWidth;
            int top = mTabViewPaddingTop;
            int bottom = top + mTabIconHeight;

            int tabCenterY = (getHeight()) / 2;

            mTabDrawablesUnSelected[i].setBounds(left, top, right, bottom);
            mTabDrawablesSelected[i].setBounds(left, top, right, bottom);

            int tabTitleRight = mTabDrawablesUnSelected[i].getBounds().left - mTabTitleMarginRight;
            mTabTitlesBounds[i].set((int)(tabTitleRight - tabTitleMeasuredWidth), (int)(tabCenterY - tabTitleMeasuredHeight / 2), tabTitleRight, (int)(tabCenterY + tabTitleMeasuredHeight / 2));

            tabCenterX -= tabAreaWidth;
        }

        updateIndicatorBounds();
    }

    private void updateTabTitlePaintInfo(boolean isTabSelected) {
        mTabTitlePaint.setColor(isTabSelected ? mTabColorSelected : mTabColorUnSelected);
        mTabTitlePaint.setTypeface(isTabSelected ? mTabTitleTypeFaceSelected : mTabTitleTypeFaceUnSelected);

        Paint.FontMetrics fm = mTabTitlePaint.getFontMetrics();
        mTabTitleDeltaY = (fm.descent + fm.ascent) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        measureCurrentFocusedPosition();

        updateIndicatorBounds();

        mTabIndicatorDrawable.draw(canvas);

        //draw tabs without clipping
        drawTabs(mTabDrawablesUnSelected, canvas, mTabTitlePaint);

        canvas.save();
        canvas.clipRect(mTabIndicatorDrawable.getBounds());

        updateTabTitlePaintInfo(true);

        drawTabs(mTabDrawablesSelected, canvas, mTabTitlePaint);

        canvas.restore();

        updateTabTitlePaintInfo(false);

        if (mTargetFocusPosition != mCurrentFocusedPosition) {
            invalidate();
        }

        mLastDrawTime = System.currentTimeMillis();
    }

    private void drawTabs(Drawable[] drawables, Canvas canvas, Paint paint) {
        for (int i = 0; i < mTabCount; ++i) {
            drawables[i].draw(canvas);
            canvas.drawText(mTabTitles[i], mTabTitlesBounds[i].left, mTabTitlesBounds[i].centerY() - mTabTitleDeltaY, paint);
        }
    }

    private void updateIndicatorBounds() {
        int tabAreaWidth = (getWidth() - 2 * mPaddingLeft) / mTabCount;
        int height = getHeight();

        int indicatorRight = (int)mCurrentFocusedPosition;
        int indicatorLeft = indicatorRight - tabAreaWidth;

        mTabIndicatorDrawable.setBounds(indicatorLeft, mTabIndicatorMarginTop, indicatorRight, height - mTabIndicatorMarginTop);
        mTabIndicatorDrawable.setCornerRadius(height / 2f);
    }

    private void measureCurrentFocusedPosition() {
        if (mCurrentFocusedPosition == -1) {
            mCurrentFocusedPosition = mTargetFocusPosition;
        } else {
            long now = System.currentTimeMillis();
            long passedTime = (now - mLastDrawTime);
            float displacement = mTabIndicatorMovementSpeed * passedTime;

            if (displacement > Math.abs(mCurrentFocusedPosition - mTargetFocusPosition)) {
                mCurrentFocusedPosition = mTargetFocusPosition;
            } else if (mCurrentFocusedPosition < mTargetFocusPosition) {
                mCurrentFocusedPosition += displacement;
            } else {
                mCurrentFocusedPosition -= displacement;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int width = getWidth();

        float xPercentage = event.getX() / width;

        float tabPercentage = xPercentage * mTabCount;

        int selectedTab = (mTabCount - 1) - (int) Math.floor(tabPercentage);
        if (selectedTab == -1) {
            selectedTab = 0;
        }

        if (mCurrentTabIndex == selectedTab) {
//            mListener.onTabReselected(selectedTab);
        } else {
            setSelectedTab(selectedTab);
            mListener.onChargeTabClicked(selectedTab, mTabItems.get(selectedTab));
        }

        return true;
    }

}
