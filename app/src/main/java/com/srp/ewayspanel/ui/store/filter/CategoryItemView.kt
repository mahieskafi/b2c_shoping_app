package com.srp.ewayspanel.ui.store.filter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.repository.storepage.category.CategoryTreeNode
import com.yashoid.sequencelayout.SequenceLayout

/**
 * Created by ErfanG on 04/11/2019.
 */
class CategoryItemView : SequenceLayout {

    companion object{
        const val ROOT_SELECTED = 0
        const val ROOT_UNSELECTED = 1
        const val ROOT_CHILD_SELECTED = 2
        const val SELECTED = 3
        const val UNSELECTED = 4
        const val CHILD_SELECTED = 5

        private const val TOP_MARGIN_HIGH : Float = (8).toFloat()
        private const val TOP_MARGIN_LOW : Float = (0).toFloat()
    }



    private val resources = DI.getABResources()

    private val mIndicatorIcon : ImageView
    private val mContentView : View
    private val mDropIcon : ImageView
    private val mItemName : TextView
    private val mDivider : View
    private val mBottomDivider : View
    private val mBackground : View
    private val mBackgroundHelper : View

    private val mIndicatorRootSelected : Drawable
    private val mIndicatorRootUnSelected : Drawable
    private val mIndicatorRootChildrenSelected : Drawable
    private val mIndicatorSelected : Drawable
    private val mIndicatorUnSelected : Drawable
    private val mIndicatorChildrenSelected : Drawable

    private val mSingleBackground : Drawable
    private val mTopBackground : Drawable
    private val mMiddleBackground : Drawable
    private val mBottomBackground : Drawable

    private val mOpenDropIcon : Drawable
    private val mCloseDropIcon : Drawable

    private var mSelectedTextColor : Int = 0
    private var mUnSelectedTextColor : Int = 0

    private var mChildren : ArrayList<CategoryTreeNode> = ArrayList()

    private var mIsRoot = false
    private var mIsLeaf = false

    private var mIsOpen = false


    constructor(context : Context) : super(context)
    constructor(context : Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context : Context, attributeSet: AttributeSet, defStyleAtr : Int) : super(context, attributeSet, defStyleAtr)

    init{
        LayoutInflater.from(context).inflate(R.layout.category_item_view, this, true)

        mIndicatorIcon = findViewById(R.id.indicator_icon)
        mContentView = findViewById(R.id.content_view)
        mDropIcon = findViewById(R.id.drop_icon)
        mItemName = findViewById(R.id.item_name)
        mDivider = findViewById(R.id.divider)
        mBottomDivider = findViewById(R.id.bottom_divider)
        mBackground = findViewById(R.id.background)
        mBackgroundHelper = findViewById(R.id.helper_background)

        addSequences(R.xml.sequences_category_item)

        mSingleBackground = resources.getDrawable(R.drawable.category_item_view_content_background)
        mTopBackground = resources.getDrawable(R.drawable.category_item_view_content_background_top)
        mMiddleBackground = resources.getDrawable(R.drawable.category_item_view_content_background_middle)
        mBottomBackground = resources.getDrawable(R.drawable.category_item_view_content_background_bottom)


        mSelectedTextColor = resources.getColor(R.color.category_item_view_selected_text)
        mUnSelectedTextColor = resources.getColor(R.color.category_item_view_unselected_text)

        mOpenDropIcon = resources.getDrawable(R.drawable.category_list_item_open_icon)
        mCloseDropIcon = resources.getDrawable(R.drawable.category_list_item_close_icon)

        mIndicatorRootSelected = resources.getDrawable(R.drawable.category_item_indicator_root_selected)
        mIndicatorRootUnSelected = resources.getDrawable(R.drawable.category_item_indicator_root_unselected)
        mIndicatorRootChildrenSelected = resources.getDrawable(R.drawable.category_item_indicator_root_children_selected)
        mIndicatorSelected = resources.getDrawable(R.drawable.category_item_indicator_selected)
        mIndicatorUnSelected = resources.getDrawable(R.drawable.category_item_indicator_unselected)
        mIndicatorChildrenSelected = resources.getDrawable(R.drawable.category_item_indicator_children_selected)

        mIndicatorIcon.setImageDrawable(mIndicatorChildrenSelected)
        mDropIcon.setImageDrawable(mOpenDropIcon)
        mItemName.setTextColor(mUnSelectedTextColor)
        mItemName.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.category_item_name_text_size).toFloat())
        mItemName.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)


        setHasElevation(true)

        mContentView.background = mSingleBackground
        mBackground.visibility = View.INVISIBLE
    }


    fun setData(data : CategoryTreeNode){
        mChildren = data.children as ArrayList<CategoryTreeNode>

        //Set Background depending on view_type
        val topMarginSpan = findSequenceById("vertical_seq").spans[0]
        when(data.boxType){
            CategoryTreeNode.BoxType.STANDARD ->{
                topMarginSpan.size = TOP_MARGIN_HIGH
                mBottomDivider.visibility = View.INVISIBLE
                mContentView.background = mSingleBackground
            }
            CategoryTreeNode.BoxType.TOP ->{
                topMarginSpan.size = TOP_MARGIN_HIGH
                mBottomDivider.visibility = View.VISIBLE
                mContentView.background = mTopBackground
            }
            CategoryTreeNode.BoxType.MIDDLE ->{
                topMarginSpan.size = TOP_MARGIN_LOW
                mBottomDivider.visibility = View.VISIBLE
                mContentView.background = mMiddleBackground
            }
            CategoryTreeNode.BoxType.BOTTOM ->{
                topMarginSpan.size = TOP_MARGIN_LOW
                mBottomDivider.visibility = View.INVISIBLE
                mContentView.background = mBottomBackground
            }
        }

        //show dropIcon and divider depend on the node is leaf or not
        mIsLeaf = mChildren.size == 0
        if(mIsLeaf){
            mDropIcon.visibility = View.INVISIBLE
            mDivider.visibility = View.INVISIBLE
        }
        else{
            mDropIcon.visibility = View.VISIBLE
            mDivider.visibility = View.VISIBLE
        }


        mIsRoot = data.depth == 0
        if(mIsRoot){
            mIndicatorIcon.setImageDrawable(mIndicatorRootUnSelected)
            if(data.isExpanded){
                mBackgroundHelper.visibility = View.VISIBLE
            }
            else{
                mBackgroundHelper.visibility = View.INVISIBLE
            }
            mBackground.visibility = View.INVISIBLE
            setHasElevation(true)
        }
        else{
            mIndicatorIcon.setImageDrawable(mIndicatorChildrenSelected)
            mBackground.visibility = View.VISIBLE
            setHasElevation(false)
        }

        if(data.isExpanded){
            mDropIcon.setImageDrawable(mCloseDropIcon)
        }
        else{
            mDropIcon.setImageDrawable(mOpenDropIcon)
        }


        //right margin of content from indicator
        val helperWidth = ((data.depth) * 24).toFloat()
        if(helperWidth >= 0) {
            findSequenceById("horizontal_seq").spans[2].size = helperWidth
        }
        else{
            findSequenceById("horizontal_seq").spans[2].size = (0).toFloat()
        }


        //left margin of content
        val helper2Width: Float = if(data.depth > 0){
            (8).toFloat()
        }
        else{
            (0).toFloat()
        }
        findSequenceById("horizontal_seq").spans[0].size = helper2Width

        setCategoryName(data.title)

        //indicator icon
        if(data.depth > 0){
            when(data.selectionState){
                CategoryTreeNode.SelectionState.IN_SELECTIONPATH ->{
                    setIndicator(CHILD_SELECTED)
                }
                CategoryTreeNode.SelectionState.SELECTED ->{
                    setIndicator(SELECTED)
                }
                CategoryTreeNode.SelectionState.UNSELECTED ->{
                    setIndicator(UNSELECTED)
                }
            }
        }
        else{
            when(data.selectionState){
                CategoryTreeNode.SelectionState.IN_SELECTIONPATH ->{
                    setIndicator(ROOT_CHILD_SELECTED)
                }
                CategoryTreeNode.SelectionState.SELECTED ->{
                    setIndicator(ROOT_SELECTED)
                }
                CategoryTreeNode.SelectionState.UNSELECTED ->{
                    setIndicator(ROOT_UNSELECTED)
                }
            }
        }

        requestLayout()
    }

    fun setCategoryName(name : String){
        mItemName.text = name
    }

    fun setDropDownClickListener(clickListener: OnClickListener) {
        mDropIcon.setOnClickListener(clickListener)
    }

    fun setSelectedClickListener(clickListener: OnClickListener){
        mItemName.setOnClickListener(clickListener)
    }

    fun setIndicator(mode : Int){

        when(mode){
            ROOT_SELECTED ->{
                mIndicatorIcon.setImageDrawable(mIndicatorRootSelected)
                mItemName.setTextColor(mSelectedTextColor)
            }
            ROOT_UNSELECTED ->{
                mIndicatorIcon.setImageDrawable(mIndicatorRootUnSelected)
                mItemName.setTextColor(mUnSelectedTextColor)
            }
            ROOT_CHILD_SELECTED ->{
                mIndicatorIcon.setImageDrawable(mIndicatorRootChildrenSelected)
                mItemName.setTextColor(mSelectedTextColor)
            }
            SELECTED ->{
                mIndicatorIcon.setImageDrawable(mIndicatorSelected)
                mItemName.setTextColor(mSelectedTextColor)
            }
            UNSELECTED ->{
                mIndicatorIcon.setImageDrawable(mIndicatorUnSelected)
                mItemName.setTextColor(mUnSelectedTextColor)
            }
            CHILD_SELECTED ->{
                mIndicatorIcon.setImageDrawable(mIndicatorChildrenSelected)
                mItemName.setTextColor(mSelectedTextColor)
            }
        }
    }

    fun setHasElevation(hasElevation : Boolean){

        if(hasElevation) {
            val elevationSize = resources.getDimenPixelSize(R.dimen.category_item_view_elevation).toFloat()
            ViewCompat.setElevation(mContentView, elevationSize)
            ViewCompat.setElevation(mDropIcon, elevationSize)
            ViewCompat.setElevation(mDivider, elevationSize)
            ViewCompat.setElevation(mItemName, elevationSize)
            ViewCompat.setElevation(mBottomDivider, elevationSize)
        }
        else{
            ViewCompat.setElevation(mContentView, (0).toFloat())
            ViewCompat.setElevation(mDropIcon, (0).toFloat())
            ViewCompat.setElevation(mDivider, (0).toFloat())
            ViewCompat.setElevation(mItemName, (0).toFloat())
            ViewCompat.setElevation(mBottomDivider, (0).toFloat())
        }
    }
}