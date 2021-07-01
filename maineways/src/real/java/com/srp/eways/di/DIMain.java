package com.srp.eways.di;

import com.srp.eways.BuildConfig;
import com.srp.eways.network.AppKeyProvider;
import com.srp.eways.repository.local.ICache;
import com.srp.eways.repository.local.PreferenceCache;
import com.srp.eways.repository.local.PreferencesProvider;
import com.srp.eways.util.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ErfanG on 3/2/2020.
 */
public class DIMain extends DIMainCommon {

    private static OkHttpClient okHttpClient;

    private static Retrofit mRetrofit;

    public static <T> T provideApi(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }


    public static void provideNetworkConfig(AppKeyProvider appKeyProvider) {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(Constants.CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(Constants.CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(DIMainCommon.getHeaderInterceptor(appKeyProvider))
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static ICache getPreferenceCache() {
        return PreferenceCache.getInstance(DIMainCommon.getContext());
    }
}
