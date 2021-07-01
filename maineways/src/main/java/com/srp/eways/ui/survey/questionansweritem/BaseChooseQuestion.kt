package com.srp.eways.ui.survey.questionansweritem

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.model.survey.Question
import com.srp.eways.model.survey.QuestionChoice
import com.srp.eways.ui.survey.SurveyActivity
import com.srp.eways.ui.survey.multichoicequestion.QuestionAnswerMulti
import com.srp.eways.ui.survey.singlechoicequestion.QuestionAnswerSingle

/**
 * Created by ErfanG on 03/11/2019.
 */
open class BaseChooseQuestion : LinearLayout {

    protected val resources = DIMain.getABResources()
    protected var mChoices : ArrayList<QuestionChoice> = ArrayList()
    protected var mChoicesView : ArrayList<QuestionAnswerItemView> = ArrayList()

    constructor(context : Context) : super(context){
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
    constructor(context : Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context : Context, attributeSet: AttributeSet, defAtrStyle: Int) : super(context, attributeSet, defAtrStyle)

    init {
        orientation = VERTICAL
    }


    open fun setChoices(question : Question){
        mChoices = question.choices

        removeAllViews()

        for(item in mChoices){
            val itemView = if(question.type == SurveyActivity.QUESTION_TYPE_RADIO){
                QuestionAnswerSingle(context)
            }
            else{
                QuestionAnswerMulti(context)
            }

            itemView.setText(item.text)
            val layoutParam = LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParam.setMargins(
                    resources.getDimenPixelSize(R.dimen.survey_question_answer_margin_side),
                    resources.getDimenPixelSize(R.dimen.survey_question_answer_margin_vertical),
                    resources.getDimenPixelSize(R.dimen.survey_question_answer_margin_side),
                    resources.getDimenPixelSize(R.dimen.survey_question_answer_margin_vertical))

            itemView.layoutParams = layoutParam

            mChoicesView.add(itemView)
            addView(itemView)
        }
        requestLayout()
    }

    open fun bindSelected(data: ArrayList<QuestionChoice>) {

    }

}