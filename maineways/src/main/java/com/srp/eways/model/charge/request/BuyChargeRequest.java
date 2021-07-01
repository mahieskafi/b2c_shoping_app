package com.srp.eways.model.charge.request;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BuyChargeRequest implements Parcelable {

    @Expose
    @SerializedName("ProductId")
    private Long mProductId;

    @Expose
    @SerializedName("Package")
    private String mPackage;

    @Expose
    @SerializedName("Amount")
    private long mAmount;

    @Expose
    @SerializedName("PaidAmount")
    private long mPaidAmount;

    @Expose
    @SerializedName("ProductType")
    private int mProductType;

    @Expose
    @SerializedName("Number")
    private String mNumber;

    @Expose
    @SerializedName("mPackageName")
    private String mPackageName;

    @Expose
    @SerializedName("Gateway")
    private int mGetway;

    public BuyChargeRequest(long productId, String mpackage, long amount, long paidmAmount, int productType,
                            String number, String packageName, int getway) {
        mProductId = productId;
        this.mPackage = mpackage;
        mAmount = amount;
        mPaidAmount = paidmAmount;
        mProductType = productType;
        mNumber = number;
        mPackageName = packageName;
        mGetway = getway;
    }

    public BuyChargeRequest(Parcel in) {
        mProductId = in.readLong();
        mPackage = in.readString();
        mAmount = in.readLong();
        mPaidAmount = in.readLong();
        mProductType = in.readInt();
        mNumber = in.readString();
        mPackageName = in.readString();
        mGetway = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mProductId);
        dest.writeString(mPackage);
        dest.writeLong(mAmount);
        dest.writeLong(mPaidAmount);
        dest.writeInt(mProductType);
        dest.writeString(mNumber);
        dest.writeString(mPackageName);
        dest.writeInt(mGetway);
    }

//    public Receipt getChargeInfo(Context context, String operatorName) {
//        Receipt.Builder chargeInfo = new Receipt.Builder();
//
//        chargeInfo.setMessage(context.getString(R.string.chargeinfo_title));
//
//        String productTypeString = null;
//        switch (mProductType) {
//            case TopupTypeItem.TYPE_CHARGE:
//                productTypeString = context.getString(R.string.topup_type_directcharge);
//                break;
//
//            case TopupTypeItem.TYPE_INTERNET:
//                productTypeString = context.getString(R.string.topup_type_internetmPackage);
//                break;
//        }
//
//        chargeInfo.addItem(context.getString(R.string.chargeinfo_topuptype_title), productTypeString);
//        chargeInfo.addItem(context.getString(R.string.chargeinfo_phonenumber_title), Utils.toPersianmNumber(mNumber));
//        chargeInfo.addItem(context.getString(R.string.chargeinfo_operator_title), operatorName);
//        switch (mProductType) {
//            case TopupTypeItem.TYPE_CHARGE:
//                chargeInfo.addItem(context.getString(R.string.chargeinfo_price_title), String.format(context.getString(R.string.rial_mAmount_string), Utils.toPersianmNumber(Utils.addThousandSeparator(mAmount))));
//                break;
//
//            case TopupTypeItem.TYPE_INTERNET:
//                chargeInfo.addItem(context.getString(R.string.internetinfo_price_title), String.format(context.getString(R.string.rial_mAmount_string), Utils.toPersianmNumber(Utils.addThousandSeparator(mAmount))));
//                chargeInfo.addItem(context.getString(R.string.internetinfo_price_name), Utils.toPersianmNumber(mmPackageName));
//                break;
//        }
//        chargeInfo.addItem(context.getString(R.string.chargeinfo_paidprice_title), String.format(context.getString(R.string.rial_mAmount_string), Utils.toPersianmNumber(Utils.addThousandSeparator(mPaidAmount))));
//        //Todo: what is this?!
////        chargeInfo.addItem("مبلغ دریافتی از مشتری", String.format(context.getString(R.string.rial_mAmount_string), Utils.toPersianmNumber(Utils.addThousandSeparator(mPaidAmount))));
//
//        return chargeInfo.build();
//    }

    public long getProductId() {
        return mProductId;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public String getPackage() {
        return mPackage;
    }

    public long getAmount() {
        return mAmount;
    }

    public long getPaidAmount() {
        return mPaidAmount;
    }

    public int getProductType() {
        return mProductType;
    }

    public String getNumber() {
        return mNumber;
    }

    public int getGetway() {
        return mGetway;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BuyChargeRequest> CREATOR = new Creator<BuyChargeRequest>() {
        @Override
        public BuyChargeRequest createFromParcel(Parcel in) {
            return new BuyChargeRequest(in);
        }

        @Override
        public BuyChargeRequest[] newArray(int size) {
            return new BuyChargeRequest[size];
        }
    };

}
