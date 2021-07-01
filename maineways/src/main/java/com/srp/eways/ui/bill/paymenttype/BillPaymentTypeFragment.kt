package com.srp.eways.ui.bill.paymenttype

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.srp.eways.BuildConfig
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.model.bill.archivedList.BillPaymentDetail
import com.srp.eways.model.deposit.Bank
import com.srp.eways.model.deposit.BankListResponse
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.ui.deposit.DepositFragment
import com.srp.eways.ui.login.UserInfoViewModel
import com.srp.eways.ui.navigation.NavigationMemberFragment
import com.srp.eways.ui.navigation.NavigationViewType
import com.srp.eways.ui.view.LoadingStateView.RetryListener
import com.srp.eways.ui.view.bill.paymenttype.BillPaymentTypeView
import com.srp.eways.ui.webview.MainWebFragment
import com.srp.eways.util.BillUtil
import com.srp.eways.util.Constants
import com.srp.eways.util.Utils

class BillPaymentTypeFragment(paymentClickListener: PaymentResponseListener, billList: ArrayList<BillInfo>) : NavigationMemberFragment<BillPaymentTypeViewModel>() {

    interface BankItemSelectedListener {
        fun onBankItemSelected(bank: Bank)
    }

    companion object {
        const val REPEATED_BILL_ERROR_CODE = 3

        @JvmStatic
        fun newInstance(paymentClickListener: PaymentResponseListener, billList: ArrayList<BillInfo>): BillPaymentTypeFragment {
            return BillPaymentTypeFragment(paymentClickListener, billList)
        }
    }

    private lateinit var mBillPaymentView: BillPaymentTypeView

    private var mBillList: ArrayList<BillInfo> = billList
    private var mPaymentClickListener: PaymentResponseListener = paymentClickListener

//    private lateinit var mSelectedBank: Bank

    private lateinit var mBankListResponseObserver: Observer<BankListResponse>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var userInfoViewModel: UserInfoViewModel = DIMain.getViewModel(UserInfoViewModel.getInstance().javaClass)

        mBillPaymentView = view.findViewById(R.id.bill_payment_view)

        mBillPaymentView.setInfo(mBillList)
        mBillPaymentView.setSaveButtonVisibility(View.GONE)
        mBillPaymentView.setNotEnoughDialogConfirmClickListener(object : BillPaymentTypeView.ConfirmNotEnoughDialogClickListener {
            override fun onConfirmClicked(payingPrice: Long, currentCredit: Long) {
                openFragment(DepositFragment.newInstance(payingPrice - currentCredit), NAVIGATION_VIEW_TYPE_HAS_TOOLBAR)
            }

        })

        mBillPaymentView.setLoadingRetryClickListener(RetryListener {
            mBillPaymentView.clearSelectedBank()

            viewModel.getBankList()
            viewModel.bankListLiveData.observe(this@BillPaymentTypeFragment, getBankListResponseObserver())
        })

        viewModel.getPayResponseLiveData().observe(this, Observer {
            if (it != null) {
                if (it.status != NetworkResponseCodes.SUCCESS) {
//                    onBackPressed()
                    if (it.result != null && it.status != REPEATED_BILL_ERROR_CODE) {
                        mPaymentClickListener.onPaymentResponseListener(it)
                    } else {
                        Toast.makeText(context, it.description, Toast.LENGTH_SHORT).show()
                    }

                    userInfoViewModel.invalidateCredit()
                    userInfoViewModel.getCredit()
                } else {
                    if (it.url != null && it.url.isNotEmpty()) {
                        openFragment(MainWebFragment.newInstance(BuildConfig.DARGAH_URL + it.url), NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR)

                    } else {
                        onBackPressed()
                        mPaymentClickListener.onPaymentResponseListener(it)
                    }
                }
                viewModel.consumePayResponseLiveData()
            }
        })

        mBillPaymentView.setPaymentClickListener(View.OnClickListener {

            if (Utils.isInternetAvailable()) {
                if (mBillPaymentView.getBillsTotalPrice() <= 0) {
                    Toast.makeText(context, "مبلغ پرداختی 0 است.", Toast.LENGTH_LONG).show()
                } else {

                    var selectedBank = mBillPaymentView.getSelectedBank()!!

                    if (selectedBank.gId == BillUtil.DEPOSIT_GID) {
                        if (mBillPaymentView.getBillsTotalPrice() > userInfoViewModel.getCreditLiveData().value!!) {
                            mBillPaymentView.showNotEnoughCreditDialog(userInfoViewModel.getCreditLiveData().value!!)
                        } else {
                            viewModel.pay(getBillPaymentDetailList(), selectedBank.gId)
                        }
                    } else {
                        viewModel.pay(getBillPaymentDetailList(), selectedBank.gId)
                    }
                }
            } else {
                Toast.makeText(context, resources.getString(R.string.network_error_no_internet), Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.getBankList()
        viewModel.bankListLiveData.observe(this, getBankListResponseObserver())

        viewModel.getDeepLinkResponseReceivedLiveData().observe(this, Observer {
            if (it != null && it.isNotEmpty() &&
                    (viewModel.getSelectedPage() == Constants.BILL_ARCHIVE_PAGE || viewModel.getSelectedPage() == Constants.BILL_REPORT_PAGE)) {
                viewModel.consumeDeepLinkResponseReceivedLiveData()

//                onBackPressed()

                mPaymentClickListener.onDeepLinkResponseReceived(it)

            }
        })

        viewModel.isBillPayRequestInProgress().observe(this, Observer {
            mBillPaymentView.setPaymentButtonLoadingVisibility(it)
        })

    }

    private fun getBankListResponseObserver(): Observer<BankListResponse> {
        mBankListResponseObserver = Observer<BankListResponse> {
            if (it.status == NetworkResponseCodes.SUCCESS) {
                if (it.items != null) {
                    mBillPaymentView.setBankList(it.items)
                }
            } else {
                mBillPaymentView.setBankListError(it.description)
            }
        }
        return mBankListResponseObserver
    }

    override fun acquireViewModel(): BillPaymentTypeViewModel {
        return DIMain.getViewModel(BillPaymentTypeViewModel.getInstance().javaClass)
    }

    override fun getLayoutId(): Int {
        return R.layout.bill_payment_type_fragment
    }

    private fun getBillPaymentDetailList(): ArrayList<BillPaymentDetail> {
        var billDetailList = arrayListOf<BillPaymentDetail>()
        for (billInfo in mBillList) {
            billDetailList.add(BillPaymentDetail(billInfo.id, billInfo.billId.toString(), billInfo.billPayId.toString(), billInfo.inquiryNumber))
        }

        return billDetailList
    }

    override fun onPause() {
        viewModel.bankListLiveData.removeObserver(mBankListResponseObserver)
        super.onPause()
    }

}