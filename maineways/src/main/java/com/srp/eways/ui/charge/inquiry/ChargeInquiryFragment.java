package com.srp.eways.ui.charge.inquiry;


import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;
import com.srp.eways.base.BaseRecyclerAdapter;
import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.transaction.inquiry.InquiryTopup;
import com.srp.eways.model.transaction.inquiry.InquiryTopupNumberResponse;
import com.srp.eways.ui.charge.ChargeViewModel;
import com.srp.eways.ui.transaction.charge.inquiry.InquiryTransactionAdapter;
import com.srp.eways.ui.transaction.charge.inquiry.InquiryTransactionViewModel;
import com.srp.eways.ui.view.button.ButtonElement;
import com.srp.eways.ui.view.input.InputElement;
import com.srp.eways.ui.view.inquiryitem.ChargeInquiryItem;
import com.srp.eways.util.CalendarListener;
import com.srp.eways.util.PersianNumberFormatter;
import com.srp.eways.util.Utils;
import com.srp.eways.ui.navigation.NavigationMemberFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import ir.abmyapp.androidsdk.IABResources;

public class ChargeInquiryFragment extends NavigationMemberFragment<InquiryTransactionViewModel>
        implements BaseRecyclerAdapter.RetryClickListener, InquiryTransactionAdapter.RepeatTransactionClickListener {

    public static ChargeInquiryFragment newInstance() {
        return new ChargeInquiryFragment();
    }

    private IABResources AB;
    private InputElement mPhoneNumberInput;
    private InputElement mStartDateInput;
    private InputElement mEndDateInput;
    private ButtonElement mInquiryButton;
    private RecyclerView mTransactionRecycler;
    private LinearLayout mUnSelectedLayout;
    private RelativeLayout mSelectedLayout;
    private LinearLayout mEntranceLayout;
    private TextView mSelectedDateText;
    private AppCompatImageView mRemoveDateIcon;

    private InquiryTransactionViewModel mViewModel;
    private InquiryTransactionAdapter mAdapter;

    private ChargeViewModel chargeViewModel;

    private PersianCalendar mStartCalendar = new PersianCalendar();
    private PersianCalendar mEndCalendar = new PersianCalendar();

    public ChargeInquiryFragment() {
        // Required empty public constructor
    }

    @Override
    public InquiryTransactionViewModel acquireViewModel() {
        chargeViewModel = DIMain.getViewModel(ChargeViewModel.class);
        return DIMain.getViewModel(InquiryTransactionViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_charge_inquiry;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    private void init() {
        AB = DIMain.getABResources();
        mViewModel = getViewModel();

        mAdapter = new InquiryTransactionAdapter(getContext(), this, this, new ChargeInquiryItem.OnChargeTopUpItemClickListener() {
            @Override
            public void onShowMoreClickListener(boolean isShowMore, @org.jetbrains.annotations.Nullable InquiryTopup inquiryTopup) {
                ArrayList<InquiryTopup> inquiryTopupArrayList = mAdapter.getData();

                for (int i = 0; i < inquiryTopupArrayList.size(); i++) {
                    assert inquiryTopup != null;
                    if (Objects.equals(inquiryTopupArrayList.get(i).getPaymentId(), inquiryTopup.getPaymentId())) {
                        if (inquiryTopupArrayList.get(i).isShowMore() != isShowMore) {
                            mAdapter.getData().get(i).setShowMore(isShowMore);
                        }
                    } else {
                        mAdapter.getData().get(i).setShowMore(false);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPhoneNumberInput = view.findViewById(R.id.in_phone_number);
        mStartDateInput = view.findViewById(R.id.in_start_date);
        mEndDateInput = view.findViewById(R.id.in_end_date);
        mInquiryButton = view.findViewById(R.id.b_inquiry);
        mSelectedLayout = view.findViewById(R.id.v_inquiry_selected_date);
        mUnSelectedLayout = view.findViewById(R.id.v_inquiry_unselected_date);
        mTransactionRecycler = view.findViewById(R.id.rv_transaction);
        mTransactionRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mTransactionRecycler.setAdapter(mAdapter);
        mEntranceLayout = view.findViewById(R.id.v_entrance);
        mSelectedDateText = view.findViewById(R.id.txt_selected_date);
        mRemoveDateIcon = view.findViewById(R.id.remove_date_icon);

        mRemoveDateIcon.setImageDrawable(AB.getDrawable(R.drawable.ic_edit));
        mRemoveDateIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPhoneNumberInput.getEditText().setEnabled(true);
                mStartDateInput.setText("");
                mEndDateInput.setText("");

                mStartCalendar = null;
                mEndCalendar = null;

                removeResultView();
            }
        });

        setupInquiryButton();
        setupPhoneNumberInputText();
        setupDateInputText();
    }

    private void setupPhoneNumberInputText() {
        mPhoneNumberInput.setIconDrawable(AB.getDrawable(R.drawable.ic_mobile));
        mPhoneNumberInput.setTextSize(AB.getDimenPixelSize(R.dimen.charge_inquiry_input_text_size));
        mPhoneNumberInput.setTextColor(AB.getColor(R.color.charge_inquiry_input_text_color));
        mPhoneNumberInput.setHintColor(AB.getColor(R.color.charge_inquiry_input_text_hint_color));
        mPhoneNumberInput.setHint(AB.getString(R.string.charge_inquiry_phone_number_hint));
        mPhoneNumberInput.setUnSelectedBackground(AB.getDrawable(R.drawable.input_text_simple_background));
        mPhoneNumberInput.setSelectedBackground(AB.getDrawable(R.drawable.input_text_simple_background));
        mPhoneNumberInput.hasIcon(AB.getBoolean(R.bool.charge_inquiry_phone_number_has_clear));
        mPhoneNumberInput.setCancelIcon(AB.getDrawable(R.drawable.ic_input_element_cancel));
        mPhoneNumberInput.setBackground(mPhoneNumberInput.getUnselectedBackground());
        mPhoneNumberInput.setImeOption(EditorInfo.IME_ACTION_DONE);
        mPhoneNumberInput.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
//        ViewCompat.setElevation(mPhoneNumberInput, AB.getDimen(R.dimen.charge_inquiry_input_elevation));
        mPhoneNumberInput.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 11) {
                    Utils.hideKeyboard(getActivity());
                }
            }
        });
        mPhoneNumberInput.getCancelImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhoneNumberInput.setText("");
                mPhoneNumberInput.getEditText().setEnabled(true);
                mPhoneNumberInput.getCancelImageView().setVisibility(View.GONE);
                mStartDateInput.setText("");
                mEndDateInput.setText("");

                mStartCalendar = null;
                mEndCalendar = null;

                removeResultView();
            }
        });

        mPhoneNumberInput.getEditText().addTextChangedListener(new PersianNumberFormatter());
        mPhoneNumberInput.getEditText().addTextChangedListener(new onInputDateChangeListener(mPhoneNumberInput));
    }

    private void setupDateInputText() {
        mStartDateInput.setIconDrawable(AB.getDrawable(R.drawable.ic_date));
        mEndDateInput.setIconDrawable(AB.getDrawable(R.drawable.ic_date));
        mStartDateInput.setTextSize(AB.getDimenPixelSize(R.dimen.charge_inquiry_input_text_size));
        mEndDateInput.setTextSize(AB.getDimenPixelSize(R.dimen.charge_inquiry_input_text_size));
        mStartDateInput.setTextColor(AB.getColor(R.color.charge_inquiry_input_text_color));
        mEndDateInput.setTextColor(AB.getColor(R.color.charge_inquiry_input_text_color));
        mStartDateInput.setHintColor(AB.getColor(R.color.charge_inquiry_input_text_hint_color));
        mEndDateInput.setHintColor(AB.getColor(R.color.charge_inquiry_input_text_hint_color));
        mStartDateInput.setHint(AB.getString(R.string.charge_inquiry_start_date_hint));
        mEndDateInput.setHint(AB.getString(R.string.charge_inquiry_end_date_hint));

        mStartDateInput.setUnSelectedBackground(AB.getDrawable(R.drawable.input_text_simple_background));
        mStartDateInput.setSelectedBackground(AB.getDrawable(R.drawable.input_text_simple_background));
        mEndDateInput.setUnSelectedBackground(AB.getDrawable(R.drawable.input_text_simple_background));
        mEndDateInput.setSelectedBackground(AB.getDrawable(R.drawable.input_text_simple_background));
        mStartDateInput.hasIcon(AB.getBoolean(R.bool.charge_inquiry_date_has_clear));
        mEndDateInput.hasIcon(AB.getBoolean(R.bool.charge_inquiry_date_has_clear));
        mStartDateInput.setBackground(mStartDateInput.getUnselectedBackground());
        mEndDateInput.setBackground(mEndDateInput.getUnselectedBackground());
        mStartDateInput.getEditText().setFocusable(false);
        mEndDateInput.getEditText().setFocusable(false);
//        ViewCompat.setElevation(mStartDateInput, AB.getDimen(R.dimen.charge_inquiry_input_elevation));
//        ViewCompat.setElevation(mEndDateInput, AB.getDimen(R.dimen.charge_inquiry_input_elevation));

        mStartDateInput.getEditText().setOnClickListener(new InputStartDateClickListener());
        mStartDateInput.setOnClickListener(new InputStartDateClickListener());
        mEndDateInput.setOnClickListener(new InputEndDateClickListener());
        mEndDateInput.getEditText().setOnClickListener(new InputEndDateClickListener());

        mStartDateInput.getEditText().addTextChangedListener(new onInputDateChangeListener(mStartDateInput));
        mEndDateInput.getEditText().addTextChangedListener(new onInputDateChangeListener(mEndDateInput));
    }

    private void setupInquiryButton() {
        mViewModel.isListLoading().observe(ChargeInquiryFragment.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (mPhoneNumberInput.getText() != null && mPhoneNumberInput.getText() != "") {
                    mAdapter.setIsLoading(aBoolean);
                }
            }
        });
        mViewModel.getErrorCode().observe(ChargeInquiryFragment.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer errorCode) {
                if (mPhoneNumberInput.getText() != null && mPhoneNumberInput.getText() != "") {
                    mAdapter.setErrorCode(errorCode);
                }
            }
        });
        mViewModel.getTransactions().observe(ChargeInquiryFragment.this, new Observer<ArrayList<InquiryTopup>>() {
            @Override
            public void onChanged(ArrayList<InquiryTopup> inquiryTopups) {
                if (mPhoneNumberInput.getText() != null && mPhoneNumberInput.getText() != "") {
                    mAdapter.setNewData(inquiryTopups);
                }
            }
        });


        mInquiryButton.setText(AB.getString(R.string.charge_inquiry_button_text));
        mInquiryButton.setTextSize(AB.getDimenPixelSize(R.dimen.charge_inquiry_button_text_size));
        mInquiryButton.setTextColor(AB.getColor(R.color.charge_inquiry_button_text_color));
        mInquiryButton.setEnabledBackground(AB.getDrawable(R.drawable.button_background_enabled));
        mInquiryButton.setDisableBackground(AB.getDrawable(R.drawable.button_background_disabled));
//        mInquiryButton.getLoading().getIndeterminateDrawable().setColorFilter(AB.getColor(R.color.inquiry_button_text_color), PorterDuff.Mode.SRC_IN);
        mInquiryButton.hasIcon(AB.getBoolean(R.bool.charge_inquiry_button_has_icon));
        mInquiryButton.setEnable(false);
        mInquiryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mViewModel.clearData();
                mAdapter.setIsLoading(true);

                if (!isValidInputText(mPhoneNumberInput)) {
                    mPhoneNumberInput.setError(AB.getString(R.string.charge_inquiry_error_invalid));
                    return;
                }
                if (!isValidInputText(mStartDateInput)) {
                    mStartDateInput.setError(AB.getString(R.string.charge_inquiry_error_invalid));
                    return;
                }

                if (!isValidInputText(mEndDateInput)) {
                    mEndDateInput.setError(AB.getString(R.string.charge_inquiry_error_invalid));
                    return;
                }

                if (!isNetworkConnected()) {
                    observeConnectivity();
                }

                mViewModel.isListLoading().observe(ChargeInquiryFragment.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        mAdapter.setIsLoading(aBoolean);
                    }
                });
                mViewModel.getErrorCode().observe(ChargeInquiryFragment.this, new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer errorCode) {
                        mAdapter.setErrorCode(errorCode);
                    }
                });
                mViewModel.getTransactions().observe(ChargeInquiryFragment.this, new Observer<ArrayList<InquiryTopup>>() {
                    @Override
                    public void onChanged(ArrayList<InquiryTopup> inquiryTopups) {
                        mAdapter.setNewData(inquiryTopups);
                        mTransactionRecycler.setAdapter(mAdapter);
                        setupResultView();
                    }
                });
                setupResultView();

                mViewModel.loadMore(mPhoneNumberInput.getText(), Utils.getDateStringServerFormat(mStartCalendar.getTimeInMillis())
                        , Utils.getDateStringServerFormat(mEndCalendar.getTimeInMillis()));
            }
        });
    }

    private void setupResultView() {
        IABResources resources = DIMain.getABResources();

        mSelectedLayout.setVisibility(View.VISIBLE);
        mUnSelectedLayout.setVisibility(View.GONE);
        mEntranceLayout.setBackground(resources.getDrawable(R.drawable.inquiry_entrance_background));
        mTransactionRecycler.setVisibility(View.VISIBLE);
        mInquiryButton.setVisibility(View.GONE);

        String selectedDateText = resources.getString(R.string.charge_inquiry_selected_date_from_date_text) +
                "  " +
                mStartDateInput.getText() +
                "  " +
                resources.getString(R.string.charge_inquiry_selected_date_to_date_text) +
                "  " +
                mEndDateInput.getText();

        mSelectedDateText.setText(selectedDateText);

        mPhoneNumberInput.getEditText().setEnabled(false);
    }

    private void removeResultView() {
        mSelectedLayout.setVisibility(View.GONE);
        mUnSelectedLayout.setVisibility(View.VISIBLE);
        mEntranceLayout.setBackgroundColor(getResources().getColor(R.color.charge_inquiry_entrance_background_color));
        mTransactionRecycler.setVisibility(View.GONE);
        mInquiryButton.setVisibility(View.VISIBLE);
    }

    private boolean isValidInputText(InputElement inputElement) {
        int phoneNumberId = mPhoneNumberInput.getId();
        int startDateId = mStartDateInput.getId();
        int endDateId = mEndDateInput.getId();

        if (inputElement.getId() == phoneNumberId)
            return !mPhoneNumberInput.getText().isEmpty() &&
                    mPhoneNumberInput.getText().length() == 11 && mPhoneNumberInput.getText().startsWith("09");
        if (inputElement.getId() == startDateId)
            return !mStartDateInput.getText().isEmpty();
        if (inputElement.getId() == endDateId)
            return !mEndDateInput.getText().isEmpty();

        return true;
    }

    @Override
    public void onClicked() {
        mViewModel.loadMore(mPhoneNumberInput.getText(), Utils.getDateStringServerFormat(mStartCalendar.getTimeInMillis())
                , Utils.getDateStringServerFormat(mEndCalendar.getTimeInMillis()));
    }

    @Override
    protected void getDataFromServer() {
        if (!mPhoneNumberInput.getText().isEmpty() && mStartCalendar != null && mEndCalendar != null) {
            mViewModel.loadMore(mPhoneNumberInput.getText(), Utils.getDateStringServerFormat(mStartCalendar.getTimeInMillis())
                    , Utils.getDateStringServerFormat(mEndCalendar.getTimeInMillis()));
            chargeViewModel.setRepeatTransactionPhoneNumber(mPhoneNumberInput.getText());
        }
    }

    @Override
    public void onItemRepeatTransactionClicked(@NotNull Object inquiryTopup) {

        if (chargeViewModel != null) {
            chargeViewModel.setRepeatTransactionPhoneNumber(mPhoneNumberInput.getText());
        }
    }

    private class InputStartDateClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (mStartCalendar == null) {
                mStartCalendar = new PersianCalendar();
            }

            PersianCalendar persianCalendar = new PersianCalendar();
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                    new CalendarListener(mStartDateInput, mStartCalendar),
                    persianCalendar.getPersianYear(),
                    persianCalendar.getPersianMonth(),
                    persianCalendar.getPersianDay()
            );
            if (mEndCalendar != null && !mEndDateInput.getText().isEmpty()) {
                datePickerDialog.setMaxDate(mEndCalendar);
            }

            datePickerDialog.setFirstDayOfWeek(Calendar.SATURDAY);
            datePickerDialog.show(getBaseActivity().getFragmentManager(), "Datepickerdialog");
        }
    }

    private class InputEndDateClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (mEndCalendar == null) {
                mEndCalendar = new PersianCalendar();
            }

            PersianCalendar persianCalendar = new PersianCalendar();
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                    new CalendarListener(mEndDateInput, mEndCalendar),
                    persianCalendar.getPersianYear(),
                    persianCalendar.getPersianMonth(),
                    persianCalendar.getPersianDay()
            );

            if (mStartCalendar != null && !mStartDateInput.getText().isEmpty()) {
                datePickerDialog.setMinDate(mStartCalendar);
            }


            datePickerDialog.setFirstDayOfWeek(Calendar.SATURDAY);
            datePickerDialog.show(getBaseActivity().getFragmentManager(), "Datepickerdialog");
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
            if (!mPhoneNumberInput.getText().isEmpty() &&
                    mPhoneNumberInput.getText().length() == 11 &&
                    isValidInputText(mStartDateInput) &&
                    isValidInputText(mEndDateInput))
                mInquiryButton.setEnable(true);
            else
                mInquiryButton.setEnable(false);

            if (isValidInputText(mSelectedInput))
                mSelectedInput.getEditText().setError(null);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
