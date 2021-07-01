package com.srp.ewayspanel.ui.store.mainpage

import android.content.Context
import android.graphics.Color
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.storepage.mainpage.Data
import com.yashoid.sequencelayout.SequenceLayout

/**
 * Created by ErfanG on 2/21/2020.
 */
class MainPageCategoryItemView : SequenceLayout {

    private lateinit var mProductListener: MainPageCategoryItemListAdapter.StoreMainPageListener

    private val mList: RecyclerView
    private val mTitle: TextView
    private val mMoreText: TextView
    private val mMoreIcon: ImageView

    private val mMoreHelper: View

    private lateinit var mItemsAdapter: MainPageCategoryItemListAdapter

    private var mFocusablePosition = -2

    private var mLoyaltyScore: Long = 0

    constructor(context: Context) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, productListener: MainPageCategoryItemListAdapter.StoreMainPageListener) : super(context) {
        mProductListener = productListener
    }

    init {

        LayoutInflater.from(context).inflate(R.layout.store_main_page_category_item, this, true)

        clipChildren = false
        clipToPadding = false

        mList = findViewById(R.id.items)
        mTitle = findViewById(R.id.title)
        mMoreText = findViewById(R.id.more_text)
        mMoreIcon = findViewById(R.id.more_icon)
        mMoreHelper = findViewById(R.id.more_helper)

        addSequences(R.xml.sequences_store_main_page_item)


        val resources = DI.getABResources()

        setBackgroundColor(Color.TRANSPARENT)

        mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.store_main_page_category_title_size).toFloat())
        mTitle.setTextColor(resources.getColor(R.color.store_main_page_category_title_color))
        mTitle.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_medium)

        mMoreText.text = resources.getString(R.string.store_main_page_category_more_text)
        mMoreText.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.store_main_page_category_more_text_size).toFloat())
        mMoreText.setTextColor(resources.getColor(R.color.store_main_page_category_more_text_color))
        mMoreText.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan)

        mMoreIcon.setImageDrawable(resources.getDrawable(R.drawable.store_main_page_category_more_icon))


        val lm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
        lm.reverseLayout = true
        mList.layoutManager = lm

    }

    fun setlistener(productListener: MainPageCategoryItemListAdapter.StoreMainPageListener) {
        mProductListener = productListener
    }

    fun setLoyaltyScore(loyaltyScore: Long) {
        mLoyaltyScore = loyaltyScore
    }

    fun setModel(data: Data) {

        mItemsAdapter = MainPageCategoryItemListAdapter(context, data.items, mProductListener)

        mItemsAdapter.setLoyaltyScore(mLoyaltyScore)
        mItemsAdapter.setHasStableIds(true)
        mList.adapter = mItemsAdapter

        mList.itemAnimator = null
        mTitle.text = data.title

    }

    fun setOnLoadMoreAction(action: OnClickListener) {

        mMoreHelper.setOnClickListener(action)
    }

    fun setListViewPool(viewPool: RecyclerView.RecycledViewPool) {
        mList.setRecycledViewPool(viewPool)
    }

    fun getAdapter() = mItemsAdapter

    fun getLayoutManager() = mList.layoutManager
}