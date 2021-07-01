package com.srp.eways.ui.survey

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.srp.eways.R
import com.srp.eways.di.DIMain
import ir.abmyapp.androidsdk.IABResources

/**
 * Created by ErfanG on 27/10/2019.
 */
class SurveyQuestionIndicator : ViewGroup {

    private val resources : IABResources = DIMain.getABResources()

    private val mUnselectedBackground : Drawable
    private val mSelectedBackground : Drawable

    private var mQuestionsIndicatorView = ArrayList<View>()

    private var mQuestionCount : Int = 0

    private var mIndicatorNumber : Int = 0

    constructor(context : Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defAtrStyle : Int) : super(context, attributeSet, defAtrStyle)

    init {
        //TODO set attrs
        mUnselectedBackground = resources.getDrawable(R.drawable.survey_question_indicator_unselected_background)
        mSelectedBackground = resources.getDrawable(R.drawable.survey_question_indicator_selected_background)
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {

        /*
        formula is : mUnitWidth is width between two item
        and each item width is 5 * mUnitWidth
        so => width = (5 * mQuestionCount * mUnitWidth) + ((mQuestionCount - 1) * mUnitWidth)
         */
        val unitWith : Float = width / ((6 * mQuestionCount) - 1).toFloat()
        val itemWidth = 5 * unitWith

        if(mQuestionCount > 0) {
            for (i in 0 until mQuestionCount) {
                mQuestionsIndicatorView[i].measure(MeasureSpec.makeMeasureSpec(itemWidth.toInt(), MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(resources.getDimenPixelSize(R.dimen.survey_question_indicator_item_height), MeasureSpec.EXACTLY))
                mQuestionsIndicatorView[i].layout(
                        (((mQuestionCount - i - 1) * unitWith) + ((mQuestionCount - i - 1) * itemWidth)).toInt(),
                        0,
                        (((mQuestionCount - i - 1) * unitWith) + ((mQuestionCount - i) * itemWidth)).toInt(),
                        mQuestionsIndicatorView[i].measuredHeight)
            }
        }


    }

    fun setQuestionCount(questionCount : Int){
        mQuestionCount = questionCount
        mIndicatorNumber = 0

        removeAllViews()
        mQuestionsIndicatorView.removeAll(mQuestionsIndicatorView)

        for(i in 0 until questionCount){
            val view = View(context)
            view.background = mUnselectedBackground
            mQuestionsIndicatorView.add(view)
            addView(view)
        }

        visibility = if(questionCount == 1){
            View.INVISIBLE
        }
        else{
            View.VISIBLE
        }

        invalidate()
    }

    fun indicateUp(){
        if(mIndicatorNumber < mQuestionCount){
            mQuestionsIndicatorView[mIndicatorNumber].background = mSelectedBackground
            mIndicatorNumber++
        }
    }
    fun indicateDown(){
        if(mIndicatorNumber > 0){
            mIndicatorNumber--
            mQuestionsIndicatorView[mIndicatorNumber].background = mUnselectedBackground
        }
    }

    fun indicateTo(indicatorNumber : Int){
        if(indicatorNumber <= mQuestionCount){
            mIndicatorNumber = indicatorNumber

            for(i in 0 until  mIndicatorNumber){
                mQuestionsIndicatorView[i].background = mSelectedBackground
            }
            for(i in mIndicatorNumber until mQuestionCount){
                mQuestionsIndicatorView[i].background = mUnselectedBackground
            }
        }
        else{
            throw IllegalArgumentException("Number of Question is lower than this indicate number. QuestionNumber: $mQuestionCount  IndicatorNumber: $indicatorNumber")
        }
    }

}