package com.srp.eways.network;

public interface NetworkResponseCodes {

    int ERROR_NO_INTERNET = -1001;
    int ERROR_TIMEOUT = -1002;
    int ERROR_CONNECTION = -1003;
    int ERROR_UNDEFINED = -1004;

    int SUCCESS = 0;

    int ERROR_AUTHORIZATION_FAILED = -1005;
    int ERROR_UNSUPPORTED_API = -1006;
    int ERROR_SERVER_PROBLEM = -1007;

}
