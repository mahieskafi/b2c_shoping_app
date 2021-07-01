package com.srp.ewayspanel.ui.store.mainpage

import androidx.collection.SparseArrayCompat
import com.srp.eways.base.BaseViewModel
import com.srp.eways.ui.navigation.BackStackMember
import com.srp.eways.ui.navigation.NavigationFragment
import com.srp.ewayspanel.R
import com.srp.eways.ui.navigation.NavigationType

/**
 * Created by ErfanG on 2/23/2020.
 */
class StoreMainPage : NavigationFragment<BaseViewModel>() {

    companion object {
        const val MAIN_PAGE = 0

    }

    override fun acquireViewModel(): BaseViewModel {
        return BaseViewModel()
    }

    override fun getNavigationViewType(): Int {
        return -1
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_main_page_container
    }

    override fun getRootTabId(): Int {
        return MAIN_PAGE
    }

    override fun createNavigationRoots(): SparseArrayCompat<BackStackMember> {
        val roots = SparseArrayCompat<BackStackMember>(1)

        roots.put(MAIN_PAGE, BackStackMember(StoreMainPageFragment()))
        return roots
    }

    override fun getFragmentHostContainerId(): Int {
        return R.id.f_container
    }

    override fun getNavigationType(): Int {
        return NavigationType.NAVIGATION_TYPE_TAB
    }
}