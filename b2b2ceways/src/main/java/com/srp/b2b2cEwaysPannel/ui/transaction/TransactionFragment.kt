package com.srp.b2b2cEwaysPannel.ui.transaction

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.ui.transaction.TransactionPagerAdapter
import com.srp.eways.ui.transaction.TransactionsFragment
import com.srp.eways.ui.transaction.charge.ChargeTransactionFragment
import com.srp.eways.ui.transaction.deposit.DepositTransactionFragment
import com.srp.eways.ui.view.SolidTabBar

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
                    else ->
                        return ChargeTransactionFragment.newInstance(false)
                }
            }

            override fun getCount(): Int {
                return 2
            }
        }
    }

    override fun setTabs(tabbar: SolidTabBar) {
        val AB = DIMain.getABResources()

        tabbar.addTab(AB.getString(R.string.transaction_tab_2), AB.getDrawable(R.drawable.transaction_deposit_tab_unselected))
        tabbar.addTab(AB.getString(R.string.transaction_tab_1), AB.getDrawable(R.drawable.ic_transactions_design_b))
    }
}