package com.srp.eways.ui.aboutus;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.srp.eways.ui.navigation.DrawerFragment;
import com.srp.eways.ui.view.toolbar.WeiredToolbar;
import com.srp.eways.R;
import com.srp.eways.di.DIMain;

import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by Eskafi on 8/28/2019.
 */
public class AboutUsFragment extends DrawerFragment<AboutUsViewModel> {

    public static AboutUsFragment newInstance() {
        return new AboutUsFragment();
    }

    private WeiredToolbar mToolbar;
    private IABResources AB;

    @Override
    public AboutUsViewModel acquireViewModel() {

        return DIMain.getViewModel(AboutUsViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        AB = DIMain.getABResources();

        mToolbar = view.findViewById(R.id.toolbar);
        mToolbar.setBackgroundToolbarColor(AB.getColor(R.color.deposit_toolbar_background));
        mToolbar.setBackgroundColor(AB.getColor(R.color.deposit_toolbar_background));
        mToolbar.setTitle(AB.getString(R.string.about_us_title));
//        mToolbar.showBelowArc(true);
        mToolbar.setShowTitle(true);
        mToolbar.setShowShop(false);
        mToolbar.showNavigationDrawer(true);
//        mToolbar.setTitleIcon(AB.getDrawable(R.drawable.ic_about_us));
        mToolbar.setTitleTextColor(AB.getColor(R.color.white));
        mToolbar.setTitleTextSize(AB.getDimen(R.dimen.about_us_title_toolbar_size));
        mToolbar.setShowDeposit(AB.getBoolean(R.bool.aboutus_toolbar_has_deposit));
        mToolbar.setOnNavigationDrawerClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawer();
            }
        });
        mToolbar.setShowNavigationUp(true);
        mToolbar.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_aboutus;
    }

}
