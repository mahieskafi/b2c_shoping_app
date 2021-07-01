package com.srp.ewayspanel.model.storepage.tabbar;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Resource implements Parcelable {

    @Expose
    @SerializedName("CategoryId")
    private Long categoryId;

    @Expose
    @SerializedName("CreateDate")
    private String createDate;

    @Expose
    @SerializedName("Culture")
    private String culture;

    @Expose
    @SerializedName("Id")
    private Long id;

    @Expose
    @SerializedName("Title")
    private String title;

    protected Resource(Parcel in) {
        if (in.readByte() == 0) {
            categoryId = null;
        } else {
            categoryId = in.readLong();
        }
        createDate = in.readString();
        culture = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (categoryId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(categoryId);
        }
        dest.writeString(createDate);
        dest.writeString(culture);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Resource> CREATOR = new Creator<Resource>() {
        @Override
        public Resource createFromParcel(Parcel in) {
            return new Resource(in);
        }

        @Override
        public Resource[] newArray(int size) {
            return new Resource[size];
        }
    };

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCulture() {
        return culture;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
