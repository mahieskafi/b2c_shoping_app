package com.srp.ewayspanel.ui.view.shopitem

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.srp.eways.util.Utils
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.storepage.filter.ProductInfo
import com.srp.ewayspanel.ui.store.product.ProductItemClickListener
import com.srp.ewayspanel.ui.view.CounterView2
import com.srp.ewayspanel.ui.view.OffPriceTextView
import com.yashoid.sequencelayout.SequenceLayout
import java.net.URL
import kotlin.math.min


/**
 * Created by ErfanG on 15/10/2019.
 */
abstract class ShopItemView : SequenceLayout {

    companion object {
        const val ACTION_ADD: Boolean = true
        const val ACTION_REMOVE: Boolean = false
    }

    private lateinit var mProductImage: ImageView
    protected lateinit var mProductName: AppCompatTextView
    private lateinit var mProductPriceBeforeOff: OffPriceTextView
    private lateinit var mProductPriceAfterOff: TextView
    protected lateinit var mProductOffValue: TextView
    private lateinit var mPrefixRial: TextView
    private lateinit var mClickhelper: View
    protected lateinit var mDeleteProduct: AppCompatImageView
    private lateinit var mProductCountError: TextView
    protected lateinit var mCountView: CounterView2

    private lateinit var mListener: ProductItemClickListener

    private lateinit var mItem: ProductInfo
    private var mEnableColor: Int = 0
    private var mDisableColor: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyleAtr: Int) : super(context, attributeSet, defStyleAtr)


    protected open fun initialize(context: Context, attributeSet: AttributeSet?, defStyleAtr: Int) {

        LayoutInflater.from(context).inflate(getView(), this, true)

        //initial views here
        mProductImage = findViewById(R.id.product_image)
        mProductName = findViewById(R.id.product_name)
        mProductPriceBeforeOff = findViewById(R.id.price_before_off)
        mProductPriceAfterOff = findViewById(R.id.price_after_off)
        mProductOffValue = findViewById(R.id.off_value)
        mClickhelper = findViewById(R.id.click_helper)
        mDeleteProduct = findViewById(R.id.delete_product)
        mProductCountError = findViewById(R.id.product_count_error)
        mCountView = findViewById(R.id.product_count)
        mPrefixRial = findViewById(R.id.price_suffix)

        addSequences(getSequence())

        val abResources = DI.getABResources()

        ViewCompat.setElevation(this, abResources.getDimenPixelSize(R.dimen.shop_item_elevation).toFloat())

        mDeleteProduct.setImageDrawable(abResources.getDrawable(R.drawable.shop_item_delete_icon_enable))
        mDeleteProduct.setOnClickListener {
            if (this::mListener.isInitialized && this::mItem.isInitialized) {
                mCountView.setCount(0)
                mListener.onDeleteClicked(mItem.id)
            }
        }

        mEnableColor = abResources.getColor(R.color.shop_item_product_name_color)
        mDisableColor = abResources.getColor(R.color.shop_item_disable_product_name_color)

        val mainTypeFace = ResourcesCompat.getFont(getContext(), R.font.iran_yekan)

        mProductName.setTextColor(mEnableColor)
        mProductName.setLineSpacing(0f, 1.4f)
        mProductName.typeface = mainTypeFace
        mProductName.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.shop_item_product_name_text_size).toFloat())
        mProductName.maxLines = 2

        mProductPriceBeforeOff.setTextColor(abResources.getColor(R.color.shop_item_price_before_color))
        mProductPriceBeforeOff.typeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan)
        mProductPriceBeforeOff.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.shop_item_price_before_text_size).toFloat())


        mProductPriceAfterOff.setTextColor(mEnableColor)
        mProductPriceAfterOff.typeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan_bold)
        mProductPriceAfterOff.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.shop_item_price_after_text_size).toFloat())

        mPrefixRial.setTextColor(mEnableColor)

        mProductOffValue.setTextColor(abResources.getColor(R.color.shop_item_off_value_color))
        mProductOffValue.typeface = ResourcesCompat.getFont(getContext(), R.font.iran_sans_regular)
        mProductOffValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimen(R.dimen.shop_item_off_value_text_size))
        mProductOffValue.background = abResources.getDrawable(R.drawable.shop_item_off_value_background)

        mProductCountError.setTextColor(abResources.getColor(R.color.shop_item_product_count_error_color))
        mProductCountError.typeface = ResourcesCompat.getFont(getContext(), R.font.iran_yekan_bold)
        mProductCountError.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.shop_item_product_count_error_size).toFloat())
        mProductCountError.text = resources.getString(R.string.club_item_unavailable_error_text)

        setPriceBeforeOff(mProductPriceBeforeOff.text.toString())
        setPriceAfterOff(mProductPriceAfterOff.text.toString())
        setOffValue(10)
        setProductName(mProductName.text.toString())
        setProductImage("https://panel.eways.co/upload/ProductPictures/63734.jpg")

        mCountView.setHorizantalyView(getIsHorizontal())
    }


    abstract fun getView(): Int

    abstract fun getSequence(): Int

    fun setProductImage(imageUrl: String) {
        Glide.with(context).load(URL(imageUrl)).fitCenter().into(mProductImage)
    }

    fun setClickAction(action: OnClickListener) {
        mClickhelper.setOnClickListener(action)
    }

    fun getCount() = mCountView.getCount()


    //ProductName
    fun setProductNameColor(productNameColor: Int) {
        mProductName.setTextColor(productNameColor)
    }

    fun setProductNameTypeface(productNameTypeface: Typeface) {
        mProductName.typeface = productNameTypeface
    }

    fun setProductName(productName: String) {
        mProductName.text = Utils.toPersianNumber(productName)
    }

    fun setProductNameSize(productNameSize: Float) {
        mProductName.setTextSize(TypedValue.COMPLEX_UNIT_PX, productNameSize)
    }

    //PriceBeforeDiscount
    fun setPriceBeforeColor(priceBeforeColor: Int) {
        mProductPriceBeforeOff.setTextColor(priceBeforeColor)
    }

    fun setPriceBeforeTypeface(priceBeforeTypeface: Typeface) {
        mProductPriceBeforeOff.typeface = priceBeforeTypeface
    }

    fun setPriceBeforeOff(priceBeforeOff: String) {
        mProductPriceBeforeOff.text = Utils.toPersianPriceNumber(priceBeforeOff)
    }

    fun setPriceBeforeSize(priceBeforeSize: Float) {
        mProductPriceBeforeOff.setTextSize(TypedValue.COMPLEX_UNIT_PX, priceBeforeSize)
    }

    //PriceAfterDiscount
    fun setPriceAfterColor(priceAfterColor: Int) {
        mProductPriceAfterOff.setTextColor(priceAfterColor)
    }

    fun setPriceAfterTypeface(priceAfterTypeface: Typeface) {
        mProductPriceAfterOff.typeface = priceAfterTypeface
    }

    fun setPriceAfterOff(priceAfterOff: String) {
        mProductPriceAfterOff.text = Utils.toPersianPriceNumber(priceAfterOff)
        mProductPriceAfterOff.requestLayout()
    }

    fun setPriceAfterSize(priceAfterSize: Float) {
        mProductPriceAfterOff.setTextSize(TypedValue.COMPLEX_UNIT_PX, priceAfterSize)
    }

    //DiscountValue
    fun setOffValueColor(offValueColor: Int) {
        mProductOffValue.setTextColor(offValueColor)
    }

    fun setOffValueTypeface(offValueTypeface: Typeface) {
        mProductOffValue.typeface = offValueTypeface
    }

    fun setOffValue(offValue: Long) {
        if (offValue == 0L) {
            mProductName.maxLines = 2
            mProductOffValue.visibility = View.INVISIBLE
            mProductPriceBeforeOff.visibility = View.INVISIBLE
        } else {
            mProductName.maxLines = 2

            mProductOffValue.text = "  " + Utils.toPersianNumber(offValue) + "% "

            mProductOffValue.visibility = View.VISIBLE
            mProductPriceBeforeOff.visibility = View.VISIBLE
        }
    }

    fun setOffValueSize(offValueSize: Float) {
        mProductOffValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, offValueSize)
    }

    fun setOffValueBackground(offValueBackground: Drawable) {
        mProductOffValue.background = offValueBackground
    }

    fun setCountListener(listener: ProductItemClickListener, productInfo: ProductInfo) {

        mListener = listener

        mCountView.setCountChangeListener(object : CounterView2.Counter2ChangeListener {
            override fun onCountAdded(newCount: Int, isTotalCount: Boolean) {
                setDeleteIconSettings(newCount)

                val productInfoo = com.srp.ewayspanel.model.shopcart.ProductInfo(availability = mItem.availability, discount = mItem.discount, id = mItem.id,
                        imageUrl = mItem.imageUrl, isSim = mItem.isSim, lawId = mItem.lawId, maxOrder = mItem.maxOrder, minOrder = mItem.minOrder,
                        name = mItem.name!!, oldPrice = mItem.oldPrice, overInventoryCount = mItem.overInventoryCount, point = mItem.point.toInt(),
                        price = mItem.price, seoName = mItem.seoName, stock = mItem.stock)

                mListener.onAddClicked(productInfoo, newCount, isTotalCount)

                setIsBuy(true);
            }

            override fun onCountRemoved(newCount: Int, isTotalCount: Boolean) {
                setDeleteIconSettings(newCount)

                mListener.onRemoveClicked(productInfo.id, newCount, isTotalCount)

                if (newCount > 0) {
                    setIsBuy(true);
                } else {
                    setIsBuy(false);
                }
            }

            override fun onCountNotChanged(count: Int) {
            }
        })
    }

    fun bind(item: ProductInfo) {

        mCountView.setMaxCount(min(item.maxOrder, item.stock))
        mItem = item
    }

    fun setCount(productCount: Int, notify: Boolean = true) {

        if (min(mItem.stock, mItem.maxOrder) > 0) {

            setDeleteIconSettings(productCount)
            setCounterViewVisibility(true)
            setAvailability(true)
            mCountView.setCount(productCount, notify)
            mDeleteProduct.setImageDrawable(resources.getDrawable(R.drawable.shop_item_delete_icon_enable))

            if (mCountView.getCount() > 0) {
                setIsBuy(true)
            } else {
                setIsBuy(false)
            }

            mProductCountError.visibility = View.INVISIBLE
            mProductPriceAfterOff.visibility = View.VISIBLE
            mPrefixRial.visibility = View.VISIBLE

        } else {

            setCounterViewVisibility(false)
            setAvailability(false)
            mDeleteProduct.setImageDrawable(resources.getDrawable(R.drawable.shop_item_delete_icon_disable))

            mProductCountError.visibility = View.VISIBLE
            mProductPriceBeforeOff.visibility = INVISIBLE
            mProductPriceAfterOff.visibility = View.INVISIBLE
            mPrefixRial.visibility = View.INVISIBLE

            setIsBuy(false)
        }
    }

    private fun setDeleteIconSettings(productCount: Int) {

        if (productCount == 0) {
            mDeleteProduct.setImageDrawable(resources.getDrawable(R.drawable.shop_item_delete_icon_disable))
            mDeleteProduct.isClickable = false
        } else {
            mDeleteProduct.setImageDrawable(resources.getDrawable(R.drawable.shop_item_delete_icon_enable))
            mDeleteProduct.isClickable = true
        }

    }

    private fun setAvailability(isAvailable: Boolean) {
        if (isAvailable) {
            mProductPriceAfterOff.setTextColor(mEnableColor)
            mProductName.setTextColor(mEnableColor)
            mPrefixRial.setTextColor(mEnableColor)
        } else {
            mProductPriceAfterOff.setTextColor(mDisableColor)
            mProductName.setTextColor(mDisableColor)
            mPrefixRial.setTextColor(mDisableColor)
        }
    }

    abstract fun setIsBuy(isBuy: Boolean)
    abstract fun getIsHorizontal(): Boolean
    abstract fun setCounterViewVisibility(isAvailable: Boolean)
}