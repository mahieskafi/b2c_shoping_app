package com.srp.ewayspanel.ui.view.store;

import com.srp.ewayspanel.model.storepage.category.CategoryItem;
import com.srp.ewayspanel.repository.storepage.category.CategoryTreeNode;

public interface CategoryItemViewListener {

    void onSeeAllProductsFromCategory(CategoryItem categoryTreeNode);

    void onCategoryHeaderClicked(CategoryItem categoryTreeNode);

    void onCategorySubItemClicked(CategoryItem categoryItem, CategoryItem categorySubItem, int index);

    void onCategorySubItemArrowClicked(CategoryItem categoryItem, CategoryItem categorySubItem, int index);

}