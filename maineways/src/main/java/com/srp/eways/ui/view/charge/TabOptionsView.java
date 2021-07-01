package com.srp.eways.ui.view.charge;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import java.util.List;

public abstract class TabOptionsView extends CardView {

    public interface ChargeOptionsTabViewListener {

        void onChargeTabClicked(int tabIndex, TabOptionsView.TabItem tabItem);

    }

    public TabOptionsView(@NonNull Context context) {
        super(context);
    }

    public TabOptionsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TabOptionsView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public abstract void setTabItems(List<TabItem> tabItems);

    public abstract void setOnChargeTabClickListener(ChargeOptionsTabViewListener listener);

    public abstract void setSelectedTab(int tab);

    public abstract void setSelectedTabAndNotify(int tab);

    public abstract void setTabBackgroundColor(int color);

    public static class TabItem {

        public String title;
        public int icon;

        public Object data;

        public TabItem(String title, int icon, Object data) {
            this.title = title;
            this.icon = icon;
            this.data = data;
        }
    }

}
