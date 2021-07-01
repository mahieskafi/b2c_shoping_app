package com.srp.eways.ui.survey

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.model.survey.Poll
import com.srp.eways.model.survey.QuestionChoice
import com.srp.eways.ui.survey.imageselector.ImageSelectorQuestion
import com.srp.eways.ui.survey.multichoicequestion.MultiChoiceQuestion
import com.srp.eways.ui.survey.singlechoicequestion.SingleChoiceQuestion
import com.srp.eways.ui.view.button.ButtonElement
import com.srp.eways.ui.view.toolbar.WeiredToolbar
import com.srp.eways.util.Utils

class SurveyActivity : AppCompatActivity() {

    enum class SurveyState {
        STATE_START,
        STATE_QUESTION,
        STATE_END
    }

    companion object{
        public const val SURVEY_SHARE_NAME = "survey_info"
        const val QUESTION_TYPE_RADIO = 0
        const val QUESTION_TYPE_MULTI_CHOICE = 1
        const val QUESTION_TYPE_IMAGE_RADIO = 2
        const val QUESTION_TYPE_RATE = 3
    }

    val mResources = DIMain.getABResources()
    val mEventSender = DIMain.getEventSender()

    private lateinit var mToolbar : WeiredToolbar

    //Start views
    private lateinit var mStartContainer : LinearLayout
    private lateinit var mStartIcon : ImageView
    private lateinit var mStartName : TextView
    private lateinit var mStartTitle : TextView
    private lateinit var mStartDescription : TextView
    private lateinit var mStartButton : ButtonElement


    //Question views
    private lateinit var mQuestionContainer : LinearLayout
    private lateinit var mQuestionIndicator : SurveyQuestionIndicator
    private lateinit var mQuestionNumber : TextView
    private lateinit var mQuestionText : TextView
    private lateinit var mQuestionAnswerContainer : FrameLayout
    private lateinit var mQuestionButton : ButtonElement


    //End views
    private lateinit var mEndContainer : LinearLayout
    private lateinit var mEndIcon : ImageView
    private lateinit var mEndTitle : TextView
    private lateinit var mEndDescription : TextView
    private lateinit var mEndButton : ButtonElement

    private lateinit var mPoll : Poll
    private var mCurrentState = SurveyState.STATE_START
    private var mCurrentQuestion = 0

    private lateinit var mSubmittedAnswers : ArrayList<Pair<Int, ArrayList<QuestionChoice>>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey)

        mToolbar = findViewById(R.id.toolbar)


        mStartContainer = findViewById(R.id.start_container)
        mStartIcon = findViewById(R.id.start_icon)
        mStartName = findViewById(R.id.start_name)
        mStartTitle = findViewById(R.id.start_title)
        mStartDescription = findViewById(R.id.start_description)
        mStartButton = findViewById(R.id.start_button)


        mQuestionContainer = findViewById(R.id.question_container)
        mQuestionIndicator = findViewById(R.id.question_indicator)
        mQuestionNumber = findViewById(R.id.question_number)
        mQuestionText = findViewById(R.id.question_text)
        mQuestionAnswerContainer = findViewById(R.id.question_answer_container)
        mQuestionButton = findViewById(R.id.question_button)

        mEndContainer = findViewById(R.id.end_container)
        mEndIcon = findViewById(R.id.end_icon)
        mEndTitle = findViewById(R.id.end_title)
        mEndDescription = findViewById(R.id.end_description)
        mEndButton = findViewById(R.id.end_button)

        setUpToolbar()

        setUpPoll()

        setUpStartPage()

        setQuestionPage()

        setEndPage()

    }

    private fun setUpToolbar(){

        //Toolbar Settings
        mToolbar.showNavigationDrawer(false)
        mToolbar.setShowDeposit(false)
        mToolbar.setShowNavigationUp(true)
        mToolbar.setShowTitle(true)
        mToolbar.setShowShop(false)
        mToolbar.setTitle(mResources.getString(R.string.survey_title))
        mToolbar.setBackgroundColor(mResources.getColor(R.color.survey_toolbar_backgrond_color))
        mToolbar.setTitleTextColor(mResources.getColor(R.color.survey_toolbar_title_color))
        mToolbar.setTitleTextSize(mResources.getDimenPixelSize(R.dimen.survey_toolbar_title_text_size).toFloat())
        mToolbar.setOnBackClickListener{
            onBackPress()
        }
    }
    
    private fun setUpPoll(){
        
        mPoll = Gson().fromJson(intent.getStringExtra("survey"), Poll::class.java)

        mSubmittedAnswers = arrayListOf()

        for(i in 0 until mPoll.questions.size){
            mSubmittedAnswers.add(Pair(mPoll.questions[i].type, ArrayList<QuestionChoice>()))
        }
    }

    private fun setUpStartPage(){

        val firstFont = ResourcesCompat.getFont(this, R.font.iran_yekan)
        val secondFont = ResourcesCompat.getFont(this, R.font.iran_yekan_bold)

        mStartIcon.setImageDrawable(mResources.getDrawable(R.drawable.survey_start_icon))

        mStartName.setTextSize(TypedValue.COMPLEX_UNIT_PX, mResources.getDimenPixelSize(R.dimen.survey_start_page_name_text_size).toFloat())
        mStartName.typeface = firstFont
        mStartName.setTextColor(mResources.getColor(R.color.survey_start_page_name_text_color))
        mStartName.text = Utils.toPersianNumber("فلانی عزیز:")


        mStartTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mResources.getDimenPixelSize(R.dimen.survey_start_page_title_text_size).toFloat())
        mStartTitle.typeface = secondFont
        mStartTitle.setTextColor(mResources.getColor(R.color.survey_start_page_title_text_color))
        mStartTitle.text = Utils.toPersianNumber(mPoll.title)


        mStartDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, mResources.getDimenPixelSize(R.dimen.survey_start_page_description_text_size).toFloat())
        mStartDescription.typeface = firstFont
        mStartDescription.setTextColor(mResources.getColor(R.color.survey_start_page_description_text_color))
        mStartDescription.text = Utils.toPersianNumber(mPoll.description)

        mStartButton.setLoadingVisibility(GONE)
        mStartButton.setTextSize(mResources.getDimenPixelSize(R.dimen.survey_start_page_button_text_size).toFloat())
        mStartButton.setTextColor(mResources.getColor(R.color.survey_start_page_button_text_color))
        mStartButton.setEnabledBackground(mResources.getDrawable(R.drawable.survey_start_button_background_enabled))
        mStartButton.setDisableBackground(mResources.getDrawable(R.drawable.survey_start_button_background_enabled))
        mStartButton.setText(mResources.getString(R.string.survey_start_page_button_text))
        mStartButton.setEnable(true)
        mStartButton.setClickListener(View.OnClickListener {

            mStartContainer.visibility = GONE
            mQuestionContainer.visibility = VISIBLE

            enterSurvey()
        })
    }

    private fun setQuestionPage(){

        val firstFont = ResourcesCompat.getFont(this, R.font.iran_yekan_medium)

        mQuestionIndicator.setQuestionCount(mPoll.questions.size)
        mQuestionIndicator.indicateTo(1)

        mQuestionNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimenPixelSize(R.dimen.survey_question_page_number_text_size).toFloat())
        mQuestionNumber.setTextColor(mResources.getColor(R.color.survey_question_page_number_color))
        mQuestionNumber.typeface = firstFont

        mQuestionText.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                mResources.getDimenPixelSize(R.dimen.survey_question_page_question_text_size).toFloat())
        mQuestionText.setTextColor(mResources.getColor(R.color.survey_question_page_text_color))
        mQuestionText.typeface = firstFont

        mQuestionButton.setLoadingVisibility(GONE)
        mQuestionButton.setTextSize(mResources.getDimenPixelSize(R.dimen.survey_question_page_button_text_size).toFloat())
        mQuestionButton.setTextColor(mResources.getColor(R.color.survey_question_page_button_text_color))
        mQuestionButton.setEnabledBackground(mResources.getDrawable(R.drawable.survey_question_button_enable))
        mQuestionButton.setDisableBackground(mResources.getDrawable(R.drawable.survey_question_button_disable))
        mQuestionButton.setText(mResources.getString(R.string.survey_question_page_button_text))
        mQuestionButton.setEnable(true)
        goQuestion(mCurrentQuestion)

    }

    private fun setEndPage(){

        val firstFont = ResourcesCompat.getFont(this, R.font.iran_yekan)
        val secondFont = ResourcesCompat.getFont(this, R.font.iran_yekan_bold)

        mEndIcon.setImageDrawable(mResources.getDrawable(R.drawable.survey_end_icon))

        mEndTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mResources.getDimenPixelSize(R.dimen.survey_end_page_title_text_size).toFloat())
        mEndTitle.typeface = secondFont
        mEndTitle.setTextColor(mResources.getColor(R.color.survey_end_page_title_text_color))
        mEndTitle.text = mPoll.endTitle

        mEndDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, mResources.getDimenPixelSize(R.dimen.survey_end_page_description_text_size).toFloat())
        mEndDescription.typeface = firstFont
        mEndDescription.setTextColor(mResources.getColor(R.color.survey_end_page_description_text_color))
        mEndDescription.text = Utils.toPersianNumber(mPoll.endDescription)

        mEndButton.setLoadingVisibility(GONE)
        mEndButton.setTextSize(mResources.getDimenPixelSize(R.dimen.survey_end_page_button_text_size).toFloat())
        mEndButton.setTextColor(mResources.getColor(R.color.survey_end_page_button_text_color))
        mEndButton.setEnabledBackground(mResources.getDrawable(R.drawable.survey_question_button_enable))
        mEndButton.setDisableBackground(mResources.getDrawable(R.drawable.survey_question_button_disable))
        mEndButton.setText(mResources.getString(R.string.survey_end_page_button_text))
        mEndButton.setEnable(true)
        mEndButton.setClickListener(View.OnClickListener {

            finish()
        })

    }

    override fun onBackPressed() {
        onBackPress()
    }
    private fun onBackPress(){

        if(mCurrentState == SurveyState.STATE_START && mCurrentQuestion != 0){
            goQuestion(mCurrentQuestion - 1)
        }
        else{
            super.onBackPressed()
            finish()
        }

    }

    private fun goQuestion(position : Int) {

        mCurrentQuestion = position

        mQuestionIndicator.indicateTo(mCurrentQuestion + 1)

        mQuestionNumber.text = Utils.toPersianNumber("سوال ${mCurrentQuestion + 1}:")

        mQuestionText.text = Utils.toPersianNumber(mPoll.questions[mCurrentQuestion].text)

        if(mCurrentQuestion == mPoll.questions.size - 1){
            mQuestionButton.setText("ثبت نظرات")
        }
        else{
            mQuestionButton.setText(mResources.getString(R.string.survey_question_page_button_text))
        }

        mQuestionAnswerContainer.removeAllViews()

        when(mPoll.questions[mCurrentQuestion].type){
            QUESTION_TYPE_RATE -> {
                val view = SurveyPointSelector(this)
                view.setChoices(mPoll.questions[mCurrentQuestion])
                view.setDefaultPoint(mSubmittedAnswers[mCurrentQuestion].second)

                mQuestionButton.setEnable(false)

                view.getSelectedPoint().observe(this, Observer<QuestionChoice> { t ->
                    if(t != null){
                        mQuestionButton.setEnable(true)
                    } else{
                        mQuestionButton.setEnable(false)
                    }
                })

                mQuestionButton.setClickListener(View.OnClickListener {

                    mSubmittedAnswers[mCurrentQuestion].second.clear()
                    mSubmittedAnswers[mCurrentQuestion].second.add(view.getSelectedPoint().value!!)
                    questionButtonAction()

                })

                mQuestionAnswerContainer.addView(view)
            }
            QUESTION_TYPE_RADIO ->{
                val view = SingleChoiceQuestion(this)
                view.setChoices(mPoll.questions[mCurrentQuestion])

                mQuestionButton.setEnable(false)

                view.getSelectedChoice().observe(this, Observer<QuestionChoice> { t ->
                    if(t != null){
                        mQuestionButton.setEnable(true)
                    } else{
                        mQuestionButton.setEnable(false)
                    }
                })

                view.bindSelected(mSubmittedAnswers[mCurrentQuestion].second)

                mQuestionButton.setClickListener(View.OnClickListener {

                    mSubmittedAnswers[mCurrentQuestion].second.clear()
                    mSubmittedAnswers[mCurrentQuestion].second.add(view.getSelectedChoice().value!!)
                    questionButtonAction()

                })

                mQuestionAnswerContainer.addView(view)
            }
            QUESTION_TYPE_MULTI_CHOICE -> {
                val view = MultiChoiceQuestion(this)
                view.setChoices(mPoll.questions[mCurrentQuestion])

                mQuestionButton.setEnable(false)

                view.getSelectedChoice().observe(this, Observer<ArrayList<QuestionChoice>> { t ->
                    if(t != null && t.size > 0){
                        mQuestionButton.setEnable(true)
                    } else{
                        mQuestionButton.setEnable(false)
                    }
                })

                view.bindSelected(mSubmittedAnswers[mCurrentQuestion].second)

                mQuestionButton.setClickListener(View.OnClickListener {

                    mSubmittedAnswers[mCurrentQuestion].second.clear()
                    mSubmittedAnswers[mCurrentQuestion].second.addAll(view.getSelectedChoice().value!!)
                    questionButtonAction()

                })

                mQuestionAnswerContainer.addView(view)
            }
            QUESTION_TYPE_IMAGE_RADIO ->{
                val view = ImageSelectorQuestion(this)
                view.setChoices(mPoll.questions[mCurrentQuestion])

                mQuestionButton.setEnable(false)

                view.getSelectedChoice().observe(this, Observer<QuestionChoice> { t ->
                    if(t != null){
                        mQuestionButton.setEnable(true)
                    } else{
                        mQuestionButton.setEnable(false)
                    }
                })

                view.bindSelected(mSubmittedAnswers[mCurrentQuestion].second)

                mQuestionButton.setClickListener(View.OnClickListener {

                    mSubmittedAnswers[mCurrentQuestion].second.clear()
                    mSubmittedAnswers[mCurrentQuestion].second.add(view.getSelectedChoice().value!!)
                    questionButtonAction()

                })

                mQuestionAnswerContainer.addView(view)
            }
        }
    }

    private fun isEnteredBefore() : Boolean{
        val preferences = applicationContext.getSharedPreferences(SURVEY_SHARE_NAME, Context.MODE_PRIVATE)

        return preferences.getBoolean("survey_" + mPoll.id + "_enter", false)
    }
    private fun enterSurvey() {

        if (!isEnteredBefore()) {
            mResources.recordEvent(mPoll.openEvent)

            val preferences = applicationContext.getSharedPreferences(SURVEY_SHARE_NAME, Context.MODE_PRIVATE)
            preferences.edit().putBoolean("survey_" + mPoll.id + "_enter", true).commit()
        }
    }
    private fun finishSurvey(){
        val preferences = applicationContext.getSharedPreferences(SURVEY_SHARE_NAME, Context.MODE_PRIVATE)

        preferences.edit().putBoolean("survey_" + mPoll.id + "_complete", true).commit()
    }

    private fun questionButtonAction(){
        if(mCurrentQuestion == mPoll.questions.size - 1){

            for(item in mSubmittedAnswers){
                for(ans in item.second){
                    mResources.recordEvent(ans.event)
                }
            }

            finishSurvey()

            mResources.recordEvent(mPoll.endEvent)

            mQuestionContainer.visibility = GONE
            mEndContainer.visibility = VISIBLE
        }
        else{
            goQuestion(mCurrentQuestion + 1)
        }
    }
}
