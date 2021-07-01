package com.srp.ewayspanel.ui.transaction

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.ui.transaction.TransactionPagerAdapter
import com.srp.eways.ui.transaction.TransactionsFragment
import com.srp.eways.ui.transaction.charge.ChargeTransactionFragment
import com.srp.eways.ui.transaction.deposit.DepositTransactionFragment
import com.srp.eways.ui.transaction.deposit.DepositTransactionViewModel
import com.srp.eways.ui.view.SolidTabBar
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.ui.transaction.order.OrderTransactionFragment

/**
 * Created by ErfanG on 3/9/2020.
 */
class TransactionFragment : TransactionsFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): TransactionsFragment =  TransactionFragment()
    }


    override fun getTransactionPagerAdapter(cf: FragmentManager): TransactionPagerAdapter {
        return object : TransactionPagerAdapter(cf){
            override fun getItem(position: Int): Fragment {
                when (position) {
                    0 ->
                        return DepositTransactionFragment.newInstance(false)
                    1 ->
                        return OrderTransactionFragment.newInstance()
                    else ->
                        return ChargeTransactionFragment.newInstance(false)
                }
            }

            override fun getCount(): Int {
                return 3
            }
        }
    }

    override fun setTabs(tabbar: SolidTabBar) {
        val AB = DIMain.getABResources()

//        mTabBar.addTab(AB.getString(R.string.transaction_tab_4), AB.getDrawable(R.drawable.transaction_shop_tab_unselected))
//        mTabBar.addTab(AB.getString(R.string.transaction_tab_3), AB.getDrawable(R.drawable.transaction_bill_tab_unselected))
        tabbar.addTab(AB.getString(R.string.transaction_tab_2), AB.getDrawable(R.drawable.transaction_deposit_tab_unselected))
        tabbar.addTab(AB.getString(R.string.transaction_tab_4), AB.getDrawable(R.drawable.transaction_shop_tab_unselected))
        tabbar.addTab(AB.getString(R.string.transaction_tab_1), AB.getDrawable(R.drawable.ic_transactions_design_b))
    }

    override fun onHiddenChanged(hidden: Boolean) {

        if(!hidden){
            val vm = DI.getViewModel(DepositTransactionViewModel::class.java)

            vm.clearData()
            vm.setIsNextPageLoadData(false)
            vm.loadMore()

        }

    }
}