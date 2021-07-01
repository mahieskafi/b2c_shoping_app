package com.srp.ewayspanel.model.storepage.category;

import androidx.annotation.ColorInt;

public interface ICategoryItem {

    String getTitle();

    void setColor(@ColorInt int color);

    @ColorInt int getColor();

}
