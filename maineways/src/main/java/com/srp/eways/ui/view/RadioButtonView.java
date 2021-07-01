package com.srp.eways.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.srp.eways.R;
import com.srp.eways.ui.charge.buy.RadioOptionModel;

import ir.abmyapp.androidsdk.ABResources;
import ir.abmyapp.androidsdk.IABResources;

public class RadioButtonView extends View {

    public interface RadioButtonSelectionListener {

        void onRadioButtonSelected(RadioButtonView radioButtonView);

        void onRemoveRadioButtonClicked(RadioButtonView radioButtonView);

    }

    private GestureDetector mGestureDetector;

    private Drawable mSelectedDrawable;
    private Drawable mUnSelectedDrawable;
    private Drawable mSelectedDrawableDisabled;
    private Drawable mUnSelectedDrawableDisabled;

    private Drawable mCrossDrawable;

    private RadioOptionModel mData;

    private int mItemPaddingVertical;

    private int mCircleDrawableWidth;
    private int mTitleMarginRight;
    private int mTitleMarginLeft;

    private int mCrossDrawableWidth;
    private int mCrossDrawableMarginLeft;

    public Rect mTextTitleBounds = new Rect();
    public Rect mTextTitlePaintTempBounds = new Rect();

    private TextPaint mTitlePaint;

    private int mTitleUserInputChoiceColor;
    private int mTitleNormalChoiceColor;

    private float mDeltaY;

    private boolean mIsEnabled = true;

    private boolean mIsSelected = false;

    private float mLineSpacing = 1.3f;
    private int mTextLines;
    private int mTitleMeasuredHeight;

    private StaticLayout mTitleLayout;

    private RadioButtonSelectionListener mListener;

    public RadioButtonView(Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public RadioButtonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public RadioButtonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        mGestureDetector = new GestureDetector(context, mOnGestureListener);

        IABResources abResources = ABResources.get(context);

        mTitleNormalChoiceColor = abResources.getColor(R.color.radiobuttonview_title_normal_color);
        mTitleUserInputChoiceColor = abResources.getColor(R.color.radiobuttonview_title_userinputchoice_color);

        mTitlePaint = new TextPaint();
        mTitlePaint.setAntiAlias(true);
        mTitlePaint.setColor(mTitleNormalChoiceColor);
        mTitlePaint.setTypeface(ResourcesCompat.getFont(context, R.font.iran_yekan));
        mTitlePaint.setTextSize(abResources.getDimen(R.dimen.radiobuttonview_title_size));

        mDeltaY = (mTitlePaint.ascent() + mTitlePaint.descent()) / 2;

        mItemPaddingVertical = abResources.getDimenPixelSize(R.dimen.n_column_radiogroup_buttonitem_paddingvertical);

        mCircleDrawableWidth = abResources.getDimenPixelSize(R.dimen.radiobuttonview_drawable_width);
        mTitleMarginRight = abResources.getDimenPixelSize(R.dimen.radiobuttonview_title_marginright);
        mTitleMarginLeft = abResources.getDimenPixelSize(R.dimen.radiobuttonview_title_margin_left);

        mSelectedDrawable = abResources.getDrawable(R.drawable.ic_radioitem_selected).mutate();
        mUnSelectedDrawable = abResources.getDrawable(R.drawable.ic_radioitem_unselected).mutate();

        mSelectedDrawableDisabled = abResources.getDrawable(R.drawable.ic_radioitem_selected_disabled).mutate();
        mUnSelectedDrawableDisabled = abResources.getDrawable(R.drawable.ic_radioitem_unselected_disabled).mutate();

        mCrossDrawable = abResources.getDrawable(R.drawable.ic_cross_cancel_contact);

        mCrossDrawableWidth = abResources.getDimenPixelSize(R.dimen.radiobuttonview_crossdrawable_width);
        mCrossDrawableMarginLeft = abResources.getDimenPixelSize(R.dimen.radiobuttonview_crossdrawable_marginleft);
    }

    public void setOnRadioButtonSelectionListener(RadioButtonSelectionListener listener) {
        mListener = listener;
    }

    public void setData(RadioOptionModel data) {
        mData = data;

        invalidate();
    }

    public void setSelected(boolean isSelected) {
        mIsSelected = isSelected;

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        String title = mData.title.replaceAll("\n", " ").trim();
        mTitlePaint.getTextBounds(title, 0, title.length(), mTextTitlePaintTempBounds);

        mTitleMeasuredHeight = mTextTitlePaintTempBounds.height();
        int titleMeasuredWidth = mTextTitlePaintTempBounds.width();

        int width;

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
            width = width - mTitleMarginRight;
        } else {//UNSPECIFIED
            width = mCircleDrawableWidth + mTitleMarginRight + titleMeasuredWidth;

            if (mData.isUserInputChoice) {
                width += mCrossDrawableWidth + mCrossDrawableMarginLeft;
            }
        }

        int textViewWidth = width - mCircleDrawableWidth - mTitleMarginLeft;

        mTextLines = (int) Math.max(1, (long) (titleMeasuredWidth) / textViewWidth);
        if (mTextLines * textViewWidth < titleMeasuredWidth) {
            mTextLines += 1;
        }
        if (textViewWidth < titleMeasuredWidth) {

            mTitleMeasuredHeight = (int) (mTextLines * mTitleMeasuredHeight * mLineSpacing);
        }

        int height = Math.max(mTitleMeasuredHeight, mCircleDrawableWidth) + 2 * mItemPaddingVertical;


        setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = getWidth();
        int height = getHeight();

        int centerY;
        if (mTextLines == 1) {
            centerY = height / 2;
        } else {
            centerY = height / 2 - (mTitleMeasuredHeight / 2) + (mCircleDrawableWidth / 2);
        }

        mSelectedDrawable.setBounds(width - mCircleDrawableWidth, centerY - (mCircleDrawableWidth / 2), width, centerY + (mCircleDrawableWidth / 2));
        mUnSelectedDrawable.setBounds(width - mCircleDrawableWidth, centerY - (mCircleDrawableWidth / 2), width, centerY + (mCircleDrawableWidth / 2));

        mSelectedDrawableDisabled.setBounds(width - mCircleDrawableWidth, centerY - (mCircleDrawableWidth / 2), width, centerY + (mCircleDrawableWidth / 2));
        mUnSelectedDrawableDisabled.setBounds(width - mCircleDrawableWidth, centerY - (mCircleDrawableWidth / 2), width, centerY + (mCircleDrawableWidth / 2));


//        int titleMeasuredWidth = mTextTitlePaintTempBounds.width();
//        int titleMeasuredHeight = mTextTitlePaintTempBounds.height();

        int titleRight = mSelectedDrawable.getBounds().left - mTitleMarginRight;
        if (mData.isUserInputChoice) {
            titleRight = titleRight - mCrossDrawableMarginLeft - mCrossDrawableWidth;
        }

        Layout.Alignment alignment;

        String title = mData.title.replaceAll(",", "");

        if (title.replaceAll("\\(.*?\\) ?", "").trim().matches("\\d+(?:\\.\\d+)?")) {
            alignment = Layout.Alignment.ALIGN_OPPOSITE;
        } else {
            alignment = Layout.Alignment.ALIGN_NORMAL;
        }
        mTitleLayout = new StaticLayout(mData.title.replaceAll("\n", " ").trim(), mTitlePaint, titleRight,
                alignment, mLineSpacing, 0.0f, false);
//        mTextTitleBounds.set(titleRight - titleMeasuredWidth, 0, titleRight, titleMeasuredHeight);

        mCrossDrawable.setBounds(mCrossDrawableMarginLeft, height / 2 - mCrossDrawableWidth / 2, mCrossDrawableMarginLeft + mCrossDrawableWidth, height / 2 + mCrossDrawableWidth / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mIsEnabled){
            if (mIsSelected) {
                mSelectedDrawable.draw(canvas);
            } else {
                mUnSelectedDrawable.draw(canvas);
            }
        }
        else{
            if (mIsSelected) {
                mSelectedDrawableDisabled.draw(canvas);
            } else {
                mUnSelectedDrawableDisabled.draw(canvas);
            }
        }



        mTitlePaint.setColor(mData.isUserInputChoice ? mTitleUserInputChoiceColor : mTitleNormalChoiceColor);
//        canvas.drawText(mData.title, mTextTitleBounds.left, mTextTitleBounds.centerY() - mDeltaY, mTitlePaint);

        if (mData.isUserInputChoice) {
            mCrossDrawable.draw(canvas);
        }


        int textXTranslate = 0;
        if (mData.isUserInputChoice) {
            textXTranslate = textXTranslate + mCrossDrawableMarginLeft + mCrossDrawableWidth;
        }

        canvas.translate(textXTranslate, mSelectedDrawable.getBounds().top);
        mTitleLayout.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (!mData.isUserInputChoice) {
                if (e.getX() < mTextTitleBounds.left) {

                    return false;

                } else {
                    notifyOnButtonSelected();

                    return true;
                }
            } else {
                if (e.getX() > (mCrossDrawable.getBounds().right + mCrossDrawableMarginLeft)) {
                    notifyOnButtonSelected();

                    return true;
                } else {//
                    notifyOnRemovedIconSelected();
                    return true;
                }
            }
        }

    };

    private void notifyOnRemovedIconSelected() {
        if (mListener != null && mIsEnabled) {
            mListener.onRemoveRadioButtonClicked(RadioButtonView.this);
        }
    }

    public RadioOptionModel getData() {
        return mData;
    }

    public int getNeededMeasuredWidth() {
        mTitlePaint.getTextBounds(mData.title, 0, mData.title.length(), mTextTitlePaintTempBounds);

        int titleMeasuredWidth = mTextTitlePaintTempBounds.width();

        int width;

        width = mCircleDrawableWidth + mTitleMarginRight + titleMeasuredWidth;

        if (mData.isUserInputChoice) {
            width += mCrossDrawableWidth + mCrossDrawableMarginLeft;
        }

        return width;
    }

    private void notifyOnButtonSelected() {
        if (mIsSelected) {
            return;
        }

        if (mListener != null && mIsEnabled) {
            mListener.onRadioButtonSelected(RadioButtonView.this);
        }
    }

    public void setTextColor(int color) {
        mTitleNormalChoiceColor = color;
    }

    public void setSelectedDrawable(Drawable selectedDrawable) {
        mSelectedDrawable = selectedDrawable;
    }

    public void setUnSelectedDrawable(Drawable unSelectedDrawable) {
        mUnSelectedDrawable = unSelectedDrawable;
    }

    public void setEnable(boolean isEnabled) {

        mIsEnabled = isEnabled;
        setClickable(isEnabled);
        setEnabled(isEnabled);
    }
}
