package com.srp.eways.ui.survey.singlechoicequestion

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.MutableLiveData
import com.srp.eways.model.survey.Question
import com.srp.eways.model.survey.QuestionChoice
import com.srp.eways.ui.survey.questionansweritem.BaseChooseQuestion

/**
 * Created by ErfanG on 03/11/2019.
 */
class SingleChoiceQuestion : BaseChooseQuestion {

    private var mSelectedChoice : MutableLiveData<QuestionChoice> = MutableLiveData()

    constructor(context : Context) : super(context)
    constructor(context : Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context : Context, attributeSet: AttributeSet, defAtrStyle: Int) : super(context, attributeSet, defAtrStyle)

    override fun setChoices(question : Question){
        super.setChoices(question)

        for(i in 0 until  mChoicesView.size){
            mChoicesView[i].setOnClickListener{
                setSelectedChoice(i)
            }
        }
    }

    private fun setSelectedChoice(position: Int) {
        for(view in mChoicesView){
            view.setIsSelected(false)
        }
        mChoicesView[position].setIsSelected(true)
        mSelectedChoice.value = mChoices[position]
    }

    fun getSelectedChoice() = mSelectedChoice

    override fun bindSelected(data : ArrayList<QuestionChoice>){

        if(data != null && data.size > 0) {
            for (i in 0 until data.size) {
                if (mChoices.contains(data[i])) {
                    setSelectedChoice(mChoices.indexOf(data[i]))
                    return
                }
            }
        }
    }

}