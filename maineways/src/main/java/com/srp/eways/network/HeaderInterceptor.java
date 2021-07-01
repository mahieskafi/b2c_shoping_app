package com.srp.eways.network;

import com.srp.eways.AppConfig;
import com.srp.eways.di.DIMain;
import com.srp.eways.util.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ErfanG on 3/3/2020.
 */
public class HeaderInterceptor implements Interceptor {

    private AppKeyProvider mAppKeyProvider;

    public HeaderInterceptor(AppKeyProvider appKeyProvider) {
        mAppKeyProvider = appKeyProvider;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        String token = DIMain.getPreferenceCache().getString(Constants.TOKEN_KEY);
        if (token != null && !token.isEmpty()) {
            token = AppConfig.HEADER_TOKEN_PREFIX + token;
        }
        Request originalRequest = chain.request();
        Request.Builder builder = originalRequest.newBuilder()
                .addHeader(AppConfig.HEADER_TOKEN_NAME, (token != null && token.length() > 0) ? token : "")
                .addHeader(AppConfig.HEADER_APP_KEY_AME, mAppKeyProvider != null ? mAppKeyProvider.getAppKey() : AppConfig.APP_KEY)
                .addHeader(AppConfig.HEADER_CONTENT_TYPE_NAME, AppConfig.HEADER_CONTENT_TYPE)
                .addHeader(AppConfig.HEADER_LANG_NAME, AppConfig.HEADER_LANG);
        Request newRequest = builder.build();
        return chain.proceed(newRequest);

    }

}