package com.srp.ewayspanel.repository.storepage;

import com.srp.ewayspanel.model.storepage.category.CategoryItem;
import com.srp.ewayspanel.model.storepage.category.CategoryListResponse;
import com.srp.ewayspanel.repository.storepage.category.CategoryTreeNode;

import java.util.List;

public interface StorePageRepository {

    interface CategoryDataReadyListener {

        void onCategoryDataReady(CategoryListResponse categoryListResponse);
    }

    interface CategoryRawDataReadyListener {

        void onCategoryDataReady(CategoryData categoryData);
    }

    void getCategoryList(long parentId, CategoryDataReadyListener callback);

    void getCategoryRawList(CategoryRawDataReadyListener callback);

    class CategoryData {

        public final List<CategoryItem> categoryRawList;

        public final CategoryTreeNode categoryTree;

        public final int status;

        public final String errorMessage;

        public CategoryData(List<CategoryItem> categoryRawList, CategoryTreeNode categoryTree, int status, String errorMessage) {
            this.categoryRawList = categoryRawList;
            this.categoryTree = categoryTree;

            this.status = status;
            this.errorMessage = errorMessage;
        }

    }

}
