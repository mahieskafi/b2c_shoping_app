package com.srp.eways.util;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ErfanG on 26/08/2019.
 */
public class Constants {

    public static final String DB_NAME = "Eways.db";

    //SharePreference Constants
    public static final String TOKEN_KEY = "token";
    public static final String USER_INFO = "userInfo";
    public static final String FIRST_OPEN = "firstOpen";
    public static final String KEY_NUMBER = "keynumber";
    public static final String TRASE_CODE = "traseCode";
    public static final String LAST_VERSION_CODE = "lastVersionCode";
    public static final String TRACE_CODE = "trace_code";
    public static final String NEEDED_TO_CONFIRM_NUMBER = "neededToConfirmNumber";
    public static final String IMEI = "imei";
    public static final String LOGIN_PEREFERENCE_KEY = "prefrence";
    public static final String LOGIN_PEREFERENCE_USERNAME_KEY = "nameKey";
    public static final String LOGIN_PEREFERENCE_PASSWORD_KEY = "passwordKey";
    public static final String LOGIN_PEREFERENCE_REMEMBERCHECK_KEY = "prefrence";


    //Other Constants
    public static final String SIGN_UP_LINK = "https://www.eways.co/store_register.aspx";
    public static final String RESET_PASSWORD = "https://www.eways.co/store_register.aspx";

    public static final String REAL_FLAVER = "real";
    public static final String MOCK_FLAVER = "mock";

    public static final long CONNECTION_TIME_OUT = 60;

    //MPL
    public static final String MPL_TYPE_KEY = "Type";
    public static final String MPL_TYPE_VALUE = "1";
    public static final String MPL_TOKEN_KEY = "Token";
    public static final String MPL_ORDER_ID_KEY = "OrderID";
    public static final String MPL_ERROR_CODE_KEY = "errorType";
    public static final String MPL_END_DATE_KEY = "enData";
    public static final String MPL_MESSAGE_KEY = "message";
    public static final String MPL_STATUS_KEY = "status";


    public static final String BUY_CHARGE_TYPE_CHARGE = "شارژ موبایل";
    public static final String BUY_CHARGE_TYPE_INTERNET = "بسته اینترنت";
    public static final String BUY_CHARGE_TYPE_SPECIALOFFERS = "پیشنهاد ویژه ایرانسل";
    public static final int RESEND_VERIFICATION_CODE_TIMEOUT = 60;

    public static final int ZXING_CAMERA_PERMISSION = 1;
    public static final int WRITE_STORAGE_PERMISSION = 2;
    public static final int READ_CONTACT_PERMISSION = 3;

    public static final int SAVE_BILL_SUCCESS_CODE = 7;

    public static final int SAVE_TYPE = 0;
    public static final int SHARE_TYPE = 1;

    public static final String DEEPLINK_SCHEME = "ewayspanel";
    public static final String DEEPLINK_CREDIT = "increasecredit";
    public static final String DEEPLINK_BILL = "bill";
    public static final String DEEPLINK_STORE = "store";

    public static final String DEEPLINK_INCREASECREDIT = "increasecredit";
    public static final String DEEPLINK_TRANSACTION = "transaction";
    public static final String DEEPLINK_SALES = "sales";
    public static final String DEEPLINK_CONTACTSALEEXPERT = "ContactSaleExpert";
    public static final String DEEPLINK_ABOUTUS = "AboutUs";
    public static final String DEEPLINK_DETAIL = "detail";
    public static final String DEEPLINK_LIST = "list";
    public static final String DEEPLINK_BILLPAY = "BillPay";
    public static final String DEEPLINK_CHARGEPAY = "ChargePay";
    public static final String DEEPLINK_BASHGAH = "Bashgah";

    public static final String DEEP_LINK_BUY_CHARGE_HOST_NAME = "topup";
    public static final String DEEP_LINK_BUY_GETWAY_HOST_NAME = "store";
    public static final String DEEP_LINK_INCREASE_CREDIT_HOST_NAME = "credit";

    public static final int BILL_PAYMENT_PAGE = 0;
    public static final int BILL_INQUIRY_PAGE = 1;
    public static final int BILL_ARCHIVE_PAGE = 2;
    public static final int BILL_REPORT_PAGE = 3;

    public static final int CHARGE_BUY_TAB_ID = 2;
    public static final int CHARGE_INQUIRY_TAB_ID = 1;
    public static final int CHARGE_TRANSACTION_TAB_ID = 0;

    public static String IS_PAGE_TRANSACTION_EXECUTED_KEY = "is_PageTransactions_Executed";
}
