package com.srp.eways.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.view.ViewUtils;
import com.srp.eways.ui.view.button.ButtonElement;

import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by Eskafi on 10/2/2019.
 */
public class ConfirmationDialog extends Dialog {

    public interface ConfirmationDialogItemClickListener {
        void onConfirmClicked();

        void onCancelClicked();
    }

    private AppCompatImageView mImageCancelDialog;
    private AppCompatTextView mTitleDialog;
    private ButtonElement mButtonConfirm;
    private FrameLayout mContentContainer;
    private View mContentView;
    private CardView mRootView;

    private FrameLayout.LayoutParams mRootViewLayoutParams;
    private FrameLayout.LayoutParams mContentViewLayoutParams;

    private ConfirmationDialogItemClickListener mListener;

    public ConfirmationDialog(@NonNull Context context) {
        super(context);
    }

    public void setChildContentView(View contentView) {
        mContentView = contentView;

        if (mContentContainer != null) {
            mContentContainer.removeAllViews();
            mContentContainer.addView(contentView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);

        IABResources AB = DIMain.getABResources();

        mRootView = findViewById(R.id.root_view);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mImageCancelDialog = findViewById(R.id.iv_cancel_dialog);
        mButtonConfirm = findViewById(R.id.b_confirm);
        mContentContainer = findViewById(R.id.content_container);
        mTitleDialog = findViewById(R.id.title);
        mTitleDialog.setVisibility(View.GONE);

        mContentContainer.removeAllViews();
        mContentContainer.addView(mContentView);

        mContentContainer.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        int maxHeight = (int) (ViewUtils.getDisplayMetrics(getContext()).heightPixels * 0.8f);

        getWindow().setGravity(Gravity.CENTER);

        mContentViewLayoutParams = (FrameLayout.LayoutParams) mContentContainer.getLayoutParams();
        mContentViewLayoutParams.rightMargin = AB.getDimenPixelSize(R.dimen.dialog_content_margin_left_right);
        mContentViewLayoutParams.leftMargin = AB.getDimenPixelSize(R.dimen.dialog_content_margin_left_right);
        mContentViewLayoutParams.topMargin = AB.getDimenPixelSize(R.dimen.dialog_content_margin_top_bottom);

        mContentContainer.setLayoutParams(mContentViewLayoutParams);

        setupConfirmButton(AB);
        setupCancelButton(AB);

        mRootViewLayoutParams = (FrameLayout.LayoutParams) mRootView.getLayoutParams();

        if (mContentContainer.getMeasuredHeight() > maxHeight) {
            mRootViewLayoutParams.height = maxHeight;
        }
    }

    public void isMatchWidth(boolean isMatchWidth) {
        int minWidth;

        if (isMatchWidth) {
            minWidth = (int) (ViewUtils.getDisplayMetrics(getContext()).widthPixels * 0.8);
        } else {
            minWidth = (int) ((ViewUtils.getDisplayMetrics(getContext()).widthPixels * 0.65));
        }

        getWindow().getAttributes().width = minWidth;
        mRootViewLayoutParams.width = getWindow().getAttributes().width;

        if (mContentView.getMeasuredWidth() > minWidth) {
            int dialogWidth = Math.min(mContentView.getMeasuredWidth(), ViewUtils.getDisplayMetrics(getContext()).widthPixels - (mContentViewLayoutParams.rightMargin + mContentViewLayoutParams.leftMargin));

            getWindow().getAttributes().width = dialogWidth + mContentViewLayoutParams.rightMargin + mContentViewLayoutParams.leftMargin;
            mRootViewLayoutParams.width = dialogWidth + mContentViewLayoutParams.rightMargin + mContentViewLayoutParams.leftMargin;
        }

        mRootView.setLayoutParams(mRootViewLayoutParams);
    }

    private void setupConfirmButton(IABResources AB) {

        LinearLayout.LayoutParams buttonConfirmLayoutParams = (LinearLayout.LayoutParams) mButtonConfirm.getLayoutParams();
        buttonConfirmLayoutParams.height = AB.getDimenPixelSize(R.dimen.buttonelement_default_height);
        buttonConfirmLayoutParams.rightMargin = AB.getDimenPixelSize(R.dimen.confirm_button_dialog_margin);
        buttonConfirmLayoutParams.leftMargin = AB.getDimenPixelSize(R.dimen.confirm_button_dialog_margin);
        buttonConfirmLayoutParams.bottomMargin = AB.getDimenPixelSize(R.dimen.confirm_button_dialog_margin);
        buttonConfirmLayoutParams.topMargin = AB.getDimenPixelSize(R.dimen.dialog_content_margin_top_bottom);

        mButtonConfirm.setLayoutParams(buttonConfirmLayoutParams);

        mButtonConfirm.setTextColor(AB.getColor(R.color.confirm_button_dialog_text_color));
        mButtonConfirm.setTextSize(AB.getDimenPixelSize(R.dimen.confirm_button_dialog_text_size));
        mButtonConfirm.setEnabledBackground(AB.getDrawable(R.drawable.login_button_background_enabled));
        mButtonConfirm.setDisableBackground(AB.getDrawable(R.drawable.login_button_background_disabled));
        mButtonConfirm.setText(AB.getString(R.string.confirm_button_dialog_text));

        mButtonConfirm.setLoadingVisibility(View.GONE);

        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan_light);
        if (typeface != null) {
            mButtonConfirm.setTextTypeFace(typeface);
        }
        mButtonConfirm.setEnable(false);
        mButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onConfirmClicked();
                }
            }
        });
    }

    private void setupCancelButton(IABResources AB) {

        LinearLayout.LayoutParams buttonCancelLayoutParams = (LinearLayout.LayoutParams) mImageCancelDialog.getLayoutParams();
        buttonCancelLayoutParams.height = AB.getDimenPixelSize(R.dimen.cancel_button_height);
        buttonCancelLayoutParams.width = AB.getDimenPixelSize(R.dimen.cancel_button_height);
        buttonCancelLayoutParams.rightMargin = AB.getDimenPixelSize(R.dimen.cancel_button_dialog_margin);
        buttonCancelLayoutParams.leftMargin = AB.getDimenPixelSize(R.dimen.cancel_button_dialog_margin);
        buttonCancelLayoutParams.topMargin = AB.getDimenPixelSize(R.dimen.cancel_button_dialog_margin);

        mImageCancelDialog.setLayoutParams(buttonCancelLayoutParams);

        mImageCancelDialog.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mImageCancelDialog.setImageDrawable(AB.getDrawable(R.drawable.ic_cancel_dialog));

        mImageCancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mListener != null) {
                    mListener.onCancelClicked();
                }
            }
        });
    }

    public void setListener(ConfirmationDialogItemClickListener mListener) {
        this.mListener = mListener;
    }

    public void setButtonEnable(boolean enabled) {
        if (mButtonConfirm != null) {
            mButtonConfirm.setEnable(enabled);
        }
    }

    public void hasButton(boolean hasButton) {

        if (hasButton) {
            mButtonConfirm.setVisibility(View.VISIBLE);
        } else {
            mButtonConfirm.setVisibility(View.GONE);
        }
    }

    public void setDialogTitle(String title,
                               Typeface typeface,
                               @DimenRes int fontSize,
                               @ColorRes int textColor) {

        IABResources abResources = DIMain.getABResources();

        mTitleDialog.setTypeface(typeface != null ? typeface : ResourcesCompat.getFont(getContext(), R.font.iran_yekan));
        mTitleDialog.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize != 0 ? fontSize : abResources.getDimenPixelSize(R.dimen.confirm_button_dialog_title_text_size));
        mTitleDialog.setTextColor(textColor != 0 ? abResources.getColor(textColor) : abResources.getColor(R.color.confirm_button_dialog_title_text_color));

        mTitleDialog.setText(title);

        LinearLayout.LayoutParams titleLayoutParams = (LinearLayout.LayoutParams) mTitleDialog.getLayoutParams();
        titleLayoutParams.rightMargin = abResources.getDimenPixelSize(R.dimen.confirm_button_dialog_margin);
        titleLayoutParams.topMargin = abResources.getDimenPixelSize(R.dimen.confirm_button_dialog_margin);

        mTitleDialog.setLayoutParams(titleLayoutParams);

        mTitleDialog.setVisibility(View.VISIBLE);
    }

    public void setDialogTitle(String title) {
        setDialogTitle(title, null, 0, 0);
    }

    public void setCustomContentMargin(int topMargin, int leftMargin, int rightMargin, int bottomMargin) {
        mContentViewLayoutParams = (FrameLayout.LayoutParams) mContentContainer.getLayoutParams();
        mContentViewLayoutParams.rightMargin = rightMargin;
        mContentViewLayoutParams.leftMargin = leftMargin;
        mContentViewLayoutParams.topMargin = topMargin;
        mContentViewLayoutParams.bottomMargin = bottomMargin;

        mContentContainer.setLayoutParams(mContentViewLayoutParams);
    }
}