package com.srp.eways.ui.view.transacionitem

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Typeface
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
import com.srp.eways.model.deposit.transaction.DepositTransactionItem
import com.srp.eways.model.transaction.charge.ChargeSale
import com.srp.eways.ui.bill.ShowMoreClickListener
import com.srp.eways.util.Utils
import com.yashoid.sequencelayout.SequenceLayout
import com.yashoid.sequencelayout.SizeInfo

/**
 * Created by ErfanG on 08/09/2019.
 */
class DepositTransactionItem : FrameLayout {

    interface OnDepositTransactionItemClickListener {
        fun onShowMoreClickListener(isShowMore: Boolean, depositTransactionItem: DepositTransactionItem?)
    }

    lateinit var mResult: ImageView
    lateinit var mType: TextView
    lateinit var mTrackingNumber: AppCompatTextView
    lateinit var mTrackingNumberSuffix: TextView
    lateinit var mDateIcon: ImageView
    lateinit var mDate: TextView
    lateinit var mTimeIcon: ImageView
    lateinit var mTime: TextView
    lateinit var mValue: TextView
    lateinit var mValueSuffix: TextView

    lateinit var mFailedIcon: Drawable
    lateinit var mUnknownIcon: Drawable
    lateinit var mOkIcon: Drawable

    private lateinit var mMore: AppCompatImageView
    private lateinit var mMoreContainer: LinearLayout
    private lateinit var mSequenseLayout: SequenceLayout
    private var mOpen = false
    private val ANIM_DURATION = 200L

    companion object {
        const val DEPOSIT_SUCCESS = 1
        const val DEPOSIT_FAIL = 2
    }

    constructor(context: Context) : super(context) {
        initialize(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(context, attrs, defStyleAttr)
    }

    fun initialize(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {

        clipChildren = false
        clipToPadding = false
        super.setClipChildren(false)
        super.setClipToPadding(false)
        val AB = DIMain.getABResources()

        background = AB.getDrawable(R.drawable.item_charge_transaction_back1)
        ViewCompat.setElevation(this, AB.getDimenPixelSize(R.dimen.item_charge_transaction_elevation).toFloat())

        LayoutInflater.from(context).inflate(R.layout.item_deposit_transaction, this, true)

        mResult = findViewById(R.id.transaction_result)
        mType = findViewById(R.id.transaction_type)
        mTrackingNumber = findViewById(R.id.tracking_number)
        mTrackingNumberSuffix = findViewById(R.id.tracking_number_suffix)
        mDateIcon = findViewById(R.id.transaction_date_icon)
        mDate = findViewById(R.id.transaction_date)
        mTimeIcon = findViewById(R.id.transaction_time_icon)
        mTime = findViewById(R.id.transaction_time)
        mValue = findViewById(R.id.deposit_transaction_value)
        mValueSuffix = findViewById(R.id.transaction_value_suffix)

        mMore = findViewById(R.id.more)
        mMoreContainer = findViewById(R.id.more_deposit_container)
        mSequenseLayout = findViewById(R.id.sequence_deposit)


        //Result icon
        mFailedIcon = AB.getDrawable(R.drawable.transaction_item_result_failed)
        mUnknownIcon = AB.getDrawable(R.drawable.transaction_item_result_unknown)
        mOkIcon = AB.getDrawable(R.drawable.transaction_item_result_ok)
        mResult.setImageDrawable(mOkIcon)

        //Transaction_type
        mType.setTextColor(AB.getColor(R.color.transaction_deposit_item_type_color))
        mType.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.transaction_deposit_item_type_size).toFloat())
        mType.gravity = Gravity.TOP

        //Tracking number
        mTrackingNumber.setTextColor(AB.getColor(R.color.transaction_deposit_item_phone_color))
        mTrackingNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.transaction_deposit_item_phone_size).toFloat())

        //Tracking Suffix
        mTrackingNumberSuffix.setTextColor(AB.getColor(R.color.transaction_deposit_item_tracking_suffix_color))
        mTrackingNumberSuffix.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.transaction_deposit_item_tracking_suffix_size).toFloat())
        mTrackingNumberSuffix.setText(AB.getString(R.string.transaction_deposit_item_tracking_suffix_string))

        //Value Suffix
        mValueSuffix.setTextColor(AB.getColor(R.color.transaction_deposit_item_value_suffix_color))
        mValueSuffix.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.transaction_deposit_item_value_suffix_size).toFloat())
        mValueSuffix.setText(AB.getString(R.string.transaction_deposit_item_value_suffix_string))

        //Value
        mValue.setTextColor(AB.getColor(R.color.transaction_deposit_item_value_color))
        mValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.transaction_deposit_item_value_size).toFloat())

        //DateIcon
        mDateIcon.setImageDrawable(AB.getDrawable(R.drawable.transaction_item_date_icon))

        //Date
        mDate.setTextColor(AB.getColor(R.color.transaction_deposit_item_date_color))
        mDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.transaction_deposit_item_date_size).toFloat())

        //TimeIcon
        mTimeIcon.setImageDrawable(AB.getDrawable(R.drawable.transaction_item_time_icon))

        //Time
        mTime.setTextColor(AB.getColor(R.color.transaction_deposit_item_time_color))
        mTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.transaction_deposit_item_time_size).toFloat())

        mMore.setImageDrawable(resources.getDrawable(R.drawable.ic_charge_transaction_show_more))

        setAttrs(attrs)

    }


    private fun setAttrs(attrs: AttributeSet?) {

        var mainTypeface: Typeface?
        var secondTypeface: Typeface?

        if (attrs != null) {
            val attrArray = context.obtainStyledAttributes(attrs, R.styleable.TransactionItem, 0, 0)

            val mainTypefaceResourceId = attrArray.getResourceId(R.styleable.TransactionItem_firstFont, 0)
            val secondTypefaceResourceId = attrArray.getResourceId(R.styleable.TransactionItem_secondFont, 0)

            mainTypeface = ResourcesCompat.getFont(context, mainTypefaceResourceId)
            secondTypeface = ResourcesCompat.getFont(context, secondTypefaceResourceId)

            attrArray.recycle()
        } else {

            mainTypeface = ResourcesCompat.getFont(context, R.font.iran_yekan)
            secondTypeface = ResourcesCompat.getFont(context, R.font.iran_yekan_light)
        }

        mValue.typeface = mainTypeface
        mType.typeface = mainTypeface

        mValueSuffix.typeface = secondTypeface
        mDate.typeface = secondTypeface
        mTime.typeface = secondTypeface
        mTrackingNumber.typeface = secondTypeface
        mTrackingNumberSuffix.typeface = mainTypeface
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

        mRightList.addView(createKeyValuePair(resources.getString(R.string.deposit_transaction_payment_id_title), paymentId))

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

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
    }


    fun setTrackingNumber(trackingNumber: String) {
        mTrackingNumber.setText(Utils.toPersianNumber(trackingNumber))
    }

    fun setTransactionValue(value: Int) {
        mValue.setText(Utils.toPersianNumber(String.format("%,d", value)))
    }

    fun setTime(value: String) {
        mTime.setText(Utils.toPersianNumber(value))
    }

    fun setDate(value: String) {
        mDate.setText(Utils.toPersianNumber(value))
    }

    fun setType(type: String) {
        mType.setText(Utils.toPersianNumber(type))
    }


    fun setResult(newResult: Int) {

        if (newResult == DEPOSIT_SUCCESS) {

            mResult.setImageDrawable(mOkIcon)
        } else if (newResult == DEPOSIT_FAIL) {

            mResult.setImageDrawable(mFailedIcon)
        } else {

            mResult.setImageDrawable(mUnknownIcon)
        }
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
            text = Utils.toPersianNumber(value ?: "----")
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
        }
    }

    private fun openClose(open: Boolean) {

        if (mOpen != open) {
            mOpen = open

            var extraSize = 136

            val anim: ValueAnimator = if (mOpen) {
                ValueAnimator.ofInt(0, extraSize)
            } else {
                ValueAnimator.ofInt(extraSize, 0)
            }

            anim.addUpdateListener {

                mSequenseLayout.findSequenceById("seq_deposit").spans[0].size = (it.animatedValue as Int).toFloat()
                mSequenseLayout.findSequenceById("seq_deposit").spans[0].metric = SizeInfo.METRIC_PX
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

            anim.start()
            rotationAnim.start()
        }

    }
}