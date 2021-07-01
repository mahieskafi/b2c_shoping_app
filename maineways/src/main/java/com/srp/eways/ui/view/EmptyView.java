package com.srp.eways.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.cardview.widget.CardView;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;

import ir.abmyapp.androidsdk.ABResources;
import ir.abmyapp.androidsdk.IABResources;

public class EmptyView extends CardView {

    private TextView mTextEmpty;
    private ImageView mImageEmpty;

    public EmptyView(@NonNull Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.emptyview, this, true);

        IABResources abResources = ABResources.get(context);

//        setRadius(abResources.getDimen(R.dimen.emptyview_cardradius));
        setCardElevation(abResources.getDimen(R.dimen.emptyview_cardelevation));
        setBackgroundColor(abResources.getColor(R.color.background));


        int contentPaddingLeft = abResources.getDimenPixelSize(R.dimen.emptyview_contentpadding);

        setContentPadding(contentPaddingLeft, contentPaddingLeft, contentPaddingLeft, contentPaddingLeft);

        mImageEmpty = findViewById(R.id.image_empty);
        mTextEmpty = findViewById(R.id.text_empty);

        int textMarginTop = abResources.getDimenPixelSize(R.dimen.emptyview_text_margintop);
        LinearLayout.LayoutParams textLP = (LinearLayout.LayoutParams) mTextEmpty.getLayoutParams();
        textLP.topMargin = textMarginTop;
        mTextEmpty.setLayoutParams(textLP);

        mTextEmpty.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.emptyview_text_size));
        mTextEmpty.setTextColor(abResources.getColor(R.color.emptyview_textcolor));
        mTextEmpty.setText(abResources.getString(R.string.emptyview_text));

        LinearLayout.LayoutParams imageLP = (LinearLayout.LayoutParams) mImageEmpty.getLayoutParams();
        imageLP.width = abResources.getDimenPixelSize(R.dimen.emptyview_image_width);
        imageLP.height = abResources.getDimenPixelSize(R.dimen.emptyview_image_height);
        mImageEmpty.setLayoutParams(imageLP);

        mImageEmpty.setImageDrawable(abResources.getDrawable(R.drawable.ic_state_empty_2));
    }

    public void setEmptyText(@StringRes int textResourceId) {
        mTextEmpty.setText(DIMain.getABResources().getString(textResourceId));
    }

    public void setImageResource(@DrawableRes int imageResourceId) {
        mImageEmpty.setImageDrawable(DIMain.getABResources().getDrawable(imageResourceId));
    }

}
