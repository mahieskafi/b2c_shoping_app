package com.srp.ewayspanel.model.storepage.filter

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by Eskafi on 10/27/2019.
 */
data class ProductInfo(

        @SerializedName("Id")
        val id: Int = 0,

        @SerializedName("Name")
        val name: String? = "",

        @SerializedName("SeoName")
        val seoName: String = "",

        @SerializedName("Price")
        val price: Long = 0,

        @SerializedName("OldPrice")
        val oldPrice: Long = 0,

        @SerializedName("ImageUrl")
        val imageUrl: String = "",

        @SerializedName("Stock")
        val stock: Int = 0,

        @SerializedName("Availability")
        val availability: Boolean,

        @SerializedName("MinOrder")
        val minOrder: Int = 0,

        @SerializedName("MaxOrder")
        val maxOrder: Int = 0,

        @SerializedName("OverInventoryCount")
        val overInventoryCount: Int = 0,

        @SerializedName("Point")
        val point: Long = 0,

        @SerializedName("Discount")
        val discount: Long = 0,

        @SerializedName("LawId")
        val lawId: Int = 0,

        @SerializedName("IsSim")
        val isSim: Boolean = false,

        @SerializedName("BrandId")
        var brandId: Int = 0,

        @SerializedName("BrandName")
        var brandName: String = "",

        var count: Int = 0
) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readValue(Int::class.java.classLoader) as Int,
                parcel.readString(),
                parcel.readString()!!,
                parcel.readValue(Int::class.java.classLoader) as Long,
                parcel.readValue(Int::class.java.classLoader) as Long,
                parcel.readString()!!,
                parcel.readValue(Int::class.java.classLoader) as Int,
                parcel.readByte() != 0.toByte(),
                parcel.readValue(Int::class.java.classLoader) as Int,
                parcel.readValue(Int::class.java.classLoader) as Int,
                parcel.readValue(Int::class.java.classLoader) as Int,
                parcel.readValue(Int::class.java.classLoader) as Long,
                parcel.readValue(Int::class.java.classLoader) as Long,
                parcel.readValue(Int::class.java.classLoader) as Int,
                parcel.readValue(Boolean::class.java.classLoader) as Boolean,
                parcel.readValue(Int::class.java.classLoader) as Int)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(seoName)
        parcel.writeValue(price)
        parcel.writeValue(oldPrice)
        parcel.writeString(imageUrl)
        parcel.writeValue(stock)
        parcel.writeByte(if (availability) 1 else 0)
        parcel.writeValue(minOrder)
        parcel.writeValue(maxOrder)
        parcel.writeValue(overInventoryCount)
        parcel.writeValue(point)
        parcel.writeValue(discount)
        parcel.writeValue(lawId)
        parcel.writeValue(isSim)
        parcel.writeValue(count)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductInfo> {
        override fun createFromParcel(parcel: Parcel): ProductInfo {
            return ProductInfo(parcel)
        }

        override fun newArray(size: Int): Array<ProductInfo?> {
            return arrayOfNulls(size)
        }
    }

}
