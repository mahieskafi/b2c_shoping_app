package com.srp.eways.model.login


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by ErfanG on 8/7/2019.
 */
data class UserInfo(
        @SerializedName("Address")
        @Expose
        val address: String? = "",

        @SerializedName("BirthDate")
        @Expose
        val birthDate: String? = "",

        @SerializedName("BlockedCredit")
        @Expose
        val blockedCredit: Int? = 0,

        @SerializedName("Credit")
        @Expose
        val credit: Long = 0,

        @SerializedName("CrmUserId")
        @Expose
        val crmUserId: Long? = 0,

        @SerializedName("CrmUserName")
        @Expose
        val crmUserName: String? = "",

        @SerializedName("Description")
        @Expose
        val description: String? = "",

        @SerializedName("Email")
        @Expose
        val email: String? = "",

        @SerializedName("FirstName")
        @Expose
        val firstName: String? = "",

        @SerializedName("FullName")
        @Expose
        val fullName: String? = "",

        @SerializedName("Gender")
        @Expose
        val gender: Int? = 0,

        @SerializedName("IsCreditUser")
        @Expose
        val isCreditUser: Boolean? = false,

        @SerializedName("IsOneTimePassword")
        @Expose
        val isOneTimePassword: Boolean? = false,

        @SerializedName("LastLogDate")
        @Expose
        val lastLogDate: String? = "",

        @SerializedName("LastName")
        @Expose
        val lastName: String? = "",

        @SerializedName("LoyaltyScore")
        @Expose
        val loyaltyScore: Long? = 0,

        @SerializedName("Mobile")
        @Expose
        val mobile: String? = "",

        @SerializedName("NationalId")
        @Expose
        val nationalId: String? = "",

        @SerializedName("NotificationCount")
        @Expose
        val notificationCount: Int? = 0,

        @SerializedName("ParentId")
        @Expose
        val parentId: Int? = 0,

        @SerializedName("Phone")
        @Expose
        val phone: String? = "",

        @SerializedName("PostCode")
        @Expose
        val postCode: String? = "",

        @SerializedName("ProvincialScore")
        @Expose
        val provincialScore: Int? = 0,

        @SerializedName("RegisterDate")
        @Expose
        val registerDate: String? = "",

        @SerializedName("Revenue")
        @Expose
        val revenue: Long? = 0,

        @SerializedName("RoleId")
        @Expose
        val roleId: Int? = 0,

        @SerializedName("SaleChannelsInfo")
        @Expose
        val saleChannelsInfo: List<SaleChannelsInfo>? = null,

        @SerializedName("SiteName")
        @Expose
        val siteName: String? = "",

        @SerializedName("SitePassword")
        @Expose
        val sitePassword: String? = "",

        @SerializedName("StateId")
        @Expose
        val stateId: Int = 0,

        @SerializedName("StateName")
        @Expose
        val stateName: String? = "",

        @SerializedName("Status")
        @Expose
        val status: Int? = 0,

        @SerializedName("SubPartId")
        @Expose
        val subPartId: Int? = 0,

        @SerializedName("TownId")
        @Expose
        val townId: Int = 0,

        @SerializedName("TownName")
        @Expose
        val townName: String? = "",

        @SerializedName("UpdateDate")
        @Expose
        val updateDate: String? = "",

        @SerializedName("UserId")
        @Expose
        val userId: Long? = 0,

        @SerializedName("UserName")
        @Expose
        val userName: String? = ""

)