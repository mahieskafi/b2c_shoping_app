package com.srp.eways.ui.survey.multichoicequestion

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import com.srp.eways.R
import com.srp.eways.ui.survey.questionansweritem.QuestionAnswerItemView

/**
 * Created by ErfanG on 03/11/2019.
 */
class QuestionAnswerMulti : QuestionAnswerItemView {

    constructor(context : Context) : super(context)
    constructor(context : Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context : Context, attributeSet: AttributeSet, defAtrStyle: Int) : super(context, attributeSet, defAtrStyle)

    init {
        mSelectorUnSelectedDrawable = resources.getDrawable(R.drawable.question_answer_multi_unselected_icon)
        mSelectorSelectedDrawable = resources.getDrawable(R.drawable.question_answer_multi_selected_icon)

        mTextUnSelectedColor = resources.getColor(R.color.survey_question_answer_multi_selected)
        mTextSelectedColor = resources.getColor(R.color.survey_question_answer_multi_unselected)

        mSelectorImage.setImageDrawable(mSelectorUnSelectedDrawable)

        mAnswerText.setTextColor(mTextUnSelectedColor)
        mAnswerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.survey_question_answer_multi_text_size).toFloat())


    }
}