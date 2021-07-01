package com.srp.eways.util;

import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class CustomHintTypeFaceTextWatcher implements TextWatcher {

    private Typeface mTypeface;
    private Typeface mHintTypeface;

    private EditText mEditText;

    public CustomHintTypeFaceTextWatcher(EditText editText, Typeface typeface, Typeface hintTypeface) {
        mTypeface = typeface;
        mHintTypeface = hintTypeface;

        mEditText = editText;
        mEditText.setTypeface(mHintTypeface);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() == 0) {
            mEditText.setTypeface(mHintTypeface);
        } else {
            mEditText.setTypeface(mTypeface);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
