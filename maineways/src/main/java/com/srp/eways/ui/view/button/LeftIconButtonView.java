package com.srp.eways.ui.view.button;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;
import com.srp.eways.ui.view.button.ButtonElement;
import com.srp.eways.R;
import com.srp.eways.di.DIMain;

import org.jetbrains.annotations.NotNull;

import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by Eskafi on 12/3/2019.
 */
public class LeftIconButtonView extends ButtonElement {

    private AppCompatImageView mIconNextLevel;

    private int mIconLeftMargin;

    public LeftIconButtonView(@NotNull Context context) {
        super(context);

        initialize(context, null, 0);
    }

    public LeftIconButtonView(@NotNull Context context, @NotNull AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0);
    }

    public LeftIconButtonView(@NotNull Context context, @NotNull AttributeSet attrs, int defStyleAtr) {
        super(context, attrs, defStyleAtr);

        initialize(context, attrs, defStyleAtr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAtr) {

        IABResources abResources = DIMain.getABResources();

        mIconNextLevel = new AppCompatImageView(context);

        mIconNextLevel.setImageDrawable(abResources.getDrawable(R.drawable.ic_next_level));

        mIconLeftMargin = abResources.getDimenPixelSize(R.dimen.left_icon_button_view_icon_margin_left);

        addView(mIconNextLevel);
    }

    public void setIconLeftMargin(int iconLeftMargin) {
        mIconLeftMargin = iconLeftMargin;

        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        mIconNextLevel.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        mIconNextLevel.layout(
                mIconLeftMargin,
                getHeight() / 2 - mIconNextLevel.getMeasuredHeight() / 2,
                mIconLeftMargin + mIconNextLevel.getMeasuredWidth(),
                getHeight() / 2 + mIconNextLevel.getMeasuredHeight() / 2);

    }

    public void setIconNextLevel(Drawable iconDrawable) {
        mIconNextLevel.setImageDrawable(iconDrawable);
    }

    public ImageView getIconImageView() {
       return mIconNextLevel;
    }
}
