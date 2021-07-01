package com.srp.eways.ui.bill.payment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import com.srp.eways.BuildConfig
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.model.bill.BillPaymentResponse
import com.srp.eways.model.bill.BillPaymentResult
import com.srp.eways.model.bill.BillPaymentStatusResult
import com.srp.eways.model.bill.archivedList.BillPaymentDetail
import com.srp.eways.model.deposit.BankListResponse
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.network.NetworkResponseCodes.SUCCESS
import com.srp.eways.ui.bill.MainBillFragment
import com.srp.eways.ui.bill.paymenttype.BillInfo
import com.srp.eways.ui.bill.paymenttype.BillPaymentTypeViewModel
import com.srp.eways.ui.bill.receipt.ReceiptFragment
import com.srp.eways.ui.bill.report.BillReportItemView
import com.srp.eways.ui.deposit.DepositFragment
import com.srp.eways.ui.login.UserInfoViewModel
import com.srp.eways.ui.navigation.NavigationMemberFragment
import com.srp.eways.ui.navigation.NavigationViewType
import com.srp.eways.ui.view.LoadingStateView.RetryListener
import com.srp.eways.ui.view.ViewUtils
import com.srp.eways.ui.view.bill.BillConfirmationDialog
import com.srp.eways.ui.view.bill.paymenttype.BillPaymentTypeView
import com.srp.eways.ui.view.button.ButtonElement
import com.srp.eways.ui.view.dialog.ConfirmationDialog.ConfirmationDialogItemClickListener
import com.srp.eways.ui.view.input.InputElement
import com.srp.eways.ui.webview.MainWebFragment
import com.srp.eways.util.BillUtil
import com.srp.eways.util.Constants
import com.srp.eways.util.PersianNumberFormatter
import com.srp.eways.util.Utils

class BillPaymentFragment() : NavigationMemberFragment<BillPaymentViewModel>() {
    private lateinit var mTitle: AppCompatTextView
    private lateinit var mDescription: AppCompatTextView
    private lateinit var mCardView: CardView
    private lateinit var mBillIdInput: InputElement
    private lateinit var mPayIdInput: InputElement

    private lateinit var mBillIdError: View
    private lateinit var mBillIdErrorText: AppCompatTextView
    private lateinit var mBillErrorIcon: AppCompatImageView

    private lateinit var mPriceIdError: View
    private lateinit var mPriceIdErrorText: AppCompatTextView
    private lateinit var mPriceErrorIcon: AppCompatImageView

    private lateinit var mQrReaderButton: ButtonElement
    private lateinit var mSmsReaderButton: ButtonElement

    private lateinit var mPayButton: ButtonElement

    private lateinit var mTypeface: Typeface

    private lateinit var mBillPaymentView: BillPaymentTypeView
    private lateinit var mRootView: ScrollView

    private lateinit var userInfoViewModel: UserInfoViewModel

    private var mBills = ArrayList<BillInfo>()

    private lateinit var mPageChangeListener: MainBillFragment.OnPageChangeListener

    private val billPaymentTypeViewModel = DIMain.getViewModel(BillPaymentTypeViewModel.getInstance().javaClass)

    private val billPaymentStatusResultObserver: Observer<BillPaymentStatusResult> = Observer { billPaymentStatusResult ->
        if (billPaymentStatusResult?.bills != null) {

            val billPaymentResult = BillPaymentResult(billPaymentStatusResult.bills, billPaymentStatusResult.status, 0, 0)
            val billPaymentResponse = BillPaymentResponse(billPaymentResult, "", billPaymentStatusResult.status, billPaymentStatusResult.description)

            if (billPaymentResponse.status == SUCCESS) {
                mBillIdInput.setText("")
                mPayIdInput.setText("")
            }

            userInfoViewModel.invalidateCredit()
            userInfoViewModel.getCredit()

            showReceipt(billPaymentResponse)

            viewModel.consumeBillPaymentStatusResponseLiveLiveData()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): BillPaymentFragment {
            return BillPaymentFragment()
        }
    }

    fun setPageChangeListener(listener: MainBillFragment.OnPageChangeListener) {
        mPageChangeListener = listener
    }

    override fun acquireViewModel(): BillPaymentViewModel {
        return DIMain.getViewModel(BillPaymentViewModel.getInstance().javaClass)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_bill_payment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTitle = view.findViewById(R.id.tv_title)
        mDescription = view.findViewById(R.id.tv_description)
        mCardView = view.findViewById(R.id.card_view)
        mBillIdInput = view.findViewById(R.id.input_bill_id)
        mPayIdInput = view.findViewById(R.id.input_pay_id)
        mQrReaderButton = view.findViewById(R.id.btn_qr_reader)
        mSmsReaderButton = view.findViewById(R.id.btn_sms_reader)

        mBillIdError = view.findViewById(R.id.bill_error)
        mBillIdErrorText = mBillIdError.findViewById(R.id.text_error)
        mBillErrorIcon = mBillIdError.findViewById(R.id.icon_error)

        mPriceIdError = view.findViewById(R.id.price_error)
        mPriceIdErrorText = mPriceIdError.findViewById(R.id.text_error)
        mPriceErrorIcon = mPriceIdError.findViewById(R.id.icon_error)

        mPayButton = view.findViewById(R.id.pay_button)

        mRootView = view.findViewById(R.id.root_view_bill_payment)
        mBillPaymentView = view.findViewById(R.id.bill_payment_view)

        setBillPaymentTypeViewLayoutParams()
        mBillPaymentView.setSaveButtonVisibility(View.VISIBLE)
        mBillPaymentView.setSaveButtonEnabled(true)
        mBillPaymentView.setNotEnoughDialogConfirmClickListener(object : BillPaymentTypeView.ConfirmNotEnoughDialogClickListener {
            override fun onConfirmClicked(payingPrice: Long, currentCredit: Long) {
                openFragment(DepositFragment.newInstance(payingPrice - currentCredit), NAVIGATION_VIEW_TYPE_HAS_TOOLBAR)
            }

        })
        mBillPaymentView.setPaymentClickListener(View.OnClickListener {
            if (Utils.isInternetAvailable()) {
                observeToPayment()
                payment()
            } else {
                Toast.makeText(context, resources.getString(R.string.network_error_no_internet), Toast.LENGTH_SHORT).show()
            }
        })

        mBillPaymentView.setSaveClickListener(View.OnClickListener {
            observeToSaveBill()
            viewModel.save(BillPaymentDetail(Utils.toEnglishNumber(mBillIdInput.getText()), Utils.toEnglishNumber(mPayIdInput.getText())))
        })

        mBillPaymentView.setLoadingRetryClickListener(RetryListener {
            mBillPaymentView.clearSelectedBank()
            viewModel.getBankList()

            observeToGetBankList()
        })

        setUpSaveAndPayButton()

        val abResources = DIMain.getABResources()

        userInfoViewModel = DIMain.getViewModel(UserInfoViewModel.getInstance().javaClass)

        mTypeface = ResourcesCompat.getFont(context!!, R.font.iran_yekan)!!

        mTitle.typeface = mTypeface
        mTitle.setTextColor(abResources.getColor(R.color.bill_payment_text_color))
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.bill_payment_title_text_size).toFloat())
        mTitle.text = abResources.getString(R.string.bill_payment_page_title)

        val titleLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        titleLayoutParams.gravity = Gravity.RIGHT
        titleLayoutParams.setMargins(0, abResources.getDimenPixelSize(R.dimen.bill_payment_title_margin_sides),
                abResources.getDimenPixelSize(R.dimen.bill_payment_title_margin_sides), 0)
        mTitle.layoutParams = titleLayoutParams

        val cardLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        cardLayoutParams.setMargins(abResources.getDimenPixelSize(R.dimen.bill_payment_card_margin_sides),
                abResources.getDimenPixelSize(R.dimen.bill_payment_card_margin_top),
                abResources.getDimenPixelSize(R.dimen.bill_payment_card_margin_sides),
                abResources.getDimenPixelSize(R.dimen.bill_payment_card_margin_bottom))
        mCardView.layoutParams = cardLayoutParams

        val cardPaddingSides = abResources.getDimenPixelSize(R.dimen.bill_payment_card_padding_sides)
        val cardPaddingTop = abResources.getDimenPixelSize(R.dimen.bill_payment_card_padding_top_bottom)
        mCardView.setContentPadding(cardPaddingSides, cardPaddingTop, cardPaddingSides, cardPaddingTop)

        mDescription.typeface = mTypeface
        mDescription.setTextColor(abResources.getColor(R.color.bill_payment_text_color))
        mDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.bill_payment_description_text_size).toFloat())
        mDescription.text = abResources.getString(R.string.bill_payment_page_description)

        val descriptionLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        descriptionLayoutParams.gravity = Gravity.RIGHT
        descriptionLayoutParams.setMargins(abResources.getDimenPixelSize(R.dimen.bill_payment_title_margin_sides),
                0,
                abResources.getDimenPixelSize(R.dimen.bill_payment_title_margin_sides),
                0)
        mDescription.layoutParams = descriptionLayoutParams

        mBillErrorIcon.setImageDrawable(abResources.getDrawable(R.drawable.ic_error))
        mBillIdErrorText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.bill_payment_error_text_size))
        mBillIdErrorText.setTextColor(abResources.getColor(R.color.bill_payment_input_error_text_color))
        mBillIdErrorText.typeface = mTypeface
        mBillIdErrorText.text = abResources.getString(R.string.bill_payment_input_error)
        val errorLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        errorLayoutParams.setMargins(abResources.getDimenPixelSize(R.dimen.bill_payment_card_margin_sides),
                0,
                abResources.getDimenPixelSize(R.dimen.bill_payment_error_margin_right),
                abResources.getDimenPixelSize(R.dimen.bill_payment_error_margin_bottom))
        mBillIdError.layoutParams = errorLayoutParams

        mPriceIdErrorText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.bill_payment_error_text_size))
        mPriceIdErrorText.setTextColor(abResources.getColor(R.color.bill_payment_input_error_text_color))
        mPriceIdErrorText.typeface = mTypeface
        mPriceIdErrorText.text = abResources.getString(R.string.bill_price_input_error)
        mPriceIdError.layoutParams = errorLayoutParams

        setupBillIdInput()
        setupPayIdInput()
        setupQrButton()
        setupSMSButton()
        observeBillPaymentDetail()

        billPaymentTypeViewModel.getClearBillInput().observe(this, object : Observer<Boolean?> {

            override fun onChanged(t: Boolean?) {

                if (mBillIdInput.getText().length >= 1 || mPayIdInput.getText().length >= 1){
                    mBillIdInput.setText("")
                    mPayIdInput.setText("")
                }

            }
        })
    }

    private fun setUpSaveAndPayButton() {

        val resources = DIMain.getABResources()

        with(mPayButton) {
            setText(resources.getString(R.string.bill_payment_pay_button_text))
            setTextSize(resources.getDimenPixelSize(R.dimen.bill_inquiry_detail_pay_button_text_size).toFloat())
            setTextColor(resources.getColor(R.color.bill_inquiry_detail_pay_button_text_color))
            setEnabledBackground(resources.getDrawable(R.drawable.button_element_default_background_enabled))
            setDisableBackground(resources.getDrawable(R.drawable.button_element_default_background_disabled))
            setLoadingColorFilter(resources.getColor(R.color.bill_inquiry_detail_pay_button_text_color))
            hasIcon(false)
            setEnable(false)
            setClickListener(View.OnClickListener {

                var price: Long = mPayIdInput.getText().substring(0, mPayIdInput.getText().length - 5).toLong() * 1000
                var billId = mBillIdInput.getText()

                var billIdLength = billId.length

                if (mBillIdInput.getText().length >= 8) {
                    if (Utils.checkBillId(mBillIdInput.getText()) ){
                        mBillIdError.visibility = View.GONE
                    }else{
                        mBillIdError.visibility = View.VISIBLE
                    }
                }

                if (mPayIdInput.getText().length >= 6 ) {
                    if (Utils.checkBillId(mPayIdInput.getText().substring(0,mPayIdInput.getText().length - 1))){
                        mPriceIdError.visibility = View.GONE
                    }else{
                        mPriceIdError.visibility = View.VISIBLE
                    }
                }

                if (!canPayBill()){
                    return@OnClickListener
                }

                mBills = arrayListOf<BillInfo>()
                mBills.add(BillInfo(0, billId.substring(billIdLength - 2, billIdLength - 1).toInt(), billId.toLong(),
                        mPayIdInput.getText().toLong(), price))

                mBillPaymentView.visibility = View.VISIBLE
                mBillPaymentView.setInfo(mBills)
                ViewUtils.scrollToView(mBillPaymentView, mRootView)
                observeToGetBankList()
            })
        }

    }

    private fun setPayEnable(enable: Boolean) {

        mPayButton.setEnable(enable)

    }

    private fun setupBillIdInput() {
        val abResources = DIMain.getABResources()
        mBillIdInput.setTextSize(abResources.getDimenPixelSize(R.dimen.bill_payment_input_text_size).toFloat())
        mBillIdInput.setTextColor(abResources.getColor(R.color.bill_payment_input_text_color))
        mBillIdInput.setHintColor(abResources.getColor(R.color.bill_payment_input_hint_color))
        mBillIdInput.setHint(abResources.getString(R.string.bill_payment_input_bill_id_hint))
        mBillIdInput.setSelectedBackground(abResources.getDrawable(R.drawable.input_element_bill_background_selected))
        mBillIdInput.setUnSelectedBackground(abResources.getDrawable(R.drawable.input_element_bill_background_unselected))
        mBillIdInput.setCancelIcon(abResources.getDrawable(R.drawable.ic_cancel_dialog))
        mBillIdInput.hasIcon(true)
        mBillIdInput.setIconVisibility(View.GONE)
        mBillIdInput.setInputType(InputType.TYPE_CLASS_NUMBER)
        mBillIdInput.getEditText().maxLines = 1
        mBillIdInput.getEditText().setText("")
        mBillIdInput.background = mBillIdInput.getUnselectedBackground()
        mBillIdInput.setTypeFace(mTypeface)
        mBillIdInput.getEditText().filters = arrayOf<InputFilter>(InputFilter.LengthFilter(13))
        mBillIdInput.addTextChangeListener(PersianNumberFormatter())
        mBillIdInput.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                mBillPaymentView.visibility = View.GONE
                mBillIdError.visibility = View.GONE

                if (mPayIdInput.getText().length >= 6 && mBillIdInput.getText().length >= 8) {
                    setPayEnable(true)
                } else {
                    setPayEnable(false)
                }
            }
        })

        mBillIdInput.getEditText().setOnFocusChangeListener { v, hasFocus ->
            when {
                hasFocus -> {
                    mBillIdInput.background = mBillIdInput.getSelectedBackground()
                }
                mBillIdInput.getText().isNotEmpty() -> {
                    mBillIdInput.background = mBillIdInput.getSelectedBackground()
                }
                else -> {
                    mBillIdInput.background = mBillIdInput.getUnselectedBackground()
                }
            }
        }
    }

    private fun setupPayIdInput() {
        val abResources = DIMain.getABResources()
        mPayIdInput.setTextSize(abResources.getDimenPixelSize(R.dimen.bill_payment_input_text_size).toFloat())
        mPayIdInput.setTextColor(abResources.getColor(R.color.bill_payment_input_text_color))
        mPayIdInput.setHintColor(abResources.getColor(R.color.bill_payment_input_hint_color))
        mPayIdInput.setHint(abResources.getString(R.string.bill_payment_input_pay_id_hint))
        mPayIdInput.setSelectedBackground(abResources.getDrawable(R.drawable.input_element_bill_background_selected))
        mPayIdInput.setUnSelectedBackground(abResources.getDrawable(R.drawable.input_element_bill_background_unselected))
        mPayIdInput.setCancelIcon(abResources.getDrawable(R.drawable.ic_cancel_dialog))
        mPayIdInput.hasIcon(true)
        mPayIdInput.setIconVisibility(View.GONE)
        mPayIdInput.setInputType(InputType.TYPE_CLASS_NUMBER)
        mPayIdInput.getEditText().maxLines = 1
        mPayIdInput.getEditText().setText("")
        mPayIdInput.background = mPayIdInput.getUnselectedBackground()
        mPayIdInput.setTypeFace(mTypeface)
        mPayIdInput.getEditText().filters = arrayOf<InputFilter>(InputFilter.LengthFilter(13))
        mPayIdInput.getEditText().imeOptions = EditorInfo.IME_ACTION_DONE
        mPayIdInput.addTextChangeListener(PersianNumberFormatter())
        mPayIdInput.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                mBillPaymentView.visibility = View.GONE
                mPriceIdError.visibility = View.GONE

                if (mPayIdInput.getText().length >= 6 && mBillIdError.visibility != View.VISIBLE) {
                    setPayEnable(true)
                } else {
                    setPayEnable(false)
                }
            }
        })

        mPayIdInput.getEditText().setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                mPayIdInput.background = mPayIdInput.getSelectedBackground()

                if (mBillIdInput.getText().length < 8) {
                    mBillIdInput.background = abResources.getDrawable(R.drawable.login_input_error_background)
                    mBillIdError.visibility = View.VISIBLE
                }
            } else if (mPayIdInput.getText().isNotEmpty()) {
                mPayIdInput.background = mPayIdInput.getSelectedBackground()
            } else {
                mPayIdInput.background = mPayIdInput.getUnselectedBackground()
            }

        }
    }

    private fun canPayBill() : Boolean{

        val joinTowStringId : String

        if (mBillIdInput.getText().isNotEmpty() && mPayIdInput.getText().isNotEmpty()
            && mPayIdInput.getText().length >= 6 && mBillIdError.visibility != View.VISIBLE
            && mPriceIdError.visibility != View.VISIBLE && mBillIdInput.getText().length >= 8){

            joinTowStringId=Utils.removeZeroFormBillId(mBillIdInput.getText()) + Utils.removeZeroFormBillId(mPayIdInput.getText())

            if (Utils.checkBillId(joinTowStringId)){
                return true
            }else{
                Toast.makeText(context,"عدم همخوانی شناسه و قبض و پرداخت" , Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return false
    }

    private fun setupQrButton() {
        val abResources = DIMain.getABResources()
        mQrReaderButton.setEnabledBackground(abResources.getDrawable(R.drawable.bill_payment_reader_type_button_background_enabled))
        mQrReaderButton.setDisableBackground(abResources.getDrawable(R.drawable.bill_payment_reader_type_button_background_enabled))
        mQrReaderButton.setText(abResources.getString(R.string.bill_payment_button_read_qr_title))
        mQrReaderButton.setEnable(true)
        mQrReaderButton.setIcon(abResources.getDrawable(R.drawable.ic_qr_reader))
        mQrReaderButton.setIconVisibility(View.VISIBLE)
        mQrReaderButton.setIconCenter(true)
        mQrReaderButton.setTextColor(abResources.getColor(R.color.bill_payment_button_text_color))
        mQrReaderButton.setTextSize(abResources.getDimenPixelSize(R.dimen.bill_payment_button_text_size).toFloat())
        mQrReaderButton.setBackgroundElevation(abResources.getDimenPixelSize(R.dimen.bill_payment_button_back_elevation).toFloat())
        mQrReaderButton.setTextTypeFace(mTypeface)

        viewModel.isPermissionAccessed.observe(this, Observer {
            if (it != null && it) {
                startActivity(activity?.baseContext?.let { it1 -> BillPaymentQrActivity.newIntent(it1) })
                viewModel.setPermissionAccessedConsumed()
            }
        })

        mQrReaderButton.setOnClickListener {

            if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA), Constants.ZXING_CAMERA_PERMISSION)
            } else {
                startActivity(activity?.baseContext?.let { it1 -> BillPaymentQrActivity.newIntent(it1) })
            }


        }
    }

    private fun setupSMSButton() {
        val abResources = DIMain.getABResources()
        mSmsReaderButton.setEnabledBackground(abResources.getDrawable(R.drawable.bill_payment_reader_type_button_background_enabled))
        mSmsReaderButton.setDisableBackground(abResources.getDrawable(R.drawable.bill_payment_reader_type_button_background_enabled))
        mSmsReaderButton.setText(abResources.getString(R.string.bill_payment_button_read_sms_title))
        mSmsReaderButton.setEnable(true)
        mSmsReaderButton.setIcon(abResources.getDrawable(R.drawable.ic_sms_reader))
        mSmsReaderButton.setIconVisibility(View.VISIBLE)
        mSmsReaderButton.setIconCenter(true)
        mSmsReaderButton.setTextColor(abResources.getColor(R.color.bill_payment_button_text_color))
        mSmsReaderButton.setTextSize(abResources.getDimenPixelSize(R.dimen.bill_payment_button_text_size).toFloat())
        mSmsReaderButton.setBackgroundElevation(abResources.getDimenPixelSize(R.dimen.bill_payment_button_back_elevation).toFloat())
        mSmsReaderButton.setTextTypeFace(mTypeface)

        mSmsReaderButton.setOnClickListener {
            openFragment(BillPaymentSmsFragment.newInstance(), NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR)
        }
    }

    private fun observeBillPaymentDetail() {
        viewModel.getPaymentLiveData().observe(this, Observer<BillPaymentDetail> {
            if (it != null) {
                mBillIdInput.setText(it.billId)
                mPayIdInput.setText(it.payId)
//                viewModel.consumeBillPaymentDetailLiveData()
            }
        })
    }

    private fun observeToSaveBill() {
        viewModel.getSaveResponseLiveData().observe(this, Observer {
            if (it != null) {
                val resources = DIMain.getABResources()

                if (it.status == Constants.SAVE_BILL_SUCCESS_CODE) {
                    val dialog = BillConfirmationDialog(context!!)
                    dialog.setIcon(resources.getDrawable(R.drawable.ic_bill_dialog_smile_mark))
                    dialog.setText(resources.getString(R.string.bill_inquiry_save_success_dialog_text))
                    dialog.setCancelButtonText(resources.getString(R.string.bill_inquiry_save_success_dialog_return_text))
                    dialog.setConfirmButtonText(resources.getString(R.string.bill_inquiry_save_success_dialog_see_list_text))
                    dialog.setClickListener(object : ConfirmationDialogItemClickListener {
                        override fun onConfirmClicked() {
                            dialog.dismiss()
                            mPageChangeListener.onPageChanged(1)
                        }

                        override fun onCancelClicked() {
                            dialog.dismiss()
                        }
                    })
                    dialog.show()
                } else {
                    Toast.makeText(context, it.description, Toast.LENGTH_LONG).show()
                }
                viewModel.consumeSaveResponseLiveData()
            }
        })

        viewModel.isBillSaveRequestInProgress().observe(this, Observer {
            if (it) {
                mBillPaymentView.setSaveButtonLoadingVisibility(true)

            } else {
                mBillPaymentView.setSaveButtonLoadingVisibility(false)
            }
        })
    }

    private fun observeToGetBankList() {

        if (viewModel.bankListLiveData.value == null) {

            viewModel.bankListLiveData.observe(this, getBankListResponseObserver())
            viewModel.getBankList()
        } else {
            mBillPaymentView.clearSelectedBank()
            viewModel.getBankList()
        }

    }

    private fun getBankListResponseObserver(): Observer<BankListResponse> {
        return Observer<BankListResponse> {
            if (it.status == NetworkResponseCodes.SUCCESS) {
                if (it.items != null) {
                    mBillPaymentView.setBankList(it.items)
                }
            } else {
                mBillPaymentView.setBankListError(it.description)
            }
        }
    }

    fun observeToPayment() {

        viewModel.getPayResponseLiveData().observe(this, Observer {
            if (it != null) {
                if (it.status != NetworkResponseCodes.SUCCESS) {
                    if (it.result != null) {
                        showReceipt(it)
                    } else {
                        Toast.makeText(context, it.description, Toast.LENGTH_SHORT).show()
                    }

                    userInfoViewModel.invalidateCredit()
                    userInfoViewModel.getCredit()
                } else {
                    if (it.url != null && it.url.isNotEmpty()) {
                        billPaymentTypeViewModel.setSelectedPageToPay(Constants.BILL_PAYMENT_PAGE)
                        openFragment(MainWebFragment.newInstance(BuildConfig.DARGAH_URL + it.url), NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR)

                    } else if (it.result != null) {
                        showReceipt(it)
                    } else {
                        Toast.makeText(context, it.description, Toast.LENGTH_SHORT).show()
                    }
                }
                billPaymentTypeViewModel.consumePayResponseLiveData()
                viewModel.consumeData()
            }
        })

        viewModel.isBillPayRequestInProgress().observe(this, Observer {
            mBillPaymentView.setPaymentButtonLoadingVisibility(it)
        })

        billPaymentTypeViewModel.getDeepLinkResponseReceivedLiveData().observe(this, Observer {
            if (it != null && it.isNotEmpty() && billPaymentTypeViewModel.getSelectedPage() == Constants.BILL_PAYMENT_PAGE) {

                viewModel.getPaymentStatus(it)
                viewModel.billPaymentStatusResponseLiveLiveData.observe(this, billPaymentStatusResultObserver)

                billPaymentTypeViewModel.consumeDeepLinkResponseReceivedLiveData()
                billPaymentTypeViewModel.setCheckLoading(true)

            }
        })
    }

    private fun showReceipt(paymentResponse: BillPaymentResponse) {
        billPaymentTypeViewModel.setCheckLoading(false)
        val listener = object : com.srp.eways.ui.bill.receipt.ReceiptFragment.BottomButtonsListener {
            override fun onRetryClicked() {
                mPayButton.performClick()
//                payment()
            }

            override fun onMainPageClicked() {
                onBackPressed()
            }

            override fun onReportClicked() {
                mPageChangeListener.onPageChanged(0)
            }

            override fun onPhoneBackPressed(isPhone: Boolean) {
            }

        }

        val receiptFragment = ReceiptFragment.newInstance()
        receiptFragment.setBillPaymentResponse(paymentResponse)
        receiptFragment.setBottomButtonsListener(listener)
        openFragment(receiptFragment, NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR)

        if (BillUtil.getBillReportStatus(paymentResponse.status) == BillReportItemView.Status.OK) {
            mBillIdInput.setText("")
            mPayIdInput.setText("")
        }

        viewModel.consumeData()
    }

    private fun payment() {

        if (mBillPaymentView.getBillsTotalPrice() <= 0) {
            Toast.makeText(context, "مبلغ پرداختی 0 است.", Toast.LENGTH_LONG).show()
            return
        }

        var selectedBank = mBillPaymentView.getSelectedBank()!!

        if (selectedBank.gId == BillUtil.DEPOSIT_GID) {
            if (mBillPaymentView.getBillsTotalPrice() > userInfoViewModel.getCreditLiveData().value!!) {
                mBillPaymentView.showNotEnoughCreditDialog(userInfoViewModel.getCreditLiveData().value!!)
            } else {
                mBillPaymentView.setPaymentButtonLoadingVisibility(true)
                viewModel.pay(getBillPaymentDetailList(), selectedBank.gId)
            }
        } else {
            mBillPaymentView.setPaymentButtonLoadingVisibility(true)
            viewModel.pay(getBillPaymentDetailList(), selectedBank.gId)
        }

    }

    private fun setBillPaymentTypeViewLayoutParams() {
        val abResources = DIMain.getABResources()

        val height = ViewUtils.getDisplayHeight(context) - abResources.getDimenPixelSize(R.dimen.bill_fragment_toolbar_height) -
                abResources.getDimenPixelSize(R.dimen.bill_page_tabbar_height) - abResources.getDimenPixelSize(R.dimen.bottomnav_height)

        val listLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, height)
        mBillPaymentView.layoutParams = listLayoutParams
    }

    private fun getBillPaymentDetailList(): BillPaymentDetail {
        return BillPaymentDetail(mBills[0].id, mBills[0].billId.toString(), mBills[0].billPayId.toString())
    }
}