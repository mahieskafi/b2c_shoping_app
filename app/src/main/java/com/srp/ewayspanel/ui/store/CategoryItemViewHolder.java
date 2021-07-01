package com.srp.ewayspanel.ui.store;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.srp.eways.base.BaseViewHolder;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.storepage.category.CategoryItem;
import com.srp.ewayspanel.repository.storepage.category.CategoryTreeNode;
import com.srp.ewayspanel.ui.view.store.CategoryItemView;
import com.srp.ewayspanel.ui.view.store.CategoryItemViewListener;

public class CategoryItemViewHolder extends BaseViewHolder<CategoryItem, CategoryItemView> {

    public static CategoryItemViewHolder createViewHolder(Context context, CategoryItemViewListener listener, int itemViewType) {
        CategoryItemView categoryItemView = new CategoryItemView(context, itemViewType);

        if (itemViewType == 0) {
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            marginLayoutParams.topMargin = DI.getABResources().getDimenPixelSize(R.dimen.store_categoryitem_marginright);
            categoryItemView.setLayoutParams(marginLayoutParams);
        }

        return new CategoryItemViewHolder(categoryItemView, listener);
    }

    private CategoryItemViewHolder(@NonNull CategoryItemView itemView, CategoryItemViewListener listener) {
        super(itemView);

        itemView.setListener(listener);
    }

    @Override
    public void onBind(CategoryItem item) {
        ((CategoryItemView) itemView).setData(item);
    }

}
