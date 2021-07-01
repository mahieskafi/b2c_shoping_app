package com.srp.eways.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Eskafi on 7/6/2020.
 */
public class LinearLayoutSoftKeyboardDetector extends LinearLayout {

    private Listener listener;

    public LinearLayoutSoftKeyboardDetector(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public interface Listener {
        void onSoftKeyboardShown(boolean isShowing);

    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Activity activity = (Activity) getContext();

        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        int statusBarHeight = rect.top;
        int screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
        int diff = (screenHeight - statusBarHeight) - height;

        if (listener != null) {
            listener.onSoftKeyboardShown(diff > 128); // assume all soft keyboards are at least 128 pixels high
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
