package com.srp.eways.ui.view.dialog;

import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.view.ViewUtils;
import com.srp.eways.ui.view.button.ButtonElement;

import ir.abmyapp.androidsdk.IABResources;

public class BottomDialog extends BottomSheetDialogFragment {


    public interface ConfirmationBottomDialogItemClickListener {
        void onConfirmClicked();

        void onCancelClicked();
    }


    private FrameLayout mContentContainer;
    private View mContentView;
    private ButtonElement mButtonConfirm;
    private AppCompatTextView mTitle;
    private AppCompatImageView mIcon;

    private String sTitle;
    private Drawable sIcon = null;

    private int mContentViewMarginLeft = 0;
    private int mContentViewMarginTop = 0;
    private int mContentViewMarginRight = 0;
    private int mContentViewMarginBottom = 0;

    private FrameLayout mRootView;

    private ConfirmationBottomDialogItemClickListener mListener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        IABResources AB = DIMain.getABResources();
        mRootView = view.findViewById(R.id.root_view);

        mContentContainer = view.findViewById(R.id.content_container);
        mButtonConfirm = view.findViewById(R.id.b_confirm);
        mTitle = view.findViewById(R.id.dilog_title);
        mIcon = view.findViewById(R.id.iv_icon_dialog);

        mIcon.setVisibility(View.GONE);

        mTitle.setText(sTitle);
        if (sIcon != null) {
            mIcon.setVisibility(View.VISIBLE);
            mIcon.setImageDrawable(sIcon);
        }

        FrameLayout.LayoutParams contentViewLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentViewLayoutParams.setMargins(mContentViewMarginLeft, mContentViewMarginTop, mContentViewMarginRight, mContentViewMarginBottom);


        if (mContentContainer != null && mContentView != null) {
            mContentContainer.removeAllViews();
            mContentContainer.addView(mContentView);
            mContentContainer.setLayoutParams(contentViewLayoutParams);
        }

        setupConfirmButton(AB);

        ViewTreeObserver vto = mRootView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    mRootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }


                DisplayMetrics displayMetrics = ViewUtils.getDisplayMetrics(getContext());
                int height = displayMetrics.heightPixels;
                int height1 = (int) (height * 0.59);

                mRootView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                int height2 = mRootView.getMeasuredHeight();

                int finalheight = Math.min(height1, height2);
                mRootView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, finalheight));


//                Toast.makeText(getContext(),"1="+height1 + "2= "+height2,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_dialog, container, false);

        if (mContentContainer != null) {
            mContentContainer.removeAllViews();
        }

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }


    public void setChildContentView(View contentView) {
        mContentView = contentView;

        if (mContentContainer != null) {
            mContentContainer.removeAllViews();
            mContentContainer.addView(contentView);
        }
    }


    private void setupConfirmButton(IABResources AB) {

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
        mButtonConfirm.setEnable(true);
        mButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onConfirmClicked();
                }
            }
        });
    }

    public void setListener(ConfirmationBottomDialogItemClickListener mListener) {
        this.mListener = mListener;
    }

    public void setButtonEnable(boolean enabled) {
        if (mButtonConfirm != null) {
            mButtonConfirm.setEnable(enabled);
        }
    }


    public void setIcon(Drawable icon) {
        sIcon = icon;
    }

    public void setTitle(String title) {
        sTitle = title;
    }

    public void setContentViewMargin(int marginLeft, int marginTop, int marginRight, int marginBottom) {

        mContentViewMarginLeft = marginLeft;
        mContentViewMarginTop = marginTop;
        mContentViewMarginRight = marginRight;
        mContentViewMarginBottom = marginBottom;
    }
}
