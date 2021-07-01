package com.srp.eways.ui.survey.questionansweritem

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.yashoid.sequencelayout.SequenceLayout

/**
 * Created by ErfanG on 03/11/2019.
 */
open class QuestionAnswerItemView : SequenceLayout {


    protected val resources = DIMain.getABResources()
    protected val mSelectorImage : ImageView
    protected val mAnswerText : TextView

    protected lateinit var mSelectorUnSelectedDrawable : Drawable
    protected lateinit var mSelectorSelectedDrawable : Drawable

    protected var mTextSelectedColor : Int = 0
    protected var mTextUnSelectedColor : Int = 0

    private var mIsSelected = false

    constructor(context : Context) : super(context)
    constructor(context : Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context : Context, attributeSet: AttributeSet, defAtrStyle: Int) : super(context, attributeSet, defAtrStyle)

    init {

        LayoutInflater.from(context).inflate(R.layout.question_answer, this, true)

        mSelectorImage = findViewById(R.id.selector_image)
        mAnswerText = findViewById(R.id.answer_text)

        addSequences(R.xml.sequences_question_answer)

        mAnswerText.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)


        ViewCompat.setElevation(this, resources.getDimenPixelSize(R.dimen.survey_question_answer_elevation).toFloat())
        background = resources.getDrawable(R.drawable.survey_question_answer_background)
    }


    fun setIsSelected(isSelected : Boolean){

        mIsSelected = isSelected

        if(isSelected){
            mSelectorImage.setImageDrawable(mSelectorSelectedDrawable)
            mAnswerText.setTextColor(mTextSelectedColor)
        }
        else{
            mSelectorImage.setImageDrawable(mSelectorUnSelectedDrawable)
            mAnswerText.setTextColor(mTextUnSelectedColor)
        }
    }

    fun isItemSelected() = mIsSelected

    fun setText(text : String){
        mAnswerText.text = text
    }

}