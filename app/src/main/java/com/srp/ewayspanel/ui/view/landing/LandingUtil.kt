package com.srp.ewayspanel.ui.view.landing

import com.srp.ewayspanel.R

class LandingUtil {
    companion object {
        const val SERVICE_CHARGE_CODE: Int = 1
        const val SERVICE_BILL_CODE: Int = 3
        const val SERVICE_STORE_CODE: Int = 2
        const val SERVICE_CLUB_CODE: Int = 4
        const val SERVICE_TICKET_CODE: Int = 6
        const val SERVICE_MOBILE_LIST_CODE: Int = 7
        const val SERVICE_BILL_INQUIRY_CODE: Int = 5
        const val SERVICE_INSURANCE_CODE: Int = 1000

        const val STORE_MOBILE_LIST_TAB_INDEX = 4285

        @JvmStatic
        fun getActiveIcon(serviceCode: Int): Int {
            return when (serviceCode) {
                SERVICE_CHARGE_CODE -> R.drawable.ic_service_charge_active
                SERVICE_BILL_CODE -> R.drawable.ic_service_bill_active
                SERVICE_STORE_CODE -> R.drawable.ic_service_store_active
                SERVICE_CLUB_CODE -> R.drawable.ic_service_club_active
                SERVICE_TICKET_CODE -> R.drawable.ic_service_ticket_active
                SERVICE_MOBILE_LIST_CODE -> R.drawable.ic_service_mobile_list_active
                SERVICE_INSURANCE_CODE -> R.drawable.ic_service_insurance_active
                SERVICE_BILL_INQUIRY_CODE -> R.drawable.ic_service_bill_inquiry_active
                else -> R.drawable.ic_service_charge_active
            }
        }

        @JvmStatic
        fun getInactiveIcon(serviceCode: Int): Int {
            return when (serviceCode) {
                SERVICE_CHARGE_CODE -> R.drawable.ic_service_charge_inactive
                SERVICE_BILL_CODE -> R.drawable.ic_service_bill_inactive
                SERVICE_STORE_CODE -> R.drawable.ic_service_store_inactive
                SERVICE_CLUB_CODE -> R.drawable.ic_service_club_inactive
                SERVICE_TICKET_CODE -> R.drawable.ic_service_ticket_inactive
                SERVICE_MOBILE_LIST_CODE -> R.drawable.ic_service_mobile_list_inactive
                SERVICE_INSURANCE_CODE -> R.drawable.ic_service_insurance_inactive
                else -> R.drawable.ic_service_charge_inactive
            }
        }

    }


}