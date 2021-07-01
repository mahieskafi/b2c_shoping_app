package com.srp.ewayspanel.ui.followorder

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar
import com.srp.eways.ui.navigation.NavigationMemberFragment
import com.srp.eways.ui.view.button.ButtonElement
import com.srp.eways.ui.view.input.InputElement
import com.srp.eways.ui.navigation.NavigationViewType
import com.srp.eways.ui.view.toolbar.WeiredToolbar
import com.srp.eways.util.CalendarListener
import com.srp.eways.util.JalaliCalendar
import com.srp.eways.util.Utils
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.followOrder.FollowOredrStatusModel
import com.yashoid.inputformatter.InputFormatter
import com.srp.ewayspanel.model.followorder.FollowOrderItem
import com.srp.ewayspanel.model.transaction.order.OrderItem
import com.srp.ewayspanel.ui.followorder.list.FollowOrderListAdapter
import com.srp.ewayspanel.ui.transaction.order.OrderTransactionViewModel
import com.srp.ewayspanel.ui.transaction.order.detail.OrderTransactionDetailFragment
import com.srp.ewayspanel.ui.view.followorder.FollowOrderItemView
import java.util.*


class FollowOrderFragment : NavigationMemberFragment<FollowOrderViewModel>(), FollowOredrStatusAdapter.ItemClickListener,
        FollowOrderListAdapter.OnShowSummaryDetailClickListener,
        FollowOrderItemView.OnMoreDetailClickListener {


    companion object {

        @JvmStatic
        fun newInstance(): FollowOrderFragment = FollowOrderFragment()
    }

    private lateinit var mToolbar: WeiredToolbar

    lateinit var mStartDate: InputElement
    lateinit var mEndDate: InputElement
    lateinit var mStartSaleNumber: InputElement
    lateinit var mEndSaleNumber: InputElement
    lateinit var mOrderConditionText: AppCompatTextView
    lateinit var mOrderConditionIcon: AppCompatImageView

    private lateinit var mStatusAdapter: FollowOredrStatusAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var mFollowOrdersAdapter: FollowOrderListAdapter

    //should be removed/////////////////////////////////////////////
    val animalNames = ArrayList<FollowOredrStatusModel>()
    val m1 = FollowOredrStatusModel("asd",0,false)
//    val m2 = FollowOredrStatusModel("asghfd",0,false)
//    val m3 = FollowOredrStatusModel("asfghfg  hhfghd",0,false)
//    val m4 = FollowOredrStatusModel("as fgh gfh fghf fhgd",0,false)
//    val m5 = FollowOredrStatusModel("asghgfd",0,false)
//    val m6 = FollowOredrStatusModel("asfghd",0,false)
//should be removed/////////////////////////////////////////////

    lateinit var mSearchButton: ButtonElement

    var mStartCalendar: PersianCalendar = PersianCalendar()
    var mEndCalendar: PersianCalendar = PersianCalendar()

    override fun acquireViewModel(): FollowOrderViewModel {
        return DI.getViewModel(FollowOrderViewModel.getInstance().javaClass)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_follow_order
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mToolbar = view.findViewById(R.id.toolbar_follow_fragment)
        mStartDate = view.findViewById(R.id.follow_order_start_date)
        mEndDate = view.findViewById(R.id.follow_order_end_date)
        mStartSaleNumber = view.findViewById(R.id.follow_order_start_sale_number)
        mEndSaleNumber = view.findViewById(R.id.follow_order_end_sale_number)
        recyclerView = view.findViewById(R.id.status_recyclerview)
        mSearchButton = view.findViewById(R.id.follow_order_search_button)
        mOrderConditionText = view.findViewById(R.id.order_condition)
        mOrderConditionIcon = view.findViewById(R.id.order_Condition_Icon)


        setupToolbar()
        setupDateInputText()
        setupSaleNumberInputText()
        setUpOrderConditionTextAndIcon()
        setupSearchButton()

        val lm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
        lm.reverseLayout = true

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = lm

        setStatusData()

        mStatusAdapter!!.setClickListener(this)
        recyclerView.adapter = mStatusAdapter

    }

    private fun setupSaleNumberInputText() {

        val AB = DI.getABResources()
        mStartSaleNumber.setIconDrawable(AB.getDrawable(R.drawable.ic_shopping_card_02))
        mEndSaleNumber.setIconDrawable(AB.getDrawable(R.drawable.ic_shopping_card_02))
        mStartSaleNumber.setTextSize(AB.getDimenPixelSize(R.dimen.follow_order_input_text_sale_number_size).toFloat())
        mEndSaleNumber.setTextSize(AB.getDimenPixelSize(R.dimen.follow_order_input_text_sale_number_size).toFloat())
        mStartSaleNumber.addTextChangeListener(InputFormatter(Utils.PersianNumberFormatter))
        mEndSaleNumber.addTextChangeListener(InputFormatter(Utils.PersianNumberFormatter))
        mStartSaleNumber.setTextColor(AB.getColor(R.color.follow_order_input_text_sale_number_color))
        mEndSaleNumber.setTextColor(AB.getColor(R.color.follow_order_input_text_sale_number_color))
        mStartSaleNumber.setHintColor(AB.getColor(R.color.follow_order_input_text_date_sale_number_hint_color))
        mEndSaleNumber.setHintColor(AB.getColor(R.color.follow_order_input_text_date_sale_number_hint_color))
        mStartSaleNumber.setHint(AB.getString(R.string.follow_order_start_sale_number_hint))
        mEndSaleNumber.setHint(AB.getString(R.string.follow_order_end_sale_number_hint))
        mEndSaleNumber.setImeOption(EditorInfo.IME_ACTION_DONE)
        mStartSaleNumber.setUnSelectedBackground(AB.getDrawable(R.drawable.input_text_simple_background))
        mStartSaleNumber.setSelectedBackground(AB.getDrawable(R.drawable.input_text_simple_background))
        mEndSaleNumber.setUnSelectedBackground(AB.getDrawable(R.drawable.input_text_simple_background))
        mEndSaleNumber.setSelectedBackground(AB.getDrawable(R.drawable.input_text_simple_background))
        mStartSaleNumber.background = mStartSaleNumber.getUnselectedBackground()
        mEndSaleNumber.background = mEndSaleNumber.getUnselectedBackground()
        mStartSaleNumber.getEditText().isFocusable = true
        mEndSaleNumber.getEditText().isFocusable = true

        mStartSaleNumber.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                mEndSaleNumber.setText("")
            }

            override fun afterTextChanged(s: Editable) {}
        })

        mStartSaleNumber.getEditText().addTextChangedListener(OnInputDateAndSaleNumberChangeListener(mStartSaleNumber))
        mEndSaleNumber.getEditText().addTextChangedListener(OnInputDateAndSaleNumberChangeListener(mEndSaleNumber))
    }

    private fun setupDateInputText() {
        val AB = DI.getABResources()
        val currentDay: String = getGorgianToPersianFormattedDate(JalaliCalendar.getInstance()[JalaliCalendar.YEAR], JalaliCalendar.getInstance()[JalaliCalendar.MONTH], JalaliCalendar.getInstance()[JalaliCalendar.DAY_OF_MONTH])

        mStartDate.setIconDrawable(AB.getDrawable(R.drawable.ic_date))
        mEndDate.setIconDrawable(AB.getDrawable(R.drawable.ic_date))
        mStartDate.setTextSize(AB.getDimenPixelSize(R.dimen.follow_order_input_date_text_size).toFloat())
        mEndDate.setTextSize(AB.getDimenPixelSize(R.dimen.follow_order_input_date_text_size).toFloat())
        mStartDate.setTextColor(AB.getColor(R.color.follow_order_input_text_date_color))
        mEndDate.setTextColor(AB.getColor(R.color.follow_order_input_text_date_color))
        mStartDate.setHintColor(AB.getColor(R.color.follow_order_input_text_date_hint_color))
        mEndDate.setHintColor(AB.getColor(R.color.follow_order_input_text_date_hint_color))
        mStartDate.setHint(AB.getString(R.string.follow_order_start_date_hint))
        mEndDate.setHint(AB.getString(R.string.follow_order_end_date_hint))
        mStartDate.setUnSelectedBackground(AB.getDrawable(R.drawable.input_text_simple_background))
        mStartDate.setSelectedBackground(AB.getDrawable(R.drawable.input_text_simple_background))
        mEndDate.setUnSelectedBackground(AB.getDrawable(R.drawable.input_text_simple_background))
        mEndDate.setSelectedBackground(AB.getDrawable(R.drawable.input_text_simple_background))

        mStartDate.background = mStartDate.getUnselectedBackground()
        mEndDate.background = mEndDate.getUnselectedBackground()
        mStartDate.getEditText().isFocusable = false
        mEndDate.getEditText().isFocusable = false

        mStartDate.setText(currentDay)
        mEndDate.setText(currentDay)
        mStartDate.getEditText().setOnClickListener(InputStartDateClickListener())
        mStartDate.setOnClickListener(InputStartDateClickListener())
        mStartDate.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                mEndDate.setText("")
            }

            override fun afterTextChanged(s: Editable) {}
        })
        mEndDate.setOnClickListener(InputEndDateClickListener())
        mEndDate.getEditText().setOnClickListener(InputEndDateClickListener())
        mStartDate.getEditText().addTextChangedListener(OnInputDateAndSaleNumberChangeListener(mStartDate))
        mEndDate.getEditText().addTextChangedListener(OnInputDateAndSaleNumberChangeListener(mEndDate))
    }


    private fun setupToolbar() {
        val AB = DI.getABResources()

        mToolbar.setBackgroundColor(AB.getColor(R.color.follow_order_toolbar_background))
        mToolbar.setTitle(AB.getString(R.string.follow_order_page_title))
        mToolbar.setShowTitle(true)
        mToolbar.setShowShop(false)
        mToolbar.setTitleTextColor(AB.getColor(R.color.white))
        mToolbar.setTitleTextSize(AB.getDimen(R.dimen.follow_order_fragment_title_toolbar_size))
        mToolbar.setShowDeposit(false)
        mToolbar.setOnNavigationDrawerClickListener { toggleDrawer() }
        mToolbar.setShowNavigationUp(true)
        mToolbar.showNavigationDrawer(true)
        mToolbar.setOnBackClickListener { onBackPressed() }
    }

    private fun setUpOrderConditionTextAndIcon() {

        val AB = DI.getABResources()
        mOrderConditionText.setTextSize(TypedValue.COMPLEX_UNIT_PX, AB.getDimenPixelSize(R.dimen.follow_order_text_of_order_condition_size).toFloat())
        mOrderConditionText.setTextColor(AB.getColor(R.color.follow_order_text_of_order_condition_color))
        mOrderConditionText.text = AB.getString(R.string.follow_order_text_of_order_condition)
        mOrderConditionIcon.setImageDrawable(AB.getDrawable(R.drawable.ic_order_condition_title_icon))

    }

    private fun setupSearchButton() {
        val AB = DI.getABResources()
        mSearchButton.setText(AB.getString(R.string.follow_order_search_button_title))
        mSearchButton.setTextSize(AB.getDimenPixelSize(R.dimen.follow_order_button_text_size).toFloat())
        mSearchButton.setTextColor(AB.getColor(R.color.follow_order_button_text_color))
        mSearchButton.setEnabledBackground(AB.getDrawable(R.drawable.button_background_enabled))
        mSearchButton.setDisableBackground(AB.getDrawable(R.drawable.button_background_disabled))
        mSearchButton.setEnable(false)

    }


    private inner class InputStartDateClickListener : View.OnClickListener {
        override fun onClick(v: View) {

            if (mStartCalendar == null) {
                mStartCalendar = PersianCalendar()
            }
            val persianCalendar = PersianCalendar()
            val datePickerDialog = DatePickerDialog.newInstance(
                    CalendarListener(mStartDate, mStartCalendar),
                    persianCalendar.persianYear,
                    persianCalendar.persianMonth,
                    persianCalendar.persianDay
            )
            datePickerDialog.firstDayOfWeek = Calendar.SATURDAY
            datePickerDialog.show(baseActivity.fragmentManager, "Datepickerdialog")
        }
    }

    private inner class InputEndDateClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            if (mStartCalendar == null && mStartDate.getText().isEmpty()) {
                mStartDate.setError(getString(R.string.follow_order_stat_date_input_error))
            } else {
                if (mEndCalendar == null) {
                    mEndCalendar = PersianCalendar()
                }
                val persianCalendar = PersianCalendar()
                val datePickerDialog = DatePickerDialog.newInstance(
                        CalendarListener(mEndDate, mEndCalendar),
                        persianCalendar.persianYear,
                        persianCalendar.persianMonth,
                        persianCalendar.persianDay
                )
                datePickerDialog.minDate = mStartCalendar
                val maxCalendar = PersianCalendar()
                maxCalendar.setPersianDate(mStartCalendar!!.persianYear, mStartCalendar!!.persianMonth, mStartCalendar!!.persianDay + 29)
                datePickerDialog.maxDate = maxCalendar
                datePickerDialog.firstDayOfWeek = Calendar.SATURDAY
                datePickerDialog.show(baseActivity!!.fragmentManager, "Datepickerdialog")
            }
        }
    }


    private inner class OnInputDateAndSaleNumberChangeListener(private val mSelectedInput: InputElement) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if ((mStartDate.getText().isNotEmpty() && mEndDate.getText().isNotEmpty()) || (mStartSaleNumber.getText().isNotEmpty() && mEndSaleNumber.getText().isNotEmpty())) {
                mSearchButton.setEnable(true)
            } else
                mSearchButton.setEnable(false)

            if (isValidInputText(mSelectedInput))
                mSelectedInput.getEditText().error = null
        }

        override fun afterTextChanged(s: Editable) {}

    }

    private fun isValidInputText(inputElement: InputElement): Boolean {
        val startDateId = mStartDate.id
        val endDateId = mEndDate.id
        if (inputElement.id == startDateId)
            return mStartDate.getText().isNotEmpty()
        return if (inputElement.id == endDateId)
            mEndDate.getText().isNotEmpty()
        else true
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            mStartCalendar = PersianCalendar()
            mEndCalendar = PersianCalendar()
            mStartDate.setText("")
            mEndDate.setText("")
        }
    }


    private fun getGorgianToPersianFormattedDate(year: Int, monthOfYear: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = monthOfYear
        calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
        calendar[Calendar.HOUR_OF_DAY] = 12
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        val persianCalendar = PersianCalendar()
        persianCalendar.timeInMillis = calendar.timeInMillis
        return Utils.toPersianNumber(persianCalendar.persianYear.toString() + "/" + getMonth(persianCalendar.persianMonth) + "/" + persianCalendar.persianDay)
    }

    private fun getMonth(monthOfYear: Int): String {
        val monthString = monthOfYear + 1
        return monthString.toString()
    }

    override fun onItemClick(view: View?, position: Int) {

        mStatusAdapter.getItem(position).setmIsClick(true)
        mStatusAdapter.notifyDataSetChanged()
    }

    //should be removed/////////////////////////////////////////////
    private fun setStatusData(){
        animalNames.add(m1)
//        animalNames.add(m2)
//        animalNames.add(m3)
//        animalNames.add(m4)
//        animalNames.add(m5)
//        animalNames.add(m6)
        mStatusAdapter = FollowOredrStatusAdapter(context, animalNames)
    }
    //should be removed/////////////////////////////////////////////



    override fun onShowMoreClickListener(isShowMore: Boolean, followOrderItem: FollowOrderItem) {
        val followOrderItems = mFollowOrdersAdapter.data as ArrayList<FollowOrderItem>
        for (i in followOrderItems.indices) {
            if (followOrderItems[i].orderID == followOrderItem.orderID) {
                if (followOrderItems[i].isShowMore != isShowMore) {
                    (mFollowOrdersAdapter.data[i] as FollowOrderItem).isShowMore = isShowMore
                }
            } else {
                (mFollowOrdersAdapter.data[i] as FollowOrderItem).isShowMore = false
            }
        }
        mFollowOrdersAdapter.notifyDataSetChanged()
    }

    override fun onDetailClicked(orderItem: FollowOrderItem) {
        if (orderItem.orderID != 0L) {
            val orderTransactionViewModel: OrderTransactionViewModel = DI.getViewModel(OrderTransactionViewModel::class.java)
            orderTransactionViewModel.setSelectedOrderTransaction(orderItem)
            orderTransactionViewModel.consumeOrderSummaryLiveData()
            orderTransactionViewModel.consumeOrderDetailLiveData()

            orderTransactionViewModel.reNewList()
            openFragment(OrderTransactionDetailFragment.newInstance(true), NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR)
        }
    }
}