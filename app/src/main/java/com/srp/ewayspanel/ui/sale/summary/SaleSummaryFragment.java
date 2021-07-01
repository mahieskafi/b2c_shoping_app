package com.srp.ewayspanel.ui.sale.summary;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.ui.view.toolbar.WeiredToolbar;
import com.srp.ewayspanel.R;
import com.srp.ewayspanel.model.sale.SaleSummaryReportResponse;
import com.srp.eways.ui.view.EmptyView;
import com.srp.eways.ui.view.LoadingStateView;
import com.srp.eways.ui.view.button.ButtonElement;
import com.srp.eways.ui.view.input.InputElement;
import com.srp.ewayspanel.di.DI;
import com.srp.eways.util.CalendarListener;
import com.srp.eways.util.JalaliCalendar;
import com.srp.eways.util.Utils;
import com.srp.ewayspanel.ui.sale.SaleReportViewModel;

import java.util.Calendar;

import ir.abmyapp.androidsdk.IABResources;

import static com.srp.ewayspanel.model.sale.DeliverStatus.ALL_STATUS;
import static com.srp.ewayspanel.model.sale.MainGroupType.ALL_PRODUCT_TYPE;
import static com.srp.eways.network.NetworkResponseCodes.SUCCESS;

/**
 * Created by Eskafi on 1/12/2020.
 */
public class SaleSummaryFragment extends NavigationMemberFragment<SaleReportViewModel> implements LoadingStateView.RetryListener {

    private static SaleSummaryFragment sInstance = null;

    private InputElement mStartDate;
    private InputElement mEndDate;
    private ButtonElement mSearchButton;
    private RecyclerView mRecyclerView;
    private CardView mListContainer;

    private EmptyView mEmptyView;
    private LoadingStateView mLoadingStateView;

    private SaleSummaryAdapter mAdapter;

    private SaleReportViewModel mViewModel;

    private PersianCalendar mStartCalendar = new PersianCalendar();
    private PersianCalendar mEndCalendar = new PersianCalendar();

    private IABResources abResources;

    private Observer<SaleSummaryReportResponse> mSaleReportResponseObserver = new Observer<SaleSummaryReportResponse>() {
        @Override
        public void onChanged(SaleSummaryReportResponse saleResponse) {

            if (saleResponse != null) {
                if (saleResponse.getStatus() == SUCCESS && saleResponse.getBuySummaryItem() != null) {
                    mLoadingStateView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.GONE);

                    mListContainer.setVisibility(View.VISIBLE);

                    mAdapter.setNewData(saleResponse.getBuySummaryItem());

                    mRecyclerView.setAdapter(mAdapter);
                } else if (saleResponse.getStatus() == SUCCESS && saleResponse.getBuySummaryItem() == null) {
                    mLoadingStateView.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);

                    mListContainer.setVisibility(View.GONE);

                }
                mViewModel.consumeSaleReportResponseLiveData();
            }
        }
    };

    private Observer<Boolean> mLoadingObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean isLoading) {
            if (isLoading) {
                mLoadingStateView.setVisibility(View.VISIBLE);
                mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_LOADING,
                        abResources.getString(R.string.loading_message),
                        true);

                mEmptyView.setVisibility(View.GONE);
                mListContainer.setVisibility(View.GONE);
            }
        }
    };

    private Observer<String> mErrorObserver = new Observer<String>() {
        @Override
        public void onChanged(String errorMessage) {
            if (errorMessage != null) {
                mLoadingStateView.setVisibility(View.VISIBLE);
                mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_ERROR,
                        errorMessage,
                        true);

                mEmptyView.setVisibility(View.GONE);
                mListContainer.setVisibility(View.GONE);

                mViewModel.consumeErrorLiveData();
            }
        }
    };

    public static SaleSummaryFragment newInstance() {

        if (sInstance == null) {
            sInstance = new SaleSummaryFragment();
        }
        return sInstance;
    }

    @Override
    public SaleReportViewModel acquireViewModel() {
        mViewModel = DI.getViewModel(SaleReportViewModel.class);
        return mViewModel;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        abResources = DI.getABResources();

        mStartDate = view.findViewById(R.id.start_date);
        mEndDate = view.findViewById(R.id.end_date);
        mSearchButton = view.findViewById(R.id.b_search);
        mRecyclerView = view.findViewById(R.id.rv_summaries);
        mEmptyView = view.findViewById(R.id.emptyview);
        mLoadingStateView = view.findViewById(R.id.loadingstateview);
        mListContainer = view.findViewById(R.id.list_container);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mListContainer.setRadius(abResources.getDimen(R.dimen.sale_summary_list_background_radius));
        mListContainer.setCardElevation(abResources.getDimen(R.dimen.sale_report_input_elevation));

        mAdapter = new SaleSummaryAdapter(getContext());

        mLoadingStateView.setRetryListener(this);

        getCurrentDayReport();

        setupDateInputText();
        setupSearchButton();

        mViewModel.getSaleReportResponseLiveData().observe(getViewLifecycleOwner(), mSaleReportResponseObserver);
        mViewModel.getIsLoadingLiveData().observe(getViewLifecycleOwner(), mLoadingObserver);
        mViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), mErrorObserver);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sale_report;
    }


    private void setupDateInputText() {

        IABResources AB = DI.getABResources();
        String currentDay = getGorgianToPersianFormattedDate(JalaliCalendar.getInstance().get(JalaliCalendar.YEAR), JalaliCalendar.getInstance().get(JalaliCalendar.MONTH), JalaliCalendar.getInstance().get(JalaliCalendar.DAY_OF_MONTH));

        mStartDate.setIconDrawable(AB.getDrawable(R.drawable.ic_date));
        mEndDate.setIconDrawable(AB.getDrawable(R.drawable.ic_date));

        mStartDate.setTextSize(AB.getDimenPixelSize(R.dimen.sale_report_input_text_size));
        mEndDate.setTextSize(AB.getDimenPixelSize(R.dimen.sale_report_input_text_size));

        mStartDate.setTextColor(AB.getColor(R.color.sale_report_input_text_color));
        mEndDate.setTextColor(AB.getColor(R.color.sale_report_input_text_color));

        mStartDate.setHintColor(AB.getColor(R.color.sale_report_input_text_hint_color));
        mEndDate.setHintColor(AB.getColor(R.color.sale_report_input_text_hint_color));

        mStartDate.setHint(AB.getString(R.string.sale_report_start_date_hint));
        mEndDate.setHint(AB.getString(R.string.sale_report_end_date_hint));

        mStartDate.setUnSelectedBackground(AB.getDrawable(R.drawable.input_text_simple_background));
        mStartDate.setSelectedBackground(AB.getDrawable(R.drawable.input_text_simple_background));

        mEndDate.setUnSelectedBackground(AB.getDrawable(R.drawable.input_text_simple_background));
        mEndDate.setSelectedBackground(AB.getDrawable(R.drawable.input_text_simple_background));

        mStartDate.hasIcon(AB.getBoolean(R.bool.sale_report_date_has_clear));
        mEndDate.hasIcon(AB.getBoolean(R.bool.sale_report_date_has_clear));

        mStartDate.setBackground(mStartDate.getUnselectedBackground());
        mEndDate.setBackground(mEndDate.getUnselectedBackground());

        mStartDate.getEditText().setFocusable(false);
        mEndDate.getEditText().setFocusable(false);

//        ViewCompat.setElevation(mStartDate, AB.getDimen(R.dimen.sale_report_input_elevation));
//        ViewCompat.setElevation(mEndDate, AB.getDimen(R.dimen.sale_report_input_elevation));

        mStartDate.setText(currentDay);
        mEndDate.setText(currentDay);

        mStartDate.getEditText().setOnClickListener(new InputStartDateClickListener());
        mStartDate.setOnClickListener(new InputStartDateClickListener());

        mStartDate.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEndDate.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mEndDate.setOnClickListener(new InputEndDateClickListener());
        mEndDate.getEditText().setOnClickListener(new InputEndDateClickListener());

        mStartDate.getEditText().addTextChangedListener(new onInputDateChangeListener(mStartDate));
        mEndDate.getEditText().addTextChangedListener(new onInputDateChangeListener(mEndDate));
    }

    private void setupSearchButton() {
        IABResources AB = DI.getABResources();

        mSearchButton.setText(AB.getString(R.string.sale_report_search_button_title));
        mSearchButton.setTextSize(AB.getDimenPixelSize(R.dimen.sale_report_button_text_size));
        mSearchButton.setTextColor(AB.getColor(R.color.sale_report_button_text_color));
        mSearchButton.setEnabledBackground(AB.getDrawable(R.drawable.button_background_enabled));
        mSearchButton.setDisableBackground(AB.getDrawable(R.drawable.button_background_disabled));
        mSearchButton.hasIcon(AB.getBoolean(R.bool.sale_report_button_has_icon));
        mSearchButton.setEnable(false);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mViewModel.getSaleSummaryReport(ALL_PRODUCT_TYPE, ALL_STATUS, mStartDate.getText(), mEndDate.getText());

            }
        });

    }

    @Override
    public void onRetryButtonClicked() {
        mViewModel.getSaleSummaryReport(ALL_PRODUCT_TYPE, ALL_STATUS, mStartDate.getText(), mEndDate.getText());
    }

    private class InputStartDateClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (mStartCalendar == null) {
                mStartCalendar = new PersianCalendar();
            }

            PersianCalendar persianCalendar = new PersianCalendar();
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                    new CalendarListener(mStartDate, mStartCalendar),
                    persianCalendar.getPersianYear(),
                    persianCalendar.getPersianMonth(),
                    persianCalendar.getPersianDay()
            );

            datePickerDialog.setFirstDayOfWeek(Calendar.SATURDAY);
            datePickerDialog.show(getBaseActivity().getFragmentManager(), "Datepickerdialog");
        }
    }

    private class InputEndDateClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mStartCalendar == null && mStartDate.getText().isEmpty()) {
                mStartDate.setError(getString(R.string.sale_report_stat_date_input_error));
            } else {
                PersianCalendar persianCalendar = new PersianCalendar();
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        new CalendarListener(mEndDate, mEndCalendar),
                        persianCalendar.getPersianYear(),
                        persianCalendar.getPersianMonth(),
                        persianCalendar.getPersianDay()
                );


                datePickerDialog.setMinDate(mStartCalendar);

                PersianCalendar maxCalendar = new PersianCalendar();
                maxCalendar.setPersianDate(mStartCalendar.getPersianYear(), mStartCalendar.getPersianMonth(), mStartCalendar.getPersianDay() + 9);

                datePickerDialog.setMaxDate(maxCalendar);

                datePickerDialog.setFirstDayOfWeek(Calendar.SATURDAY);
                datePickerDialog.show(getBaseActivity().getFragmentManager(), "Datepickerdialog");
            }
        }
    }

    private class onInputDateChangeListener implements TextWatcher {

        private InputElement mSelectedInput;

        public onInputDateChangeListener(InputElement mSelectedInput) {
            this.mSelectedInput = mSelectedInput;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!mStartDate.getText().isEmpty() &&
                    !mEndDate.getText().isEmpty())
                mSearchButton.setEnable(true);
            else
                mSearchButton.setEnable(false);

            if (isValidInputText(mSelectedInput))
                mSelectedInput.getEditText().setError(null);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private boolean isValidInputText(InputElement inputElement) {
        int startDateId = mStartDate.getId();
        int endDateId = mEndDate.getId();

        if (inputElement.getId() == startDateId)
            return !mStartDate.getText().isEmpty();
        if (inputElement.getId() == endDateId)
            return !mEndDate.getText().isEmpty();

        return true;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {

        if (!hidden) {
            mStartCalendar = null;
            mEndCalendar = new PersianCalendar();

            mStartDate.setText("");
            mEndDate.setText("");
            mViewModel.consumeSaleReportResponseLiveData();

            mListContainer.setVisibility(View.GONE);
            mLoadingStateView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.GONE);

            getCurrentDayReport();
        }

    }

    private void getCurrentDayReport() {
        String currentDay = getGorgianToPersianFormattedDate(JalaliCalendar.getInstance().get(JalaliCalendar.YEAR), JalaliCalendar.getInstance().get(JalaliCalendar.MONTH), JalaliCalendar.getInstance().get(JalaliCalendar.DAY_OF_MONTH));
        mViewModel.getSaleSummaryReport(ALL_PRODUCT_TYPE, ALL_STATUS, currentDay, currentDay);
    }

    private String getGorgianToPersianFormattedDate(int year, int monthOfYear, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        PersianCalendar persianCalendar = new PersianCalendar();
        persianCalendar.setTimeInMillis(calendar.getTimeInMillis());

        return Utils.toPersianNumber(persianCalendar.getPersianYear() + "/" + getMonth(persianCalendar.getPersianMonth()) + "/" + persianCalendar.getPersianDay());
    }

    private String getMonth(int monthOfYear) {
        int monthString = monthOfYear + 1;

        return String.valueOf(monthString);
    }
}
