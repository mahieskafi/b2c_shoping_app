package com.srp.eways.ui.main;

import com.srp.eways.base.BaseViewModel;
import com.srp.eways.ui.navigation.NavigationActivity;

public abstract class CommonMainActivity<V extends BaseViewModel> extends NavigationActivity<V> {

    public abstract void toggleDrawer();

    public abstract void onBackPressed();

    public void onChildBackPress()
    {
        super.onBackPressed();
    }

    public abstract void gotoLogin();
}
