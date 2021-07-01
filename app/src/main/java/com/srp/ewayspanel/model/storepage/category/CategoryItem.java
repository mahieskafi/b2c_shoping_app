package com.srp.ewayspanel.model.storepage.category;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.srp.ewayspanel.model.storepage.tabbar.Resource;
import com.srp.ewayspanel.repository.storepage.category.CategoryTreeNode;

import java.util.ArrayList;
import java.util.List;

public class CategoryItem implements Parcelable {

    @Expose
    @SerializedName("CategoryId")
    private Long categoryId;

    @Expose
    @SerializedName("Title")
    private String title;

    @Expose
    @SerializedName("SeoTitle")
    private String seoTitle;

    @Expose
    @SerializedName("ProductTags")
    private String productTags;

    @Expose
    @SerializedName("ProductTagList")
    private List<Integer> productTagList;

    @Expose
    @SerializedName("ParentId")
    private Long parentId;

    @Expose
    @SerializedName("ParentIDTitle")
    private String parentIDTitle;

    @Expose
    @SerializedName("ChildGroupCount")
    private Integer childGroupCount;

    @Expose
    @SerializedName("BaseProductTag")
    private Integer baseProductTag;

    @Expose
    @SerializedName("CategoryPicture")
    private String categoryPicture;

    @Expose
    @SerializedName("ChannelType")
    private Integer channelType;

    @Expose
    @SerializedName("DefaultResourceJson")
    private String defaultResourceJson;

    @Expose
    @SerializedName("hasChildren")
    private Boolean hasChildren;

    @Expose
    @SerializedName("InStock")
    private Integer inStock;

    @Expose
    @SerializedName("IsVisible")
    private Boolean isVisible;

    @Expose
    @SerializedName("Priority")
    private Integer priority;

    @Expose
    @SerializedName("ProductCount")
    private Integer productCount;

    @Expose
    @SerializedName("ResourceJson")
    private String resourceJson;

    @Expose
    @SerializedName("Resources")
    private List<Resource> resources;

    @Expose
    @SerializedName("Status")
    private Integer status;

    @Expose
    @SerializedName("StatusIsVisible")
    private Boolean statusIsVisible;

    @Expose
    @SerializedName("Depth")
    private Integer depth;

    @Expose
    @SerializedName("Path")
    private long[] path;

    private boolean mExpanded;

    private List<CategoryItem> mChildren = new ArrayList<>();

    private boolean mIsExpandedLoading;

    protected CategoryItem(Parcel in) {
        if (in.readByte() == 0) {
            categoryId = null;
        } else {
            categoryId = in.readLong();
        }
        title = in.readString();
        seoTitle = in.readString();
        productTags = in.readString();

        in.readList(productTagList, null);

        if (in.readByte() == 0) {
            parentId = null;
        } else {
            parentId = in.readLong();
        }
        parentIDTitle = in.readString();
        if (in.readByte() == 0) {
            childGroupCount = null;
        } else {
            childGroupCount = in.readInt();
        }
        if (in.readByte() == 0) {
            baseProductTag = null;
        } else {
            baseProductTag = in.readInt();
        }
        categoryPicture = in.readString();
        if (in.readByte() == 0) {
            channelType = null;
        } else {
            channelType = in.readInt();
        }
        defaultResourceJson = in.readString();
        byte tmpHasChildren = in.readByte();
        hasChildren = tmpHasChildren == 0 ? null : tmpHasChildren == 1;
        if (in.readByte() == 0) {
            inStock = null;
        } else {
            inStock = in.readInt();
        }
        byte tmpIsVisible = in.readByte();
        isVisible = tmpIsVisible == 0 ? null : tmpIsVisible == 1;
        if (in.readByte() == 0) {
            priority = null;
        } else {
            priority = in.readInt();
        }
        if (in.readByte() == 0) {
            productCount = null;
        } else {
            productCount = in.readInt();
        }
        resourceJson = in.readString();
        resources = in.createTypedArrayList(Resource.CREATOR);
        if (in.readByte() == 0) {
            status = null;
        } else {
            status = in.readInt();
        }
        byte tmpStatusIsVisible = in.readByte();
        statusIsVisible = tmpStatusIsVisible == 0 ? null : tmpStatusIsVisible == 1;
        if (in.readByte() == 0) {
            depth = null;
        } else {
            depth = in.readInt();
        }
        mExpanded = in.readByte() != 0;
        mChildren = in.createTypedArrayList(CategoryItem.CREATOR);
        path = in.createLongArray();
        mIsExpandedLoading = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (categoryId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(categoryId);
        }
        dest.writeString(title);
        dest.writeString(seoTitle);
        dest.writeString(productTags);

        dest.writeList(productTagList);

        if (parentId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(parentId);
        }
        dest.writeString(parentIDTitle);
        if (childGroupCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(childGroupCount);
        }
        if (baseProductTag == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(baseProductTag);
        }
        dest.writeString(categoryPicture);
        if (channelType == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(channelType);
        }
        dest.writeString(defaultResourceJson);
        dest.writeByte((byte) (hasChildren == null ? 0 : hasChildren ? 1 : 2));
        if (inStock == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(inStock);
        }
        dest.writeByte((byte) (isVisible == null ? 0 : isVisible ? 1 : 2));
        if (priority == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(priority);
        }
        if (productCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(productCount);
        }
        dest.writeString(resourceJson);
        dest.writeTypedList(resources);
        if (status == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(status);
        }
        dest.writeByte((byte) (statusIsVisible == null ? 0 : statusIsVisible ? 1 : 2));
        if (depth == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(depth);
        }
        dest.writeByte((byte) (mExpanded ? 1 : 0));
        dest.writeTypedList(mChildren);
        dest.writeLongArray(path);
        dest.writeByte((byte) (mIsExpandedLoading ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryItem> CREATOR = new Creator<CategoryItem>() {
        @Override
        public CategoryItem createFromParcel(Parcel in) {
            return new CategoryItem(in);
        }

        @Override
        public CategoryItem[] newArray(int size) {
            return new CategoryItem[size];
        }
    };

    public Integer getBaseProductTag() {
        return baseProductTag;
    }

    public void setBaseProductTag(Integer baseProductTag) {
        this.baseProductTag = baseProductTag;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryPicture() {
        return categoryPicture;
    }

    public void setCategoryPicture(String categoryPicture) {
        this.categoryPicture = categoryPicture;
    }

    public Integer getChannelType() {
        return channelType;
    }

    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }

    public Integer getChildGroupCount() {
        return childGroupCount;
    }

    public void setChildGroupCount(Integer childGroupCount) {
        this.childGroupCount = childGroupCount;
    }

    public String getDefaultResourceJson() {
        return defaultResourceJson;
    }

    public void setDefaultResourceJson(String defaultResourceJson) {
        this.defaultResourceJson = defaultResourceJson;
    }

    public Boolean getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public Integer getInStock() {
        return inStock;
    }

    public void setInStock(Integer inStock) {
        this.inStock = inStock;
    }

    public Boolean isVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }

    public String getParentIDTitle() {
        return parentIDTitle;
    }

    public void setParentIDTitle(String parentIDTitle) {
        this.parentIDTitle = parentIDTitle;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public List<Integer> getProductTagList() {
        return productTagList;
    }

    public void setProductTagList(List<Integer> productTagList) {
        this.productTagList = productTagList;
    }

    public String getProductTags() {
        return productTags;
    }

    public void setProductTags(String productTags) {
        this.productTags = productTags;
    }

    public String getResourceJson() {
        return resourceJson;
    }

    public void setResourceJson(String resourceJson) {
        this.resourceJson = resourceJson;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getStatusIsVisible() {
        return statusIsVisible;
    }

    public void setStatusIsVisible(Boolean statusIsVisible) {
        this.statusIsVisible = statusIsVisible;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDepth() {
        return depth;
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
    }

    public List<CategoryItem> getChildren() {
        return mChildren;
    }

    public void setChildren(List<CategoryItem> mChildren) {
        this.mChildren = mChildren;
    }

    public boolean isExpandedLoading() {
        return mIsExpandedLoading;
    }

    public void setExpandedLoading(boolean isExpandedLoading) {
        mIsExpandedLoading = isExpandedLoading;
    }

    public long[] getPath() {
        return path;
    }

    public void setPath(long[] path) {
        this.path = path;
    }
}
