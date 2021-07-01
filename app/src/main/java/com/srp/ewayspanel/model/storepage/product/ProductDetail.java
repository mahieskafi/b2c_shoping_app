package com.srp.ewayspanel.model.storepage.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProductDetail implements Serializable {

    @SerializedName("ParentId")
    private Long ParentId;

    @SerializedName("MinCount")
    private Long MinCount;

    @SerializedName("MaxCount")
    private Integer MaxCount;

    @SerializedName("HaveDiscount")
    private Boolean HaveDiscount;

    @SerializedName("DiscountValue")
    private Double DiscountValue;

    @SerializedName("CouponDiscount")
    private Double CouponDiscount;

    @SerializedName("GstTax")
    private Double GstTax;

    @SerializedName("PstTax")
    private Double PstTax;

    @SerializedName("Tax")
    private Double Tax;

    @SerializedName("Images")
    private List<String> Images;

    @SerializedName("Description")
    private String Description;

    @SerializedName("TranslateName")
    private String TranslateName;

    @SerializedName("TranslateDescription")
    private String TranslateDescription;

    @SerializedName("Attributes")
    private List<ProductAttribute> Attributes;

    @SerializedName("SellerId")
    private Long SellerId;

    @SerializedName("SellerName")
    private String SellerName;

    @SerializedName("SellerNameTranslate")
    private String SellerNameTranslate;

    @SerializedName("Id")
    private Long Id;

    @SerializedName("Name")
    private String Name;

    @SerializedName("SeoName")
    private String SeoName;

    @SerializedName("Price")
    private Long Price;

    @SerializedName("OldPrice")
    private Long OldPrice;

    @SerializedName("ImageUrl")
    private String ImageUrl;

    @SerializedName("Stock")
    private Long Stock;

    @SerializedName("Availability")
    private Boolean Availability;

    @SerializedName("MinOrder")
    private Long MinOrder;

    @SerializedName("MaxOrder")
    private Long MaxOrder;

    @SerializedName("OverInventoryCount")
    private Long OverInventoryCount;

    @SerializedName("Point")
    private int Point;

    @SerializedName("Discount")
    private Integer Discount;

    @SerializedName("LawId")
    private Long LawId;

    @SerializedName("IsSim")
    private Boolean IsSim;

    public Long getParentId() {
        return ParentId;
    }

    public Long getMinCount() {
        return MinCount;
    }

    public Integer getMaxCount() {
        return MaxCount;
    }

    public Boolean getHaveDiscount() {
        return HaveDiscount;
    }

    public Double getDiscountValue() {
        return DiscountValue;
    }

    public Double getCouponDiscount() {
        return CouponDiscount;
    }

    public Double getGstTax() {
        return GstTax;
    }

    public Double getPstTax() {
        return PstTax;
    }

    public Double getTax() {
        return Tax;
    }

    public List<String> getImages() {
        return Images;
    }

    public String getDescription() {
        return Description;
    }

    public String getTranslateName() {
        return TranslateName;
    }

    public String getTranslateDescription() {
        return TranslateDescription;
    }

    public List<ProductAttribute> getAttributes() {
        return Attributes;
    }

    public Long getSellerId() {
        return SellerId;
    }

    public String getSellerName() {
        return SellerName;
    }

    public String getSellerNameTranslate() {
        return SellerNameTranslate;
    }

    public Long getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getSeoName() {
        return SeoName;
    }

    public Long getPrice() {
        return Price;
    }

    public Long getOldPrice() {
        return OldPrice;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public Long getStock() {
        return Stock;
    }

    public Boolean getAvailability() {
        return Availability;
    }

    public Long getMinOrder() {
        return MinOrder;
    }

    public Long getMaxOrder() {
        return MaxOrder;
    }

    public Long getOverInventoryCount() {
        return OverInventoryCount;
    }

    public int getPoint() {
        return Point;
    }

    public Integer getDiscount() {
        return Discount;
    }

    public Long getLawId() {
        return LawId;
    }

    public Boolean getSim() {
        return IsSim;
    }

}
