package com.srp.ewayspanel.ui.store;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.ewayspanel.model.storepage.category.CategoryItem;
import com.srp.ewayspanel.model.storepage.filter.FilterProductRequest;
import com.srp.ewayspanel.repository.storepage.category.CategoryTreeNode;
import com.srp.ewayspanel.ui.store.mainpage.StoreMainPage;
import com.srp.eways.util.Utils;
import com.srp.ewayspanel.ui.store.mobilelist.StoreMobileFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by ErfanG on 22/10/2019.
 */
public class StoreCategoryPagerAdapter extends FragmentStatePagerAdapter {

    private List<CategoryItem> mCategoryTree;
    private FilterProductRequest mFilterProductRequest = new FilterProductRequest();
    private int mFilteredPosition;

    private Long mDefaultItemId = -1L;

    public StoreCategoryPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(List<CategoryItem> categoryTree) {
        mCategoryTree = categoryTree;

        mFilterProductRequest = null;

        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (object instanceof StoreMobileFragment) {
            return POSITION_UNCHANGED;
        }
        return POSITION_NONE;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

        if (mDefaultItemId != -1 && position == getCount() - 2 - getSelectedPosition(mDefaultItemId)) {
            mDefaultItemId = -1L;
            fragment = StoreMobileFragment.Companion.newInstance();
        } else if (position == mCategoryTree.size()) {//ToDo: AllCategories --> go to ProductListFragment
            fragment = new StoreMainPage();
        } else {
            int currentPosition = (getCount() - 2) - position;

            if (mCategoryTree.get(currentPosition).getChildGroupCount() > 0) {

//                int currentPosition = (getCount() - 2) - position;

                if (currentPosition == mFilteredPosition && mFilterProductRequest != null) {

                    fragment = CategoryFragment.newInstance(mCategoryTree.get((getCount() - 2) - position).getCategoryId(), mFilterProductRequest);
                    mFilterProductRequest = null;
                    return fragment;
                } else {
                    return CategoryFragment.newInstance(mCategoryTree.get((getCount() - 2) - position).getCategoryId(), null);
                }
            } else {//ToDo: AllCategories --> go to ProductListFragment
                fragment = new GridProductFragment();
            }
        }

        return fragment;
    }

//    private boolean childTreeHasChildren(int position) {
//        return mCategoryTree.get((getCount() - 2) - position).hasChildren();
//    }

    @Override
    public int getCount() {
        if (mCategoryTree == null) {
            return 0;
        }

        return mCategoryTree.size() + 1;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == mCategoryTree.size()) {
            return DI.getABResources().getString(R.string.category_title_main_page);
        }

        return Utils.toPersianNumber(mCategoryTree.get((getCount() - 2) - position).getTitle());
    }

    public int getSelectedPosition(Long parentCategoryId) {
        int position = -1;

        if (mCategoryTree != null && mCategoryTree.size() > 0) {
            for (int i = mCategoryTree.size() - 1; i > 1; i--)
                if (parentCategoryId.equals(mCategoryTree.get(i).getCategoryId())) {
                    position = i;
                }
        }

        return position;
    }

    public void setFilterProductRequest(int position, FilterProductRequest filterProductRequest) {
        mFilteredPosition = position;
        mFilterProductRequest = filterProductRequest;
    }

    public void setDefaultItem(Long categoryId) {
        mDefaultItemId = categoryId;
    }

}
