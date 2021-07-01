package com.srp.eways.util;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

public class ToastManager {
    private static ArrayList<Toast> toastArrayList = new ArrayList<>();


    public static void show(Context context, String description) {
        Toast toast = Toast.makeText(context, description, Toast.LENGTH_SHORT);
        toast.show();
        toastArrayList.add(toast);
    }

    public static void addToast(Toast toast) {
        toastArrayList.add(toast);
    }

    public static void dismissAllToasts() {
        for (Toast toast : toastArrayList) {
            if (toast != null) {
                toast.cancel();
            }
        }
    }


}
