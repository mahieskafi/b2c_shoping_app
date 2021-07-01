package com.srp.eways.model.charge.result;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.List;

public class ITopupTypeItem implements Parcelable {

    public static final int AMAZING_CHARGE_IRANCELL_PRODUCT_ID = 41;

    public static class InternetPackageItem implements Parcelable {
        @Expose
        private long Parent;
        @Expose
        private String Id;
        @Expose
        private String PName;
        @Expose
        private String EName;
        @Expose
        private long Price;
        @Expose
        private long PaidPrice;

        public InternetPackageItem(Parcel in) {
            Parent = in.readLong();
            Id = in.readString();
            PName = in.readString();
            EName = in.readString();
            Price = in.readLong();
            PaidPrice = in.readLong();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(Parent);
            dest.writeString(Id);
            dest.writeString(PName);
            dest.writeString(EName);
            dest.writeLong(Price);
            dest.writeLong(PaidPrice);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<InternetPackageItem> CREATOR = new Creator<InternetPackageItem>() {
            @Override
            public InternetPackageItem createFromParcel(Parcel in) {
                return new InternetPackageItem(in);
            }

            @Override
            public InternetPackageItem[] newArray(int size) {
                return new InternetPackageItem[size];
            }
        };

        public long getParent() {
            return Parent;
        }

        public String getId() {
            return Id;
        }

        public String getPName() {
            return PName;
        }

        public String getEName() {
            return EName;
        }

        public long getPrice() {
            return Price;
        }

        public long getPaidPrice() {
            return PaidPrice;
        }

    }

    // common child fields
    @Expose
    private String PName;
    @Expose
    private String EName;
    @Expose
    private double Cofficient;
    @Expose
    private double Tax;

    // charge child fields
    @Expose
    private long ProductId;
    @Expose
    private boolean HaveAmount;
    @Expose
    private boolean HaveFixAmount;
    @Expose
    private HashMap<String, String> AmountList;

    // internetPackage child fields
    @Expose
    private long id;
    @Expose
    private List<InternetPackageItem> PackageMasterList;
    @Expose
    private List<InternetPackageItem> PackageItems;

    public ITopupTypeItem(Parcel in) {
        PName = in.readString();
        EName = in.readString();
        Cofficient = in.readDouble();
        Tax = in.readDouble();
        ProductId = in.readLong();
        HaveAmount = in.readByte() != 0;
        HaveFixAmount = in.readByte() != 0;
        AmountList = (HashMap<String, String>) in.readSerializable();
        id = in.readLong();
        PackageMasterList = in.createTypedArrayList(InternetPackageItem.CREATOR);
        PackageItems = in.createTypedArrayList(InternetPackageItem.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(PName);
        dest.writeString(EName);
        dest.writeDouble(Cofficient);
        dest.writeDouble(Tax);
        dest.writeLong(ProductId);
        dest.writeByte((byte) (HaveAmount ? 1 : 0));
        dest.writeByte((byte) (HaveFixAmount ? 1 : 0));
        dest.writeSerializable(AmountList);
        dest.writeLong(id);
        dest.writeTypedList(PackageMasterList);
        dest.writeTypedList(PackageItems);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ITopupTypeItem> CREATOR = new Creator<ITopupTypeItem>() {
        @Override
        public ITopupTypeItem createFromParcel(Parcel in) {
            return new ITopupTypeItem(in);
        }

        @Override
        public ITopupTypeItem[] newArray(int size) {
            return new ITopupTypeItem[size];
        }
    };

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

    public double getCofficient() {
        return Cofficient;
    }

    public void setCofficient(double cofficient) {
        Cofficient = cofficient;
    }

    public double getTax() {
        return Tax;
    }

    public void setTax(double tax) {
        Tax = tax;
    }

    public long getProductId() {
        return ProductId;
    }

    public void setProductId(long productId) {
        ProductId = productId;
    }

    public boolean haveAmount() {
        return HaveAmount;
    }

    public void setHaveAmount(boolean haveAmount) {
        HaveAmount = haveAmount;
    }

    public boolean haveFixAmount() {
        return HaveFixAmount;
    }

    public void setHaveFixAmount(boolean haveFixAmount) {
        HaveFixAmount = haveFixAmount;
    }

    public HashMap<String, String> getAmountList() {
        return AmountList;
    }

    public void setAmountList(HashMap<String, String> amountList) {
        AmountList = amountList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<InternetPackageItem> getPackageMasterList() {
        return PackageMasterList;
    }

    public void setPackageMasterList(List<InternetPackageItem> packageMasterList) {
        PackageMasterList = packageMasterList;
    }

    public List<InternetPackageItem> getPackageItems() {
        return PackageItems;
    }

    public void setPackageItems(List<InternetPackageItem> packageItems) {
        PackageItems = packageItems;
    }

}
