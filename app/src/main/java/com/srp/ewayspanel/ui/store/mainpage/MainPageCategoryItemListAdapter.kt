package com.srp.ewayspanel.ui.store.mainpage

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.srp.eways.base.BaseRecyclerAdapter2
import com.srp.eways.base.BaseViewHolder
import com.srp.eways.ui.view.ViewUtils
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.storepage.filter.ProductInfo
import com.srp.ewayspanel.model.storepage.mainpage.Data
import com.srp.ewayspanel.model.storepage.mainpage.Item
import com.srp.ewayspanel.ui.club.ClubItem
import com.srp.ewayspanel.ui.shopcart.ShopCartViewModel
import com.srp.ewayspanel.ui.store.product.ProductAdapter
import com.srp.ewayspanel.ui.store.product.ProductItemClickListener
import com.srp.ewayspanel.ui.view.shopitem.ShopItemView

/**
 * Created by ErfanG on 2/21/2020.
 */
class MainPageCategoryItemListAdapter : RecyclerView.Adapter<BaseViewHolder<Any, View>> {

    companion object {
        const val TYPE_CLUB = 3
        const val VIEW_TYPE_ITEM = 0

    }

    interface StoreMainPageListener {
        fun onItemClicked(position: Int, item: Item)

        fun onLoadMoreClicked(data: Data)

        fun onAddClicked(productInfo: com.srp.ewayspanel.model.shopcart.ProductInfo, newCount: Int, isTotalCount: Boolean)

        fun onRemoveClicked(productId: Int, newCount: Int, isTotalCount: Boolean)

        fun onDeleteClicked(productId: Int)

        fun onSaveInstaceState(state: Parcelable?, itemPosition: Int)
    }

    private val mContext: Context
    private val mData: ArrayList<Item>
    private val mListener: StoreMainPageListener

    private var mLoyaltyScore: Long = 0

    constructor(context: Context, data: ArrayList<Item>, itemListener: StoreMainPageListener) {

        mContext = context
        mData = data

        mListener = itemListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Any, View> {
        return getViewHolderItem(parent, viewType, mListener, mLoyaltyScore)
    }

    override fun getItemViewType(position: Int): Int {
        if (mData[position].point > 0) {
            return TYPE_CLUB
        }
        return VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun setLoyaltyScore(loyaltyScore: Long) {
        mLoyaltyScore = loyaltyScore
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Any, View>, position: Int) {
        holder.onBind(mData[position])
    }


    class MainPageCategoryItemListViewHolder(view: View, listener: StoreMainPageListener) : BaseViewHolder<Any, View>(view) {

        private val mView = view as ShopItemView
        private val mListener: StoreMainPageListener = listener
        private val mShopCartViewModel = DI.getViewModel(ShopCartViewModel::class.java)

        override fun onBind(productInfo: Any) {

            val item = productInfo as Item

            with(mView) {

                setProductImage(item.imageUrl)
                setProductName(item.name)

                setPriceBeforeOff(item.oldPrice.toString())

                setPriceAfterOff(item.price.toString())

                setOffValue(item.discount)

                setClickAction(android.view.View.OnClickListener {
                    mListener.onItemClicked(position, item)
                })

                var productInfo = com.srp.ewayspanel.model.storepage.filter.ProductInfo(id = item.id, name = item.name, seoName = item.seoName, price = item.price, oldPrice = item.oldPrice, imageUrl = item.imageUrl, stock = item.stock, availability = item.availability,
                        minOrder = item.minOrder, maxOrder = item.maxOrder, overInventoryCount = item.overInventoryCount, point = item.point, discount = item.discount,
                        lawId = item.lawId, isSim = item.isSim)

                bind(productInfo)

                setCountListener(object : ProductItemClickListener {
                    override fun onDeleteClicked(productId: Int) {
                        mListener.onDeleteClicked(productId)
                    }

                    override fun onRemoveClicked(productId: Int, newCount: Int, isTotalCount: Boolean) {
                        mListener.onRemoveClicked(productId, newCount, isTotalCount)
                    }

                    override fun onAddClicked(productInfo: com.srp.ewayspanel.model.shopcart.ProductInfo, newCount: Int, isTotalCount: Boolean) {
                        mListener.onAddClicked(productInfo, newCount, isTotalCount)
                    }

                    override fun onItemClick(position: Int, item: ProductInfo) {
                        mListener.onItemClicked(position, com.srp.ewayspanel.model.storepage.mainpage.Item(id = item.id, name = item.name!!, seoName = item.seoName, price = item.price, oldPrice = item.oldPrice, imageUrl = item.imageUrl, stock = item.stock, availability = item.availability,
                                minOrder = item.minOrder, maxOrder = item.maxOrder, overInventoryCount = item.overInventoryCount, point = item.point, discount = item.discount,
                                lawId = item.lawId, isSim = item.isSim))
                    }

                }, productInfo)

                setCount(mShopCartViewModel.getProductCount(item.id), false)

            }

        }

    }

    class MainPageClubProductItemViewHolder(itemView: View, listener: StoreMainPageListener, mLoyaltyScore: Long) : BaseViewHolder<Any, View>(itemView), View.OnClickListener {
        private var mData: Item? = null
        private val mShopCartViewModel = DI.getViewModel(ShopCartViewModel::class.java)
        private val loyaltyScore = mLoyaltyScore
        private val mListener = listener

        override fun onBind(productInfo: Any) {

            val item = productInfo as Item
            val view = view as ClubItem?

            var productInfo = com.srp.ewayspanel.model.storepage.filter.ProductInfo(id = item.id, name = item.name, seoName = item.seoName, price = item.price, oldPrice = item.oldPrice, imageUrl = item.imageUrl, stock = item.stock, availability = item.availability,
                    minOrder = item.minOrder, maxOrder = item.maxOrder, overInventoryCount = item.overInventoryCount, point = item.point, discount = item.discount,
                    lawId = item.lawId, isSim = item.isSim)

            mData = item

            view!!.bind(productInfo)
            view.setUserPoint(loyaltyScore)
            view.setProductName(item.name)
            if (item.imageUrl != null) {
                view.setProductImage(item.imageUrl)
            }
            view.setPriceBefforOff(item.oldPrice)
            view.setPrice(item.price)
            view.setOffValue(item.discount)
            view.setClickAction(this)
            view.setCountListener(object : ProductItemClickListener {
                override fun onDeleteClicked(productId: Int) {
                    mListener.onDeleteClicked(productId)
                }

                override fun onRemoveClicked(productId: Int, newCount: Int, isTotalCount: Boolean) {
                    mListener.onRemoveClicked(productId, newCount, isTotalCount)
                }

                override fun onAddClicked(productInfo: com.srp.ewayspanel.model.shopcart.ProductInfo, newCount: Int, isTotalCount: Boolean) {
                    mListener.onAddClicked(productInfo, newCount, isTotalCount)
                }

                override fun onItemClick(position: Int, item: ProductInfo) {
                    mListener.onItemClicked(position, com.srp.ewayspanel.model.storepage.mainpage.Item(id = item.id, name = item.name!!, seoName = item.seoName, price = item.price, oldPrice = item.oldPrice, imageUrl = item.imageUrl, stock = item.stock, availability = item.availability,
                            minOrder = item.minOrder, maxOrder = item.maxOrder, overInventoryCount = item.overInventoryCount, point = item.point, discount = item.discount,
                            lawId = item.lawId, isSim = item.isSim))
                }

            }, productInfo)
            view.setUserPoint(item.point, loyaltyScore)
            view.setCount(mShopCartViewModel.getProductCount(item.id), false)
        }

        override fun onClick(v: View) {
            mListener.onItemClicked(adapterPosition, mData!!)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private fun getViewHolderItem(parent: ViewGroup, viewType: Int, listener: StoreMainPageListener, mLoyaltyScore: Long): BaseViewHolder<Any, View> {

        var view: View? = null
        val displayMetrics = ViewUtils.getDisplayMetrics(parent.context)
        var height = 0

        if (viewType == BaseRecyclerAdapter2.VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_product_store_main_page, parent, false)
        } else if (viewType == ProductAdapter.TYPE_CLUB) {
            view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_club_store_main_page, parent, false)
        }


        height = ((displayMetrics.density * 250).toInt())
        val margin = ((displayMetrics.density * 4).toInt())

        val marginLayoutParams = view!!.layoutParams
        marginLayoutParams.height = height
        (marginLayoutParams as RecyclerView.LayoutParams).setMargins(margin, 0, margin, 0)
        view.layoutParams = marginLayoutParams



        return if (viewType == ProductAdapter.TYPE_CLUB) {
            MainPageClubProductItemViewHolder(view, listener, mLoyaltyScore)
        } else {
            MainPageCategoryItemListViewHolder(view, listener)
        }
    }
}