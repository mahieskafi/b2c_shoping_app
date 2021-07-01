package com.srp.eways.model.charge.result;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.List;

public class TopupTypeItem implements Parcelable {

    public static final int TYPE_CHARGE = 0;
    public static final int TYPE_INTERNET = 1;
    public static final int TYPE_GROUP = 2;
    public static final int TYPE_SPECIALOFFERS = 3;
    public static final int TYPE_SPECIALOFFERS_ARJI = 2;

    @Expose
    private String PName;

    @Expose
    private String EName;

    @Expose
    private boolean IsDisabled;

    @Expose
    private int ChildType; // نوع زیر مجموعه = ['0', '1', '2'],

    @Expose
    private List<ITopupTypeItem> Childs;

    @Expose
    private List<TopupTypeItem> SubTypes;

    public static TopupTypeItem createFakeInstance(String name) {
        TopupTypeItem item = new TopupTypeItem();
        item.setPName(name);

        return item;
    }

    private TopupTypeItem() {}

    public TopupTypeItem(Parcel in) {
        PName = in.readString();
        EName = in.readString();
        IsDisabled = in.readByte() != 0;
        ChildType = in.readInt();
        Childs = in.createTypedArrayList(ITopupTypeItem.CREATOR);
        SubTypes = in.createTypedArrayList(TopupTypeItem.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(PName);
        dest.writeString(EName);
        dest.writeByte((byte) (IsDisabled ? 1 : 0));
        dest.writeInt(ChildType);
        dest.writeTypedList(Childs);
        dest.writeTypedList(SubTypes);
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

    public int getChildType() {
        return ChildType;
    }

    public void setChildType(int childType) {
        ChildType = childType;
    }

    public List<ITopupTypeItem> getChilds() {
        return Childs;
    }

    public void setChilds(List<ITopupTypeItem> childs) {
        Childs = childs;
    }

    public List<TopupTypeItem> getSubTypes() {
        return SubTypes;
    }

    public void setSubTypes(List<TopupTypeItem> subTypes) {
        SubTypes = subTypes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TopupTypeItem> CREATOR = new Creator<TopupTypeItem>() {
        @Override
        public TopupTypeItem createFromParcel(Parcel in) {
            return new TopupTypeItem(in);
        }

        @Override
        public TopupTypeItem[] newArray(int size) {
            return new TopupTypeItem[size];
        }
    };

}