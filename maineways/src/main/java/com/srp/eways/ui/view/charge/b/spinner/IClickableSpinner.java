package com.srp.eways.ui.view.charge.b.spinner;

import android.graphics.drawable.Drawable;

public interface IClickableSpinner {

    void setHint(String hint);

    void setText(String text);

    void setIcon(Drawable drawable);

    void setClickableSpinnerViewListener(ClickableSpinner.ClickableSpinnerViewListener listener);

}
