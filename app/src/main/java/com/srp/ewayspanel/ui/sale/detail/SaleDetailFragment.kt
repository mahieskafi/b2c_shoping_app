package com.srp.ewayspanel.ui.sale.detail

import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar
import com.srp.eways.base.BasePageableListFragment
import com.srp.eways.ui.IContentLoadingStateManager
import com.srp.eways.ui.view.LoadingStateView
import com.srp.eways.ui.view.button.ButtonElement
import com.srp.eways.ui.view.input.InputElement
import com.srp.eways.util.CalendarListener
import com.srp.ewayspanel.R
import com.srp.ewayspanel.di.DI
import com.srp.ewayspanel.model.sale.DeliverStatus
import com.srp.ewayspanel.model.sale.MainGroupType
import com.srp.ewayspanel.model.sale.detail.SaleDetailReportItem
import com.srp.ewayspanel.ui.sale.SaleReportViewModel
import java.util.*

class SaleDetailFragment : BasePageableListFragment<SaleReportViewModel>(), SaleDetailListAdapter.ActionsListener,
        BasePageableListFragment.OnRecyclereScrollListener {

    companion object {
        @JvmStatic
        fun newInstance(): SaleDetailFragment {
            return SaleDetailFragment()
        }
    }

    private lateinit var mStartDateInput: InputElement
    private lateinit var mEndDateInput: InputElement
    private lateinit var mSearchButton: ButtonElement

    var mStartCalendarDetail: PersianCalendar? = null
    var mEndCalendarDeatil: PersianCalendar? = null

    override fun acquireViewModel(): SaleReportViewModel {
        return DI.getViewModel(SaleReportViewModel::class.java)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_sale_detail
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mStartDateInput = view.findViewById(R.id.txt_start_date)
        mEndDateInput = view.findViewById(R.id.txt_end_date)
        mSearchButton = view.findViewById(R.id.b_confirm)
        mRecyclerView = view.findViewById(R.id.rv_details)
        mEmptyView = view.findViewById(R.id.emptyview)
        mLoadingStateView = view.findViewById(R.id.loadingstateview)


        mAdapter = SaleDetailListAdapter(context!!, this@SaleDetailFragment)


        setupDateInputText()
        setupSearchButton()

        viewModel.saleDetailReportResponseLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null && mAdapter != null) {

                checkDataStatus(it.saleList, it.status)
                if (mAdapter.data.size <= 10) {
                    mRecyclerView.adapter = mAdapter
                }
                viewModel.consumeSaleDetailReportResponseLiveData()
            }
        })

        setLoadingRetryListener {
            setLoadingState(LoadingStateView.STATE_LOADING)
            loadMoreItems()
        }
    }

    private fun setupDateInputText() {
        val abResources = DI.getABResources()

        mStartDateInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_date))
        mEndDateInput.setIconDrawable(abResources.getDrawable(R.drawable.ic_date))
        mStartDateInput.setTextSize(abResources.getDimenPixelSize(R.dimen.sale_report_input_text_size).toFloat())
        mEndDateInput.setTextSize(abResources.getDimenPixelSize(R.dimen.sale_report_input_text_size).toFloat())
        mStartDateInput.setTextColor(abResources.getColor(R.color.sale_report_input_text_color))
        mEndDateInput.setTextColor(abResources.getColor(R.color.sale_report_input_text_color))
        mStartDateInput.setHintColor(abResources.getColor(R.color.sale_report_input_text_hint_color))
        mEndDateInput.setHintColor(abResources.getColor(R.color.sale_report_input_text_hint_color))
        mStartDateInput.setHint(abResources.getString(R.string.sale_report_start_date_hint))
        mEndDateInput.setHint(abResources.getString(R.string.sale_report_end_date_hint))
        mStartDateInput.setUnSelectedBackground(abResources.getDrawable(R.drawable.input_text_simple_background))
        mStartDateInput.setSelectedBackground(abResources.getDrawable(R.drawable.input_text_simple_background))
        mEndDateInput.setUnSelectedBackground(abResources.getDrawable(R.drawable.input_text_simple_background))
        mEndDateInput.setSelectedBackground(abResources.getDrawable(R.drawable.input_text_simple_background))
        mStartDateInput.hasIcon(abResources.getBoolean(R.bool.sale_report_date_has_clear))
        mEndDateInput.hasIcon(abResources.getBoolean(R.bool.sale_report_date_has_clear))
        mStartDateInput.background = mStartDateInput.getUnselectedBackground()
        mEndDateInput.background = mEndDateInput.getUnselectedBackground()
        mStartDateInput.getEditText().isFocusable = false
        mEndDateInput.getEditText().isFocusable = false

        mStartDateInput.getEditText().setOnClickListener(InputStartDateClickListener())
        mStartDateInput.setOnClickListener(InputStartDateClickListener())
        mEndDateInput.setOnClickListener(InputEndDateClickListener())
        mEndDateInput.getEditText().setOnClickListener(InputEndDateClickListener())
        mStartDateInput.getEditText().addTextChangedListener(onInputDateChangeListener(mStartDateInput))
        mEndDateInput.getEditText().addTextChangedListener(onInputDateChangeListener(mEndDateInput))
        mStartDateInput.addTextChangeListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                mEndDateInput.setText("")
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

    }

    private fun setupSearchButton() {
        val abResources = DI.getABResources()

        mSearchButton.setText(abResources.getString(R.string.sale_report_search_button_title))
        mSearchButton.setTextSize(abResources.getDimenPixelSize(R.dimen.sale_report_button_text_size).toFloat())
        mSearchButton.setTextColor(abResources.getColor(R.color.sale_report_button_text_color))
        mSearchButton.setEnabledBackground(abResources.getDrawable(R.drawable.button_background_enabled))
        mSearchButton.setDisableBackground(abResources.getDrawable(R.drawable.button_background_disabled))
        mSearchButton.hasIcon(abResources.getBoolean(R.bool.sale_report_button_has_icon))
        mSearchButton.setEnable(false)
        mSearchButton.setOnClickListener {
            if (!isNetworkConnected) {
                setLoadingState(IContentLoadingStateManager.VIEWSTATE_NO_INTERNET_ERROR)
            } else {
                setLoadingState(IContentLoadingStateManager.VIEWSTATE_SHOW_LOADING)
                viewModel.resetDetailList()
                mAdapter.clearData()
                mRecyclerView.layoutManager = LinearLayoutManager(context)
                mRecyclerView.clearOnScrollListeners()
                setPaginationScrollListener(this)
                loadMoreItems()
            }
        }
    }

    private inner class InputStartDateClickListener : View.OnClickListener {
        override fun onClick(v: View) {

//            mEndDateInput.setText("")

            if (mStartCalendarDetail == null) {
                mStartCalendarDetail = PersianCalendar()
            }
            val persianCalendar = PersianCalendar()
            val datePickerDialog = DatePickerDialog.newInstance(
                    CalendarListener(mStartDateInput, mStartCalendarDetail),
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
            if (mStartCalendarDetail == null && mStartDateInput.getText().isEmpty()) {
                mStartDateInput.setError(getString(R.string.sale_report_stat_date_input_error))
            } else {
                if (mEndCalendarDeatil == null) {
                    mEndCalendarDeatil = PersianCalendar()
                }
                val persianCalendar = PersianCalendar()
                val datePickerDialog = DatePickerDialog.newInstance(
                        CalendarListener(mEndDateInput, mEndCalendarDeatil),
                        persianCalendar.persianYear,
                        persianCalendar.persianMonth,
                        persianCalendar.persianDay
                )
                datePickerDialog.minDate = mStartCalendarDetail
                val maxCalendar = PersianCalendar()
                maxCalendar.setPersianDate(mStartCalendarDetail!!.persianYear, mStartCalendarDetail!!.persianMonth, mStartCalendarDetail!!.persianDay + 9)
                datePickerDialog.maxDate = maxCalendar
                datePickerDialog.firstDayOfWeek = Calendar.SATURDAY
                datePickerDialog.show(baseActivity.fragmentManager, "Datepickerdialog")
            }
        }
    }

    private inner class onInputDateChangeListener(private val mSelectedInput: InputElement) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (mStartDateInput.getText().isNotEmpty() && mEndDateInput.getText().isNotEmpty()) {
                mSearchButton.setEnable(true)
                mLoadingStateView.visibility = View.GONE
            } else {
                mSearchButton.setEnable(false)
            }
            if (isValidInputText(mSelectedInput)) mSelectedInput.getEditText().error = null
        }

        override fun afterTextChanged(s: Editable) {}

    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            mStartCalendarDetail = null
            mEndCalendarDeatil = PersianCalendar()
            mStartDateInput.setText("")
            mEndDateInput.setText("")
        }
    }

    private fun isValidInputText(inputElement: InputElement): Boolean {
        val startDateId: Int = mStartDateInput.id
        val endDateId: Int = mEndDateInput.id
        if (inputElement.id == startDateId) return mStartDateInput.getText().isNotEmpty()
        return if (inputElement.id == endDateId) mEndDateInput.getText().isNotEmpty() else true
    }

    override fun onShowMoreClickListener(isShowMore: Boolean, saleDetailReportItem: SaleDetailReportItem) {

        val saleDetailList = mAdapter.data as ArrayList<SaleDetailReportItem>
        for (i in saleDetailList.indices) {
            if (saleDetailList[i].id == saleDetailReportItem.id) {
                if (saleDetailList[i].isShowMore != isShowMore) {
                    (mAdapter.data[i] as SaleDetailReportItem).isShowMore = isShowMore
                }
            } else {
                (mAdapter.data[i] as SaleDetailReportItem).isShowMore = false
            }
        }
        mAdapter.notifyDataSetChanged()
    }

    override fun hasMoreItems(): Boolean {
        return if (viewModel.hasMoreDetailList() != null) {
            viewModel.hasMoreDetailList()
        } else {
            true
        }
    }

    override fun loadMoreItems() {
        viewModel.getSaleDetailReport(MainGroupType.ALL_PRODUCT_TYPE, DeliverStatus.ALL_STATUS, mStartDateInput.getText(), mEndDateInput.getText())
    }

}