package com.srp.eways.ui.view.receipt;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eskafi on 9/7/2019.
 */
public class Receipt implements Serializable {

    public static final int STATUS_CODE_SUCCESS = 0;
    public static final int STATUS_CODE_FAILURE = 1;
    public static final int STATUS_CODE_UNKNOWN = 2;

    public static final int TRANSACTION = 1;
    public static final int RECEIPT_CHARGE = 2;
    public static final int RECEIPT_INCREASE_DEPOSIT = 3;
    public static final int RECEIPT_INTERNET = 4;

    private String mTitleDeposit;
    private String mValueDeposit;
    private Drawable mIcon;
    private int mStatusCode;
    private List<ReceiptItem> mReceiptItems = new ArrayList<>();
    private int mReceiptType = RECEIPT_CHARGE;

    public void setTitleDeposit(String titleDeposit) {
        this.mTitleDeposit = titleDeposit;
    }

    public void setValueDeposit(String valueDeposit) {
        this.mValueDeposit = valueDeposit;
    }

    public void setIcon(Drawable icon) {
        this.mIcon = icon;
    }

    public void setReceiptItems(List<ReceiptItem> receiptItems) {
        this.mReceiptItems = receiptItems;
    }

    public void setReceiptType(int mReceiptType) {
        this.mReceiptType = mReceiptType;
    }

    public int getReceiptType() {
        return mReceiptType;
    }

    public void setStatusCode(int statusCode) {
        this.mStatusCode = statusCode;
    }

    public String getTitleDeposit() {
        return mTitleDeposit;
    }

    public String getValueDeposit() {
        return mValueDeposit;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public List<ReceiptItem> getReceiptItems() {
        return mReceiptItems;
    }

    public int getStatusCode() {
        return mStatusCode;
    }


}
