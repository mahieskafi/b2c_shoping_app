package com.srp.ewayspanel.ui.store.mobilelist.util

import com.srp.ewayspanel.R

class MobileBrandUtil {

    companion object {
        private const val SPECIAL_BASKET: Int = 0
        private const val ALCATEL: Int = 10
        private const val APPLE: Int = 25
        private const val IPHONE: Int = 343
        private const val ENERGIZER: Int = 210
        private const val HONOR: Int = 704
        private const val HUAWEI: Int = 26
        private const val SAMSUNG: Int = 21
        private const val ASUS: Int = 36
        private const val LENOVO: Int = 37
        private const val XIAOMI: Int = 46
        private const val NOKIA: Int = 76
        private const val MOTOROLA: Int = 194
        private const val ALPHA: Int = 382
        private const val G_PLUS: Int = 466
        private const val SONY: Int = -1
        private const val OROD: Int = 581
        private const val RAK: Int = 403
        private const val MABE: Int = 585

        fun getBrandIcon(brandId: Int): Int {

            return when (brandId) {
                SPECIAL_BASKET -> R.drawable.ic_mobile_brand_special_basket
                ALCATEL -> R.drawable.ic_mobile_brand_alcatel
                APPLE -> R.drawable.ic_mobile_brand_apple
                IPHONE -> R.drawable.ic_mobile_brand_apple
                ENERGIZER -> R.drawable.ic_mobile_brand_energizer
                HONOR -> R.drawable.ic_mobile_brand_honor
                HUAWEI -> R.drawable.ic_mobile_brand_huawei
                SAMSUNG -> R.drawable.ic_mobile_brand_samsung
                ASUS -> R.drawable.ic_mobile_brand_asus
                LENOVO -> R.drawable.ic_mobile_brand_lenovo
                XIAOMI -> R.drawable.ic_mobile_brand_xiaomi
                NOKIA -> R.drawable.ic_mobile_brand_nokia
                MOTOROLA -> R.drawable.ic_mobile_brand_motorola
                ALPHA -> R.drawable.ic_mobile_brand_alpha
                G_PLUS -> R.drawable.ic_mobile_brand_g_plus
                SONY -> R.drawable.ic_mobile_brand_sony
                OROD -> R.drawable.ic_mobile_brand_unknown
                RAK -> R.drawable.ic_mobile_brand_unknown
                MABE -> R.drawable.ic_mobile_brand_unknown

                else -> R.drawable.ic_mobile_brand_unknown
            }
        }
    }



}