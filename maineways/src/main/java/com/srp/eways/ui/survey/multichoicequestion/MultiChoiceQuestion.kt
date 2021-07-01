package com.srp.eways.ui.survey.multichoicequestion

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.srp.eways.R
import com.srp.eways.model.survey.Question
import com.srp.eways.model.survey.QuestionChoice
import com.srp.eways.ui.survey.questionansweritem.BaseChooseQuestion

/**
 * Created by ErfanG on 11/11/2019.
 */
class MultiChoiceQuestion : BaseChooseQuestion {

    private var mHasMaxLimit = false
    private var mMaxChoiceLimit = 1

    private var mSelectedChoicePositions : ArrayList<Int> = arrayListOf()
    private var mSelectedChoices : MutableLiveData<ArrayList<QuestionChoice>> = MutableLiveData()

    constructor(context : Context) : super(context)
    constructor(context : Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context : Context, attributeSet: AttributeSet, defAtrStyle: Int) : super(context, attributeSet, defAtrStyle)

    override fun setChoices(question: Question) {
        setChoices(question, false, 1)
    }

    fun setChoices(question: Question, hasMax : Boolean = false, maxLimit : Int = 1) {
        super.setChoices(question)

        mSelectedChoices.value = arrayListOf()

        mHasMaxLimit = hasMax
        mMaxChoiceLimit = maxLimit

        for(i in 0 until mChoicesView.size){
            mChoicesView[i].setOnClickListener{
                setSelectedChoice(i)
            }
        }
    }

    private fun setSelectedChoice(position: Int) {

        //deselect
        if(mSelectedChoicePositions.contains(position)){
            mChoicesView[position].setIsSelected(false)
            mSelectedChoicePositions.remove(position)

            val newArray = arrayListOf<QuestionChoice>()
            newArray.addAll(mSelectedChoices.value!!)
            newArray.remove(mChoices[position])
            mSelectedChoices.value = newArray
        }
        //select
        else{
            if(mHasMaxLimit && mSelectedChoicePositions.size >= mMaxChoiceLimit){
                Toast.makeText(context, resources.getString(R.string.survey_multi_choice_max_limit_message), Toast.LENGTH_SHORT).show()
            }
            else{
                mChoicesView[position].setIsSelected(true)
                mSelectedChoicePositions.add(position)

                val newArray = arrayListOf<QuestionChoice>()
                newArray.addAll(mSelectedChoices.value!!)
                newArray.add(mChoices[position])
                mSelectedChoices.value = newArray
            }
        }
    }

    fun getSelectedChoice() = mSelectedChoices

    override fun bindSelected(data : ArrayList<QuestionChoice>){

        if(data != null && data.size > 0) {
            for (i in 0 until data.size) {
                if (mChoices.contains(data[i])) {
                    setSelectedChoice(mChoices.indexOf(data[i]))
                }
            }
        }
    }

}