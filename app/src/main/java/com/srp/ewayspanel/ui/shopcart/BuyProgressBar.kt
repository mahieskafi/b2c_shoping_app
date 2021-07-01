package com.srp.ewayspanel.ui.shopcart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI

/**
 * Created by ErfanG on 11/27/2019.
 */
class BuyProgressBar : ViewGroup {

    interface OnStateClickListener {
        fun onStateChanged(state: Int)
    }

    enum class BuyProgressBarType {
        JUST_SHOW, PROGRESSIVE
    }

    private var mIconList: ArrayList<ImageView> = arrayListOf()
    private var mTextList: ArrayList<FrameLayout> = arrayListOf()

    private lateinit var mSelectedIconList: ArrayList<Drawable>
    private lateinit var mUnSelectedIconList: ArrayList<Drawable>

    private val mHeight: Int
    private val mMarginRight: Int
    private val mTextMarginTop: Int
    private val mCircleRadius: Int

    private val mIconWidth: Int

    private val mBackHeight: Int
    private val mTextSideMargin: Int
    private val mTextFont: Typeface

    private val mTopIconMargin:Int

    private var mwidth :Int = 0

    private var mWhiteBack: Paint

    private var BackCircle :ArrayList<Paint> = arrayListOf()
    private var BackCircleShape :Paint

    @ColorInt
    private val mSelectedTextColor: Int

    @ColorInt
    private val mUnSelectedTextColor: Int

    @ColorInt
    private val mUnSelectedLineColor: Int

    private lateinit var mViewType: BuyProgressBarType

    private var mStateClickListener: OnStateClickListener? = null

    private var mStateCount = 0

    private var mCurrentState = 1


    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defAttrSet: Int) : super(
        context,
        attributeSet,
        defAttrSet
    )


    init {

        val resources = DI.getABResources()

        setBackgroundColor(Color.TRANSPARENT)

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        mWhiteBack= Paint()
        BackCircleShape= Paint()

        mHeight = resources.getDimenPixelSize(R.dimen.buy_progress_bar_height)
        mIconWidth = resources.getDimenPixelSize(R.dimen.buy_progress_bar_icon_width)
        mBackHeight = resources.getDimenPixelSize(R.dimen.buy_progress_bar_line_height)
        mCircleRadius = resources.getDimenPixelSize(R.dimen.buy_progress_bar_circle_radius)
        mTextSideMargin = resources.getDimenPixelSize(R.dimen.buy_progress_bar_text_sides_margin)

        mMarginRight = resources.getDimenPixelSize(R.dimen.buy_progress_bar_margin_right)
        mTextMarginTop = resources.getDimenPixelSize(R.dimen.buy_progress_bar_icone_margin_left)

        mTopIconMargin=resources.getDimenPixelSize(R.dimen.buy_progress_bar_icon_margin_top)

        mTextFont = ResourcesCompat.getFont(context, R.font.iran_yekan)!!

        mSelectedTextColor = resources.getColor(R.color.buy_progress_bar_selected_text_color)
        mUnSelectedTextColor = resources.getColor(R.color.buy_progress_bar_unselected_text_color)
        mUnSelectedLineColor = resources.getColor(R.color.buy_progress_bar_unselected_line_color)


        BackCircleShape.setColor(Color.WHITE)
        BackCircleShape.setStyle(Paint.Style.FILL)
        BackCircleShape.setAntiAlias(true)
        BackCircleShape.setShadowLayer(1F,0F,1.5F,resources.getColor(R.color.shadow_border))

        mWhiteBack.setColor(Color.WHITE)
        mWhiteBack.setStyle(Paint.Style.FILL)
        mWhiteBack.setAntiAlias(true)
        mWhiteBack.setShadowLayer(1F,0F,0.5F,resources.getColor(R.color.shadow_border))

    }

    fun setAttributes(
        viewType: BuyProgressBarType,
        itemCount: Int,
        textArray: ArrayList<String>,
        selectedIconArray: ArrayList<Drawable>,
        unselectedIconArray: ArrayList<Drawable>,
        stateClickListener: OnStateClickListener?
    ) {

        if (itemCount < 1 ||
                itemCount != textArray.size ||
                itemCount != selectedIconArray.size ||
                itemCount != unselectedIconArray.size) {
            throw IllegalArgumentException("Drawable arrays and text array should be the same size as itemCount")
        }

        val resources = DI.getABResources()

        mStateClickListener = stateClickListener

        mViewType = viewType

        mStateCount = itemCount

        mSelectedIconList = selectedIconArray
        mUnSelectedIconList = unselectedIconArray

        val textSize = resources.getDimenPixelSize(R.dimen.buy_progress_bar_text_size).toFloat()

        for (i in 0 until itemCount) {
            val textView = TextView(context)
            textView.layoutParams = FrameLayout.LayoutParams(
                WRAP_CONTENT,
                WRAP_CONTENT,
                Gravity.CENTER
            )
            textView.setPadding(mTextSideMargin, 0, mTextSideMargin, 0)
            textView.gravity = Gravity.CENTER
            textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            textView.setTextColor(mUnSelectedTextColor)
            textView.typeface = mTextFont
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            textView.text = textArray[i]
            textView.setBackgroundColor(Color.TRANSPARENT)

            var mTextContainer = FrameLayout(context)
            mTextContainer.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

            mTextContainer.addView(textView)

            addView(mTextContainer)
            mTextList.add(mTextContainer)

            val iconView = ImageView(context)

            iconView.layoutParams = LayoutParams(
                resources.getDimenPixelSize(R.dimen.buy_progress_bar_icon_width),
                resources.getDimenPixelSize(R.dimen.buy_progress_bar_icon_height)
            )

            iconView.setImageDrawable(mUnSelectedIconList[i])

            addView(iconView)
            mIconList.add(iconView)

            BackCircle.add(BackCircleShape)
        }

        invalidate()
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            widthMeasureSpec, MeasureSpec.makeMeasureSpec(
                mHeight,
                MeasureSpec.EXACTLY
            )
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
         mwidth = width
        val height = height

        for (i in 0 until mStateCount) {
            mTextList[i].measure(
                MeasureSpec.makeMeasureSpec(
                    (mwidth / mStateCount),
                    MeasureSpec.EXACTLY
                ),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            )

            mIconList[i].measure(
                MeasureSpec.makeMeasureSpec((mwidth / mStateCount), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mIconWidth, MeasureSpec.EXACTLY)
            )

            mTextList[i].layout(
                i * (mwidth / mStateCount),
                mTextMarginTop + (height / 2) - mTextList[i].measuredHeight / 2,
                i * (mwidth / mStateCount) + mTextList[i].measuredWidth,
                (height / 2) + mTextList[i].measuredHeight / 2
            )

            mIconList[i].layout(
                i * (mwidth / mStateCount),
                mTopIconMargin,
                i * (mwidth / mStateCount) + mIconList[i].measuredWidth,
                mTopIconMargin + mIconWidth
            )

        }

    }

    fun goNext() {

        if (mCurrentState + 1 <= mStateCount) {
            goState(++mCurrentState)
        }
    }

    fun goStateAt(position: Int) {

        if (position in 0..mStateCount) {
            goState(position)
            requestLayout()
        }
    }

    fun goPrevious() {
        if (mCurrentState - 1 >= 0) {
            goState(--mCurrentState)
        }

    }

    fun setTextVisibility(visibility: Int) {
//        mTextContainer.visibility = visibility
//        requestLayout()
    }

    fun getTextVisibility() = View.VISIBLE

    private fun goState(state: Int) {

        var selectedItemsCount = state - 1

        for (i in mIconList.size - 1 downTo 0) {
            mIconList[i].setImageDrawable(mUnSelectedIconList[i])
//            mTextList[i].getChildAt(0).setBackgroundColor(mUnSelectedLineColor)
            (mTextList[i].getChildAt(0) as TextView).setTextColor(mUnSelectedTextColor)
        }
        for (i in selectedItemsCount downTo 0) {

            val currentPos = mIconList.size - 1 - i

            mIconList[currentPos].setImageDrawable(mSelectedIconList[currentPos])
//            mTextList[currentPos].getChildAt(0).setBackgroundColor(mSelectedTextColor)
            (mTextList[currentPos].getChildAt(0) as TextView).setTextColor(mSelectedTextColor)

        }

    }

    override fun onDraw(canvas: Canvas) {

        canvas.drawRect(0F,0F, mwidth.toFloat(), mBackHeight.toFloat(),mWhiteBack)

        for (i in 0 until mStateCount) {
            canvas.drawCircle((((i+1) * (mwidth / mStateCount)) - mIconList[i].width / 2 ).toFloat(),
                (mBackHeight-mMarginRight).toFloat(), mCircleRadius.toFloat(), BackCircle[i]);
        }

    }
}