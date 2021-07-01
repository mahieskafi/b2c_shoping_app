package com.srp.eways.ui.navigation;

import android.content.Context;

import com.srp.eways.base.BaseActivity;
import com.srp.eways.base.BaseLoaderFragment;
import com.srp.eways.base.BaseViewModel;
import com.srp.eways.ui.main.CommonMainActivity;

public abstract class DrawerFragment<V extends BaseViewModel> extends BaseLoaderFragment<V> {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(getActivity() instanceof BaseActivity)) {
            throw new RuntimeException("Drawer fragment can only be used inside MainActivity.");
        }
    }

    public void toggleDrawer() {
        ((CommonMainActivity) getActivity()).toggleDrawer();
    }

    public void onBackPressed(){
        ((CommonMainActivity) getActivity()).onBackPressed();
    }

    @Override
    protected void getDataFromServer() {

    }
}
