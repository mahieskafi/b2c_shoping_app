package com.srp.ewayspanel.ui.shopcart.confirm

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import com.srp.eways.model.deposit.Bank
import com.srp.eways.ui.deposit.DepositFragment
import com.srp.eways.ui.deposit.increasedeposit.IncreaseDepositViewModel
import com.srp.eways.ui.login.UserInfoViewModel
import com.srp.eways.ui.navigation.NavigationMemberFragment
import com.srp.eways.util.BillUtil
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.login.Address
import com.srp.ewayspanel.ui.shopcart.ShopCartFragment
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel
import com.srp.ewayspanel.ui.view.shopcart.ReceiverDetailView
import com.srp.ewayspanel.ui.view.store.basket.OrderSummaryView
import java.util.*

/**
 * Created by ErfanG on 1/15/2020.
 */
class ShopCartConfirmAndPaymentFragment : NavigationMemberFragment<ShopCartViewModel>() {

    private lateinit var mPayListener: ShopCartFragment.ShopCartNextLevelListener

    private lateinit var mUserInfoViewModel: UserInfoViewModel

    //Views
    private lateinit var mOrderSummaryView: OrderSummaryView

    private lateinit var mInfoSummaryTitle: AppCompatTextView
    private lateinit var mInfoSummaryCard: CardView
    private lateinit var mInfoSummary: ReceiverDetailView

    private lateinit var mPay: ShopCartPaymentMethodView
    private var mBank: Bank? = null

//    private lateinit var mEditAddressIcon: AppCompatImageView

    private var mPaymentType: Int = ShopCartFragment.SHOP_CART_PAY_DARGAH


    private lateinit var mBuyInProgressObserver: Observer<Boolean>
    private lateinit var mGetBankListInProgressObserver: Observer<Boolean>

    companion object {

        fun newInstance(): ShopCartConfirmAndPaymentFragment {
            val fragment = ShopCartConfirmAndPaymentFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun acquireViewModel(): ShopCartViewModel {

        return DI.getViewModel(ShopCartViewModel::class.java)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_shop_cart_confirm_payment
    }

    override fun onStart() {
        super.onStart()

        viewModel.isBuyInProgress().observe(this, mBuyInProgressObserver)
        DI.getViewModel(IncreaseDepositViewModel::class.java).isLoading.observe(this, mGetBankListInProgressObserver)
    }

    override fun onStop() {
        super.onStop()

        viewModel.isBuyInProgress().removeObserver(mBuyInProgressObserver)
        DI.getViewModel(IncreaseDepositViewModel::class.java).isLoading.removeObserver(mGetBankListInProgressObserver)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mUserInfoViewModel = DI.getViewModel(UserInfoViewModel::class.java)

        mOrderSummaryView = view.findViewById(R.id.order_summary)

        mInfoSummaryTitle = view.findViewById(R.id.information_summary_title)
        mInfoSummaryCard = view.findViewById(R.id.information_summary_card)
        mInfoSummary = view.findViewById(R.id.information_summary)
//        mEditAddressIcon = view.findViewById(R.id.iv_edit)


        mPay = view.findViewById(R.id.payment_view)

        callBankList()

        val resources = DI.getABResources()

        mBuyInProgressObserver = Observer {
            if (it != null) {
                handleBuyButton(it)
            }
        }

        mGetBankListInProgressObserver = Observer {
            if (it != null) {
                handleBuyButton(it)
            }
        }


        //region Set Attrs

        ViewCompat.setElevation(mOrderSummaryView, resources.getDimenPixelSize(R.dimen.shop_card_confirm_order_summary_elevation).toFloat())
        mOrderSummaryView.collapseView()
        mOrderSummaryView.setNextLevelButtonEnabled(false)
        mOrderSummaryView.setNextLevelButtonLoading(true)
        mOrderSummaryView.nextLevelButton.setText(resources.getString(R.string.order_summary_buy_button_text))

        mInfoSummaryTitle.text = resources.getString(R.string.shop_cart_confirmation_info_summary_title)
        mInfoSummaryTitle.typeface = ResourcesCompat.getFont(context!!, R.font.iran_yekan_medium)
        mInfoSummaryTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_confirmation_info_summary_title_size).toFloat())
        mInfoSummaryTitle.setTextColor(resources.getColor(R.color.shop_cart_confirmation_info_summary_title_color))

        val detailCardPadding: Int = resources.getDimenPixelSize(R.dimen.shop_cart_confirm_info_summary_card_padding)

        mInfoSummaryCard.setPadding(detailCardPadding, detailCardPadding, detailCardPadding, detailCardPadding)
        mInfoSummaryCard.background = resources.getDrawable(R.drawable.address_detail_card_backgound)
        mInfoSummaryCard.cardElevation = resources.getDimenPixelSize(R.dimen.shop_cart_confirm_info_summary_card_elevation).toFloat()
        mInfoSummaryCard.radius = resources.getDimenPixelSize(R.dimen.shop_cart_confirm_info_summary_card_radius).toFloat()

        setAddress(viewModel.getSelectedAddress()!!)


        val payTitleMargin: Int = resources.getDimenPixelSize(R.dimen.shop_cart_confirm_info_summary_title_margin_right)
        val payTitleLayParam = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        payTitleLayParam.setMargins(payTitleMargin, 0, payTitleMargin, 0)


        mPay.setDepositValue(mUserInfoViewModel.getCreditLiveData().value!!.toLong())

        mPay.setAddDepositClickListener(View.OnClickListener {
            val payingPrice: Long = viewModel.getShopCartItem().value!!.payingPrice.toLong()
            val currentCredit: Long = mUserInfoViewModel.getCreditLiveData().value!!.toLong()

            if (currentCredit < payingPrice) {
                openFragment(DepositFragment.newInstance(payingPrice - currentCredit), NAVIGATION_VIEW_TYPE_HAS_TOOLBAR)
            } else {
                openFragment(DepositFragment.newInstance(), NAVIGATION_VIEW_TYPE_HAS_TOOLBAR)
            }
        })

        mPay.setDiscountCodeAction(object : ShopCartPaymentMethodView.SubmitDiscountCode {
            override fun onSubmitCode(discountCode: String) {
                mPay.setSubmitButtonLoadingVisibility(View.VISIBLE)
                viewModel.callGetShopCartList(discountCode)
            }

            override fun onCodeCleared() {
                mPay.setSubmitButtonLoadingVisibility(View.VISIBLE)
                viewModel.callGetShopCartList()
            }
        })

        mPay.setOnPayItemsClicked(object : ShopCartPaymentMethodView.PaymentItemsSelectedListener {
            override fun onItemSelected(item: Int) {
                mPaymentType = item
            }

        })

        observeToShippingPrice()

        mOrderSummaryView.setOnNextLevelButtonAction {
            mPayListener.onLevelChanged(ShopCartFragment.SHOP_CART_CONFIRMATION, mPaymentType)
        }

        viewModel.getCouponError().observe(viewLifecycleOwner, Observer {
            mPay.setSubmitButtonLoadingVisibility(View.GONE)
            if (it != null) {
                if (it.isEmpty()) {
                    Toast.makeText(context, getString(R.string.shop_cart_payment_coupon_code_error), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
                viewModel.consumeCouponError()

            }
        })

        viewModel.getDiscountCodeLiveData().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.isNotEmpty()) {
                    updatePrices(viewModel.getShippingPrice().value!!)
                    Toast.makeText(context, getString(R.string.shop_cart_payment_coupon_code_success), Toast.LENGTH_LONG).show()
                }
                viewModel.consumeDiscountCodeLiveData()

            }
        })
    }

    private fun observeToShippingPrice() {
        viewModel.getShippingPrice().observe(viewLifecycleOwner, Observer<Long> { price ->

            updatePrices(price)
        })
    }

    private fun updatePrices(price: Long) {
        val extractedPrices: ArrayList<Long>? = viewModel.calculatePrices(price, viewModel.getShopCartProductList().value!!)

        if (extractedPrices != null) {
            mOrderSummaryView.resetData()
            mOrderSummaryView.setTotalRealPrice(extractedPrices[0])
            mOrderSummaryView.setDeliveryPrice(viewModel.getSelectedPostType()!!.title, price)
            mOrderSummaryView.setTotalDiscount(extractedPrices[2])
            mOrderSummaryView.setTotalPrice(extractedPrices[1])
            mOrderSummaryView.setTotalScore(extractedPrices[3])
        } else {
            mOrderSummaryView.visibility = GONE
        }
    }

    private fun handleBuyButton(isLoading: Boolean) {

        with(mOrderSummaryView.getNextLevelButton()) {

            setEnable(!isLoading)
            isClickable = !isLoading

            if (isLoading) {
                setLoadingVisibility(View.VISIBLE)
            } else {
                setLoadingVisibility(View.INVISIBLE)
            }
        }
    }

    private fun setAddress(address: Address) {

        mInfoSummary.setName(address.fullName)
        mInfoSummary.setPhone(address.phoneNumber)
        mInfoSummary.setMobile(address.mobile)
        mInfoSummary.setState(address.stateName)
        mInfoSummary.setCity(address.cityName)
        mInfoSummary.setPostCode(address.postCode)
        mInfoSummary.setAddress(address.address)
    }


    fun setNextLevelAction(nextLevelListener: ShopCartFragment.ShopCartNextLevelListener) {

        mPayListener = nextLevelListener


    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            observeToShippingPrice()
            setAddress(viewModel.getSelectedAddress()!!)
//            observeToProductList()
        }

    }

    fun getBank(): Bank? {
        return mBank
    }


    public fun callBankList() {
        var increaseDepositViewModel: IncreaseDepositViewModel? = DI.getViewModel(IncreaseDepositViewModel::class.java)
        increaseDepositViewModel?.getBankList(true)
        increaseDepositViewModel?.getBankListLiveData()?.observe(viewLifecycleOwner, Observer { bankListResponse ->
            if (bankListResponse != null) {
                if (bankListResponse.status == 0 && bankListResponse.items != null) {

                    var bankList = bankListResponse.items as MutableList
                    mBank = bankList.findLast { it.gId == BillUtil.MELLAT_BANK || it.gId == BillUtil.MELLAT_BANK2 }
                    if (mBank == null) {
                        mBank = bankList.findLast {
                            it.gId == BillUtil.PERSIAN_BANK || it.gId == BillUtil.PERSIAN_BANK2 ||
                                    it.gId == BillUtil.PERSIAN_BANK3
                        }
                    }
                    if (mBank != null) {
                        mOrderSummaryView.setNextLevelButtonEnabled(true)
                    }
                    mOrderSummaryView.setNextLevelButtonLoading(false)
                } else {
                    Toast.makeText(context, bankListResponse.description, Toast.LENGTH_LONG).show()
                }
//                increaseDepositViewModel.consumeBankListLiveData()
            }
        })
    }

}