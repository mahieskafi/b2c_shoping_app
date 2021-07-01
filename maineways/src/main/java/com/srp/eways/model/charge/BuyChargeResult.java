package com.srp.eways.model.charge;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.charge.result.TopupTypeItem;
import com.srp.eways.ui.view.receipt.Receipt;
import com.srp.eways.ui.view.receipt.ReceiptItem;
import com.srp.eways.util.Utils;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

public class BuyChargeResult {

    @Expose
    private String ReqId;

    @Expose
    private long CustomerId;

    @Expose
    @SerializedName("Number")
    private String phoneNumber;

    @Expose
    private long Debt;//حساب دفتری مشتری ,

    @Expose
    private long Price;

    @Expose
    private long PaidPrice;//مبلغ قابل پرداخت ,

    @Expose
    private long Credit;//اعتبار ,

    @Expose
    private Integer Status;

    @Expose
    private String Description;

    public static BuyChargeResult getMoCkBuyChargeResult(String ReqId, long CustomerId, String phoneNumber, long Debt, long Price, long PaidPrice, long Credit) {
        BuyChargeResult buyChargeResult = new BuyChargeResult();
        buyChargeResult.ReqId = ReqId;
        buyChargeResult.CustomerId = CustomerId;
        buyChargeResult.phoneNumber = phoneNumber;
        buyChargeResult.Debt = Debt;
        buyChargeResult.Price = Price;
        buyChargeResult.PaidPrice = PaidPrice;
        buyChargeResult.Credit = Credit;

        return buyChargeResult;
    }

    public String getReqId() {
        return ReqId;
    }

    public long getCustomerId() {
        return CustomerId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public long getDebt() {
        return Debt;
    }

    public long getPrice() {
        return Price;
    }

    public long getPaidPrice() {
        return PaidPrice;
    }

    public long getCredit() {
        return Credit;
    }

    public Integer getStatus() {
        return Status;
    }

    public String getDescription() {
        return Description;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Receipt getReceipt(String chargeType, String transactionType, int chargeTypeId) {
        IABResources resources = DIMain.getABResources();

        Receipt receipt = new Receipt();

        receipt.setStatusCode(getStatus());

        List<ReceiptItem> receiptItemList = new ArrayList<>();
        receiptItemList.add(new ReceiptItem(resources.getString(R.string.buycharge_transactiontype), transactionType, null));
        receiptItemList.add(new ReceiptItem(resources.getString(R.string.buycharge_mobilenumber), Utils.toPersianNumber(phoneNumber), null));
        if (chargeTypeId == TopupTypeItem.TYPE_INTERNET) {
            receiptItemList.add(new ReceiptItem(resources.getString(R.string.buycharge_internrttype), chargeType, null));
            receipt.setReceiptType(Receipt.RECEIPT_INTERNET);
        } else {
            receiptItemList.add(new ReceiptItem(resources.getString(R.string.buycharge_chargetype), chargeType, null));
            receipt.setReceiptType(Receipt.RECEIPT_CHARGE);

        }

        receiptItemList.add(new ReceiptItem(resources.getString(R.string.buycharge_paymentamount),
                Utils.toPersianPriceNumber(String.valueOf(getPrice())) + " " + resources.getString(R.string.rial),
                null));

        receipt.setReceiptItems(receiptItemList);
        receipt.setValueDeposit(Utils.toPersianPriceNumber(String.valueOf(getCredit())));
        receipt.setTitleDeposit(resources.getString(R.string.buycharge_current_credit));

        return receipt;
    }

}
