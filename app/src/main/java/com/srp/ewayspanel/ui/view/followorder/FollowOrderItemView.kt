package com.srp.ewayspanel.ui.view.followorder

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.srp.eways.di.DIMain
import com.srp.eways.ui.view.button.ButtonElement
import com.srp.eways.util.Utils
import com.srp.ewayspanel.R
import com.srp.ewayspanel.model.followorder.FollowOrderItem
import com.srp.ewayspanel.model.transaction.order.OrderItem
import com.srp.ewayspanel.ui.followorder.ShowMoreDetailClickListener
import com.srp.ewayspanel.ui.transaction.order.list.OrderStatus

class FollowOrderItemView : LinearLayout {

    interface OnMoreDetailClickListener {
        fun onDetailClicked(orderItem: FollowOrderItem)
    }


    private val ANIM_DURATION = 200L

    private var mOrderId: AppCompatTextView
    private var mOrderStatus: AppCompatTextView
    private var mOrderPrice: AppCompatTextView
    private var mOrderStatusIcon: AppCompatImageView
    private var mOrderDetailIcon: AppCompatImageView
    private var mOrderDetailIconView: FrameLayout

    private var mOrderDetailKeyValuePair: KeyValuePairView
    private var mOrderMoreDetailButton: ButtonElement
    private var mOrderDetailLinear: LinearLayout

    private var mOpen = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defAttrSet: Int) : super(
            context,
            attributeSet,
            defAttrSet
    )

    init {
        LayoutInflater.from(context).inflate(R.layout.item_follow_order, this, true)

        mOrderId = findViewById(R.id.tv_id)
        mOrderStatus = findViewById(R.id.tv_status)
        mOrderPrice = findViewById(R.id.tv_amount)

        mOrderStatusIcon = findViewById(R.id.iv_status)
        mOrderDetailIcon = findViewById(R.id.iv_detail)
        mOrderDetailIconView = findViewById(R.id.frame_detail)

        mOrderDetailKeyValuePair = findViewById(R.id.key_value_detail)
        mOrderMoreDetailButton = findViewById(R.id.btn_more_detail)
        mOrderDetailLinear = findViewById(R.id.linear_detail)

        var resources = DIMain.getABResources()

        mOrderMoreDetailButton.setEnabledBackground(resources.getDrawable(R.drawable.follow_order_item_more_detail_button_background))
        mOrderMoreDetailButton.setText(resources.getString(R.string.follow_order_list_detail_button_text))
        mOrderMoreDetailButton.setEnable(true)
        mOrderMoreDetailButton.setIconVisibility(View.GONE)
        mOrderMoreDetailButton.setTextColor(resources.getColor(R.color.follow_order_detail_button_text_color))
        mOrderMoreDetailButton.setTextSize(resources.getDimenPixelSize(R.dimen.follow_order_detail_button_text_size).toFloat())
        mOrderMoreDetailButton.setTextTypeFace(ResourcesCompat.getFont(context, R.font.iran_sans_medium)!!)

    }


    fun setOrderId(id: String) {
        mOrderId.text = Utils.toPersianNumber(id)
    }

    fun setOrderPrice(price: Long) {
        mOrderPrice.text = Utils.toPersianPriceNumber(price)
    }

    fun setOrderStatus(status: Int, statusValue: String) {
        mOrderStatus.text = Utils.toPersianPriceNumber(statusValue)
        mOrderStatusIcon.setImageDrawable(DIMain.getABResources().getDrawable(OrderStatus.getResultDrawable(status)))
    }

    fun setDetailValues(deliveryStatus: String, paymentStatus: String?, deliveryType: String, date: String, time: String) {
        val keyValue = ArrayList<Pair<String, String>>()

        var resources = DIMain.getABResources()

        keyValue.add(Pair(resources.getString(R.string.follow_order_list_detail_delivery_status_title), deliveryStatus))
        if (paymentStatus != null) {
            keyValue.add(Pair(resources.getString(R.string.follow_order_list_detail_payment_status_title), paymentStatus))
        } else {
            keyValue.add(Pair(resources.getString(R.string.follow_order_list_detail_payment_status_title), "-"))
        }

        keyValue.add(Pair(resources.getString(R.string.follow_order_list_detail_delivery_type_title), deliveryType))
        keyValue.add(Pair(resources.getString(R.string.follow_order_list_detail_date_title), Utils.toPersianNumber(date)))
        keyValue.add(Pair(resources.getString(R.string.follow_order_list_detail_time_title), Utils.toPersianNumber(time)))

        mOrderDetailKeyValuePair.setPairs(keyValue)
    }

    fun setMoreDetailClickListener(followOrderItem: FollowOrderItem, onclickListener: OnMoreDetailClickListener) {
        mOrderMoreDetailButton.setOnClickListener {
            onclickListener.onDetailClicked(followOrderItem)
        }
    }

    fun setOnShowMoreClickListener(listener: ShowMoreDetailClickListener) {
        setOnClickListener {
            mOrderDetailLinear.visibility = View.VISIBLE
            openClose(!mOpen)
            listener.onShowMoreClicked(mOpen)
        }
    }

    private fun openClose(open: Boolean) {

        if (mOpen != open) {
            mOpen = open

            val itemHeight = DIMain.getABResources().getDimenPixelSize(R.dimen.follow_order_list_item_height)
            val detailHeight = DIMain.getABResources().getDimenPixelSize(R.dimen.follow_order_list_detail_height)

            val anim: ValueAnimator = if (mOpen) {
                mOrderDetailLinear.visibility = View.VISIBLE
                ValueAnimator.ofInt(itemHeight, itemHeight + detailHeight)
            } else {
                ValueAnimator.ofInt(itemHeight + detailHeight, itemHeight)
            }

            anim.addUpdateListener {
                layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, it.animatedValue as Int)
                requestLayout()
            }
            anim.duration = ANIM_DURATION


            val rotationAnim: ValueAnimator = if (mOpen) {
                ValueAnimator.ofFloat(0f, 180f)
            } else {
                ValueAnimator.ofFloat(180f, 0f)
            }

            rotationAnim.addUpdateListener {
                mOrderDetailIcon.rotation = it.animatedValue as Float
            }
            rotationAnim.duration = ANIM_DURATION


            anim.start()
            rotationAnim.start()
        }
    }
}