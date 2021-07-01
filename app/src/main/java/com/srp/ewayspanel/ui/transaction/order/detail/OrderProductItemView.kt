package com.srp.ewayspanel.ui.transaction.order.detail

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.transaction.order.OrderDetail
import com.srp.eways.util.Utils
import com.yashoid.sequencelayout.SequenceLayout
import java.net.URL

/**
 * Created by Eskafi on 2/8/2020.
 */

class OrderProductItemView : SequenceLayout {

    private val mProductImage: ImageView
    private val mProductName: TextView

    private val mUnitPriceBackground: View
    private val mUnitPriceText: TextView
    private val mUnitPrice: TextView
    private val mUnitPriceSuffix: TextView
    private val mTotalPriceBackground: View
    private val mTotalPriceText: TextView
    private val mTotalPrice: TextView
    private val mTotalPriceSuffix: TextView


    private val mCountText: TextView
    private val mCount: TextView
    private val mCountBackground: View


    private val mProductIdText: TextView
    private val mProductIdBackground: View
    private val mProductId: TextView

    private lateinit var mProduct: OrderDetail

    private var mProductCount = 0

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAtr: Int) : super(context, attributeSet, defStyleAtr)

    init {

        val resources = DI.getABResources()

        LayoutInflater.from(context).inflate(R.layout.order_product_item, this, true)

        mProductImage = findViewById(R.id.product_image)
        mProductName = findViewById(R.id.product_name)
        mCountText = findViewById(R.id.count_text)
        mCount = findViewById(R.id.count)
        mCountBackground = findViewById(R.id.count_background)

        mUnitPriceBackground = findViewById(R.id.unit_price_background)
        mUnitPrice = findViewById(R.id.unit_price)
        mUnitPriceText = findViewById(R.id.unit_price_text)
        mUnitPriceSuffix = findViewById(R.id.unit_price_suffix)

        mTotalPriceBackground = findViewById(R.id.total_price_background)
        mTotalPriceText = findViewById(R.id.total_price_text)
        mTotalPrice = findViewById(R.id.total_price)
        mTotalPriceSuffix = findViewById(R.id.total_price_suffix)
        mProductIdBackground = findViewById(R.id.order_id_background)
        mProductId = findViewById(R.id.order_id)
        mProductIdText = findViewById(R.id.order_id_text)

        addSequences(R.xml.sequences_order_product_item)

        ViewCompat.setElevation(this, resources.getDimenPixelSize(R.dimen.shop_cart_view_elevation).toFloat())

        background = resources.getDrawable(R.drawable.shop_cart_item_background)

        val textFont = ResourcesCompat.getFont(context, R.font.iran_yekan)
        val numberFont = ResourcesCompat.getFont(context, R.font.iran_sans_regular)

        Glide.with(context)
                .load(URL("https://staticcontent.eways.co//upload/ProductPictures/71462.jpg"))
                .fitCenter()
                .into(mProductImage)


        val cc = CheckBox(context)
        cc.isSelected

        mProductName.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.order_transaction_product_item_name_text_size).toFloat())
        mProductName.setTextColor(resources.getColor(R.color.order_transaction_product_item_text_color))
        mProductName.typeface = textFont

        mProductIdText.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.order_transaction_product_item_price_items_title_text_size).toFloat())
        mProductIdText.setTextColor(resources.getColor(R.color.order_transaction_product_item_text_color))
        mProductIdText.typeface = textFont
        mProductIdText.text = resources.getString(R.string.order_transaction_product_item_product_id_text)

        mProductId.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.order_transaction_product_item_price_items_value_text_size).toFloat())
        mProductId.setTextColor(resources.getColor(R.color.order_transaction_product_item_text_color))
        mProductId.typeface = textFont

        mCountText.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.order_transaction_product_item_price_items_title_text_size).toFloat())
        mCountText.setTextColor(resources.getColor(R.color.order_transaction_product_item_text_color))
        mCountText.typeface = textFont
        mCountText.text = resources.getString(R.string.order_transaction_product_item_count_text)

        mCount.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.order_transaction_product_item_price_items_value_text_size).toFloat())
        mCount.setTextColor(resources.getColor(R.color.order_transaction_product_item_text_color))
        mCount.typeface = textFont

        mUnitPriceText.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.order_transaction_product_item_price_items_title_text_size).toFloat())
        mUnitPriceText.setTextColor(resources.getColor(R.color.order_transaction_product_item_text_color))
        mUnitPriceText.typeface = textFont
        mUnitPriceText.text = resources.getString(R.string.order_transaction_product_item_unit_price_text)

        mUnitPriceSuffix.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.order_transaction_product_item_price_items_suffix_text_size).toFloat())
        mUnitPriceSuffix.setTextColor(resources.getColor(R.color.order_transaction_product_item_text_color))
        mUnitPriceSuffix.typeface = textFont
        mUnitPriceSuffix.text = resources.getString(R.string.rial)

        mUnitPrice.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.order_transaction_product_item_price_items_value_text_size).toFloat())
        mUnitPrice.setTextColor(resources.getColor(R.color.order_transaction_product_item_text_color))
        mUnitPrice.typeface = numberFont
        setUnitPrice(mUnitPrice.text.toString())

        mTotalPriceText.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.order_transaction_product_item_price_items_title_text_size).toFloat())
        mTotalPriceText.setTextColor(resources.getColor(R.color.order_transaction_product_item_text_color))
        mTotalPriceText.typeface = textFont
        mTotalPriceText.text = resources.getString(R.string.order_transaction_product_item_total_price_text)

        mTotalPriceSuffix.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.order_transaction_product_item_price_items_suffix_text_size).toFloat())
        mTotalPriceSuffix.setTextColor(resources.getColor(R.color.order_transaction_product_item_text_color))
        mTotalPriceSuffix.typeface = textFont
        mTotalPriceSuffix.text = resources.getString(R.string.rial)

        mTotalPrice.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.order_transaction_product_item_price_items_value_text_size).toFloat())
        mTotalPrice.setTextColor(resources.getColor(R.color.order_transaction_product_item_text_color))
        mTotalPrice.typeface = numberFont
        setTotalPrice(mTotalPrice.text.toString())




//        bindData(ShopCartItemModel(1, 11, 125536, ProductInfo(discount = 20, oldPrice = 15000000, price = 12000000,
//                imageUrl = "https://staticcontent.eways.co//upload/ProductPictures/71462.jpg",
//                minOrder = 1, maxOrder = 100, name = "اب پ ت ث ج چ ح خ د ذ ر ز م ن ک ف ق ع غ ح ح ح ح حاسسسسسسسسسسسسسسسس", point = 106)))
//
//        setAddRemoveListener(object : OnAddRemoveListener{
//            override fun onAddClicked() {
//            }
//
//            override fun onRemoveClicked() {
//            }
//
//        })

    }


    private fun setTotalPrice(totalPrice: String) {
        mTotalPrice.text = Utils.toPersianPriceNumber(totalPrice)

    }

    private fun setUnitPrice(price: String) {

        mUnitPrice.text = Utils.toPersianPriceNumber(price)

    }


    fun bindData(model: OrderDetail) {

        mProduct = model

        val productInfo = model

        Glide.with(context).load(URL(productInfo.imageUrl)).fitCenter().into(mProductImage)

        mProductName.text = Utils.toPersianNumber(productInfo.productName)

        mProductId.text = Utils.toPersianNumber(productInfo.productId)

        mProductCount = model.quantity
        setCount(model.quantity)


        updatePrices()

    }

    private fun updatePrices() {

        setUnitPrice(mProduct.price.toString())
        setTotalPrice((mProductCount * mProduct.price).toString())


    }



    private fun setCount(count: Int) {

        mCount.text = Utils.toPersianPriceNumber(count)
    }

    fun getCount(): Int {
        return mProductCount
    }

}