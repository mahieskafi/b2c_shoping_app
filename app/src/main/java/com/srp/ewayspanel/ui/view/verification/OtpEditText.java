package com.srp.ewayspanel.ui.view.verification;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.view.ViewCompat;


import com.srp.eways.util.Utils;
import com.srp.ewayspanel.R;

public class OtpEditText extends AppCompatEditText {
    private static final String XML_NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android";

    public interface OnPinEnteredListener {
        void onPinEntered(CharSequence str);
    }

    private float mSpace = 24;
    private float mCharSize;
    private float mNumChars = 4;
    private float mTextBottomPadding = 8;
    private int mMaxLength = 4;

    private RectF[] mLineCoords;
    private float[] mCharBottom;
    private Rect mTextHeight = new Rect();

    private int defaultColor;
    private boolean mHasError = false;

    private OnClickListener mClickListener;
    private OnPinEnteredListener mPinEnteredListener = null;

    private float mLineStroke = 1;
    private Paint mLinesPaint;
    private Paint mCharPaint;
    private Paint mLastCharPaint;

    private ColorStateList mOriginalTextColors;
    private int[] mColors;
    private ColorStateList mColorStates;

    private int[][] mStates = new int[][]{
            new int[]{android.R.attr.state_selected}, // selected
            new int[]{android.R.attr.state_active}, // error
            new int[]{android.R.attr.state_focused}, // focused
            new int[]{-android.R.attr.state_focused}, // unfocused
    };

    public OtpEditText(Context context) {
        super(context);
    }

    public OtpEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public OtpEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setMaxLength(final int maxLength) {
        mMaxLength = maxLength;
        mNumChars = maxLength;

        setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        setText(null);
        invalidate();
    }

    private void init(Context context, AttributeSet attrs) {
        float multi = context.getResources().getDisplayMetrics().density;

        mLineStroke = multi * mLineStroke;
        mSpace = multi * mSpace; //convert to pixels for our density
        mTextBottomPadding = multi * mTextBottomPadding; //convert to pixels for our density

        defaultColor = Color.GRAY;
        mColors = new int[]{defaultColor, Color.RED, defaultColor, defaultColor};
        mColorStates = new ColorStateList(mStates, mColors);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.OtpEditText, 0, 0);
        try {
            mLineStroke = ta.getDimension(R.styleable.OtpEditText_pinLineStroke, mLineStroke);
            mSpace = ta.getDimension(R.styleable.OtpEditText_pinCharacterSpacing, mSpace);
            mTextBottomPadding = ta.getDimension(R.styleable.OtpEditText_pinTextBottomPadding, mTextBottomPadding);
            defaultColor = ta.getColor(R.styleable.OtpEditText_pinLineColors, defaultColor);
        } finally {
            ta.recycle();
        }

        mCharPaint = new Paint(getPaint());
        mLastCharPaint = new Paint(getPaint());
        mLinesPaint = new Paint(getPaint());
        mLinesPaint.setStrokeWidth(mLineStroke);

        mColors[0] = defaultColor;
        mColors[1] = Color.RED;
        mColors[2] = defaultColor;//focusColor
        mColors[3] = defaultColor;//colorUnfocused

        setBackgroundResource(0);

        mMaxLength = attrs.getAttributeIntValue(XML_NAMESPACE_ANDROID, "maxLength", 4);
        mNumChars = mMaxLength;

        setImeOptions(EditorInfo.IME_ACTION_DONE);

        super.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });

        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelection(getText().length());

                if (mClickListener != null) {
                    mClickListener.onClick(v);
                }
            }
        });

        getPaint().getTextBounds("|", 0, 1, mTextHeight);
    }

    @Override
    public void setInputType(int type) {
        super.setInputType(type);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mOriginalTextColors = getTextColors();

        if (mOriginalTextColors != null) {
            mLastCharPaint.setColor(mOriginalTextColors.getDefaultColor());
            mCharPaint.setColor(mOriginalTextColors.getDefaultColor());
        }

        int availableWidth = getWidth() - ViewCompat.getPaddingEnd(this) - ViewCompat.getPaddingStart(this);

        if (mSpace < 0) {
            mCharSize = (availableWidth / (mNumChars * 2 - 1));
        } else {
            mCharSize = (availableWidth - (mSpace * (mNumChars - 1))) / mNumChars;
        }

        mLineCoords = new RectF[(int) mNumChars];
        mCharBottom = new float[(int) mNumChars];

        int startX;
        int bottom = getHeight() - getPaddingBottom();
        startX = ViewCompat.getPaddingStart(this);

        for (int i = 0; i < mNumChars; i++) {
            mLineCoords[i] = new RectF(startX, bottom, startX + mCharSize, bottom);

            if (mSpace < 0) {
                startX += mCharSize * 2;
            } else {
                startX += (mCharSize + mSpace);
            }
            mCharBottom[i] = mLineCoords[i].bottom - mTextBottomPadding;
        }
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        mClickListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        CharSequence text = Utils.toPersianNumber(String.valueOf(getFullText()));
        int textLength = text.length();

        float[] textWidths = new float[textLength];
        getPaint().getTextWidths(text, 0, textLength, textWidths);

        for (int i = 0; i < mNumChars; i++) {
            float middle = mLineCoords[i].left + mCharSize / 2;

            if (textLength > i) {
                if (i != textLength - 1) {
                    canvas.drawText(text, i, i + 1, middle - textWidths[i] / 2, mCharBottom[i], mCharPaint);
                } else {
                    canvas.drawText(text, i, i + 1, middle - textWidths[i] / 2, mCharBottom[i], mLastCharPaint);
                }
            }

            updateColorForLines(i <= textLength);
            canvas.drawLine(mLineCoords[i].left, mLineCoords[i].top, mLineCoords[i].right, mLineCoords[i].bottom, mLinesPaint);

        }
    }

    private CharSequence getFullText() {
        return getText();
    }

    private int getColorForState(int... states) {
        return mColorStates.getColorForState(states, defaultColor);
    }

    public void setColorForState(int... states) {
        mColors = new int[]{states[0], states[1], states[2], states[3]};
        mColorStates = new ColorStateList(mStates, mColors);

        invalidate();
    }

    private void updateColorForLines(boolean hasTextOrIsNext) {
        if (mHasError) {
            mLinesPaint.setColor(getColorForState(android.R.attr.state_active));
        } else if (isFocused()) {
            mLinesPaint.setColor(getColorForState(android.R.attr.state_focused));
            if (hasTextOrIsNext) {
                mLinesPaint.setColor(getColorForState(android.R.attr.state_selected));
            }
        } else {
            mLinesPaint.setColor(getColorForState(-android.R.attr.state_focused));
        }
    }

    public void setError(boolean hasError) {
        mHasError = hasError;
        invalidate();
    }

    public boolean isError() {
        return mHasError;
    }

    public void focus() {
        requestFocus();

        // Show keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(this, 0);
    }

    @Override
    public void setTypeface(@Nullable Typeface tf) {
        super.setTypeface(tf);
        setCustomTypeface(tf);
    }

    @Override
    public void setTypeface(@Nullable Typeface tf, int style) {
        super.setTypeface(tf, style);
        setCustomTypeface(tf);
    }

    private void setCustomTypeface(@Nullable Typeface tf) {
        if (mCharPaint != null) {
            mCharPaint.setTypeface(tf);
            mLastCharPaint.setTypeface(tf);
            mLinesPaint.setTypeface(tf);
        }
    }

    public void setPinLineColors(ColorStateList colors) {
        mColorStates = colors;
        invalidate();
    }


    @Override
    protected void onTextChanged(CharSequence text, final int start, int lengthBefore, final int lengthAfter) {
        setError(false);

        if (mPinEnteredListener != null) {
            mPinEnteredListener.onPinEntered(text);
        }

    }

    public void setOnPinEnteredListener(OnPinEnteredListener pinEnteredListener) {
        mPinEnteredListener = pinEnteredListener;
    }

}
