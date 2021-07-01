package com.srp.ewayspanel.ui.store.product.detail.imagefullscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.srp.eways.di.DIMain
import com.srp.ewayspanel.R

class ProductFullScreenSmallImageAdapter : RecyclerView.Adapter<ProductFullScreenSmallImageAdapter.ImageViewHolder> {

    data class ProductImageUrl(
            var mUrl: String,
            var mIsSelected: Boolean = false
    )

    interface ImageClickListener {
        fun onImageClickListener(position: Int)
    }

    private lateinit var mImageUrl: List<ProductImageUrl>
    private lateinit var mImageClickListener: ImageClickListener

    constructor(imageClickListener: ImageClickListener) : super() {
        mImageClickListener = imageClickListener
    }

    fun setUrls(urls: List<ProductImageUrl>) {
        mImageUrl = urls
        notifyDataSetChanged()
    }

    fun setSelectedItem(position: Int) {
        for (imageUrl in mImageUrl) {
            imageUrl.mIsSelected = false
        }

        mImageUrl[position].mIsSelected = true
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_detail_small_image_list, parent, false)
        return ImageViewHolder(view, mImageClickListener)
    }

    override fun getItemCount(): Int {
        return mImageUrl.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(mImageUrl[position])
    }

    class ImageViewHolder : RecyclerView.ViewHolder {
        private var mImageView: AppCompatImageView
        private var mImageBackground: LinearLayout
        private var mImageClickListener: ImageClickListener?

        constructor(itemView: View, imageClickListener: ImageClickListener) : super(itemView) {
            mImageView = itemView.findViewById(R.id.image)
            mImageBackground = itemView.findViewById(R.id.image_background)

            mImageClickListener = imageClickListener
        }

        fun bind(url: ProductImageUrl) {
            Glide.with(itemView.context)
                    .load(url.mUrl)
                    .into(mImageView)

            if (url.mIsSelected) {
                mImageBackground.background = DIMain.getABResources().getDrawable(R.drawable.product_detail_small_image_list_selected_background)
            } else {
                mImageBackground.background = DIMain.getABResources().getDrawable(R.drawable.product_detail_small_image_list_unselected_background)
            }

            itemView.setOnClickListener {
                if (mImageClickListener != null) {
                    mImageClickListener!!.onImageClickListener(position)
                }
            }
        }
    }
}