package com.srp.ewayspanel.ui.store.mobilelist

import android.os.Bundle
import android.view.View
import androidx.collection.SparseArrayCompat
import com.srp.eways.base.BaseViewModel
import com.srp.eways.ui.navigation.*
import com.srp.ewayspanel.R
import com.srp.ewayspanel.ui.store.CategoryFragment

class StoreMobileFragment : NavigationFragment<BaseViewModel>() {

    private var mMenuVisibilityCount = 0

    companion object {
        private const val CATEGORY_ID_KEY = "extraCategoryId"
        private const val MOBILE_LIST_CATEGORY_ID = 4285L

        private const val CATEGORY_PAGE = 1
        private const val MOBILE_PAGE = 0

        fun newInstance(): StoreMobileFragment {
            return StoreMobileFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun acquireViewModel(): BaseViewModel {
        return BaseViewModel()
    }

    override fun getNavigationViewType(): Int {
        return -1
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_store_mobile
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (menuVisible && mMenuVisibilityCount > 1) {
            onSwitchRoot(CATEGORY_PAGE)
        } else
            if (!menuVisible) {
                mMenuVisibilityCount++
            }
    }

    override fun getRootTabId(): Int {
        return MOBILE_PAGE
    }

    override fun createNavigationRoots(): SparseArrayCompat<BackStackMember> {
        val roots = SparseArrayCompat<BackStackMember>(2)

        roots.put(MOBILE_PAGE, BackStackMember(StoreMobileListFragment.newInstance()))

        val bundle = Bundle()
        bundle.putLong(CATEGORY_ID_KEY, MOBILE_LIST_CATEGORY_ID)
        roots.put(CATEGORY_PAGE, BackStackMember(CategoryFragment.newInstance(MOBILE_LIST_CATEGORY_ID, null), bundle))
        return roots
    }

    override fun getFragmentHostContainerId(): Int {
        return R.id.f_container_mobile
    }

    override fun getNavigationType(): Int {
        return NavigationType.NAVIGATION_TYPE_DRAWER
    }

    override fun onBackPress(): Boolean {
        return false
    }
}