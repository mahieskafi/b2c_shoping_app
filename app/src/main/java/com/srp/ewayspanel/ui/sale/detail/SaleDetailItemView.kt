package com.srp.ewayspanel.ui.sale.detail

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Paint
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
import com.srp.eways.di.DIMain
import com.srp.eways.ui.bill.ShowMoreClickListener
import com.srp.eways.ui.view.ViewUtils
import com.srp.eways.util.Utils
import com.srp.ewayspanel.R
import com.yashoid.sequencelayout.SequenceLayout

class SaleDetailItemView : SequenceLayout {

    enum class Status {
        OK, FAILED, UNKNOWN
    }

    private val ANIM_DURATION = 200L

    //Views
    private val mTitle: AppCompatTextView
    private val mPrice: AppCompatTextView
    private val mPriceSuffix: AppCompatTextView
    private val mBillIdTitle: AppCompatTextView
    private val mBillId: AppCompatTextView
    private val mDate: AppCompatTextView
    private val mTime: AppCompatTextView
    private val mTitleType: AppCompatTextView

    private val mMore: AppCompatImageView
    private val mMoreContainer: LinearLayout
    private lateinit var mDivider: View
    private lateinit var mRightList: LinearLayout
    private lateinit var mLeftList: LinearLayout
    private lateinit var mRowContainer: View
    private var mOpen = false

    private var extraSize = 100
    private var mRightMargin = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    init {

        LayoutInflater.from(context).inflate(R.layout.sale_detail_item_view, this, true)

        mTitle = findViewById(R.id.title)
        mPrice = findViewById(R.id.price)
        mPriceSuffix = findViewById(R.id.price_suffix)
        mBillIdTitle = findViewById(R.id.bill_id_title)
        mBillId = findViewById(R.id.bill_id)
        mDate = findViewById(R.id.date)
        mTime = findViewById(R.id.time)
        mTitleType = findViewById(R.id.type_title)
        mRowContainer = findViewById(R.id.colaps_background)

        mMore = findViewById(R.id.more)
        mMoreContainer = findViewById(R.id.more_container)


        addSequences(R.xml.sequences_sale_detail_item)


        findSequenceById("seq").spans[1].size = 0f
        requestLayout()

        val resources = DIMain.getABResources()

        background = resources.getDrawable(R.drawable.shop_item_background_horizontal)
        ViewCompat.setElevation(this, resources.getDimenPixelSize(R.dimen.bill_report_item_elevation).toFloat())
        mMoreContainer.background = resources.getDrawable(R.drawable.sale_report_detail_default_background)

        val boldFont = ResourcesCompat.getFont(context, R.font.iran_yekan_bold)
        val mediumFont = ResourcesCompat.getFont(context, R.font.iran_yekan_medium)
        val regularFont = ResourcesCompat.getFont(context, R.font.iran_yekan)
        val lightFont = ResourcesCompat.getFont(context, R.font.iran_yekan_light)
        val extraBold = ResourcesCompat.getFont(context, R.font.iran_yekan_extrabold)
        mRightMargin = resources.getDimen(R.dimen.salereport_detail_margin_right).toInt()

        with(mTitleType) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_report_item_title_size).toFloat())
            setTextColor(resources.getColor(R.color.saleReport_detail_type_title_color))
            typeface = boldFont
            setLineSpacing(0f, 1.4f)
        }

        with(mTitle) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_report_item_title_size).toFloat())
            setTextColor(resources.getColor(R.color.bill_report_item_title_color))
            typeface = mediumFont
        }

        with(mPrice) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_report_item_price_size).toFloat())
            setTextColor(resources.getColor(R.color.saleReport_detail_price_color))
            typeface = regularFont
        }

        with(mPriceSuffix) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_report_item_price_suffix_size).toFloat())
            setTextColor(resources.getColor(R.color.saleReport_detail_price_color))
            typeface = regularFont
            text = resources.getString(R.string.rial)
        }

        with(mBillIdTitle) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_report_item_bill_id_title_size).toFloat())
            setTextColor(resources.getColor(R.color.bill_report_item_bill_id_title_color))
            typeface = regularFont
            text = resources.getString(R.string.sale_detail_report_item_bill_id_title)
        }

        with(mBillId) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_report_item_bill_id_size).toFloat())
            setTextColor(resources.getColor(R.color.bill_report_item_bill_id_color))
            typeface = mediumFont
        }

        with(mDate) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_report_item_date_size).toFloat())
            setTextColor(resources.getColor(R.color.bill_report_item_date_color))
            typeface = lightFont
        }

        with(mTime) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_report_item_time_size).toFloat())
            setTextColor(resources.getColor(R.color.bill_report_item_time_color))
            typeface = lightFont
        }


        mMore.setImageDrawable(resources.getDrawable(R.drawable.bill_report_item_show_more_icon))

    }

    private fun openClose(open: Boolean) {

        if (mOpen != open) {
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

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        var textSize = getTextTypeSize(mTitleType.text.toString())
        var width = ViewUtils.convertPixelsToDp(measuredWidth.toFloat(), context) -
                resources.getDimensionPixelSize(R.dimen.sale_detail_horizontal_margin)

        if (textSize > width) {
            mTitleType.gravity = Gravity.RIGHT
        } else {
            mTitleType.gravity = Gravity.CENTER_HORIZONTAL
        }
    }

    fun setShowMore(itemShowMore: Boolean) {
        openClose(itemShowMore)
    }

    fun setOnShowMoreClickListener(listener: ShowMoreClickListener) {
        setOnClickListener {
            openClose(!mOpen)
            listener.onShowMoreClicked(mOpen)
        }
    }

    fun setMoreDetails(moreList: ArrayList<String>) {

        val resources = DIMain.getABResources()

        mMoreContainer.removeAllViews()
//        mMoreContainer.setBackgroundColor(resources.getColor(R.color.saleReport_detail_mora_container_backgroun_color))
        mMoreContainer.background = resources.getDrawable(R.drawable.sale_report_detail_default_background)


        mRightList = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f).apply {
                gravity = Gravity.RIGHT
                rightMargin = mRightMargin
            }
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.RIGHT
        }

        mLeftList = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f).apply {
                gravity = Gravity.RIGHT
                rightMargin = mRightMargin
            }
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.RIGHT
        }


        mRightList.addView(createKeyValuePair(resources.getString(R.string.sale_datail_report_item_more_key_1), moreList[0]))
        mRightList.addView(createKeyValuePair(resources.getString(R.string.sale_datail_report_item_more_key_2), moreList[1]))
        mRightList.addView(createKeyValuePair(resources.getString(R.string.sale_datail_report_item_more_key_3), moreList[2]))
//        mRightList.addView(createKeyValuePair(resources.getString(R.string.sale_datail_report_item_more_key_4), moreList[3]))
        mLeftList.addView(createKeyValuePair(resources.getString(R.string.sale_datail_report_item_more_key_5), moreList[3]))
        mLeftList.addView(createKeyValuePair(resources.getString(R.string.sale_datail_report_item_more_key_6), moreList[4]))
        mLeftList.addView(createKeyValuePair(resources.getString(R.string.sale_datail_report_item_more_key_7), moreList[5]))


        val midParent = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            orientation = LinearLayout.HORIZONTAL
            weightSum = 2f
            addView(mLeftList)
            addView(mRightList)
        }

        mMoreContainer.addView(midParent)

        requestLayout()
    }

    private fun createKeyValuePair(key: String, value: String?): LinearLayout {

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


    fun setProductTitle(title: String) {
        mTitle.text = title
    }

    fun setTypeTitle(title: String) {
        mTitleType.text = Utils.toPersianNumber(title)
    }

    private fun getTextTypeSize(your_text: String): Float {
        val p = Paint()
        return p.measureText(your_text)
    }

    fun setPrice(price: Long) {
        mPrice.text = Utils.toPersianPriceNumber(price)
    }

    fun setBillId(billId: String) {
        mBillId.text = Utils.toPersianNumber(billId)
    }

    fun setDate(date: String) {
        mDate.text = Utils.toPersianNumber(date)
    }

    fun setTime(time: String) {
        mTime.text = Utils.toPersianNumber(time)
    }


}