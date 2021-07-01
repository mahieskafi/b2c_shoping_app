package com.srp.eways.ui.survey

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.model.survey.Question
import com.srp.eways.model.survey.QuestionChoice
import com.srp.eways.util.Utils
import com.yashoid.sequencelayout.SequenceLayout
import com.yashoid.sequencelayout.Span
import ir.abmyapp.androidsdk.IABResources

/**
 * Created by ErfanG on 28/10/2019.
 */
class SurveyPointSelector : SequenceLayout {

    companion object{
        const val POINT_ONE = 0
        const val POINT_TWO = 1
        const val POINT_THREE = 2
        const val POINT_FOUR = 3
        const val POINT_FIVE = 4
    }

    private val mLine : View
    private var mProgressLine : View

    private val mPoints = ArrayList<Pair<TextView, TextView>>()

    private val resources : IABResources = DIMain.getABResources()

    private val mMainFont : Typeface
    private val mSecondaryFont : Typeface

    private val mUnSelectedPointNumberColor : Int
    private val mSelectedPointNumberColor : Int
    private val mUnSelectedPointTextColor : Int
    private val mSelectedPointTextColor : Int

    private val mSelectedCirclePaint : Paint
    private val mLinePaint : Paint
    private val mArcPaint : Paint
    private val mArcWidth : Float

    private var mCurrentSelectedItem = POINT_THREE
    private lateinit var mChoices : ArrayList<QuestionChoice>
    private var mCurrentSelected = MutableLiveData<QuestionChoice>()

    constructor(context : Context) : super(context)
    constructor(context : Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context : Context, attributeSet: AttributeSet, defAtrStyle : Int) : super(context, attributeSet, defAtrStyle)

    init {

        mCurrentSelected.value = null

        LayoutInflater.from(context).inflate(R.layout.item_point_selector, this, true)

        mLine = findViewById(R.id.line)
        mProgressLine = findViewById(R.id.progress_line)

        mPoints.add(Pair(findViewById(R.id.point_1_text), findViewById(R.id.point_1_number)))
        mPoints.add(Pair(findViewById(R.id.point_2_text), findViewById(R.id.point_2_number)))
        mPoints.add(Pair(findViewById(R.id.point_3_text), findViewById(R.id.point_3_number)))
        mPoints.add(Pair(findViewById(R.id.point_4_text), findViewById(R.id.point_4_number)))
        mPoints.add(Pair(findViewById(R.id.point_5_text), findViewById(R.id.point_5_number)))

        addSequences(R.xml.sequences_point_selector)

        mMainFont = ResourcesCompat.getFont(context, R.font.iran_yekan)!!
        mSecondaryFont = ResourcesCompat.getFont(context, R.font.iran_yekan_medium)!!

        mUnSelectedPointNumberColor = resources.getColor(R.color.item_point_selector_unselected_point_number)
        mSelectedPointNumberColor = resources.getColor(R.color.item_point_selector_selected_point_number)
        mUnSelectedPointTextColor = resources.getColor(R.color.item_point_selector_unselected_text_number)
        mSelectedPointTextColor = resources.getColor(R.color.item_point_selector_selected_text_number)

        mSelectedCirclePaint = Paint()
        mSelectedCirclePaint.isAntiAlias = true
        mSelectedCirclePaint.color = resources.getColor(R.color.item_point_selector_progress_line_background)
        mSelectedCirclePaint.style = Paint.Style.FILL


        mLinePaint = Paint()
        mLinePaint.isAntiAlias = true
        mLinePaint.color = resources.getColor(R.color.item_point_selector_progress_line_background)
        mLinePaint.style = Paint.Style.FILL
        mLinePaint.strokeWidth = resources.getDimenPixelSize(R.dimen.item_point_selector_line_width).toFloat()

        mArcWidth = resources.getDimenPixelSize(R.dimen.item_point_selector_line_width).toFloat()

        mArcPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mArcPaint.color = resources.getColor(R.color.item_point_selector_progress_line_background)
        mArcPaint.style = Paint.Style.STROKE
        mArcPaint.strokeWidth = mArcWidth

        for(i in 0 until mPoints.size){
            val text = mPoints[i].first
            val number = mPoints[i].second

            text.typeface = mMainFont
            text.setTextColor(mUnSelectedPointTextColor)
            text.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.item_point_selector_text_size).toFloat())

            number.typeface = mMainFont
            number.setTextColor(mUnSelectedPointNumberColor)
            number.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.item_point_selector_number_size).toFloat())
            number.text = Utils.toPersianNumber(number.text.toString())

            text.setOnClickListener {
                setPoint(i)
            }
            number.setOnClickListener {
                setPoint(i)
            }
        }

    }


    private fun setPoint(selectedPoint : Int){

        if(selectedPoint > POINT_FIVE)
            return

        val oldText = mPoints[mCurrentSelectedItem].first
        val oldNumber = mPoints[mCurrentSelectedItem].second
        oldText.typeface = mMainFont
        oldText.setTextColor(mUnSelectedPointTextColor)
        oldNumber.setTextColor(mUnSelectedPointNumberColor)

        mCurrentSelectedItem = selectedPoint
        mCurrentSelected.value = if(mChoices.size == 5) mChoices[mCurrentSelectedItem] else null

        val text = mPoints[mCurrentSelectedItem].first
        val number = mPoints[mCurrentSelectedItem].second
        text.typeface = mSecondaryFont
        text.setTextColor(mSelectedPointTextColor)
        number.setTextColor(mSelectedPointNumberColor)

        invalidate()

        val progressSpan: Span = findSequenceById("progress_line_sequence").spans[1]

        when (mCurrentSelectedItem) {
            POINT_FIVE -> {
                progressSpan.size = mLine.height.toFloat()
            }
            else -> {

                progressSpan.size = (mLine.height - ((mPoints[mCurrentSelectedItem].second.y) +
                        (mPoints[mCurrentSelectedItem].second.height / 2) -
                        resources.getDimenPixelSize(R.dimen.item_point_selector_progress_circle_radius)))

                invalidate()
            }
        }

        requestLayout()
    }

    fun setDefaultPoint(choices: ArrayList<QuestionChoice>){
        var listener : OnLayoutChangeListener = object : OnLayoutChangeListener{
            override fun onLayoutChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int, p7: Int, p8: Int) {
                if(p3 - p1 > 0 && p4 - p2 > 0){

                    var point = POINT_FOUR

                    if(choices != null && choices.size >= 1) {
                        point = choices[0].text.split(";")[0].toInt() - 1
                    }

                    setPoint(point)
                    mPoints[point].second.callOnClick()

                    removeOnLayoutChangeListener(this)
                }
            }
        }
        addOnLayoutChangeListener(listener)

    }

    fun getSelectedPoint() = mCurrentSelected

    override fun dispatchDraw(canvas: Canvas?) {

        canvas?.drawCircle(
                mPoints[mCurrentSelectedItem].second.x + (mPoints[mCurrentSelectedItem].second.width / 2),
                mPoints[mCurrentSelectedItem].second.y + (mPoints[mCurrentSelectedItem].second.height / 2),
                resources.getDimenPixelSize(R.dimen.item_point_selector_progress_circle_radius).toFloat(),
                mSelectedCirclePaint)

        canvas?.drawLine(
                mLine.x,
                mPoints[mCurrentSelectedItem].second.y + mPoints[mCurrentSelectedItem].second.height / 2,
                mPoints[mCurrentSelectedItem].second.x,
                mPoints[mCurrentSelectedItem].second.y + mPoints[mCurrentSelectedItem].second.height / 2,
                mLinePaint)


        val secondCircleRadius = resources.getDimenPixelSize(R.dimen.item_point_selector_second_circle_radius)

        var left: Float = mPoints[mCurrentSelectedItem].second.x + (mPoints[mCurrentSelectedItem].second.width / 2) - resources.getDimenPixelSize(R.dimen.item_point_selector_progress_circle_radius).toFloat() + (1 * mArcWidth) - (2 * secondCircleRadius)
        var top1 = mPoints[mCurrentSelectedItem].second.y + (mPoints[mCurrentSelectedItem].second.height / 2) - (2 * secondCircleRadius)
        var top2 = mPoints[mCurrentSelectedItem].second.y + (mPoints[mCurrentSelectedItem].second.height / 2)


        var rectTop = RectF(left, top1, left + 2.0f * secondCircleRadius, top1 + 2 * secondCircleRadius)
        var rectBottom = RectF(left, top2, left + 2.0f * secondCircleRadius, top2 + 2 * secondCircleRadius)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas?.drawArc(rectTop,0f, 90f, false, mArcPaint)
            canvas?.drawArc(rectBottom,270f, 90f, false, mArcPaint)
        }

        super.dispatchDraw(canvas)
    }

    fun setChoices(question: Question) {
        mChoices = question.choices
    }

}