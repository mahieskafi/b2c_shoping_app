package com.srp.ewayspanel.ui.store.mobilelist

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.srp.eways.base.BaseViewHolder
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.ui.view.store.StoreMobileBrandItemView

class MobileBrandAdapter constructor(mContext: Context, itemClickListener: BrandItemClickListener) :
        RecyclerView.Adapter<MobileBrandAdapter.BrandViewHolder>() {

    data class Brand(
            var brandId: Int,
            var brandName: String?,
            var isSelected: Boolean
    )

    private var mData = mutableListOf<Brand>()

    fun setData(data: List<Brand>) {
        mData.addAll(data)
        notifyDataSetChanged()
    }

    fun setData(data: List<Brand>, defaultSelectedPosition: Int) {
        mData.addAll(data)
        mData[defaultSelectedPosition].isSelected = true
        notifyDataSetChanged()
    }

    interface BrandItemClickListener {
        fun onBrandClicked(brandId: Brand, position: Int)
    }

    private var mItemClickListener = itemClickListener

    fun resetListSelection() {

        for (brand in mData) {
            brand.isSelected = false
        }
        notifyDataSetChanged()
    }

    fun getData(): List<Brand> {
        return mData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        return BrandViewHolder(StoreMobileBrandItemView(parent.context), mItemClickListener)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        holder.onBind(mData[position])
    }

    class BrandViewHolder constructor(mView: View, itemClickListener: BrandItemClickListener) : BaseViewHolder<Brand, View>(mView) {
        private var mItemClickListener = itemClickListener

        override fun onBind(item: Brand) {

            val itemView = view as StoreMobileBrandItemView

            if (item.brandId == 0) {
                item.brandName = DI.getABResources().getString(R.string.mobile_list_brand_item_special_title_text)
            }
            item.brandName?.let { itemView.setTitle(it) }

            itemView.setItemSelected(item.isSelected)

            itemView.setIcon(item.brandId)

            itemView.setListener(View.OnClickListener {
                itemView.setItemSelected(true)

                if (mItemClickListener != null) {
                    mItemClickListener.onBrandClicked(item, adapterPosition)
                }
            })

        }

    }


}