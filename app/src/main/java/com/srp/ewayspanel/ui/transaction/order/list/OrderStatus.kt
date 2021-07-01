package com.srp.ewayspanel.ui.transaction.order.list

import com.srp.ewayspanel.R

/**
 * Created by Eskafi on 2/8/2020.
 */
class OrderStatus {

    companion object {

        const val SUCCESS = 0
        const val FAILD = 1
        const val UNKNOWN = 2

        private var OrderStatus_New = 0 //unknown
        private var OrderStatus_Canceled = 1 //failed
        private var OrderStatus_WaitingForPayment = 2  //unknown
        private var OrderStatus_Paid = 3//success
        private var OrderStatus_Done = 4//success
        private var OrderStatus_ReturnFromSale = 5 //failed
        private var OrderStatus_Unsuccessful_ExceedBuyAmount = 20  //unknown
        private var OrderStatus_Unsuccessful_LoyaltyScore = 21  //unknown
        private var OrderStatus_Unsuccessful_Deposit = 22  //unknown
        private var OrderStatus_Unsuccessful_UnAvailable = 23  //unknown
        private var OrderStatus_Unsuccessful_SystemError_1 = 24  //unknown
        private var OrderStatus_Unsuccessful_SystemError_2 = 25  //unknown
        private var OrderStatus_Unsuccessful_SystemError_3 = 26  //unknown


        fun getResultColor(orderStatus: Int): Int {

            when (orderStatus) {
                OrderStatus_New,
                OrderStatus_WaitingForPayment-> {
                    return R.color.order_transaction_type_unknown_color
                }

                OrderStatus_Canceled-> {
                    return R.color.order_transaction_type_unknown_color
                }

                OrderStatus_Paid,
                OrderStatus_Done -> {
                    return R.color.order_transaction_type_success_color
                }

                else -> {
                    return R.color.order_transaction_type_failed_color
                }
            }
        }

        fun getResultDrawable(orderStatus: Int): Int {

            when (orderStatus) {
                OrderStatus_New,
                OrderStatus_WaitingForPayment-> {
                    return R.drawable.transaction_item_result_unknown
                }

                OrderStatus_Canceled -> {
                    return R.drawable.transaction_item_result_unknown
                }

                OrderStatus_Paid,
                OrderStatus_Done -> {
                    return R.drawable.transaction_item_result_ok
                }

                else -> {
                    return R.drawable.transaction_item_result_failed
                }
            }
        }

        fun getReceiptResultDrawable(orderStatus: Int): Int {

            var orderStatus = getOrderStatus(orderStatus)

            when (orderStatus) {
                UNKNOWN -> {
                    return R.drawable.ic_receipt_unknown
                }

                FAILD -> {
                    return R.drawable.ic_receipt_failed
                }

               SUCCESS -> {
                    return R.drawable.ic_receipt_success
                }

                else -> {
                    return R.drawable.ic_receipt_unknown
                }
            }
        }

        fun getReceiptResultDescription(orderStatus: Int): Int {

            var orderStatus = getOrderStatus(orderStatus)

            when (orderStatus) {
                UNKNOWN -> {
                    return R.string.shop_receipt_result_description_unknown_state
                }

                FAILD -> {
                    return R.string.shop_receipt_result_description_failed_state
                }

                SUCCESS -> {
                    return R.string.shop_receipt_result_description_success_state
                }

                else -> {
                    return R.string.shop_receipt_result_description_unknown_state
                }
            }
        }

        fun getOrderStatus(orderStatus: Int): Int {

            when (orderStatus) {
                OrderStatus_New,
                OrderStatus_WaitingForPayment-> {
                    return UNKNOWN
                }

                OrderStatus_Canceled -> {
                    return FAILD
                }

                OrderStatus_Paid,
                OrderStatus_Done -> {
                    return SUCCESS
                }

                else -> {
                    return FAILD
                }
            }
        }
    }

}
