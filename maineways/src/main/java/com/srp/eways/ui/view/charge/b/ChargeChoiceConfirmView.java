package com.srp.eways.ui.view.charge.b;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.charge.model.IChargeChoice;

import ir.abmyapp.androidsdk.IABResources;

public class ChargeChoiceConfirmView extends LinearLayout {

    public interface Listener {

        void OnButtonClicked();

    }

    private static final int STATE_SHOW_BUTTON_ONLY = 0;
    private static final int STATE_SHOW_TITLE_AND_BUTTON = 1;

    private AppCompatTextView mTextSelectedChargeInfo;
    private Button mButtonConfirm;

    private int mButtonMarginLeft;

    private int mButtonEnabledBackgroundColor;
    private int mButtonDisabledBackgroundColor;

    private int mButtonConfirmMinLength;
    private int mButtonConfirmMaxLength;

    private ValueAnimator mScaleUpAnimator;
    private ValueAnimator mScaleDownAnimator;

    private Object mChoiceAmountSpan;

    private IChargeChoice mSelectedChargeChoice;

    private int mState = STATE_SHOW_BUTTON_ONLY;

    private Listener mListener;

    public ChargeChoiceConfirmView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public ChargeChoiceConfirmView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public ChargeChoiceConfirmView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        setClipChildren(false);
        setClipToPadding(false);

        IABResources abResources = DIMain.getABResources();

        mButtonEnabledBackgroundColor = abResources.getColor(R.color.chargechoice_confirmview_button_enabled_background);
        mButtonDisabledBackgroundColor = abResources.getColor(R.color.chargechoice_confirmview_button_diabled_background);

        mTextSelectedChargeInfo = new AppCompatTextView(context);
        mButtonConfirm = new Button(context);

        mButtonMarginLeft = abResources.getDimenPixelSize(R.dimen.chargechoice_confirmview_button_margin_left);

        mTextSelectedChargeInfo.setGravity(Gravity.RIGHT);

        LinearLayout.LayoutParams titleLp = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleLp.weight = 1;
        titleLp.rightMargin = mButtonMarginLeft;
        mTextSelectedChargeInfo.setLayoutParams(titleLp);

        mButtonConfirmMaxLength = context.getResources().getDisplayMetrics().widthPixels - (2 * mButtonMarginLeft);
        mButtonConfirmMinLength = (int) (0.4 * mButtonConfirmMaxLength);

        mButtonConfirm.setTextColor(abResources.getColor(R.color.chargechoice_confirmview_button_text_color));
        mButtonConfirm.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.chargechoice_confirmview_button_text_size));
        setButtonBackground(mButtonDisabledBackgroundColor);
        mButtonConfirm.setText(abResources.getString(R.string.chargechoice_confirmview_button_title));
        mButtonConfirm.setTypeface(ResourcesCompat.getFont(context, R.font.iran_yekan_light));

        LinearLayout.LayoutParams buttonLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonLp.leftMargin = mButtonMarginLeft;
        buttonLp.rightMargin = mButtonMarginLeft;
        mButtonConfirm.setLayoutParams(buttonLp);

        addView(mButtonConfirm);
        addView(mTextSelectedChargeInfo);

        mTextSelectedChargeInfo.setText(getAmountString());

        mButtonConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnButtonClicked();
                }
            }
        });

        makeScaleDownAnimator();
        makeScaleUpAnimator();

        final int choiceTitleTextSize = abResources.getDimenPixelSize(R.dimen.chargechoice_confirmview_amount_title_textsize);
        final int choiceTitleTextColor = abResources.getColor(R.color.chargechoice_confirmview_amount_title_color);
        final Typeface choiceTitleFont = ResourcesCompat.getFont(context, R.font.iran_yekan);

        mTextSelectedChargeInfo.setTextSize(TypedValue.COMPLEX_UNIT_PX, choiceTitleTextSize);
        mTextSelectedChargeInfo.setTypeface(choiceTitleFont);
        mTextSelectedChargeInfo.setTextColor(choiceTitleTextColor);

        final float choiceAmountTextSize = abResources.getDimen(R.dimen.chargechoice_confirmview_amount_value_textsize);
        final int choiceAmountTextColor = abResources.getColor(R.color.chargechoice_confirmview_amount_value_color);
        final Typeface choiceAmountFont = ResourcesCompat.getFont(context, R.font.iran_yekan_medium);

        mChoiceAmountSpan = new MetricAffectingSpan() {

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setTypeface(choiceAmountFont);
                textPaint.setTextSize(choiceAmountTextSize);
                textPaint.setColor(choiceAmountTextColor);
            }

            @Override
            public void updateMeasureState(@NonNull TextPaint textPaint) {
                textPaint.setTypeface(choiceAmountFont);
                textPaint.setTextSize(choiceAmountTextSize);
                textPaint.setColor(choiceAmountTextColor);
            }
        };
    }

    public void setListener(Listener listener) {
       mListener = listener;
    }

    public void setChargeChoice(IChargeChoice selectedChargeChoice) {
        if (selectedChargeChoice == null) {
            mButtonConfirm.setEnabled(false);
        }
        else {
            mButtonConfirm.setEnabled(true);
        }

        if (selectedChargeChoice == mSelectedChargeChoice) {
            return;
        }

        cancelScaleDownAnimation();
        cancelScaleUpAnimation();

        mSelectedChargeChoice = selectedChargeChoice;

        if (mState == STATE_SHOW_BUTTON_ONLY) {
            if (selectedChargeChoice == null) {
                //do nothing
            }
            else {
                setButtonBackground(mButtonEnabledBackgroundColor);
                mScaleDownAnimator.start();
            }
        } else if (mState == STATE_SHOW_TITLE_AND_BUTTON){
            if (selectedChargeChoice == null) {
                setButtonBackground(mButtonDisabledBackgroundColor);
                mScaleUpAnimator.start();
            }
            else {
                mTextSelectedChargeInfo.setText(getAmountString());
            }
        }
    }

    private void makeScaleDownAnimator() {
        mScaleDownAnimator = new ValueAnimator();
        mScaleDownAnimator.setFloatValues(0, 1);
        mScaleDownAnimator.setDuration(400);
        mScaleDownAnimator.setInterpolator(new DecelerateInterpolator());
        mScaleDownAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float ratio = animation.getAnimatedFraction();

                if (ratio >= 0.6) {//buttonWidth's minWidth = 0.6 * (it's original width)
                    mScaleDownAnimator.cancel();
                    return;
                }

                int buttonNewWidth = (int) ((1 - ratio) * mButtonConfirmMaxLength);

                LinearLayout.LayoutParams buttonLP = (LayoutParams) mButtonConfirm.getLayoutParams();
                buttonLP.width = buttonNewWidth;

                mButtonConfirm.setLayoutParams(buttonLP);
            }
        });

        mScaleDownAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mState = STATE_SHOW_TITLE_AND_BUTTON;

                LinearLayout.LayoutParams buttonLP = (LayoutParams) mButtonConfirm.getLayoutParams();
                buttonLP.width = mButtonConfirmMinLength;
                mButtonConfirm.setLayoutParams(buttonLP);

                mTextSelectedChargeInfo.setText(getAmountString());
                mTextSelectedChargeInfo.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    private void makeScaleUpAnimator() {
        mScaleUpAnimator = new ValueAnimator();
        mScaleUpAnimator.setFloatValues(0, 1);
        mScaleUpAnimator.setDuration(400);
        mScaleUpAnimator.setInterpolator(new DecelerateInterpolator());
        mScaleUpAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float ratio = animation.getAnimatedFraction();

                int difference = mButtonConfirmMaxLength - mButtonConfirmMinLength;

                int buttonNewWidth = mButtonConfirmMinLength + (int) (ratio * difference);

                LinearLayout.LayoutParams buttonLP = (LayoutParams) mButtonConfirm.getLayoutParams();
                buttonLP.width = buttonNewWidth;

                mButtonConfirm.setLayoutParams(buttonLP);
            }
        });

        mScaleUpAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mTextSelectedChargeInfo.setText(getAmountString());
                mTextSelectedChargeInfo.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_out));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mState = STATE_SHOW_BUTTON_ONLY;

                LinearLayout.LayoutParams buttonLP = (LayoutParams) mButtonConfirm.getLayoutParams();
                buttonLP.width = mButtonConfirmMaxLength;
                mButtonConfirm.setLayoutParams(buttonLP);

                mTextSelectedChargeInfo.setText(getAmountString());
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    private void cancelScaleUpAnimation() {
        if (mScaleUpAnimator.isRunning() || mScaleUpAnimator.isStarted()) {
            mScaleUpAnimator.cancel();
        }
    }

    private void cancelScaleDownAnimation() {
        if (mScaleDownAnimator.isRunning() || mScaleDownAnimator.isStarted()) {
            mScaleDownAnimator.cancel();
        }
    }

    private SpannableStringBuilder getAmountString() {
        if (mSelectedChargeChoice == null) {
            return new SpannableStringBuilder("");
        }


        String choiceTitle = DIMain.getABResources().getString(R.string.chargechoice_confirmview_choice_title) + " ";
        String amount = mSelectedChargeChoice.getDisplayPaidAmount() + " ";
        String rial = DIMain.getABResources().getString(R.string.rial);

        SpannableStringBuilder ssb = new SpannableStringBuilder(choiceTitle + amount + rial);
//        ssb.setSpan(mChoiceTitleSpan, 0, choiceTitle.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        ssb.setSpan(mChoiceAmountSpan, choiceTitle.length(), choiceTitle.length() + amount.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//        ssb.setSpan(mChoiceTitleSpan, choiceTitle.length() + amountString.length(), ssb.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        return ssb;
    }

    private void setButtonBackground(int buttonBackgroundColor) {
        IABResources abResources = DIMain.getABResources();

        float cr = abResources.getDimen(R.dimen.chargechoice_confirmview_button_cornerradius);
        RoundRectShape shape = new RoundRectShape(new float[] { cr, cr, cr, cr, cr, cr, cr, cr }, null, null);

        ShapeDrawable drawable = new ShapeDrawable();
        drawable.setShape(shape);

        drawable.setColorFilter(buttonBackgroundColor, PorterDuff.Mode.SRC_IN);

        int paddingLeft = abResources.getDimenPixelSize(R.dimen.chargechoice_confirmview_button_padding_left);
        int paddingTop = abResources.getDimenPixelSize(R.dimen.chargechoice_confirmview_button_padding_top);
        int paddingRight = abResources.getDimenPixelSize(R.dimen.chargechoice_confirmview_button_padding_right);
        int paddingBottom = abResources.getDimenPixelSize(R.dimen.chargechoice_confirmview_button_padding_bottom);
        drawable.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

        mButtonConfirm.setBackground(drawable);
        //Todo: set elevation for this view from it's parent fragment(4dp).
//        ViewCompat.setElevation(mButtonConfirm, getContext().getResources().getDimension(R.dimen.chargechoice_confirmview_button_elevation));
    }

}
