package com.srp.ewayspanel.ui.store.mainpage

import android.content.Context
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.srp.eways.base.BaseRecyclerAdapter
import com.srp.ewayspanel.R
import com.srp.ewayspanel.model.storepage.mainpage.Data
import com.srp.ewayspanel.ui.view.slider.SliderView
import java.util.*


/**
 * Created by ErfanG on 2/21/2020.
 */
class CategoryListAdapter : BaseRecyclerAdapter<Data, RecyclerView.ViewHolder> {

    companion object {
        const val VIEW_TYPE_BANNER = 4
        const val BANNER_SLIDER_TIMER = 5000
    }

    private val mContext: Context

    private val mProductListener: MainPageCategoryItemListAdapter.StoreMainPageListener

    private lateinit var mViewPool: RecyclerView.RecycledViewPool
    private var mSliderList = ArrayList<com.srp.eways.model.banner.Data>()

    private val positionList = SparseIntArray()

    private var mLoyaltyScore: Long = 0

    constructor(context: Context, retryListener: RetryClickListener, productListener: MainPageCategoryItemListAdapter.StoreMainPageListener) : super(context, retryListener, false) {
        mContext = context

        mProductListener = productListener

        mViewPool = RecyclerView.RecycledViewPool()
    }

    override fun getItemViewType(position: Int): Int {

        if (getData().size > 0) {
            if (position == 0) {
                return VIEW_TYPE_BANNER
            }
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder2(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == VIEW_TYPE_BANNER) {
            return BannerViewHolder(SliderView(mContext))
        }

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_store_main_page_category, parent, false) as MainPageCategoryItemView
        view.setlistener(mProductListener)

        val itemHolder = CategoryItemViewHolder(view, mLoyaltyScore)
        itemHolder.setListViewPool(mViewPool)

        return itemHolder
    }

    override fun onBindViewHolder2(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CategoryItemViewHolder) {
            holder.bindData(getData()[position]!!)

            holder.setLoadMoreAction(View.OnClickListener {
                mProductListener.onLoadMoreClicked(getData()[position]!!)
            })

        } else if (holder is BannerViewHolder) {
            holder.setSliderList(mSliderList)
        }
    }

    override fun onViewRecycled(viewHolder: RecyclerView.ViewHolder) {
        if (viewHolder is CategoryItemViewHolder) {
            val position: Int = viewHolder.adapterPosition
            val firstVisiblePosition: Int = viewHolder.mLayoutManager.findFirstVisibleItemPosition()
            positionList.put(position, firstVisiblePosition)
        }
        super.onViewRecycled(viewHolder)
    }

    fun setSliderList(sliderList: ArrayList<com.srp.eways.model.banner.Data>) {
        mSliderList = sliderList

        notifyDataSetChanged()
    }

    fun setLoyaltyScore(loyaltyScore: Long) {
        mLoyaltyScore = loyaltyScore
    }

    class CategoryItemViewHolder(view: View, mLoyaltyScore: Long) : RecyclerView.ViewHolder(view) {

        val mView = view as MainPageCategoryItemView
        lateinit var mLayoutManager: LinearLayoutManager
        val loyaltyScore = mLoyaltyScore

        fun bindData(data: Data) {
            mView.setLoyaltyScore(loyaltyScore)
            mView.setModel(data)

            mLayoutManager = mView.getLayoutManager() as LinearLayoutManager
        }

        fun setLoadMoreAction(action: View.OnClickListener) {
            mView.setOnLoadMoreAction(action)
        }

        fun setListViewPool(viewPool: RecyclerView.RecycledViewPool) {
            mView.setListViewPool(viewPool)
        }
    }

    class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val mView = view as SliderView

        fun setSliderList(sliderList: List<com.srp.eways.model.banner.Data>) {
            mView.setSliderList(sliderList)
        }
    }
}