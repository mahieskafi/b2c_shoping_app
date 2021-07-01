package com.srp.eways.ui.survey

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain


/**
 * Created by ErfanG on 23/10/2019.
 */
class SurveyIconView : ViewGroup {

    private val mIcon : ImageView
    private val mText : TextView

    private var mOuterPaint : Paint

    val resources = DIMain.getABResources()

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle)

    init {
//        LayoutInflater.from(context).inflate(R.layout.survey_icon_view, this)

        mIcon = ImageView(context)
        mText = TextView(context)


        mOuterPaint = Paint()
        mOuterPaint.isAntiAlias = true
        mOuterPaint.isDither = true
        mOuterPaint.color = resources.getColor(R.color.survey_icon_around_start_color)
        mOuterPaint.strokeWidth = resources.getDimenPixelSize(R.dimen.survey_icon_view_outer_circle_width).toFloat()
        mOuterPaint.style = Paint.Style.STROKE
        mOuterPaint.strokeJoin = Paint.Join.MITER
        mOuterPaint.strokeCap = Paint.Cap.SQUARE

        addView(mIcon)
        addView(mText)

        mIcon.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        mText.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        mText.text = "نظرسنجی"
        mText.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_medium)
        mText.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.survey_icon_view_text_size).toFloat())
        mText.setTextColor(resources.getColor(R.color.survey_icon_view_text_color))
        mText.gravity = Gravity.CENTER
        mText.textAlignment = View.TEXT_ALIGNMENT_GRAVITY


        mIcon.setImageDrawable(resources.getDrawable(R.drawable.survey_icon))


        val startColor = resources.getColor(R.color.survey_icon_around_start_color)
        val endColor = resources.getColor(R.color.survey_icon_around_end_color)

        val animationFadeIn  = ValueAnimator.ofObject(ArgbEvaluator(), startColor, endColor)
        val animationFadeOut = ValueAnimator.ofObject(ArgbEvaluator(), endColor, startColor)

        animationFadeIn.duration = 1000
        animationFadeOut.duration = 1000

        val animListener = ValueAnimator.AnimatorUpdateListener { valueAnimator ->
            var value : Int = valueAnimator?.animatedValue as? Int ?: 0

            if(valueAnimator == animationFadeIn){
                if(value == endColor){
                    animationFadeOut.start()
                } else{
                    mOuterPaint.color = value
                    invalidate()
                }
            } else if(valueAnimator == animationFadeOut){
                if(value == startColor){
                    animationFadeIn.start()
                } else{
                    mOuterPaint.color = value
                    invalidate()
                }
            }
        }

        animationFadeIn.addUpdateListener(animListener)
        animationFadeOut.addUpdateListener(animListener)

        animationFadeIn.start()
    }


    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {

        val marginTopIcon = resources.getDimenPixelSize(R.dimen.survey_icon_view_margin_top)

        mIcon.measure(MeasureSpec.makeMeasureSpec(resources.getDimenPixelSize(R.dimen.survey_icon_view_width), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(resources.getDimenPixelSize(R.dimen.survey_icon_view_height), MeasureSpec.EXACTLY) )
        mIcon.layout((width / 2) - (mIcon.measuredWidth / 2),
                marginTopIcon,
                (width / 2) + (mIcon.measuredWidth / 2),
                marginTopIcon + mIcon.measuredHeight)

        mText.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
        mText.layout((width / 2) - (mText.measuredWidth / 2),
                mIcon.bottom,
                (width / 2) + (mText.measuredWidth / 2),
                mIcon.bottom + mText.measuredHeight)

    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)

        canvas?.drawCircle(
                mIcon.x + mIcon.width / 2,
                mIcon.y + mIcon.height / 2,
                resources.getDimenPixelSize(R.dimen.survey_icon_view_outer_circle_radius).toFloat(),
                mOuterPaint)
    }

    public fun setAction(action : OnClickListener){
        setOnClickListener(action)
    }

}