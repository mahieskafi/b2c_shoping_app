package com.srp.ewayspanel.ui.store;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.srp.eways.base.BaseViewHolder;
import com.srp.eways.ui.charge.model.IChargeChoice;
import com.srp.ewayspanel.model.storepage.category.CategoryItem;
import com.srp.ewayspanel.repository.storepage.category.CategoryTreeNode;
import com.srp.ewayspanel.ui.base.BaseRecyclerViewAdapter;
import com.srp.ewayspanel.ui.view.store.CategoryItemViewListener;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CategoryListAdapter extends BaseRecyclerViewAdapter<CategoryItem> {

    private static final int FIRST_ITEM_VIEW_TYPE = 0;
    private static final int OTHERS_ITEM_VIEW_TYPE = 1;
    private static final int LAST_ITEM_VIEW_TYPE = 2;

    private List<CategoryItem> mTreeRootNode;

    private CategoryItemViewListener mListener;

    public CategoryListAdapter(Context context, CategoryItemViewListener listener) {
        super(context);

        mListener = listener;
    }

    public void setData(List<CategoryItem> rootNode) {
        mTreeRootNode = rootNode;

        notifyDataSetChanged();
    }

    public List<CategoryItem> getData() {
        return mTreeRootNode;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return FIRST_ITEM_VIEW_TYPE;
        }
        return OTHERS_ITEM_VIEW_TYPE;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return CategoryItemViewHolder.createViewHolder(parent.getContext(), mListener, viewType);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        ((CategoryItemViewHolder) holder).onBind(getItemAtPosition(position));
    }

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder holder) {
        ((CategoryItemViewHolder) holder).onBind(null);
    }

    public void expandCategory(List<CategoryItem> categoryItemList, long parentId) {
        for (int i = 0; i < mTreeRootNode.size(); i++) {

            mTreeRootNode.get(i).setExpanded(false);
            if (mTreeRootNode.get(i).getCategoryId().equals(parentId)) {
                mTreeRootNode.get(i).setExpanded(true);
                mTreeRootNode.get(i).setExpandedLoading(false);
                mTreeRootNode.get(i).setChildren(categoryItemList);
            }
        }
        notifyDataSetChanged();

    }

    public void showExpandLoading(long categoryId) {
        for (int i = 0; i < mTreeRootNode.size(); i++) {

            mTreeRootNode.get(i).setExpandedLoading(false);
            if (mTreeRootNode.get(i).getCategoryId().equals(categoryId)) {
                mTreeRootNode.get(i).setExpandedLoading(true);
            }
        }
        notifyDataSetChanged();

    }

    public void onSubCategorySelected(CategoryItem categoryItem, CategoryItem subCategoryItem) {
        if (categoryItem.isExpanded()) {
            boolean isExpanded = subCategoryItem.isExpanded();

            for (int i = 0; i < mTreeRootNode.size(); i++) {
                if (mTreeRootNode.get(i).getChildren() != null) {

                    for (int j = 0; j < mTreeRootNode.get(i).getChildren().size(); j++) {

                        for (long id : subCategoryItem.getPath()) {
                            if (mTreeRootNode.get(i).getChildren().get(j).getCategoryId().equals(id)) {
                                mTreeRootNode.get(i).getChildren().get(j).setExpanded(true);
                                break;
                            }
                            mTreeRootNode.get(i).getChildren().get(j).setExpanded(false);
                        }

                        ListIterator<CategoryItem> iterator = mTreeRootNode.get(i).getChildren().listIterator();
                        while (iterator.hasNext()) {
                            if (iterator.next().getDepth() > subCategoryItem.getDepth()) {
                                iterator.remove();
                            }
                        }

                    }
                    if (mTreeRootNode.get(i).getCategoryId().equals(categoryItem.getCategoryId())) {
                        int selectedIndex = mTreeRootNode.get(i).getChildren().indexOf(subCategoryItem);
                        if (!isExpanded) {
                            mTreeRootNode.get(i).getChildren().get(selectedIndex).setExpanded(true);
                        } else {
                            mTreeRootNode.get(i).getChildren().get(selectedIndex).setExpanded(false);
                        }

                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    private CategoryItem getItemAtPosition(int position) {
        return mTreeRootNode.get(position);
    }

    @Override
    public int getItemCount() {
        return mTreeRootNode.size();
    }

}
