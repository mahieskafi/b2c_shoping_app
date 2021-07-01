package com.srp.ewayspanel.ui.landing

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.srp.eways.base.BaseRecyclerAdapter2
import com.srp.eways.base.BaseViewHolder
import com.srp.ewayspanel.model.appservice.AppService
import com.srp.ewayspanel.ui.view.landing.AppServiceItemView
import com.srp.ewayspanel.ui.view.landing.LandingUtil

class LandingServiceAdapter constructor(mContext: Context, mData: List<AppService>, itemClickListener: AppServiceItemClickListener) :
        BaseRecyclerAdapter2<AppService, LandingServiceAdapter.AppServiceViewHolder>(mContext, mData) {


    interface AppServiceItemClickListener {
        fun onAppServiceClicked(appService: AppService)
    }

    private var mItemClickListener = itemClickListener

    override fun onCreateViewHolder2(parent: ViewGroup?, viewType: Int): AppServiceViewHolder {
        return AppServiceViewHolder(AppServiceItemView(parent!!.context), mItemClickListener)
    }

    fun resetListSelection() {

        for (appService in data) {
            appService.isSelected = false
        }
        notifyDataSetChanged()
    }

    class AppServiceViewHolder constructor(mView: View, itemClickListener: AppServiceItemClickListener) : BaseViewHolder<AppService, View>(mView) {
        private var mItemClickListener = itemClickListener

        override fun onBind(item: AppService) {

            val itemView = view as AppServiceItemView

            itemView.setTitle(item.title)

            itemView.setActive(item.isActive)

            if (item.isActive) {
                itemView.setIcon(LandingUtil.getActiveIcon(item.id))
            } else {
                itemView.setIcon(LandingUtil.getInactiveIcon(item.id))
            }

            itemView.setItemSelected(item.isSelected)

            itemView.setListener(View.OnClickListener {
                itemView.setItemSelected(true)

                if (mItemClickListener != null) {
                    mItemClickListener.onAppServiceClicked(item)
                }
            })

        }

    }


}