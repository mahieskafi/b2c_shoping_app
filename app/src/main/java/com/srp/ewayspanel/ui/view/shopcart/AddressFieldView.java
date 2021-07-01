package com.srp.ewayspanel.ui.view.shopcart;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;

import ir.abmyapp.androidsdk.ABResources;
import ir.abmyapp.androidsdk.IABResources;

public class AddressFieldView extends ViewGroup {

    private int mEditValueHeight;
    private int mTitleMarginRight;
    private int mEditValueMarginTop;

    private Rect mTitleBounds = new Rect();
    private Rect mValueBounds = new Rect();

    private TextView mTextTitle;
    private EditText mEditTextValue;

    private String mDefaultError;

    public AddressFieldView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public AddressFieldView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public AddressFieldView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.addressfield, this, true);

        IABResources abResources = DI.getABResources();

        mDefaultError = abResources.getString(R.string.addressdialog_empty_error);

        mTextTitle = findViewById(R.id.text_title);
        mEditTextValue = findViewById(R.id.edit_value);

        Resources resources = getResources();

        int valueTextPaddingLeft = resources.getDimensionPixelSize(R.dimen.addressfield_edit_value_padding_left);
        int valueTextPaddingRight = resources.getDimensionPixelSize(R.dimen.addressfield_edit_value_padding_right);
        int valueTextPaddingTop = resources.getDimensionPixelSize(R.dimen.addressfield_edit_value_padding_top);

        mEditTextValue.setPadding(valueTextPaddingLeft, valueTextPaddingTop, valueTextPaddingRight, valueTextPaddingTop);

        mTitleMarginRight = resources.getDimensionPixelSize(R.dimen.addressfield_title_marginleft);
        mEditValueHeight = resources.getDimensionPixelSize(R.dimen.addressfield_editvalue_height);
        mEditValueMarginTop = resources.getDimensionPixelSize(R.dimen.addressfield_edit_value_margin_top);
    }

    public void setTitle(String title) {
        mTextTitle.setText(title);
    }

    public void setValue(String value) {
        mEditTextValue.setText(value);
    }

    public String getValue() {
        return String.valueOf(mEditTextValue.getText());
    }

    public void setError(String error) {
        if (error.isEmpty()) {
            mEditTextValue.setError(mDefaultError);
        } else {
            mEditTextValue.setError(error);
        }
    }

    public void setError() {
        mEditTextValue.setError(mDefaultError);
    }

    public void setData(String title, String value) {
        mTextTitle.setText(title);

        if (!TextUtils.isEmpty(value)) {
            mEditTextValue.setText(value);
        } else {
            mEditTextValue.setText("");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mTextTitle.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mTextTitle.getMeasuredHeight() + mEditValueHeight + mEditValueMarginTop, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getWidth();
        int height = getHeight();

        mTextTitle.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int titleWidth = mTextTitle.getMeasuredWidth();

        mTitleBounds.set(width - titleWidth - mTitleMarginRight, 0, width - mTitleMarginRight, mTextTitle.getMeasuredHeight());

        mValueBounds.set(0, mTextTitle.getMeasuredHeight() + mEditValueMarginTop, width, mTextTitle.getMeasuredHeight() + mEditValueHeight + mEditValueMarginTop);

        layoutChild(mTextTitle, mTitleBounds);
        layoutChild(mEditTextValue, mValueBounds);
    }

    private void layoutChild(View child, Rect bounds) {
        child.measure(MeasureSpec.makeMeasureSpec(bounds.width(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(bounds.height(), MeasureSpec.EXACTLY));
        child.layout(bounds.left, bounds.top, bounds.right, bounds.bottom);
    }

    public void setInputType(int inputType) {
        mEditTextValue.setInputType(inputType);
    }

    public void setMaxLength(int maxLength) {
        mEditTextValue.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    public void setMaxLines(int maxLines) {
        mEditTextValue.setMaxLines(maxLines);
        mEditTextValue.setLines(maxLines);
    }

    private void setValueTextGravity(int gravity) {
        mEditTextValue.setGravity(gravity);
    }

    public EditText getEditTextValue()
    {
        return mEditTextValue;
    }
}
