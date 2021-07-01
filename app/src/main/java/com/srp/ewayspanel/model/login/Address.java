package com.srp.ewayspanel.model.login;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by Eskafi on 12/24/2019.
 */
@Entity(tableName = "address")
public class Address implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int addressId;
    private String addressName;
    private String fullName;
    private String phoneNumber;
    private String mobile;
    private String postCode;
    private int stateId;
    private String stateName;
    private int cityId;
    private String cityName;
    private String address;
    private Long userId;


    public String getAddressName() {
        return addressName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getStateName() {
        return stateName;
    }

    public String getCityName() {
        return cityName;
    }

    public String getAddress() {
        return address;
    }

    public int getAddressId() {
        return addressId;
    }

    public Long getUserId() {
        return userId;
    }

    public int getStateId() {
        return stateId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setAddressName(String address_name) {
        addressName = address_name;
    }

    public void setFullName(String full_name) {
        fullName = full_name;
    }

    public void setPhoneNumber(String phone_number) {
        phoneNumber = phone_number;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPostCode(String post_code) {
        postCode = post_code;
    }

    public void setStateName(String state_name) {
        stateName = state_name;
    }

    public void setCityName(String city_name) {
        cityName = city_name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAddressId(int address_id) {
        addressId = address_id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
