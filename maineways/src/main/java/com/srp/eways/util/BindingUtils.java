package com.srp.eways.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

/**
 * Created by Eskafi on 7/27/2019.
 */
public class BindingUtils {

    public BindingUtils() {

    }

    public static void setImageUrl(ImageView imageView, String url) {

        Context context = imageView.getContext();

        if (url == null) {

            imageView.setImageDrawable(ContextCompat.getDrawable(context, 0));
        } else {

            Glide.with(context).load(url).into(imageView);
        }
    }

    public static void openLink(String url, Context context) {

        if (url != null && !url.equals("")) {

            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }
}
