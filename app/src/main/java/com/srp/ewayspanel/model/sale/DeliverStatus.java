package com.srp.ewayspanel.model.sale;

/**
 * Created by Eskafi on 1/13/2020.
 */
public interface DeliverStatus {

    int NEW_OR_WAITING_FOR_PAYMENT_STATUS = 1;
    int SUCCESSFULL_STATUS = 2;
    int CANCEL_OR_BANK_FAILED_STATUS = 5;
    int BUY_CANCELD_STATUS = 6;
    int UNKNWON_STATUS = 8;
    int ALL_STATUS = -1;
}
