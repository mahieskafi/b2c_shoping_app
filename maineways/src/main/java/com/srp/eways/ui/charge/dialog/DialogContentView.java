package com.srp.eways.ui.charge.dialog;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.util.Utils;

import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by Eskafi on 10/8/2019.
 */
public class DialogContentView extends ViewGroup {

    private float mChoiceViewTitleTextSize;
    private int mChoiceViewTitleColor;
    private Typeface mChoiceViewTitleFont;

    private float mChoiceViewTitleRialTextSize;
    private Typeface mChoiceViewTitleRialFont;

    private int mChoiceTitleMarginRight;
    private int mChildContentViewMarginTop;
    private int mAmountViewMarginTop;

    private AppCompatTextView mChoiceTitle;
    private AppCompatTextView mChoiceTitleRial;
    private AppCompatImageView mChoiceTitleIcon;
    private AppCompatTextView mAmountText;
    private FrameLayout mChildContentView;

    private Object mAmountSpan;

    private Typeface amountFont;
    private float amountTextSize;

    private int mIconWidth;
    private int mIconHeight;

    private int mChildContentViewMinWidth;
    private int mSideMargin;

    private String mAmount;
    private String mAmountTitle;

    private boolean isBottomDialig = false;

    public DialogContentView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public DialogContentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public DialogContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        IABResources abResources = DIMain.getABResources();

        mSideMargin = 2 * abResources.getDimenPixelSize(R.dimen.dialog_content_margin_left_right);
        mChildContentViewMinWidth = (int) (getDisplayMetrics().widthPixels * 0.65) - mSideMargin;


        mChoiceTitleMarginRight = abResources.getDimenPixelSize(R.dimen.dialog_charge_choice_title_marginright);
        mChildContentViewMarginTop = abResources.getDimenPixelSize(R.dimen.dialog_charge_choice_content_view_margin_top);
        mAmountViewMarginTop = abResources.getDimenPixelSize(R.dimen.dialog_charge_choice_amount_margin_top);

        //title
        mChoiceViewTitleTextSize = abResources.getDimenPixelSize(R.dimen.dialog_charge_choice_title_textsize);
        mChoiceViewTitleFont = ResourcesCompat.getFont(context, R.font.iran_yekan_medium);
        mChoiceViewTitleColor = abResources.getColor(R.color.add_choice_dialog_title_textcolor);

        mChoiceTitle = new AppCompatTextView(getContext());
        mChoiceTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mChoiceViewTitleTextSize);
        mChoiceTitle.setTypeface(mChoiceViewTitleFont);
        mChoiceTitle.setTextColor(mChoiceViewTitleColor);
        mChoiceTitle.setText(abResources.getString(R.string.add_choice_dialog_title));

        //title rial
        mChoiceViewTitleRialTextSize = abResources.getDimenPixelSize(R.dimen.dialog_charge_choice_title_rial_textsize);
        mChoiceViewTitleRialFont = ResourcesCompat.getFont(context, R.font.iran_yekan);

        mChoiceTitleRial = new AppCompatTextView(getContext());
        mChoiceTitleRial.setTextSize(TypedValue.COMPLEX_UNIT_PX, mChoiceViewTitleRialTextSize);
        mChoiceTitleRial.setTypeface(mChoiceViewTitleRialFont);
        mChoiceTitleRial.setTextColor(mChoiceViewTitleColor);
        mChoiceTitleRial.setText(abResources.getString(R.string.toolbar_deposittitle_rials));
        showRial(false);

        //icon
        mIconWidth = abResources.getDimenPixelSize(R.dimen.dialog_charge_choice_icon_width);
        mIconHeight = abResources.getDimenPixelSize(R.dimen.dialog_charge_choice_icon_height);

        mChoiceTitleIcon = new AppCompatImageView(context);
        mChoiceTitleIcon.setImageDrawable(abResources.getDrawable(R.drawable.ic_add_choice_b));

        mChildContentView = new FrameLayout(context);

        //amount text
        mChoiceViewTitleTextSize = abResources.getDimenPixelSize(R.dimen.dialog_charge_choice_title_textsize);
        mChoiceViewTitleFont = ResourcesCompat.getFont(context, R.font.iran_yekan_medium);
        mChoiceViewTitleColor = abResources.getColor(R.color.add_choice_dialog_title_textcolor);

        mChoiceTitle = new AppCompatTextView(getContext());
        mChoiceTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mChoiceViewTitleTextSize);
        mChoiceTitle.setTypeface(mChoiceViewTitleFont);
        mChoiceTitle.setTextColor(mChoiceViewTitleColor);
        mChoiceTitle.setText(abResources.getString(R.string.add_choice_dialog_title));

        amountTextSize = abResources.getDimen(R.dimen.chargechoice_confirmview_amount_title_textsize);
        amountFont = ResourcesCompat.getFont(context, R.font.iran_yekan_medium);

        mAmountText = new AppCompatTextView(context);
        mAmountText.setTextSize(TypedValue.COMPLEX_UNIT_PX, amountTextSize);
        mAmountText.setTypeface(amountFont);
        mAmountText.setTextColor(abResources.getColor(R.color.add_choice_dialog_title_textcolor));
        mAmountText.setVisibility(GONE);

        final int amountTextColor = abResources.getColor(R.color.chargechoice_confirmview_amount_value_color);

        mAmountSpan = new MetricAffectingSpan() {

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setTypeface(amountFont);
                textPaint.setTextSize(amountTextSize);
                textPaint.setColor(amountTextColor);
            }

            @Override
            public void updateMeasureState(@NonNull TextPaint textPaint) {
                textPaint.setTypeface(amountFont);
                textPaint.setTextSize(amountTextSize);
                textPaint.setColor(amountTextColor);
            }
        };

        addView(mChoiceTitleIcon);
        addView(mChoiceTitle);
        addView(mChoiceTitleRial);
        addView(mChildContentView);
        addView(mAmountText);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);

        mChildContentView.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        if (isBottomDialig){
            mChildContentView.measure(
                    MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        }

        int childContentViewWidth;

        if (mChildContentView.getMeasuredWidth() > mChildContentViewMinWidth) {
            childContentViewWidth = mChildContentView.getMeasuredWidth();
        } else {
            childContentViewWidth = mChildContentViewMinWidth;
        }

        mChildContentView.measure(
                MeasureSpec.makeMeasureSpec(childContentViewWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        mChoiceTitleIcon.measure(
                MeasureSpec.makeMeasureSpec(mIconWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mIconHeight, MeasureSpec.EXACTLY));

        mAmountText.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        mChoiceTitle.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        mChoiceTitleRial.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        int maxWidth = Math.max(childContentViewWidth, mChildContentViewMinWidth);
        width = Math.min(maxWidth, getDisplayMetrics().widthPixels - mSideMargin);

        int height = mChoiceTitleIcon.getMeasuredHeight() + (mChildContentViewMarginTop) + mChildContentView.getMeasuredHeight();
        if (mAmountText.getVisibility() == VISIBLE) {
            height += mAmountViewMarginTop + mAmountText.getMeasuredHeight();
        }

        setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int width = getWidth();

        mChoiceTitleIcon.measure(
                MeasureSpec.makeMeasureSpec(mIconWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mIconHeight, MeasureSpec.EXACTLY));

        mChoiceTitleIcon.layout(
                width - mChoiceTitleIcon.getMeasuredWidth(),
                0,
                width,
                mChoiceTitleIcon.getMeasuredHeight());

        mChoiceTitle.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        int iconWidth = mChoiceTitleIcon.getVisibility() == VISIBLE ? mChoiceTitleIcon.getMeasuredWidth() + mChoiceTitleMarginRight : 0;
        int right = mChoiceTitleIcon.getVisibility() == VISIBLE ? mChoiceTitleIcon.getLeft() + mChoiceTitleMarginRight : width;

        mChoiceTitle.layout(
                width - iconWidth - mChoiceTitle.getMeasuredWidth(),
                mChoiceTitleIcon.getMeasuredHeight() / 2 - mChoiceTitle.getMeasuredHeight() / 2,
                right,
                mChoiceTitleIcon.getMeasuredHeight() / 2 + mChoiceTitle.getMeasuredHeight() / 2);

        mChoiceTitleRial.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        mChoiceTitleRial.layout(
                mChoiceTitle.getLeft() - mChoiceTitleRial.getMeasuredWidth(),
                mChoiceTitleIcon.getMeasuredHeight() / 2 - mChoiceTitleRial.getMeasuredHeight() / 2,
                mChoiceTitle.getLeft() + mChoiceTitleMarginRight,
                mChoiceTitleIcon.getMeasuredHeight() / 2 + mChoiceTitleRial.getMeasuredHeight() / 2);

        mChildContentView.layout(
                width - mChildContentView.getMeasuredWidth(),
                mChoiceTitle.getBottom() + mChildContentViewMarginTop,
                width,
                mChildContentView.getMeasuredHeight() + mChoiceTitle.getBottom() + mChildContentViewMarginTop);

        if (isBottomDialig) {
            mChildContentView.layout(
                    width - mChildContentView.getMeasuredWidth(),
                    mChildContentViewMarginTop,
                    width,
                    mChildContentView.getMeasuredHeight() + mChildContentViewMarginTop);
        }

        mAmountText.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        mAmountText.layout(
                width / 2 - mAmountText.getMeasuredWidth() / 2,
                mChildContentView.getBottom() + mAmountViewMarginTop,
                width / 2 + mAmountText.getMeasuredWidth() / 2,
                mChildContentView.getBottom() + mAmountViewMarginTop + mAmountText.getMeasuredHeight());


    }

    private SpannableStringBuilder getFinalAmountString(String amount) {

        String title = mAmountTitle;

        if (title.isEmpty()) {
            title = DIMain.getABResources().getString(R.string.add_choice_dialog_amount_title) + " ";
        }

        String amountText = Utils.toPersianNumber(amount) + " ";
        String rial = DIMain.getABResources().getString(R.string.rial);

        SpannableStringBuilder ssb = new SpannableStringBuilder(title + amountText + rial);
        ssb.setSpan(mAmountSpan, title.length(), title.length() + amountText.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        return ssb;
    }

    public void setAmountText(String amountText) {
        mAmountTitle = amountText;
    }

    public void setAmount(String amount) {
        mAmount = amount;

        mAmountText.setVisibility(VISIBLE);
        mAmountText.setText(getFinalAmountString(amount));
    }

    public String getAmount() {
        return mAmount;
    }

    public void setIcon(Drawable icon) {
        mChoiceTitleIcon.setImageDrawable(icon);
    }

    public void setTitleTextSize(int size) {
        mChoiceTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setTitleRialTextSize(int size) {
        mChoiceTitleRial.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }


    public void setIconVisibility(boolean visibility) {
        if (visibility) {
            mChoiceTitleIcon.setVisibility(VISIBLE);
        } else {

            mChoiceTitleIcon.setVisibility(GONE);
        }
    }

    public void setChoiceTitleVisibility(boolean visibility) {
        if (visibility) {
            mChoiceTitle.setVisibility(VISIBLE);
        } else {
            mChoiceTitle.setVisibility(GONE);
        }
    }

    public void setChoiceTitle(String title) {
        mChoiceTitle.setText(title);
    }

    public void setChildContentView(View view) {
        if (view != null) {
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
            mChildContentView.addView(view);
        }
    }

    public View getChildContentView() {
        return mChildContentView;
    }

    public void showRial(boolean show) {
        mChoiceTitleRial.setVisibility(show ? VISIBLE : GONE);
    }

    private DisplayMetrics getDisplayMetrics() {
        DisplayMetrics metrics = new DisplayMetrics();

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);

        return metrics;
    }

    public void setBottomDialig(boolean bottomDialig) {
        isBottomDialig = bottomDialig;
    }
}
