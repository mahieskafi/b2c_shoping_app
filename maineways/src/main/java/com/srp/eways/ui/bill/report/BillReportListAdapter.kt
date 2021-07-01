package com.srp.eways.ui.bill.report

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendarConstants
import com.srp.eways.R
import com.srp.eways.base.BaseRecyclerAdapter2
import com.srp.eways.base.BaseViewHolder
import com.srp.eways.di.DIMain
import com.srp.eways.model.bill.report.BillReportItem
import com.srp.eways.ui.bill.ShowMoreClickListener
import com.srp.eways.util.BillUtil
import com.srp.eways.util.Utils

class BillReportListAdapter constructor(
        private val mContext: Context,
        private val mRetryClickListener: RetryClickListener,
        private val mAactionClickListener: ActionsListener)
    : BaseRecyclerAdapter2<BillReportItem, BaseViewHolder<BillReportItem, View>>(mContext, mRetryClickListener) {

    interface ActionsListener {
        fun onShareActionClickListener(billReportItem: BillReportItem)

        fun onSaveActionClickListener(billReportItem: BillReportItem)

        fun onPayActionClickListener(billReportItem: BillReportItem)

        fun onShowMoreClickListener(isShowMore: Boolean, billReportItem: BillReportItem)
    }

    companion object {
        const val VIEW_TYPE_REPORT = 0
        const val VIEW_TYPE_MONTH_NAME = 2
    }

    private val mResources = DIMain.getABResources()

    override fun getItemViewType(position: Int): Int {
        val superValue = super.getItemViewType(position)

        if (superValue == VIEW_TYPE_ITEM) {

            if (data[position] != null && !data[position].isTopData) {
                return VIEW_TYPE_ITEM
            } else {
                return VIEW_TYPE_MONTH_NAME
            }
        }

        return superValue
    }


    override fun onCreateViewHolder2(parent: ViewGroup?, viewType: Int): BaseViewHolder<BillReportItem, View> {
        when (viewType) {

            VIEW_TYPE_REPORT -> {

                val view = BillReportItemView(mContext).apply {
                    layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {

                        rightMargin = mResources.getDimenPixelSize(R.dimen.bill_report_list_item_margin_side)
                        leftMargin = mResources.getDimenPixelSize(R.dimen.bill_report_list_item_margin_side)
                        topMargin = mResources.getDimenPixelSize(R.dimen.bill_report_list_item_margin_top)
                        bottomMargin = mResources.getDimenPixelSize(R.dimen.bill_report_list_item_margin_bottom)

                        gravity = Gravity.CENTER
                    }
                }

                return BillReportViewHolder(view, mAactionClickListener)

            }
            else -> {
                return BillMonthHeaderViewHolder(AppCompatTextView(mContext).apply {

                    layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {

                        rightMargin = mResources.getDimenPixelSize(R.dimen.bill_report_list_item_margin_side)
                        topMargin = mResources.getDimenPixelSize(R.dimen.bill_report_list_item_month_margin_top)
                        bottomMargin = mResources.getDimenPixelSize(R.dimen.bill_report_list_item_month_margin_bottom)

                        gravity = Gravity.RIGHT

                    }

                    gravity = Gravity.RIGHT
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, mResources.getDimenPixelSize(R.dimen.bill_report_list_month_size).toFloat())
                    setTextColor(mResources.getColor(R.color.bill_report_list_month_color))
                    typeface = ResourcesCompat.getFont(mContext, R.font.iran_yekan)
                    textAlignment = View.TEXT_ALIGNMENT_GRAVITY

                })
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) {
        if (position == itemCount) {
            holder.onBind(null)
        } else if (position < data.size) {
            super.onBindViewHolder(holder, position)
        }
    }


    override fun appendData(data: MutableList<BillReportItem>?) {
        super.appendData(data)

        sortData()
        notifyDataSetChanged()
    }

    override fun setNewData(data: MutableList<BillReportItem>?) {
        super.setNewData(data)

        sortData()
        notifyDataSetChanged()

    }

    private fun sortData() {

        val iterator = data.iterator()
        while (iterator.hasNext()) {

            val item = iterator.next()
            if (item.isTopData) {
                iterator.remove()
            }
        }

        val newArray = ArrayList<BillReportItem>()

        var lastDate: BillReportItem? = null

        data.forEach { item ->

            if (lastDate == null || !isTheSameYearDate(item, lastDate)) {

                val date = item.logDate.split(" ")[0].split("/")

                val month = date[1].toInt()
                val year = date[0].toInt()

                val yearMonth = BillReportItem(isTopData = true, topDateText = "${PersianCalendarConstants.persianMonthNames[month - 1]} ${year}")
                lastDate = item
                newArray.add(yearMonth)
            }

            newArray.add(item)
        }

        super.setNewData(newArray)

    }

    private fun isTheSameYearDate(item: BillReportItem, lastDate: BillReportItem?): Boolean {

        val itemDate = item.logDate.split(" ")[0].split("/")
        val lastDatee = lastDate!!.logDate.split(" ")[0].split("/")

        if (itemDate[0] == lastDatee[0] && itemDate[1] == lastDatee[1]) {
            return true
        }

        return false
    }


    class BillReportViewHolder(view: View, actionClickListener: ActionsListener) : BaseViewHolder<BillReportItem, View>(view) {

        private val mActionClickListener = actionClickListener

        override fun onBind(item: BillReportItem?) {

            with(itemView as BillReportItemView) {

                setStatusIcon(BillUtil.getBillReportStatusIcon(item!!.status))
                setStatus(BillUtil.getBillReportStatus(item.status))
                setTitle("قبض " + BillUtil.getServiceName(item.billTypeId))
                setBillId(item.billId)
                setPrice(item.price)
                setDate(item.logDate.split(" ")[0]);
                setTime(item.logDate.split(" ")[1].split(":")[0] + ":" +
                        item.logDate.split(" ")[1].split(":")[1])


                val resultNumber: String? = when {
                    item.resultNumber != 0L -> item.resultNumber.toString()
                    else -> "-"
                }

                val tempResultNumber: String? = when {
                    item.tempResultNumber.isNotEmpty() -> {
                        item.tempResultNumber
                    }
                    else -> "-"
                }

                val inquiryNumber: String? = when {
                    item.inquiryNumber!!.isNotEmpty() -> {
                        item.inquiryNumber
                    }
                    else -> "-"
                }

                setShowMore(item.isShowMore)
                //todo
                setMoreDetails(arrayListOf(item.paymentId, tempResultNumber, item.bankName, resultNumber, inquiryNumber))

                setShareAction(View.OnClickListener {
                    mActionClickListener.onShareActionClickListener(item)
                })

                setSaveAction(View.OnClickListener {
                    mActionClickListener.onSaveActionClickListener(item)
                })
                setPayAction(View.OnClickListener {
                    mActionClickListener.onPayActionClickListener(item)
                })

                setOnShowMoreClickListener(object : ShowMoreClickListener {
                    override fun onShowMoreClicked(isShowMore: Boolean) {
                        mActionClickListener.onShowMoreClickListener(isShowMore, item)
                    }
                })
            }

        }

    }

    class BillMonthHeaderViewHolder(view: View) : BaseViewHolder<BillReportItem, View>(view) {

        override fun onBind(item: BillReportItem?) {
            (itemView as AppCompatTextView).text = Utils.toPersianNumber(item!!.topDateText)

            adapterPosition
        }

    }
}
