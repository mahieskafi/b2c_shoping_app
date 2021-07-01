package com.srp.eways.ui.deposit.increasedeposit;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.srp.eways.model.deposit.ConfirmPaymentResponse;
import com.srp.eways.model.deposit.MPLResponse;
import com.srp.eways.model.deposit.MPLTokenResponse;
import com.srp.eways.ui.bill.paymenttype.BankAdapter;
import com.srp.eways.ui.bill.paymenttype.BillPaymentTypeFragment;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.ui.navigation.NavigationViewType;
import com.srp.eways.ui.view.LoadingStateView;
import com.srp.eways.ui.view.dialog.BankListDialog;
import com.srp.eways.ui.view.receipt.Receipt;
import com.srp.eways.ui.view.receipt.ReceiptItem;
import com.srp.eways.BuildConfig;
import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.deposit.Bank;
import com.srp.eways.model.deposit.BankListResponse;
import com.srp.eways.model.deposit.IncreaseDepositResponse;
import com.srp.eways.model.deposit.IncreaseDepositStatusResponse;
import com.srp.eways.ui.login.UserInfoViewModel;
import com.srp.eways.ui.receipt.ReceiptFragment;
import com.srp.eways.ui.view.button.ButtonElement;
import com.srp.eways.ui.view.input.InputElement;
import com.srp.eways.ui.webview.MainWebFragment;
import com.srp.eways.util.BillUtil;
import com.srp.eways.util.Constants;
import com.srp.eways.util.NumberInputTextFilter;
import com.srp.eways.util.PersianPriceFormatter;
import com.srp.eways.util.Utils;
import com.srp.eways.util.analytic.AnalyticConstant;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;
//import ir.pec.mpl.pecpayment.view.PaymentInitiator;


/**
 * Created by Eskafi on 9/1/2019.
 */
public class IncreaseDepositFragment extends NavigationMemberFragment<IncreaseDepositViewModel> implements AmountAdapter.ItemClickListener {

    private static IncreaseDepositFragment sIncreaseDepositFragmentInstance;

    public static final String AMOUNT_OF_INCREASE = "amount_of_increase";
    private static final long MAX_AMOUNT = 50000000;

    private static final int REQUEST_CODE_MPL_TRANSACTION = 1;
    private static final int RESULT_CODE_MPL_TRANSACTION_DONE = 1;
    private static final int RESULT_CODE_MPL_TRANSACTION_CANCELED = 2;
    private static final int RESULT_CODE_MPL_TRANSACTION_ERROR = 5;

    private static final String[] amounts = {"10000", "100000", "200000", "500000", "1000000"};

    public static IncreaseDepositFragment newInstance() {
        if (sIncreaseDepositFragmentInstance == null) {
            sIncreaseDepositFragmentInstance = new IncreaseDepositFragment();
        }
        return sIncreaseDepositFragmentInstance;
    }

    public static IncreaseDepositFragment newInstance(long amountOfIncrease) {
        if (sIncreaseDepositFragmentInstance == null) {
            sIncreaseDepositFragmentInstance = new IncreaseDepositFragment();
        }

        Bundle args = new Bundle();
        args.putSerializable(AMOUNT_OF_INCREASE, amountOfIncrease);

        sIncreaseDepositFragmentInstance.setArguments(args);
        return sIncreaseDepositFragmentInstance;
    }

    private IncreaseDepositViewModel mViewModel;
    private UserInfoViewModel mUserInfoViewModel;
    private IABResources AB;

    private ButtonElement mPaymentButton;
    private InputElement mAmountInput;
    private Observer<BankListResponse> mBankListObserver;

    private CardView mBankCard;
    private BankAdapter mBankAdapter;
    private RecyclerView mBankRecycler;
    private LoadingStateView mBankLoadingStateView;

    private Long mDeposit;

    private Observer<MPLTokenResponse> mMplTokenObserver;

    Observer<IncreaseDepositStatusResponse> mStatusObserver = new Observer<IncreaseDepositStatusResponse>() {
        @Override
        public void onChanged(IncreaseDepositStatusResponse statusResponse) {
            if (statusResponse != null) {
                if (statusResponse.getStatus() == 0) {

                    mUserInfoViewModel.onCreditIncreased(true);

                    mAmountInput.setText("");

                } else {
                    mUserInfoViewModel.onCreditIncreased(false);

                    mAmountInput.setText("");

                }

                mBankCard.setVisibility(View.GONE);

                mViewModel.consumeAmountLiveData();

                showReceiptView(statusResponse);

                mViewModel.consumeStatusLiveData();
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAmountInput = view.findViewById(R.id.ie_amount);
        mPaymentButton = view.findViewById(R.id.btn_payment);
        RecyclerView recyclerView = view.findViewById(R.id.rv_amount);
        mBankCard = view.findViewById(R.id.bank_card);
        mBankRecycler = view.findViewById(R.id.rv_bank);
        mBankLoadingStateView = view.findViewById(R.id.loadingstateview);
        mBankLoadingStateView.setViewOrientation(LinearLayout.HORIZONTAL);

        setupInputText();
        setupPaymentButton();

        int numberOfColumns = 2;

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns, RecyclerView.HORIZONTAL, true));
        AmountAdapter adapter = new AmountAdapter(getContext(), amounts, this);
        recyclerView.setAdapter(adapter);

        DIMain.getViewModel(UserInfoViewModel.class).getCreditLiveData().observe(this, getGetCreditObserver());

        mViewModel.getIncreaseDepositStatusLiveData().observe(this, mStatusObserver);

        if (getArguments() != null) {
            long amountOfIncrease = getArguments().getLong(AMOUNT_OF_INCREASE);

            mAmountInput.setText(String.valueOf(amountOfIncrease));
        }

        mBankRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, true));
        mBankAdapter = new BankAdapter(new BillPaymentTypeFragment.BankItemSelectedListener() {
            @Override
            public void onBankItemSelected(@NotNull Bank bank) {
                if (mAmountInput.getText().length() > 1) {
                    mPaymentButton.setEnable(true);
                }
                mViewModel.setBankId(bank.getGId());
            }
        });
        mBankRecycler.setAdapter(mBankAdapter);

        mViewModel.getIsClearData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null) {
                    if (aBoolean) {
                        mViewModel.consumeBankListLiveData();
                        mViewModel.getBankListLiveData().observe(getViewLifecycleOwner(), getBankListObserver());
                        mViewModel.getAmountValidate().observe(getViewLifecycleOwner(), getGetAmountValidateObserver());
                        if (getArguments() != null) {
                            long amountOfIncrease = getArguments().getLong(AMOUNT_OF_INCREASE);

                            mAmountInput.setText(String.valueOf(amountOfIncrease));
                        }
                    } else {
                        setArguments(null);
                        mAmountInput.clearText();
                        mViewModel.getBankListLiveData().removeObservers(getViewLifecycleOwner());
                        mViewModel.getAmountValidate().removeObservers(getViewLifecycleOwner());
                    }
                    mViewModel.consumeClearData();
                }
            }
        });
    }

    private void init() {
        mViewModel = getViewModel();
        mUserInfoViewModel = DIMain.getViewModel(UserInfoViewModel.class);

        AB = DIMain.getABResources();

        mViewModel.isLoading().observe(this, new Observer<Boolean>() {

            @Override
            public void onChanged(Boolean loading) {
                if (loading != null) {
//                    mPaymentButton.setEnable(!loading);
                    mAmountInput.setEnabled(!loading);
                }
            }

        });
    }

    private void setupInputText() {
        mAmountInput.setIconDrawable(AB.getDrawable(R.drawable.ic_deposit_amount));
        mAmountInput.setTextSize(AB.getDimenPixelSize(R.dimen.increase_deposit_input_text_size));
        mAmountInput.setTextColor(AB.getColor(R.color.increase_deposit_input_text_color));
        mAmountInput.setHintColor(AB.getColor(R.color.increase_deposit_input_text_hint_color));
        mAmountInput.setHint(AB.getString(R.string.deposit_amount_hint));
        mAmountInput.setUnSelectedBackground(AB.getDrawable(R.drawable.input_text_increase_credit_background));
        mAmountInput.setSelectedBackground(AB.getDrawable(R.drawable.input_text_increase_credit_background));
        mAmountInput.hasIcon(AB.getBoolean(R.bool.deposit_amount_has_clear));
        mAmountInput.setCancelIcon(AB.getDrawable(R.drawable.ic_input_element_cancel));
        mAmountInput.setBackground(mAmountInput.getUnselectedBackground());
        mAmountInput.hasDescription(true);
        mAmountInput.setDescriptionSize(AB.getDimenPixelSize(R.dimen.increase_deposit_rial_text_size));
        mAmountInput.setDescription(AB.getString(R.string.deposit_rial));
        mAmountInput.setImeOption(EditorInfo.IME_ACTION_DONE);

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(9);
        mAmountInput.getEditText().setFilters(new InputFilter[]{new NumberInputTextFilter("1000", String.valueOf(MAX_AMOUNT))});
        mAmountInput.getEditText().setFilters(filterArray);

        ViewCompat.setElevation(mAmountInput, AB.getDimen(R.dimen.increase_deposit_amount_input_elevation));

        mViewModel.getAmountValidate().observe(IncreaseDepositFragment.this, getGetAmountValidateObserver());

        mAmountInput.addTextChangeListener(new PersianPriceFormatter(","));
        mAmountInput.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String amount = mAmountInput.getInputText();
                if (!amount.isEmpty()) {
                    mViewModel.onAmountChanged(Long.valueOf(amount.replaceAll("،", "")));
                } else {
                    mBankCard.setVisibility(View.GONE);
                    mPaymentButton.setEnable(false);
                }
                mAmountInput.getEditText().setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setupPaymentButton() {
        mPaymentButton.setText(AB.getString(R.string.deposit_payment));
        mPaymentButton.setTextSize(AB.getDimenPixelSize(R.dimen.increase_deposit_button_payment_text_size));
        mPaymentButton.setTextColor(AB.getColor(R.color.increase_deposit_button_text_color));
        mPaymentButton.setEnabledBackground(AB.getDrawable(R.drawable.login_button_background_enabled));
        mPaymentButton.setDisableBackground(AB.getDrawable(R.drawable.login_button_background_disabled));
        mPaymentButton.setLoadingColorFilter(AB.getColor(R.color.increase_deposit_button_text_color));
        mPaymentButton.hasIcon(AB.getBoolean(R.bool.deposit_button_payment_has_icon));
        mPaymentButton.setEnable(false);
        mPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DIMain.getEventSender().sendAction(AnalyticConstant.DEPOSIT, AnalyticConstant.INCREASE_DEPOSIT_ACTION);

                mViewModel.getIncreaseDepositLiveData().observe(IncreaseDepositFragment.this, getIncreaseDepositObserver());
                mViewModel.increaseDeposit();

            }
        });
    }

    public Observer<BankListResponse> getBankListObserver() {
        mBankListObserver = new Observer<BankListResponse>() {
            @Override
            public void onChanged(BankListResponse bankListResponse) {
                if (bankListResponse != null) {
                    if (bankListResponse.getStatus() == 0 && bankListResponse.getItems() != null && bankListResponse.getItems().size() > 0) {
                        mBankCard.setVisibility(View.VISIBLE);
                        mBankLoadingStateView.setVisibility(View.GONE);
                        mBankRecycler.setVisibility(View.VISIBLE);

                        List<Bank> validBankList = new ArrayList<>();

                        for (int i = 0; i < bankListResponse.getItems().size(); i++) {
                            Bank bank = bankListResponse.getItems().get(i);

                            if (BillUtil.getBankName(bank.getGId()) != 0) {
                                bank.setSelcted(false);
                                validBankList.add(bank);
                            }
                        }
                        mBankAdapter.setData(validBankList);
                        mBankAdapter.notifyDataSetChanged();
                    } else {
                        mBankCard.setVisibility(View.VISIBLE);
                        mBankLoadingStateView.setVisibility(View.VISIBLE);
                        mBankLoadingStateView.setStateAndDescription(LoadingStateView.STATE_ERROR, DIMain.getABResources().getString(R.string.network_error_undefined), false);

                        mBankRecycler.setVisibility(View.GONE);
                    }
                }
            }
        };

        return mBankListObserver;
    }

    public Observer<IncreaseDepositResponse> getIncreaseDepositObserver() {
        return new Observer<IncreaseDepositResponse>() {
            @Override
            public void onChanged(IncreaseDepositResponse increaseDepositResponse) {
                if (increaseDepositResponse != null) {
                    if (increaseDepositResponse.getStatus() == 0 && !(increaseDepositResponse.getUrl().isEmpty())) {
                        mViewModel.setLoading(true);
                        openFragment(MainWebFragment.Companion.newInstance(BuildConfig.DARGAH_URL + increaseDepositResponse.getUrl()), NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);

                    } else {
                        Toast.makeText(getContext(), increaseDepositResponse.getDescription(), Toast.LENGTH_LONG).show();
                    }

                    mViewModel.consumeAmountLiveData();
                    mViewModel.consumeStatusLiveData();
                    mViewModel.consumeIncreaseDepositResponseLiveData();
                }
            }
        };
    }


    private Observer<Boolean> getGetAmountValidateObserver() {
        return new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null) {
                    if (aBoolean) {
                        if (mViewModel.getBankListLiveData().getValue() == null) {
                            mViewModel.getBankList(true);
                        }
                        mViewModel.getBankListLiveData().observe(getViewLifecycleOwner(), getBankListObserver());
//                        mViewModel.getMPLTokenLiveData().observe(IncreaseDepositFragment.this, getMPLTokenObserver());
//                        mViewModel.getMPLToken();
                    } else {
                        mPaymentButton.setEnable(false);
                        mBankCard.setVisibility(View.GONE);
                        mAmountInput.setError(Utils.toPersianNumber(AB.getString(R.string.deposit_error_invalid_amount)));
                    }
                    mViewModel.consumeAmountLiveData();
                }
            }
        };
    }

    private Observer<Long> getGetCreditObserver() {
        return new Observer<Long>() {
            @Override
            public void onChanged(Long deposit) {
                if (deposit != null) {
                    mDeposit = deposit;
                }
            }
        };
    }

    public Observer<MPLTokenResponse> getMPLTokenObserver() {
        mMplTokenObserver = new Observer<MPLTokenResponse>() {
            @Override
            public void onChanged(MPLTokenResponse mplTokenResponse) {
                if (mplTokenResponse.getStatus() != null && mplTokenResponse.getStatus() == 0 && mplTokenResponse.getMplStatus())
                    openMPLActivity(mplTokenResponse);
                else {
                    mViewModel.getBankList(false);
                    mViewModel.getBankListLiveData().observe(IncreaseDepositFragment.this, getBankListObserver());
                }
            }
        };
        return mMplTokenObserver;
    }

    public Observer<ConfirmPaymentResponse> getConfirmPaymentObserver() {
        return new Observer<ConfirmPaymentResponse>() {
            @Override
            public void onChanged(ConfirmPaymentResponse confirmPaymentResponse) {
                if (confirmPaymentResponse.getStatus() != null && confirmPaymentResponse.getStatus() == 0) {
                    mUserInfoViewModel.onCreditIncreased(true);

                    mAmountInput.setText("");
                } else {
                    mAmountInput.setText("");
                    mUserInfoViewModel.onCreditIncreased(false);
                    Toast.makeText(getContext(), confirmPaymentResponse.getDescription(), Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private void openMPLActivity(MPLTokenResponse mplTokenResponse) {
//        Intent intent = new Intent(getContext(), PaymentInitiator.class);
//        intent.putExtra(Constants.MPL_TYPE_KEY, Constants.MPL_TYPE_VALUE);
//        intent.putExtra(Constants.MPL_TOKEN_KEY, mplTokenResponse.getAuthority());
//        intent.putExtra(Constants.MPL_ORDER_ID_KEY, mplTokenResponse.getRequestId());
//        startActivityForResult(intent, REQUEST_CODE_MPL_TRANSACTION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_MPL_TRANSACTION) {
            if (resultCode == RESULT_CODE_MPL_TRANSACTION_CANCELED) {
                // do nothing
            } else if (resultCode == RESULT_CODE_MPL_TRANSACTION_DONE) {
                mViewModel.getConfirmPaymentLiveData().observe(IncreaseDepositFragment.this, getConfirmPaymentObserver());
                createConfirmPaymentRequest(data);
                mViewModel.confirmPayment();
            } else if (resultCode == RESULT_CODE_MPL_TRANSACTION_ERROR) {
                int errorCode = data.getIntExtra(Constants.MPL_ERROR_CODE_KEY, 0);
            }
        }
    }

    private void createConfirmPaymentRequest(Intent mplData) {
        String enData = mplData.getStringExtra(Constants.MPL_END_DATE_KEY);
        String message = mplData.getStringExtra(Constants.MPL_MESSAGE_KEY);
        int status = mplData.getIntExtra(Constants.MPL_STATUS_KEY, 0);

        MPLResponse mplResponse = new Gson().fromJson(enData, MPLResponse.class);
        mViewModel.setMPLResponse(mplResponse, status);
    }

    private void showReceiptView(IncreaseDepositStatusResponse statusResponse) {
        IABResources abResources = DIMain.getABResources();

        mViewModel.setLoading(false);

        List<ReceiptItem> list = new ArrayList<>();
        list.add(new ReceiptItem(abResources.getString(R.string.receipt_increase_deposit_transaction_type_title),
                abResources.getString(R.string.receipt_increase_deposit_transaction_type), null));
        list.add(new ReceiptItem(abResources.getString(R.string.receipt_increase_deposit_amount_title),
                Utils.toPersianPriceNumber(statusResponse.getAmount()) + " " + abResources.getString(R.string.rial), null));
        list.add(new ReceiptItem(abResources.getString(R.string.receipt_increase_deposit_paymentid_title),
                statusResponse.getPaymentId(), null));

        int status = statusResponse.getStatus();

        Receipt receipt = new Receipt();
        receipt.setReceiptItems(list);
        receipt.setStatusCode(status);
        receipt.setReceiptType(Receipt.RECEIPT_INCREASE_DEPOSIT);

        if (status == 0) {
            receipt.setTitleDeposit(abResources.getString(R.string.receipt_increase_deposit_your_credit));
            receipt.setValueDeposit(Utils.toPersianPriceNumber(mDeposit + statusResponse.getAmount()));
        } else {
            receipt.setTitleDeposit(abResources.getString(R.string.receipt_deposit_your_credit));
            receipt.setValueDeposit(Utils.toPersianPriceNumber(mDeposit));
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(ReceiptFragment.RECEIPT_KEY, receipt);
        openFragment(ReceiptFragment.newInstance(receipt), NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);
    }

    @Override
    public IncreaseDepositViewModel acquireViewModel() {
        return DIMain.getViewModel(IncreaseDepositViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_increase_deposit;
    }

    @Override
    public void onItemClick(View view, int position) {
        Boolean loading = mViewModel.isLoading().getValue();

        if (loading != null && loading) {
            return;
        }

        String inputTextValue = mAmountInput.getInputText();

        if (inputTextValue.length() > 0) {
            if (Long.valueOf(inputTextValue) < MAX_AMOUNT) {
                mAmountInput.setText(String.valueOf(Integer.valueOf(inputTextValue.replaceAll("،", "")) + Integer.valueOf(amounts[position])));
            } else {
                mAmountInput.setText(inputTextValue);
            }
        } else {
            mAmountInput.setText(amounts[position]);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            mAmountInput.setText("");
        }
    }
}
