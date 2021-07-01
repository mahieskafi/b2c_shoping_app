package com.srp.eways.ui.bill.receipt

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.model.bill.BillPaymentResponse
import com.srp.eways.model.bill.BillPaymentResult
import com.srp.eways.model.bill.archivedList.BillTemp
import com.srp.eways.ui.bill.report.BillReportItemView
import com.srp.eways.ui.login.UserInfoViewModel
import com.srp.eways.ui.navigation.NavigationMemberFragment
import com.srp.eways.ui.view.ViewUtils
import com.srp.eways.ui.view.button.ButtonElement
import com.srp.eways.ui.view.toolbar.WeiredToolbar
import com.srp.eways.util.BillUtil
import com.srp.eways.util.Constants
import com.srp.eways.util.Constants.SAVE_TYPE
import com.srp.eways.util.Utils
import java.io.File
import java.util.*

/**
 * Created by ErfanG on 5/10/2020.
 */
class ReceiptFragment : NavigationMemberFragment<ReceiptViewModel>() {


    interface BottomButtonsListener {

        fun onRetryClicked()

        fun onMainPageClicked()

        fun onReportClicked()

        fun onPhoneBackPressed(isPhone: Boolean)

    }

    private var isPhoneBacked : Boolean = true

    private lateinit var mToolbar: WeiredToolbar

    private lateinit var mMultipleContainer: RelativeLayout
    private lateinit var mList: RecyclerView
    private lateinit var mMultipleNewDepositSuffix: AppCompatTextView
    private lateinit var mMultipleNewDeposit: AppCompatTextView
    private lateinit var mMultipleNewDepositTitle: AppCompatTextView
    private lateinit var mMultipleNewDepositContainer: LinearLayout
    private lateinit var mMultipleMainButton: ButtonElement
    private lateinit var mMultipleSecondaryButton: ButtonElement

    private lateinit var mSingleContainer: RelativeLayout
    private lateinit var mSingleReceipt: ReceiptItemView
    private lateinit var mSingleNewDepositContainer: LinearLayout
    private lateinit var mSingleNewDepositSuffix: AppCompatTextView
    private lateinit var mSingleNewDeposit: AppCompatTextView
    private lateinit var mSingleNewDepositTitle: AppCompatTextView
    private lateinit var mSingleMainButton: ButtonElement
    private lateinit var mSingleSecondaryButton: ButtonElement

    private lateinit var mBillPaymentResponse: BillPaymentResponse
    private lateinit var mListener: BottomButtonsListener

    private var mIsSingle = false

    companion object {
        @JvmStatic
        fun newInstance(): ReceiptFragment {
            return ReceiptFragment()
        }
    }

    fun setBillPaymentResponse(billPaymentResponse: BillPaymentResponse) {
        mBillPaymentResponse = billPaymentResponse
        mIsSingle = mBillPaymentResponse.result?.bills?.size == 1
    }

    fun setBottomButtonsListener(listener: BottomButtonsListener) {
        mListener = listener
    }

    override fun acquireViewModel(): ReceiptViewModel {
        return DIMain.getViewModel(ReceiptViewModel::class.java)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_bill_receipt
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mToolbar = view.findViewById(R.id.toolbar)

        mMultipleContainer = view.findViewById(R.id.multiple_container)
        mList = view.findViewById(R.id.list)
        mMultipleNewDepositSuffix = view.findViewById(R.id.multiple_new_deposit_suffix)
        mMultipleNewDeposit = view.findViewById(R.id.multiple_new_deposit)
        mMultipleNewDepositTitle = view.findViewById(R.id.multiple_new_deposit_title)
        mMultipleNewDepositContainer = view.findViewById(R.id.multiple_new_deposit_container)
        mMultipleMainButton = view.findViewById(R.id.multiple_main_button)
        mMultipleSecondaryButton = view.findViewById(R.id.multiple_secondary_button)

        mSingleContainer = view.findViewById(R.id.single_container)
        mSingleReceipt = view.findViewById(R.id.single_receipt)
        mSingleNewDepositContainer = view.findViewById(R.id.single_new_deposit_container)
        mSingleNewDepositSuffix = view.findViewById(R.id.single_new_deposit_suffix)
        mSingleNewDeposit = view.findViewById(R.id.single_new_deposit)
        mSingleNewDepositTitle = view.findViewById(R.id.single_new_deposit_title)
        mSingleMainButton = view.findViewById(R.id.single_main_button)
        mSingleSecondaryButton = view.findViewById(R.id.single_secondary_button)


        setUpToolbar()
        val resources = DIMain.getABResources()

        with(mSingleNewDeposit) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_receipt_multiple_new_deposit_size).toFloat())
            setTextColor(resources.getColor(R.color.bill_receipt_multiple_new_deposit_color))
            typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_medium)
        }

        with(mMultipleNewDeposit) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_receipt_multiple_new_deposit_size).toFloat())
            setTextColor(resources.getColor(R.color.bill_receipt_multiple_new_deposit_color))
            typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_medium)
        }

        var userInfoViewModel: UserInfoViewModel? = DIMain.getViewModel(UserInfoViewModel.getInstance().javaClass) as UserInfoViewModel
        if (userInfoViewModel != null) {
            userInfoViewModel.getCreditLiveData().observe(this, Observer {
                if (it != null && it > 0) {

                    if (mIsSingle) {
                        mSingleNewDepositContainer.visibility = View.VISIBLE
                        mSingleNewDeposit.text = Utils.toPersianPriceNumber(it)
                    } else {
                        mMultipleNewDepositContainer.visibility = View.VISIBLE
                        mMultipleNewDeposit.text = Utils.toPersianPriceNumber(it)
                    }
                }
            })
        }

        showResult()
    }

    private fun showResult() {

        val resources = DIMain.getABResources()
        var bitmap: Bitmap? = null

        if (mIsSingle) {

            with(mSingleMainButton) {
                setText(resources.getString(R.string.bill_receipt_multiple_main_button_text))
                setTextSize(resources.getDimenPixelSize(R.dimen.bill_receipt_multiple_main_button_text_size).toFloat())
                setTextColor(resources.getColor(R.color.bill_receipt_multiple_main_button_text_color))
                setEnabledBackground(resources.getDrawable(R.drawable.button_element_default_background_enabled))
                setDisableBackground(resources.getDrawable(R.drawable.button_element_default_background_disabled))
                setLoadingColorFilter(resources.getColor(R.color.bill_receipt_multiple_main_button_text_color))
                hasIcon(false)
                setEnable(true)
            }

            with(mSingleSecondaryButton) {
                setText(resources.getString(R.string.bill_receipt_multiple_secondary_button_text))
                setTextSize(resources.getDimenPixelSize(R.dimen.bill_receipt_multiple_secondary_button_text_size).toFloat())
                setTextColor(resources.getColor(R.color.bill_receipt_multiple_secondary_button_text_color))
                setEnabledBackground(resources.getDrawable(R.drawable.bill_inquiry_detail_save_background_enabled))
                setDisableBackground(resources.getDrawable(R.drawable.button_element_default_background_disabled))
                setLoadingColorFilter(resources.getColor(R.color.bill_receipt_multiple_secondary_button_text_color))
                hasIcon(false)
                setEnable(true)
            }

            with(mSingleNewDepositSuffix) {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_receipt_multiple_new_deposit_suffix_size).toFloat())
                setTextColor(resources.getColor(R.color.bill_receipt_multiple_new_deposit_suffix_color))
                typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
                text = resources.getString(R.string.rial)
            }

            with(mSingleNewDepositTitle) {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_receipt_multiple_new_deposit_title_size).toFloat())
                setTextColor(resources.getColor(R.color.bill_receipt_multiple_new_deposit_title_color))
                typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
                text = resources.getString(R.string.bill_receipt_new_deposit_title)
            }

            mSingleContainer.visibility = View.VISIBLE
            mMultipleContainer.visibility = View.GONE

            val result = mBillPaymentResponse
            with(mSingleReceipt)
            {
                setBillResult(result)
                setSaveButtonAction(View.OnClickListener {
                    createBitmap(Constants.SAVE_TYPE, result.result!!.bills?.get(0)!!)
                })

                setShareButtonAction(View.OnClickListener {
                    createBitmap(Constants.SHARE_TYPE, result.result!!.bills?.get(0)!!)
                })
            }

            if (BillUtil.getBillReportStatus(result.result!!.bills!![0].status) == BillReportItemView.Status.OK) {
                mSingleNewDepositContainer.visibility = View.VISIBLE
            } else {
                mSingleNewDepositContainer.visibility = View.GONE
            }

            //todo change bottom buttons text and clickListener
            if (BillUtil.getBillReportStatus(result.result!!.bills!![0].status) == BillReportItemView.Status.OK) {

                with(mSingleMainButton) {
                    setText(resources.getString(R.string.bill_receipt_multiple_main_button_text))
                    setOnClickListener {
                        isPhoneBacked=false
                        onBackPressed()

                        mListener.onReportClicked()
                    }
                }

                with(mSingleSecondaryButton) {
                    setText(resources.getString(R.string.bill_receipt_multiple_secondary_button_text))
                    setOnClickListener {
                        isPhoneBacked=false
                        onBackPressed()

                        mListener.onMainPageClicked()
                    }
                }

            } else {

                with(mSingleMainButton) {

                    if (BillUtil.getBillReportStatus(result.result!!.bills!![0].status) != BillReportItemView.Status.FAILED) {

                        setText(resources.getString(R.string.bill_receipt_multiple_secondary_button_text))
                        setOnClickListener {
                            isPhoneBacked=false
                            onBackPressed()

                            mListener.onMainPageClicked()
                        }
                    } else {
                        setText(resources.getString(R.string.bill_receipt_multiple_main_button_text_failed))
                        setOnClickListener {
                            isPhoneBacked=false
                            onBackPressed()

                            mListener.onRetryClicked()
                        }
                    }
                }


                with(mSingleSecondaryButton) {
                    setText(resources.getString(R.string.bill_receipt_multiple_main_button_text))
                    setOnClickListener {
                        isPhoneBacked=false
                        onBackPressed()

                        mListener.onReportClicked()
                    }
                }
            }
        } else {

            with(mMultipleMainButton) {
                setText(resources.getString(R.string.bill_receipt_multiple_main_button_text))
                setTextSize(resources.getDimenPixelSize(R.dimen.bill_receipt_multiple_main_button_text_size).toFloat())
                setTextColor(resources.getColor(R.color.bill_receipt_multiple_main_button_text_color))
                setEnabledBackground(resources.getDrawable(R.drawable.button_element_default_background_enabled))
                setDisableBackground(resources.getDrawable(R.drawable.button_element_default_background_disabled))
                setLoadingColorFilter(resources.getColor(R.color.bill_receipt_multiple_main_button_text_color))
                hasIcon(false)
                setEnable(true)
                setOnClickListener {
                    isPhoneBacked=false
                    onBackPressed()

                    mListener.onReportClicked()
                }
            }

            with(mMultipleSecondaryButton) {
                setText(resources.getString(R.string.bill_receipt_multiple_secondary_button_text))
                setTextSize(resources.getDimenPixelSize(R.dimen.bill_receipt_multiple_secondary_button_text_size).toFloat())
                setTextColor(resources.getColor(R.color.bill_receipt_multiple_secondary_button_text_color))
                setEnabledBackground(resources.getDrawable(R.drawable.bill_inquiry_detail_save_background_enabled))
                setDisableBackground(resources.getDrawable(R.drawable.button_element_default_background_disabled))
                setLoadingColorFilter(resources.getColor(R.color.bill_receipt_multiple_secondary_button_text_color))
                hasIcon(false)
                setEnable(true)
                setOnClickListener {
                    isPhoneBacked=false
                    onBackPressed()

                    mListener.onMainPageClicked()
                }
            }

            with(mMultipleNewDepositSuffix) {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_receipt_multiple_new_deposit_suffix_size).toFloat())
                setTextColor(resources.getColor(R.color.bill_receipt_multiple_new_deposit_suffix_color))
                typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
                text = resources.getString(R.string.rial)
            }

            with(mMultipleNewDepositTitle) {
                setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_receipt_multiple_new_deposit_title_size).toFloat())
                setTextColor(resources.getColor(R.color.bill_receipt_multiple_new_deposit_title_color))
                typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
                text = resources.getString(R.string.bill_receipt_new_deposit_title)
            }


            mMultipleContainer.visibility = View.VISIBLE
            mSingleContainer.visibility = View.GONE

            mList.layoutManager = LinearLayoutManager(context)

            val adapter = MultipleBillReceiptAdapter(context!!)

            mList.adapter = adapter

            adapter.setData(mBillPaymentResponse.result!!.bills!!)

        }
    }

    private fun saveBitmap(bitmap: Bitmap?, billTemp: BillTemp, type: Int) {
        val resources = DIMain.getABResources()

        if (bitmap != null) {
            val random_int = (Math.random() * (100 - 1 + 1) + 1).toInt()
            val fileName = "Bill" + billTemp.billId + "_" + billTemp.paymentId + "_" + billTemp.inquiryNumber + "_" +random_int

            if (Utils.saveBitmap(bitmap, context, fileName)) {
                if (type == SAVE_TYPE) {
                    Toast.makeText(context, resources.getString(R.string.bill_receipt_save_result_success), Toast.LENGTH_LONG).show()
                } else {

                    var uri: Uri? = null
                    uri = FileProvider.getUriForFile(context!!, "com.srp.ewayspanel.fileprovider", Utils.getFileforShare(fileName, context, bitmap))
                    val shareIntent = Utils.getShareFileIntent("image/jpeg", uri)
                    startActivity(Intent.createChooser(shareIntent, "Share Image"))
                }
            } else {
                Toast.makeText(context, resources.getString(R.string.bill_receipt_save_result_error), Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, resources.getString(R.string.bill_receipt_save_result_error), Toast.LENGTH_LONG).show()
        }
    }

    private fun createBitmap(type: Int, billTemp: BillTemp) {
//        val view = mSingleReceipt.getViewForSaving()

        val view = ReceiptForShairingView(context)

        val billTemps = ArrayList<BillTemp>()
        billTemps.add(billTemp)

        val billPaymentResult = BillPaymentResult(billTemps, 0, 41, 0)
        val billPaymentResponse = BillPaymentResponse(billPaymentResult, null, 700, "")

        view.setBillResult(billPaymentResponse)

        var bitmap = ViewUtils.convertViewToBitmap(context, view)

        viewModel.isPermissionAccessed.observe(this@ReceiptFragment, Observer {
            if (it != null && it) {
                saveBitmap(bitmap, billTemp, type)
                viewModel.setPermissionAccessedConsumed()
            }
        })

        if (ContextCompat.checkSelfPermission(context!!, WRITE_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!, arrayOf(WRITE_EXTERNAL_STORAGE), Constants.WRITE_STORAGE_PERMISSION)
        } else {
            saveBitmap(bitmap, billTemp, type)
        }

    }

    private fun setUpToolbar() {

        val resources = DIMain.getABResources()

        mToolbar.setShowTitle(true)
        mToolbar.setShowDeposit(false)
        mToolbar.setShowShop(false)
        mToolbar.setShowNavigationDrawerMenu(false)
        mToolbar.setShowNavigationUp(false)
        mToolbar.setTitle(resources.getString(R.string.bill_pay_receipt_title))
        mToolbar.setBackgroundColor(resources.getColor(R.color.buycharge_toolbar_background))
    }

    override fun onResume() {
        super.onResume()
        Utils.hideKeyboard(activity)
    }

    override fun onBackPress(): Boolean {
        mListener.onPhoneBackPressed(isPhoneBacked)
        return super.onBackPress()
    }
}