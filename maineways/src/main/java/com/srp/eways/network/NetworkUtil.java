package com.srp.eways.network;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;

import ir.abmyapp.androidsdk.IABResources;

public class NetworkUtil implements NetworkResponseCodes {

    public static String getErrorText(int errorCode) {

        IABResources resources = DIMain.getABResources();

        switch (errorCode) {
            case ERROR_NO_INTERNET:
                return resources.getString(R.string.network_error_no_internet);

            case ERROR_AUTHORIZATION_FAILED:
                return resources.getString(R.string.network_error_authorization_failed);

            case ERROR_CONNECTION:
                return resources.getString(R.string.network_error_connection);

            case ERROR_SERVER_PROBLEM:
                return resources.getString(R.string.network_error_server_problem);

            case ERROR_TIMEOUT:
                return resources.getString(R.string.network_error_timeout);

            case ERROR_UNSUPPORTED_API:
                return resources.getString(R.string.network_error_unsupported_api);

            case ERROR_UNDEFINED:
            default:
                return resources.getString(R.string.network_error_undefined);
        }
    }
}
