package com.srp.ewayspanel.ui.view.store;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.eways.ui.view.ViewUtils;

import ir.abmyapp.androidsdk.IABResources;

public class StoreFilterHeaderView extends ViewGroup {

    public interface FilterHeaderViewClickListener {
        void onFilterClicked();

        void onSortClicked();

        void onChangeViewClicked();
    }

    private int mPaddingTop;
    private int mPaddingBottom;
    private int mDividerMargin;
    private StoreFilterItemView mSortItemView;
    private StoreFilterItemView ChangeViewItemView;
    private StoreFilterItemView filterItemView;
    private StoreFilterHeaderDivider mTitleDivider , mTitleDivider2;
    int mwidth;

    private FilterHeaderViewClickListener mListener;

    public StoreFilterHeaderView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public StoreFilterHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public StoreFilterHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        setClipToPadding(false);
        setClipChildren(false);

        IABResources abResources = DI.getABResources();

        mPaddingTop = abResources.getDimenPixelSize(R.dimen.sortfilterheader_paddingtop);
        mPaddingBottom = abResources.getDimenPixelSize(R.dimen.sortfilterheader_paddingbottom);
        mDividerMargin = abResources.getDimenPixelSize(R.dimen.sortfilterheader_divider_margin);

        mSortItemView = new StoreFilterItemView(context, attrs, defStyleAttr);
        mSortItemView.setTitle(abResources.getString(R.string.storefilter_title_sortby));
        mSortItemView.setIcon(getResources().getDrawable(R.drawable.ic_filteritem_sort_new));
        mSortItemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSortClicked();
            }
        });

        filterItemView = new StoreFilterItemView(context, attrs, defStyleAttr);
        filterItemView.setTitle(abResources.getString(R.string.storefilter_title_filter));
        filterItemView.setIcon(getResources().getDrawable(R.drawable.ic_filteritem_filter_new));
        filterItemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFilterClicked();
            }
        });

        ChangeViewItemView = new StoreFilterItemView(context, attrs, defStyleAttr);
        ChangeViewItemView.setTitle(abResources.getString(R.string.storefilter_title_changeview));
        ChangeViewItemView.setIcon(getResources().getDrawable(R.drawable.ic_filteritem_changeview_list));
        ChangeViewItemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onChangeViewClicked();
            }
        });

        mTitleDivider = new StoreFilterHeaderDivider(context,attrs,defStyleAttr);
        mTitleDivider2 = new StoreFilterHeaderDivider(context,attrs,defStyleAttr);


        float elevation = abResources.getDimen(R.dimen.storefilter_headerview_cardelevation);

        ViewUtils.setCardBackground(this, elevation);

        addView(mSortItemView);
        addView(filterItemView);
        addView(ChangeViewItemView);
        addView(mTitleDivider);
        addView(mTitleDivider2);
    }

    public void setSortSubtitle(String subtitle) {
        mSortItemView.setSubTitle(subtitle);

    }

    public void setListener(FilterHeaderViewClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mwidth = MeasureSpec.getSize(widthMeasureSpec);

        View sortItemView = getChildAt(0);
        View filterItemView = getChildAt(1);

        sortItemView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        filterItemView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        int height = mPaddingTop + sortItemView.getMeasuredHeight() + mPaddingBottom;

        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getWidth();
        int height = getHeight();

        int space=mwidth/3;

        View sortItemView = getChildAt(0);
        View filterItemView = getChildAt(1);
        View ChangeViewItemView = getChildAt(2);
        View TitleDivider = getChildAt(3);
        View TitleDivider2 = getChildAt(4);

        sortItemView.measure(MeasureSpec.makeMeasureSpec(sortItemView.getMeasuredWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(sortItemView.getMeasuredHeight(), MeasureSpec.EXACTLY));
        filterItemView.measure(MeasureSpec.makeMeasureSpec(filterItemView.getMeasuredWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(filterItemView.getMeasuredHeight(), MeasureSpec.EXACTLY));
        ChangeViewItemView.measure(MeasureSpec.makeMeasureSpec(ChangeViewItemView.getMeasuredWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(ChangeViewItemView.getMeasuredHeight(), MeasureSpec.EXACTLY));
        TitleDivider.measure(MeasureSpec.makeMeasureSpec(TitleDivider.getMeasuredWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(TitleDivider.getMeasuredHeight(), MeasureSpec.EXACTLY));
        TitleDivider2.measure(MeasureSpec.makeMeasureSpec(TitleDivider2.getMeasuredWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(TitleDivider2.getMeasuredHeight(), MeasureSpec.EXACTLY));

        


        TitleDivider.layout(
                2*space -mDividerMargin  ,
                mPaddingTop,
                2*space - mDividerMargin  ,
                mPaddingTop + sortItemView.getMeasuredHeight());

        TitleDivider2.layout(
                space + mDividerMargin  ,
                mPaddingTop,
                space + mDividerMargin ,
                mPaddingTop + sortItemView.getMeasuredHeight());

        sortItemView.layout(
                (2*space) + (space/2) - sortItemView.getMeasuredWidth()/2 ,
                mPaddingTop,
                (2*space) + (space/2) + sortItemView.getMeasuredWidth()/2,
                mPaddingTop + sortItemView.getMeasuredHeight());

        filterItemView.layout(
                space + (space/2) - (filterItemView.getMeasuredWidth()/2) ,
                mPaddingTop,
                2*space - (space/2)  + (filterItemView.getMeasuredWidth()/2)  ,
                mPaddingTop + filterItemView.getMeasuredHeight());

        ChangeViewItemView.layout(
                (space/2) - ChangeViewItemView.getMeasuredWidth()/2 ,
                mPaddingTop,
                (space/2) + ChangeViewItemView.getMeasuredWidth()/2 ,
                mPaddingTop + ChangeViewItemView.getMeasuredHeight());

    }

    public void changeChangeViewIcon(boolean isList){
        if (isList){
            ChangeViewItemView.setIcon(getResources().getDrawable(R.drawable.ic_filteritem_changeview_window));

        }else {
            ChangeViewItemView.setIcon(getResources().getDrawable(R.drawable.ic_filteritem_changeview_list));
        }
    }

}
