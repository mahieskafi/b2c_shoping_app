package com.srp.eways.ui.confirmtransaction;

import android.graphics.drawable.Drawable;

import com.srp.eways.ui.view.receipt.ReceiptItem;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Eskafi on 9/14/2019.
 */
public class ConfirmTransaction implements Serializable {

    private String mTitle;
    private String mTitlePaidPrice;
    private String mValuePaidPrice;
    private Drawable mIcon;
    private List<ReceiptItem> mReceiptItems;
    private String mPayButtonText;
    private String mCancelButtonText;

    public String getTitlePaidPrice() {
        return mTitlePaidPrice;
    }

    public String getValuePaidPrice() {
        return mValuePaidPrice;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public List<ReceiptItem> getReceiptItems() {
        return mReceiptItems;
    }

    public String getPayButtonText() {
        return mPayButtonText;
    }

    public String getCancelButtonText() {
        return mCancelButtonText;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setTitlePaidPrice(String titlePaidPrice) {
        mTitlePaidPrice = titlePaidPrice;
    }

    public void setValuePaidPrice(String valuePaidPrice) {
        mValuePaidPrice = valuePaidPrice;
    }

    public void setIcon(Drawable icon) {
        mIcon = icon;
    }

    public void setReceiptItems(List<ReceiptItem> receiptItems) {
        mReceiptItems = receiptItems;
    }

    public void setPayButtonText(String payButtonText) {
        mPayButtonText = payButtonText;
    }

    public void setCancelButtonText(String cancelButtonText) {
        mCancelButtonText = cancelButtonText;
    }
}
