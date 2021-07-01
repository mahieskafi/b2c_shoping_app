package com.srp.ewayspanel.ui.view.textview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;

import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by Eskafi on 12/9/2019.
 */
public class KeyValueTextView extends RelativeLayout {

    private AppCompatTextView mKeyTextView;
    private AppCompatTextView mValueTextView;

    public KeyValueTextView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public KeyValueTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public KeyValueTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {

        IABResources abResources = DI.getABResources();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.key_value_text_view, this);

        Typeface textTypeFace = ResourcesCompat.getFont(context, R.font.iran_yekan);
        int textSize = abResources.getDimenPixelSize(R.dimen.key_value_text_view_default_text_size);
        int textColor = abResources.getColor(R.color.key_value_text_view_default_text_color);

        mKeyTextView = findViewById(R.id.key_text);
        mValueTextView = findViewById(R.id.value_text);

        mKeyTextView.setTypeface(textTypeFace);
        mValueTextView.setTypeface(textTypeFace);

        mKeyTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mValueTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

        mKeyTextView.setTextColor(textColor);
        mValueTextView.setTextColor(textColor);

        mValueTextView.setLineSpacing(0,1.5f);

    }

    public void setTextColor(int color) {
        mKeyTextView.setTextColor(color);
        mValueTextView.setTextColor(color);
    }

    public void setKeyTextColor(int color) {
        mKeyTextView.setTextColor(color);
    }

    public void setValueTextColor(int color) {
        mValueTextView.setTextColor(color);
    }

    public void setTextSize(int textSize) {
        mKeyTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mValueTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    public void setKeyTextSize(int textSize) {
        mKeyTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    public void setValueTextSize(int textSize) {
        mValueTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    public void setTypeface(Typeface typeface) {
        mKeyTextView.setTypeface(typeface);
        mValueTextView.setTypeface(typeface);
    }

    public void setKeyTextTypeface(Typeface typeface) {
        mKeyTextView.setTypeface(typeface);
    }

    public void setValueTextTypeface(Typeface typeface) {
        mValueTextView.setTypeface(typeface);
    }

    public void setKeyText(String text) {
        mKeyTextView.setText(text);
    }

    public void setValueText(String text) {
        mValueTextView.setText(text);
    }
}
