package com.srp.ewayspanel.ui.view.store;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.core.content.res.ResourcesCompat;

import com.srp.eways.ui.view.toolbar.HorizontalDottedProgress;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.storepage.category.CategoryItem;
import com.srp.ewayspanel.ui.club.ClubProductsAdapter;

import ir.abmyapp.androidsdk.IABResources;

public class CategoryItemHeaderView extends ViewGroup {

    private int mHeight;

    private float mTitleTextSize;
    private int mTitleMarginRight;
    private int mTitleColor;

    private int mSeeChildrenArrowMarginLeft;
    private int mSeeChildrenArrowWidth;
    private int mSeeChildrenArrowHeight;

    private Rect mTitleBounds = new Rect();

    private Rect mStringSizeHolderRect = new Rect();

    private Drawable mSeeChildrenArrowDrawable;
    private Drawable mSeeChildrenArrowDrawableExpanded;

    private Paint mTitlePaint;
    private float mDeltaYTitleText;

    private HorizontalDottedProgress mProgress;
    private int mProgressWidth;
    private int mProgressHeight;
    private boolean mIsLoadingVisible;

    private CategoryItem mCategoryItem;

    private GestureDetector mGestureDetector;

    private CategoryItemViewListener mListener;

    public CategoryItemHeaderView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public CategoryItemHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public CategoryItemHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        mGestureDetector = new GestureDetector(context, mOnGestureListener);

        IABResources abResources = DI.getABResources();

        setBackgroundColor(abResources.getColor(R.color.white));

        mHeight = abResources.getDimenPixelSize(R.dimen.categoryitem_header_height);

        mTitleTextSize = abResources.getDimen(R.dimen.categoryitem_header_title_textsize);
        mTitleMarginRight = abResources.getDimenPixelSize(R.dimen.categoryitem_header_title_marginright);
        mTitleColor = abResources.getColor(R.color.categoryitem_header_title);

        mSeeChildrenArrowWidth = abResources.getDimenPixelSize(R.dimen.categoryitem_header_seeallchildren_width);
        mSeeChildrenArrowHeight = abResources.getDimenPixelSize(R.dimen.categoryitem_header_seeallchildren_height);

        mSeeChildrenArrowMarginLeft = abResources.getDimenPixelSize(R.dimen.categoryitem_header_seeallbackground_margin_left);

        mProgress = new HorizontalDottedProgress(context);
        mProgress.setDotsColor(abResources.getColor(R.color.twolevelcategory_header_loading_fillColor), abResources.getColor(R.color.twolevelcategory_header_loading_shadowcolor));
        mIsLoadingVisible = false;
        mProgress.setVisibility(GONE);

        mProgressWidth = abResources.getDimenPixelSize(R.dimen.categoryitem_header_progress_width);
        mProgressHeight = abResources.getDimenPixelSize(R.dimen.categoryitem_header_progress_height);

        mTitlePaint = new Paint();
        mTitlePaint.setAntiAlias(true);
        mTitlePaint.setStyle(Paint.Style.FILL);
        mTitlePaint.setColor(mTitleColor);
        mTitlePaint.setTextSize(mTitleTextSize);
        mTitlePaint.setTypeface(ResourcesCompat.getFont(context, R.font.iran_yekan_medium));

        mDeltaYTitleText = (mTitlePaint.getFontMetrics().ascent + mTitlePaint.getFontMetrics().descent) / 2;

        mSeeChildrenArrowDrawable = abResources.getDrawable(R.drawable.ic_arrow_categoryitem_header).mutate();
        mSeeChildrenArrowDrawableExpanded = abResources.getDrawable(R.drawable.ic_arrow_categoryitem_header_expanded).mutate();

        addView(mProgress);
    }

    private Drawable getRotatedDrawable(final Drawable d, final float angle) {
        final Drawable[] arD = {d};
        return new LayerDrawable(arD) {
            @Override
            public void draw(final Canvas canvas) {
                canvas.save();
                canvas.rotate(angle, d.getBounds().width() / 2, d.getBounds().height() / 2);
                super.draw(canvas);
                canvas.restore();
            }
        };
    }

    public void setListener(CategoryItemViewListener listener) {
        mListener = listener;
    }

    public void setData(CategoryItem categoryItem) {
        mCategoryItem = categoryItem;

        if (mCategoryItem != null && mCategoryItem.isExpandedLoading()) {
            showProgress();
        } else {
            stopProgress();
        }

        invalidate();
    }

    public void showProgress() {
        mProgress.startAnimation();
        mProgress.setVisibility(VISIBLE);
        mIsLoadingVisible = true;
    }

    public void stopProgress() {
        mProgress.setVisibility(View.GONE);
        mProgress.stopAnimation();
        mIsLoadingVisible = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = mHeight;

        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = getWidth();
        int height = getHeight();

        if (mCategoryItem == null) {
            return;
        }

        //measure title
        mTitlePaint.getTextBounds(mCategoryItem.getTitle().trim(), 0, mCategoryItem.getTitle().trim().length(), mStringSizeHolderRect);

        int titleRight = width - mTitleMarginRight;
        int titleLeft = titleRight - mStringSizeHolderRect.width();
        int titleTop = (height - mStringSizeHolderRect.height()) / 2;
        int titleBottom = (height + mStringSizeHolderRect.height()) / 2;

        mTitleBounds.set(titleLeft, titleTop, titleRight, titleBottom);

        int seeChildrenArrowLeft = mSeeChildrenArrowMarginLeft;
        int seeChildrenArrowRight = seeChildrenArrowLeft + mSeeChildrenArrowWidth;
        int seeChildrenArrowTop = (height - mSeeChildrenArrowHeight) / 2;
        int seeChildrenArrowBottom = (height + mSeeChildrenArrowHeight) / 2;

        mSeeChildrenArrowDrawable.setBounds(seeChildrenArrowLeft, seeChildrenArrowTop, seeChildrenArrowRight, seeChildrenArrowBottom);
        mSeeChildrenArrowDrawableExpanded.setBounds(seeChildrenArrowLeft, seeChildrenArrowTop, seeChildrenArrowRight, seeChildrenArrowBottom);

        mProgress.measure(MeasureSpec.makeMeasureSpec(mProgressWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mProgressHeight, MeasureSpec.EXACTLY));
        mProgress.layout(seeChildrenArrowLeft,
                (height - mProgressHeight) / 2,
                seeChildrenArrowLeft + mProgressWidth,
                (height + mProgressHeight) / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mCategoryItem == null) {
            return;
        }

        if (mCategoryItem.getChildGroupCount() > 0) {
            if (!mIsLoadingVisible) {
                if (mCategoryItem.isExpanded()) {
                    mSeeChildrenArrowDrawableExpanded.draw(canvas);
                } else {
                    mSeeChildrenArrowDrawable.draw(canvas);
                }
            }
        }

        canvas.drawText(mCategoryItem.getTitle().trim(), mTitleBounds.left, mTitleBounds.centerY() - mDeltaYTitleText, mTitlePaint);
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
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();

            if (mCategoryItem == null) {
                return false;
            }

            if (mCategoryItem.getChildGroupCount() > 0) {
                mCategoryItem.setExpanded(!mCategoryItem.isExpanded());
            } else {
                mListener.onSeeAllProductsFromCategory(mCategoryItem);
            }
            mListener.onCategoryHeaderClicked(mCategoryItem);

            invalidate();

            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };

}
