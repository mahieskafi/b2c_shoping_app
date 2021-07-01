package com.srp.eways.util.analytic;

import android.content.Context;

import com.srp.eways.base.BaseActivity;
import com.srp.eways.di.DIMain;

/**
 * Created by Eskafi on 8/19/2019.
 */
public class EventSender {
    
    private Context context;
    
    private static EventSender INSTANCE;

    public static EventSender get(Context context) {

        if (INSTANCE == null) {

            INSTANCE = new EventSender(context);
        }

        return INSTANCE;
    }

    EventSender(Context context) {

        this.context = context;
    }

    public void sendScreen(BaseActivity activity, String screenName) {

        AnalyticSender.getInstance(context).sendScreen(activity, screenName);
    }

    public void sendAction(String category, String action, String label, String value) {

        AnalyticSender.getInstance(context).sendAction(category, action, label, value);
        DIMain.getABResources().recordEvent(action);
    }

    public void sendAction(String category, String action, String label) {

        AnalyticSender.getInstance(context).sendAction(category, action, label);
        DIMain.getABResources().recordEvent(action);
    }

    public void sendAction(String category, String action) {

        AnalyticSender.getInstance(context).sendAction(category, action);
        DIMain.getABResources().recordEvent(action);
    }
}
