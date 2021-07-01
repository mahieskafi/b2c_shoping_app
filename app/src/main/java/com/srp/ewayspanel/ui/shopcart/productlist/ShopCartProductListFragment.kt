package com.srp.ewayspanel.ui.shopcart.productlist

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srp.eways.ui.navigation.NavigationMemberFragment
import com.srp.eways.ui.navigation.NavigationViewType
import com.srp.eways.ui.view.LoadingStateView
import com.srp.eways.util.Utils
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.shopcart.ProductInfo
import com.srp.ewayspanel.model.shopcart.ShopCartItemModel
import com.srp.ewayspanel.ui.shopcart.ShopCartFragment
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel
import com.srp.ewayspanel.ui.store.product.detail.ProductDetailFragment
import com.srp.ewayspanel.ui.view.store.basket.OrderSummaryView
import kotlin.math.min

class ShopCartProductListFragment : NavigationMemberFragment<ShopCartViewModel>() {

    private val resources = DI.getABResources()
    private lateinit var mProductList: RecyclerView
    private lateinit var mOrderSummary: OrderSummaryView
    private var mRemoveDialog: ShopCartRemoveProductDialog? = null

    private lateinit var mProductListAdapter: ShopCartProductListAdapter
    private lateinit var mNextLevelAction: ShopCartFragment.ShopCartNextLevelListener

    private lateinit var mScrollListener: ShopCartFragment.ShopCartProgressTextListener

    private var mIsKeyBoardOpened: Boolean = false
    private var mEditedItemPosition: Int = -1

    companion object {

        fun newInstance(): ShopCartProductListFragment {
            val fragment = ShopCartProductListFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun acquireViewModel(): ShopCartViewModel {
        return DI.getViewModel(ShopCartViewModel::class.java)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_shop_cart_product_list
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mProductList = view.findViewById(R.id.product_list)
        mOrderSummary = view.findViewById(R.id.order_summary)

        applyScrollListener()

        ViewCompat.setElevation(mOrderSummary, resources.getDimenPixelSize(R.dimen.shop_card_list_order_summary_elevation).toFloat())
        mOrderSummary.collapseView()

        viewModel.getShopCartProductList().observe(viewLifecycleOwner, Observer<ArrayList<ShopCartItemModel>> { newList ->
            if (mProductListAdapter.itemCount == 1) {
                mProductListAdapter.setData(newList)
                mProductListAdapter.notifyDataSetChanged()
            } else {
                mProductListAdapter.setData(newList)
                if (!mIsKeyBoardOpened && mEditedItemPosition != -1) {
                    mProductListAdapter.notifyDataSetChanged()
                    mEditedItemPosition = -1
                }
            }
            mOrderSummary.setNextLevelButtonLoading(false)
            mRemoveDialog?.dismiss()
            mRemoveDialog = null


            if (newList != null && newList.size > 0) {
                val extractedPrices = viewModel.calculatePrices(0, newList)

                if (extractedPrices != null) {
                    mOrderSummary.resetData()
                    mOrderSummary.setTotalRealPrice(extractedPrices[0])
                    mOrderSummary.setTotalPrice(extractedPrices[1])
                    mOrderSummary.setTotalDiscount(extractedPrices[2])
                    mOrderSummary.setTotalScore(extractedPrices[3])

                    mOrderSummary.visibility = View.VISIBLE
                } else {
                    mOrderSummary.visibility = View.GONE
                }
            } else {
                mOrderSummary.visibility = View.GONE
            }
        })

        mOrderSummary.setOnNextLevelButtonAction {
            if (viewModel.getShopCartItem().value != null) {
                if (viewModel.hasAnythingToChange()) {
                    Toast.makeText(context, resources.getString(R.string.shop_cart_product_list_next_level_error_message), Toast.LENGTH_LONG).show()
                } else if (viewModel.getShopCartItem().value!!.basket.size == 0) {
                    Toast.makeText(context, resources.getString(R.string.shop_cart_product_list_next_level_empty_list_error_message), Toast.LENGTH_LONG).show()
                } else if (hasLackItem(viewModel.getShopCartItem().value!!.basket)) {
                    Toast.makeText(context, resources.getString(R.string.shop_cart_product_list_next_level_has_lack_error_message), Toast.LENGTH_LONG).show()
                } else if (hasLessQuantityItem(viewModel.getShopCartItem().value!!.basket)) {
                    Toast.makeText(context, resources.getString(R.string.shop_cart_product_list_next_level_has_less_count_error_message), Toast.LENGTH_LONG).show()
                } else {
                    mNextLevelAction.onLevelChanged(ShopCartFragment.SHOP_CART_LIST, ShopCartFragment.SHOP_CART_ADDRESS)
                    //TODO switch root
                }
            }
        }


        Utils.observeKeyboardVisibility(view) { visible ->
            mIsKeyBoardOpened = visible

            if (!visible) {
                if (mEditedItemPosition != -1) {
                    mProductListAdapter.notifyItemChanged(mEditedItemPosition)
                    mEditedItemPosition = -1
                } else {
                    mOrderSummary.setNextLevelButtonLoading(false)
                }
            } else {
                mOrderSummary.setNextLevelButtonLoading(true)
            }
        }


        mProductListAdapter = ShopCartProductListAdapter(context!!,
                LoadingStateView.RetryListener { viewModel.callGetShopCartList() },
                object : ShopCartProductListAdapter.ItemCountChangeListener {
                    override fun itemCountChanged(productInfo: ProductInfo, productCount: Int, position: Int, addOrRemove: Boolean) {

                        mOrderSummary.setNextLevelButtonLoading(true)
                        mEditedItemPosition = position
                        if (addOrRemove) {
                            viewModel.addProduct(productInfo, productCount, true)

                        } else {
                            if (productCount != 0) {
                                viewModel.removeProduct(productInfo.id, productCount, true)

                            }
                        }

                        if (!addOrRemove && productCount == 0) {

                            mRemoveDialog = ShopCartRemoveProductDialog(context!!, object : ShopCartRemoveProductDialog.ShopCartRemoveProductDialogActionListeners {
                                override fun onClose() {
                                    mRemoveDialog?.dismiss()
                                    mRemoveDialog = null

                                }

                                override fun onRemove() {
                                    mProductListAdapter.notifyItemRemoved(position)
                                    viewModel.removeFromCart(productInfo.id)

                                    //Due to remove dialog immediately


                                }

                                override fun onKeep() {
                                    mOrderSummary.setNextLevelButtonLoading(false)
                                    mRemoveDialog?.dismiss()
                                    mRemoveDialog = null
                                }

                            }, ShopCartRemoveProductDialog.DialogMode.SINGLE_MODE)
                            mRemoveDialog!!.show()
                        }
                    }

                    override fun openProductDetail(item: ProductInfo) {
                        val productInfo = com.srp.ewayspanel.model.storepage.filter.ProductInfo(id = item.id, name = item.name, seoName = item.seoName, price = item.price, oldPrice = item.oldPrice, imageUrl = item.imageUrl, stock = item.stock, availability = item.availability,
                                minOrder = item.minOrder, maxOrder = item.maxOrder, overInventoryCount = item.overInventoryCount, point = item.point.toLong(), discount = item.discount,
                                lawId = item.lawId, isSim = item.isSim)

                        openFragment(ProductDetailFragment.newInstance(productInfo), NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR)
                    }


                },
                View.OnClickListener {
                    if (!viewModel.hasAnythingToChange()) {
                        mRemoveDialog = ShopCartRemoveProductDialog(context!!, object : ShopCartRemoveProductDialog.ShopCartRemoveProductDialogActionListeners {
                            override fun onClose() {
                                mRemoveDialog?.dismiss()
                                mRemoveDialog = null
                            }

                            override fun onRemove() {
                                viewModel.removeAllProducts()

                                //Due to remove dialog immediately
//                                mRemoveDialog?.dismiss()
//                                mRemoveDialog = null

                            }

                            override fun onKeep() {
                                mRemoveDialog?.dismiss()
                                mRemoveDialog = null
                            }

                        }, ShopCartRemoveProductDialog.DialogMode.ALL_MODE)
                        mRemoveDialog!!.show()
                    } else {
                        Toast.makeText(context, resources.getString(R.string.shop_cart_product_list_next_level_error_message), Toast.LENGTH_LONG).show()
                    }
                }
        )

        viewModel.isLoadingMainList().observe(this, Observer<Boolean> { isLoading ->
            mProductListAdapter.setIsLoading(isLoading)
        })
        viewModel.getMainListError().observe(this, Observer<Int> { errorCode ->
            mProductListAdapter.setErrorCode(errorCode)
        })

        viewModel.hasAnythingToChangeLiveData().observe(this, Observer<Boolean> {
            if (!it) {
                if (mRemoveDialog != null) {
                    mRemoveDialog!!.dismiss()
                    mRemoveDialog = null
                }
            }
        })

        viewModel.callGetShopCartList()

        mProductList.layoutManager = LinearLayoutManager(context)

        mProductList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                if (view is ShopCartItemView) {
                    with(outRect) {
                        left = resources.getDimenPixelSize(R.dimen.shop_cart_fragment_product_item_margin_side)
                        right = resources.getDimenPixelSize(R.dimen.shop_cart_fragment_product_item_margin_side)
                        top = resources.getDimenPixelSize(R.dimen.shop_cart_fragment_product_item_margin_top)
                        bottom = resources.getDimenPixelSize(R.dimen.shop_cart_fragment_product_item_margin_bottom)
                    }

                }
            }
        })

        mProductList.adapter = mProductListAdapter
    }

    private fun hasLackItem(basket: ArrayList<ShopCartItemModel>): Boolean {

        for (item in basket) {
            if (!item.productInfo!!.availability) {
                return true
            }
        }

        return false
    }

    private fun hasLessQuantityItem(basket: ArrayList<ShopCartItemModel>): Boolean {

        for (item in basket) {
            if (min(item.productInfo!!.stock, item.productInfo!!.maxOrder) < item.count) {
                return true
            }
        }

        return false
    }

    fun setNextLevelAction(nextLevelAction: ShopCartFragment.ShopCartNextLevelListener) {
        mNextLevelAction = nextLevelAction
    }

    fun setScrollListener(scrollListener: ShopCartFragment.ShopCartProgressTextListener) {

        mScrollListener = scrollListener
        applyScrollListener()

    }

    private fun applyScrollListener() {
        if (::mProductList.isInitialized && ::mScrollListener.isInitialized) {

            mProductList.clearOnScrollListeners()

            mProductList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                    Utils.hideKeyboard(activity)

                    val position = (mProductList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                    if (mScrollListener.isShowing()) {
                        if (position > 1) {
                            mScrollListener.onShow(false)
                        }
                    } else {
                        if (position == 0) {
                            mScrollListener.onShow(true)
                        }
                    }
                }
            })
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            viewModel.callGetShopCartList()
        }
    }
}
