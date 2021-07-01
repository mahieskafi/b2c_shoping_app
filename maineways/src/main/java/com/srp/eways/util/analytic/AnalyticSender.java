package com.srp.eways.util.analytic;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.srp.eways.base.BaseActivity;

public class AnalyticSender {

    private static AnalyticSender INSTANCE;
    
    private FirebaseAnalytics mFireBaseAnalytic;

    public static AnalyticSender getInstance(Context context) {
        
        if (INSTANCE == null) {
            
            INSTANCE = new AnalyticSender(context);
        }

        return INSTANCE;
    }

    private AnalyticSender(Context context) {

        mFireBaseAnalytic = FirebaseAnalytics.getInstance(context);
    }

//    public void setUserId(String userId) {
//        //The User ID feature enables the measurement of user activities that span across devices in Google Analytics
//        mTracker.set("&uid", userId);
//    }

    public void sendScreen(BaseActivity activity, String name) {

        mFireBaseAnalytic.setCurrentScreen(activity, name, null);
    }

    public void sendAction(String category, String action, String label, String value) {

        Bundle bundle = new Bundle();
        bundle.putString(label, action);
        bundle.putString(BaseAnalyticConstant.VALUE, value);

        mFireBaseAnalytic.logEvent(category, bundle);
    }

    public void sendAction(String category, String action, String label) {

        Bundle bundle = new Bundle();
        bundle.putString(label, action);

        mFireBaseAnalytic.logEvent(category, bundle);
    }

    public void sendAction(String category, String action) {

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, action);

        mFireBaseAnalytic.logEvent(category, bundle);
    }
}
