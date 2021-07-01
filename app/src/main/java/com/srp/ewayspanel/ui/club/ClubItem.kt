package com.srp.ewayspanel.ui.club

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
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
 * Created by ErfanG on 19/11/2019.
 */
abstract class  ClubItem : SequenceLayout {

    companion object {
        const val ACTION_ADD: Boolean = true
        const val ACTION_REMOVE: Boolean = false
    }

    private lateinit var mProductImage: ImageView
    private lateinit var mProductName: TextView
    private lateinit var mPoint: TextView
    private lateinit var mPrice: TextView
    private lateinit var mPriceSuffix: TextView
    private lateinit var mDivider: View
    private lateinit var mProductPriceBeforeOff: OffPriceTextView
    private lateinit var mProductOffValue: TextView

    private lateinit var mBuyError: TextView
    private lateinit var mAvailabilityError: TextView
    private lateinit var mClickhelper: View

    protected lateinit var mCountView: CounterView2
    private lateinit var mDeleteProduct: AppCompatImageView
    private lateinit var mListener: ProductItemClickListener
    private lateinit var mStarPoint: AppCompatImageView

    private lateinit var mNotEnoughPointTextError: String
    private lateinit var mUnAvailableTextError: String
    private var mNotEnoughPointErrorTextSize: Float = 0f
    private var mUnAvailableErrorTextSize: Float = 0f

    @ColorInt
    private var mSufficientPointColor: Int = 0

    @ColorInt
    private var mInsufficientPointColor: Int = 0

    private lateinit var mItem: ProductInfo

    private var mUserPoint: Long = 0

    private var mEnableColor: Int = 0
    private var mDisableColor: Int = 0

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defAtrStyle: Int) : super(context, attributeSet, defAtrStyle)

    protected open fun initialize(context: Context, attributeSet: AttributeSet?, defStyleAtr: Int) {

        LayoutInflater.from(context).inflate(getView(), this, true)

        //initial views here
        mProductImage = findViewById(R.id.product_image)
        mProductName = findViewById(R.id.product_name)
        mPoint = findViewById(R.id.point)
        mPrice = findViewById(R.id.price)
        mPriceSuffix = findViewById(R.id.price_suffix)
        mDivider = findViewById(R.id.divider)
        mCountView = findViewById(R.id.product_count)
        mDeleteProduct = findViewById(R.id.delete_product)
        mBuyError = findViewById(R.id.buy_error_text)
        mAvailabilityError = findViewById(R.id.availability_error_text)
        mClickhelper = findViewById(R.id.click_helper)
        mStarPoint = findViewById(R.id.star_point)
        mProductPriceBeforeOff = findViewById(R.id.price_before_off)
        mProductOffValue = findViewById(R.id.off_value)


        addSequences(getSequence())

        val resources = DI.getABResources()

        mDeleteProduct.setImageDrawable(resources.getDrawable(R.drawable.shop_item_delete_icon_enable))
        mStarPoint.setImageDrawable(resources.getDrawable(R.drawable.star_point_icon_enable))
        mDeleteProduct.setOnClickListener {
            if (this::mListener.isInitialized && this::mItem.isInitialized) {
                mCountView.setCount(0)
                mListener.onDeleteClicked(mItem.id)
            }
        }

        mEnableColor = resources.getColor(R.color.shop_item_product_name_color)
        mDisableColor = resources.getColor(R.color.shop_item_disable_product_name_color)

        ViewCompat.setElevation(this, resources.getDimenPixelSize(R.dimen.slub_item_elevation).toFloat())
        val textTypeFace = ResourcesCompat.getFont(context, R.font.iran_yekan)
        val numberTypeFace = ResourcesCompat.getFont(context, R.font.iran_sans_regular)

        mProductPriceBeforeOff.setTextColor(resources.getColor(R.color.shop_item_price_before_color))
        mProductPriceBeforeOff.typeface = ResourcesCompat.getFont(getContext(), R.font.iran_sans_regular)
        mProductPriceBeforeOff.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_item_price_before_text_size).toFloat())

        mProductOffValue.setTextColor(resources.getColor(R.color.shop_item_off_value_color))
        mProductOffValue.typeface = ResourcesCompat.getFont(getContext(), R.font.iran_sans_regular)
        mProductOffValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.shop_item_off_value_text_size).toFloat())
        mProductOffValue.background = resources.getDrawable(R.drawable.shop_item_off_value_background)

        mProductName.setTextColor(mEnableColor)
        mProductName.typeface = textTypeFace
        mProductName.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.club_item_product_name_text_size).toFloat())
        mProductName.setLineSpacing(0f, 1.4f)
        mProductName.maxLines = 2
        mProductName.minLines = 2

        mSufficientPointColor = resources.getColor(R.color.club_item_point_sufficient_color)
        mInsufficientPointColor = resources.getColor(R.color.club_item_point_insufficient_color)

        mPoint.setTextColor(mInsufficientPointColor)
        mPoint.typeface = textTypeFace
        mPoint.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.club_item_point_size).toFloat())

        mPrice.setTextColor(mEnableColor)
        mPrice.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_bold)
        mPrice.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.club_item_price_size).toFloat())

        mPriceSuffix.setTextColor(mEnableColor)
        mPriceSuffix.typeface = textTypeFace
        mPriceSuffix.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimenPixelSize(R.dimen.club_item_price_suffix_size).toFloat())
        mPriceSuffix.text = resources.getString(R.string.club_item_price_suffix)

        mNotEnoughPointErrorTextSize = resources.getDimenPixelSize(R.dimen.club_item_buy_error_size).toFloat()
        mUnAvailableErrorTextSize = resources.getDimenPixelSize(R.dimen.club_item_unavailable_error_size).toFloat()

        mNotEnoughPointTextError = resources.getString(R.string.club_item_buy_error_text)
        mUnAvailableTextError = resources.getString(R.string.club_item_unavailable_error_text)

        mBuyError.setTextColor(resources.getColor(R.color.club_item_point_insufficient_color))
        mBuyError.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_bold)
        mBuyError.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNotEnoughPointErrorTextSize)
        mBuyError.text = mNotEnoughPointTextError

        mAvailabilityError.setTextColor(resources.getColor(R.color.club_item_point_insufficient_color))
        mAvailabilityError.typeface = ResourcesCompat.getFont(context, R.font.iran_yekan_bold)
        mAvailabilityError.setTextSize(TypedValue.COMPLEX_UNIT_PX, mUnAvailableErrorTextSize)
        mAvailabilityError.text = mUnAvailableTextError

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

    public fun setProductName(productName: String) {
        mProductName.text = Utils.toPersianNumber(productName)
    }

    private fun setPoint(point: String) {
        mPoint.text = Utils.toPersianNumber(Utils.toPersianPriceNumber(point))
    }

    public fun setPrice(price: Long) {
        mPrice.text = Utils.toPersianPriceNumber(price)
    }

    public fun setPriceBefforOff(price: Long) {
        mProductPriceBeforeOff.text = Utils.toPersianPriceNumber(price)

        if (mPrice.text == mProductPriceBeforeOff.text) {
            mProductPriceBeforeOff.visibility = GONE
        }
    }

    fun setOffValue(offValue: Long) {
        if (offValue == 0L) {
            mProductName.maxLines = 2
            mProductOffValue.visibility = View.INVISIBLE
            if (getIsHorizontal()) {
                findSequenceById("seq").spans[0].size = 15f
                requestLayout()
            }
            mProductPriceBeforeOff.visibility = View.GONE
        } else {
            mProductName.maxLines = 2

            mProductOffValue.text = "  " + Utils.toPersianNumber(offValue) + "% "

            if (getIsHorizontal()) {
                findSequenceById("seq").spans[0].size = 6f
                requestLayout()
            }

            mProductOffValue.visibility = View.VISIBLE
            mProductPriceBeforeOff.visibility = View.VISIBLE
        }
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

                setIsBuy(true)
            }

            override fun onCountRemoved(newCount: Int, isTotalCount: Boolean) {
                setDeleteIconSettings(newCount)

                mListener.onRemoveClicked(productInfo.id, newCount, isTotalCount)

                if (newCount > 0) {
                    setIsBuy(true)
                } else {
                    setIsBuy(false)
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

    fun setUserPoint(point: Long) {
        mUserPoint = point
    }

    fun setCount(productCount: Int, notify: Boolean = true) {

        if (min(mItem.stock, mItem.maxOrder) > 0) {
            if (mBuyError.visibility != View.VISIBLE) {
                mCountView.counterViewDisable(false)
                setAvailability(true)
                mCountView.visibility = View.VISIBLE
                mDeleteProduct.visibility = View.VISIBLE
                mDeleteProduct.setImageDrawable(resources.getDrawable(R.drawable.shop_item_delete_icon_enable))
                mBuyError.visibility = View.INVISIBLE
                mAvailabilityError.visibility = View.INVISIBLE

                if (getIsHorizontal()) {
                    findSequenceById("seq1").spans[0].size = 34f
                    requestLayout()
                }

                setDeleteIconSettings(productCount)

                mCountView.setCount(productCount, notify)
                if (mCountView.getCount() > 0) {
                    setIsBuy(true)
                } else {
                    setIsBuy(false)
                }
            }
        } else {

            mAvailabilityError.visibility = View.VISIBLE
            mBuyError.visibility = View.INVISIBLE
            mPriceSuffix.visibility = View.INVISIBLE
            mPrice.visibility = View.INVISIBLE
            mPoint.visibility = View.INVISIBLE
            mStarPoint.visibility = View.INVISIBLE
            showErrorInHorizontalMode()
            setAvailability(false)
            setIsBuy(false)

            if (getIsHorizontal()) {
                mCountView.counterViewDisable(true)
                mDeleteProduct.setImageDrawable(resources.getDrawable(R.drawable.shop_item_delete_icon_disable))
                mDeleteProduct.isClickable = false

            } else {
                mCountView.visibility = View.INVISIBLE
                mDeleteProduct.visibility = View.INVISIBLE
            }
        }
        if (mItem.point <= mUserPoint) {
            mStarPoint.setImageDrawable(resources.getDrawable(R.drawable.star_point_icon_enable))
        } else {
            mStarPoint.setImageDrawable(resources.getDrawable(R.drawable.star_point_icon_disable))
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


    fun setUserPoint(itemPoint: Long, loyaltyScore: Long) {

        mUserPoint = loyaltyScore

        if (itemPoint == 0L) {
            mPoint.visibility = INVISIBLE
            mStarPoint.visibility = INVISIBLE
        } else {
            mPoint.visibility = VISIBLE
            mStarPoint.visibility = VISIBLE
        }

        if (itemPoint <= loyaltyScore) {
            mPoint.setTextColor(mSufficientPointColor)
            mPriceSuffix.visibility = View.VISIBLE
            mPrice.visibility = View.VISIBLE
            mBuyError.visibility = View.INVISIBLE
            mAvailabilityError.visibility = View.INVISIBLE

            if (getIsHorizontal()) {
                findSequenceById("seq1").spans[0].size = 34f
                requestLayout()
            }

            mCountView.visibility = View.VISIBLE
            mDeleteProduct.visibility = View.VISIBLE
            mDeleteProduct.setImageDrawable(resources.getDrawable(R.drawable.shop_item_delete_icon_enable))
            mDeleteProduct.isClickable = true
            if (mCountView.getCount() > 0) {
                setIsBuy(true)
            } else {
                setIsBuy(false)
            }

        } else {
            mPoint.setTextColor(mInsufficientPointColor)
            mPriceSuffix.visibility = View.VISIBLE
            mPrice.visibility = View.VISIBLE
            mStarPoint.setImageDrawable(resources.getDrawable(R.drawable.star_point_icon_disable))

            mBuyError.visibility = View.VISIBLE
            mAvailabilityError.visibility = View.INVISIBLE
            setIsBuy(false)

            if (getIsHorizontal()) {
                mCountView.counterViewDisable(true)
                mDeleteProduct.setImageDrawable(resources.getDrawable(R.drawable.shop_item_delete_icon_disable))
                mDeleteProduct.isClickable = false
                showErrorInHorizontalMode()
            } else {
                mCountView.visibility = View.GONE
                mDeleteProduct.visibility = View.GONE
            }

        }

        mPoint.setText(Utils.toPersianPriceNumber(itemPoint))
    }

    private fun setAvailability(isAvailable: Boolean) {
        if (isAvailable) {
            mPrice.setTextColor(mEnableColor)
            mProductName.setTextColor(mEnableColor)
            mPriceSuffix.setTextColor(mEnableColor)
        } else {
            mPrice.setTextColor(mDisableColor)
            mProductName.setTextColor(mDisableColor)
            mPriceSuffix.setTextColor(mDisableColor)
            mProductPriceBeforeOff.visibility = View.GONE
            mProductOffValue.visibility = View.INVISIBLE
        }
    }

    abstract fun setIsBuy(isBuy: Boolean)
    abstract fun getIsHorizontal(): Boolean

    private fun showErrorInHorizontalMode() {
        if (getIsHorizontal()) {
            findSequenceById("seq").spans[0].size = 3f
            findSequenceById("seq1").spans[0].size = 28f
            requestLayout()
        }
    }
}