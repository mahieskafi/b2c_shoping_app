package com.srp.eways.ui.survey.imageselector

import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.srp.eways.di.DIMain
import com.srp.eways.model.survey.Question
import com.srp.eways.model.survey.QuestionChoice
import com.yashoid.sequencelayout.SequenceLayout
import java.net.URL
import com.srp.eways.R


/**
 * Created by ErfanG on 11/11/2019.
 */
class ImageSelectorQuestion : SequenceLayout {

    companion object{
        private const val FIRST = 0
        private const val SECOND = 1
    }

    private val mFirstImage : ImageView
    private val mFirstImageName : TextView
    private val mFirstIcon : ImageView
    private val mSecondImage : ImageView
    private val mSecondImageName : TextView
    private val mSecondIcon : ImageView


    private val mSelectedFont : Typeface?
    private val mUnSelectedFont : Typeface?

    @ColorInt private val mSelectedColor : Int
    @ColorInt private val mUnSelectedColor : Int

    private val mSelectedBackground : Drawable
    private val mUnSelectedBackground : Drawable

    private val mSelectedIcon : Drawable
    private val mUnSelectedIcon : Drawable


    protected var mChoices : ArrayList<QuestionChoice> = ArrayList()

    private var mSelectedChoice : MutableLiveData<QuestionChoice> = MutableLiveData()

    constructor(context : Context) : super(context){
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }
    constructor(context : Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context : Context, attributeSet: AttributeSet, defAtrStyle: Int) : super(context, attributeSet, defAtrStyle)

    init {

        val resources = DIMain.getABResources()

        LayoutInflater.from(context).inflate(R.layout.question_select_image, this, true)

        mFirstImage = findViewById(R.id.image_view1)
        mFirstImageName = findViewById(R.id.image_name1)
        mFirstIcon = findViewById(R.id.image_select_icon1)

        mSecondImage = findViewById(R.id.image_view2)
        mSecondImageName = findViewById(R.id.image_name2)
        mSecondIcon = findViewById(R.id.image_select_icon2)

        addSequences(R.xml.sequences_question_select_image)

        mSelectedFont = ResourcesCompat.getFont(context, R.font.iran_yekan_medium)
        mUnSelectedFont = ResourcesCompat.getFont(context, R.font.iran_yekan)

        mSelectedColor = resources.getColor(R.color.survey_image_selector_text_selected_color)
        mUnSelectedColor = resources.getColor(R.color.survey_image_selector_text_unselected_color)

        mSelectedBackground = resources.getDrawable(R.drawable.survey_image_selector_selected_background)
        mUnSelectedBackground = resources.getDrawable(R.drawable.survey_image_selector_unselected_background)

        mSelectedIcon = resources.getDrawable(R.drawable.survey_image_selector_selected_icon)
        mUnSelectedIcon = resources.getDrawable(R.drawable.survey_image_selector_unselected_icon)

        mFirstIcon.setImageDrawable(mUnSelectedIcon)
        mSecondIcon.setImageDrawable(mUnSelectedIcon)

        mFirstImageName.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.survey_image_selector_text_size).toFloat())
        mFirstImageName.typeface = mUnSelectedFont
        mFirstImageName.setTextColor(mUnSelectedColor)
        mFirstImage.background = mUnSelectedBackground

        mSecondImageName.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.survey_image_selector_text_size).toFloat())
        mSecondImageName.typeface = mUnSelectedFont
        mSecondImageName.setTextColor(mUnSelectedColor)
        mSecondImage.background = mUnSelectedBackground

    }

    fun setChoices(question : Question){
        mChoices = question.choices

        setData(Pair(mChoices[0], mChoices[1]))

        val firstAction = OnClickListener {
            setSelectedItem(FIRST)
        }
        mFirstIcon.setOnClickListener(firstAction)
        mFirstImage.setOnClickListener(firstAction)
        mFirstImageName.setOnClickListener(firstAction)

        val secondAction = OnClickListener {
            setSelectedItem(SECOND)
        }
        mSecondIcon.setOnClickListener(secondAction)
        mSecondImage.setOnClickListener(secondAction)
        mSecondImageName.setOnClickListener(secondAction)
    }

    private fun setSelectedItem(position : Int) {

        if(position == FIRST){

            mSelectedChoice.value = mChoices[0]

            mFirstImageName.setTextColor(mSelectedColor)
            mFirstImageName.typeface = mSelectedFont
            mFirstIcon.setImageDrawable(mSelectedIcon)
            mFirstImage.background = mSelectedBackground

            mSecondImageName.setTextColor(mUnSelectedColor)
            mSecondImageName.typeface = mUnSelectedFont
            mSecondIcon.setImageDrawable(mUnSelectedIcon)
            mSecondImage.background = mUnSelectedBackground
        }
        else if(position == SECOND){

            mSelectedChoice.value = mChoices[1]

            mSecondImageName.setTextColor(mSelectedColor)
            mSecondImageName.typeface = mSelectedFont
            mSecondIcon.setImageDrawable(mSelectedIcon)
            mSecondImage.background  = mSelectedBackground

            mFirstImageName.setTextColor(mUnSelectedColor)
            mFirstImageName.typeface = mUnSelectedFont
            mFirstIcon.setImageDrawable(mUnSelectedIcon)
            mFirstImage.background = mUnSelectedBackground
        }
    }

    private fun setSelectedItem(item : QuestionChoice){

        for(i in 0 until mChoices.size){
            if(mChoices[i] == item){

                setSelectedItem(i)
                return
            }
        }
    }

    fun setData(choices : Pair<QuestionChoice, QuestionChoice>){
        Glide.with(context)
                .load(URL(choices.first.imageUrl))
                .fitCenter().into(mFirstImage)
        mFirstImageName.text = choices.first.text

        Glide.with(context).load(URL(choices.second.imageUrl))
                .fitCenter().into(mSecondImage)
        mSecondImageName.text = choices.second.text


        val longAction1 = OnLongClickListener {
            val dialog = Dialog(context)
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)

            val mDialogView = LayoutInflater.from(context).inflate(R.layout.question_select_image_full, null)

            val close = mDialogView.findViewById<ImageButton>(R.id.close_dialog)
            val image = mDialogView.findViewById<ImageView>(R.id.image_full_screen)
            close.setOnClickListener{
                dialog.cancel()
            }
            Glide.with(context)
                    .load(URL(choices.first.imageUrl))
                    .fitCenter().into(image)

            dialog.setContentView(mDialogView)
            dialog.show()
            true
        }
        val longAction2 = OnLongClickListener {
            val dialog = Dialog(context)
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)

            val mDialogView = LayoutInflater.from(context).inflate(R.layout.question_select_image_full, null)

            val close = mDialogView.findViewById<ImageButton>(R.id.close_dialog)
            val image = mDialogView.findViewById<ImageView>(R.id.image_full_screen)
            close.setOnClickListener{
                dialog.cancel()
            }
            Glide.with(context)
                    .load(URL(choices.second.imageUrl))
                    .fitCenter().into(image)

            dialog.setContentView(mDialogView)
            dialog.show()
            true
        }
        mFirstImage.setOnLongClickListener(longAction1)
        mSecondImage.setOnLongClickListener(longAction2)
    }

    fun getSelectedChoice() = mSelectedChoice

    fun bindSelected(data: ArrayList<QuestionChoice>) {

        if(data != null && data.size > 0){
            setSelectedItem(data[0])
        }
    }

}