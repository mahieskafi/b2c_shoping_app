package com.srp.eways.ui.bill.navigation

import android.os.Bundle
import androidx.collection.SparseArrayCompat
import androidx.fragment.app.Fragment
import com.srp.eways.R
import com.srp.eways.base.BaseViewModel
import com.srp.eways.ui.bill.MainBillFragment
import com.srp.eways.ui.bill.report.BillReportFragment
import com.srp.eways.ui.navigation.BackStackMember
import com.srp.eways.ui.navigation.NavigationFragment
import com.srp.eways.ui.navigation.NavigationType

class BillNavigationFragment : NavigationFragment<BaseViewModel>() {

    private var mRootFragment: Int = 0

    private lateinit var mFragment: Fragment
    private var mPageChangeListener: MainBillFragment.OnPageChangeListener? = null

    companion object {
        const val mRootId: Int = 0

        @JvmStatic
        fun newInstance(rootFragment: Int): BillNavigationFragment = BillNavigationFragment().apply {
            arguments = Bundle().apply {
                putInt("FRAGMENT", rootFragment)
            }
        }
    }

    public fun setPageChangeListener(pageChangeListener: MainBillFragment.OnPageChangeListener) {
        mPageChangeListener = pageChangeListener
    }

    override fun acquireViewModel(): BaseViewModel? {
        return null
    }

    override fun getNavigationViewType(): Int {
        return -1
    }

    override fun getLayoutId(): Int {
        return R.layout.bill_container_fragment
    }

    override fun getRootTabId(): Int {
        return mRootId
    }

    override fun createNavigationRoots(): SparseArrayCompat<BackStackMember> {
        val roots = SparseArrayCompat<BackStackMember>(1)

        mRootFragment = arguments!!.getInt("FRAGMENT")
        if (mPageChangeListener != null) {
            mFragment = mPageChangeListener!!.getFragment(mRootFragment)
        } else {
            mFragment = BillReportFragment.newInstance()
        }

        roots.put(mRootId, BackStackMember(mFragment))
        return roots
    }

    override fun getFragmentHostContainerId(): Int {
        return R.id.f_container
    }

    override fun getNavigationType(): Int {
        return NavigationType.NAVIGATION_TYPE_TAB
    }

    fun getFragment(): Fragment {
        return mFragment
    }
}