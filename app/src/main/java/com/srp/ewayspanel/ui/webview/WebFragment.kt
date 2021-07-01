package com.srp.ewayspanel.ui.webview

import android.net.Uri
import com.srp.eways.ui.main.MainRootIds
import com.srp.eways.ui.navigation.NavigationViewType
import com.srp.eways.ui.webview.MainWebFragment
import com.srp.eways.util.analytic.AnalyticConstant
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.followorder.FollowOrderItem
import com.srp.ewayspanel.model.transaction.order.OrderItem
import com.srp.ewayspanel.ui.transaction.order.OrderTransactionViewModel
import com.srp.ewayspanel.ui.transaction.order.detail.OrderTransactionDetailFragment

/**
 * Created by ErfanG on 6/9/2020.
 */
class WebFragment(url: String) : MainWebFragment(url) {

    companion object {
        fun newInstance(url: String): WebFragment {
            return WebFragment(url)
        }
    }

    override fun handleStore(url: Uri) {
        super.handleStore(url)

        val pathSegments = url.pathSegments
        val orderId = pathSegments[1].toLong()

        onSwitchRoot(MainRootIds.ROOT_HOME)

        if (orderId != 0L) {
            val orderTransactionViewModel: OrderTransactionViewModel = DI.getViewModel(OrderTransactionViewModel::class.java)
            orderTransactionViewModel.setSelectedOrderTransaction(FollowOrderItem(orderId))
            orderTransactionViewModel.consumeOrderSummaryLiveData()
            orderTransactionViewModel.consumeOrderDetailLiveData()

            orderTransactionViewModel.reNewList()

            DI.getEventSender().sendAction(AnalyticConstant.BUY, AnalyticConstant.BUY_ACTION, AnalyticConstant.BUY_TYPE, "Dargah")

            openFragment(OrderTransactionDetailFragment.newInstance(true), NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR)
        }
    }
}