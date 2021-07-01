package com.srp.eways.model.phonebook;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;

public class UserPhoneBook implements Parcelable {

    @Expose
    private long Id;

    @Expose
    private int IsDeleted;

    @Expose
    private long UserId;

    @Expose
    private long RowNumber;

    @Expose
    private String FirstName;

    @Expose
    private String LastName;

    @Expose
    private String CellPhoneShow;

    @Expose
    private String CellPhone;

    @Expose
    private String EwaysCode;

    @Expose
    private Long Debt;

    @Expose
    private Boolean IsNegativeDebt;

    @Expose
    private String Description;

    @Expose
    private String CreateDate;

    @Expose
    private String UpdateDate;

    private Boolean isShowMore;

    public UserPhoneBook(String firstName, String lastName, String cellPhone, Long debt, String description) {
        FirstName = firstName;
        LastName = lastName;
        CellPhone = cellPhone;
        Debt = debt;
        Description = description;
        isShowMore = false;
    }

    public UserPhoneBook(int id, String firstName, String lastName, String cellPhone, Long debt, String description) {
        Id = id;
        FirstName = firstName;
        LastName = lastName;
        CellPhone = cellPhone;
        Debt = debt;
        Description = description;
        isShowMore = false;
    }

    public UserPhoneBook(String name, String phone) {
        this.FirstName = name;
        this.LastName = "";
        this.CellPhone = phone;
        isShowMore = false;
    }

    protected UserPhoneBook(Parcel in) {
        Id = in.readLong();
        IsDeleted = in.readInt();
        UserId = in.readLong();
        RowNumber = in.readLong();
        FirstName = in.readString();
        LastName = in.readString();
        CellPhoneShow = in.readString();
        CellPhone = in.readString();
        EwaysCode = in.readString();
        if (in.readByte() == 0) {
            Debt = null;
        } else {
            Debt = in.readLong();
        }
        byte tmpIsNegativeDebt = in.readByte();
        IsNegativeDebt = tmpIsNegativeDebt == 0 ? null : tmpIsNegativeDebt == 1;
        Description = in.readString();
        CreateDate = in.readString();
        UpdateDate = in.readString();
        byte tmpIsShowMore = in.readByte();
        isShowMore = tmpIsShowMore == 0 ? null : tmpIsShowMore == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(Id);
        dest.writeInt(IsDeleted);
        dest.writeLong(UserId);
        dest.writeLong(RowNumber);
        dest.writeString(FirstName);
        dest.writeString(LastName);
        dest.writeString(CellPhoneShow);
        dest.writeString(CellPhone);
        dest.writeString(EwaysCode);
        if (Debt == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(Debt);
        }
        dest.writeByte((byte) (IsNegativeDebt == null ? 0 : IsNegativeDebt ? 1 : 2));
        dest.writeString(Description);
        dest.writeString(CreateDate);
        dest.writeString(UpdateDate);
        dest.writeByte((byte) (isShowMore == null ? 0 : isShowMore ? 1 : 0));
    }

    public void setPhone(String phone) {
        CellPhone = phone;
    }

    public long getId() {
        return Id;
    }

    public int getIsDeleted() {
        return IsDeleted;
    }

    public long getUserId() {
        return UserId;
    }

    public long getRowNumber() {
        return RowNumber;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getCellPhoneShow() {
        return CellPhoneShow;
    }

    public String getCellPhone() {
        return CellPhone;
    }

    public String getFullName() {
        return (TextUtils.isEmpty(FirstName) ? "" : FirstName + " ") + (TextUtils.isEmpty(LastName) ? "" : LastName);
    }

    public String getEwaysCode() {
        return EwaysCode;
    }

    public Long getDebt() {
        return Debt;
    }

    public Boolean getNegativeDebt() {
        return IsNegativeDebt;
    }

    public String getDescription() {
        return Description;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setId(long id) {
        Id = id;
    }

    public void setIsDeleted(int isDeleted) {
        IsDeleted = isDeleted;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public void setRowNumber(long rowNumber) {
        RowNumber = rowNumber;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setCellPhoneShow(String cellPhoneShow) {
        CellPhoneShow = cellPhoneShow;
    }

    public void setCellPhone(String cellPhone) {
        CellPhone = cellPhone;
    }

    public void setEwaysCode(String ewaysCode) {
        EwaysCode = ewaysCode;
    }

    public void setDebt(Long debt) {
        Debt = debt;
    }

    public void setNegativeDebt(Boolean negativeDebt) {
        IsNegativeDebt = negativeDebt;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }

    public void setShowMore(Boolean showMore) {
        isShowMore = showMore;
    }

    public Boolean getShowMore() {
        if (isShowMore == null)
            return false;
        return isShowMore;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserPhoneBook> CREATOR = new Creator<UserPhoneBook>() {
        @Override
        public UserPhoneBook createFromParcel(Parcel in) {
            return new UserPhoneBook(in);
        }

        @Override
        public UserPhoneBook[] newArray(int size) {
            return new UserPhoneBook[size];
        }
    };

}
