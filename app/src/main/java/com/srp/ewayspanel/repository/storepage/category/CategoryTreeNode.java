package com.srp.ewayspanel.repository.storepage.category;

import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.ColorInt;

import com.srp.ewayspanel.model.storepage.category.ICategoryItem;
import com.srp.ewayspanel.model.storepage.category.CategoryItem;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class CategoryTreeNode implements Parcelable, ICategoryItem {

    public CategoryTreeNode findCategoryTreeNode(long categoryRootNodeId) {
        if (mInstance == null) {
            return null;
        }

        if (!mInstance.hasChildren()) {
            return null;
        }

        for (CategoryTreeNode child : mInstance.getChildren()) {
            if (child.getCategoryId() == categoryRootNodeId) {
                return child;
            }
        }

        return null;
    }


    public interface CopyCallback {

        void onCopied(CategoryTreeNode copy);

    }

    private static CategoryTreeNode mInstance = null;

    public static CategoryTreeNode get() {
        return mInstance;
    }

    public static CategoryTreeNode getCopy() {
        return get().fork();
    }

    public static void getCopy(final CopyCallback callback) {
        new AsyncTask<Void, Void, CategoryTreeNode>() {

            @Override
            protected CategoryTreeNode doInBackground(Void... voids) {
                return getCopy();
            }

            @Override
            protected void onPostExecute(CategoryTreeNode categoryTreeNode) {
                callback.onCopied(categoryTreeNode);
            }

        }.execute();
    }

    private CategoryTreeNode() {

    }

    private CategoryTreeNode fork() {
        CategoryTreeNode copy = new CategoryTreeNode();

        copy.mChildren = new ArrayList<>(mChildren.size());

        for (CategoryTreeNode child : mChildren) {
            CategoryTreeNode childCopy = child.fork();

            childCopy.setParent(this);

            copy.mChildren.add(childCopy);
        }

        copy.mDepth = mDepth;
        copy.mCategoryItem = mCategoryItem;
        copy.mExpanded = false;
        copy.mIsInPath = false;
        copy.mSelectionState = SelectionState.UNSELECTED;
        copy.mBoxType = BoxType.STANDARD;
        copy.mCategoryItemColor = mCategoryItemColor;

        return copy;
    }

    public enum SelectionState {

        UNSELECTED,

        IN_SELECTIONPATH,

        SELECTED

    }

    public enum ToggleState {

        EXPANDED,

        COLLAPSED

    }

    public enum BoxType {

        STANDARD,

        TOP,

        MIDDLE,

        BOTTOM

    }

    private CategoryTreeNode mParent;

    private List<CategoryTreeNode> mChildren = new ArrayList<>();

    private int mDepth;

    private CategoryItem mCategoryItem;

    private boolean mExpanded = false;

    private boolean mIsInPath = false;

    private @ColorInt
    int mCategoryItemColor;

    private SelectionState mSelectionState = SelectionState.UNSELECTED;

    private BoxType mBoxType = BoxType.STANDARD;

    public CategoryTreeNode(List<CategoryItem> categoryItems) {
        mParent = null;
        mCategoryItem = null;

        mDepth = -1;

        List<CategoryItem> remainedItems = new ArrayList<>(categoryItems);
        takeChildren(remainedItems);

        mInstance = this;
    }

    private CategoryTreeNode(CategoryItem item, CategoryTreeNode parent, int depth) {
        mParent = parent;
        mCategoryItem = item;

        mDepth = depth;
    }

    protected CategoryTreeNode(Parcel in) {
//        mParent = in.readParcelable(CategoryTreeNode.class.getClassLoader());
        mChildren = in.createTypedArrayList(CategoryTreeNode.CREATOR);
        mDepth = in.readInt();
        mCategoryItem = in.readParcelable(CategoryItem.class.getClassLoader());
        mExpanded = in.readByte() != 0;

        if (mChildren != null) {
            for (CategoryTreeNode child : mChildren) {
                child.setParent(this);
            }
        }
    }

    private void setParent(CategoryTreeNode parent) {
        mParent = parent;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeParcelable(mParent, flags);
        dest.writeTypedList(mChildren);
        dest.writeInt(mDepth);
        dest.writeParcelable(mCategoryItem, flags);
        dest.writeByte((byte) (mExpanded ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryTreeNode> CREATOR = new Creator<CategoryTreeNode>() {
        @Override
        public CategoryTreeNode createFromParcel(Parcel in) {
            return new CategoryTreeNode(in);
        }

        @Override
        public CategoryTreeNode[] newArray(int size) {
            return new CategoryTreeNode[size];
        }
    };

    public CategoryTreeNode getParent() {
        return mParent;
    }

    public List<CategoryTreeNode> getChildren() {
        return mChildren;
    }

    public CategoryItem getItem() {
        return mCategoryItem;
    }

    public Long getCategoryId() {
        return mCategoryItem.getCategoryId();
    }

    public int getDepth() {
        return mDepth;
    }

    public boolean isRoot() {
        return mDepth == 0;
    }

    public void setIsInPath(boolean isInPath) {
        mIsInPath = isInPath;
    }

    public boolean isInPath() {
        return mIsInPath;
    }

    public void setSelectionState(SelectionState selectionState) {
        mSelectionState = selectionState;
    }

    public SelectionState getSelectionState() {
        return mSelectionState;
    }

    public boolean hasChildren() {
        return !mChildren.isEmpty();
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        mExpanded = expanded;
    }

    private void takeChildren(List<CategoryItem> remainedItems) {
        ListIterator<CategoryItem> listIterator = remainedItems.listIterator();

        while (listIterator.hasNext()) {
            CategoryItem categoryItem = listIterator.next();

            if ((mCategoryItem == null && categoryItem.getParentId() == 0) || (mCategoryItem != null && mCategoryItem.getCategoryId().equals(categoryItem.getParentId()))) {
                if (categoryItem.getChildGroupCount() > 0 || categoryItem.getProductCount() > 0) {
                    mChildren.add(new CategoryTreeNode(categoryItem, this, mDepth + 1));

                    listIterator.remove();

                }
            }
        }
        for (CategoryTreeNode categoryTreeNode : mChildren) {
            categoryTreeNode.takeChildren(remainedItems);
        }
    }

    @Override
    public String getTitle() {
        return mCategoryItem.getTitle();
    }

    @Override
    public void setColor(@ColorInt int color) {
        mCategoryItemColor = color;

        int childCount = mChildren != null ? mChildren.size() : 0;

        if (childCount > 0) {
            for (int i = 0; i < childCount; ++i) {
                mChildren.get(i).setColor(color);
            }
        }
    }

    @Override
    public int getColor() {
        return mCategoryItemColor;
    }

    public void setBoxType(BoxType boxType) {
        mBoxType = boxType;
    }

    public BoxType getBoxType() {
        return mBoxType;
    }

}
