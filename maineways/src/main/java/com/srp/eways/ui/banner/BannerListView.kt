package com.srp.eways.ui.banner

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.Tricks.ViewPagerEx
import com.srp.eways.R


class BannerListView : SliderLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    init {

        setDuration(4000L)
        setPresetIndicator(PresetIndicators.Center_Bottom);
        addOnPageChangeListener(object : ViewPagerEx.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
            }

        })
    }

}