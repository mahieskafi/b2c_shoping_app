package com.srp.eways;

/**
 * Created by ErfanG on 3/2/2020.
 */
public abstract class AppConfig {

    public static final String APP_KEY = "JXZYtqDmdPqpHkYL";
    public static final int SERVER_VERSION = 1;

    public static final String EWAYS_PREFERENCES_FILE_NAME = "ewaysPreferences";

    //HeaderInterceptor
    public static final String HEADER_TOKEN_NAME = "Authorization";
    public static final String HEADER_TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_APP_KEY_AME = "appKey";
    public static final String HEADER_CONTENT_TYPE_NAME = "content-type";
    public static final String HEADER_CONTENT_TYPE = "application/json";
    public static final String HEADER_LANG_NAME = "lang";
    public static final String HEADER_LANG = "FA";
}