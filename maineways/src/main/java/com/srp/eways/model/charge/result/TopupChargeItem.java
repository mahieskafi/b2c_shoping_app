package com.srp.eways.model.charge.result;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.DrawableRes;

import com.google.gson.annotations.Expose;

import java.util.List;

public class TopupChargeItem implements Parcelable {

    @Expose
    private String OperatorKey;

    @Expose
    private String PName;

    @Expose
    private String EName;

    @Expose
    private boolean IsDisabled;

    @Expose
    private List<String> PerfixList;

    @Expose
    private boolean HaveTransport;

    @Expose
    private long InternetProductId;

    @Expose
    private List<TopupTypeItem> Items;

    public List<TopupTypeItem> getServiceList() {
        return Items;
    }

    public TopupChargeItem(Parcel in) {
        OperatorKey = in.readString();
        PName = in.readString();
        EName = in.readString();
        IsDisabled = in.readByte() != 0;
        PerfixList = in.createStringArrayList();
        HaveTransport = in.readByte() != 0;
        InternetProductId = in.readLong();
        Items = in.createTypedArrayList(TopupTypeItem.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(OperatorKey);
        dest.writeString(PName);
        dest.writeString(EName);
        dest.writeByte((byte) (IsDisabled ? 1 : 0));
        dest.writeStringList(PerfixList);
        dest.writeByte((byte) (HaveTransport ? 1 : 0));
        dest.writeLong(InternetProductId);
        dest.writeTypedList(Items);
    }

    public String getOperatorKey() {
        return OperatorKey;
    }

    public void setOperatorKey(String operatorKey) {
        OperatorKey = operatorKey;
    }

    public String getPName() {
        return PName;
    }

    public void setPName(String PName) {
        this.PName = PName;
    }

    public String getEName() {
        return EName;
    }

    public void setEName(String EName) {
        this.EName = EName;
    }

    public boolean isDisabled() {
        return IsDisabled;
    }

    public void setDisabled(boolean disabled) {
        IsDisabled = disabled;
    }

    public List<String> getPerfixList() {
        return PerfixList;
    }

    public void setPerfixList(List<String> perfixList) {
        PerfixList = perfixList;
    }

    public boolean isHaveTransport() {
        return HaveTransport;
    }

    public void setHaveTransport(boolean haveTransport) {
        HaveTransport = haveTransport;
    }

    public long getInternetProductId() {
        return InternetProductId;
    }

    public void setInternetProductId(long internetProductId) {
        InternetProductId = internetProductId;
    }

    public List<TopupTypeItem> getItems() {
        return Items;
    }

    public void setItems(List<TopupTypeItem> items) {
        Items = items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TopupChargeItem> CREATOR = new Creator<TopupChargeItem>() {
        @Override
        public TopupChargeItem createFromParcel(Parcel in) {
            return new TopupChargeItem(in);
        }

        @Override
        public TopupChargeItem[] newArray(int size) {
            return new TopupChargeItem[size];
        }
    };

    public @DrawableRes int getOperatorResId() {
        switch (OperatorKey) {
            case "MTNTD":
            case "MTN":
//                return R.drawable.ic_irancell;
//            case "MCI":
//                return R.drawable.ic_hamraah_avval;
//            case "RIGHTELPR":
//            case "RIGHTELPO":
//                return R.drawable.ic_rightel;
        }

        return 0;
    }

}
