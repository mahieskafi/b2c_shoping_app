package com.srp.eways.ui.bill.inquiry;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;

import com.srp.eways.BuildConfig;
import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.bill.BillPaymentResponse;
import com.srp.eways.model.bill.BillPaymentResult;
import com.srp.eways.model.bill.BillPaymentStatusResult;
import com.srp.eways.model.bill.archivedList.BillPaymentDetail;
import com.srp.eways.model.bill.inquiry.BillInquiryResponse;
import com.srp.eways.model.bill.inquiry.TermBill;
import com.srp.eways.model.deposit.Bank;
import com.srp.eways.model.deposit.BankListResponse;
import com.srp.eways.ui.bill.MainBillFragment;
import com.srp.eways.ui.bill.inquiry.views.BillInquiryDetailView;
import com.srp.eways.ui.bill.inquiry.views.SelectBillTypeView;
import com.srp.eways.ui.bill.paymenttype.BillInfo;
import com.srp.eways.ui.bill.paymenttype.BillPaymentTypeViewModel;
import com.srp.eways.ui.bill.receipt.ReceiptFragment;
import com.srp.eways.ui.bill.report.BillReportItemView;
import com.srp.eways.ui.deposit.DepositFragment;
import com.srp.eways.ui.login.UserInfoViewModel;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.ui.navigation.NavigationViewType;
import com.srp.eways.ui.view.LoadingStateView;
import com.srp.eways.ui.view.ViewUtils;
import com.srp.eways.ui.view.bill.BillConfirmationDialog;
import com.srp.eways.ui.view.bill.paymenttype.BillPaymentTypeView;
import com.srp.eways.ui.view.button.ButtonElement;
import com.srp.eways.ui.view.dialog.ConfirmationDialog;
import com.srp.eways.ui.view.input.InputElement;
import com.srp.eways.ui.webview.MainWebFragment;
import com.srp.eways.util.BillUtil;
import com.srp.eways.util.Constants;
import com.srp.eways.util.ToastManager;
import com.srp.eways.util.Utils;
import com.yashoid.inputformatter.InputFormatter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import ir.abmyapp.androidsdk.IABResources;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.srp.eways.network.NetworkResponseCodes.SUCCESS;

public class BillInquiryFragment extends NavigationMemberFragment<BillInquiryViewModel> {


    private IABResources mResources = DIMain.getABResources();

    private SelectBillTypeView mSelectBillTypeView;
    private AppCompatTextView mSelectBillDescription;

    private LinearLayout mNumberContainer;
    private InputElement mPrePhoneInput;
    private InputElement mNumberInput;
    private ButtonElement mInquiryRequestButton;

    private BillInquiryDetailView mDetailContainer;

    private TextWatcher mPrePhoneTextWatcher;
    private TextWatcher mPhoneTextWatcher;
    private TextWatcher mMobileTextWatcher;
    private TextWatcher mNormalTextWatcher;

    private BillPaymentTypeView mBillPaymentView;
    private ScrollView mRootView;

    private UserInfoViewModel mUserInfoViewModel;

    private ArrayList<BillInfo> mBill = new ArrayList<>();

    private MainBillFragment.OnPageChangeListener mPageChangeListener;

    private BillPaymentTypeViewModel billPaymentTypeViewModel = DIMain.getViewModel(BillPaymentTypeViewModel.Companion.getInstance().getClass());

    private Observer<BillPaymentStatusResult> billPaymentStatusResultObserver = new Observer<BillPaymentStatusResult>() {
        @Override
        public void onChanged(BillPaymentStatusResult billPaymentStatusResult) {
            if (billPaymentStatusResult != null) {
                BillPaymentResult billPaymentResult =
                        new BillPaymentResult(billPaymentStatusResult.getBills(), billPaymentStatusResult.getStatus(), 0, 0);
                BillPaymentResponse billPaymentResponse =
                        new BillPaymentResponse(billPaymentResult, "", billPaymentStatusResult.getStatus(), billPaymentStatusResult.getDescription());

                showReceipt(billPaymentResponse);

                getViewModel().consumeBillPaymentStatusResponseLiveLiveData();
            }
        }
    };

    public static BillInquiryFragment newInstance(MainBillFragment.OnPageChangeListener listener) {

        Bundle args = new Bundle();

        BillInquiryFragment fragment = new BillInquiryFragment(listener);
        fragment.setArguments(args);
        return fragment;
    }

    public BillInquiryFragment(MainBillFragment.OnPageChangeListener listener) {
        super();
        mPageChangeListener = listener;
    }

    @Override
    public BillInquiryViewModel acquireViewModel() {
        return DIMain.getViewModel(BillInquiryViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bill_inquiry;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSelectBillTypeView = view.findViewById(R.id.select_bill_type);
        mSelectBillDescription = view.findViewById(R.id.select_bill_description);

        mNumberContainer = view.findViewById(R.id.bill_inquiry_number_container);
        mPrePhoneInput = view.findViewById(R.id.phone_pre_number);
        mNumberInput = view.findViewById(R.id.bill_number);
        mInquiryRequestButton = view.findViewById(R.id.inquiry);

        mDetailContainer = view.findViewById(R.id.detail_container);

        mRootView = view.findViewById(R.id.root_view_bill_inquiry);
        mBillPaymentView = view.findViewById(R.id.bill_payment_view);

        setBillPaymentTypeViewLayoutParams();

        mUserInfoViewModel = DIMain.getViewModel(UserInfoViewModel.Companion.getInstance().getClass());

        getViewModel().consumeData();

        mBillPaymentView.setVisibility(View.GONE);
        mBillPaymentView.setSaveButtonVisibility(View.VISIBLE);
        mBillPaymentView.setSaveButtonEnabled(true);
        mBillPaymentView.setNotEnoughDialogConfirmClickListener(new BillPaymentTypeView.ConfirmNotEnoughDialogClickListener() {

            @Override
            public void onConfirmClicked(long payingPrice, long currentCredit) {
                openFragment(DepositFragment.newInstance(payingPrice - currentCredit), NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);
            }
        });
        mBillPaymentView.setPaymentClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Utils.isInternetAvailable()) {
                    observeToPayment();
                    payment();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.network_error_no_internet), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBillPaymentView.setLoadingRetryClickListener(new LoadingStateView.RetryListener() {
            @Override
            public void onRetryButtonClicked() {
                mBillPaymentView.clearSelectedBank();
                getViewModel().getBankList();

                observeToGetBankList();
            }
        });

        mBillPaymentView.setSaveClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                observeToSaveBill();
                getViewModel().save();
            }

        });

        mSelectBillDescription.setTextColor(mResources.getColor(R.color.bill_inquiry_select_type_description_color));
        mSelectBillDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, mResources.getDimenPixelSize(R.dimen.bill_inquiry_select_type_description_size));
        mSelectBillDescription.setTypeface(ResourcesCompat.getFont(getContext(), R.font.iran_yekan));
        mSelectBillDescription.setText(mResources.getString(R.string.bill_inquiry_select_type_description));


        setupDetailView();

        setupInquiryNumberContainer();

        setupSelectBillType();

        getViewModel().isBillRequestInProgress().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {

                if (isLoading) {

                    mInquiryRequestButton.setLoadingVisibility(VISIBLE);

                    mSelectBillTypeView.setClickable(false);
                    mInquiryRequestButton.setClickable(false);
                    mPrePhoneInput.setEnabled(false);
                    mNumberInput.setEnabled(false);

                    mNumberInput.hasIcon(false);

                } else {
                    mInquiryRequestButton.setLoadingVisibility(INVISIBLE);

                    mSelectBillTypeView.setClickable(true);
                    mInquiryRequestButton.setClickable(true);
                    mPrePhoneInput.setEnabled(true);
                    mNumberInput.setEnabled(true);

                    mNumberInput.hasIcon(true);
                }

            }
        });

        getViewModel().getBillDetails().observe(this, new Observer<BillInquiryResponse>() {
            @Override
            public void onChanged(BillInquiryResponse billInquiryResponse) {

                if (billInquiryResponse != null) {

                    BillType billType = getViewModel().getBillType();

                    if (billInquiryResponse.getBillType() == null) {
                        billInquiryResponse.setBillType(BillUtil.getServiceName(billType));
                    }

                    if (billType == BillType.PHONE || billType == BillType.MOBILE) {
                        mDetailContainer.setVisibility(VISIBLE);
                        ViewUtils.scrollToView(mDetailContainer, mRootView);

                        mDetailContainer.setupInfo(billInquiryResponse, getViewModel().getBillType(),
                                getViewModel().getSelectedTermBill().getValue());
                    } else {

                        BillType realBillType = BillUtil.getBillType(billInquiryResponse.getBillType());

                        if (realBillType != null && realBillType != billType) {

                            String billId = mNumberInput.getText();

                            mSelectBillTypeView.selectBillType(realBillType);

                            mNumberInput.setText(billId);

                            ToastManager.show(getContext(), "قبض وارد شده از نوع " + BillUtil.getServiceName(realBillType) + " می باشد");

                            billType = realBillType;
                        }

                        TermBill termBill = billInquiryResponse.getLastTermBill();
                        getViewModel().setSelectedTermBill(termBill);

                        mBill = new ArrayList<>();
                        mBill.add(new BillInfo(0, BillUtil.getServiceId(billType), Long.valueOf(termBill.getBillId()),
                                Long.valueOf(termBill.getPayId()), termBill.getPrice()));

                        mBillPaymentView.setVisibility(VISIBLE);
                        mBillPaymentView.setInfo(mBill);
                        ViewUtils.scrollToView(mBillPaymentView, mRootView);
                        observeToGetBankList();
                    }
                    getViewModel().consumeBillDetailLiveData();
                }
            }
        });

        getViewModel().getBillRequestError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    ToastManager.show(getContext(), error);
                    getViewModel().consumeError();
                }
            }
        });


        getViewModel().getSelectedTermBill().observe(this, new Observer<TermBill>() {

            @Override
            public void onChanged(TermBill termBill) {

                if (termBill != null) {
                    mBill = new ArrayList<>();
                    mBill.add(new BillInfo(0, BillUtil.getServiceId(getViewModel().getBillType()), Long.valueOf(termBill.getBillId()),
                            Long.valueOf(termBill.getPayId()), termBill.getPrice()));

                    mBillPaymentView.setVisibility(VISIBLE);
                    mBillPaymentView.setInfo(mBill);
                    ViewUtils.scrollToView(mBillPaymentView, mRootView);
                    getViewModel().getBankList();
                    observeToGetBankList();
                }
            }
        });
    }

    private void observeToGetBankList() {

        if (getViewModel().getBankListLiveData().getValue() == null) {
            getViewModel().getBankListLiveData().observe(this, getBankListResponseObserver());

            getViewModel().getBankList();
        } else {

            mBillPaymentView.clearSelectedBank();
            getViewModel().getBankList();
        }

        billPaymentTypeViewModel.getDeepLinkResponseReceivedLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String requestId) {
                if (requestId != null && !requestId.isEmpty() && billPaymentTypeViewModel.getSelectedPage() == Constants.BILL_INQUIRY_PAGE) {
//                    resetView();

                    getViewModel().getPaymentStatus(requestId);
                    getViewModel().getBillPaymentStatusResponseLiveLiveData().observe(BillInquiryFragment.this, billPaymentStatusResultObserver);

                    billPaymentTypeViewModel.setCheckLoading(true);
                    billPaymentTypeViewModel.consumeDeepLinkResponseReceivedLiveData();

                }
            }
        });
    }

    private Observer<BankListResponse> getBankListResponseObserver() {
        return new Observer<BankListResponse>() {
            @Override
            public void onChanged(BankListResponse bankListResponse) {
                if (bankListResponse.getStatus() == SUCCESS) {
                    if (bankListResponse.getItems() != null) {
                        mBillPaymentView.setBankList(bankListResponse.getItems());
                    }
                } else {
                    mBillPaymentView.setBankListError(bankListResponse.getDescription());
                }
            }
        };
    }

    private void observeToSaveBill() {
        getViewModel().getSaveResponseLiveData().observe(this, new Observer<BillPaymentResponse>() {
            @Override
            public void onChanged(BillPaymentResponse billPaymentResponse) {
                if (billPaymentResponse != null) {
                    IABResources resources = DIMain.getABResources();

                    if (billPaymentResponse.getStatus() == Constants.SAVE_BILL_SUCCESS_CODE) {
                        final BillConfirmationDialog dialog = new BillConfirmationDialog(getContext());
                        dialog.setIcon(resources.getDrawable(R.drawable.ic_bill_dialog_smile_mark));
                        dialog.setText(resources.getString(R.string.bill_inquiry_save_success_dialog_text));
                        dialog.setCancelButtonText(resources.getString(R.string.bill_inquiry_save_success_dialog_return_text));
                        dialog.setConfirmButtonText(resources.getString(R.string.bill_inquiry_save_success_dialog_see_list_text));
                        dialog.setClickListener(new ConfirmationDialog.ConfirmationDialogItemClickListener() {
                            @Override
                            public void onConfirmClicked() {
                                dialog.dismiss();
                                mPageChangeListener.onPageChanged(1);
                            }

                            @Override
                            public void onCancelClicked() {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    } else {
                        ToastManager.show(getContext(), billPaymentResponse.getDescription());
                    }
                    getViewModel().consumeSaveResponseLiveData();
                }
            }
        });

        getViewModel().isBillSaveRequestInProgress().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null && aBoolean) {
                    mBillPaymentView.setSaveButtonLoadingVisibility(true);
                } else {
                    mBillPaymentView.setSaveButtonLoadingVisibility(false);
                }
            }

        });
    }

    private void payment() {
        Bank selectedBank = mBillPaymentView.getSelectedBank();


        if (mBillPaymentView.getBillsTotalPrice() <= 0) {

            Toast.makeText(getContext(), "مبلغ پرداختی 0 است.", Toast.LENGTH_LONG).show();
            return;

        }

        if (selectedBank.getGId() == BillUtil.DEPOSIT_GID) {
            if (mBillPaymentView.getBillsTotalPrice() > mUserInfoViewModel.getCreditLiveData().getValue()) {
                mBillPaymentView.showNotEnoughCreditDialog(mUserInfoViewModel.getCreditLiveData().getValue());
            } else {
                mBillPaymentView.setPaymentButtonLoadingVisibility(true);
                getViewModel().pay(selectedBank.getGId());
            }
        } else {
            mBillPaymentView.setPaymentButtonLoadingVisibility(true);
            getViewModel().pay(selectedBank.getGId());
        }

    }

    private void observeToPayment() {

        getViewModel().getPayResponseLiveData().observe(this, new Observer<BillPaymentResponse>() {
            @Override
            public void onChanged(final BillPaymentResponse billPaymentResponse) {
                if (billPaymentResponse != null) {
                    if (billPaymentResponse.getStatus() == SUCCESS) {
                        if (billPaymentResponse.getUrl() != null && !(billPaymentResponse.getUrl().isEmpty())) {

                            billPaymentTypeViewModel.setSelectedPageToPay(Constants.BILL_INQUIRY_PAGE);
                            openFragment(MainWebFragment.Companion.newInstance(BuildConfig.DARGAH_URL + billPaymentResponse.getUrl()), NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);
                        } else {
                            showReceipt(billPaymentResponse);
                            mUserInfoViewModel.invalidateCredit();
                            mUserInfoViewModel.getCredit();
                        }

                    } else if (billPaymentResponse.getResult() != null && billPaymentResponse.getResult().getBills() != null) {
                        showReceipt(billPaymentResponse);
                    } else {
                        ToastManager.show(getContext(), getString(R.string.network_error_undefined));
                    }

                    getViewModel().consumeData();

                }
            }
        });

        getViewModel().isBillPayRequestInProgress().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mBillPaymentView.setPaymentButtonLoadingVisibility(aBoolean);
            }
        });
    }

    private void showReceipt(BillPaymentResponse billPaymentResponse) {
        billPaymentTypeViewModel.setCheckLoading(false);
        ReceiptFragment.BottomButtonsListener listener = new ReceiptFragment.BottomButtonsListener() {
            @Override
            public void onPhoneBackPressed(boolean isPhone) {

                if (isPhone){
                    mPrePhoneInput.setText("");
                    mNumberInput.setText("");
                }
            }

            @Override
            public void onRetryClicked() {

                mPrePhoneInput.setText(mPrePhoneInput.getInputText());
                mNumberInput.setText(mNumberInput.getInputText());

                mInquiryRequestButton.performClick();
//                payment();
            }

            @Override
            public void onMainPageClicked() {
                onBackPressed();
            }

            @Override
            public void onReportClicked() {
                mPageChangeListener.onPageChanged(0);
            }
        };

        ReceiptFragment receiptFragment = ReceiptFragment.newInstance();
        receiptFragment.setBillPaymentResponse(billPaymentResponse);
        receiptFragment.setBottomButtonsListener(listener);
        openFragment(receiptFragment, NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);

        getViewModel().consumeData();
        billPaymentTypeViewModel.consumePayResponseLiveData();

        if (BillUtil.getBillReportStatus(billPaymentResponse.getStatus()) == BillReportItemView.Status.OK) {
            resetView();
        }
    }

    private void setupDetailView() {

        getViewModel().getSaveResponseLiveData().observe(this, new Observer<BillPaymentResponse>() {
            @Override
            public void onChanged(BillPaymentResponse billPaymentResponse) {
                if (billPaymentResponse != null) {

                    IABResources resources = DIMain.getABResources();

                    if (billPaymentResponse.getStatus() == 7) {

                        final BillConfirmationDialog dialog = new BillConfirmationDialog(getContext());

                        dialog.setIcon(resources.getDrawable(R.drawable.ic_bill_dialog_smile_mark));
                        dialog.setText(resources.getString(R.string.bill_inquiry_save_success_dialog_text));
                        dialog.setCancelButtonText(resources.getString(R.string.bill_inquiry_save_success_dialog_return_text));
                        dialog.setConfirmButtonText(resources.getString(R.string.bill_inquiry_save_success_dialog_see_list_text));
                        dialog.setClickListener(new ConfirmationDialog.ConfirmationDialogItemClickListener() {
                            @Override
                            public void onConfirmClicked() {

                                dialog.dismiss();

                                mPageChangeListener.onPageChanged(1);
                            }

                            @Override
                            public void onCancelClicked() {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                    } else {
                        ToastManager.show(getContext(), billPaymentResponse.getDescription());
                    }

                    getViewModel().getSaveResponseLiveData().setValue(null);
                }
            }
        });

        mDetailContainer.setRadioListeners(new BillInquiryDetailView.OnBillChoiceClickListener() {
            @Override
            public void onChoiceClicked(@NotNull TermBill termBill) {
                getViewModel().setSelectedTermBill(termBill);
            }
        });

        getViewModel().isBillSaveRequestInProgress().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {

                mSelectBillTypeView.setClickable(!isLoading);
                mDetailContainer.setDetailEnable(!isLoading);
            }
        });

    }

    private void setupInquiryNumberContainer() {

        mPrePhoneInput.setTextSize(mResources.getDimenPixelSize(R.dimen.bill_inquiry_number_pre_phone_text_size));
        mPrePhoneInput.setTextColor(mResources.getColor(R.color.bill_inquiry_number_text_color));
        mPrePhoneInput.setHintColor(mResources.getColor(R.color.bill_inquiry_number_hint_color));
        mNumberInput.setTypeFace(ResourcesCompat.getFont(getContext(),R.font.iran_yekan));
        mPrePhoneInput.setHint(mResources.getString(R.string.bill_inquiry_number_pre_phone_hint));
        mPrePhoneInput.setUnSelectedBackground(mResources.getDrawable(R.drawable.bill_inquiry_number_pre_phone_background_unselected));
        mPrePhoneInput.setSelectedBackground(mResources.getDrawable(R.drawable.bill_inquiry_number_pre_phone_background_selected));
        mPrePhoneInput.hasGlobalIcon(false);
        mPrePhoneInput.hasIcon(true);
        mPrePhoneInput.setInputType(TYPE_CLASS_NUMBER);
        mPrePhoneInput.setCancelIcon(mResources.getDrawable(R.drawable.ic_survey_close));
        mPrePhoneInput.addTextChangeListener(new InputFormatter(Utils.PersianNumberFormatter));
        mPrePhoneInput.setBackground(mPrePhoneInput.getUnselectedBackground());
        mPrePhoneInput.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!mNumberInput.getText().isEmpty()) {
                        mPrePhoneInput.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
                    } else {
                        mPrePhoneInput.getEditText().setImeOptions(EditorInfo.IME_ACTION_NEXT);
                    }
                }
            }
        });


        mPrePhoneTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String prePhoneNumber = s.toString();

                getViewModel().setPrePhone(prePhoneNumber);

                String prePhoneNumberErrorText = DIMain.getABResources().getString(R.string.input_phonenumber_error);

                if (prePhoneNumber.length() > 3) {
                    mPrePhoneInput.setError(prePhoneNumberErrorText);

                    return;
                }
            }
        };
        mPrePhoneInput.addTextChangeListener(mPrePhoneTextWatcher);


        mNumberInput.setTextSize(mResources.getDimenPixelSize(R.dimen.bill_inquiry_number_input_text_size));
        mNumberInput.setTextColor(mResources.getColor(R.color.bill_inquiry_number_text_color));
        mNumberInput.setHintColor(mResources.getColor(R.color.bill_inquiry_number_hint_color));
        mNumberInput.setTypeFace(ResourcesCompat.getFont(getContext(),R.font.iran_yekan));
        mNumberInput.setHint(mResources.getString(R.string.bill_inquiry_number_phone_hint));
        mNumberInput.setUnSelectedBackground(mResources.getDrawable(R.drawable.bill_inquiry_number_input_background_unselected));
        mNumberInput.setSelectedBackground(mResources.getDrawable(R.drawable.bill_inquiry_number_input_background_selected));
        mNumberInput.hasGlobalIcon(false);
        mNumberInput.hasIcon(true);
        mNumberInput.setInputType(TYPE_CLASS_NUMBER);
        mNumberInput.addTextChangeListener(new InputFormatter(Utils.PersianNumberFormatter));
        mNumberInput.setCancelIcon(mResources.getDrawable(R.drawable.ic_survey_close));
        mNumberInput.setBackground(mNumberInput.getUnselectedBackground());
        mNumberInput.getEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);

        mPhoneTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String phoneNumber = s.toString();

                getViewModel().setMainNumber(phoneNumber);

                String phoneNumberErrorText = DIMain.getABResources().getString(R.string.input_phonenumber_error);

                if (phoneNumber.length() > 8) {
                    mNumberInput.setError(phoneNumberErrorText);

                    return;
                }

                if (phoneNumber.length() == 8) {
                    Utils.hideKeyboard(getActivity());
                }

            }
        };
        mMobileTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String phoneNumber = s.toString();

                getViewModel().setMainNumber(phoneNumber);

                String phoneNumberErrorText = DIMain.getABResources().getString(R.string.input_phonenumber_error);

                if (!Utils.isPhoneNumberLikeString(phoneNumber)) {
                    mNumberInput.setError(phoneNumberErrorText);

                    return;
                }

                if (phoneNumber.length() == 11) {
                    Utils.hideKeyboard(getActivity());
                }

                if (phoneNumber.length() > 11) {
                    mNumberInput.setError(phoneNumberErrorText);

                    return;
                }
            }
        };
        mNormalTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                String billNumber = s.toString();

                getViewModel().setMainNumber(billNumber);

                String billNumberErrorText = DIMain.getABResources().getString(R.string.input_phonenumber_error);

                if (billNumber.length() > 13) {
                    mNumberInput.setError(billNumberErrorText);

                    return;
                }
            }
        };


        mInquiryRequestButton.setText(mResources.getString(R.string.bill_inquiry_request_button_text));
        mInquiryRequestButton.setTextSize(mResources.getDimenPixelSize(R.dimen.bill_inquiry_request_button_text_size));
        mInquiryRequestButton.setTextColor(mResources.getColor(R.color.bill_inquiry_request_button_text_color));
        mInquiryRequestButton.setEnabledBackground(mResources.getDrawable(R.drawable.button_element_default_background_enabled));
        mInquiryRequestButton.setDisableBackground(mResources.getDrawable(R.drawable.button_element_default_background_disabled));
        mInquiryRequestButton.setLoadingColorFilter(mResources.getColor(R.color.bill_inquiry_request_button_text_color));
        mInquiryRequestButton.hasIcon(false);
        mInquiryRequestButton.setEnable(false);

        mInquiryRequestButton.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewModel().getBillInquiryDetails();
            }
        });

        getViewModel().isAllowedToRequest().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean allowed) {
                if (allowed) {
                    mInquiryRequestButton.setEnable(true);
                } else {
                    mInquiryRequestButton.setEnable(false);
                    mBillPaymentView.setVisibility(View.GONE);
                    mDetailContainer.setVisibility(View.GONE);
                }
            }
        });

    }

    private void setNumberContainer(BillType billType) {
        mNumberInput.setText("");
        mNumberInput.setError(null);

        InputFilter[] filterArray = new InputFilter[1];

        switch (billType) {

            case GAS:
            case ELECTRICITY:
            case WATER:
                mNumberContainer.setVisibility(VISIBLE);
                mPrePhoneInput.setText("");
                mPrePhoneInput.setVisibility(View.GONE);
                mNumberInput.setHint(mResources.getString(R.string.bill_inquiry_number_normal_hint));
                mNumberInput.removeTextChangeListener(mPhoneTextWatcher);
                mNumberInput.removeTextChangeListener(mMobileTextWatcher);
                mNumberInput.addTextChangeListener(mNormalTextWatcher);

                filterArray[0] = new InputFilter.LengthFilter(13);
                mNumberInput.getEditText().setFilters(filterArray);

                break;

            case MOBILE:
                mNumberContainer.setVisibility(VISIBLE);
                mPrePhoneInput.setText("");
                mPrePhoneInput.setVisibility(View.GONE);
                mNumberInput.setHint(mResources.getString(R.string.bill_inquiry_number_mobile_hint));
                mNumberInput.removeTextChangeListener(mPhoneTextWatcher);
                mNumberInput.removeTextChangeListener(mNormalTextWatcher);
                mNumberInput.addTextChangeListener(mMobileTextWatcher);


                filterArray[0] = new InputFilter.LengthFilter(11);
                mNumberInput.getEditText().setFilters(filterArray);

                break;
            case PHONE:
                mNumberContainer.setVisibility(VISIBLE);
                mPrePhoneInput.setVisibility(VISIBLE);
                mNumberInput.setHint(mResources.getString(R.string.bill_inquiry_number_phone_hint));
                mNumberInput.removeTextChangeListener(mMobileTextWatcher);
                mNumberInput.removeTextChangeListener(mNormalTextWatcher);
                mNumberInput.addTextChangeListener(mPhoneTextWatcher);

                filterArray[0] = new InputFilter.LengthFilter(8);
                mNumberInput.getEditText().setFilters(filterArray);

                mPrePhoneInput.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});

                break;
        }

        mDetailContainer.setVisibility(View.GONE);

        mNumberContainer.requestLayout();
        mNumberInput.requestLayout();
    }

    private void setupSelectBillType() {

        ViewCompat.setElevation(mSelectBillTypeView, DIMain.getABResources().getDimenPixelSize(R.dimen.bill_inquiry_select_type_view_elevation));

        ArrayList<BillType> types = new ArrayList<>();

        types.add(BillType.GAS);
        types.add(BillType.ELECTRICITY);
        types.add(BillType.WATER);
        types.add(BillType.PHONE);
        types.add(BillType.MOBILE);

        mSelectBillTypeView.setBillTypes(types);

        mSelectBillTypeView.setBillClickListener(new SelectBillTypeView.OnBillItemClickListener() {
            @Override
            public void onItemClicked(@NotNull BillType type) {
                mSelectBillDescription.setVisibility(VISIBLE);
                getViewModel().updateBillType(type);
                setNumberContainer(type);
                mInquiryRequestButton.setVisibility(VISIBLE);
                mBillPaymentView.setVisibility(View.GONE);
            }
        });
    }

    private void resetView() {

        mSelectBillDescription.setVisibility(VISIBLE);

        mPrePhoneInput.setText("");
        mPrePhoneInput.setError(null);
        mDetailContainer.setVisibility(View.GONE);
        mNumberContainer.setVisibility(View.GONE);

        mSelectBillTypeView.unSelectAll();
        mBillPaymentView.setVisibility(View.GONE);

    }

    private void setBillPaymentTypeViewLayoutParams() {
        IABResources abResources = DIMain.getABResources();

        int height = ViewUtils.getDisplayHeight(getContext()) - abResources.getDimenPixelSize(R.dimen.bill_fragment_toolbar_height) -
                abResources.getDimenPixelSize(R.dimen.bill_page_tabbar_height) - abResources.getDimenPixelSize(R.dimen.bottomnav_height);

        LinearLayout.LayoutParams listLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, height);
        mBillPaymentView.setLayoutParams(listLayoutParams);
    }

    private ArrayList<BillPaymentDetail> getBillPaymentDetailList() {
        ArrayList<BillPaymentDetail> billDetailList = new ArrayList<>();

        billDetailList.add(new BillPaymentDetail(mBill.get(0).getId(), String.valueOf(mBill.get(0).getBillId()), String.valueOf(mBill.get(0).getBillPayId()), ""));

        return billDetailList;
    }
}
