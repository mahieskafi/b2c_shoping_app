package com.srp.eways.ui.transaction.deposit


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srp.eways.R
import com.srp.eways.base.BaseRecyclerAdapter
import com.srp.eways.di.DIMain
import com.srp.eways.model.transaction.charge.ChargeSale
import com.srp.eways.ui.charge.MainChargePagerAdapter
import com.srp.eways.ui.login.UserInfoViewModel
import com.srp.eways.ui.navigation.NavigationMemberFragment
import com.srp.eways.ui.transaction.charge.OnChargeTransactionItemClickListener
import com.srp.eways.ui.view.toolbar.WeiredToolbar
import com.srp.eways.ui.view.transacionitem.DepositTransactionItem
import ir.abmyapp.androidsdk.IABResources
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class DepositTransactionFragment : NavigationMemberFragment<DepositTransactionViewModel>(), MainChargePagerAdapter.PagerFragmentLifeCycle {

    companion object {
        private const val DEPOSIT_TRANSACTION_HAS_TOOLBAR = "deposit_transaction_has_toolbar"

        @JvmStatic
        fun newInstance(hasToolbar: Boolean): DepositTransactionFragment {
            val args = Bundle()
            args.putBoolean(DEPOSIT_TRANSACTION_HAS_TOOLBAR, hasToolbar)

            val fragment = DepositTransactionFragment()
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var mViewModel: DepositTransactionViewModel
    lateinit var mUserInfoViewModel: UserInfoViewModel
    lateinit var mTransactionList: RecyclerView
    lateinit var mAdapter: DepositTransactionListAdapter
    private lateinit var mToolbar: WeiredToolbar

    private val getCreditChangedObserver = Observer<Boolean> { aBoolean ->
        // if increase credit is successful, goto homePage
        if (aBoolean != null && !aBoolean) {
            mUserInfoViewModel.onCreditIncreasedConsumed()
            mViewModel.clearData()
            mViewModel.setIsNextPageLoadData(false)
            mViewModel.loadMore()
        }
    }

    override fun acquireViewModel(): DepositTransactionViewModel {
        mViewModel = DIMain.getViewModel(DepositTransactionViewModel::class.java)
        return mViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_deposit_transaction
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mUserInfoViewModel = DIMain.getViewModel(UserInfoViewModel::class.java)
        mUserInfoViewModel.isCreditIncreased().observe(this, getCreditChangedObserver)

        mTransactionList = getView()!!.findViewById(R.id.deposit_transaction_list)

        mToolbar = view.findViewById(R.id.toolbar)
        if (arguments != null && arguments!!.getBoolean(DEPOSIT_TRANSACTION_HAS_TOOLBAR)) {
            setupToolbar()
        }

        initialListAttrs()
    }

    private fun initialListAttrs() {

        mTransactionList.layoutManager = LinearLayoutManager(context)

        mAdapter = DepositTransactionListAdapter(context!!, object : BaseRecyclerAdapter.RetryClickListener {
            override fun onClicked() {
                mViewModel.loadMore()
            }

        }, object : DepositTransactionItem.OnDepositTransactionItemClickListener {
            override fun onShowMoreClickListener(isShowMore: Boolean, depositTransaction: com.srp.eways.model.deposit.transaction.DepositTransactionItem?) {
                val depositTransactionList = mAdapter.getData()

                for (i in depositTransactionList.indices) {
                    if (depositTransactionList[i]!!.requestId == depositTransaction!!.requestId) {
                        if (depositTransactionList[i]!!.isShowMore != isShowMore) {
                            (mAdapter.getData()[i] as com.srp.eways.model.deposit.transaction.DepositTransactionItem).isShowMore = isShowMore
                        }
                    } else {
                        (mAdapter.getData()[i] as com.srp.eways.model.deposit.transaction.DepositTransactionItem).isShowMore = false
                    }
                }
                mAdapter.notifyDataSetChanged()
            }

        })

//        mViewModel.getTransactions().value?.let { mAdapter.setNewData(it) }

        mTransactionList.adapter = mAdapter

        mTransactionList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (mViewModel.isListLoading().value == false) {

                    if (mViewModel.isHasMore().value == true) {

                        val layoutManager = mTransactionList.layoutManager as LinearLayoutManager

                        if (mViewModel.getTransactions().value != null && mAdapter.getData().size > 1 &&
                                mAdapter.getData().size - 1 == layoutManager.findLastCompletelyVisibleItemPosition()) {

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
//
//        })

        mViewModel.getErrorCode().observe(this, Observer { newValue ->
            mAdapter.setErrorCode(newValue)
        })


        mViewModel.getTransactions().observe(this, Observer { newValue ->

            if (newValue == null) {
                mAdapter.setNewData(arrayListOf())
            } else {
                mAdapter.appendData(newValue)
            }
        })

        mViewModel.clearData()
        mViewModel.setIsNextPageLoadData(false)
        mViewModel.loadMore()
    }

    override fun onResumePagerFragment() {
        mViewModel = DIMain.getViewModel(DepositTransactionViewModel::class.java)

        mViewModel.setIsNextPageLoadData(false)
        mViewModel.loadMore()
    }

    override fun getDataFromServer() {
        mViewModel.invalidateDepositTransaction()

        mViewModel.setIsNextPageLoadData(false)
        mViewModel.loadMore()
    }

    override fun onBackPress(): Boolean {
        return super.onBackPress()
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
        mToolbar.setOnBackClickListener {
            onBackPressed()
        }
        mToolbar.visibility = View.VISIBLE
    }

}
