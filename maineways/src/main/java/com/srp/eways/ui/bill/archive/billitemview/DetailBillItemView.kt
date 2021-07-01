package com.srp.eways.ui.bill.archive.billitemview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.util.Utils
import com.yashoid.sequencelayout.SequenceLayout

/**
 * Created by ErfanG on 4/15/2020.
 */
class DetailBillItemView : SequenceLayout {

    //Views
    private val mIconBackground: BillIconBackground
    private val mIcon: AppCompatImageView
    private val mRemoveIcon: AppCompatImageView
    private val mTitle: AppCompatTextView
    private val mDate: AppCompatTextView
    private val mTime: AppCompatTextView
    private val mNumberTitle: AppCompatTextView
    private val mNumber: AppCompatTextView
    private val mPrice: AppCompatTextView
    private val mPriceSuffix: AppCompatTextView


//    private var mBillHolePivot = 0f

    private var mCornerRadius: Float = 0f

    //    private var mHoleRadius: Float = 0f
//    private val path = Path()
    private val mBackgroundPaint = Paint()
    private val mBorderPaint = Paint()

    private var mUnSelectedBackgroundColor = Color.parseColor("#9effffff")
    private var mUnselectedBorderColor = Color.parseColor("#cececa")

    private var mSelectedBackground: Drawable
    private var mUnSelectedBackground: Drawable

    private var mIsSelected = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    init {

        LayoutInflater.from(context).inflate(R.layout.item_bill_detail, this, true)

        mIconBackground = findViewById(R.id.bill_icon_background)
        mIcon = findViewById(R.id.bill_icon)
        mRemoveIcon = findViewById(R.id.delete_bill_icon)
        mTitle = findViewById(R.id.bill_name)
        mDate = findViewById(R.id.bill_date)
        mTime = findViewById(R.id.bill_time)
        mNumberTitle = findViewById(R.id.bill_number_title)
        mNumber = findViewById(R.id.bill_number)
        mPrice = findViewById(R.id.bill_price)
        mPriceSuffix = findViewById(R.id.bill_price_prefix)

        addSequences(R.xml.sequences_bill_detail_item)

        val resources = DIMain.getABResources()

        mSelectedBackground = resources.getDrawable(R.drawable.bill_item_view_detail_selected_background)
        mUnSelectedBackground = resources.getDrawable(R.drawable.bill_item_view_detail_unselected_background)

        background = mUnSelectedBackground

        mIcon.setImageDrawable(resources.getDrawable(R.drawable.ic_bill_item_icon_water))

        mIconBackground.setColor(Color.parseColor("#1A0297f1"))

        mRemoveIcon.setImageDrawable(resources.getDrawable(R.drawable.bill_item_remove_icon))

        setTextFeatures()

        val res = DIMain.getABResources()

        mCornerRadius = res.getDimenPixelSize(R.dimen.bill_item_view_corner_radius).toFloat()
//        mHoleRadius = res.getDimenPixelSize(R.dimen.bill_item_view_hole_radius).toFloat()

        mBackgroundPaint.alpha = 0
        mBackgroundPaint.isAntiAlias = true
        mBackgroundPaint.color = mUnSelectedBackgroundColor
        mBackgroundPaint.style = Paint.Style.FILL

        mBorderPaint.alpha = 0
        mBorderPaint.isAntiAlias = true
        mBorderPaint.color = mUnselectedBorderColor
        mBorderPaint.strokeWidth = res.getDimenPixelSize(R.dimen.bill_item_border_stroke).toFloat()
        mBorderPaint.strokeCap = Paint.Cap.ROUND
        mBorderPaint.style = Paint.Style.STROKE

        var padding = res.getDimenPixelSize(R.dimen.bill_item_delete_button_margin_left)
        mRemoveIcon.setPadding(padding, padding, padding, padding)
    }

//    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        super.onSizeChanged(w, h, oldw, oldh)
//
//        calculatePath(w, h)
//    }

//    private fun calculatePath(w: Int, h: Int) {
//
//        path.reset()
//
//        var rectF = RectF()
//
//        path.moveTo(0f, mCornerRadius)
//
//        if (mCornerRadius != 0f) {
//
//            rectF.set(0f, 0f, mCornerRadius * 2, mCornerRadius * 2)
//            path.arcTo(rectF, 180f, 90f)
//
//            path.lineTo(w - mCornerRadius, 0f)
//
//            rectF.set(w - 2 * mCornerRadius, 0f, w.toFloat(), mCornerRadius * 2)
//            path.arcTo(rectF, 270f, 90f)
//
//            path.lineTo(w.toFloat(), h - mCornerRadius)
//
//            rectF.set(w - 2 * mCornerRadius, h - 2 * mCornerRadius, w.toFloat(), h.toFloat())
//            path.arcTo(rectF, 0f, 90f)
//
//            path.lineTo(mCornerRadius, h.toFloat())
//
//            rectF.set(0f, h - 2 * mCornerRadius, 2 * mCornerRadius, h.toFloat())
//            path.arcTo(rectF, 90f, 90f)
//
//            path.close()
//
//        }
//    }

//    override fun dispatchDraw(canvas: Canvas?) {
//        val save = canvas!!.save()
//        canvas.clipPath(path)
//
//        canvas.drawPath(path, mBackgroundPaint)
//        super.dispatchDraw(canvas)
//        canvas.restoreToCount(save)
//
//
//        canvas.drawPath(path, mBorderPaint)
//    }

    private fun setTextFeatures() {

        val resources = DIMain.getABResources()

        val boldFont = ResourcesCompat.getFont(context, R.font.iran_yekan_bold)
        val mediumFont = ResourcesCompat.getFont(context, R.font.iran_yekan_medium)
        val regularFont = ResourcesCompat.getFont(context, R.font.iran_yekan)
        val lightFont = ResourcesCompat.getFont(context, R.font.iran_yekan_light)

        mTitle.typeface = boldFont
        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_item_name_text_size).toFloat())
        mTitle.setTextColor(resources.getColor(R.color.bill_item_name_color))
        setTitle("اسم قبض")

        mDate.typeface = lightFont
        mDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_item_date_text_size).toFloat())
        mDate.setTextColor(resources.getColor(R.color.bill_item_date_color))
//        setDate("13399/01/08")

        mTime.typeface = lightFont
        mTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_item_time_text_size).toFloat())
        mTime.setTextColor(resources.getColor(R.color.bill_item_time_color))
//        setTime("12:30")

        mNumberTitle.typeface = lightFont
        mNumberTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_item_number_title_text_size).toFloat())
        mNumberTitle.setTextColor(resources.getColor(R.color.bill_number_title_time_color))
        mNumberTitle.text = resources.getString(R.string.bill_item_number_title)

        mNumber.typeface = mediumFont
        mNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_item_number_text_size).toFloat())
        mNumber.setTextColor(resources.getColor(R.color.bill_number_time_color))
        setNumber("3013072304310")

        mPrice.typeface = boldFont
        mPrice.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_item_price_text_size).toFloat())
        mPrice.setTextColor(resources.getColor(R.color.bill_item_price_color))
        setPrice(100000L)

        mPriceSuffix.typeface = regularFont
        mPriceSuffix.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.bill_item_price_suffix_text_size).toFloat())
        mPriceSuffix.setTextColor(resources.getColor(R.color.bill_item_price_suffix_color))
        mPriceSuffix.text = resources.getString(R.string.bill_item_price_suffix)


    }

    fun setTitle(title: String) {
        mTitle.text = Utils.toPersianNumber(title)
    }

    fun setDate(date: String) {
        mDate.text = Utils.toPersianNumber(date)
    }

    fun setTime(time: String) {
        mTime.text = Utils.toPersianNumber(time)
    }

    fun setNumber(number: String) {
        mNumber.text = Utils.toPersianNumber(number)
    }

    fun setPrice(price: Long) {
        mPrice.text = Utils.toPersianPriceNumber(price)
    }

    fun setIcon(iconRes: Int) {
        if (iconRes != 0) {
            mIcon.setImageDrawable(resources.getDrawable(iconRes))
        }
    }

    fun setColor(color: Int) {
        if (color != 0) {
            mIconBackground.setColor(resources.getColor(color))
        }
    }

    fun selectItem(isSelected: Boolean) {

        if (mIsSelected == isSelected) {
            return
        }

        mIsSelected = isSelected

        if (isSelected) {

            background = mSelectedBackground
//            mBackgroundPaint.color = mSelectedBackgroundColor
//            mBorderPaint.color = mSelectedBorderColor
//            mBorderPaint.strokeWidth = mBorderPaint.strokeWidth * 2f
        } else {
            background = mUnSelectedBackground
//            mBackgroundPaint.color = mUnSelectedBackgroundColor
//            mBorderPaint.color = mUnselectedBorderColor
//            mBorderPaint.strokeWidth = mBorderPaint.strokeWidth / 2f
        }
        invalidate()
    }

    fun isItemSelected() = mIsSelected

    fun setDeleteClickListener(listener: OnClickListener) {
        mRemoveIcon.setOnClickListener(listener)
    }


    class BillIconBackground : View {

        private var mCornerRadius = 0f
        private var mBackgroundColor = Color.TRANSPARENT
        private val mBackgroundPaint = Paint()

        private var mClipPath = Path()

        constructor(context: Context?) : super(context)
        constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
        constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

        init {

            mCornerRadius = DIMain.getABResources().getDimenPixelSize(R.dimen.bill_item_icon_background_corner_radius).toFloat()

            mBackgroundPaint.alpha = 0
            mBackgroundPaint.isAntiAlias = true
            mBackgroundPaint.color = mBackgroundColor
            mBackgroundPaint.style = Paint.Style.FILL

        }

        override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
            super.onSizeChanged(w, h, oldw, oldh)

            val spacing = DIMain.getABResources().getDimenPixelSize(R.dimen.bill_item_icon_background_margin).toFloat()
            val arcBounds = RectF()


            mClipPath.reset()

            mClipPath.moveTo(0f, 0f)

            val angle: Float = (Math.asin(spacing.toDouble() / mCornerRadius.toDouble()) * 180 / Math.PI).toFloat()

            arcBounds.set(-1 * mCornerRadius, -1 * mCornerRadius - spacing, mCornerRadius, mCornerRadius - spacing)
            mClipPath.arcTo(arcBounds, 90f, 270f + angle)

            mClipPath.lineTo(w - mCornerRadius, 0f)

            arcBounds.set(w - 2 * mCornerRadius, 0f, w.toFloat(), 2 * mCornerRadius)
            mClipPath.arcTo(arcBounds, 270f, 90f)

            mClipPath.lineTo(w.toFloat(), h - mCornerRadius)

            arcBounds.set(w - 2 * mCornerRadius, h - 2 * mCornerRadius, w.toFloat(), h.toFloat())
            mClipPath.arcTo(arcBounds, 0f, 90f)

            mClipPath.lineTo(mCornerRadius, h.toFloat())

            arcBounds.set(-1 * mCornerRadius, h - mCornerRadius + spacing, mCornerRadius, h + mCornerRadius + spacing)
            mClipPath.arcTo(arcBounds, angle, (90f - angle))

            mClipPath.close()
        }


        override fun dispatchDraw(canvas: Canvas?) {
            val save = canvas!!.save()
            canvas.clipPath(mClipPath)

            canvas.drawPath(mClipPath, mBackgroundPaint)
            super.dispatchDraw(canvas)
            canvas.restoreToCount(save)

        }


        fun setColor(color: Int) {
            mBackgroundPaint.color = color

            invalidate()
        }

    }

}