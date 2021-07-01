package com.srp.eways.ui.charge;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.appbar.AppBarLayout;

/**
 * Created by ErfanG on 13/10/2019.
 */
public class ChargeBannerBehaviour extends AppBarLayout.ScrollingViewBehavior {

    public ChargeBannerBehaviour(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                   @NonNull View child,
                                   @NonNull View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);

        ViewCompat.setElevation(child, 0);
        ViewCompat.setElevation(target, 0);

    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull View child,
                               @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);

        ViewCompat.setElevation(child, 0);
    }
}
