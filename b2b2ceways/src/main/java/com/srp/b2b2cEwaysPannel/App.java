package com.srp.b2b2cEwaysPannel;


import androidx.multidex.MultiDexApplication;

import com.srp.b2b2cEwaysPannel.di.DI;
import com.srp.eways.di.DIMain;
import com.srp.eways.network.AppKeyProvider;
import com.srp.eways.repository.local.PreferencesProvider;
import com.srp.eways.ui.view.toolbar.WeiredToolbar;

import ir.abmyapp.androidsdk.ABConfig;
import ir.abmyapp.androidsdk.ABResources;

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {

        super.onCreate();

        DIMain.setContext(this);

        WeiredToolbar.SEQUENCE_LAYOUT = R.xml.sequences_weirdtoolbar;

        DI.setPreferencesProvider(new PreferencesProvider() {
            @Override
            public String getPreferencesFileName() {
                return AppConfig.EWAYS_PREFERENCES_FILE_NAME;
            }
        });

        DIMain.provideNetworkConfig(new AppKeyProvider() {
            @Override
            public String getAppKey() {
                return AppConfig.APP_KEY;
            }
        });

        abConfig();
    }

    private void abConfig() {
        ABResources.setConfiguration(new ABConfig.Builder()
                .setDebug(true)
                .build()
        );
    }
}
