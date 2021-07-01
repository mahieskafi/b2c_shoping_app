package com.srp.eways.ui.bill.report

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.ui.bill.ShowMoreClickListener
import com.srp.eways.ui.view.button.ButtonElement
import com.srp.eways.util.Utils
import com.yashoid.sequencelayout.SequenceLayout

/**
 * Created by ErfanG on 5/21/2020.
 */
class BillReportItemView : SequenceLayout{


    enum class Status{
        OK, FAILED, UNKNOWN
    }

    private val ANIM_DURATION = 200L

    //Views
    private val mStatusIcon : AppCompatImageView
    private val mTitle : AppCompatTextView
    private val mPrice : AppCompatTextView
    private val mPriceSuffix : AppCompatTextView
    private val mBillIdTitle : AppCompatTextView
    private val mBillId : AppCompatTextView
    private val mDate : AppCompatTextView
    private val mTime : AppCompatTextView
    private val mSave : AppCompatImageView
    private val mShare : AppCompatImageView
    private val mPay : ButtonElement
    private val mWaiting : ButtonElement
    private val mMore : AppCompatImageView
    private val mMoreContainer : LinearLayout
    private lateinit var mDivider : View
    private lateinit var mRightList : LinearLayout
    private lateinit var mLeftList : LinearLayout

    private var mOpen = false

    private var extraSize = 116

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    init {

        LayoutInflater.from(context).inflate(R.layout.item_bill_report, this, true)

        mStatusIcon = findViewById(R.id.status_icon)
        mTitle = findViewById(R.id.title)
        mPrice = findViewById(R.id.price)
        mPriceSuffix = findViewById(R.id.price_suffix)
        mBillIdTitle = findViewById(R.id.bill_id_title)
        mBillId = findViewById(R.id.bill_id)
        mDate = findViewById(R.id.date)
        mTime = findViewById(R.id.time)
        mSave = findViewById(R.id.save)
        mShare = findViewById(R.id.share)
        mPay = findViewById(R.id.pay)
        mWaiting = findViewById(R.id.waiting)
        mMore = findViewById(R.id.more)
        mMoreContainer = findViewById(R.id.more_container)


        addSequences(R.xml.sequences_bill_report_item)


        findSequenceById("seq").spans[1].size = 0f
        requestLayout()

        val resources = DIMain.getABResources()

        background = resources.getDrawable(R.drawable.bill_report_item_background)
        ViewCompat.setElevation(this, resources.getDimenPixelSize(R.dimen.bill_report_item_elevation).toFloat())

        val boldFont = ResourcesCompat.getFont(context, R.font.iran_yekan_bold)
        val mediumFont = ResourcesCompat.getFont(context, R.font.iran_yekan_medium)
        val regularFont = ResourcesCompat.getFont(context, R.font.iran_yekan)
        val lightFont = ResourcesCompat.getFont(context, R.font.iran_yekan_light)

        with(mTitle){
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_report_item_title_size).toFloat())
            setTextColor(resources.getColor(R.color.bill_report_item_title_color))
            typeface = boldFont
        }

        with(mPrice){
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_report_item_price_size).toFloat())
            setTextColor(resources.getColor(R.color.bill_report_item_price_color))
            typeface = mediumFont
        }

        with(mPriceSuffix){
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_report_item_price_suffix_size).toFloat())
            setTextColor(resources.getColor(R.color.bill_report_item_price_suffix_color))
            typeface = regularFont
            text = resources.getString(R.string.rial)
        }

        with(mBillIdTitle){
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_report_item_bill_id_title_size).toFloat())
            setTextColor(resources.getColor(R.color.bill_report_item_bill_id_title_color))
            typeface = lightFont
            text = resources.getString(R.string.bill_report_item_bill_id_title)
        }

        with(mBillId){
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_report_item_bill_id_size).toFloat())
            setTextColor(resources.getColor(R.color.bill_report_item_bill_id_color))
            typeface = mediumFont
        }

        with(mDate){
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_report_item_date_size).toFloat())
            setTextColor(resources.getColor(R.color.bill_report_item_date_color))
            typeface = lightFont
        }

        with(mTime){
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_report_item_time_size).toFloat())
            setTextColor(resources.getColor(R.color.bill_report_item_time_color))
            typeface = lightFont
        }

        mSave.setImageDrawable(resources.getDrawable(R.drawable.bill_report_item_save))
        mShare.setImageDrawable(resources.getDrawable(R.drawable.bill_report_item_share))

        with(mPay){
            setText(resources.getString(R.string.bill_report_item_pay_text))
            setTextSize(resources.getDimenPixelSize(R.dimen.bill_report_item_pay_text_size).toFloat())
            setTextColor(resources.getColor(R.color.bill_report_item_pay_text_color))
            setEnabledBackground(resources.getDrawable(R.drawable.bill_report_item_pay_background))
            setDisableBackground(resources.getDrawable(R.drawable.bill_report_item_pay_background))
            setLoadingColorFilter(resources.getColor(R.color.bill_inquiry_detail_pay_button_text_color))
            hasIcon(false)
            setEnable(true)
            visibility = View.INVISIBLE
        }

        with(mWaiting){
            setTextTypeFace(mediumFont!!)
            setText(resources.getString(R.string.bill_report_item_waiting_text))
            setTextSize(resources.getDimenPixelSize(R.dimen.bill_report_item_pay_text_size).toFloat())
            setTextColor(resources.getColor(R.color.bill_report_item_waiting_text_color))
            setEnabledBackground(resources.getDrawable(R.drawable.button_background_disable_bill_itemview))
            setDisableBackground(resources.getDrawable(R.drawable.button_background_disable_bill_itemview))
            setLoadingColorFilter(resources.getColor(R.color.bill_inquiry_detail_pay_button_text_color))
            hasIcon(false)
            setEnable(false)
            visibility = View.INVISIBLE
        }

        mMore.setImageDrawable(resources.getDrawable(R.drawable.bill_report_item_show_more_icon))

    }

    private fun openClose(open : Boolean){

        if(mOpen != open) {
            mOpen = open

            val anim: ValueAnimator = if (mOpen) {
                ValueAnimator.ofInt(0, extraSize)
            } else {
                ValueAnimator.ofInt(extraSize, 0)
            }

            anim.addUpdateListener {

                findSequenceById("seq").spans[1].size = (it.animatedValue as Int).toFloat()
                requestLayout()
            }
            anim.duration = ANIM_DURATION



            val rotationAnim: ValueAnimator = if (mOpen) {
                ValueAnimator.ofFloat(0f, 180f)
            } else {
                ValueAnimator.ofFloat(180f, 0f)
            }

            rotationAnim.addUpdateListener {
                mMore.rotation = it.animatedValue as Float
            }
            rotationAnim.duration = ANIM_DURATION


            anim.start()
            rotationAnim.start()


        }

    }


    fun setShowMore(itemShowMore: Boolean) {
        openClose(itemShowMore)
    }

    fun setOnShowMoreClickListener(listener: ShowMoreClickListener) {
        mMore.setOnClickListener {
            openClose(!mOpen)
            listener.onShowMoreClicked(mOpen)
        }
    }

    fun setMoreDetails(moreList : ArrayList<String?>){

        val resources = DIMain.getABResources()

        mMoreContainer.removeAllViews()

        mDivider = View(context).apply {
            val lp = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, resources.getDimenPixelSize(R.dimen.bill_report_item_divider_height))
            lp.topMargin = resources.getDimenPixelSize(R.dimen.bill_report_item_divider_margin)
            lp.bottomMargin = resources.getDimenPixelSize(R.dimen.bill_report_item_divider_margin)

            layoutParams = lp
            setBackgroundColor(resources.getColor(R.color.bill_report_item_divider_color))

        }

        mRightList = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f).apply {
                gravity = Gravity.RIGHT
            }
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.RIGHT
        }

        mLeftList = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f).apply {
                gravity = Gravity.RIGHT
            }
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.RIGHT
        }


        mRightList.addView(createKeyValuePair(resources.getString(R.string.bill_report_item_more_key_1), moreList[0]))
        mRightList.addView(createKeyValuePair(resources.getString(R.string.bill_report_item_more_key_2), moreList[1]))
        mRightList.addView(createKeyValuePair(resources.getString(R.string.bill_report_item_more_key_3), moreList[2]))
        mLeftList.addView(createKeyValuePair(resources.getString(R.string.bill_report_item_more_key_4), moreList[3]))
        mLeftList.addView(createKeyValuePair(resources.getString(R.string.bill_report_item_more_key_5), moreList[4]))


        val midParent = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            orientation = LinearLayout.HORIZONTAL
            weightSum = 2f
            addView(mLeftList)
            addView(mRightList)
        }

        mMoreContainer.addView(mDivider)
        mMoreContainer.addView(midParent)

        requestLayout()
    }

    private fun createKeyValuePair(key : String, value : String?): LinearLayout {

        val resources = DIMain.getABResources()

        val key = AppCompatTextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setTextColor(resources.getColor(R.color.bill_report_item_more_key_color))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_report_item_more_key_size).toFloat())
            typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_light)
            text = Utils.toPersianNumber(key) + "   "
            gravity = Gravity.RIGHT
        }

        val value = AppCompatTextView(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setTextColor(resources.getColor(R.color.bill_report_item_more_value_color))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_report_item_more_value_size).toFloat())
            typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_medium)
            text = Utils.toPersianNumber(value ?: "-----")
            gravity = Gravity.RIGHT
        }


        return LinearLayout(context).apply {

            val lp = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            lp.bottomMargin = resources.getDimenPixelSize(R.dimen.bill_report_item_more_key_value_margin_bottom)
            layoutParams = lp
            orientation = LinearLayout.HORIZONTAL

            addView(value)
            addView(key)
        }

    }

    fun setStatus(status : Status){

        //todo: Update drawable after merge with bill branch. Icons exit in this branch.

        when(status){
            Status.OK ->{
                mPay.visibility = View.INVISIBLE
                mWaiting.visibility = View.INVISIBLE
                mSave.visibility = View.VISIBLE
                mShare.visibility = View.VISIBLE
            }
            Status.FAILED ->{
                mPay.visibility = View.VISIBLE
                mWaiting.visibility = View.INVISIBLE
                mSave.visibility = View.INVISIBLE
                mShare.visibility = View.INVISIBLE
            }
            Status.UNKNOWN ->{
                mPay.visibility = View.INVISIBLE
                mWaiting.visibility = View.VISIBLE
                mSave.visibility = View.INVISIBLE
                mShare.visibility = View.INVISIBLE
            }
        }
    }

    fun setStatusIcon(icon: Int){
        mStatusIcon.setImageDrawable(resources.getDrawable(icon))
    }

    fun setTitle(title : String){
        mTitle.text = Utils.toPersianNumber(title)
    }

    fun setPrice(price : Long){
        mPrice.text = Utils.toPersianPriceNumber(price)
    }

    fun setBillId(billId : String){
        mBillId.text = Utils.toPersianNumber(billId)
    }

    fun setDate(date : String){
        mDate.text = Utils.toPersianNumber(date)
    }

    fun setTime(time : String){
        mTime.text = Utils.toPersianNumber(time)
    }

    fun setSaveAction(action : OnClickListener){
        mSave.setOnClickListener(action)
    }

    fun setShareAction(action : OnClickListener){
        mShare.setOnClickListener(action)
    }

    fun setPayAction(action : OnClickListener){
        mPay.setClickListener(action)
    }

}