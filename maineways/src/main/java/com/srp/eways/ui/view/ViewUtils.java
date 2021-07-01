package com.srp.eways.ui.view;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;

import ir.abmyapp.androidsdk.ABResources;
import ir.abmyapp.androidsdk.IABResources;

import static android.view.View.LAYER_TYPE_SOFTWARE;

public class ViewUtils {

    public static final int NO_ELEVATION_SET = -1;
    public static final int NO_CARD_BACKGROUNDCOLOR_SET = -1;

    public static void setCardBackground(View view) {
        setCardBackground(view, NO_ELEVATION_SET, NO_CARD_BACKGROUNDCOLOR_SET);
    }

    public static void setCardBackground(View view, float elevation) {
        setCardBackground(view, elevation, NO_CARD_BACKGROUNDCOLOR_SET);
    }

    public static void setCardBackground(View view, float elevation, @ColorInt int cardBackgroundColor) {
        IABResources abResources = DIMain.getABResources();

        float cr = abResources.getDimen(R.dimen.card_radius);
        RoundRectShape shape = new RoundRectShape(new float[]{cr, cr, cr, cr, cr, cr, cr, cr}, null, null);
        ShapeDrawable drawable = new ShapeDrawable();
        drawable.setShape(shape);
        drawable.setColorFilter(cardBackgroundColor == NO_ELEVATION_SET ? abResources.getColor(R.color.card_background) : cardBackgroundColor, PorterDuff.Mode.SRC_IN);
        int paddingLeft = abResources.getDimenPixelSize(R.dimen.card_padding_left);
        int paddingTop = abResources.getDimenPixelSize(R.dimen.card_padding_top);
        int paddingRight = abResources.getDimenPixelSize(R.dimen.card_padding_right);
        int paddingBottom = abResources.getDimenPixelSize(R.dimen.card_padding_bottom);
        drawable.setPadding(new Rect(paddingLeft, paddingTop, paddingRight, paddingBottom));

        view.setBackground(drawable);
        ViewCompat.setElevation(view, elevation == NO_ELEVATION_SET ? abResources.getDimenPixelSize(R.dimen.card_elevation) : elevation);
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);

        return metrics;
    }

    public static Bitmap convertViewToBitmap(Context context, View view) {
        IABResources abResources = DIMain.getABResources();
        Bitmap bitmap;


        if (view.getWidth() == 0 && view.getHeight() == 0) {
            view.measure(abResources.getDimenPixelSize(R.dimen.bill_receipt_default_width), abResources.getDimenPixelSize(R.dimen.bill_receipt_default_height));
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        }
        bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);


        Canvas canvas = new Canvas(bitmap);

        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);

        return bitmap;
    }

    public static void getDeepChildOffset(final ViewGroup mainParent, final ViewParent parent, final View child, final Point accumulatedOffset) {
        ViewGroup parentGroup = (ViewGroup) parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getTop();
        if (parentGroup.equals(mainParent)) {
            return;
        }
        getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
    }

    public static void scrollToView(final View view, final ViewGroup rootView) {
        rootView.post(new Runnable() {
            @Override
            public void run() {
                Point childOffset = new Point();
                ViewUtils.getDeepChildOffset(rootView, view.getParent(), view, childOffset);
                ObjectAnimator animator = ObjectAnimator.ofInt(rootView, "scrollY", childOffset.y);
                animator.setDuration(800);
                animator.start();
            }
        });
    }

    public static int getDisplayHeight(Context context)
    {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) getDisplayMetrics(context).densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static class RtlGridLayoutManager extends GridLayoutManager {

        public RtlGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public RtlGridLayoutManager(Context context, int spanCount) {
            super(context, spanCount);
        }

        public RtlGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
            super(context, spanCount, orientation, reverseLayout);
        }

        @Override
        protected boolean isLayoutRTL() {
            return true;
        }
    }
}
