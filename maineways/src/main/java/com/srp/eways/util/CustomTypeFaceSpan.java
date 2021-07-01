package com.srp.eways.util;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Eskafi on 9/23/2019.
 */
public class CustomTypeFaceSpan extends TypefaceSpan {
    private Typeface mNewTypeFace;

    public CustomTypeFaceSpan(@Nullable String family, Typeface typeface) {
        super(family);
        mNewTypeFace = typeface;
    }

    @Override
    public void updateDrawState(@NonNull TextPaint textPaint) {
        applyCustomTypeFace(textPaint, mNewTypeFace);
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint paint) {
        applyCustomTypeFace(paint, mNewTypeFace);
    }

    private static void applyCustomTypeFace(Paint paint, Typeface tf) {
        int oldStyle;
        Typeface oldTypeFace = paint.getTypeface();
        if (oldTypeFace == null) {
            oldStyle = 0;
        } else {
            oldStyle = oldTypeFace.getStyle();
        }

        int fake = oldStyle & ~tf.getStyle();
        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(tf);
    }
}
