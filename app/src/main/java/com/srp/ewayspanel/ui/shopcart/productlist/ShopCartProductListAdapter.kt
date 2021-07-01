package com.srp.ewayspanel.ui.shopcart.productlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.srp.eways.network.NetworkResponseCodes
import com.srp.eways.network.NetworkUtil
import com.srp.eways.ui.navigation.NavigationViewType
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.shopcart.ShopCartItemModel
import com.srp.ewayspanel.ui.view.EmptyShopCartView
import com.srp.eways.ui.view.LoadingStateView
import com.srp.ewayspanel.model.shopcart.ProductInfo
import com.srp.ewayspanel.model.storepage.mainpage.Item
import com.srp.ewayspanel.ui.store.product.detail.ProductDetailFragment

/**
 * Created by ErfanG on 12/10/2019.
 */
class ShopCartProductListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {


    interface ItemCountChangeListener {
        fun itemCountChanged(productInfo: ProductInfo, productCount: Int, position: Int, addOrRemove: Boolean)
        fun openProductDetail(productInfo: ProductInfo)
    }

    companion object {
        private const val VIEW_TYPE_LOADING = 0
        private const val VIEW_TYPE_NO_DATA = 1
        private const val VIEW_TYPE_TOP_LIST = 2
        private const val VIEW_TYPE_ITEM = 3
    }

    private val mContext: Context
    private var mData = ArrayList<ShopCartItemModel>()
    private val mItemCountChangeListener: ItemCountChangeListener

    private var mRetryViewHolder: RetryViewHolder? = null
    private var mRetryListener: LoadingStateView.RetryListener
    private val mDeleteAllAction: View.OnClickListener

    private var mErrorCode = NetworkUtil.SUCCESS
    private var mIsLoading = true

    constructor(context: Context,
                retryClickListener: LoadingStateView.RetryListener,
                itemCountChangeListener: ItemCountChangeListener,
                deleteAllAction: View.OnClickListener
    ) {
        mContext = context
        mRetryListener = retryClickListener
        mItemCountChangeListener = itemCountChangeListener
        mDeleteAllAction = deleteAllAction
    }

    fun setData(newData: ArrayList<ShopCartItemModel>) {
        mData = newData


    }

    override fun getItemViewType(position: Int): Int {

        if (mData.size == 0) {
            if (mIsLoading || mErrorCode != NetworkUtil.SUCCESS) {
                return VIEW_TYPE_LOADING
            } else {
                return VIEW_TYPE_NO_DATA
            }
        } else {
            return if (position == 0) {
                VIEW_TYPE_TOP_LIST
            } else {
                VIEW_TYPE_ITEM
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            VIEW_TYPE_LOADING -> {
                if (mRetryViewHolder == null) {

                    mRetryViewHolder = RetryViewHolder(LayoutInflater.from(mContext)
                            .inflate(R.layout.item_retry, parent, false))
                }
                return mRetryViewHolder as RecyclerView.ViewHolder
            }
            VIEW_TYPE_NO_DATA -> {
                return NoDataViewHolder(EmptyShopCartView(mContext))
            }
            VIEW_TYPE_TOP_LIST -> {
                return TopItemViewHolder(ShopCartTopItemView(mContext))
            }
            else -> {
                return ProductViewHolder(ShopCartItemView(mContext))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_LOADING -> {
                mRetryViewHolder?.retry?.setRetryListener(mRetryListener)
                if (mIsLoading) {

                    setRetryState(LoadingStateView.STATE_LOADING)
                } else {

                    setRetryState(LoadingStateView.STATE_ERROR)
                }
            }
            VIEW_TYPE_NO_DATA -> {

            }
            VIEW_TYPE_TOP_LIST -> {
                (holder as TopItemViewHolder).setDeleteAction(mDeleteAllAction)
                holder.setProductCount(mData.size)
            }
            VIEW_TYPE_ITEM -> {
                val productItem = mData[position - 1]

                if (holder is ProductViewHolder && productItem.productInfo != null) {
                    holder.bindData(productItem)
                    holder.setAddRemoveAction(object : ShopCartItemView.OnAddRemoveListener {

                        override fun countChanged(newCount: Int) {
                            if (newCount > Math.min(productItem.productInfo!!.maxOrder, productItem.productInfo!!.stock)) {
                                Toast.makeText(mContext, DI.getABResources().getString(R.string.shop_cart_item_max_order_message), Toast.LENGTH_SHORT).show()
                            } else if (newCount < productItem.productInfo!!.minOrder) {
                                Toast.makeText(mContext, DI.getABResources().getString(R.string.shop_cart_item_minOrder_message), Toast.LENGTH_SHORT).show()
                            }
                            else{
                                if(productItem.count < newCount){
                                    mItemCountChangeListener.itemCountChanged(productItem.productInfo!!, newCount, position, true)
                                }
                                else if(productItem.count > newCount){
                                    mItemCountChangeListener.itemCountChanged(productItem.productInfo!!, newCount, position, false)

                                }
                            }
                        }

                    })
                    holder.setRemoveAction(View.OnClickListener {
                        mItemCountChangeListener.itemCountChanged(productItem.productInfo!!, 0, position, false)
                    })
                    holder.setOpenProduct(View.OnClickListener {
                        mItemCountChangeListener.openProductDetail(productItem.productInfo!!)
                    })
                }
            }
        }

    }

    private fun setRetryState(stateLoading: Int) {
        if (mRetryViewHolder != null) {

            val AB = DI.getABResources()

            if (stateLoading == LoadingStateView.STATE_ERROR) {

                (mRetryViewHolder as RetryViewHolder).retry.setStateAndDescription(stateLoading, NetworkUtil.getErrorText(mErrorCode), mErrorCode != NetworkResponseCodes.ERROR_NO_INTERNET)
            } else if (stateLoading == LoadingStateView.STATE_LOADING) {

                (mRetryViewHolder as RetryViewHolder).retry.setStateAndDescription(stateLoading, AB.getString(R.string.loading_message), true)
            }
        }
    }

    override fun getItemCount(): Int {
        return mData.size + 1
    }

    fun setErrorCode(errorCode: Int) {
        mErrorCode = errorCode

        notifyDataSetChanged()
    }

    fun setIsLoading(isLoading: Boolean) {
        mIsLoading = isLoading

        notifyDataSetChanged()
    }


    class ProductViewHolder : RecyclerView.ViewHolder {
        private val item: ShopCartItemView
        val resources = DI.getABResources()

        constructor(view: View) : super(view) {
            item = view as ShopCartItemView
        }

        fun bindData(dataModel: ShopCartItemModel) {

            setAddRemoveAction(null)
            item.bindData(dataModel)
        }

        fun setRemoveAction(action: View.OnClickListener) {
            item.setRemoveAction(action)
        }

        fun setOpenProduct(action: View.OnClickListener) {
            item.setOpenProduct(action)
        }

        fun setAddRemoveAction(listener: ShopCartItemView.OnAddRemoveListener?) {
            item.setAddRemoveListener(listener)
        }

        fun getCount(): Int {
            return item.getCount()
        }
    }

    class TopItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val view = view as ShopCartTopItemView

        init {
            val resources = DI.getABResources()

            view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val layoutParams = view.layoutParams as RecyclerView.LayoutParams

                    layoutParams.setMargins(
                            resources.getDimenPixelSize(R.dimen.shop_cart_fragment_product_item_margin_side),
                            resources.getDimenPixelSize(R.dimen.shop_cart_fragment_product_delete_all_item_margin_top),
                            resources.getDimenPixelSize(R.dimen.shop_cart_fragment_product_item_margin_side),
                            resources.getDimenPixelSize(R.dimen.shop_cart_fragment_product_delete_all_item_margin_bottom))
                    view.layoutParams = layoutParams

                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }

            })
        }

        fun setDeleteAction(onClickListener: View.OnClickListener) {
            view.setDeleteAction(onClickListener)
        }

        fun setProductCount(count: Int) {
            view.setProductCount(count)
        }

        //TODO customize loading view
    }

    class RetryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var retry = view.findViewById<LoadingStateView>(R.id.retry_view)

        init {

            val AB = DI.getABResources()
            retry.setStateAndDescription(LoadingStateView.STATE_LOADING, AB.getString(R.string.loading_message), true)
        }

        fun setListener(listener: LoadingStateView.RetryListener) {
            retry.setRetryListener(listener)
        }
    }

    class NoDataViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}