package com.srp.eways.ui.transaction


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.srp.eways.R
import com.srp.eways.base.BaseViewModel
import com.srp.eways.di.DIMain
import com.srp.eways.ui.charge.MainChargePagerAdapter
import com.srp.eways.ui.navigation.INavigationController
import com.srp.eways.ui.navigation.NavigationMemberFragment
import com.srp.eways.ui.view.SolidTabBar
import com.srp.eways.ui.view.charge.TabOptionsView
import com.srp.eways.ui.view.charge.a.ChargeOptionsTabViewA
import com.srp.eways.ui.view.toolbar.WeiredToolbar
import com.srp.eways.util.Utils
import ir.abmyapp.androidsdk.IABResources
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
abstract class TransactionsFragment : NavigationMemberFragment<BaseViewModel>(), TabOptionsView.ChargeOptionsTabViewListener {

    override fun acquireViewModel(): BaseViewModel? {
        return null
    }

    override fun getLayoutId(): Int {
        return 0
    }

    private lateinit var mPager: ViewPager
    private lateinit var mToolbar: WeiredToolbar
    private lateinit var mAdapter: TransactionPagerAdapter

    private lateinit var AB: IABResources

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transactions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initilize()
        setupToolbar()
    }

    abstract fun getTransactionPagerAdapter(cf: FragmentManager): TransactionPagerAdapter
    abstract fun setTabs(tabbar: SolidTabBar)


    private fun initilize() {
        mPager = view!!.findViewById<ViewPager>(R.id.pager)
        mToolbar = view!!.findViewById<WeiredToolbar>(R.id.transactions_toolbar)
        AB = DIMain.getABResources()


        val chargeOptionsTabViewA: ChargeOptionsTabViewA = view!!.findViewById(R.id.tab_view)
        val tabTitles = arrayOf(getString(R.string.transaction_tab_1), getString(R.string.transaction_tab_4), getString(R.string.transaction_tab_2))
        val tabIcons = intArrayOf(R.drawable.ic_charge_transaction_pager, R.drawable.transaction_shop_tab_unselected, R.drawable.transaction_deposit_tab_unselected)

        val tabItems: MutableList<TabOptionsView.TabItem> = ArrayList()
        for (i in tabTitles.indices) {
            tabItems.add(TabOptionsView.TabItem(tabTitles[i], tabIcons[i], tabTitles[i]))
        }
        chargeOptionsTabViewA.setTabItems(tabItems)

        chargeOptionsTabViewA.setUnselectedTabColor(resources.getColor(R.color.deposit_fragmnet_charge_option_tab_unselected_color))
        chargeOptionsTabViewA.setTabBackgroundColor(resources.getColor(R.color.deposit_fragmnet_charge_option_tab_background_color))
        chargeOptionsTabViewA.setOnChargeTabClickListener(this);
        chargeOptionsTabViewA.setSelectedTab(0)

        mAdapter = getTransactionPagerAdapter(childFragmentManager)
        mPager.adapter = mAdapter


        mPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

                Utils.hideKeyboard(activity)
                chargeOptionsTabViewA.setSelectedTab(2 - position)
                refreshChargeTransactionList(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
//        mPager.offscreenPageLimit = getOffsetLimit()

        mPager.currentItem = 1

    }

    open fun getOffsetLimit(): Int {
        return 0
    }

    private fun setupToolbar() {
        mToolbar.setBackgroundColor(AB.getColor(R.color.transaction_toolbar_background))
        mToolbar.setTitle(AB.getString(R.string.transaction_page_title))
        mToolbar.setShowTitle(true)
        mToolbar.setShowShop(false)
        mToolbar.setTitleTextColor(AB.getColor(R.color.white))
        mToolbar.setTitleTextSize(AB.getDimen(R.dimen.transaction_fragment_title_toolbar_size))
        mToolbar.setShowDeposit(AB.getBoolean(R.bool.transaction_toolbar_has_deposit))
        mToolbar.setOnNavigationDrawerClickListener { toggleDrawer() }
        mToolbar.setShowNavigationUp(true)
        mToolbar.showNavigationDrawer(true)
        mToolbar.setOnBackClickListener { onBackPressed() }
    }

    private fun refreshChargeTransactionList(position: Int) {
        val selectedFragment = mAdapter.getItem(position)
        if (selectedFragment is MainChargePagerAdapter.PagerFragmentLifeCycle) {
            val fragmentToShow = selectedFragment as MainChargePagerAdapter.PagerFragmentLifeCycle
            fragmentToShow.onResumePagerFragment()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            refreshChargeTransactionList(mPager.currentItem)
        }
    }

    override fun onBackPress(): Boolean {
        val member = mAdapter.instantiateItem(mPager, mPager.currentItem) as INavigationController

        return member.onBackPress()
    }

    override fun onChargeTabClicked(tabIndex: Int, tabItem: TabOptionsView.TabItem?) {
        mPager.currentItem = 2 - tabIndex
    }
}
