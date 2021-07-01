package com.srp.ewayspanel.ui.followorder.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.srp.eways.base.BaseRecyclerAdapter2
import com.srp.eways.base.BaseViewHolder
import com.srp.ewayspanel.R
import com.srp.ewayspanel.model.followorder.FollowOrderItem
import com.srp.ewayspanel.ui.view.followorder.FollowOrderItemView
import com.srp.ewayspanel.ui.followorder.ShowMoreDetailClickListener

class FollowOrderListAdapter(private val mContext: Context,
                             private val mOnMoreDetailClickListener: FollowOrderItemView.OnMoreDetailClickListener?,
                             private val mOnShowSummaryDetailClickListener: OnShowSummaryDetailClickListener?)
    : BaseRecyclerAdapter2<FollowOrderItem, BaseViewHolder<FollowOrderItem, View>>(mContext) {

    interface OnShowSummaryDetailClickListener {
        fun onShowMoreClickListener(isShowMore: Boolean, followOrderItem: FollowOrderItem)
    }

    companion object {
        const val VIEW_TYPE_HEADER = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_HEADER
        } else {
            super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder2(parent: ViewGroup, viewType: Int): BaseViewHolder<FollowOrderItem, View> {
        val view: View

        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.view_follow_order_list_header, parent, false)
                FollowOrderHeaderViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_follow_order, parent, false)
                FollowOrderItemViewHolder(view, mOnMoreDetailClickListener, mOnShowSummaryDetailClickListener)
            }
        }
    }

    class FollowOrderItemViewHolder(view: View,
                                    onMoreDetailClickListener: FollowOrderItemView.OnMoreDetailClickListener?,
                                    onShowSummaryDetailClickListener: OnShowSummaryDetailClickListener?
    ) : BaseViewHolder<FollowOrderItem, View>(view) {

        private val mOnMoreDetailClickListener = onMoreDetailClickListener
        private val mOnShowSummaryDetailClickListener = onShowSummaryDetailClickListener

        override fun onBind(item: FollowOrderItem) {
            with(itemView as FollowOrderItemView) {

                setOrderId(item.orderID.toString())
                setOrderPrice(item.totalPrice)
                setOrderStatus(item.orderStatus, item.orderStatusName)

                val dateTime = item.createDate.split(" ")

                val date = dateTime[0]
                val time = dateTime[1].split(":")[0] + ":" + dateTime[1].split(":")[1]


                setDetailValues(item.deliveryStatusName, item.gatewayTitle, item.postMethodName, date, time)
                if (mOnMoreDetailClickListener != null) {
                    setMoreDetailClickListener(item, mOnMoreDetailClickListener)
                }

                if (mOnShowSummaryDetailClickListener != null) {
                    setOnShowMoreClickListener(object : ShowMoreDetailClickListener {
                        override fun onShowMoreClicked(isShowMore: Boolean) {
                            mOnShowSummaryDetailClickListener.onShowMoreClickListener(isShowMore, item)
                        }
                    })
                }
            }

        }
    }

    class FollowOrderHeaderViewHolder(itemView: View) : BaseViewHolder<FollowOrderItem, View>(itemView) {
        override fun onBind(item: FollowOrderItem?) {
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) {
        if (position == itemCount) {
            holder.onBind(null)
        } else if (position < data.size) {
            if (position != 0) {
                super.onBindViewHolder(holder as FollowOrderItemViewHolder, position - 1)
            } else {
                (holder as FollowOrderHeaderViewHolder).onBind(null)
            }
        }
    }


}