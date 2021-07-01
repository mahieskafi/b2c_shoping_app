package com.srp.ewayspanel.ui.view.toolbar;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.yashoid.sequencelayout.SequenceLayout;

import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by Eskafi on 11/10/2019.
 */
public class FilterToolbar extends SequenceLayout {

    public interface FilterToolbarClickListener {
        void onExitClicked();

        void onCancelAllClicked();
    }

    private AppCompatImageView mIconExit;
    private AppCompatTextView mTextCancelAll;
    private AppCompatTextView mTextTitle;

    private FilterToolbarClickListener mListener;

    public FilterToolbar(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public FilterToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);

    }

    public FilterToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);

    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {

        IABResources abResources = DI.getABResources();

        LayoutInflater.from(context).inflate(R.layout.view_toolbar_filter, this, true);

        int iconPadding = abResources.getDimenPixelSize(R.dimen.filter_toolbar_icon_padding);
        int textCancelAllSidePadding = abResources.getDimenPixelSize(R.dimen.filter_toolbar_cancel_all_text_padding_side);
        int textCancelAllTopBottomPadding = abResources.getDimenPixelSize(R.dimen.filter_toolbar_cancel_all_text_padding_top_bottom);

        Typeface titleTypeface = ResourcesCompat.getFont(context, R.font.iran_yekan_bold);
        Typeface cancelAllTypeFace = ResourcesCompat.getFont(context, R.font.iran_yekan);

        LayoutParams layoutParams = new LayoutParams(context, attrs);
        layoutParams.height = abResources.getDimenPixelSize(R.dimen.filter_toolbar_height);
        setLayoutParams(layoutParams);

        setBackgroundColor(abResources.getColor(R.color.filter_toolbar_back_color));

        mIconExit = findViewById(R.id.image_exit_icon);
        mTextCancelAll = findViewById(R.id.text_cancel_filter);
        mTextTitle = findViewById(R.id.text_title);

        addSequences(R.xml.sequences_filter_toolbar);

        mIconExit.setImageDrawable(abResources.getDrawable(R.drawable.ic_cancel_dialog));
        mIconExit.setPadding(iconPadding, iconPadding, iconPadding, iconPadding);

        mIconExit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onExitClicked();
            }
        });

        mTextCancelAll.setTypeface(cancelAllTypeFace);
        mTextCancelAll.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.filter_toolbar_cancel_all_text_size));
        mTextCancelAll.setTextColor(abResources.getColor(R.color.filter_toolbar_cancel_all_title_text_color));
        mTextCancelAll.setText(abResources.getString(R.string.filter_toolbar_cancel_all_text));
        mTextCancelAll.setBackground(abResources.getDrawable(R.drawable.button_cancel_all_filter_background));
        mTextCancelAll.setPadding(textCancelAllSidePadding, textCancelAllTopBottomPadding, textCancelAllSidePadding, textCancelAllTopBottomPadding);
        mTextCancelAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelAllClicked();
            }
        });


        mTextTitle.setTextColor(abResources.getColor(R.color.filter_toolbar_title_text_color));
        mTextTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.filter_toolbar_title_text_size));
        mTextTitle.setTypeface(titleTypeface);
        mTextTitle.setText(abResources.getString(R.string.filter_toolbar_title));

    }

    public void setListener(FilterToolbarClickListener listener) {
        mListener = listener;
    }
}
