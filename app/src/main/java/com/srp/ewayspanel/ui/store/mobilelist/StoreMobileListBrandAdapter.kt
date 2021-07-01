package com.srp.ewayspanel.ui.store.mobilelist

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srp.eways.base.BaseRecyclerAdapter2
import com.srp.eways.base.BaseViewHolder
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.storepage.filter.ProductInfo
import com.srp.ewayspanel.ui.store.product.ProductAdapter
import com.srp.ewayspanel.ui.store.product.ProductItemClickListener

class StoreMobileListBrandAdapter constructor(context: Context, productItemClickListener: ProductItemClickListener)
    : RecyclerView.Adapter<BaseViewHolder<Any, View>>() {

    data class MobileListItem(
            var brandId: Int,
            var brandName: String?,
            var productList: List<ProductInfo>
    )

    private var mData = mutableListOf<MobileListItem>()
    private var mContext = context
    private var mProductItemClickListener = productItemClickListener
    private var mHeaderViewHeight = 0

    companion object {
        const val TYPE_HEADER = 2
    }

    fun setData(data: List<MobileListItem>) {
        mData.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Any, View> {
        if (viewType == TYPE_HEADER) {
            val view = LinearLayout(parent.context)

            val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.height = mHeaderViewHeight

            view.layoutParams = layoutParams

            return RecyclerHeaderViewHolder(view)
        }
        return BrandViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_mobile_list_product, parent, false), mProductItemClickListener)
    }

    override fun getItemCount(): Int {
        return mData.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            ProductAdapter.TYPE_HEADER
        } else super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Any, View>, position: Int) {
        if (position == 0) {
            val item = Any()
            holder.onBind(item)

        } else {
            holder.onBind(mData[position - 1])
        }
    }

    fun setHeaderViewHeight(headerViewHeight: Int) {
        mHeaderViewHeight = headerViewHeight
    }

    inner class BrandViewHolder constructor(itemView: View, productItemClickListener: ProductItemClickListener) : BaseViewHolder<Any, View>(itemView) {

        private val mRecyclerView: RecyclerView = itemView.findViewById(R.id.recycler)
        private val mTitleView: AppCompatTextView = itemView.findViewById(R.id.title)

        private lateinit var mProductAdapter: ProductAdapter
        private var mProductItemClickListener = productItemClickListener

        private val mDefaultSpecialName: String
        private val  mItemHeight: Int

        init {

            val abResources = DI.getABResources()

            mDefaultSpecialName = abResources.getString(R.string.mobile_list_brand_item_special_title_text)
            mItemHeight = abResources.getDimenPixelSize(R.dimen.mobile_list_product_item_height)

            mTitleView.typeface = ResourcesCompat.getFont(itemView.context, R.font.iran_yekan_medium)
            mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.mobile_list_product_item_title_text_size))
            mTitleView.setTextColor(abResources.getColor(R.color.white))
            mTitleView.setBackgroundColor(abResources.getColor(R.color.mobile_list_product_item_title_back_color))

            mRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
        }

        override fun onBind(mobileListItem: Any) {

            var mobileItem = mobileListItem as MobileListItem

            if (mobileItem.brandId == 0) {
                mobileItem.brandName = mDefaultSpecialName
            }
            mTitleView.text = mobileItem.brandName

            mProductAdapter = ProductAdapter(itemView.context, BaseRecyclerAdapter2.RetryClickListener {
                //TODO: retry
            }, mProductItemClickListener, true)
//            mProductAdapter.setItemViewHeight(mItemHeight)
            mProductAdapter.setParentPosition(adapterPosition)
            mProductAdapter.setNewData(mobileItem.productList)
            mRecyclerView.adapter = mProductAdapter

        }
    }

    class RecyclerHeaderViewHolder(itemView: View) : BaseViewHolder<Any, View>(itemView) {
        override fun onBind(item: Any) {}
    }

}