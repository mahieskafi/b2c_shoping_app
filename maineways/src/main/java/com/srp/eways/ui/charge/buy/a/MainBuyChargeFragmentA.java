package com.srp.eways.ui.charge.buy.a;


import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.srp.eways.ui.charge.model.ChargeData;
import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.charge.BuyChargeResult;
import com.srp.eways.model.charge.result.TopupTypeItem;
import com.srp.eways.model.charge.result.topinquiry.Item;
import com.srp.eways.model.charge.result.topinquiry.TopInquiriesResult;
import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.ui.charge.ChargeSelectionState;
import com.srp.eways.ui.charge.ChargeViewModel;
import com.srp.eways.ui.charge.buy.AddableRadioOptionManager;
import com.srp.eways.ui.charge.buy.ChargeOptionManager;
import com.srp.eways.ui.charge.buy.OperatorChargeOptionManager;
import com.srp.eways.ui.charge.buy.RadioOptionManager;
import com.srp.eways.ui.charge.buy.SpecialOfferOptionManager;
import com.srp.eways.ui.charge.buy.TabOptionManager;
import com.srp.eways.ui.charge.model.IChargeChoice;
import com.srp.eways.ui.charge.model.IChargeOption;
import com.srp.eways.ui.charge.model.IChargeOptionViewType;
import com.srp.eways.ui.charge.model.IOperator;
import com.srp.eways.ui.charge.model.TopupFixedChoice;
import com.srp.eways.ui.charge.model.TopupUserInputChoice;
import com.srp.eways.ui.confirmtransaction.ChargePaymentDetailView;
import com.srp.eways.ui.confirmtransaction.ConfirmTransaction;
import com.srp.eways.ui.login.UserInfoViewModel;
import com.srp.eways.ui.navigation.NavigationViewType;
import com.srp.eways.ui.phonebook.mobilecontact.MobilePhoneBookActivity;
import com.srp.eways.ui.phonebook.ewayscontact.contactlist.EwaysContactFragment;
import com.srp.eways.ui.phonebook.ewayscontact.EwaysContactActivity;
import com.srp.eways.ui.receipt.ReceiptFragment;
import com.srp.eways.ui.transaction.charge.ChargeTransactionViewModel;
import com.srp.eways.ui.transaction.charge.inquiry.InquiryTransactionAdapter;
import com.srp.eways.ui.view.NColumnRadioGroup;
import com.srp.eways.ui.view.UserInputChoiceRadioGroup;
import com.srp.eways.ui.view.button.ButtonElement;
import com.srp.eways.ui.view.EmptyView;
import com.srp.eways.ui.view.LoadingStateView;
import com.srp.eways.ui.view.charge.PhoneAndOperatorsView;
import com.srp.eways.ui.view.receipt.ReceiptItem;
import com.srp.eways.util.Constants;
import com.srp.eways.util.Utils;
import com.srp.eways.ui.navigation.NavigationMemberFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public abstract class MainBuyChargeFragmentA extends NavigationMemberFragment<ChargeViewModel> implements InquiryTransactionAdapter.RepeatTransactionClickListener {

    private static final int REQUEST_CODE_SELECT_CONTACT = 1000;
    private static final int REQUEST_CODE_SELECT_MOBILE_CONTACT = 2000;

    public static final int CHARGEPAGE_VIEWSTATE_NO_INTERNET = -2;
    public static final int CHARGEPAGE_VIEWSTATE_SHOW_ERROR = -1;
    public static final int CHARGEPAGE_VIEWSTATE_SHOW_LOADING = 0;
    public static final int CHARGEPAGE_VIEWSTATE_SELECTING_CHARGEOPTIONS = 1;

    protected ChargeViewModel mChargeViewModel;
    private UserInfoViewModel mUserInfoViewModel;
    private ChargeTransactionViewModel mChargeTransactionViewModel;

    private String mNoInternetDescription;
    private String mLoadingChargeDataDescription;
    private String mFailedLoadingChargeDataDescription;

    private EmptyView mEmptyView;
    private LoadingStateView mLoadingStateView;

    private LinearLayout mContainerLevelOptions;

    private ButtonElement mButtonConfirm;

    protected ChargePaymentDetailView mPaymentDetail;

    private RecyclerView mTopInquiries;
    private ChargeTopInquiryAdapter mTopInquiriesAdapter;

    private boolean mOperatorAnimationIsInProgress = false;

    private String mTransactionType;
    private String mChargeType;
    private int mChargeTypeId;

    protected String mDefaultPhoneNumber = "";

    private ScrollView rootView;
    private Observer<BuyChargeResult> mBuyChargeResultObserver = new Observer<BuyChargeResult>() {
        @Override
        public void onChanged(BuyChargeResult buyChargeResult) {
            chargeBought(buyChargeResult);

            if (buyChargeResult.getStatus() == 0) {

                mUserInfoViewModel.invalidateCredit();
                mUserInfoViewModel.getCredit();

                openFragment(ReceiptFragment.newInstance(buyChargeResult.getReceipt(mChargeType, mTransactionType, mChargeTypeId)), NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);
            } else if (buyChargeResult.getStatus() != ChargeViewModel.REDIRECTED) {

                SpannableStringBuilder biggerText = new SpannableStringBuilder(buyChargeResult.getDescription());
                biggerText.setSpan(new RelativeSizeSpan(1.15f), 0, buyChargeResult.getDescription().length(), 0);

                final Toast toast = Toast.makeText(getContext(), biggerText, Toast.LENGTH_LONG);
                toast.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 4000);
            }

            if (mChargeTransactionViewModel != null && buyChargeResult.getStatus() != mChargeViewModel.NOT_ENOUGH_CREDIT) {
                mChargeTransactionViewModel.invalidateChargeTransaction();
                mChargeTransactionViewModel.loadMore();
            }
        }
    };

    @Override
    public ChargeViewModel acquireViewModel() {
        return mChargeViewModel = DIMain.getViewModel(ChargeViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = view.findViewById(R.id.rootview_charge_a);

        final IABResources abResources = DIMain.getABResources();

        mUserInfoViewModel = DIMain.getViewModel(UserInfoViewModel.class);
        mChargeTransactionViewModel = DIMain.getViewModel(ChargeTransactionViewModel.class);

        mEmptyView = view.findViewById(R.id.emptyview);
        mLoadingStateView = view.findViewById(R.id.loadingstateview);
        mContainerLevelOptions = view.findViewById(R.id.container_leveloptions);
        mButtonConfirm = view.findViewById(R.id.button_confirm);
        mPaymentDetail = view.findViewById(R.id.payment_detail);
        mTopInquiries = view.findViewById(R.id.top_inquiries);

        mButtonConfirm.setTextSize(abResources.getDimen(R.dimen.buycharge_button_confirm_text_size));
        mButtonConfirm.setTextColor(abResources.getColor(R.color.buycharge_button_confirm_text_color));
        mButtonConfirm.setText(abResources.getString(R.string.charge_buy_button_confirm_text));
        mButtonConfirm.setBackground(abResources.getDrawable(R.drawable.login_button_background_enabled));
        mButtonConfirm.setTextTypeFace(ResourcesCompat.getFont(getContext(), R.font.iran_yekan_light));
        mButtonConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mChargeViewModel.getBuyChargeResultLive().observe(MainBuyChargeFragmentA.this, mBuyChargeResultObserver);

                IABResources resources = DIMain.getABResources();

                long minChargeValueIrancell = 5000;
                long minChargeValueOtherOperator = 10000;

                IChargeChoice iChargeChoice = mChargeViewModel.getSelectedChargeChoice().getValue();
                IOperator selectedOperator = mChargeViewModel.getOperator().getValue();

                switch (selectedOperator.getOperatorKey()) {
                    case 1:
                    case 2: {
                        if (iChargeChoice.getAmount() < minChargeValueIrancell) {
                            Toast.makeText(getContext(), Utils.toPersianNumber(abResources.getString(R.string.minimum_chargeamount_irancell)), Toast.LENGTH_LONG).show();
                            return;
                        }
                        break;
                    }
                    case 0: {
                        if (iChargeChoice.getAmount() < minChargeValueOtherOperator) {
                            Toast.makeText(getContext(), Utils.toPersianNumber(abResources.getString(R.string.minimum_chargeamount_otheroperatror)), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        break;
                    }
                }

                ConfirmTransaction confirmTransaction = new ConfirmTransaction();
                List<ReceiptItem> receiptItemList = new ArrayList<>();

                IChargeChoice choice = mChargeViewModel.getSelectedChargeChoice().getValue();

                receiptItemList.add(new ReceiptItem(resources.getString(R.string.buycharge_transactiontype), mTransactionType, null));
                receiptItemList.add(new ReceiptItem(resources.getString(R.string.buycharge_mobilenumber), Utils.toPersianNumber(mChargeViewModel.getPhoneNumber()), null));
//                mTopInquiriesAdapter.notifyChange();

                String typename = "";
                switch (mChargeViewModel.getSelectedChargeChoice().getValue().getProductType()) {

                    case TopupTypeItem.TYPE_INTERNET:
                        typename = "نوع بسته:";
                        break;
                    default:
                        typename = resources.getString(R.string.buycharge_chargetype);
                }

                receiptItemList.add(new ReceiptItem(typename, mChargeType, null));

                if (choice instanceof TopupUserInputChoice.Choice || choice instanceof TopupFixedChoice) {
                    receiptItemList.add(new ReceiptItem(resources.getString(R.string.buycharge_chargeamount), choice.getDisplayChargeAmount(), null));
                    receiptItemList.add(new ReceiptItem(resources.getString(R.string.buycharge_taxamount), choice.getDisplayTaxAmount(), null));
                    receiptItemList.add(new ReceiptItem(resources.getString(R.string.buycharge_sum), choice.getDisplaySum(), null));
                } else {
                    receiptItemList.add(new ReceiptItem(resources.getString(R.string.buycharge_paymentamount), Utils.toPersianPriceNumber((int) choice.getPaidAmount()) + " " + getString(R.string.rial), null));
                }

                confirmTransaction.setValuePaidPrice(choice.getDisplayPaidAmount());
                confirmTransaction.setReceiptItems(receiptItemList);

                showConfirmTransaction(confirmTransaction);
            }

        });

        LinearLayout.LayoutParams buttonConfirmLP = (LinearLayout.LayoutParams) mButtonConfirm.getLayoutParams();

        buttonConfirmLP.leftMargin = abResources.getDimenPixelSize(R.dimen.buycharge_chargelevelview_margin_left);
        buttonConfirmLP.topMargin = abResources.getDimenPixelSize(R.dimen.buycharge_button_confirm_margin_top);
        buttonConfirmLP.rightMargin = abResources.getDimenPixelSize(R.dimen.buycharge_chargelevelview_margin_right);

        mButtonConfirm.setLayoutParams(buttonConfirmLP);

        mNoInternetDescription = abResources.getString(R.string.loadingstateview_text_network_unavailable);
        mLoadingChargeDataDescription = abResources.getString(R.string.loading_message);
        mFailedLoadingChargeDataDescription = abResources.getString(R.string.buycharge_loadingstateview_text_problem_loading_chargedata);

        mLoadingStateView.setRetryListener(new LoadingStateView.RetryListener() {
            @Override
            public void onRetryButtonClicked() {
                mChargeViewModel.loadChargeData();
                mChargeViewModel.loadTopInquiries();
            }
        });


        mTopInquiries.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Rect oldBound = new Rect(0, -1, v.getWidth(), v.getHeight() + 10);
                ViewCompat.setClipBounds(mTopInquiries, oldBound);

            }
        });

        mTopInquiries.setLayoutManager(new LinearLayoutManager(getContext()));
        mTopInquiriesAdapter = new ChargeTopInquiryAdapter(getContext(), this);
        mTopInquiries.setAdapter(mTopInquiriesAdapter);
        if (mChargeViewModel.getTopInquiries().getValue() != null) {
            mTopInquiriesAdapter.setData(new ArrayList<>(mChargeViewModel.getTopInquiries().getValue().getItems()));
        }

        getData();

        mChargeViewModel.getHasNewChargeLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean hasNewData) {
                if (hasNewData) {
                    mChargeViewModel.loadTopInquiries();

                    if (mContainerLevelOptions.getChildCount() > 0) {
                        ((PhoneAndOperatorsView) mContainerLevelOptions.getChildAt(0)).onRemovePhoneNumberClicked();
                    }

                    mChargeViewModel.consumeHasNewChargeLiveData();
                }
            }
        });

        observeRepeatTransactionPhoneNumber();


    }

    protected void observeRepeatTransactionPhoneNumber() {

        mChargeViewModel.getRepeatTransactionPhoneNumberLive().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String phoneNumber) {
                if (phoneNumber != null) {
                    UserPhoneBook userPhoneBook = new UserPhoneBook("", phoneNumber);

                    mTopInquiries.setVisibility(GONE);
                    mChargeViewModel.onContactInfoChanged(userPhoneBook);

                    mChargeViewModel.consumeRepeatTransactionPhoneNumber();
                }
            }
        });

    }

    private void getData() {
        if (!isNetworkConnected()) {
            onViewStateChanged(CHARGEPAGE_VIEWSTATE_NO_INTERNET);
            mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_ERROR, mNoInternetDescription, false);
//            mLoadingStateView.setRetryListener(new LoadingStateView.RetryListener() {
//                @Override
//                public void onRetryButtonClicked() {
//                    mViewMode
//                }
//            });
        } else if (mChargeViewModel.getChargeData() == null) {
            mChargeViewModel.loadChargeData();
        }

        if (mChargeViewModel.getTopInquiries().getValue() == null) {
            mChargeViewModel.loadTopInquiries();
        }
        registerObserverForLoadingChargeData();
        registerObserverForChargeDataResult();
//            registerObserverForOperator();
        registerObserveForChargeSelectionState();

        registerObserverForChargeChoice();
    }

    private void registerObserverForChargeChoice() {
        mChargeViewModel.getSelectedChargeChoice().observe(this, new Observer<IChargeChoice>() {
            @Override
            public void onChanged(IChargeChoice chargeChoice) {
                mButtonConfirm.setVisibility(chargeChoice != null ? View.VISIBLE : GONE);
                mPaymentDetail.setVisibility(GONE);

                if (mButtonConfirm.getVisibility() == VISIBLE) {
                    if (chargeChoice == null || (chargeChoice != null && chargeChoice.isAddedToUserChoices())) {
                        scrollToView(mButtonConfirm);
                    }
                }

                if (chargeChoice != null) {
                    mChargeTypeId = chargeChoice.getProductType();

                    switch (chargeChoice.getProductType()) {
                        case TopupTypeItem.TYPE_CHARGE:
                            mTransactionType = Constants.BUY_CHARGE_TYPE_CHARGE;
                            mChargeType = mChargeViewModel.getLastChargeOption().getName();
                            break;
                        case TopupTypeItem.TYPE_INTERNET:
                            mTransactionType = Constants.BUY_CHARGE_TYPE_INTERNET;
                            mChargeType = mChargeViewModel.getSelectedChargeChoice().getValue().getName();
                            break;
                        case TopupTypeItem.TYPE_SPECIALOFFERS:
                        case TopupTypeItem.TYPE_SPECIALOFFERS_ARJI:
                            mTransactionType = Constants.BUY_CHARGE_TYPE_SPECIALOFFERS;
                            mChargeType = mChargeViewModel.getSelectedChargeChoice().getValue().getName();
                            break;
                    }
                }
            }
        });
    }

    private void registerObserverForLoadingChargeData() {
        mChargeViewModel.isLoadingChargeData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoadingChargeData) {
                if (isLoadingChargeData == null) {
                    return;
                }

                checkLoading();
            }
        });

//        mChargeViewModel.isLoadingTopInquiries().observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean isLoadingChargeData) {
//                if (isLoadingChargeData == null) {
//                    return;
//                }
//
////                checkLoading();
//            }
//        });
    }

    private void checkLoading() {

        boolean isLoading = (mChargeViewModel.isLoadingTopInquiries().getValue() != null &&
                mChargeViewModel.isLoadingTopInquiries().getValue() == true) || (mChargeViewModel.isLoadingChargeData().getValue() != null &&
                mChargeViewModel.isLoadingChargeData().getValue() == true);
        if (isLoading) {
            onViewStateChanged(CHARGEPAGE_VIEWSTATE_SHOW_LOADING);

            mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_LOADING,
                    mLoadingChargeDataDescription,
                    true);
        }

//        mChargeViewModel.setLoadingChargeDataConsumed();
    }

    private void registerObserverForChargeDataResult() {
        mChargeViewModel.getChargeDataLive().observe(this, new Observer<ChargeData>() {
            @Override
            public void onChanged(ChargeData chargeData) {

                checkData();
            }
        });

        mChargeViewModel.getTopInquiries().observe(this, new Observer<TopInquiriesResult>() {
            @Override
            public void onChanged(TopInquiriesResult data) {

                if (data != null) {
                    mTopInquiriesAdapter.setData(new ArrayList<>(data.getItems()));
                }
                checkData();
            }
        });
    }

    private void checkData() {

        TopInquiriesResult topInquiriesResult = mChargeViewModel.getTopInquiries().getValue();
        ChargeData chargeData = mChargeViewModel.getChargeDataLive().getValue();


        if (chargeData != null) {
            String errorString = chargeData.errorMessage;

            if (errorString != null) {
                onViewStateChanged(CHARGEPAGE_VIEWSTATE_SHOW_ERROR);
                mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_ERROR, errorString, true);

                return;
            }
            //success condition
            onViewStateChanged(CHARGEPAGE_VIEWSTATE_SELECTING_CHARGEOPTIONS);

            mContainerLevelOptions.removeAllViews();

            if (topInquiriesResult != null) {
                for (Item item : topInquiriesResult.getItems()) {
                    if (item.getDeliverStatus() == 8) {

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mChargeViewModel.loadTopInquiries();
                            }
                        }, 10000);

                        return;
                    }
                }
            }
//                View view = createOperatorsView(null, -1);
//                mContainerLevelOptions.addView(view);

//            mChargeViewModel.setChargeDataConsumed();
//            mChargeViewModel.setTopInquiriesConsumed();
        } else {
            if (!isNetworkConnected()) {
                onViewStateChanged(CHARGEPAGE_VIEWSTATE_NO_INTERNET);
                mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_ERROR, mNoInternetDescription, false);
            } else {
                onViewStateChanged(CHARGEPAGE_VIEWSTATE_SHOW_ERROR);
                mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_ERROR, mFailedLoadingChargeDataDescription, true);
            }
        }
    }

    private void registerObserveForChargeSelectionState() {
        mChargeViewModel.getChargeSelectionState().observe(this, new Observer<List<ChargeSelectionState.ChargeLevelInfo>>() {
            @Override
            public void onChanged(List<ChargeSelectionState.ChargeLevelInfo> chargeLevelsInfo) {
                updateViewState();
            }
        });
    }

    private void updateViewState() {
        List<ChargeSelectionState.ChargeLevelInfo> chargeLevelsInfo = mChargeViewModel.getChargeSelectionState().getValue();

        View phoneOperatorView = mContainerLevelOptions.getChildAt(0);

        while (mContainerLevelOptions.getChildCount() > chargeLevelsInfo.size()) {
            if (!(mContainerLevelOptions.getChildAt(mContainerLevelOptions.getChildCount() - 1) instanceof PhoneAndOperatorsView))
                mContainerLevelOptions.removeViewAt(mContainerLevelOptions.getChildCount() - 1);
            else if (mContainerLevelOptions.getChildCount() == chargeLevelsInfo.size() + 1) {
                break;
            }
        }

        while (mContainerLevelOptions.getChildCount() < chargeLevelsInfo.size() - 1) {
            int currentLevel = mContainerLevelOptions.getChildCount() - 1;

            View chargeLevelView = createViewForChargeLevelInfo(chargeLevelsInfo, currentLevel > -1 ? currentLevel : 0, false);

            if (currentLevel > 0 && mOperatorAnimationIsInProgress) {
                chargeLevelView.setVisibility(GONE);
            }

            mContainerLevelOptions.addView(chargeLevelView);
        }

        int level = chargeLevelsInfo.size() - 1;

        if (level >= 0) {
            List<?> options = getLevelOptions(chargeLevelsInfo, level);

            int viewType = getViewType(options);

            if (viewType == IChargeOptionViewType.VIEWTYPE_OPERATOR) {
                mOperatorAnimationIsInProgress = true;
            }

            if (mContainerLevelOptions.getChildCount() < chargeLevelsInfo.size()) {
                View chargeLevelView = createViewForChargeLevelInfo(chargeLevelsInfo, level, true, mChargeViewModel.getContactInfo());

                if (level > 0 && mOperatorAnimationIsInProgress) {
                    chargeLevelView.setVisibility(GONE);
                }

                mContainerLevelOptions.addView(chargeLevelView);
            } else {
                setSelectedOption(mContainerLevelOptions.getChildAt(level), viewType, options, chargeLevelsInfo.get(level).chargeOptionIndex, true, mChargeViewModel.getContactInfo());
            }
        }

        addViewForNextLevel(chargeLevelsInfo, phoneOperatorView);
    }

    private void addViewForNextLevel(List<ChargeSelectionState.ChargeLevelInfo> chargeLevelsInfo, View phoneOperatorView) {
        if (chargeLevelsInfo.isEmpty()) {
            View view = createViewForLevel(0, chargeLevelsInfo, null, IChargeOptionViewType.VIEWTYPE_OPERATOR);
            view.setLayoutParams(getDefaultLayoutParams());

            if (phoneOperatorView == null) {
                mDefaultPhoneNumber = getViewModel().getDefaultPhoneNumberLiveData().getValue();
                if (mDefaultPhoneNumber != null && !mDefaultPhoneNumber.isEmpty()) {
                    mTopInquiries.setVisibility(GONE);

                    mChargeViewModel.onContactInfoChanged(new UserPhoneBook(null, mDefaultPhoneNumber));
                    mChargeViewModel.consumeDefaultNumber();
                }
                mContainerLevelOptions.addView(view);
            }
            return;
        }

        int lastSelectedLevelIndex = chargeLevelsInfo.size() - 1;
        ChargeSelectionState.ChargeLevelInfo lastChargeLevelInfo = chargeLevelsInfo.get(lastSelectedLevelIndex);

        Object lastSelectedOption = lastChargeLevelInfo.selectedOption;

        if (!(lastSelectedOption instanceof IChargeChoice)) {
            List<?> options = getLevelOptions(chargeLevelsInfo, lastSelectedLevelIndex + 1);

            int levelViewType = getViewType(options);

            View view = createViewForLevel(lastSelectedLevelIndex + 1, chargeLevelsInfo, options, levelViewType);
            view.setLayoutParams(getDefaultLayoutParams());

            if (mOperatorAnimationIsInProgress) {
                view.setVisibility(GONE);
            }

            mContainerLevelOptions.addView(view);

            getChargeOptionManager(levelViewType).setSelectedOption(view, options, -1);

            scrollToView(mContainerLevelOptions.getChildAt(mContainerLevelOptions.getChildCount() - 1));
        }
    }

    private View createViewForChargeLevelInfo(List<ChargeSelectionState.ChargeLevelInfo> chargeLevelsInfo, int level, Object... args) {
        List<?> options = getLevelOptions(chargeLevelsInfo, level);

        int viewType = getViewType(options);

        int selectedIndex = chargeLevelsInfo.get(level).chargeOptionIndex;

        View view = createViewForLevel(level, chargeLevelsInfo, options, viewType);
        view.setLayoutParams(getDefaultLayoutParams());

        setSelectedOption(view, viewType, options, selectedIndex, args);

        scrollToView(mContainerLevelOptions.getChildAt(mContainerLevelOptions.getChildCount() - 1));
        return view;
    }

    private int getViewType(List options) {
        Object selectedOption = options.get(0);

        return ((IChargeOptionViewType) selectedOption).getViewType();
    }

    private List<?> getLevelOptions(List<ChargeSelectionState.ChargeLevelInfo> chargeLevelsInfo, int level) {
        if (level <= 0) {//operatorLevel
            return mChargeViewModel.getChargeData().mChargeInfo.getOperators();
        }

        Object prevLevelSelectedOption = chargeLevelsInfo.get(level - 1).selectedOption;

        if (prevLevelSelectedOption instanceof IOperator) {
            return ((IOperator) prevLevelSelectedOption).getChargeOptions();
        } else if (prevLevelSelectedOption instanceof IChargeOption) {
            if (((IChargeOption) prevLevelSelectedOption).hasChargeOptions()) {
                return ((IChargeOption) prevLevelSelectedOption).getChargeOptions();
            } else {
                return ((IChargeOption) prevLevelSelectedOption).getChargeChoices();
            }
        }

        throw new RuntimeException("NotSupportedChargeOptionException! className = " + prevLevelSelectedOption.getClass().getName());
    }

    protected View createViewForLevel(int level, List<ChargeSelectionState.ChargeLevelInfo> selectionState, List<?> options, int viewType) {
        return getChargeOptionManager(viewType).createChargeOption(getActivity(), level, selectionState, options, mChargeOptionsCallback);
    }

    private void setSelectedOption(View view, int viewType, List options, int selectedIndex, Object... args) {
        getChargeOptionManager(viewType).setSelectedOption(view, options, selectedIndex, args);
    }

    private ChargeOptionManager.ChargeOptionsCallback mChargeOptionsCallback = new ChargeOptionManager.ChargeOptionsCallback() {

        @Override
        public void onUserPhoneBookClicked() {
            Utils.hideKeyboard(getActivity());
            startActivityForResult(EwaysContactActivity.getIntent(getContext()), REQUEST_CODE_SELECT_CONTACT);
        }

        @Override
        public void onMobilePhoneBookClicked() {
            Utils.hideKeyboard(getActivity());
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, Constants.READ_CONTACT_PERMISSION);
            } else {
                startActivityForResult(MobilePhoneBookActivity.getIntent(getContext()), REQUEST_CODE_SELECT_MOBILE_CONTACT);
            }
        }

        @Override
        public void onPhoneNumberChanged(String phoneNumber) {
            UserPhoneBook contact = new UserPhoneBook(null, phoneNumber);

            mChargeViewModel.onContactInfoChanged(contact);
        }

        @Override
        public void onRemovePhoneNumberClicked() {
            mChargeViewModel.onContactInfoChanged(null);
        }

        @Override
        public void onOperatorSelected(IOperator operator) {
            mChargeViewModel.onOperatorChanged(operator);
        }

        @Override
        public void onOperatorLoadAnimationEnded() {
            mOperatorAnimationIsInProgress = false;

            final int childCount = mContainerLevelOptions.getChildCount();

            for (int i = 0; i < childCount; i++) {
                mContainerLevelOptions.getChildAt(i).setVisibility(View.VISIBLE);
            }
            scrollToView(mContainerLevelOptions.getChildAt(childCount - 2));
        }

        @Override
        public void onChargeOptionSelected(int level, int selectedIndex, Object option) {
            mChargeViewModel.onChargeOptionSelected(level, selectedIndex, option);
        }

        @Override
        public void onAddUserInputChoiceClicked(long amount) {
            mChargeViewModel.addAndSelectChargeChoice(amount);
        }

        @Override
        public void onChangeUserInputChoice(long amount) {
            if (amount == 0) {
                mButtonConfirm.setVisibility(GONE);
            } else {
                View lastLevelView = mContainerLevelOptions.getChildAt(mContainerLevelOptions.getChildCount() - 1);
                if (lastLevelView instanceof UserInputChoiceRadioGroup) {
                    ((UserInputChoiceRadioGroup) lastLevelView).setSelectedRadioButton(-1);
                }
                mChargeViewModel.changeChargeChoice(amount);
            }
        }

        @Override
        public void onRemoveUserInputChoiceClicked(long amount) {
            mChargeViewModel.removeChargeChoice(amount);
        }

        @Override
        public void onTopInquiriesVisibility(boolean visibility) {
            if (visibility) {
                mTopInquiries.setVisibility(View.VISIBLE);
            } else {
                mTopInquiries.setVisibility(GONE);
            }
        }

    };

    private SparseArray<ChargeOptionManager> mChargeOptionManagers = new SparseArray<>(5);

    private ChargeOptionManager getChargeOptionManager(int viewType) {
        ChargeOptionManager optionManager = mChargeOptionManagers.get(viewType);

        if (optionManager == null) {
            optionManager = createChargeOptionManager(viewType);
        }

        return optionManager;
    }

    private ChargeOptionManager createChargeOptionManager(int viewType) {
        switch (viewType) {
            case IChargeOptionViewType.VIEWTYPE_OPERATOR:
                return new OperatorChargeOptionManager();
            case IChargeOptionViewType.VIEWTYPE_TAB:
                return new TabOptionManager();
            case IChargeOptionViewType.VIEWTYPE_RADIOBUTTON_ONECOLUMN:
                return new RadioOptionManager(1);
            case IChargeOptionViewType.VIEWTYPE_RADIOBUTTON_ONECOLUMN_ADDABLE:
                return new AddableRadioOptionManager(1);
            case IChargeOptionViewType.VIEWTYPE_RADIOBUTTON_TWOCOLUMN:
                return new RadioOptionManager(2);
            case IChargeOptionViewType.VIEWTYPE_IRANCELL_SPECIALOFFER_LOADING:
            case IChargeOptionViewType.VIEWTYPE_IRANCELL_SPECIALOFFER_FAILED:
            case IChargeOptionViewType.VIEWTYPE_IRANCELL_SPECIALOFFER_EMPTY:
            case IChargeOptionViewType.VIEWTYPE_IRANCELL_SPECIALOFFER_CHOICE:
                return new SpecialOfferOptionManager(viewType);

            default:
                throw new RuntimeException("We don't support viewType = " + viewType + " as chargeOption's viewType");
        }
    }

    private LinearLayout.LayoutParams getDefaultLayoutParams() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        IABResources abResources = DIMain.getABResources();

        lp.topMargin = abResources.getDimenPixelSize(R.dimen.buycharge_chargelevelview_margin_top);
        lp.leftMargin = abResources.getDimenPixelSize(R.dimen.buycharge_chargelevelview_margin_left);
        lp.rightMargin = abResources.getDimenPixelSize(R.dimen.buycharge_chargelevelview_margin_right);

        return lp;
    }

    private void onViewStateChanged(int chargePageViewState) {
        switch (chargePageViewState) {
            case CHARGEPAGE_VIEWSTATE_NO_INTERNET:
            case CHARGEPAGE_VIEWSTATE_SHOW_ERROR:
            case CHARGEPAGE_VIEWSTATE_SHOW_LOADING:
                mEmptyView.setVisibility(GONE);
                mContainerLevelOptions.setVisibility(GONE);
                mTopInquiries.setVisibility(GONE);

                mLoadingStateView.setVisibility(View.VISIBLE);

                break;

            case CHARGEPAGE_VIEWSTATE_SELECTING_CHARGEOPTIONS:
                mEmptyView.setVisibility(GONE);
                mLoadingStateView.setVisibility(GONE);

                mContainerLevelOptions.setVisibility(View.VISIBLE);
                mTopInquiries.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SELECT_CONTACT) {

            if (resultCode == Activity.RESULT_OK) {

                UserPhoneBook userPhoneBook = data.getParcelableExtra(EwaysContactFragment.EXTRA_USERPHONEBOOK);
                if (mContainerLevelOptions.getChildCount() > 0) {
                    ((PhoneAndOperatorsView) mContainerLevelOptions.getChildAt(0)).setPhoneNumber(userPhoneBook.getCellPhone());
                }
            }
        } else if (requestCode == REQUEST_CODE_SELECT_MOBILE_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {

                UserPhoneBook userPhoneBook = data.getParcelableExtra(MobilePhoneBookActivity.EXTRA_MOBILERPHONEBOOK);
                if (mContainerLevelOptions.getChildCount() > 0) {
                    ((PhoneAndOperatorsView) mContainerLevelOptions.getChildAt(0)).setPhoneNumber(userPhoneBook.getCellPhone());
                }
            }
        }
    }

    @Override
    protected void getDataFromServer() {
        boolean isInRestoringMode = mChargeViewModel.hasData();
        if (!isInRestoringMode) {
            return;
        }

        mChargeViewModel.loadChargeData();
        mChargeViewModel.loadTopInquiries();
    }

    private void scrollToView(final View view) {
        rootView.post(new Runnable() {
            @Override
            public void run() {
                Point childOffset = new Point();
                if (view != null) {
                    getDeepChildOffset(rootView, view.getParent(), view, childOffset);

                    ObjectAnimator animator = ObjectAnimator.ofInt(rootView, "scrollY", childOffset.y);
                    animator.setDuration(800);
                    animator.start();
                }
            }
        });
    }

    private void getDeepChildOffset(final ViewGroup mainParent, final ViewParent parent, final View child, final Point accumulatedOffset) {
        ViewGroup parentGroup = (ViewGroup) parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getTop();
        if (parentGroup == null || parentGroup.equals(mainParent)) {
            return;
        }
        getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
    }

    @Override
    public void onItemRepeatTransactionClicked(@NotNull Object inquiryTopup) {
        if (mContainerLevelOptions.getChildCount() > 0 && inquiryTopup instanceof Item) {
            ((PhoneAndOperatorsView) mContainerLevelOptions.getChildAt(0)).setPhoneNumber(((Item) inquiryTopup).getCustomerMobile());

        }
    }

    public abstract void showConfirmTransaction(ConfirmTransaction confirmTransaction);

    public abstract void chargeBought(BuyChargeResult buyChargeResult);

    @Override
    public boolean onBackPress() {
        return super.onBackPress();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
