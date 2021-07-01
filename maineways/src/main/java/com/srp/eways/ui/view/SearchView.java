package com.srp.eways.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.res.ResourcesCompat;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;

import ir.abmyapp.androidsdk.IABResources;

public class SearchView extends FrameLayout {

    public interface SearchViewListener {

        void onSearchTextChanged(String searchText);

    }

    private AppCompatEditText mEditText;

    private SearchViewListener mListener;

    public SearchView(@NonNull Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public SearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public SearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        IABResources abResources = DIMain.getABResources();

        Drawable searchIcon = abResources.getDrawable(R.drawable.ic_userphonebook_searchview_search).mutate();
        searchIcon.setColorFilter(abResources.getColor(R.color.userinputchoice_searchview_searchicon_color), PorterDuff.Mode.SRC_IN);

        int hintColor = abResources.getColor(R.color.userphonebook_searchview_hint_color);
        int textColor = abResources.getColor(R.color.userphonebook_searchview_text_color);

        String editTextHint = abResources.getString(R.string.userphonebook_searchview_hint);

        Typeface textTypeFace = ResourcesCompat.getFont(context, R.font.iran_yekan_light);

        int textSize = abResources.getDimenPixelSize(R.dimen.userphonebook_searchview_textsize);

        setFocusable(true);
        setFocusableInTouchMode(true);

        mEditText = new AppCompatEditText(context);

        mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mEditText.setTextColor(textColor);
        mEditText.setHintTextColor(hintColor);
        mEditText.setTypeface(textTypeFace);
        mEditText.setHint(editTextHint);
        mEditText.setGravity(Gravity.RIGHT);
        mEditText.setMaxLines(1);
        mEditText.setSingleLine(true);

        mEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, searchIcon, null);
        mEditText.setCompoundDrawablePadding(abResources.getDimenPixelSize(R.dimen.serachview_drawablepadding));

        mEditText.setBackground(getEditTextBackgroundDrawable());

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        mEditText.setLayoutParams(layoutParams);

        addView(mEditText);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mListener != null) {
                    mListener.onSearchTextChanged(s.toString());
                }
            }
        });
    }

    public void setDonKeyboard(boolean done){
        if (done){
            mEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }
    }

    public void setSearchViewListener(SearchViewListener listener) {
        mListener = listener;
    }

    public void setHint(String hint) {
        mEditText.setHint(hint);
    }

    public void setText(String text) {
        mEditText.setText(text);
    }

    public void setBackground( @DrawableRes int background) {
        mEditText.setBackground(DIMain.getABResources().getDrawable(background));
    }

    private Drawable getEditTextBackgroundDrawable() {
        IABResources abResources = DIMain.getABResources();

        int editTextBackgroundColor = abResources.getColor(R.color.userphonebook_searchview_background_color);
        int editTextBackgroundRadius = abResources.getDimenPixelSize(R.dimen.userphonebook_searchview_background_radius);

        RoundRectShape shape = new RoundRectShape(new float[]{editTextBackgroundRadius,
                editTextBackgroundRadius,
                editTextBackgroundRadius,
                editTextBackgroundRadius,
                editTextBackgroundRadius,
                editTextBackgroundRadius,
                editTextBackgroundRadius,
                editTextBackgroundRadius}, null, null);

        ShapeDrawable drawable = new ShapeDrawable();

        drawable.setShape(shape);
        drawable.setColorFilter(editTextBackgroundColor, PorterDuff.Mode.SRC_IN);

        int paddingLeft = abResources.getDimenPixelSize(R.dimen.searchview_background_paddingleft);
        int paddingTop = abResources.getDimenPixelSize(R.dimen.searchview_background_paddingtop);
        int paddingRight = abResources.getDimenPixelSize(R.dimen.searchview_background_paddingright);
        int paddingBottom = abResources.getDimenPixelSize(R.dimen.searchview_background_paddingbottom);

        drawable.setPadding(new Rect(paddingLeft, paddingTop, paddingRight, paddingBottom));

        return drawable;
    }

}
