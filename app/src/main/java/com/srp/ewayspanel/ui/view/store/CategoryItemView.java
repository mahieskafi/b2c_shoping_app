package com.srp.ewayspanel.ui.view.store;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.core.view.ViewCompat;

import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.storepage.category.CategoryItem;
import com.srp.ewayspanel.repository.storepage.category.CategoryTreeNode;

import java.util.ArrayList;

public class CategoryItemView extends ViewGroup {

    private static final int ANIMATION_DURATION = 300;

    private CategoryItemHeaderView mCategoryItemHeaderView;
    private CategorySubItemsView mCategorySubItemsView;

    private int mCategoryHeaderSize;

    private Rect mClipBounds = new Rect();

    private int mHorizontalExtraClipBounds;
    private int mVerticalExtraClipBounds;

    private int mDividerHeight;

    private ValueAnimator mAnimator;

    private float mRatio = 0;
    private boolean mIsAnimating = false;

    private boolean mIsExpanded = false;
    private boolean mIsExpanding = false;

    float mAnimationStartPoint;
    float mAnimationEndPoint;

    private CategoryItem mCategoryItem;

    private CategoryItemViewListener mListener;
    private int mItemViewType;

    public CategoryItemView(Context context, int itemViewType) {
        super(context);
        mItemViewType = itemViewType;

        initialize(context, null, 0);
    }

    public CategoryItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public CategoryItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        setClipChildren(false);
        setClipToPadding(false);

        mHorizontalExtraClipBounds = DI.getABResources().getDimenPixelSize(R.dimen.category_subitem_indicator_marginright);
        mVerticalExtraClipBounds = DI.getABResources().getDimenPixelSize(R.dimen.categoryitem_extraclippbounds_top);

        mDividerHeight = DI.getABResources().getDimenPixelSize(R.dimen.categoryitems_divider_height);

        mCategoryItemHeaderView = new CategoryItemHeaderView(context);
        mCategoryItemHeaderView.setId(View.generateViewId());

        mCategorySubItemsView = new CategorySubItemsView(context);
        mCategorySubItemsView.setId(View.generateViewId());

        mCategorySubItemsView.setBackground(DI.getABResources().getDrawable(R.drawable.category_sub_item_view_background));

        mCategoryHeaderSize = DI.getABResources().getDimenPixelSize(R.dimen.categoryitem_header_height);
//        int subItemsViewPaddingTop = mCategoryHeaderSize - mDividerHeight;
//        mCategorySubItemsView.setPadding(0, subItemsViewPaddingTop, 0, 0);

        if (mItemViewType == 0) {
            mCategoryItemHeaderView.setBackground(DI.getABResources().getDrawable(R.drawable.category_first_item_header_view_background));
        } else {
            mCategoryItemHeaderView.setBackground(DI.getABResources().getDrawable(R.drawable.category_item_header_view_background));
        }

        addView(mCategoryItemHeaderView);
        addView(mCategorySubItemsView);

        mAnimator = new ValueAnimator();
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.setDuration(ANIMATION_DURATION);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRatio = (float) animation.getAnimatedValue();

                requestLayout();
            }
        });

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIsAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsAnimating = false;

                if (mAnimationEndPoint == 1) {
                    mIsExpanded = true;
                    mRatio = 1;
                } else {
                    mIsExpanded = false;
                    mRatio = 0;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mIsAnimating = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    public void setListener(final CategoryItemViewListener listener) {
        mListener = listener;

        mCategoryItemHeaderView.setListener(new CategoryItemViewListener() {
            @Override
            public void onSeeAllProductsFromCategory(CategoryItem categoryTreeNode) {
                mListener.onSeeAllProductsFromCategory(categoryTreeNode);
            }

            @Override
            public void onCategoryHeaderClicked(CategoryItem categoryItem) {
                onCategoryHeaderViewClicked();

                mListener.onCategoryHeaderClicked(categoryItem);
            }

            @Override
            public void onCategorySubItemClicked(CategoryItem categoryItem, CategoryItem categorySubItem, int index) {
                mListener.onCategorySubItemClicked(categoryItem, categorySubItem, index);
            }

            @Override
            public void onCategorySubItemArrowClicked(CategoryItem categoryItem, CategoryItem categorySubItem, int index) {
                mListener.onCategorySubItemArrowClicked(categoryItem, categorySubItem, index);
            }
        });

        mCategorySubItemsView.setListener(new CategorySubItemsView.CategorySubItemsViewListener() {
            @Override
            public void onSubCategorySelected(CategoryItem categoryItem, CategoryItem categorySubItem, int index) {
                mListener.onCategorySubItemClicked(categoryItem, categorySubItem, index);
            }

            @Override
            public void onSubCategoryArrowClicked(CategoryItem categoryItem, CategoryItem categorySubItem, int index) {
                mListener.onCategorySubItemArrowClicked(categoryItem, categorySubItem, index);
            }

            @Override
            public void onSeeAllProductsFromCategory(CategoryItem categoryTreeNode) {
                mListener.onSeeAllProductsFromCategory(categoryTreeNode);
            }
        });
    }

    public void setData(CategoryItem categoryItem) {
        mCategoryItem = categoryItem;

        if (mIsAnimating) {
            mAnimator.cancel();
            mIsAnimating = false;
        }

        mRatio = mCategoryItem == null ? 0 : (mCategoryItem.isExpanded() ? 1 : 0);
        mIsExpanded = mCategoryItem != null && mCategoryItem.isExpanded();
        mIsExpanding = false;

        mCategoryItemHeaderView.setData(mCategoryItem);
        if (mCategoryItem != null && mCategoryItem.getChildren() != null && mCategoryItem.getChildren().size() > 0) {
            ArrayList<CategoryItem> subCategoryList = new ArrayList<>();

            for (CategoryItem subItem : mCategoryItem.getChildren()) {
                if (subItem.getChildGroupCount() > 0) {
                    subCategoryList.add(subItem);
                }
            }
            mCategorySubItemsView.setData(mCategoryItem, subCategoryList);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mCategoryItemHeaderView.measure(widthMeasureSpec, heightMeasureSpec);
        int categoryHeaderViewHeight = mCategoryItemHeaderView.getMeasuredHeight();

        mCategoryItemHeaderView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(categoryHeaderViewHeight, MeasureSpec.EXACTLY));

        int height = categoryHeaderViewHeight;

        if (!parentCategoryHasChildren()) {
            mCategorySubItemsView.setVisibility(GONE);
        } else {
            mCategorySubItemsView.setVisibility(VISIBLE);

            mCategorySubItemsView.measure(widthMeasureSpec, heightMeasureSpec);
            int subItemsViewHeight = (int) (mCategorySubItemsView.getMeasuredHeight() * mRatio);

            mCategorySubItemsView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(subItemsViewHeight, MeasureSpec.EXACTLY));

            height += subItemsViewHeight;

            mCategorySubItemsView.setVisibility((!mIsExpanded && !mIsExpanding) ? GONE : VISIBLE);
        }

        mClipBounds.set(-mHorizontalExtraClipBounds, -mVerticalExtraClipBounds, MeasureSpec.getSize(widthMeasureSpec) + mHorizontalExtraClipBounds, height + mVerticalExtraClipBounds);
        ViewCompat.setClipBounds(this, mClipBounds);

        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mCategoryItemHeaderView.layout(0, 0, mCategoryItemHeaderView.getMeasuredWidth(), mCategoryItemHeaderView.getMeasuredHeight());

        if (parentCategoryHasChildren()) {
            mCategorySubItemsView.layout(0, mCategoryHeaderSize - mDividerHeight, mCategorySubItemsView.getMeasuredWidth(), mCategoryHeaderSize - mDividerHeight + mCategorySubItemsView.getMeasuredHeight());
        }
    }

    private void onCategoryHeaderViewClicked() {
        if (!parentCategoryHasChildren()) {
            return;
        }

        if (mIsAnimating) {
//            mAnimator.cancel();

            if (mIsExpanding) {
                mAnimationStartPoint = mRatio;
                mAnimationEndPoint = 0;
            } else {
                mAnimationStartPoint = mRatio;
                mAnimationEndPoint = 1;
            }
        } else {
            if (mIsExpanded) {
                mRatio = 1;

                mAnimationStartPoint = 1;
                mAnimationEndPoint = 0;
            } else {
                mRatio = 0;

                mAnimationStartPoint = 0;
                mAnimationEndPoint = 1;
            }
        }

        mIsExpanding = mAnimationStartPoint < mAnimationEndPoint;

        mAnimator.setFloatValues(mAnimationStartPoint, mAnimationEndPoint);
        mAnimator.start();
    }

    private boolean parentCategoryHasChildren() {
        return mCategoryItem.getChildGroupCount() > 0;
    }

}
