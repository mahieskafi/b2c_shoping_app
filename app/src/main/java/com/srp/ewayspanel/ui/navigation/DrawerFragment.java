package com.srp.ewayspanel.ui.navigation;

import android.content.Context;

import com.srp.eways.base.BaseLoaderFragment;
import com.srp.eways.base.BaseViewModel;
import com.srp.ewayspanel.ui.main.MainActivity;

public abstract class DrawerFragment<V extends BaseViewModel> extends com.srp.eways.ui.navigation.DrawerFragment<V> {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(getActivity() instanceof MainActivity)) {
            throw new RuntimeException("Drawer fragment can only be used inside MainActivity.");
        }
    }

    public void toggleDrawer() {
        ((MainActivity) getActivity()).toggleDrawer();
    }

    public void onBackPressed(){
        ((MainActivity) getActivity()).onBackPressed();
    }

    @Override
    protected void getDataFromServer() {

    }
}
