package com.srp.eways.ui.view.charge.a;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.CompoundButton;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.charge.model.IOperator;
import com.srp.eways.ui.view.charge.IOperatorsView;
import com.srp.eways.util.Utils;

import java.util.List;

import ir.abmyapp.androidsdk.ABResources;
import ir.abmyapp.androidsdk.IABResources;

public class OperatorsViewA extends IOperatorsView {

    private static final long ANIMATION_DURATION = 1000;

    private GradientDrawable mDrawableOperatorAnimation;
    private Drawable mDrawablePhone;

    private AppCompatTextView mTextPhoneNumber;

    private AppCompatTextView mTextOperatorName;
    private Drawable mDrawableOperator;

    private Drawable mDrawableCancelOperator;

    private SwitchCompat mSwitchTransport;
    private AppCompatTextView mTextSwitchTransportTitle;

    private AppCompatTextView mTextChooseTransportOptionTitle;
    private Drawable mDividerChooseTransportOption;

    private int mOperatorColorLineHeight;

    private int mOperatorColorLineMarginLeft;
    private int mOperatorColorLineMarginTop;
    private int mOperatorColorLineMarginRight;

    private int mImagePhoneWidth;
    private int mImagePhoneHeight;

    private int mImagePhoneMarginTop;
    private int mImagePhoneMarginRight;

    private int mTextPhoneNumberMarginRight;

    private int mImageCancelOperatorWidth;
    private int mImageCancelOperatorMarginLeft;

    private int mOperatorDrawableWidth;
    private int mOperatorDrawableMarginLeft;

    private int mTextOperatorNameMarginLeft;

    private int mSwitchTransportHeight;
    private int mSwitchTransportWidth;

    private int mTransportSwitchMarginTop;
    private int mTransportSwitchMarginRight;

    private int mTransportSwitchTitleMarginRight;

    private int mTextChooseOperatorMarginTop;
    private int mTextChooseOperatorMarginRight;

    private int mChooseOperatorDividerMarginLeft;
    private int mChooseOperatorDividerMarginRight;

    private int mIconTransportOptionMarginTop;

    private int mIconTransportOptionHeight;

    private int mContentPaddingBottom;

    private int mDividerChooseOperatorHeight;

    private Rect mPhoneTextBounds = new Rect();

    private Rect mOperatorNameBounds = new Rect();

    private Rect mCancelOperatorIconBounds = new Rect();

    private Rect mTransportSwitchBounds = new Rect();
    private Rect mTransportSwitchTitleBounds = new Rect();

    private Rect mChooseOperatorTitleBounds = new Rect();

    private Drawable[] mTransportableOperatorDrawables = new Drawable[3];

    private float mDeltaY;

    private ValueAnimator mAnimator;

    private float mRatio = 0;

    private String mPhoneNumber;
    private IOperator mOperator;

    private OperatorsViewListener mListener;

    public OperatorsViewA(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public OperatorsViewA(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public OperatorsViewA(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        setWillNotDraw(false);

        // prepare resources
        IABResources abResources = ABResources.get(context);

        setCardElevation(abResources.getDimen(R.dimen.phoneandoperatorsview_cardelevation));
        setRadius(abResources.getDimen(R.dimen.phoneandoperatorsview_cardecornerradius));

        mOperatorColorLineHeight = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_colorline_height);

        mOperatorColorLineMarginLeft = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_colorline_marginleft);
        mOperatorColorLineMarginTop = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_colorline_margintop);
        mOperatorColorLineMarginRight = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_colorline_marginright);

        mImagePhoneWidth = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_image_phone_width);
        mImagePhoneHeight = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_image_phone_height);

        mImagePhoneMarginTop = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_image_phone_margintop);
        mImagePhoneMarginRight = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_image_phone_marginright);

        mTextPhoneNumberMarginRight = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_text_phone_marginright);

        mImageCancelOperatorWidth = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_image_canceloperator_width);
        mImageCancelOperatorMarginLeft = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_image_canceloperator_marginleft);

        mOperatorDrawableMarginLeft = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_drawable_operatoricon_marginleft);
        mOperatorDrawableWidth = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_drawable_operatoricon_width);

        mTextOperatorNameMarginLeft = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_text_operatorname_marginleft);

        mTransportSwitchMarginTop = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_switch_transport_margintop);
        mTransportSwitchMarginRight = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_switch_transport_marginright);

        mTransportSwitchTitleMarginRight = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_switchtitle_transport_marginright);

        mTextChooseOperatorMarginRight = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_title_chooseoperator_marginright);
        mTextChooseOperatorMarginTop = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_title_chooseoperator_margintop);

        mChooseOperatorDividerMarginLeft = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_divider_chooseoperator_marginleft);
        mChooseOperatorDividerMarginRight = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_divider_chooseoperator_marginright);

        mIconTransportOptionMarginTop = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_icon_transportoption_margintop);

        mIconTransportOptionHeight = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_icon_transportoption_first_width);

        mContentPaddingBottom = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_contentpadding_bottom);

        float phoneNumberTextSize = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_phonenumber_textsize);
        Typeface phoneNumberFont = ResourcesCompat.getFont(context, R.font.iran_yekan);
        int phoneNumberTextColor = abResources.getColor(R.color.phoneandoperatorsview_phonenumber_textcolor);

        float operatorNameTextSize = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_operatorname_textsize);
        Typeface operatorNameFont = ResourcesCompat.getFont(context, R.font.iran_yekan);
        int operatorNameTextColor = abResources.getColor(R.color.phoneandoperatorsview_operatorname_textcolor);

        float transportSwitchTitleTextSize = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_transportswitch_textsize);
        Typeface transportSwitchTitleFont = ResourcesCompat.getFont(context, R.font.iran_yekan);
        int transportSwitchTitleColor = abResources.getColor(R.color.phoneandoperatorsview_transportswitch_textcolor);

        float chooseTransportOptionTitleTextSize = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_transportoption_title_textsize);
        final Typeface chooseTransportOptionCheckedTitleFont = ResourcesCompat.getFont(context, R.font.iran_yekan);
        final Typeface chooseTransportOptionNotCheckedTitleFont = ResourcesCompat.getFont(context, R.font.iran_yekan_light);
        int chooseTransportOptionTitleColor = abResources.getColor(R.color.phoneandoperatorsview_transportoption_title_textcolor);

        mDividerChooseOperatorHeight = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_divider_height);

        // create child views
        mDrawableOperatorAnimation = new GradientDrawable();
        mDrawableOperatorAnimation.setCornerRadius(abResources.getDimen(R.dimen.phoneandoperatorsview_colorline_cornerradius));

        mDrawablePhone = abResources.getDrawable(R.drawable.ic_mobile);

        mTextPhoneNumber = new AppCompatTextView(context);
        mTextPhoneNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, phoneNumberTextSize);
        mTextPhoneNumber.setTypeface(phoneNumberFont);
        mTextPhoneNumber.setTextColor(phoneNumberTextColor);

        mTextOperatorName = new AppCompatTextView(context);
        mTextOperatorName.setTextSize(TypedValue.COMPLEX_UNIT_PX, operatorNameTextSize);
        mTextOperatorName.setTypeface(operatorNameFont);
        mTextOperatorName.setTextColor(operatorNameTextColor);

        mDrawableCancelOperator = abResources.getDrawable(R.drawable.ic_cross_cancel_contact);

        mSwitchTransportHeight = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_transportswitch_height);
        mSwitchTransportWidth = abResources.getDimenPixelSize(R.dimen.phoneandoperatorsview_transportswitch_width);

        mSwitchTransport = new SwitchCompat(context);
        mSwitchTransport.setShowText(false);
        mSwitchTransport.setChecked(false);
        mSwitchTransport.setMinWidth(0);
        mSwitchTransport.setGravity(Gravity.RIGHT);

        setSwitchStyle();

        mTextSwitchTransportTitle = new AppCompatTextView(context);
        mTextSwitchTransportTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, transportSwitchTitleTextSize);
        mTextSwitchTransportTitle.setTypeface(transportSwitchTitleFont);
        mTextSwitchTransportTitle.setTextColor(transportSwitchTitleColor);
        mTextSwitchTransportTitle.setText(abResources.getString(R.string.phoneandoperatorsview_transportswitch_title));

        mSwitchTransport.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTextSwitchTransportTitle.setTypeface(isChecked ? chooseTransportOptionCheckedTitleFont : chooseTransportOptionNotCheckedTitleFont);
                requestLayout();
            }
        });

        mTextChooseTransportOptionTitle = new AppCompatTextView(context);
        mTextChooseTransportOptionTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, chooseTransportOptionTitleTextSize);
        mTextChooseTransportOptionTitle.setTypeface(chooseTransportOptionNotCheckedTitleFont);
        mTextChooseTransportOptionTitle.setTextColor(chooseTransportOptionTitleColor);
        mTextChooseTransportOptionTitle.setText(abResources.getString(R.string.phoneandoperatorsview_transportoptions_title));

        Paint paint = new Paint();
        paint.setTypeface(chooseTransportOptionCheckedTitleFont);
        paint.setTextSize(chooseTransportOptionTitleTextSize);

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        mDeltaY = (fontMetrics.ascent + fontMetrics.descent) / 2;


        mDividerChooseTransportOption = new ColorDrawable(abResources.getColor(R.color.phoneandoperatorsview_transportoptions_divider_color));

        // add child views
        addView(mTextPhoneNumber);
        addView(mTextOperatorName);
        addView(mSwitchTransport);
        addView(mTextSwitchTransportTitle);
        addView(mTextChooseTransportOptionTitle);

        mAnimator = new ValueAnimator();
        mAnimator.setFloatValues(0, 1);
        mAnimator.setDuration(ANIMATION_DURATION);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setOperatorAnimationProgressRatio((Float) animation.getAnimatedValue());
            }
        });
        mAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) { }

            @Override
            public void onAnimationEnd(Animator animation) {
                setOperatorAnimationProgressRatio(1);

                if (mListener != null) {
                    mListener.onOperatorLoadAnimationEnded();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) { }

            @Override
            public void onAnimationRepeat(Animator animation) { }

        });
    }

    private void setSwitchStyle() {
        IABResources abResources = DIMain.getABResources();

        int switchDisabledThumbColor = abResources.getColor(R.color.switch_disabled_thumb_color);
        int switchEnabledThumbColor = abResources.getColor(R.color.switch_enabled_thumb_color);

        int switchDisabledTrackColor = abResources.getColor(R.color.switch_disabled_track_color);
        int switchEnabledTrackColor = abResources.getColor(R.color.switch_enabled_track_color);

        int[][] states = new int[][] {
                new int[] {-android.R.attr.state_checked},
                new int[] {android.R.attr.state_checked},
        };

        int[] thumbColors = new int[] {
                switchDisabledThumbColor,
                switchEnabledThumbColor,
        };

        int[] trackColors = new int[] {
                switchDisabledTrackColor,
                switchEnabledTrackColor,
        };

        DrawableCompat.setTintList(DrawableCompat.wrap(mSwitchTransport.getThumbDrawable()), new ColorStateList(states, thumbColors));
        DrawableCompat.setTintList(DrawableCompat.wrap(mSwitchTransport.getTrackDrawable()), new ColorStateList(states, trackColors));
    }

    private void setOperatorAnimationProgressRatio(float ratio) {
        mRatio = ratio;

        updateOperatorAnimationBounds();
        invalidate();
    }

    @Override
    public void setListener(OperatorsViewListener listener) {
        mListener = listener;
    }

    @Override
    public void setData(String phoneNumber, IOperator operator, boolean animate) {
        mPhoneNumber = phoneNumber;
        mOperator = operator;
        updateOperatorRelatedIcons();

        IABResources abResources = ABResources.get(getContext());

        mTextPhoneNumber.setText(Utils.toPersianNumber(phoneNumber));
        mTextOperatorName.setText(operator.getName());

        mDrawableOperatorAnimation.setColor(abResources.getColor(operator.getOperatorColor()));

        if (mAnimator.isRunning()) {
            mAnimator.cancel();
        }

        if (animate) {
            mAnimator.start();
        }
        else {
            setOperatorAnimationProgressRatio(1);
        }

        mSwitchTransport.setChecked(false);
        requestLayout();
    }

    private void updateOperatorRelatedIcons() {
        IABResources abResources = ABResources.get(getContext());

        mDrawableOperator = abResources.getDrawable(mOperator.getIconResId());

        List<IOperator> transportableOperators = mOperator.getTransportableOperators();

        for (int i = 0; i < transportableOperators.size(); ++i) {
            IOperator transportableOperator = transportableOperators.get(i);

            if (transportableOperator == null) {
                mTransportableOperatorDrawables[i] = null;
            } else {
                mTransportableOperatorDrawables[i] = abResources.getDrawable(transportableOperator.getTransportableIconResId());
            }
        }
    }

    private void updateOperatorAnimationBounds() {
        int width = getWidth();

        if (getWidth() > 0) {
            int animationLength = width - mOperatorColorLineMarginRight - mOperatorColorLineMarginLeft;
            int animationLeft = width - (int) (mRatio * animationLength) - mOperatorColorLineMarginLeft;
            animationLeft = animationLeft > mOperatorColorLineMarginLeft ? animationLeft : mOperatorColorLineMarginLeft;

            mDrawableOperatorAnimation.setBounds(animationLeft, mOperatorColorLineMarginTop, width - mOperatorColorLineMarginRight, mOperatorColorLineMarginTop + mOperatorColorLineHeight);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);

        mDrawablePhone.setBounds(width - mImagePhoneMarginRight - mImagePhoneWidth, mDrawableOperatorAnimation.getBounds().bottom + mImagePhoneMarginTop, width - mImagePhoneMarginRight, mDrawableOperatorAnimation.getBounds().bottom + mImagePhoneMarginTop + mImagePhoneHeight);

        mTextPhoneNumber.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        mPhoneTextBounds.right = mDrawablePhone.getBounds().left - mTextPhoneNumberMarginRight;
        mPhoneTextBounds.top = mDrawablePhone.getBounds().centerY() - (mTextPhoneNumber.getMeasuredHeight() / 2);
        mPhoneTextBounds.left = mPhoneTextBounds.right - mTextPhoneNumber.getMeasuredWidth();
        mPhoneTextBounds.bottom = mDrawablePhone.getBounds().centerY() + (mTextPhoneNumber.getMeasuredHeight() / 2);

        mCancelOperatorIconBounds.left = mImageCancelOperatorMarginLeft;
        mCancelOperatorIconBounds.top = mDrawablePhone.getBounds().centerY() - (mImageCancelOperatorWidth / 2);
        mCancelOperatorIconBounds.right = mCancelOperatorIconBounds.left + mImageCancelOperatorWidth;
        mCancelOperatorIconBounds.bottom = mDrawablePhone.getBounds().centerY() + (mImageCancelOperatorWidth / 2);

        mDrawableCancelOperator.setBounds(mCancelOperatorIconBounds);

        mDrawableOperator.setBounds(mCancelOperatorIconBounds.right + mOperatorDrawableMarginLeft, mCancelOperatorIconBounds.centerY() - (mOperatorDrawableWidth / 2), mCancelOperatorIconBounds.right + mOperatorDrawableMarginLeft + mOperatorDrawableWidth, mCancelOperatorIconBounds.centerY() + (mOperatorDrawableWidth / 2));

        mTextOperatorName.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        mOperatorNameBounds.left = mDrawableOperator.getBounds().right + mTextOperatorNameMarginLeft;
        mOperatorNameBounds.top = mCancelOperatorIconBounds.centerY() - (mTextOperatorName.getMeasuredHeight() / 2);
        mOperatorNameBounds.right = mOperatorNameBounds.left + mTextOperatorName.getMeasuredWidth();
        mOperatorNameBounds.bottom = mCancelOperatorIconBounds.centerY() + (mTextOperatorName.getMeasuredHeight() / 2);

        mSwitchTransport.measure(MeasureSpec.makeMeasureSpec(mSwitchTransportWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mSwitchTransportHeight, MeasureSpec.EXACTLY));

        mTransportSwitchBounds.right = width - mTransportSwitchMarginRight;
        mTransportSwitchBounds.left = mTransportSwitchBounds.right - mSwitchTransport.getMeasuredWidth();
        mTransportSwitchBounds.top = mDrawablePhone.getBounds().bottom + mTransportSwitchMarginTop;
        mTransportSwitchBounds.bottom = mTransportSwitchBounds.top + mSwitchTransport.getMeasuredHeight();

        mTextSwitchTransportTitle.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        mTransportSwitchTitleBounds.right = mTransportSwitchBounds.left - mTransportSwitchTitleMarginRight;
        mTransportSwitchTitleBounds.left = mTransportSwitchTitleBounds.right - mTextSwitchTransportTitle.getMeasuredWidth();
        mTransportSwitchTitleBounds.top = mTransportSwitchBounds.centerY() - (mTextSwitchTransportTitle.getMeasuredHeight() / 2);
        mTransportSwitchTitleBounds.bottom = mTransportSwitchBounds.centerY() + (mTextSwitchTransportTitle.getMeasuredHeight() / 2);

        int height = mOperatorColorLineMarginTop
                + mOperatorColorLineHeight
                + mImagePhoneMarginTop
                + mImagePhoneHeight
                + mTransportSwitchMarginTop
                + mSwitchTransportHeight
                + mContentPaddingBottom;

        if (mSwitchTransport.isChecked()) {
            mTextChooseTransportOptionTitle.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            height = height
                    + mTextChooseOperatorMarginTop
                    + mTextChooseTransportOptionTitle.getMeasuredHeight()
                    + mIconTransportOptionMarginTop
                    + mIconTransportOptionHeight;

            mChooseOperatorTitleBounds.right = width - mTextChooseOperatorMarginRight;
            mChooseOperatorTitleBounds.left = mChooseOperatorTitleBounds.right - mTextChooseTransportOptionTitle.getMeasuredWidth();
            mChooseOperatorTitleBounds.top = mTransportSwitchBounds.bottom + mTextChooseOperatorMarginTop;
            mChooseOperatorTitleBounds.bottom = mChooseOperatorTitleBounds.top + mTextChooseTransportOptionTitle.getMeasuredHeight();

            mDividerChooseTransportOption.setBounds(mChooseOperatorDividerMarginLeft, (int)(mChooseOperatorTitleBounds.centerY() - (mDividerChooseOperatorHeight / 2) - mDeltaY), width - mChooseOperatorDividerMarginRight - mTextChooseTransportOptionTitle.getMeasuredWidth() - mChooseOperatorDividerMarginRight, (int)(mChooseOperatorTitleBounds.centerY() + (mDividerChooseOperatorHeight / 2) - mDeltaY));

            int transportableOptionWidth = width / 3;
            int transportableOptionCenter = width - transportableOptionWidth / 2;

            for (int i = 0; i < mTransportableOperatorDrawables.length; ++i) {
                Drawable optionDrawable = mTransportableOperatorDrawables[i];
                if (optionDrawable != null) {
                    int optionDrawableWidth = optionDrawable.getIntrinsicWidth();

                    mTransportableOperatorDrawables[i].setBounds(transportableOptionCenter - optionDrawableWidth / 2,
                            mChooseOperatorTitleBounds.bottom + mIconTransportOptionMarginTop,
                            transportableOptionCenter + optionDrawableWidth / 2,
                            mChooseOperatorTitleBounds.bottom + mIconTransportOptionMarginTop + mIconTransportOptionHeight);

                }

                transportableOptionCenter -= transportableOptionWidth;
            }
        }

        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChild(mTextPhoneNumber, mPhoneTextBounds);
        layoutChild(mTextOperatorName, mOperatorNameBounds);
        layoutChild(mSwitchTransport, mTransportSwitchBounds);
        layoutChild(mTextSwitchTransportTitle, mTransportSwitchTitleBounds);
        layoutChild(mTextChooseTransportOptionTitle, mChooseOperatorTitleBounds);
    }

    private void layoutChild(View child, Rect bounds) {
        child.measure(MeasureSpec.makeMeasureSpec(bounds.width(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(bounds.height(), MeasureSpec.EXACTLY));
        child.layout(bounds.left, bounds.top, bounds.right, bounds.bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDrawableOperatorAnimation.draw(canvas);

        mDrawablePhone.draw(canvas);

        mDrawableOperator.draw(canvas);
        mDrawableCancelOperator.draw(canvas);

        mDividerChooseTransportOption.draw(canvas);

        for (int i = 0; i < mTransportableOperatorDrawables.length; ++i) {
            Drawable optionDrawable = mTransportableOperatorDrawables[i];
            if (optionDrawable != null) {
                optionDrawable.draw(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (boundsContainsPoint(mCancelOperatorIconBounds, event.getX(), event.getY())) {
            mListener.onCancelOperator();

            return true;
        }

        List<IOperator> transportableOperators = mOperator.getTransportableOperators();
        for (int i = 0; i < mTransportableOperatorDrawables.length; ++i) {
            Drawable optionDrawable = mTransportableOperatorDrawables[i];

            if (optionDrawable != null && boundsContainsPoint(optionDrawable.getBounds(), event.getX(), event.getY())) {
                mListener.onTransportableOperatorSelected(transportableOperators.get(i));

                return true;
            }
        }

        return super.onTouchEvent(event);
    }

    private boolean boundsContainsPoint(Rect bounds, float X, float Y) {
        int x = (int)X;
        int y = (int)Y;

        return bounds.contains(x, y);
    }

}
