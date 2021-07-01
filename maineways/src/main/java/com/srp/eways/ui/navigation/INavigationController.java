package com.srp.eways.ui.navigation;


import androidx.fragment.app.Fragment;

/**
 * Created by Eskafi on 8/11/2019.
 */
public interface INavigationController {

    boolean onBackPress();
    void openFragment(Fragment fragment);
    void onSwitchRoot(int id);

}
