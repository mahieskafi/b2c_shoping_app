package com.srp.eways.ui.view.button

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain


/**
 * Created by ErfanG on 19/08/2019.
 */
open class ButtonElement : ViewGroup {

    private var mIcon: ImageView = ImageView(context)
    private var mLoading: ProgressBar = ProgressBar(context)

    private var mBackground: Drawable? = null
    private var mDisableBackground: Drawable? = null
    private var mHasIcon = false

    private var mTextPaint: Paint? = null
    private val mBounds = Rect()
    private var mText: String = ""

    private var mRightMargin: Int = 0

    private var mIsCenterIcon: Boolean = false

    constructor(context: Context) : super(context) {
        initialize(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAtr: Int) : super(context, attrs, defStyleAtr) {
        initialize(context, attrs, defStyleAtr)
    }

    private fun initialize(context: Context, attrs: AttributeSet?, defStyleAtr: Int) {

        mTextPaint = Paint()

        addView(mIcon)
        addView(mLoading)

        var textTypeface: Typeface? = null

        if (attrs != null) {
            val attrArray = context.obtainStyledAttributes(attrs, R.styleable.ButtonElement, 0, 0)

            if (attrArray.hasValue(R.styleable.ButtonElement_button_font)) {
                val fontResId = attrArray.getResourceId(R.styleable.ButtonElement_button_font, 0)

                textTypeface = ResourcesCompat.getFont(context, fontResId)
            }

            mLoading.visibility = View.GONE

            attrArray.recycle()
        }

        if (textTypeface == null) {
            textTypeface = ResourcesCompat.getFont(context, R.font.iran_yekan_light)
        }


        mTextPaint?.typeface = textTypeface
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        if (heightMode == MeasureSpec.UNSPECIFIED) {
            val heightSize = DIMain.getABResources().getDimenPixelSize(R.dimen.buttonelement_default_height)

            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY))
        }

        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.EXACTLY))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        val density = context.resources.displayMetrics.density

        mTextPaint?.getTextBounds(mText, 0, mText?.length, mBounds)
        var textRight = width.div(2).plus(mBounds?.width().div(2))

        mIcon.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
        var width: Float = r - l.toFloat()
        if (mIsCenterIcon) {
            width = mBounds.width() + (density * 16) + mIcon.measuredWidth
            textRight = width.div(2).plus(width.div(2)).toInt()
        }
        mIcon.layout(
                (textRight + (density * 16)).toInt(),
                (density * 8).toInt(),
                (textRight + (density * 16)).toInt() + mIcon.measuredWidth,
                height - (density * 8).toInt())

        mLoading.measure(MeasureSpec.makeMeasureSpec((density * 24).toInt(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
        if (mRightMargin != 0) textRight = width.minus(mRightMargin).minus(mBounds.width()).plus(density * 16).toInt()

        mLoading.layout(
                (textRight + (density * 16)).toInt(),
                (density * 8).toInt(),
                (textRight + (density * 16)).toInt() + mLoading.measuredWidth,
                height - (density * 8).toInt())


    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val density = context.resources.displayMetrics.density

        mTextPaint?.getTextBounds(mText, 0, mText.length, mBounds)

        mIcon.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
        val width: Int
        width = when (mIsCenterIcon) {
            true -> (mBounds.width() + density * 16 + mIcon.measuredWidth).toInt()
            false -> mBounds.width()
        }

        val xPos: Float
        if (mRightMargin == 0) {
            xPos = canvas.width.div(2).minus(width.div(2)).toFloat()
        } else {
            xPos = canvas.width.minus(mRightMargin).minus(mBounds.width()).toFloat()
        }

        val yPos = ((canvas.height.div(2)).minus((mTextPaint?.descent()?.plus(mTextPaint?.ascent()!!))?.div(2)!!))

        canvas.drawText(mText, xPos, yPos, mTextPaint!!)
    }

    fun setClickListener(listener: OnClickListener) {
        setOnClickListener(listener)
    }

    fun setEnable(isEnable: Boolean) {

        isEnabled = isEnable
        if (isEnable) {
            background = mBackground
        } else {
            background = mDisableBackground
        }
    }

    fun hasIcon(has: Boolean) {
        mHasIcon = has

        if (mHasIcon)
            mIcon.visibility = View.GONE
        else
            mIcon.visibility = View.GONE

    }

    fun setTextMarginRight(right: Int) {
        mRightMargin = right

        invalidate()
    }

    fun setText(text: String) {
        this.mText = text
        invalidate()
    }

    fun getText() = mText

    fun setTextColor(color: Int) {
        mTextPaint?.color = color
    }

    fun getTextColor() = mTextPaint?.color

    fun setTextSize(size: Float) {
        mTextPaint?.textSize = size
    }

    fun getTextSize() = mTextPaint?.textSize

    fun setTextTypeFace(typeface: Typeface) {
        mTextPaint?.typeface = typeface
    }

    fun getTextTypeFace() = mTextPaint?.typeface

    fun setIconVisibility(visibility: Int) {
        mIcon.visibility = visibility
    }

    fun setIcon(icon: Drawable) {
        mIcon.setImageDrawable(icon)
    }

    fun getIconVisibility() = mIcon.visibility

    fun setLoadingVisibility(visibility: Int) {
        mLoading.visibility = visibility
    }

    fun getLoadingVisibility() = mIcon.visibility

    fun setLoadingColorFilter(color: Int) {
        mLoading.indeterminateDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    fun setEnabledBackground(background: Drawable) {
        mBackground = background
    }

    fun getEnabledBackground() = mBackground

    fun setDisableBackground(disableBackground: Drawable) {
        mDisableBackground = disableBackground
    }

    fun getDisabledBackground() = mDisableBackground

    fun setBackgroundElevation(size: Float) {
        ViewCompat.setElevation(this, size)
    }

    fun setIconCenter(isCenter: Boolean) {
        mIsCenterIcon = isCenter
    }
}