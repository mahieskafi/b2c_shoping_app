package com.srp.ewayspanel.ui.view.store;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.storepage.category.CategoryItem;

import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

public class CategorySubItemsView extends View {

    public interface CategorySubItemsViewListener {

        void onSubCategorySelected(CategoryItem categoryItem, CategoryItem categorySubItem, int index);

        void onSubCategoryArrowClicked(CategoryItem categoryItem, CategoryItem categorySubItem, int index);

        void onSeeAllProductsFromCategory(CategoryItem categoryTreeNode);
    }

    private int mChildPaddingTop;
    private int mChildPaddingBottom;
    private int mChildTitleHeight;

    private int mTitleMarginRight;
    private int mArrowMarginLeft;

    private float mTitleTextSize;
    private float mTitleTextBold;
    private int mTitleTextColor;
    private int mTitleRight;

    private float mTitleDeltaY;

    private Paint mTitlePaint;
    private Paint mSeeAllTitlePaint;

    private String mSeeAllString;

    private Rect mTitleSizeHolderRect = new Rect();

    private int mChildHeight;

    private List<CategoryItem> mCategorySubItems;

    private CategorySubItemsViewListener mListener;
    private GestureDetector mGestureDetector;

    private CategoryItem mCategoryItem;

    private Drawable mSeeAllArrowDrawable;
    private int mArrowMarginRight;

    private Typeface mIranYekan;
    private Typeface mIranYekanBold;
    private int mSeeAllTextColor;

    private Drawable mSeeSubItemsArrowDownDrawable;
    private Drawable mSeeSubItemsArrowUpDrawable;

    public CategorySubItemsView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public CategorySubItemsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public CategorySubItemsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        mGestureDetector = new GestureDetector(context, mOnGestureListener);

        IABResources abResources = DI.getABResources();

        mChildPaddingTop = abResources.getDimenPixelSize(R.dimen.category_subitem_padding_top);
        mChildPaddingBottom = abResources.getDimenPixelSize(R.dimen.category_subitem_padding_bottom);
        mChildTitleHeight = abResources.getDimenPixelSize(R.dimen.category_subitem_title_height);

        mTitleMarginRight = abResources.getDimenPixelSize(R.dimen.category_subitem_title_marginright);
        mArrowMarginLeft = abResources.getDimenPixelSize(R.dimen.category_subitem_title_marginright);

        mTitleTextSize = abResources.getDimen(R.dimen.category_subitem_title_textsize);
        mTitleTextBold = abResources.getDimen(R.dimen.category_subitem_title_textsize_bold);
        mTitleTextColor = abResources.getColor(R.color.category_subitem_title_textcolor);

        mSeeAllTextColor = abResources.getColor(R.color.categoryitem_seeall_text_color);
        mIranYekanBold = ResourcesCompat.getFont(context, R.font.iran_yekan_medium);
        mIranYekan = ResourcesCompat.getFont(context, R.font.iran_yekan);

        mTitlePaint = new Paint();
        mTitlePaint.setAntiAlias(true);
        mTitlePaint.setColor(mTitleTextColor);
        mTitlePaint.setTypeface(mIranYekan);
        mTitlePaint.setTextSize(mTitleTextSize);

        mTitleDeltaY = (mTitlePaint.ascent() + mTitlePaint.descent()) / 2;

        mSeeAllTitlePaint = new Paint();
        mSeeAllTitlePaint.setAntiAlias(true);
        mSeeAllTitlePaint.setColor(mSeeAllTextColor);
        mSeeAllTitlePaint.setTypeface(ResourcesCompat.getFont(context, R.font.iran_yekan));
        mSeeAllTitlePaint.setTextSize(mTitleTextSize);

        mSeeAllString = abResources.getString(R.string.category_title_all);

        mSeeAllArrowDrawable = abResources.getDrawable(R.drawable.ic_leftarrow_seeall_categoryitem).mutate();
        mArrowMarginRight = abResources.getDimenPixelSize(R.dimen.categoryitem_header_seealltext_arrow_margin_right);

        mSeeSubItemsArrowDownDrawable = abResources.getDrawable(R.drawable.ic_arrow_down_category_sub_item).mutate();
        mSeeSubItemsArrowUpDrawable = abResources.getDrawable(R.drawable.ic_arrow_up_category_sub_item).mutate();
    }

    public void setListener(CategorySubItemsViewListener listener) {
        mListener = listener;
    }

    public void setData(CategoryItem categoryItem, List<CategoryItem> categorySubItems) {
        mCategorySubItems = categorySubItems;
        mCategoryItem = categoryItem;

        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mCategorySubItems == null) {
            setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY));
        } else {
            mChildHeight = mChildPaddingTop + mChildTitleHeight + mChildPaddingBottom;

            int childCount = mCategorySubItems.size() + 1;
            int height = getPaddingTop() + (childCount * mChildHeight) + (childCount - 1) + getPaddingBottom();

            setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (mCategorySubItems == null) {
            return;
        }

        int width = getWidth();

        mTitleRight = width - mTitleMarginRight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mCategorySubItems == null) {
            return;
        }

        int childCount = mCategorySubItems.size();

        for (int i = 0; i <= childCount; i++) {
            canvas.save();

            canvas.translate(0, getPaddingTop() + (i * (mChildHeight)));
            String currentTitle;
            Paint drawPaint;

            if (i < mCategorySubItems.size()) {
                CategoryItem categorySubItem = mCategorySubItems.get(i);
                currentTitle = categorySubItem.getTitle();

                if (categorySubItem.getDepth() == 3) {
                    mTitlePaint.setTypeface(mIranYekanBold);
                    mTitlePaint.setTextSize(mTitleTextBold);
                }

                if (categorySubItem.isExpanded()) {
                    mTitlePaint.setTypeface(mIranYekanBold);
                    mTitlePaint.setColor(mSeeAllTextColor);
                } else {
                    if (categorySubItem.getDepth() > 3) {
                        mTitlePaint.setTypeface(mIranYekan);
                    }
                    mTitlePaint.setColor(mTitleTextColor);
                }
                mTitlePaint.getTextBounds(currentTitle, 0, currentTitle.length(), mTitleSizeHolderRect);
                drawPaint = mTitlePaint;
            } else {
                currentTitle = mSeeAllString;
                mSeeAllTitlePaint.getTextBounds(currentTitle, 0, currentTitle.length(), mTitleSizeHolderRect);
                drawPaint = mSeeAllTitlePaint;
            }

            int titleTop = getPaddingTop() + (mChildHeight - mTitleSizeHolderRect.height()) / 2;
            int titleBottom = getPaddingTop() + (mChildHeight + mTitleSizeHolderRect.height()) / 2;
            int titleLeft;

            //Draw title and arrow for all items and seeAll title for last item
            if (i < mCategorySubItems.size()) {
                CategoryItem categorySubItem = mCategorySubItems.get(i);

                titleLeft = mTitleRight - (mTitleMarginRight * (categorySubItem.getDepth() - 2)) - mTitleSizeHolderRect.width();

                //Draw arrow icon of everyItem
                int arrowLeft = mArrowMarginLeft;
                int arrowRight = arrowLeft + mSeeSubItemsArrowDownDrawable.getIntrinsicWidth();
                int arrowTop = (mChildHeight - mSeeSubItemsArrowDownDrawable.getIntrinsicHeight()) / 2;
                int arrowBottom = (mChildHeight + mSeeSubItemsArrowDownDrawable.getIntrinsicHeight()) / 2;

                mSeeSubItemsArrowDownDrawable.setBounds(arrowLeft, arrowTop, arrowRight, arrowBottom);
                mSeeSubItemsArrowUpDrawable.setBounds(arrowLeft, arrowTop, arrowRight, arrowBottom);

                if (categorySubItem.getChildGroupCount() > 0) {
                    if (categorySubItem.isExpanded()) {
                        mSeeSubItemsArrowUpDrawable.draw(canvas);
                    } else {
                        mSeeSubItemsArrowDownDrawable.draw(canvas);
                    }
                }

            } else {
                titleLeft = mTitleRight - mTitleMarginRight - mTitleSizeHolderRect.width();
            }
            canvas.drawText(currentTitle, titleLeft, titleTop + ((float) titleBottom - titleTop) / 2 - mTitleDeltaY, drawPaint);

            //Draw seeAll icon
            if (i == childCount) {
                int arrowRight = titleLeft - mArrowMarginRight;
                int arrowLeft = arrowRight - mSeeAllArrowDrawable.getIntrinsicWidth();
                int arrowTop = getPaddingTop() + (mChildHeight - mSeeAllArrowDrawable.getIntrinsicHeight()) / 2;
                int arrowBottom = getPaddingTop() + (mChildHeight + mSeeAllArrowDrawable.getIntrinsicHeight()) / 2;

                mSeeAllArrowDrawable.setBounds(arrowLeft, arrowTop, arrowRight, arrowBottom);
                mSeeAllArrowDrawable.draw(canvas);
            }

            canvas.restore();
        }
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
            float y = e.getY();
            float x = e.getX();


            if (mCategorySubItems == null) {
                return false;
            }

            int childCount = mCategorySubItems.size();

            int selectedSubItemIndex = -1;

            for (int i = 0; i <= childCount; i++) {
                selectedSubItemIndex = i;

                if (y < getPaddingTop() + mChildHeight + getPaddingBottom()) {
                    break;
                }

                y -= mChildHeight;
            }

            if (mListener != null) {
                Rect seeSubCategoriesDrawableBounds = new Rect();
                seeSubCategoriesDrawableBounds.set(0, 0
                        , (mArrowMarginLeft * 2) + mSeeSubItemsArrowDownDrawable.getIntrinsicWidth(),
                        mChildHeight);

                if (selectedSubItemIndex < childCount) {
                    if (seeSubCategoriesDrawableBounds.contains((int) x, (int) y) ) {
                        mListener.onSubCategoryArrowClicked(mCategoryItem, mCategorySubItems.get(selectedSubItemIndex), selectedSubItemIndex);
                    } else {
                        mListener.onSubCategorySelected(mCategoryItem, mCategorySubItems.get(selectedSubItemIndex), selectedSubItemIndex);
                    }
                } else {
                    mListener.onSeeAllProductsFromCategory(mCategoryItem);
                }
            }
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
