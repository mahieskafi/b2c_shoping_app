package com.srp.eways.ui.bill.inquiry.views

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.R
import com.srp.eways.di.DIMain
import com.srp.eways.ui.bill.inquiry.BillType
import java.lang.Exception
import java.security.InvalidParameterException

/**
 * Created by ErfanG on 4/22/2020.
 */
class SelectBillTypeView : ViewGroup{


    interface OnBillItemClickListener{

        fun onItemClicked(type : BillType)
    }

    private val mUnselectedTypeFace : Typeface
    private val mSelectedTypeFace : Typeface
    private val mUnselectedFontSize : Float
    private val mSelectedFontSize : Float
    private val mUnselectedTextColor : Int
    private val mSelectedTextColor : Int

    private var mBillTypeIconViews = ArrayList<ImageView>()
    private var mBillTypeNameViews = ArrayList<TextView>()
    private var mBillTypes = ArrayList<BillType>()

    private var mSelectedIndex = -1

    private var mBillIcons = ArrayList<Drawable>()
    private var mBillNames = ArrayList<String>()

    private var mBigIconPadding = 0
    private var mSmallIconPadding = 0

    private var mBigIconTopPadding = 0
    private var mSmallIconTopPadding = 0


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {

        val resources = DIMain.getABResources()

        clipToPadding = false
        clipChildren = false

        background = resources.getDrawable(R.drawable.bill_inquiry_select_type_background)

        mUnselectedTypeFace = ResourcesCompat.getFont(context, R.font.iran_yekan)!!
        mSelectedTypeFace = ResourcesCompat.getFont(context, R.font.iran_yekan_bold)!!

        mUnselectedFontSize = resources.getDimenPixelSize(R.dimen.bill_inquiry_select_type_name_size_unselected).toFloat()
        mSelectedFontSize = resources.getDimenPixelSize(R.dimen.bill_inquiry_select_type_name_size_selected).toFloat()

        mUnselectedTextColor = resources.getColor(R.color.bill_inquiry_select_type_name_color_unselected)
        mSelectedTextColor = resources.getColor(R.color.bill_inquiry_select_type_name_color_selected)

    }


    fun setBillTypes(billTypes : ArrayList<BillType>){

        if(billTypes.size < 1){
            throw InvalidParameterException("Icon and name lists size should be more than 0")
        }

        mBillIcons.clear()
        mBillNames.clear()

        for(item in billTypes){
            mBillIcons.add(getBillIcon(item))
            mBillNames.add(getBillName(item))
        }

        if(mBillTypeIconViews.size < mBillIcons.size){

            do {
                mBillTypeIconViews.add(makeBillIconView())
            }
            while (mBillTypeIconViews.size < mBillIcons.size)
        }
        else if(mBillTypeIconViews.size > mBillIcons.size){

            do {
                mBillTypeIconViews.removeAt(0)
            }
            while (mBillTypeIconViews.size > mBillIcons.size)
        }

        if(mBillTypeNameViews.size < mBillNames.size){

            do {
                mBillTypeNameViews.add(makeBillNameTextView())
            }
            while (mBillTypeNameViews.size < mBillNames.size)
        }
        else if(mBillTypeNameViews.size > mBillNames.size){

            do {
                mBillTypeNameViews.removeAt(0)
            }
            while (mBillTypeNameViews.size > mBillNames.size)
        }

        removeAllViews()

        mBillTypeIconViews.forEachIndexed { index, view ->
            
            view.setImageDrawable(mBillIcons[index])
            addView(view)
        }

        mBillTypeNameViews.forEachIndexed { index, view ->

            view.text = mBillNames[index]
            addView(view)
        }

        mBillTypes = billTypes

        requestLayout()
    }


    fun selectBillType(billType: BillType){

        for(i in 0 until mBillTypes.size){
            if(mBillTypes[i] == billType){

                mBillTypeIconViews[i].performClick()

                return
            }
        }

    }

    fun setBillClickListener(listener : OnBillItemClickListener){

        for(j in 0 until mBillTypeIconViews.size){

            val clickListener = OnClickListener {

                for(i in 0 until mBillTypeIconViews.size) {

                    if(j != mSelectedIndex) {

                        internalUnSelectAll()

                        if (i != j) {
                            with(mBillTypeIconViews[i]) {
                                setPadding(mSmallIconPadding, mSmallIconTopPadding, mSmallIconPadding, this.paddingBottom)
                                alpha = 0.7f
                            }

                            with(mBillTypeNameViews[i]) {
                                typeface = mUnselectedTypeFace
                                setTextSize(TypedValue.COMPLEX_UNIT_PX, mUnselectedFontSize)
                                setTextColor(mUnselectedTextColor)
                                alpha = 0.7f
                            }
                        }
                        else {
                            mSelectedIndex = j
                            with(mBillTypeIconViews[i]) {
                                setPadding(mBigIconPadding, mBigIconTopPadding, mBigIconPadding, this.paddingBottom)
                                alpha = 1.0f
                            }

                            with(mBillTypeNameViews[i]) {
                                typeface = mSelectedTypeFace
                                setTextSize(TypedValue.COMPLEX_UNIT_PX, mSelectedFontSize)
                                setTextColor(mSelectedTextColor)
                                alpha = 1.0f
                            }
                            listener.onItemClicked(mBillTypes[j])
                        }
                    }
                    else{
                        //selected before
                    }
                }
            }

            mBillTypeNameViews[j].setOnClickListener(clickListener)
            mBillTypeIconViews[j].setOnClickListener(clickListener)

        }
    }

    private fun getBillName(billType: BillType): String {
        val resources = DIMain.getABResources()

        when(billType){
            BillType.ELECTRICITY -> return resources.getString(R.string.bill_inquiry_electricity_name)
            BillType.MOBILE -> return resources.getString(R.string.bill_inquiry_mobile_name)
            BillType.PHONE -> return resources.getString(R.string.bill_inquiry_phone_name)
            BillType.GAS -> return resources.getString(R.string.bill_inquiry_gas_name)
            BillType.WATER -> return resources.getString(R.string.bill_inquiry_water_name)
        }
    }

    private fun getBillIcon(billType: BillType): Drawable {
        val resources = DIMain.getABResources()

        when(billType){
            BillType.ELECTRICITY -> return resources.getDrawable(R.drawable.ic_bill_item_icon_electricity)
            BillType.MOBILE -> return resources.getDrawable(R.drawable.ic_bill_item_icon_mobile)
            BillType.PHONE -> return resources.getDrawable(R.drawable.ic_bill_item_icon_phone)
            BillType.GAS -> return resources.getDrawable(R.drawable.ic_bill_item_icon_gas)
            BillType.WATER -> return resources.getDrawable(R.drawable.ic_bill_item_icon_water)
        }
    }

    private fun makeBillNameTextView() : TextView{

        val textView = TextView(context)

        textView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        textView.typeface = mUnselectedTypeFace
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mUnselectedFontSize)
        textView.setTextColor(mUnselectedTextColor)
        textView.gravity = Gravity.CENTER
        textView.textAlignment = View.TEXT_ALIGNMENT_GRAVITY

        return textView
    }

    private fun makeBillIconView() : ImageView{

        val iconView = ImageView(context)

        iconView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        iconView.alpha = 0.74f


        return iconView
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {


        val widthWSize  : Int = ((r - l).toFloat() / mBillTypeIconViews.size.toFloat()).toInt()

        val widthHeightSize = DIMain.getABResources().getDimenPixelSize(R.dimen.bill_inquiry_select_type_icon_width_height)

        mBigIconPadding = (widthWSize - widthHeightSize) / 2
        mSmallIconPadding = ((widthWSize - widthHeightSize) / 2) + (widthHeightSize * 0.15).toInt()

        if(mSelectedIndex == -1) {
            mBillTypeIconViews.forEach { view ->
                view.setPadding(mSmallIconPadding, mSmallIconTopPadding, mSmallIconPadding, 0)

            }
        }

        mBillTypeNameViews.forEach { view ->
            view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))

        }



        if(b - t < mBillTypeIconViews[0].measuredHeight + mBillTypeNameViews[0].measuredHeight){
            throw Exception("height size should be more higher")
        }

        if(r - l < mBillTypeIconViews[0].measuredWidth * mBillTypeIconViews.size){
            throw Exception("width should be more wider")
        }

        val hSpaceing = ((r - l) - (mBillTypeIconViews.size * mBillTypeIconViews[0].measuredWidth)) / (2 * mBillTypeIconViews.size)
        val vSpaceing = ((b - t) - (widthHeightSize + mBillTypeNameViews[0].measuredHeight)) / 2

        if(mBigIconTopPadding == 0) {
            mBigIconTopPadding = vSpaceing
            mSmallIconTopPadding = vSpaceing + (widthHeightSize * 0.3).toInt()
        }

        mBillTypeIconViews.forEachIndexed { index, imageView ->

//            imageView.layout(
//                    hSpaceing + index * (imageView.measuredWidth + (2 * hSpaceing)),
//                    vSpaceing ,
//                    hSpaceing + imageView.measuredWidth + index * (imageView.measuredWidth + (2 * hSpaceing)),
//                    vSpaceing + imageView.measuredHeight)

            imageView.layout(
                    index * (widthWSize),
                    0 ,
                    (index + 1) * (widthWSize),
                    vSpaceing + widthHeightSize)
//            if(mSelectedIndex == -1){
//                imageView.setPadding(mSmallIconPadding, vSpaceing, mSmallIconPadding, 0)
//            }
        }

        mBillTypeNameViews.forEachIndexed { index, nameView ->

            val margin = (mBillTypeIconViews[index].width - nameView.measuredWidth) / 2
            nameView.layout(
                    mBillTypeIconViews[index].left + margin,
                    mBillTypeIconViews[index].bottom ,
                    mBillTypeIconViews[index].right - margin,
                    mBillTypeIconViews[index].bottom + nameView.measuredHeight)

            nameView.setPadding(margin, 0, margin, 0)
        }

//        val widthWSize  : Int = ((r - l).toFloat() / mBillTypeIconViews.size.toFloat()).toInt();
//         mBillTypeIconViews.forEachIndexed { index, imageView ->
//
//            imageView.layout(
//                    index * (widthWSize),
//                    0 ,
//                    (index + 1) * (widthWSize),
//                    vSpaceing + imageView.measuredHeight)
//
////             val sidePadding = ((widthWSize - widthHeightSize) / 2f).toInt()
////             imageView.setPadding(sidePadding, 0, sidePadding, 0)
//        }
//
//        mBillTypeNameViews.forEachIndexed { index, nameView ->
//
//            val paddingSide = (widthWSize - nameView.measuredWidth) / 2
//            nameView.layout(
//                    index * (widthWSize),
//                    mBillTypeIconViews[index].bottom ,
//                    (index + 1) * (widthWSize),
//                    mBillTypeIconViews[index].bottom + nameView.measuredHeight)
//
//            nameView.setPadding(paddingSide, 0, paddingSide, 0)
//        }

    }

    override fun setClickable(clickable: Boolean) {

        mBillTypeIconViews.forEach { icon ->
            icon.isClickable = clickable
        }
        mBillTypeNameViews.forEach { name ->
            name.isClickable = clickable
        }

        super.setClickable(clickable)
    }

    fun unSelectAll() {

        mSelectedIndex = -1

        for(i in 0 until mBillTypeIconViews.size){

            with(mBillTypeIconViews[i]) {
//                scaleX = 0.7f
//                scaleY = 0.7f
                setPadding(mSmallIconPadding, mSmallIconTopPadding, mSmallIconPadding, this.paddingBottom)
                alpha = 1f
            }

            with(mBillTypeNameViews[i]) {
                typeface = mUnselectedTypeFace
                setTextSize(TypedValue.COMPLEX_UNIT_PX, mUnselectedFontSize)
                setTextColor(mUnselectedTextColor)
                alpha = 1f
            }

        }

    }

    private fun internalUnSelectAll() {

        mSelectedIndex = -1

        for(i in 0 until mBillTypeIconViews.size){

            with(mBillTypeIconViews[i]) {
                setPadding(mSmallIconPadding, mSmallIconTopPadding, mSmallIconPadding, this.paddingBottom)
                alpha = 0.74f
            }

            with(mBillTypeNameViews[i]) {
                typeface = mUnselectedTypeFace
                setTextSize(TypedValue.COMPLEX_UNIT_PX, mUnselectedFontSize)
                setTextColor(mUnselectedTextColor)
                alpha = 1f
            }

        }

    }
}