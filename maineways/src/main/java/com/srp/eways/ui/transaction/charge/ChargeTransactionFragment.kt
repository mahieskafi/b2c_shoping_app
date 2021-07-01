package com.srp.eways.ui.transaction.charge


import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srp.eways.R
import com.srp.eways.base.BaseRecyclerAdapter
import com.srp.eways.di.DIMain
import com.srp.eways.model.bill.report.BillReportItem
import com.srp.eways.model.transaction.charge.ChargeSale
import com.srp.eways.ui.charge.MainChargePagerAdapter
import com.srp.eways.ui.navigation.NavigationMemberFragment
import com.srp.eways.ui.transaction.deposit.DepositTransactionFragment
import com.srp.eways.ui.view.toolbar.WeiredToolbar
import ir.abmyapp.androidsdk.IABResources
import java.util.*

class ChargeTransactionFragment : NavigationMemberFragment<ChargeTransactionViewModel>(), MainChargePagerAdapter.PagerFragmentLifeCycle {

    private val TAG = "ChargeTransactionFr"

//    companion object {
//        @JvmStatic
//        fun newInstance(): ChargeTransactionFragment = ChargeTransactionFragment()
//    }

    companion object {
        private const val CHARGE_TRANSACTION_HAS_TOOLBAR = "charge_transaction_has_toolbar"

        @JvmStatic
        fun newInstance(hasToolbar: Boolean):  ChargeTransactionFragment
        {
            val args = Bundle()
            args.putBoolean(CHARGE_TRANSACTION_HAS_TOOLBAR, hasToolbar)

            val fragment = ChargeTransactionFragment()
            fragment.arguments = args
            return fragment
        }
    }


    lateinit var mViewModel: ChargeTransactionViewModel
    lateinit var mTransactionList: RecyclerView
    lateinit var mAdapter: ChargeTransactionListAdapter
    private lateinit var mToolbar: WeiredToolbar

    override fun acquireViewModel(): ChargeTransactionViewModel {
        return DIMain.getViewModel(ChargeTransactionViewModel::class.java)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_charge_transaction
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = viewModel

        mTransactionList = getView()!!.findViewById(R.id.charge_transaction_list)

        mToolbar = view.findViewById(R.id.toolbar)
        if (arguments != null && arguments!!.getBoolean(ChargeTransactionFragment.CHARGE_TRANSACTION_HAS_TOOLBAR)) {
            setupToolbar()
        }

        initialListAttrs()
    }

    private fun initialListAttrs() {

        mTransactionList.layoutManager = LinearLayoutManager(context)

        mAdapter = ChargeTransactionListAdapter(context!!, object : BaseRecyclerAdapter.RetryClickListener {
            override fun onClicked() {
                mAdapter.setIsLoading(true)
                Handler().postDelayed(
                        {
                            mViewModel.invalidateChargeTransaction()
                            mViewModel.loadMore()
                        }, 2000)

            }

        }, object : OnChargeTransactionItemClickListener {
            override fun onShowMoreClickListener(isShowMore: Boolean, chargeSale: ChargeSale?) {
                val chargeSaleList = mAdapter.getData() as ArrayList<ChargeSale?>

                for (i in chargeSaleList.indices) {
                    if (chargeSaleList[i]!!.id == chargeSale!!.id) {
                        if (chargeSaleList[i]!!.isShowMore != isShowMore) {
                            (mAdapter.getData()[i] as ChargeSale).isShowMore = isShowMore
                        }
                    } else {
                        (mAdapter.getData()[i] as ChargeSale).isShowMore = false
                    }
                }
                mAdapter.notifyDataSetChanged()
            }

        })

        mTransactionList.adapter = mAdapter

        mTransactionList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (mViewModel.isListLoading().value == false) {

                    if (mViewModel.isHasMore().value == true) {

                        val layoutManager = mTransactionList.layoutManager as LinearLayoutManager

                        if (mViewModel.getTransactions().value != null && mViewModel.getTransactions().value!!.size > 1 &&
                                mViewModel.getTransactions().value!!.size - 1 == layoutManager.findLastCompletelyVisibleItemPosition()) {

                            mViewModel.setIsNextPageLoadData(true)
                            mViewModel.loadMore()

                        } else {
                            //last item not seen yet
                        }
                    } else {
                        //No more data to load
                    }
                } else {
                    //now loading is showing
                }

            }
        })

        mViewModel.isListLoading().observe(this, Observer { newValue ->
            mAdapter.setIsLoading(newValue)
        })

//        mViewModel.isHasMore().observe(this, Observer { newValue ->
//            Log.d("TransactionHasMore", newValue.toString())
//        })

        mViewModel.getErrorCode().observe(this, Observer { newValue ->

            mAdapter.setErrorCode(newValue)
        })

        mViewModel.getTransactions().observe(this, Observer { newValue ->

            if (newValue != null) {
                mAdapter.setNewData(newValue)
            }
        })

        mViewModel.loadMore()
    }

    override fun onResumePagerFragment() {
        mViewModel = DIMain.getViewModel(ChargeTransactionViewModel::class.java)

        mViewModel.loadMore()
    }

    override fun getDataFromServer() {
        mViewModel.invalidateChargeTransaction()

        mViewModel.loadMore()
    }


    private fun setupToolbar() {
        val resources: IABResources = DIMain.getABResources()

        mToolbar.setTitle(resources.getString(R.string.transaction_page_title))
        mToolbar.setShowTitle(true)
        mToolbar.setShowDeposit(false)
        mToolbar.setShowShop(false)
        mToolbar.setBackgroundColor(resources.getColor(R.color.deposit_toolbar_background))
        mToolbar.setShowNavigationDrawer(false)
        mToolbar.setShowNavigationUp(true)
        mToolbar.setOnBackClickListener{
            onBackPressed()
        }
        mToolbar.visibility = View.VISIBLE
    }


    override fun onBackPress(): Boolean {
        return super.onBackPress()
    }
}
