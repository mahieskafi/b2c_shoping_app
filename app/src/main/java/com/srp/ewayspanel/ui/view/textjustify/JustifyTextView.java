package com.srp.ewayspanel.ui.view.textjustify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.widget.TextView;


public class JustifyTextView extends TextView {
    private Integer charCounter;
    private Paint paint = new Paint();

    private String[] blocks;
    private float spaceOffset = 0;
    private float horizontalOffset = 0;
    private float verticalOffset = 0;
    private float horizontalFontOffset = 0;
    private float dirtyRegionWidth = 0;
    private boolean wrapEnabled = false;
    int left, top, right, bottom = 0;
    private Align _align = Align.RIGHT;
    private float strecthOffset;
    private float wrappedEdgeSpace;
    private String block;
    private String wrappedLine;
    private String[] lineAsWords;
    private Object[] wrappedObj;

    public boolean hyphenate = false;
    private String syllableSeparator = "";

    private Bitmap cache = null;
    private boolean cacheEnabled = false;

    public JustifyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setPadding(10, 0, 10, 0);
    }

    public JustifyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setPadding(10, 0, 10, 0);
    }

    public JustifyTextView(Context context) {
        super(context);
        this.setPadding(10, 0, 10, 0);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        // TODO Auto-generated method stub

        super.setPadding(left + 10, top, right + 10, bottom);
    }

    @Override
    public void setDrawingCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public void setText(String st, boolean wrap) {
        wrapEnabled = wrap;
        super.setText(st);
    }

    public void setTextAlign(Align align) {
        _align = align;
    }

    @SuppressLint("NewApi")
    @Override
    protected void onDraw(Canvas canvas) {

        if (!wrapEnabled) {
            super.onDraw(canvas);
            return;
        }

        Canvas activeCanvas = null;

        if (cacheEnabled) {

            if (cache != null) {
                canvas.drawBitmap(cache, 0, 0, paint);
                return;
            } else {
                cache = Bitmap.createBitmap(getWidth(), getHeight(),
                        Config.ARGB_4444);
                activeCanvas = new Canvas(cache);
            }
        } else {
            activeCanvas = canvas;
        }

        paint.setColor(getCurrentTextColor());
        paint.setTypeface(getTypeface());
        paint.setTextSize(getTextSize());
        paint.setTextAlign(_align);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        dirtyRegionWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int maxLines = Integer.MAX_VALUE;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            maxLines = getMaxLines();
        }
        int lines = 1;
        blocks = getText().toString().split("((?<=\n)|(?=\n))");
        verticalOffset = horizontalFontOffset = getLineHeight() - 0.5f; // Temp fix
        spaceOffset = paint.measureText(" ");

        for (int i = 0; i < blocks.length && lines <= maxLines; i++) {
            block = blocks[i];
            horizontalOffset = 0;

            if (block.length() == 0) {
                continue;
            } else if (block.equals("\n")) {
                verticalOffset += horizontalFontOffset;
                continue;
            }

            block = block.trim();

            if (block.length() == 0) {
                continue;
            }

            wrappedObj = TextJustifyUtils.createWrappedLine(block, paint, spaceOffset, dirtyRegionWidth, hyphenate, syllableSeparator);

            wrappedLine = ((String) wrappedObj[0]);
            wrappedEdgeSpace = (Float) wrappedObj[1];
            charCounter = (Integer) wrappedObj[2];
            lineAsWords = wrappedLine.split(" ");
            strecthOffset = wrappedEdgeSpace != Float.MIN_VALUE ? wrappedEdgeSpace / (lineAsWords.length - 1) : 0;

            for (int j = 0; j < lineAsWords.length; j++) {
                String word = lineAsWords[j];
                if (lines == maxLines && j == lineAsWords.length - 1) {
                    activeCanvas.drawText("...", horizontalOffset, verticalOffset, paint);


                } else if (j == 0) {
                    if (_align == Align.RIGHT) {
                        activeCanvas.drawText(word, getWidth() - (getPaddingRight()), verticalOffset, paint);
                        horizontalOffset += getWidth() - (getPaddingRight());
                    } else {
                        activeCanvas.drawText(word, getPaddingLeft(), verticalOffset, paint);
                        horizontalOffset += getPaddingLeft();
                    }

                } else {
                    activeCanvas.drawText(word, horizontalOffset, verticalOffset, paint);
                }
                if (_align == Align.RIGHT)
                    horizontalOffset -= paint.measureText(word) + spaceOffset + strecthOffset;
                else
                    horizontalOffset += paint.measureText(word) + spaceOffset + strecthOffset;
            }

            lines++;

            if (blocks[i].length() > 0) {
                blocks[i] = blocks[i].substring(charCounter);
                verticalOffset += blocks[i].length() > 0 ? horizontalFontOffset : 0;
                i--;
            }
        }

        if (cacheEnabled) {
            canvas.drawBitmap(cache, 0, 0, paint);
        }
    }

    public void setHyphenate(boolean hyphenate, String syllableSeparator) {
        this.hyphenate = hyphenate;
        this.syllableSeparator = syllableSeparator;
    }
}

