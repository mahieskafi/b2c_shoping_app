package com.srp.eways.ui.view.charge.b.spinner;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.srp.eways.R;
import com.srp.eways.util.Utils;

import ir.abmyapp.androidsdk.ABResources;
import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by Eskafi on 10/5/2019.
 */
public class ClickableSpinner extends CardView implements IClickableSpinner {

    public interface ClickableSpinnerViewListener {
        void onChargeInputViewClicked();

        void onAddButtonClicked(long amount);
    }

    public interface ChargeInputViewListener extends ClickableSpinnerViewListener {

        double getTax(long amount);

        double getCoef();

    }

    private AppCompatImageView mImageIcon;
    private AppCompatTextView mTextAmount;
    private AppCompatTextView mTextAmountHint;
    private AppCompatImageView mImageAddChoice;

    private int mIconMarginTop;

    private int mIconWidth;
    private int mIconHeight;

    private int mImageArrowWidth;
    private int mImageArrowHeight;

    private Rect mIconBounds = new Rect();
    private Rect mTextAmountBounds = new Rect();
    private Rect mTextAmountHintBounds = new Rect();
    private Rect mArrowIconBounds = new Rect();

    private int mIconMarginRight;
    private int mTextAmountMarginRight;
    private int mHintAmountMarginRight;
    private int mIconArrowMarginLeft;

    private int cardElevation;

    private ClickableSpinnerViewListener mClickableSpinnerViewListener;

    private int textHeight;

    public ClickableSpinner(@NonNull Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public ClickableSpinner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public ClickableSpinner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        IABResources abResources = ABResources.get(context);

        cardElevation = abResources.getDimenPixelSize(R.dimen.charge_input_view_shadowheight);

        mImageIcon = new AppCompatImageView(context);
        mImageIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageIcon.setImageDrawable(abResources.getDrawable(R.drawable.ic_charge_choice_b));

        Typeface typeface = ResourcesCompat.getFont(context, R.font.iran_yekan);
        float amountTextSize = abResources.getDimen(R.dimen.charge_choice_input_text_size_b);

        Typeface typefaceHint = ResourcesCompat.getFont(context, R.font.iran_yekan_light);
        float amountHintSize = abResources.getDimen(R.dimen.charge_choice_input_hint_size_b);


        Paint helperPaint = new Paint();
        helperPaint.setTypeface(typefaceHint);
        helperPaint.setTextSize(amountHintSize);

        Paint.FontMetrics fontMetrics = helperPaint.getFontMetrics();
        textHeight = (int) ((fontMetrics.descent - fontMetrics.ascent));

        mTextAmount = new AppCompatTextView(context);
        mTextAmount.setTextSize(TypedValue.COMPLEX_UNIT_PX, amountTextSize);
        mTextAmount.setTextColor(abResources.getColor(R.color.charge_choice_input_text_color_b));
        mTextAmount.setTypeface(typeface);
        mTextAmount.setSingleLine();
        mTextAmount.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        mTextAmount.setGravity(Gravity.RIGHT);
        mTextAmount.setVisibility(GONE);


        mTextAmountHint = new AppCompatTextView(context);
        mTextAmountHint.setTextSize(TypedValue.COMPLEX_UNIT_PX, amountHintSize);
        mTextAmountHint.setTextColor(abResources.getColor(R.color.charge_choice_input_hint_color_b));
        mTextAmountHint.setTypeface(typefaceHint);
        mTextAmountHint.setVisibility(VISIBLE);

        mImageAddChoice = new AppCompatImageView(context);
        mImageAddChoice.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mImageAddChoice.setImageDrawable(abResources.getDrawable(R.drawable.ic_arrow_charge_input));

        setRadius(abResources.getDimen(R.dimen.charge_input_view_cornerradius));
        setCardElevation(cardElevation);

        mIconMarginTop = abResources.getDimenPixelSize(R.dimen.phonenumberview_contacticon_margintop);
        mIconWidth = abResources.getDimenPixelSize(R.dimen.charge_choice_input_icon_width);
        mIconHeight = abResources.getDimenPixelSize(R.dimen.charge_choice_input_icon_height);

        mIconArrowMarginLeft = abResources.getDimenPixelSize(R.dimen.charge_choice_input_arrow_icon_marginleft);

        mImageArrowWidth = abResources.getDimenPixelSize(R.dimen.charge_choice_input_arrow_icon_width);
        mImageArrowHeight = abResources.getDimenPixelSize(R.dimen.charge_choice_input_arrow_icon_height);

        mIconMarginRight = abResources.getDimenPixelSize(R.dimen.phonenumberview_contacticon_marginright);
        mTextAmountMarginRight = abResources.getDimenPixelSize(R.dimen.charge_choice_input_text_marginright);
        mHintAmountMarginRight = abResources.getDimenPixelSize(R.dimen.charge_choice_input_text_marginright);

        addView(mImageIcon);
        addView(mTextAmount);
        addView(mTextAmountHint);
        addView(mImageAddChoice);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickableSpinnerViewListener != null) {
                    mClickableSpinnerViewListener.onChargeInputViewClicked();
                }
            }
        });
    }


    public void setHint(String hint) {
        mTextAmountHint.setText(hint);
        mTextAmountHint.setVisibility(VISIBLE);
        mTextAmount.setVisibility(GONE);
    }

    public void setText(String text) {
        mTextAmount.setVisibility(VISIBLE);
        mTextAmountHint.setVisibility(GONE);
        mTextAmount.setText(Utils.toPersianNumber(text));
    }

    public void setIcon(Drawable drawable) {
        mImageIcon.setImageDrawable(drawable);
    }

    public void setIconVisibility(boolean isVisible) {
        if (isVisible) {
            mImageIcon.setVisibility(VISIBLE);
        } else {
            mImageIcon.setVisibility(GONE);
        }
    }

    public void setClickableSpinnerViewListener(ClickableSpinnerViewListener listener) {
        mClickableSpinnerViewListener = listener;
    }

    public void setHasElevation(boolean hasElevation) {
        if (hasElevation) {
            setCardElevation(cardElevation);
        } else {
            setCardElevation(0);
        }
    }

    public void setBackground(int background) {
        setBackgroundResource(background);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = mIconMarginTop + mIconHeight + mIconMarginTop;

        int width = MeasureSpec.getSize(widthMeasureSpec);

        mIconBounds.right = width - mIconMarginRight;
        mIconBounds.left = mIconBounds.right - mIconWidth;
        mIconBounds.top = mIconMarginTop;
        mIconBounds.bottom = mIconBounds.top + mIconHeight;

        mTextAmount.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        mTextAmountHint.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        mTextAmountBounds.right = mImageIcon.getVisibility() == VISIBLE ? mIconBounds.left : width - mTextAmountMarginRight;
        mTextAmountBounds.left = mIconArrowMarginLeft + mImageArrowWidth + mIconArrowMarginLeft;
        mTextAmountBounds.top = height / 2 - (mTextAmount.getMeasuredHeight() / 2);
        mTextAmountBounds.bottom = height / 2 + (mTextAmount.getMeasuredHeight() / 2);

        mTextAmountHintBounds.right = mImageIcon.getVisibility() == VISIBLE ? mIconBounds.left : width - mTextAmountMarginRight;
        mTextAmountHintBounds.left = mIconArrowMarginLeft + mImageArrowWidth + mIconArrowMarginLeft;
        mTextAmountHintBounds.top = height / 2 - (mTextAmountHint.getMeasuredHeight() / 2);
        mTextAmountHintBounds.bottom = height / 2 + (mTextAmountHint.getMeasuredHeight() / 2);

        mArrowIconBounds.left = mIconArrowMarginLeft;
        mArrowIconBounds.right = mArrowIconBounds.left + mImageArrowWidth;
        mArrowIconBounds.top = height / 2 - (mImageArrowHeight / 2);
        mArrowIconBounds.bottom = height / 2 + (mImageArrowHeight / 2);

        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        layoutChild(mImageIcon, mIconBounds);
        layoutChild(mTextAmount, mTextAmountBounds);
        layoutChild(mTextAmountHint, mTextAmountHintBounds);
        layoutChild(mImageAddChoice, mArrowIconBounds);
    }

    private void layoutChild(View child, Rect bounds) {
        child.measure(MeasureSpec.makeMeasureSpec(bounds.width(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(bounds.height(), MeasureSpec.EXACTLY));
        child.layout(bounds.left, bounds.top, bounds.right, bounds.bottom);
    }

    public String getText() {
        return mTextAmount.getText().toString();
    }

    public AppCompatTextView getInputText() {
        return mTextAmount;
    }


}
