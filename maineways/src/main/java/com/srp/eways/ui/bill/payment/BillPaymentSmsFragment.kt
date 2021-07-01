package com.srp.eways.ui.bill.payment

import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Scroller
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.model.bill.archivedList.BillPaymentDetail
import com.srp.eways.ui.login.UserInfoViewModel
import com.srp.eways.ui.navigation.NavigationMemberFragment
import com.srp.eways.ui.view.button.ButtonElement
import com.srp.eways.ui.view.toolbar.WeiredToolbar
import com.srp.eways.util.BillUtil


class BillPaymentSmsFragment : NavigationMemberFragment<BillPaymentViewModel>() {

    companion object {
        @JvmStatic
        fun newInstance(): BillPaymentSmsFragment = BillPaymentSmsFragment()
    }

    private lateinit var mToolbar: WeiredToolbar
    private lateinit var mDescriptionText: AppCompatTextView
    private lateinit var mTitleText: AppCompatTextView
    private lateinit var mCardView: CardView
    private lateinit var mReadSmsButton: ButtonElement

    private lateinit var mPasteIcon: AppCompatImageView
    private lateinit var mPasteText: AppCompatTextView
    private lateinit var mClearIcon: AppCompatImageView
    private lateinit var mSmsInput: AppCompatTextView

    private lateinit var mErrorView: View
    private lateinit var mErrorText: AppCompatTextView
    private lateinit var mErrorIcon: AppCompatImageView

    private lateinit var mTypeface: Typeface
    private lateinit var mPasteEnabledDrawable: Drawable
    private lateinit var mPasteDisabledDrawable: Drawable
    private lateinit var mSmsInputEnabledDrawable: Drawable
    private lateinit var mSmsInputDisabledDrawable: Drawable
    private lateinit var mSmsInputErrorDrawable: Drawable
    private var mPasteEnabledColor: Int = 0
    private var mPasteDisabledColor: Int = 0

    private lateinit var mReadBillPaymentDetail: BillPaymentDetail

    override fun acquireViewModel(): BillPaymentViewModel {
        return DIMain.getViewModel(BillPaymentViewModel.getInstance().javaClass)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_bill_payment_sms
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mToolbar = view.findViewById(R.id.toolbar)
        mDescriptionText = view.findViewById(R.id.tv_description)
        mTitleText = view.findViewById(R.id.tv_title)
        mCardView = view.findViewById(R.id.card_view)
        mReadSmsButton = view.findViewById(R.id.btn_read)

        mPasteIcon = view.findViewById(R.id.iv_paste)
        mPasteText = view.findViewById(R.id.tv_paste)
        mClearIcon = view.findViewById(R.id.iv_clear)
        mSmsInput = view.findViewById(R.id.tv_sms)

        mErrorView = view.findViewById(R.id.v_error)
        mErrorText = mErrorView.findViewById(R.id.text_error);
        mErrorIcon = mErrorView.findViewById(R.id.icon_error)

        val abResources = DIMain.getABResources()

        mTypeface = ResourcesCompat.getFont(context!!, R.font.iran_yekan)!!

        mPasteEnabledDrawable = abResources.getDrawable(R.drawable.ic_paste_enabled)
        mPasteDisabledDrawable = abResources.getDrawable(R.drawable.ic_paste)
        mPasteEnabledColor = abResources.getColor(R.color.bill_payment_sms_text_color)
        mPasteDisabledColor = abResources.getColor(R.color.bill_payment_sms_text_disabled_color)

        mSmsInputDisabledDrawable = abResources.getDrawable(R.drawable.input_element_bill_sms_background_unselected)
        mSmsInputEnabledDrawable = abResources.getDrawable(R.drawable.input_element_bill_sms_background_selected)
        mSmsInputErrorDrawable = abResources.getDrawable(R.drawable.input_element_bill_sms_background_error)

        setupToolbar()
        setupSMSButton()

        //Description
        mDescriptionText.typeface = mTypeface
        mDescriptionText.setTextColor(abResources.getColor(R.color.bill_payment_sms_text_color))
        mDescriptionText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.bill_payment_sms_text_size).toFloat())
        mDescriptionText.text = abResources.getString(R.string.bill_payment_sms_description)

        val descriptionLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        descriptionLayoutParams.gravity = Gravity.RIGHT
        descriptionLayoutParams.setMargins(abResources.getDimenPixelSize(R.dimen.bill_payment_sms_description_margin_sides),
                abResources.getDimenPixelSize(R.dimen.bill_payment_sms_description_margin_top),
                abResources.getDimenPixelSize(R.dimen.bill_payment_sms_description_margin_sides),
                abResources.getDimenPixelSize(R.dimen.bill_payment_sms_description_margin_bottom))
        mDescriptionText.layoutParams = descriptionLayoutParams

        //Title
        mTitleText.typeface = mTypeface
        mTitleText.setTextColor(abResources.getColor(R.color.bill_payment_sms_text_color))
        mTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.bill_payment_sms_title_size).toFloat())
        mTitleText.text = abResources.getString(R.string.bill_payment_sms_title)

        val titleLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        titleLayoutParams.gravity = Gravity.RIGHT
        titleLayoutParams.setMargins(0, 0,
                abResources.getDimenPixelSize(R.dimen.bill_payment_sms_title_margin),
                abResources.getDimenPixelSize(R.dimen.bill_payment_sms_title_margin))
        mTitleText.layoutParams = titleLayoutParams

        //CardView
        val cardLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        cardLayoutParams.setMargins(
                abResources.getDimenPixelSize(R.dimen.bill_payment_card_margin_sides), 0,
                abResources.getDimenPixelSize(R.dimen.bill_payment_card_margin_sides),
                abResources.getDimenPixelSize(R.dimen.bill_payment_sms_button_margin_top))
        mCardView.layoutParams = cardLayoutParams

        val cardPaddingSides = abResources.getDimenPixelSize(R.dimen.bill_payment_sms_card_padding_sides)
        val cardPaddingTop = abResources.getDimenPixelSize(R.dimen.bill_payment_sms_card_padding_top)
        val cardPaddingBottom = abResources.getDimenPixelSize(R.dimen.bill_payment_sms_card_padding_bottom)
        mCardView.setContentPadding(cardPaddingSides, cardPaddingTop, cardPaddingSides, cardPaddingBottom)

        mPasteIcon.setImageDrawable(mPasteDisabledDrawable)
        mPasteIcon.setOnClickListener {
            mSmsInput.text = getClipboardText()
        }

        mPasteText.typeface = mTypeface
        mPasteText.setTextColor(mPasteDisabledColor)
        mPasteText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.bill_payment_sms_paste_text_size).toFloat())
        mPasteText.text = abResources.getString(R.string.bill_payment_sms_paste_text)

        mClearIcon.setImageDrawable(abResources.getDrawable(R.drawable.ic_bin))
        mClearIcon.setPadding(abResources.getDimenPixelSize(R.dimen.bill_payment_sms_clear_icon_padding_start),
                abResources.getDimenPixelSize(R.dimen.bill_payment_sms_clear_icon_padding_sides),
                abResources.getDimenPixelSize(R.dimen.bill_payment_sms_clear_icon_padding_sides),
                abResources.getDimenPixelSize(R.dimen.bill_payment_sms_clear_icon_padding_sides))
        mClearIcon.setOnClickListener {
            mSmsInput.setText("")
            mSmsInput.background = mSmsInputDisabledDrawable
            mPasteText.setTextColor(mPasteDisabledColor)
            mPasteIcon.setImageDrawable(mPasteDisabledDrawable)
            mReadSmsButton.setEnable(false)
            mErrorView.visibility = View.GONE
        }

        setupSmsInput()
        setupError()
    }

    fun setupToolbar() {
        val resources = DIMain.getABResources()

        mToolbar.setShowTitle(true)
        mToolbar.setShowDeposit(false)
        mToolbar.setShowShop(false)
        mToolbar.setShowNavigationDrawerMenu(false)
        mToolbar.setShowNavigationUp(true)
        mToolbar.setTitle(resources.getString(R.string.bill_payment_sms_page_title))
//        mToolbar.setTitleIcon(resources.getDrawable(R.drawable.ic_sms_payment_title))
        mToolbar.setBackgroundColor(resources.getColor(R.color.buycharge_toolbar_background))
        mToolbar.setOnBackClickListener() { onBackPressed() }
    }

    private fun setupSMSButton() {
        val abResources = DIMain.getABResources()

        mReadSmsButton.setEnabledBackground(abResources.getDrawable(R.drawable.login_button_background_enabled))
        mReadSmsButton.setDisableBackground(abResources.getDrawable(R.drawable.login_button_background_disabled))
        mReadSmsButton.setText(abResources.getString(R.string.bill_payment_sms_button_text))
        mReadSmsButton.setEnable(false)
        mReadSmsButton.setIconVisibility(View.GONE)
        mReadSmsButton.setTextColor(abResources.getColor(R.color.white))
        mReadSmsButton.setTextSize(abResources.getDimenPixelSize(R.dimen.bill_payment_sms_button_text_size).toFloat())
        mReadSmsButton.setTextTypeFace(mTypeface)

        val buttonLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, abResources.getDimenPixelSize(R.dimen.bill_payment_sms_button_height))
        buttonLayoutParams.setMargins(
                abResources.getDimenPixelSize(R.dimen.bill_payment_sms_button_margin_sides),
                0,
                abResources.getDimenPixelSize(R.dimen.bill_payment_sms_button_margin_sides),
                abResources.getDimenPixelSize(R.dimen.bill_payment_sms_button_margin_bottom))
        mReadSmsButton.layoutParams = buttonLayoutParams


        mReadSmsButton.setOnClickListener {
            viewModel.setBillPaymentDetail(mReadBillPaymentDetail)
            onBackPressed()
        }
    }

    private fun setupSmsInput() {
        val abResources = DIMain.getABResources()

        mSmsInput.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.bill_payment_sms_input_element_text_size).toFloat())
        mSmsInput.setTextColor(abResources.getColor(R.color.bill_payment_input_text_color))
        mSmsInput.background = mSmsInputDisabledDrawable
        mSmsInput.setText("")
        mSmsInput.gravity = Gravity.TOP or Gravity.RIGHT
        mSmsInput.minimumWidth = abResources.getDimenPixelSize(R.dimen.bill_payment_sms_input_height)
        mSmsInput.typeface = mTypeface
        mSmsInput.setScroller(Scroller(context))
        mSmsInput.isVerticalScrollBarEnabled = true
        mSmsInput.movementMethod = ScrollingMovementMethod()

        mSmsInput.setPadding(abResources.getDimenPixelSize(R.dimen.bill_payment_sms_input_element_text_padding),
                abResources.getDimenPixelSize(R.dimen.bill_payment_sms_input_element_text_padding),
                abResources.getDimenPixelSize(R.dimen.bill_payment_sms_input_element_text_padding),
                abResources.getDimenPixelSize(R.dimen.bill_payment_sms_input_element_text_padding))

        mSmsInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (mSmsInput.text.isNotEmpty()) {
                    var billDetail = BillUtil.getPaymentDetailFromText(mSmsInput.text.toString())
                    if (billDetail != null && billDetail.billId.isNotEmpty()) {

                        mReadBillPaymentDetail = billDetail
                        mSmsInput.background = mSmsInputEnabledDrawable
                        mPasteIcon.setImageDrawable(mPasteEnabledDrawable)
                        mPasteText.setTextColor(mPasteEnabledColor)
                        mErrorView.visibility = View.GONE
                        mReadSmsButton.setEnable(true)
                    } else {
                        mSmsInput.background = mSmsInputErrorDrawable
                        mPasteIcon.setImageDrawable(mPasteDisabledDrawable)
                        mPasteText.setTextColor(mPasteDisabledColor)
                        mErrorView.visibility = View.VISIBLE
                        mReadSmsButton.setEnable(false)
                    }
                }
            }
        })
    }

    private fun setupError() {
        val abResources = DIMain.getABResources()

        mErrorIcon.setImageDrawable(abResources.getDrawable(R.drawable.ic_error))
        mErrorText.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.bill_payment_error_text_size))
        mErrorText.setTextColor(abResources.getColor(R.color.bill_payment_input_error_text_color))
        mErrorText.typeface = mTypeface
        mErrorText.text = abResources.getString(R.string.bill_payment_sms_error)
        val errorLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        errorLayoutParams.setMargins(0, abResources.getDimenPixelSize(R.dimen.bill_payment_card_margin_sides),
                0, abResources.getDimenPixelSize(R.dimen.bill_payment_card_margin_sides))
        mErrorView.layoutParams = errorLayoutParams

        mErrorView.visibility = View.GONE
    }

    private fun getClipboardText(): String {
        val clipboard = context!!.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        if (clipboard.hasPrimaryClip() && clipboard.primaryClipDescription!!.hasMimeType(MIMETYPE_TEXT_PLAIN)) {
            val item = clipboard.primaryClip!!.getItemAt(0)
            return com.srp.eways.util.Utils.toPersianNumber("" + item.text)
        }
        return ""
    }

}