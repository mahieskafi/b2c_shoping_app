package com.srp.eways.ui.view.navigationdraweritem

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.yashoid.sequencelayout.SizeInfo
import kotlinx.android.synthetic.main.dialog_confirmation_bill.view.*

/**
 * Created by ErfanG on 27/08/2019.
 */
class NavigationDrawerItem : ViewGroup {

    private val mIcon = ImageView(context)
    private val mTitle = TextView(context)
    private val mDescription = TextView(context)
    private val mArrow = ImageView(context)
    private val mSubItemContainer = LinearLayout(context)

    private var mCo: Double = 0.180555
    private var mOpen = false
    private val ANIM_DURATION = 200L
    private var mSubItemList = arrayListOf<NavigationDrawerSubItem>()

    constructor(context: Context) : super(context)
    constructor(context: Context, atributeSet: AttributeSet?) : super(context, atributeSet)

    init {
        addView(mIcon)
        addView(mTitle)
        addView(mDescription)
        addView(mArrow)
        addView(mSubItemContainer)

        setAttributes()
    }

    private fun setAttributes() {

        val attrArray = context.obtainStyledAttributes(null, R.styleable.NavigationDrawerItem, 0, 0)
//
//        val typefaceResourceId = attrArray.getResourceId(R.styleable.NavigationDrawerItem_android_fontFamily, 0)
        val typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)

        mTitle.setTypeface(typeface)
        mDescription.setTypeface(typeface)

        //title
        mTitle.gravity = Gravity.RIGHT
        mTitle.textAlignment = View.TEXT_ALIGNMENT_GRAVITY

        //description
        mDescription.textDirection = View.TEXT_DIRECTION_RTL

        mArrow.setImageResource(R.drawable.ic_navigation_drawer_item_arrow)
        mArrow.visibility = View.GONE

        mSubItemContainer.orientation = LinearLayout.VERTICAL
        mSubItemContainer.gravity = Gravity.RIGHT

        attrArray.recycle()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val density = context.resources.displayMetrics.density


        val abResources = DIMain.getABResources()
        val viewHeight = abResources.getDimenPixelSize(R.dimen.navigation_drawer_item_height)

        mIcon.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
        mIcon.layout(
                ((1 - mCo) * width).toInt() - mIcon.measuredWidth / 2,
                viewHeight / 2 - mIcon.measuredHeight / 2,
                ((1 - mCo) * width).toInt() + mIcon.measuredWidth / 2,
                viewHeight / 2 + mIcon.measuredHeight / 2)

        mTitle.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
        mTitle.layout(
                (((1 - mCo) * width) - (density * 35)).toInt() - mTitle.measuredWidth,
                viewHeight / 2 - mTitle.measuredHeight / 2,
                (((1 - mCo) * width) - (density * 35)).toInt(),
                viewHeight / 2 + mTitle.measuredHeight / 2)

        mDescription.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
        mDescription.layout(
                mTitle.left - (density * 24).toInt() - mDescription.measuredWidth,
                viewHeight / 2 - mDescription.measuredHeight / 2,
                mTitle.left - (density * 24).toInt(),
                viewHeight / 2 + mDescription.measuredHeight / 2)

        mArrow.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
        mArrow.layout(
                l + abResources.getDimenPixelSize(R.dimen.navigation_drawer_arrow_margin_left),
                viewHeight / 2 - mArrow.measuredHeight / 2,
                mArrow.left + mArrow.measuredWidth,
                viewHeight / 2 + mArrow.measuredHeight / 2)

        mSubItemContainer.measure(MeasureSpec.makeMeasureSpec(width
                , MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
        mSubItemContainer.layout(
                0,
                viewHeight + abResources.getDimenPixelSize(R.dimen.navigation_drawer_subItem_container_margin_top),
                width,
                mSubItemContainer.top + (mSubItemList.size * abResources.getDimenPixelSize(R.dimen.navigation_drawer_sub_item_height))
        )
    }

    fun setIcon(drawable: Drawable) {
        mIcon.setImageDrawable(drawable)
    }

    fun setIcon(drawable: Drawable, color: Int) {
        var icon = drawable

        if (icon != null) {
            icon = drawable.mutate()
            icon.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }

        mIcon.setImageDrawable(icon)
    }

    fun getIconDrawable() = mIcon.drawable

    fun setTitleTextColor(color: Int) {
        mTitle.setTextColor(color)
    }

    fun setTitleTextSize(size: Float) {
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    fun setTitleText(text: String) {
        mTitle.setText(text)
    }

    fun getTitleText(): String {
        return mTitle.text.toString()
    }

    fun setDescriptionTextColor(color: Int) {
        mDescription.setTextColor(color)
    }

    fun setDescriptionTextSize(size: Float) {
        mDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    fun setDescriptionText(text: CharSequence) {
        mDescription.setText(text)
    }

    fun setAction(action: OnClickListener) {
        this.setOnClickListener(action)
    }

    fun hasSubItems(hasSubItems: Boolean) {
        if (hasSubItems) {
            mArrow.visibility = View.VISIBLE
            setAction(OnClickListener {
                openCloseItem()
            })
        }
    }

    fun setSubItems(subItems: ArrayList<NavigationDrawerSubItem>) {
        mSubItemContainer.removeAllViews()

        for (item in subItems) {
            mSubItemContainer.addView(item)
            mSubItemList.add(item)
        }
    }

    private fun openCloseItem() {


        mOpen = !mOpen
        val abResources = DIMain.getABResources()

        var extraSize =  abResources.getDimenPixelSize(R.dimen.navigation_drawer_item_height) +
                abResources.getDimenPixelSize(R.dimen.navigation_drawer_sub_item_height) * (mSubItemList.size)

        val anim: ValueAnimator = if (mOpen) {
            ValueAnimator.ofInt(abResources.getDimenPixelSize(R.dimen.navigation_drawer_item_height), extraSize)
        } else {
            ValueAnimator.ofInt(extraSize, abResources.getDimenPixelSize(R.dimen.navigation_drawer_item_height))
        }

        anim.addUpdateListener {

            layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, it.animatedValue as Int)
            requestLayout()
        }
        anim.duration = ANIM_DURATION


        val rotationAnim: ValueAnimator = if (mOpen) {
            ValueAnimator.ofFloat(0f, 180f)
        } else {
            ValueAnimator.ofFloat(180f, 0f)
        }

        rotationAnim.addUpdateListener {
            mArrow.rotation = it.animatedValue as Float
        }
        rotationAnim.duration = ANIM_DURATION

        anim.start()
        rotationAnim.start()
    }

}