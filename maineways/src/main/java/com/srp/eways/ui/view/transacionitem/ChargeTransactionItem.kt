package com.srp.eways.ui.view.transacionitem

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.ui.bill.ShowMoreClickListener
import com.srp.eways.ui.view.ViewUtils
import com.srp.eways.util.Utils
import com.yashoid.sequencelayout.SequenceLayout
import com.yashoid.sequencelayout.SizeInfo.METRIC_PX

/**
 * Created by ErfanG on 30/08/2019.
 */
class ChargeTransactionItem : FrameLayout {

    lateinit var mResult: ImageView
    lateinit var mType: TextView
    lateinit var mPhoneNumber: TextView
    lateinit var mDateIcon: ImageView
    lateinit var mDate: TextView
    lateinit var mTimeIcon: ImageView
    lateinit var mTime: TextView
    lateinit var mValue: TextView
    lateinit var mValueSuffix: TextView
    private lateinit var mMore: AppCompatImageView
    private lateinit var mMoreContainer: LinearLayout
    private lateinit var mSequenseLayout: SequenceLayout

    lateinit var mFailedIcon: Drawable
    lateinit var mUnknownIcon: Drawable
    lateinit var mOkIcon: Drawable

    private var mOpen = false
    private val ANIM_DURATION = 200L

//    private var mBackHeight: Int = 0

    constructor(context: Context) : super(context) {
        initilize(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initilize(context, attrs)
    }


    fun initilize(context: Context, attrs: AttributeSet?) {

        clipChildren = false
        clipToPadding = false
        super.setClipChildren(false)
        super.setClipToPadding(false)

        val AB = DIMain.getABResources()

        background = AB.getDrawable(R.drawable.item_charge_transaction_back1)
        ViewCompat.setElevation(this, AB.getDimenPixelSize(R.dimen.item_charge_transaction_elevation).toFloat())

        LayoutInflater.from(context).inflate(R.layout.item_charge_transaction, this, true)

        mResult = findViewById(R.id.transaction_result)
        mType = findViewById(R.id.transaction_type)
        mPhoneNumber = findViewById(R.id.phone_number)
        mDateIcon = findViewById(R.id.transaction_date_icon)
        mDate = findViewById(R.id.transaction_date)
        mTimeIcon = findViewById(R.id.transaction_time_icon)
        mTime = findViewById(R.id.transaction_time)
        mValue = findViewById(R.id.transaction_value)
        mValueSuffix = findViewById(R.id.transaction_value_suffix)
        mMore = findViewById(R.id.more)
        mMoreContainer = findViewById(R.id.more_container)
        mSequenseLayout = findViewById(R.id.sequence)

        //region AB attrs


//        mBackHeight = AB.getDimenPixelSize(R.dimen.item_charge_inquiry_back1_height) + AB.getDimenPixelSize(R.dimen.item_charge_inquiry_back2_height)
        //Result icon
        mFailedIcon = AB.getDrawable(R.drawable.transaction_item_result_failed)
        mUnknownIcon = AB.getDrawable(R.drawable.transaction_item_result_unknown)
        mOkIcon = AB.getDrawable(R.drawable.transaction_item_result_ok)
        mResult.setImageDrawable(mOkIcon)

        //Transaction_type
        mType.setTextColor(AB.getColor(R.color.transaction_charge_item_type_color))
        mType.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.transaction_charge_item_type_size).toFloat())
        mType.gravity = Gravity.TOP
        mType.setLineSpacing(0f, 1.3f)

        //Phone number
        mPhoneNumber.setTextColor(AB.getColor(R.color.transaction_charge_item_phone_color))
        mPhoneNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.transaction_charge_item_phone_size).toFloat())

        //Value Suffix
        mValueSuffix.setTextColor(AB.getColor(R.color.transaction_charge_item_value_suffix_color))
        mValueSuffix.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.transaction_charge_item_value_suffix_size).toFloat())
        mValueSuffix.setText(AB.getString(R.string.transaction_charge_item_value_suffix_string))

        //Value
        mValue.setTextColor(AB.getColor(R.color.transaction_charge_item_value_color))
        mValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.transaction_charge_item_value_size).toFloat())

        //DateIcon
        mDateIcon.setImageDrawable(AB.getDrawable(R.drawable.transaction_item_date_icon))

        //Date
        mDate.setTextColor(AB.getColor(R.color.transaction_charge_item_date_color))
        mDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.transaction_charge_item_date_size).toFloat())

        //TimeIcon
        mTimeIcon.setImageDrawable(AB.getDrawable(R.drawable.transaction_item_time_icon))

        //Time
        mTime.setTextColor(AB.getColor(R.color.transaction_charge_item_time_color))
        mTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.transaction_charge_item_time_size).toFloat())

        //endregion
        mMore.setImageDrawable(resources.getDrawable(R.drawable.ic_charge_transaction_show_more))

        setAttrs(attrs)

    }

    private fun setAttrs(attrs: AttributeSet?) {

        if (attrs != null) {

            val attrArray = context.obtainStyledAttributes(attrs, R.styleable.TransactionItem, 0, 0)

            val mainTypefaceResourceId = attrArray.getResourceId(R.styleable.TransactionItem_firstFont, 0)
            val secondTypefaceResourceId = attrArray.getResourceId(R.styleable.TransactionItem_secondFont, 0)


            val mainTypeface = ResourcesCompat.getFont(context, mainTypefaceResourceId)
            val secondTypeface = ResourcesCompat.getFont(context, secondTypefaceResourceId)

            mValue.typeface = mainTypeface
            mType.typeface = mainTypeface

            mValueSuffix.typeface = secondTypeface
            mDate.typeface = secondTypeface
            mTime.typeface = secondTypeface
            mPhoneNumber.typeface = secondTypeface

            attrArray.recycle()
        } else {
            val first = ResourcesCompat.getFont(context, R.font.iran_yekan)
            val second = ResourcesCompat.getFont(context, R.font.iran_yekan_light)

            mValue.setTypeface(first)
            mType.setTypeface(first)

            mValueSuffix.setTypeface(second)
            mDate.setTypeface(second)
            mTime.setTypeface(second)
            mPhoneNumber.setTypeface(second)
        }
    }


    fun setMoreDetails(paymentId: String) {

        val resources = DIMain.getABResources()

        mMoreContainer.removeAllViews()

        var mDivider = View(context).apply {
            val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, resources.getDimenPixelSize(R.dimen.bill_report_item_divider_height))
            lp.bottomMargin = resources.getDimenPixelSize(R.dimen.bill_report_item_divider_margin)
            lp.bottomMargin = resources.getDimenPixelSize(R.dimen.bill_report_item_divider_margin)

            layoutParams = lp
            setBackgroundColor(resources.getColor(R.color.transaction_charge_item_divider_color))

        }

        var mRightList = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f).apply {
                gravity = Gravity.RIGHT
            }
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.RIGHT
        }

        mRightList.addView(createKeyValuePair(resources.getString(R.string.bill_report_item_more_key_1), paymentId))

        val midParent = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            orientation = LinearLayout.HORIZONTAL
            weightSum = 1f
            addView(mRightList)
        }

        mMoreContainer.addView(mDivider)
        mMoreContainer.addView(midParent)

        mSequenseLayout.requestLayout()
    }

    private fun createKeyValuePair(key: String, value: String?): LinearLayout {

        val resources = DIMain.getABResources()

        val key = AppCompatTextView(context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setTextColor(resources.getColor(R.color.bill_report_item_more_key_color))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_report_item_more_key_size).toFloat())
            typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_light)
            text = Utils.toPersianNumber(key) + "   "
            gravity = Gravity.RIGHT
        }

        val value = AppCompatTextView(context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setTextColor(resources.getColor(R.color.bill_report_item_more_value_color))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_report_item_more_value_size).toFloat())
            typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_medium)
            text = Utils.toPersianNumber(value ?: "-----")
            gravity = Gravity.RIGHT
        }


        return LinearLayout(context).apply {

            val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.bottomMargin = resources.getDimenPixelSize(R.dimen.bill_report_item_more_key_value_margin_bottom)
            layoutParams = lp
            orientation = LinearLayout.HORIZONTAL

            addView(value)
            addView(key)
        }

    }

    fun setShowMore(itemShowMore: Boolean) {
        openClose(itemShowMore)
    }

    fun setOnShowMoreClickListener(listener: ShowMoreClickListener) {
        mMore.setOnClickListener {
            listener.onShowMoreClicked(!mOpen)
//            openClose(!mOpen)
        }
    }

    fun setPhoneNumber(phoneNumber: String) {
        mPhoneNumber.setText(Utils.toPersianNumber(phoneNumber))
    }

    fun setTransactionValue(value: Int) {
        mValue.setText(Utils.toPersianNumber(String.format("%,d", value)))
    }

    fun setTime(value: String) {
        mTime.setText(Utils.toPersianNumber(value))
    }

    fun setDate(value: String) {
        mDate.text = (Utils.toPersianNumber(value))
    }

    fun setType(type: String) {
        mType.text = Utils.toPersianPriceNumberWithText(type)
    }


    fun setResult(newResult: Int) {
        if (newResult.equals(2))
            mResult.setImageDrawable(mOkIcon)
        else if (newResult.equals(8))
            mResult.setImageDrawable(mUnknownIcon)
        else
            mResult.setImageDrawable(mFailedIcon)
    }

    private fun openClose(open: Boolean) {

        if (mOpen != open) {
            mOpen = open
            var abResources = DIMain.getABResources()

            var extraSize = 136

            var extraBackSize = abResources.getDimenPixelSize(R.dimen.item_charge_transaction_back1_height) + 136

//            val animBack: ValueAnimator = if (mOpen) {
//                ValueAnimator.ofInt(abResources.getDimenPixelSize(R.dimen.item_charge_transaction_back1_height), extraBackSize)
//            } else {
//                ValueAnimator.ofInt(extraBackSize, abResources.getDimenPixelSize(R.dimen.item_charge_transaction_back1_height))
//            }
//
//            animBack.addUpdateListener {
//                mSequenseLayout.findSequenceById("back").spans[0].size = (it.animatedValue as Int).toFloat()
//                mSequenseLayout.findSequenceById("back").spans[0].metric = METRIC_PX
//                mSequenseLayout.requestLayout()
//
//            }
//            animBack.duration = ANIM_DURATION

            val anim: ValueAnimator = if (mOpen) {
                ValueAnimator.ofInt(0, extraSize)
            } else {
                ValueAnimator.ofInt(extraSize, 0)
            }

            anim.addUpdateListener {

                mSequenseLayout.findSequenceById("seq").spans[0].size = (it.animatedValue as Int).toFloat()
                mSequenseLayout.findSequenceById("seq").spans[0].metric = METRIC_PX
                mSequenseLayout.requestLayout()
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


//            animBack.start()
            anim.start()
            rotationAnim.start()
        }

    }
}