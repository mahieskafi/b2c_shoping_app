package com.srp.ewayspanel.ui.view.store.basket;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;

import com.srp.eways.ui.view.LoadingStateView;
import com.srp.eways.ui.view.ViewUtils;
import com.srp.eways.ui.view.button.ButtonElement;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.eways.ui.view.receipt.ReceiptItem;
import com.srp.eways.ui.view.receipt.ReceiptListView;
import com.srp.eways.util.Utils;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;


/**
 * Created by Eskafi on 12/4/2019.
 */
public class OrderSummaryView extends ViewGroup {

    private ImageView mArrowUpIcon;
    private ImageView mArrowDownIcon;
    private ReceiptListView mReceiptListView;
    private LinearLayout mMoreDetailView;
    private LinearLayout mDetailView;
    private FrameLayout mSummaryView;
    private ButtonElement mNextLevelButton;
    private LoadingStateView mLoadingStateView;

    private ViewGroup mScoreRowView;
    private AppCompatTextView mScoreTitle;
    private AppCompatTextView mScoreValue;

    private ViewGroup mTotalPriceRowView;
    private AppCompatTextView mTotalPriceTitle;
    private AppCompatTextView mTotalPriceValue;
    private AppCompatTextView mTotalPriceValueDescription;

    private Drawable mIconArrowDown;
    private Drawable mIconArrowUp;

    private Typeface mIranyekanMediumTypeface;
    private Typeface mIranYekanBoldTypeface;
    private Typeface mIranyekanTypeface;
    private Typeface mButtonTextTypeface;

    private boolean isExpand = false;
    private boolean mIsLoading = false;

    private int mHeight;

    private int mDetailPaddingTop;
    private int mButtonMargin;
    private int mButtonWidth;
    private int mLoadingMarginSide;
    private int mButtonHeight;
    private int mArrowHeight;
    private int mReceiptListMarginSides;
    private int mReceiptItemsMargin;
    private int mArrowIconMarginRight;
    private int mArrowIconMarginLeft;
    private int mReceiptItemsSpace;
    private int mMoreDetailViewElevation;

    private String mRialText;
    private String mTotalRealPriceText;
    private String mTotalDiscountText;
    private String mDeliveryText;
    private String mLoadingText;
    private String mErrorText;

    private int mReceiptListCount = 0;

    List<ReceiptItem> mReceiptItemList = new ArrayList();

    public OrderSummaryView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public OrderSummaryView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public OrderSummaryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }


    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {

        IABResources abResources = DI.getABResources();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        setClipToPadding(false);
        setClipChildren(false);

        mDetailPaddingTop = abResources.getDimenPixelSize(R.dimen.order_summary_receipt_list_view_margin_top);
        mButtonMargin = abResources.getDimenPixelSize(R.dimen.order_summary_button_margin_sides);
        mLoadingMarginSide = abResources.getDimenPixelSize(R.dimen.order_summary_loading_margin_sides);
        mButtonHeight = abResources.getDimenPixelSize(R.dimen.order_summary_receipt_button_height);
        mButtonWidth = abResources.getDimenPixelSize(R.dimen.order_summary_receipt_button_width);
        mArrowHeight = abResources.getDimenPixelSize(R.dimen.order_summary_arrow_height);
        mReceiptListMarginSides = abResources.getDimenPixelSize(R.dimen.order_summary_receipt_list_margin_sides);
        mReceiptItemsMargin = abResources.getDimenPixelSize(R.dimen.order_summary_receipt_items_margin);
        mArrowIconMarginRight = abResources.getDimenPixelSize(R.dimen.order_summary_arrow_icon_margin_right);
        mArrowIconMarginLeft = abResources.getDimenPixelSize(R.dimen.order_summary_arrow_icon_margin_left);
        mReceiptItemsSpace = 2 * mReceiptItemsMargin;
        mMoreDetailViewElevation = abResources.getDimenPixelSize(R.dimen.shopcard_more_detail_view_elevation);

        mIranyekanMediumTypeface = ResourcesCompat.getFont(context, R.font.iran_yekan_medium);
        mIranYekanBoldTypeface = ResourcesCompat.getFont(context, R.font.iran_yekan_bold);
        mIranyekanTypeface = ResourcesCompat.getFont(context, R.font.iran_yekan);
        mButtonTextTypeface = ResourcesCompat.getFont(context, R.font.iran_yekan);

        mRialText = abResources.getString(R.string.rial);
        mTotalRealPriceText = abResources.getString(R.string.order_summary_total_price_text);
        mTotalDiscountText = abResources.getString(R.string.order_summary_total_discount_text);
        mDeliveryText = abResources.getString(R.string.order_summary_delivery_price_text);
        mLoadingText = abResources.getString(R.string.loading_message);
        mErrorText = abResources.getString(R.string.network_error_unsupported_api);

        mIconArrowUp = abResources.getDrawable(R.drawable.ic_icon_arrow_up_order_summary);
        mIconArrowDown = abResources.getDrawable(R.drawable.ic_icon_arrow_down_order_summary);

        mArrowUpIcon = new AppCompatImageView(context);

        LinearLayout.LayoutParams paramsIcon = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mArrowUpIcon.setLayoutParams(paramsIcon);
        mArrowUpIcon.setImageDrawable(mIconArrowUp);

        mArrowDownIcon = new AppCompatImageView(context);
        mArrowDownIcon.setLayoutParams(paramsIcon);
        mArrowDownIcon.setImageDrawable(mIconArrowDown);

        mReceiptListView = new ReceiptListView(context);
        mReceiptListView.setItemViewLayoutId(R.layout.item_order_summary);
        mReceiptListView.setTextColor(abResources.getColor(R.color.order_summary_receipt_items_text_color));
        mReceiptListView.setTextSize(abResources.getDimenPixelSize(R.dimen.order_summary_receipt_items_text_size));
        mReceiptListView.setTextTypeface(ResourcesCompat.getFont(context, R.font.iran_yekan_medium));
        mReceiptListView.setValueTextTypeface(ResourcesCompat.getFont(context, R.font.iran_yekan));
        mReceiptListView.setValueDescrptionTextSize(abResources.getDimenPixelSize(R.dimen.order_summary_total_price_value_description_size));

        mMoreDetailView = new LinearLayout(context);
        mMoreDetailView.setOrientation(LinearLayout.VERTICAL);
        ViewUtils.setCardBackground(mMoreDetailView, mMoreDetailViewElevation,
                abResources.getColor(R.color.white));
        mMoreDetailView.setPadding(mReceiptListMarginSides - mDetailPaddingTop - mArrowHeight,
                mReceiptItemsSpace, mReceiptListMarginSides, mReceiptItemsSpace);
        expandView();

        mDetailView = new LinearLayout(context);
        mDetailView.setOrientation(LinearLayout.VERTICAL);
        mDetailView.setPadding(mReceiptListMarginSides - mArrowIconMarginLeft, mDetailPaddingTop, 0, 0);

        mNextLevelButton = new ButtonElement(context);
        mNextLevelButton.setDisableBackground(abResources.getDrawable(R.drawable.login_button_background_disabled));
        mNextLevelButton.setEnabledBackground(abResources.getDrawable(R.drawable.login_button_background_enabled));
        mNextLevelButton.setTextTypeFace(mButtonTextTypeface);
        mNextLevelButton.setTextColor(abResources.getColor(R.color.order_summary_button_text_color));
        mNextLevelButton.setTextSize(abResources.getDimenPixelSize(R.dimen.order_summary_button_text_size));
        mNextLevelButton.setText(abResources.getString(R.string.order_summary_button_text));
        mNextLevelButton.setEnable(true);
        mNextLevelButton.setLoadingVisibility(View.GONE);
        mNextLevelButton.setLoadingColorFilter(abResources.getColor(R.color.login_button_text_color));

        mLoadingStateView = new LoadingStateView(context);
        mLoadingStateView.setViewOrientation(LinearLayout.HORIZONTAL);
//        LinearLayout.LayoutParams paramsLoading = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        mLoadingStateView.setLayoutParams(paramsLoading);
        mLoadingStateView.setVisibility(GONE);

        mArrowUpIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsLoading) {
                    expandView();
                }
            }
        });

        mArrowDownIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsLoading) {
                    collapseView();
                }
            }
        });

        mScoreRowView = (ViewGroup) inflater.inflate(R.layout.item_order_summary, null);
        LinearLayout.LayoutParams paramsScoreView = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mScoreRowView.setLayoutParams(paramsScoreView);

        mScoreTitle = mScoreRowView.findViewById(R.id.txt_title);
        mScoreTitle.setTypeface(mIranyekanMediumTypeface);
        mScoreTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.order_summary_total_score_text_size));
        mScoreTitle.setTextColor(abResources.getColor(R.color.order_summary_total_score_text_color));
        mScoreTitle.setText(abResources.getString(R.string.order_summary_total_score_text));

        mScoreValue = mScoreRowView.findViewById(R.id.txt_value);
        mScoreValue.setTypeface(mIranyekanMediumTypeface);
        mScoreValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.order_summary_total_score_text_size));
        mScoreValue.setTextColor(abResources.getColor(R.color.order_summary_total_score_text_color));

        mTotalPriceRowView = (ViewGroup) inflater.inflate(R.layout.item_total_price_order_summary, null);
        LinearLayout.LayoutParams paramsPriceView = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mTotalPriceRowView.setLayoutParams(paramsPriceView);

        mTotalPriceTitle = mTotalPriceRowView.findViewById(R.id.txt_title);
        mTotalPriceTitle.setTypeface(mIranyekanMediumTypeface);
        mTotalPriceTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.order_summary_total_price_text_size));
        mTotalPriceTitle.setTextColor(abResources.getColor(R.color.order_summary_total_price_text_color));
        mTotalPriceTitle.setText(abResources.getString(R.string.order_summary_total_cost_text));

        mTotalPriceValue = mTotalPriceRowView.findViewById(R.id.txt_value);
        mTotalPriceValue.setTypeface(mIranYekanBoldTypeface);
        mTotalPriceValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.order_summary_total_price_value_size));
        mTotalPriceValue.setTextColor(abResources.getColor(R.color.order_summary_total_price_value_color));

        mTotalPriceValueDescription = mTotalPriceRowView.findViewById(R.id.value_description);
        mTotalPriceValueDescription.setVisibility(VISIBLE);
        mTotalPriceValueDescription.setTypeface(mIranyekanTypeface);
        mTotalPriceValueDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.order_summary_total_price_value_description_size));
        mTotalPriceValueDescription.setTextColor(abResources.getColor(R.color.order_summary_total_price_value_description_color));
        mTotalPriceValueDescription.setText(abResources.getString(R.string.rial));

        mSummaryView = new FrameLayout(context);
        ViewCompat.setElevation(mSummaryView, abResources.getDimenPixelSize(R.dimen.order_summary_view_elevation));
        mSummaryView.setBackgroundColor(abResources.getColor(R.color.order_summary_total_price_container_back_color));

        mDetailView.addView(mReceiptListView);
        mDetailView.addView(mScoreRowView);
        mMoreDetailView.addView(mArrowDownIcon);
        mMoreDetailView.addView(mDetailView);
        addView(mMoreDetailView);
        mSummaryView.addView(mTotalPriceRowView);
        mSummaryView.addView(mNextLevelButton);
        mSummaryView.addView(mLoadingStateView);
        mSummaryView.addView(mArrowUpIcon);
        addView(mSummaryView);
    }


    public void expandView() {
        mArrowUpIcon.setVisibility(GONE);

        mMoreDetailView.setVisibility(VISIBLE);

        isExpand = true;
        requestLayout();
    }

    public void collapseView() {
        mArrowUpIcon.setVisibility(VISIBLE);

        mMoreDetailView.setVisibility(GONE);

        isExpand = false;

        requestLayout();
    }

    public void setArrowIcons(Drawable iconArrowDown, Drawable iconArrowUp) {

        mIconArrowDown = iconArrowDown;
        mIconArrowUp = iconArrowUp;
    }

    public void setTotalScore(long score) {
        mScoreValue.setText(Utils.toPersianNumber(String.valueOf(score)));
    }

    public void setTotalRealPrice(long price) {

        ReceiptItem receiptItemTotalPrice = new ReceiptItem(mTotalRealPriceText,
                Utils.toPersianPriceNumber(String.valueOf(price)), mRialText, null);

        mReceiptItemList.add(receiptItemTotalPrice);
        setReceiptListItems(mReceiptItemList);
    }

    public void setTotalDiscount(long price) {

        ReceiptItem receiptItemTotalDiscount = new ReceiptItem(mTotalDiscountText,
                Utils.toPersianPriceNumber(String.valueOf(price)), mRialText, null);

        mReceiptItemList.add(receiptItemTotalDiscount);
        setReceiptListItems(mReceiptItemList);
    }

    public void setDeliveryPrice(String deliveryTitle, long price) {

        ReceiptItem receiptItemDeliveryPrice = new ReceiptItem(mDeliveryText + " " + deliveryTitle + ":",
                Utils.toPersianPriceNumber(String.valueOf(price)), mRialText, null);


        mReceiptItemList.add(1, receiptItemDeliveryPrice);
        setReceiptListItems(mReceiptItemList);
    }

    public void resetData() {
        mReceiptItemList = new ArrayList<>();
    }

    public void setTotalPrice(long price) {
        mTotalPriceValue.setText(Utils.toPersianPriceNumber(price));
    }

    public void setReceiptListItems(List<ReceiptItem> receiptListItems) {

        mReceiptListCount = receiptListItems.size();

        mReceiptListView.setItemsMargin(mReceiptItemsMargin);
        mReceiptListView.setReceiptItem(receiptListItems);

        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mHeight = 0;
        mScoreRowView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        mReceiptListView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        mArrowUpIcon.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        mHeight += mButtonMargin * 2 + mButtonHeight;

//        if (!mIsLoading) {
//            mHeight += mScoreRowView.getMeasuredHeight() * 2;

        if (isExpand) {
            mHeight += mScoreRowView.getMeasuredHeight() + mReceiptListView.getMeasuredHeight() + mArrowHeight + mDetailPaddingTop +
                    mReceiptItemsSpace * 4;
        }
//        } else {
//            mHeight += mLoadingStateView.getMeasuredHeight() + (2 * mButtonMargin);
//        }
        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int width = getWidth();
        int height = getHeight();

        mSummaryView.measure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(2 * mButtonMargin + mButtonHeight, MeasureSpec.EXACTLY));

        mSummaryView.layout(
                0,
                height - mSummaryView.getMeasuredHeight(),
                width,
                height);

        mLoadingStateView.measure(
                MeasureSpec.makeMeasureSpec(getWidth() - mArrowIconMarginLeft - mArrowHeight - (2 * mLoadingMarginSide), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        mLoadingStateView.layout(
                mSummaryView.getMeasuredWidth() - mLoadingStateView.getMeasuredWidth() - mLoadingMarginSide,
                0,
                mSummaryView.getMeasuredWidth() - mLoadingMarginSide,
                mSummaryView.getMeasuredHeight());

        mMoreDetailView.measure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        mMoreDetailView.layout(
                0,
                height - mMoreDetailView.getMeasuredHeight() - mSummaryView.getMeasuredHeight(),
                width,
                height + mMoreDetailView.getMeasuredHeight());

//        mDetailView.measure(
//                MeasureSpec.makeMeasureSpec(width - (2 * mReceiptListMarginSides), MeasureSpec.EXACTLY),
//                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));


        mNextLevelButton.measure(
                MeasureSpec.makeMeasureSpec(mButtonWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mButtonHeight, MeasureSpec.EXACTLY));

        mNextLevelButton.layout(
                mSummaryView.getMeasuredWidth() - mButtonMargin - mButtonWidth,
                mSummaryView.getMeasuredHeight() / 2 - mButtonHeight / 2,
                mSummaryView.getMeasuredWidth() - mButtonMargin,
                mSummaryView.getMeasuredHeight() / 2 + mButtonHeight / 2);


        mTotalPriceRowView.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        int totalPriceRight = mArrowIconMarginLeft + mArrowHeight + mArrowIconMarginRight + mTotalPriceRowView.getMeasuredWidth();

        mTotalPriceRowView.layout(
                totalPriceRight - mTotalPriceRowView.getMeasuredWidth(),
                mSummaryView.getMeasuredHeight() / 2 - mTotalPriceRowView.getMeasuredHeight() / 2,
                totalPriceRight,
                mSummaryView.getMeasuredHeight() / 2 + mTotalPriceRowView.getMeasuredHeight() / 2);

        mArrowUpIcon.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));


        mArrowUpIcon.layout(
                mTotalPriceRowView.getLeft() - mArrowIconMarginRight - mArrowUpIcon.getMeasuredWidth(),
                mNextLevelButton.getBottom() - mArrowUpIcon.getMeasuredHeight(),
                mTotalPriceRowView.getLeft() - mArrowIconMarginRight,
                mNextLevelButton.getBottom());

    }

    public void setOnNextLevelButtonAction(OnClickListener action) {
        mNextLevelButton.setOnClickListener(action);
    }

    public void showLoading() {
//        expandView();

        mIsLoading = true;

        mNextLevelButton.setEnable(false);
        mNextLevelButton.setLoadingVisibility(View.VISIBLE);
        mNextLevelButton.setVisibility(VISIBLE);
        mMoreDetailView.setVisibility(INVISIBLE);
        mScoreRowView.setVisibility(GONE);
        mTotalPriceRowView.setVisibility(GONE);
        mLoadingStateView.setVisibility(GONE);
        mArrowUpIcon.setVisibility(VISIBLE);

//        mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_LOADING, mLoadingText, false);
//        mLoadingStateView.setVisibility(VISIBLE);
    }

    public void showResult(boolean hasError, String errorMessage, LoadingStateView.RetryListener retryListener) {
        if (hasError) {
            mIsLoading = false;

            mNextLevelButton.setVisibility(GONE);
            mNextLevelButton.setLoadingVisibility(View.GONE);
            mMoreDetailView.setVisibility(INVISIBLE);
            mScoreRowView.setVisibility(GONE);
            mTotalPriceRowView.setVisibility(GONE);
            mArrowUpIcon.setVisibility(GONE);

            mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_ERROR, !(errorMessage.isEmpty()) ? errorMessage : mErrorText, true);
            mLoadingStateView.setRetryListener(retryListener);
            mLoadingStateView.setVisibility(VISIBLE);

            requestLayout();
        } else {
            mIsLoading = false;

            mNextLevelButton.setEnable(true);
            mNextLevelButton.setLoadingVisibility(View.GONE);
//            mMoreDetailView.setVisibility(VISIBLE);
            mScoreRowView.setVisibility(VISIBLE);
            mTotalPriceRowView.setVisibility(VISIBLE);
            mArrowUpIcon.setVisibility(VISIBLE);

            mLoadingStateView.setVisibility(GONE);
        }
    }

    public ButtonElement getNextLevelButton() {
        return mNextLevelButton;
    }

    public void setNextLevelButtonLoading(boolean visibility) {
        if (visibility) {
            mNextLevelButton.setLoadingVisibility(View.VISIBLE);
            mNextLevelButton.setClickable(false);
        } else {
            mNextLevelButton.setLoadingVisibility(View.INVISIBLE);
            mNextLevelButton.setClickable(true);
        }
    }

    public void setNextLevelButtonEnabled(boolean enabled) {
        mNextLevelButton.setEnable(enabled);
    }

    public void setLoadingRetryListener(LoadingStateView.RetryListener listener) {
        mLoadingStateView.setRetryListener(listener);
    }
}
