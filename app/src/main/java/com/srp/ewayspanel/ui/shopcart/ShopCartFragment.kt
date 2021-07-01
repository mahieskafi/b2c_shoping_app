package com.srp.ewayspanel.ui.shopcart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.collection.SparseArrayCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.srp.eways.BuildConfig
import com.srp.eways.model.deposit.IncreaseDepositResponse
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.ui.deposit.increasedeposit.IncreaseDepositViewModel
import com.srp.eways.ui.login.UserInfoViewModel
import com.srp.eways.ui.navigation.*
import com.srp.eways.ui.view.toolbar.WeiredToolbar
import com.srp.eways.ui.webview.MainWebFragment
import com.srp.eways.util.Utils
import com.srp.eways.util.analytic.AnalyticConstant
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.ui.shopcart.address.AddressFragment
import com.srp.ewayspanel.ui.shopcart.confirm.ShopCartConfirmAndPaymentFragment
import com.srp.ewayspanel.ui.shopcart.confirm.ShopCartPaymentDialog
import com.srp.ewayspanel.ui.shopcart.productlist.ShopCartProductListFragment
import com.srp.ewayspanel.ui.shopcart.reciept.ShopReceiptFragment
import com.srp.ewayspanel.ui.transaction.order.OrderTransactionViewModel
import com.srp.ewayspanel.ui.webview.WebFragment

/**
 * Created by ErfanG on 12/10/2019.
 */
class ShopCartFragment : NavigationMemberFragment<ShopCartViewModel>() {


    interface ShopCartNextLevelListener {

        fun onLevelChanged(currentLevel: Int, nextLevel: Int)
    }

    interface ShopCartProgressTextListener {

        fun onShow(show: Boolean)

        fun isShowing(): Boolean
    }


    companion object {

        const val SHOP_CART_LIST = 0
        const val SHOP_CART_ADDRESS = 1
        const val SHOP_CART_CONFIRMATION = 2
        const val SHOP_CART_PAY_DEPOSIT = 3
        const val SHOP_CART_PAY_DARGAH = 4

        const val NUMBER_OF_LEVELS = 3

        fun newInstance(): ShopCartFragment {
            val fragment = ShopCartFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var mNavigationController: NavigationController

    private lateinit var mToolbar: WeiredToolbar
    private lateinit var mBuyProgressBar: BuyProgressBar

    private lateinit var mNextLevelListener: ShopCartNextLevelListener
    private var mCurrentState = SHOP_CART_LIST

    private val confirmationFragment = ShopCartConfirmAndPaymentFragment.newInstance()
    private var check: Boolean = true

    private var mSwitchedRoot = 0

    override fun acquireViewModel(): ShopCartViewModel {
        return DI.getViewModel(ShopCartViewModel::class.java)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_shop_cart
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mNextLevelListener = object : ShopCartNextLevelListener {
            override fun onLevelChanged(currentLevel: Int, nextLevel: Int) {

                onSwitchRoot(nextLevel)

                mCurrentState = nextLevel

                mBuyProgressBar.goStateAt(mCurrentState + 1)

                when (nextLevel) {
                    SHOP_CART_LIST -> {
                        mBuyProgressBar.setTextVisibility(View.VISIBLE)
                        mToolbar.setTitle(resources.getString(R.string.shop_cart_product_list_fragment_title))
                    }
                    SHOP_CART_ADDRESS -> {
                        mBuyProgressBar.setTextVisibility(View.VISIBLE)
                        mToolbar.setTitle(resources.getString(R.string.shop_cart_address_fragment_title))
                    }
                    SHOP_CART_CONFIRMATION -> {
                        mBuyProgressBar.setTextVisibility(View.VISIBLE)
                        mToolbar.setTitle(resources.getString(R.string.shop_cart_confirmation_fragment_title))


                    }
                }
                mToolbar.requestLayout()
            }
        }

        val resources = DI.getABResources()

        mToolbar = view.findViewById(R.id.toolbar)
        mBuyProgressBar = view.findViewById(R.id.buy_progress_bar)

        setUpToolbar()

//        mBuyProgressBar.background = resources.getDrawable(R.drawable.shop_cart_fragment_buy_progress_background)
        mBuyProgressBar.setTextVisibility(View.VISIBLE)
        mBuyProgressBar.setAttributes(
                BuyProgressBar.BuyProgressBarType.PROGRESSIVE,
                NUMBER_OF_LEVELS,
                arrayListOf(resources.getString(R.string.shop_cart_progress_bar_payment_title), resources.getString(R.string.shop_cart_progress_bar_address_page_title), resources.getString(R.string.shop_cart_progress_bar_product_list_title)),
                arrayListOf(resources.getDrawable(R.drawable.buy_progress_bar_confirm_and_pay_selected), resources.getDrawable(R.drawable.buy_progress_bar_address_selected), resources.getDrawable(R.drawable.buy_progress_bar_check_selected)),
                arrayListOf(resources.getDrawable(R.drawable.buy_progress_bar_confirm_and_pay_unselected), resources.getDrawable(R.drawable.buy_progress_bar_address_unselected), resources.getDrawable(R.drawable.buy_progress_bar_check_selected)),
                null
        )
        mBuyProgressBar.requestLayout()
        mBuyProgressBar.goStateAt(1)

        mNavigationController = NavigationController(
                childFragmentManager,
                R.id.fragment_container,
                createNavigationRoots(),
                getRootTabId(),
                NavigationType.NAVIGATION_TYPE_TAB)

        viewModel.getShopCartProductList().observe(viewLifecycleOwner, Observer { newData ->

            if (newData != null && newData.size == 0) {
                mBuyProgressBar.visibility = View.GONE
            } else {
                mBuyProgressBar.visibility = View.VISIBLE
            }

        })

        viewModel.callGetShopCartList()

        viewModel.getBuyResponse().observe(viewLifecycleOwner, Observer { buyResponse ->

            if (buyResponse != null && buyResponse.status == NetworkResponseCodes.SUCCESS) {
                if (buyResponse.url == null || buyResponse.url?.isEmpty()!!) {
                    var userInfoViewModel: UserInfoViewModel = DI.getViewModel(UserInfoViewModel::class.java)
                    userInfoViewModel.invalidateCredit()
                    userInfoViewModel.getCredit()

                    DI.getEventSender().sendAction(AnalyticConstant.BUY, AnalyticConstant.BUY_ACTION, AnalyticConstant.BUY_TYPE, "deposit")

                    val orderTransactionViewModel: OrderTransactionViewModel = DI.getViewModel(OrderTransactionViewModel::class.java)
                    orderTransactionViewModel.reNewList()

                    openFragment(ShopReceiptFragment.newInstance(buyResponse), NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR)
                } else {

                    openFragment(WebFragment.newInstance(BuildConfig.DARGAH_URL + buyResponse.url), NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR)

                    viewModel.consumeBuyResponseLiveData()
                }
            }
        })

        viewModel.getBuyResponseError().observe(viewLifecycleOwner, Observer { errorMessage ->

            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()

                viewModel.consumeBuyResponseError()
            }
        })
    }


    internal fun createNavigationRoots(): SparseArrayCompat<BackStackMember> {

        val roots = SparseArrayCompat<BackStackMember>(3)

        val productListFragment = ShopCartProductListFragment.newInstance()
        productListFragment.setNextLevelAction(mNextLevelListener)
        productListFragment.setScrollListener(object : ShopCartProgressTextListener {
            override fun onShow(show: Boolean) {
                if (show) {
                    mBuyProgressBar.setTextVisibility(View.VISIBLE)
                } else {
                    mBuyProgressBar.setTextVisibility(View.GONE)
                }
            }

            override fun isShowing(): Boolean {
                return mBuyProgressBar.getTextVisibility() == View.VISIBLE
            }

        })

        val addressFragment = AddressFragment.newInstance()
        addressFragment.setNextLevelAction(mNextLevelListener)

        confirmationFragment.setNextLevelAction(object : ShopCartNextLevelListener {
            @SuppressLint("FragmentLiveDataObserve")
            override fun onLevelChanged(currentLevel: Int, nextLevel: Int) {

                if (nextLevel == SHOP_CART_PAY_DEPOSIT) {
                    val userInfoViewModel = DI.getViewModel(UserInfoViewModel::class.java)
                    val depositValue = userInfoViewModel.getCreditLiveData().value!!
                    val payingPrice = viewModel.getShopCartItem().value!!.payingPrice

                    if (depositValue >= payingPrice) {
                        viewModel.setBank(null)
                        viewModel.buy()
                    } else {
                        val increaseDepositViewModel = DI.getViewModel(IncreaseDepositViewModel::class.java)
                        if (confirmationFragment.getBank() != null) {
                            if (payingPrice - depositValue < 100000) {
                                var shopCartPaymentDialog = ShopCartPaymentDialog(context!!, object : ShopCartPaymentDialog.ShopCartPaymentDialogActionListeners {
                                    override fun onCancel() {
                                    }

                                    override fun onPayment() {
                                        increaseDepositViewModel.setBankId(confirmationFragment.getBank()!!.gId)
                                        increaseDepositViewModel.onAmountChanged(100000)
                                        increaseDepositViewModel.increaseDeposit()
                                    }

                                })
                                shopCartPaymentDialog.show()
                                shopCartPaymentDialog.setTitleValue(100000 - (payingPrice - depositValue))
                            } else {
                                var shopCartPaymentDialog = ShopCartPaymentDialog(context!!, object : ShopCartPaymentDialog.ShopCartPaymentDialogActionListeners {
                                    override fun onCancel() {
                                    }

                                    override fun onPayment() {
                                        increaseDepositViewModel.setBankId(confirmationFragment.getBank()!!.gId)
                                        increaseDepositViewModel.onAmountChanged(payingPrice - depositValue)
                                        increaseDepositViewModel.increaseDeposit()
                                    }

                                })
                                shopCartPaymentDialog.show()
                                shopCartPaymentDialog.setDescription(payingPrice - depositValue)
                            }

                            increaseDepositViewModel.getIncreaseDepositLiveData().observe(viewLifecycleOwner, Observer<IncreaseDepositResponse> {
                                if (it != null) {
                                    if (it.status == 0 && it.url.isNotEmpty()) {
                                        openFragment(MainWebFragment(BuildConfig.DARGAH_URL.toString() + it.url), NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR)
                                    } else {
                                        Toast.makeText(context, it.description, Toast.LENGTH_LONG).show()
                                    }
                                    increaseDepositViewModel.consumeAmountLiveData()
                                    increaseDepositViewModel.consumeStatusLiveData()
                                    increaseDepositViewModel.consumeIncreaseDepositResponseLiveData()
                                }
                            })

                            increaseDepositViewModel.getIncreaseDepositStatusLiveData().observe(viewLifecycleOwner, Observer {
                                if (it != null) {
                                    if (it.status == 0) {
                                        userInfoViewModel.onCreditIncreased(true)
                                        viewModel.setBank(null)
                                        viewModel.buy()
                                    } else {
                                        userInfoViewModel.onCreditIncreased(false)
                                    }
                                    increaseDepositViewModel.consumeStatusLiveData()
                                }
                            })
                        } else {
                            Toast.makeText(context, resources.getString(R.string.shop_cart_dargah_is_not_valid), Toast.LENGTH_SHORT).show()
                            confirmationFragment.callBankList()
                        }
                    }

                } else if (nextLevel == SHOP_CART_PAY_DARGAH) {
                    var payingPrice: Long = viewModel.getShopCartItem().value!!.payingPrice
                    if (payingPrice in 20000..500000000) {
                        if (confirmationFragment.getBank() != null) {
                            viewModel.setBank(confirmationFragment.getBank())
                            viewModel.buy()
                        } else {
                            Toast.makeText(context, resources.getString(R.string.shop_cart_dargah_is_not_valid), Toast.LENGTH_SHORT).show()
                            confirmationFragment.callBankList()
                        }

                    } else {
                        Toast.makeText(context, getString(R.string.paying_price_invalid), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })


        roots.put(ShopCartRootIds.ROOT_ITEM_LIST, BackStackMember(productListFragment))
        roots.put(ShopCartRootIds.ROOT_ADDRESS, BackStackMember(addressFragment))
        roots.put(ShopCartRootIds.ROOT_CONFIRMATION, BackStackMember(confirmationFragment))

        return roots
    }

    internal fun getRootTabId(): Int {
        return ShopCartRootIds.ROOT_ITEM_LIST
    }

    private fun setUpToolbar() {

        val resources = DI.getABResources()

        mToolbar.setShowShop(false)
        mToolbar.setShowTitle(true)
        mToolbar.setShowNavigationUp(true)
        mToolbar.setShowDeposit(false)
        mToolbar.setShowNavigationDrawerMenu(true)
        mToolbar.setOnNavigationDrawerClickListener { toggleDrawer() }
        mToolbar.setOnBackClickListener { onBackPressed() }

        mToolbar.setBackgroundColor(resources.getColor(R.color.shop_cart_fragment_toolbar_color))
        mToolbar.setTitle(resources.getString(R.string.shop_cart_product_list_fragment_title))
        mToolbar.setTitleTextSize(resources.getDimenPixelSize(R.dimen.shop_cart_title_text_size).toFloat())

    }

    override fun onBackPress(): Boolean {

        var returnValue = false

        if (mCurrentState == SHOP_CART_LIST) {
            mNavigationController.onBackPress()
            returnValue = false

        } else {
            mCurrentState--
            onSwitchRoot(mCurrentState)
            mBuyProgressBar.goStateAt(mCurrentState + 1)

            when (mCurrentState) {
                SHOP_CART_LIST -> {
                    mBuyProgressBar.setTextVisibility(View.VISIBLE)
                    mToolbar.setTitle(resources.getString(R.string.shop_cart_product_list_fragment_title))
                }
                SHOP_CART_ADDRESS -> {
                    mBuyProgressBar.setTextVisibility(View.VISIBLE)
                    mToolbar.setTitle(resources.getString(R.string.shop_cart_address_fragment_title))
                }
                SHOP_CART_CONFIRMATION -> {
                    mBuyProgressBar.setTextVisibility(View.VISIBLE)
                    mToolbar.setTitle(resources.getString(R.string.shop_cart_confirmation_fragment_title))

                }
            }
            mToolbar.requestLayout()
            returnValue = true
        }

        return returnValue
    }

    override fun openFragment(fragment: Fragment?) {
        mNavigationController.openFragment(fragment)
    }

    override fun onSwitchRoot(id: Int) {
        Utils.hideKeyboard(activity)

        mSwitchedRoot = id
        mNavigationController.onSwitchRoot(id)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        mNavigationController.getBackStackMember(mSwitchedRoot).fragment.onHiddenChanged(hidden)
    }

}