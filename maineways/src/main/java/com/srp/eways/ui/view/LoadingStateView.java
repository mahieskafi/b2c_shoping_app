package com.srp.eways.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.util.Constants;
import com.srp.eways.util.Utils;

import ir.abmyapp.androidsdk.IABResources;

public class LoadingStateView extends LinearLayout {

    public interface RetryListener {

        void onRetryButtonClicked();

    }

    public static final int STATE_LOADING = 0;
    public static final int STATE_ERROR = 1;

    private LinearLayout mRootView;
    private FrameLayout mLoadingContainer;
    private ImageView mProgressIcon;

    private TextView mTextStateDescription;
    private AppCompatTextView mTextStateDescriptionHorizontal;

    private AppCompatTextView mTextTimer;
    private RotateAnimation mRotateAnimation;

    private TextView mButtonRetry;

    private int mRetryButtonMarginTop;

    int mLoadingContainerVerticalModeWidth;
    int mLoadingContainerVerticalModeHeight;
    int mLoadingContainerHorizontalModeWidth;
    int mLoadingContainerHorizontalModeHeight;

    String mTextRetry;
    String mTextWeakInternetSpeed;
    String mTextErrorWeakInternetSpeed;

    CountDownTimer mCountDownTimer;

    private boolean mShowRetryInErrorState = true;

    private RetryListener mRetryListener;

    public LoadingStateView(@NonNull Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public LoadingStateView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public LoadingStateView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.loading_state, this, true);

        mRootView = findViewById(R.id.root_view);
        mTextStateDescription = findViewById(R.id.text_description);
        mTextStateDescriptionHorizontal = findViewById(R.id.text_statedescription);
        mLoadingContainer = findViewById(R.id.container_loading);
        mTextTimer = findViewById(R.id.text_timer);
        mButtonRetry = findViewById(R.id.button_retry);
        mProgressIcon = findViewById(R.id.progress_circular);

        mRotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setRepeatCount(RotateAnimation.INFINITE);
        mRotateAnimation.setRepeatMode(RotateAnimation.RESTART);
        mRotateAnimation.setDuration(3000);
        mRotateAnimation.setInterpolator(new LinearInterpolator());

        final IABResources abResources = DIMain.getABResources();

        mLoadingContainerVerticalModeWidth = abResources.getDimenPixelSize(R.dimen.loadingstateview_icon_vertical_mode_width);
        mLoadingContainerVerticalModeHeight = abResources.getDimenPixelSize(R.dimen.loadingstateview_icon_vertical_mode_height);

        mLoadingContainerHorizontalModeWidth = abResources.getDimenPixelSize(R.dimen.loadingstateview_icon_horizontal_mode_width);
        mLoadingContainerHorizontalModeHeight = abResources.getDimenPixelSize(R.dimen.loadingstateview_icon_horizontal_mode_height);

        setLoadingContainerLP();

        mTextRetry = " \n" + abResources.getString(R.string.loadingstateview_loading_error_text);
        mTextWeakInternetSpeed = abResources.getString(R.string.loadingstateview_loading_internetspeed_text);
        mTextErrorWeakInternetSpeed = abResources.getString(R.string.loadingstateview_loading_connectiontimeout_error_text);

        int descriptionTextColor = abResources.getColor(R.color.loadingstateview_textcolor);
        Typeface descriptionTextFont = ResourcesCompat.getFont(context, R.font.iran_yekan);

        mTextStateDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.loadingstateview_textsize));
        mTextStateDescription.setTextColor(descriptionTextColor);
        mTextStateDescription.setTypeface(descriptionTextFont);

        mTextStateDescriptionHorizontal.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.loadingstateview_textsize_horizontal));
        mTextStateDescriptionHorizontal.setTextColor(descriptionTextColor);
        mTextStateDescriptionHorizontal.setTypeface(descriptionTextFont);

        float retryButtonTextSize = abResources.getDimen(R.dimen.loadingstateview_retry_textsize);
        int retryButtonTextColor = abResources.getColor(R.color.loadingstateview_retry_textcolor);
        Typeface retryButtonTextFont = ResourcesCompat.getFont(context, R.font.iran_yekan);

        mRetryButtonMarginTop = abResources.getDimenPixelSize(R.dimen.loadingstateview_retry_margintop);

        mButtonRetry.setTextSize(TypedValue.COMPLEX_UNIT_PX, retryButtonTextSize);
        mButtonRetry.setTextColor(retryButtonTextColor);
        mButtonRetry.setTypeface(retryButtonTextFont);

        mButtonRetry.setText(abResources.getString(R.string.loadingstateview_retry_text));

        GradientDrawable retryButtonBackground = new GradientDrawable();
        retryButtonBackground.setColor(abResources.getColor(R.color.loadingstateview_retry_background));
        retryButtonBackground.setCornerRadius(abResources.getDimen(R.dimen.loadingstateview_retry_background_radius));
        mButtonRetry.setBackground(retryButtonBackground);

        mButtonRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRetryListener != null) {
                    setStateAndDescription(STATE_LOADING, abResources.getString(R.string.loading_message), true);
                    mRetryListener.onRetryButtonClicked();
                }
            }
        });

        mTextTimer.setTextColor(retryButtonTextColor);
        mTextTimer.setTypeface(retryButtonTextFont);

        setViewOrientation(VERTICAL);
        setTimer();
    }

    public void setRetryListener(RetryListener listener) {
        mRetryListener = listener;
    }

    public void setStateAndDescription(int state, String description, boolean showRetryInErrorState) {

        switch (state) {
            case STATE_LOADING:
                mProgressIcon.startAnimation(mRotateAnimation);

                if (mRootView.getOrientation() == VERTICAL) {
                    mButtonRetry.setVisibility(GONE);
                    mLoadingContainer.setVisibility(VISIBLE);

                    mTextStateDescription.setVisibility(VISIBLE);
                    mTextStateDescriptionHorizontal.setVisibility(GONE);

                    mTextStateDescription.setText(description);
                } else {
                    mButtonRetry.setVisibility(GONE);
                    mLoadingContainer.setVisibility(VISIBLE);

                    mTextStateDescription.setVisibility(GONE);
                    mTextStateDescriptionHorizontal.setVisibility(VISIBLE);

                    mTextStateDescriptionHorizontal.setText(description);
                }
                mTextTimer.setVisibility(GONE);
                mCountDownTimer.start();
                break;

            case STATE_ERROR:
                String errorDescription = description + mTextRetry;

                mLoadingContainer.setVisibility(GONE);

                if (mCountDownTimer != null) {
                    mCountDownTimer.cancel();
                }

                if (showRetryInErrorState) {
                    mButtonRetry.setVisibility(VISIBLE);
                } else {
                    mButtonRetry.setVisibility(GONE);
                }

                if (mRootView.getOrientation() == VERTICAL) {
                    mTextStateDescription.setVisibility(VISIBLE);
                    mTextStateDescriptionHorizontal.setVisibility(GONE);

                    mTextStateDescription.setText(errorDescription);
                } else {
                    mTextStateDescription.setVisibility(GONE);
                    mTextStateDescriptionHorizontal.setVisibility(VISIBLE);

                    mTextStateDescriptionHorizontal.setText(errorDescription);
                }

                break;
        }
    }

    private void setLoadingContainerLP() {
        IABResources abResources = DIMain.getABResources();

        MarginLayoutParams loadingContainerLP = (MarginLayoutParams) mLoadingContainer.getLayoutParams();
        LayoutParams retryButtonLP = (LayoutParams) mButtonRetry.getLayoutParams();

        retryButtonLP.height = abResources.getDimenPixelSize(R.dimen.loadingstateview_retry_height);
        retryButtonLP.width = abResources.getDimenPixelSize(R.dimen.loadingstateview_retry_width);

        if (mRootView.getOrientation() == VERTICAL) {
            loadingContainerLP.width = mLoadingContainerVerticalModeWidth;
            loadingContainerLP.height = mLoadingContainerVerticalModeHeight;
            loadingContainerLP.setMargins(0, 0, 0, abResources.getDimenPixelSize(R.dimen.loadingstateview_contentpadding_bottom));

            retryButtonLP.topMargin = mRetryButtonMarginTop;

            mTextTimer.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.loadingstateview_timer_textsize));
        } else {
            loadingContainerLP.width = mLoadingContainerHorizontalModeWidth;
            loadingContainerLP.height = mLoadingContainerHorizontalModeHeight;
            loadingContainerLP.setMargins(0, 0, 0, 0);

            retryButtonLP.topMargin = 0;

            mTextTimer.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.loadingstateview_timer_horizontal_textsize));
        }

        mLoadingContainer.setLayoutParams(loadingContainerLP);
        mButtonRetry.setLayoutParams(retryButtonLP);

    }

    public void setViewOrientation(int orientation) {
        mRootView.setOrientation(orientation);
        setLoadingContainerLP();

    }

    public void setButtonText(String text) {
        mButtonRetry.setText(text);
    }

    public void setTextColor(int color) {
        mTextStateDescription.setTextColor(color);
        mTextStateDescriptionHorizontal.setTextColor(color);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);


        if (visibility == GONE || visibility == INVISIBLE) {
            if (mCountDownTimer != null) {
                mCountDownTimer.cancel();
            }
            if(mRotateAnimation!= null) {
                mRotateAnimation.cancel();
            }

        } else {
            mProgressIcon.startAnimation(mRotateAnimation);
        }
    }

    public void setTimer() {

        final long time = Constants.CONNECTION_TIME_OUT * 1000;
        mTextTimer.setVisibility(GONE);

        mCountDownTimer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                if ((time - millisUntilFinished) / 1000 >= 5) {
                    mTextTimer.setVisibility(VISIBLE);
                    mTextTimer.setText(Utils.toPersianNumber(millisUntilFinished / 1000));

                    if (mTextStateDescription.getVisibility() == VISIBLE) {
                        mTextStateDescription.setText(mTextWeakInternetSpeed);
                    } else {
                        mTextStateDescriptionHorizontal.setText(mTextWeakInternetSpeed);
                    }

                }
            }

            @Override
            public void onFinish() {
                setStateAndDescription(STATE_ERROR, mTextErrorWeakInternetSpeed, true);
            }
        };
    }

    @Override
    protected void onDetachedFromWindow() {
        mCountDownTimer.cancel();

        super.onDetachedFromWindow();

    }
}
