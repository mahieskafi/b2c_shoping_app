package com.srp.ewayspanel;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;

import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.srp.eways.di.DIMain;
import com.srp.eways.network.AppKeyProvider;
import com.srp.eways.repository.local.PreferencesProvider;
import com.srp.eways.ui.view.toolbar.WeiredToolbar;
import com.srp.ewayspanel.di.DI;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;
import com.srp.ewayspanel.BuildConfig;
import ir.abmyapp.androidsdk.ABConfig;
import ir.abmyapp.androidsdk.ABResources;

/**
 * Created by Arcane on 7/16/2019.
 */
public class App extends MultiDexApplication {

    private static  String YANDEXApiID= "021e0f16-448b-4524-8fa5-465ca6a97302";
    @Override
    public void onCreate() {

        super.onCreate();

        YandexMetricaConfig yandexConfig = YandexMetricaConfig.newConfigBuilder(YANDEXApiID)
                .withNativeCrashReporting(true)
                .withLocationTracking(true)
                .withAppVersion(BuildConfig.VERSION_NAME)
                .build();

        YandexMetrica.activate(this, yandexConfig);
        YandexMetrica.enableActivityAutoTracking(this);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        DI.setContext(this);

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

        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(this, config);

    }

    private void abConfig() {
        ABResources.setConfiguration(new ABConfig.Builder()
                        .setDebug(true)
                        .build()
        );
    }
}
