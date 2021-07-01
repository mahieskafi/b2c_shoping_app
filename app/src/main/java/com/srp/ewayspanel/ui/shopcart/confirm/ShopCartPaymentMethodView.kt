package com.srp.ewayspanel.ui.shopcart.confirm

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.widget.*
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.model.deposit.Bank
import com.srp.eways.ui.view.button.ButtonElement
import com.srp.eways.ui.view.input.InputElement
import com.srp.eways.util.Utils
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.repository.storepage.category.CategoryTreeNode.get
import com.srp.ewayspanel.ui.shopcart.ShopCartFragment
import com.yashoid.inputformatter.InputFormatter
import com.yashoid.sequencelayout.SequenceLayout
import kotlinx.android.synthetic.main.shop_cart_payment_dialog.view.*


/**
 * Created by ErfanG on 1/1/2020.
 */
class ShopCartPaymentMethodView : SequenceLayout {


    interface SubmitDiscountCode {
        fun onSubmitCode(discountCode: String)

        fun onCodeCleared()
    }

    interface PaymentItemsSelectedListener {
        fun onItemSelected(item: Int)
    }

    interface BankContentListener {
        fun onBankChanged(bank: Bank)
    }

    private val resources = DI.getABResources()

    //Views
    private val mDepositTitle: AppCompatTextView
    private val mDepositValueDescription: AppCompatTextView
    private val mDepositValue: AppCompatTextView
    private val mSwitchText: AppCompatTextView
    private val mSwitchBox: SwitchCompat

    private val mDiscountText: TextView
    private val mDiscountCode: InputElement
    private val mSubmitDiscount: ButtonElement
    private var mDiscountCheckBox: CheckBox
    private val mDiscoountChecked: ImageView
    private val mPayTitle: TextView

    private val mDiscountContainer: CardView
    private val mPaymentContainer: CardView

    private lateinit var mBankContentListener: BankContentListener
    private lateinit var mAction: SubmitDiscountCode

    private var mIsDepositEnough: Boolean = true
    private var mItemsSelectedListener: PaymentItemsSelectedListener? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defAttrSet: Int) : super(context, attributeSet, defAttrSet)

    init {

        LayoutInflater.from(context).inflate(R.layout.shop_cart_payment_method_item, this, true)

        mDepositTitle = findViewById(R.id.deposit_title)
        mDepositValueDescription = findViewById(R.id.tv_value_description)
        mDepositValue = findViewById(R.id.tv_deposit_value)
        mSwitchText = findViewById(R.id.switch_title)
        mSwitchBox = findViewById(R.id.switch_box)

        mDiscountText = findViewById(R.id.discount_text)
        mDiscountCode = findViewById(R.id.discount_code_value)
        mSubmitDiscount = findViewById(R.id.submit_discount_code)
        mDiscountCheckBox = findViewById(R.id.discount_checkbox)
        mDiscoountChecked = findViewById(R.id.discount_checked)
        mPayTitle = findViewById(R.id.pay_title_text)

        mDiscountContainer = findViewById(R.id.discount_container)
        mPaymentContainer = findViewById(R.id.deposit_container)

        addSequences(R.xml.sequences_shop_cart_payment_method_item)

        mDiscountCheckBox.isChecked = false

        mDiscountContainer.background = resources.getDrawable(R.drawable.address_detail_card_backgound)
        mPaymentContainer.background = resources.getDrawable(R.drawable.address_detail_card_backgound)

        val mainTypeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
        mPayTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_payment_method_item_radio_text_size).toFloat())
        mPayTitle.setTextColor(resources.getColor(R.color.shop_cart_payment_method_item_radio_message_color))
        mPayTitle.text = resources.getString(R.string.shop_cart_payment_method_item_pay_radio_text)
        mPayTitle.typeface = mainTypeface

        val listener = CompoundButton.OnCheckedChangeListener { switchView, isChecked ->
            when {
                isChecked -> {
                    mItemsSelectedListener?.onItemSelected(ShopCartFragment.SHOP_CART_PAY_DEPOSIT)
                }
                else -> {
                    mItemsSelectedListener?.onItemSelected(ShopCartFragment.SHOP_CART_PAY_DARGAH)
                }
            }
        }

        mSwitchBox.setOnCheckedChangeListener(listener)

        mSwitchBox.setOnClickListener {
            mSwitchBox.setOnCheckedChangeListener(listener)
        }

        var discountText = ""

        //region third row
        mDiscountText.text = resources.getString(R.string.shop_cart_payment_method_item_discount_text)
        mDiscountText.setTextColor(resources.getColor(R.color.shop_cart_payment_method_item_discount_color))
        mDiscountText.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_payment_method_item_discount_size).toFloat())
        mDiscountText.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_light)

        mDiscountCode.background = resources.getDrawable(R.drawable.shop_cart_payment_method_item_discount_code)
        mDiscountCode.setTextColor(resources.getColor(R.color.shop_cart_payment_method_item_discount_code_color))
        mDiscountCode.setHint(resources.getString(R.string.shop_cart_payment_method_item_discount_hint))
        mDiscountCode.setTextSize(resources.getDimenPixelSize(R.dimen.shop_cart_payment_method_item_discount_code_size).toFloat())
        mDiscountCode.setTypeFace(ResourcesCompat.getFont(context, R.font.iran_yekan_light)!!)
        mDiscountCode.getEditText().addTextChangedListener(InputFormatter(Utils.PersianNumberFormatter))
        mDiscountCode.getEditText().addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if (s!!.length > 0) {
                    var c = s?.get(s.length - 1)

                    if (s != null) {
                        for (i in s.indices)
                            if (!(s[i] in 'a'..'z') && !(s[i] in 'A'..'Z') && !(s[i] in '0'..'9')) {

                                Toast.makeText(context, resources.getString(R.string.shop_cart_payment_coupon_error_persian_letter), Toast.LENGTH_SHORT).show()

                                if (!discountText.isEmpty()) {
                                    mDiscountCode.getEditText().setText(discountText)
                                } else {
                                    mDiscountCode.getEditText().setText("")
                                }

                                return
                            }
                    }
                }
                if (!s.isNullOrEmpty()) {
                    mSubmitDiscount.setEnable(true)
                } else {
                    mSubmitDiscount.setEnable(false)

                    if (::mAction.isInitialized) {
                        mAction.onCodeCleared()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                discountText = mDiscountCode.getEditText().text.toString()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        mSubmitDiscount.setText(resources.getString(R.string.shop_cart_payment_method_item_submit_discount))
        mSubmitDiscount.setTextTypeFace(ResourcesCompat.getFont(context, R.font.iran_yekan)!!)
        mSubmitDiscount.setTextSize(resources.getDimenPixelSize(R.dimen.shop_cart_payment_method_item_submit_discount_text_size).toFloat())
        mSubmitDiscount.setTextMarginRight(resources.getDimenPixelSize(R.dimen.shop_cart_payment_method_item_submit_discount_text_right_margin))
        mSubmitDiscount.setTextColor(resources.getColor(R.color.shop_cart_payment_method_item_submit_discount_text_color))
        mSubmitDiscount.setEnabledBackground(resources.getDrawable(R.drawable.shop_cart_payment_method_item_submit_enable_background))
        mSubmitDiscount.setDisableBackground(resources.getDrawable(R.drawable.shop_cart_payment_method_item_submit_enable_background))
        mSubmitDiscount.setLoadingColorFilter(resources.getColor(R.color.shop_cart_payment_method_item_submit_discount_text_color))
        mSubmitDiscount.hasIcon(false)
        mSubmitDiscount.setEnable(true)
        //endregion


        mDiscountCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (mDiscountCheckBox.isChecked) {
                mDiscoountChecked.visibility = VISIBLE
                mSubmitDiscount.visibility = VISIBLE
                mDiscountCode.visibility = VISIBLE

                mDiscountText.setTextColor(resources.getColor(R.color.shop_cart_payment_method_item_discount_color))
            } else {
                mDiscoountChecked.visibility = INVISIBLE
                mSubmitDiscount.visibility = GONE
                mDiscountCode.visibility = GONE

                mDiscountText.setTextColor(resources.getColor(R.color.shop_cart_payment_method_item_discount_color_unchecked))
            }
        }

        mDepositTitle.setTextColor(resources.getColor(R.color.shop_cart_payment_method_deposit_color))
        mDepositTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_payment_method_deposit_size).toFloat())
        mDepositTitle.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
        mDepositTitle.text = resources.getString(R.string.shop_cart_deposit_title)

        mDepositValue.setTextColor(resources.getColor(R.color.shop_cart_payment_method_deposit_color))
        mDepositValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_payment_method_item_deposit_message_text_size).toFloat())
        mDepositValue.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_bold)

        mDepositValueDescription.setTextColor(resources.getColor(R.color.shop_cart_payment_method_deposit_color))
        mDepositValueDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_payment_method_deposit_size).toFloat())
        mDepositValueDescription.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
        mDepositValueDescription.text = resources.getString(R.string.rial)

        mSwitchText.setTextColor(resources.getColor(R.color.shop_cart_payment_method_deposit_color))
        mSwitchText.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_payment_method_switch_text_size).toFloat())
        mSwitchText.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
        mSwitchText.text = resources.getString(R.string.shop_cart_switch_title)


    }


    fun setDiscountCodeAction(action: SubmitDiscountCode) {

        mAction = action

        mSubmitDiscount.setClickListener(OnClickListener {
            if (mDiscountCode.getText().isNotEmpty()) {
                mAction.onSubmitCode(Utils.toEnglishNumber(mDiscountCode.getText().toString()))
            }
        })
    }


    fun setOnPayItemsClicked(listener: PaymentItemsSelectedListener) {

        mItemsSelectedListener = listener
    }

    fun setAddDepositClickListener(action: OnClickListener) {

//        mChooseIncreasDeposit.setOnClickListener(action)
    }

    fun setDepositValue(deposit: Long) {
        mDepositValue.text = Utils.toPersianPriceNumber(deposit)
    }

    fun setSubmitButtonLoadingVisibility(visibility: Int) {
        mSubmitDiscount.setLoadingVisibility(visibility = visibility)
    }

//    fun setBank(bankList: List<Bank>) {
//        if (contentView.getmBankViews().isEmpty()) {
//            contentView.removeAllViews()
//            contentView.setBankList(bankList)
////            Toast.makeText(context,"1111",Toast.LENGTH_SHORT).show()
//        }
//
//
//    }

}