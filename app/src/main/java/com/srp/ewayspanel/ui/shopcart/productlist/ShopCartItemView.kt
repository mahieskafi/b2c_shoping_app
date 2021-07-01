package com.srp.ewayspanel.ui.shopcart.productlist

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.solver.GoalRow
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.shopcart.ShopCartItemModel
import com.srp.eways.util.Utils
import com.srp.ewayspanel.model.shopcart.ProductInfo
import com.srp.ewayspanel.ui.view.CounterView2
import com.yashoid.sequencelayout.SequenceLayout
import java.net.MalformedURLException
import java.net.URL
import kotlin.math.max
import kotlin.math.min

/**
 * Created by ErfanG on 11/25/2019.
 */
class ShopCartItemView : SequenceLayout {


    interface OnAddRemoveListener {

        fun countChanged(newCount: Int)
    }

    private var mRemoveButton: AppCompatImageView
    private var mRowContainer: View
    private var mRowStockContainer: View
    private var mStockDivider: View

    private val mProductImage: ImageView
    private val mProductName: AppCompatTextView

    private val mUnitPriceBackground: View
    private val mUnitPriceText: TextView
    private val mUnitPrice: TextView
    private val mUnitPriceSuffix: TextView

    private val mTotalPriceBackground: View
    private val mTotalPriceText: TextView
    private val mTotalPrice: TextView
    private val mTotalPriceSuffix: TextView

    private val mLackText: TextView
    private val mStockText: TextView
    private val mCounterView: CounterView2
    private lateinit var mProduct: ShopCartItemModel

    private var mProductCount = 0

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAtr: Int) : super(context, attributeSet, defStyleAtr)

    init {

        val resources = DI.getABResources()

        LayoutInflater.from(context).inflate(R.layout.shop_cart_item, this, true)

        mRowContainer = findViewById(R.id.row_background)
        mRowStockContainer = findViewById(R.id.row_stock_background)

        mProductImage = findViewById(R.id.product_image)
        mRemoveButton = findViewById(R.id.remove_product_icon)
        mProductName = findViewById(R.id.product_name)
        mCounterView = findViewById(R.id.counterview)
        mCounterView.setHorizantalyView(true)

        mLackText = findViewById(R.id.lack_text)
        mStockText = findViewById(R.id.stock_text_error)
        mStockDivider = findViewById(R.id.stock_divider)

        mUnitPriceBackground = findViewById(R.id.unit_price_background)
        mUnitPrice = findViewById(R.id.unit_price)
        mUnitPriceText = findViewById(R.id.unit_price_text)
        mUnitPriceSuffix = findViewById(R.id.unit_price_suffix)

        mTotalPriceBackground = findViewById(R.id.total_price_background)
        mTotalPriceText = findViewById(R.id.total_price_text)
        mTotalPrice = findViewById(R.id.total_price)
        mTotalPriceSuffix = findViewById(R.id.total_price_suffix)

        mCounterView.background = resources.getDrawable(R.drawable.horizontal_counter_view_background)
        mRowContainer.background = resources.getDrawable(R.drawable.shop_item_background_horizontal)
        mRowStockContainer.background = resources.getDrawable(R.drawable.shop_cart_item_lack_background)


        addSequences(R.xml.sequences_shop_cart_item)

        ViewCompat.setElevation(this, resources.getDimenPixelSize(R.dimen.shop_cart_view_elevation).toFloat())

        val textFont = ResourcesCompat.getFont(context, R.font.iran_yekan)
        val numberFont = ResourcesCompat.getFont(context, R.font.iran_sans_regular)

        Glide.with(context)
                .load(URL("https://staticcontent.eways.co//upload/ProductPictures/71462.jpg"))
                .fitCenter()
                .into(mProductImage)


        mRemoveButton.setImageDrawable(resources.getDrawable(R.drawable.shop_item_delete_icon_enable))


        mProductName.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_item_name_text_size).toFloat())
        mProductName.setTextColor(resources.getColor(R.color.shop_cart_item_text_color))
        mProductName.setLineSpacing(0f, 1.4f)
        mProductName.typeface = textFont

        mLackText.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_item_lack_text_size).toFloat())
        mLackText.setTextColor(resources.getColor(R.color.shop_cart_item_lack_text_color))
        mLackText.typeface = textFont
        mLackText.text = resources.getString(R.string.shop_cart_item_lack_text)
        mLackText.visibility = View.INVISIBLE

        mUnitPriceText.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_item_unit_price_text_size).toFloat())
        mUnitPriceText.setTextColor(resources.getColor(R.color.shop_cart_item_text_color))
        mUnitPriceText.typeface = textFont
        mUnitPriceText.text = resources.getString(R.string.shop_cart_item_unit_price_text)

        mStockText.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_item_unit_price_text_size).toFloat())
        mStockText.setTextColor(resources.getColor(R.color.shop_cart_item_lack_text_color))
        mStockText.typeface = textFont
        mStockText.text = resources.getString(R.string.shop_cart_item_stock_error_text)
        mStockText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.product_stock_icon_error, 0);


        mUnitPriceSuffix.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_item_unit_price_suffix_size).toFloat())
        mUnitPriceSuffix.setTextColor(resources.getColor(R.color.shop_cart_item_text_color))
        mUnitPriceSuffix.typeface = textFont
        mUnitPriceSuffix.text = resources.getString(R.string.shop_cart_item_unit_price_suffix_text)

        mUnitPrice.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_item_unit_price_size).toFloat())
        mUnitPrice.setTextColor(resources.getColor(R.color.shop_cart_item_text_color))
        mUnitPrice.typeface = numberFont
        setUnitPrice(mUnitPrice.text.toString())


        mTotalPriceText.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_item_total_price_text_size).toFloat())
        mTotalPriceText.setTextColor(resources.getColor(R.color.shop_cart_item_text_color))
        mTotalPriceText.typeface = textFont
        mTotalPriceText.text = resources.getString(R.string.shop_cart_item_total_price_text)

        mTotalPriceSuffix.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_item_total_price_suffix_size).toFloat())
        mTotalPriceSuffix.setTextColor(resources.getColor(R.color.shop_cart_item_text_color))
        mTotalPriceSuffix.typeface = textFont
        mTotalPriceSuffix.text = resources.getString(R.string.shop_cart_item_total_price_suffix_text)

        mTotalPrice.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_cart_item_total_price_size).toFloat())
        mTotalPrice.setTextColor(resources.getColor(R.color.shop_cart_item_text_color))
        mTotalPrice.typeface = numberFont
//        setTotalPrice(mTotalPrice.text.toString())

    }


    private fun setTotalPrice(totalPrice: String) {
        mTotalPrice.text = Utils.toPersianPriceNumber(totalPrice)

    }

    private fun setUnitPrice(price: String) {

        mUnitPrice.text = Utils.toPersianPriceNumber(price)

    }

    fun bindData(model: ShopCartItemModel) {

        mProduct = model

        val productInfo = model.productInfo

        try {
            Glide.with(context).load(URL(productInfo!!.imageUrl)).fitCenter().into(mProductImage)
        } catch (exception: MalformedURLException) {
        }

        mProductName.text = Utils.toPersianNumber(productInfo!!.name)

        mProductCount = model.count
        setCount(model.count, false)
        mCounterView.setMaxCount(min(model.productInfo!!.maxOrder, model.productInfo!!.stock))
        mCounterView.setMinCount(min(model.productInfo!!.minOrder, model.productInfo!!.stock))

        setIsLack(model.productInfo!!)

        updatePrices()

    }

    private fun updatePrices() {

        setUnitPrice(mProduct.productInfo!!.price.toString())
        setTotalPrice((mProductCount * mProduct.productInfo!!.price).toString())

    }

    private fun setCount(count: Int, notify: Boolean = true) {

        mCounterView.setCount(count, notify)
    }

    fun setAddRemoveListener(listener: OnAddRemoveListener?) {

        mCounterView.setCountChangeListener(object : CounterView2.Counter2ChangeListener {
            override fun onCountAdded(newCount: Int, isTotalCount: Boolean) {
                listener?.countChanged(newCount)
            }

            override fun onCountRemoved(newCount: Int, isTotalCount: Boolean) {
                listener?.countChanged(newCount)
            }

            override fun onCountNotChanged(count: Int) {
            }

        })
    }

    fun setRemoveAction(listener: OnClickListener) {

        mRemoveButton.setOnClickListener(listener)
    }

    fun setOpenProduct(listener: OnClickListener) {
        mRowContainer.setOnClickListener(listener)
    }

    fun getCount(): Int {
        return mProductCount
    }

    fun setIsLack(productInfo: ProductInfo) {

        if (!productInfo.availability) {
            mCounterView.counterViewDisable(true)

            mLackText.visibility = View.VISIBLE
            mUnitPriceBackground.visibility = View.GONE
            mUnitPrice.visibility = View.GONE
            mUnitPriceText.visibility = View.GONE
            mUnitPriceSuffix.visibility = View.GONE

            mTotalPriceBackground.visibility = View.GONE
            mTotalPriceText.visibility = View.GONE
            mTotalPrice.visibility = View.GONE
            mTotalPriceSuffix.visibility = View.GONE

            mRowStockContainer.visibility = GONE
            mStockText.visibility = GONE
            mStockDivider.visibility = GONE

            mRowContainer.background = resources.getDrawable(R.drawable.shop_cart_item_lack_background)
        } else {
            mCounterView.visibility = View.VISIBLE

            mLackText.visibility = View.INVISIBLE
            mUnitPriceBackground.visibility = View.VISIBLE
            mUnitPrice.visibility = View.VISIBLE
            mUnitPriceText.visibility = View.VISIBLE
            mUnitPriceSuffix.visibility = View.VISIBLE

            mTotalPriceBackground.visibility = View.VISIBLE
            mTotalPriceText.visibility = View.VISIBLE
            mTotalPrice.visibility = View.VISIBLE
            mTotalPriceSuffix.visibility = View.VISIBLE

            setCount(mProduct.count, false)
            mRowContainer.background = resources.getDrawable(R.drawable.shop_cart_item_background)

            if (min(productInfo.stock, productInfo.maxOrder) < mProduct.count) {

                mRowStockContainer.visibility = VISIBLE
                mStockText.visibility = VISIBLE
                mStockDivider.visibility = VISIBLE
                mRowContainer.visibility = INVISIBLE
                mRowContainer.visibility = INVISIBLE

                mRowStockContainer.background = resources.getDrawable(R.drawable.shop_cart_item_lack_background)
            } else {

                mRowStockContainer.visibility = GONE
                mStockText.visibility = GONE
                mStockDivider.visibility = GONE
                mRowContainer.visibility = VISIBLE
                mRowContainer.visibility = VISIBLE

                mRowStockContainer.background = resources.getDrawable(R.drawable.shop_cart_item_background)
            }
        }
    }

}