package com.srp.eways.ui.charge.buy.b;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;

import com.srp.eways.di.DIMain;
import com.srp.eways.ui.charge.model.ChargeData;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.R;
import com.srp.eways.model.charge.BuyChargeResult;
import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.ui.charge.ChargeSelectionState;
import com.srp.eways.ui.charge.ChargeViewModel;
import com.srp.eways.ui.charge.buy.AddableRadioOptionManager;
import com.srp.eways.ui.charge.buy.ChargeOptionManager;
import com.srp.eways.ui.charge.buy.SpinnerDialogChargeOptionManager;
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
import com.srp.eways.model.charge.result.TopupTypeItem;
import com.srp.eways.ui.confirmtransaction.ConfirmTransaction;
import com.srp.eways.ui.confirmtransaction.ConfirmTransactionClickListener;
import com.srp.eways.ui.confirmtransaction.ConfirmTransactionDialog;
import com.srp.eways.ui.login.UserInfoViewModel;
import com.srp.eways.ui.navigation.NavigationViewType;
import com.srp.eways.ui.phonebook.mobilecontact.MobilePhoneBookActivity;
import com.srp.eways.ui.phonebook.ewayscontact.contactlist.EwaysContactFragment;
import com.srp.eways.ui.phonebook.ewayscontact.EwaysContactActivity;
import com.srp.eways.ui.receipt.ReceiptFragment;
import com.srp.eways.ui.transaction.charge.ChargeTransactionViewModel;
import com.srp.eways.ui.view.EmptyView;
import com.srp.eways.ui.view.LoadingStateView;
import com.srp.eways.ui.view.charge.b.ChargeChoiceConfirmView;
import com.srp.eways.ui.view.receipt.ReceiptItem;
import com.srp.eways.util.Constants;
import com.srp.eways.util.Utils;
import com.srp.eways.util.analytic.AnalyticConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

/**
 * Created by Eskafi on 9/30/2019.
 */
public class BuyChargeFragmentB extends NavigationMemberFragment<ChargeViewModel> {

    public static final int CHARGEPAGE_VIEWSTATE_NO_INTERNET = -2;
    public static final int CHARGEPAGE_VIEWSTATE_SHOW_ERROR = -1;
    public static final int CHARGEPAGE_VIEWSTATE_SHOW_LOADING = 0;
    public static final int CHARGEPAGE_VIEWSTATE_FINISH_LOADING = 2;
    public static final int CHARGEPAGE_VIEWSTATE_SELECTING_CHARGEOPTIONS = 1;

    private static final int REQUEST_CODE_SELECT_CONTACT = 1000;
    private static final int REQUEST_CODE_SELECT_MOBILE_CONTACT = 2000;

    private static final int OPERATOR_LEVEL = 0;
    private static final int OPERATOR_SERVICES_LEVEL = 1;
    private static final int SERVICES_TYPE_LEVEL = 2;
    private static final int SERVICES_TYPE_CHILD_LEVEL = 3;

    private boolean mOperatorAnimationIsInProgress = false;

    private ChargeViewModel mChargeViewModel;
    private UserInfoViewModel mUserInfoViewModel;
    private ChargeTransactionViewModel mChargeTransactionViewModel;

    private String mNoInternetDescription;
    private String mLoadingChargeDataDescription;
    private String mFailedLoadingChargeDataDescription;

    private EmptyView mEmptyView;
    private LoadingStateView mLoadingStateView;

    private LinearLayout mContainerLevelOptions;

    private ChargeChoiceConfirmView mButtonConfirm;
    private FrameLayout mButtonConfirmContainer;

    private String mTransactionType;
    private String mChargeType;
    private int mChargeTypeId;

    private ConfirmTransactionDialog mConfirmTransactionDialog;

    private SparseArray<ChargeOptionManager> mChargeOptionManagers = new SparseArray<>(5);
    private HashMap<View, Integer> mViewTypes = new HashMap<>(5);

    public static BuyChargeFragmentB newInstance() {
        BuyChargeFragmentB buyChargeFragmentB = new BuyChargeFragmentB();

        Bundle args = new Bundle();
        buyChargeFragmentB.setArguments(args);

        return buyChargeFragmentB;
    }

    @Override
    public ChargeViewModel acquireViewModel() {
        return mChargeViewModel = DIMain.getViewModel(ChargeViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserInfoViewModel = DIMain.getViewModel(UserInfoViewModel.class);
        mChargeTransactionViewModel = DIMain.getViewModel(ChargeTransactionViewModel.class);

        IABResources abResources = DIMain.getABResources();

        mEmptyView = view.findViewById(R.id.emptyview);
        mLoadingStateView = view.findViewById(R.id.loadingstateview);
        mContainerLevelOptions = view.findViewById(R.id.container_leveloptions);
        mButtonConfirm = view.findViewById(R.id.button_confirm);

        mButtonConfirmContainer = view.findViewById(R.id.button_container);
        mButtonConfirmContainer.setPadding(0, abResources.getDimenPixelSize(R.dimen.button_confirmchoice_container_padding_top), 0, abResources.getDimenPixelSize(R.dimen.button_confirmchoice_container_padding_bottom));

        mButtonConfirm = view.findViewById(R.id.button_confirm);
        mButtonConfirm.setListener(new ChargeChoiceConfirmView.Listener() {
            @Override
            public void OnButtonClicked() {
                mChargeViewModel.getBuyChargeResultLive().observe(BuyChargeFragmentB.this, mBuyChargeResultObserver);

                IABResources resources = DIMain.getABResources();
                ConfirmTransaction confirmTransaction = new ConfirmTransaction();
                List<ReceiptItem> receiptItemList = new ArrayList<>();

                IChargeChoice choice = mChargeViewModel.getSelectedChargeChoice().getValue();

                receiptItemList.add(new ReceiptItem(resources.getString(R.string.buycharge_transactiontype), mTransactionType, null));
                receiptItemList.add(new ReceiptItem(resources.getString(R.string.buycharge_mobilenumber), Utils.toPersianNumber(mChargeViewModel.getPhoneNumber()), null));
                receiptItemList.add(new ReceiptItem(resources.getString(R.string.buycharge_chargetype), mChargeType, null));

                if (choice instanceof TopupUserInputChoice.Choice || choice instanceof TopupFixedChoice) {
                    receiptItemList.add(new ReceiptItem(resources.getString(R.string.buycharge_chargeamount), choice.getDisplayChargeAmount(), null));
                    receiptItemList.add(new ReceiptItem(resources.getString(R.string.buycharge_taxamount), choice.getDisplayTaxAmount(), null));
                    receiptItemList.add(new ReceiptItem(resources.getString(R.string.buycharge_sum), choice.getDisplaySum(), null));
                } else {
                    receiptItemList.add(new ReceiptItem(resources.getString(R.string.buycharge_paymentamount), Utils.toPersianPriceNumber((int) choice.getPaidAmount()), null));
                }

                confirmTransaction.setValuePaidPrice(choice.getDisplayPaidAmount());
                confirmTransaction.setReceiptItems(receiptItemList);

                mConfirmTransactionDialog = new ConfirmTransactionDialog(getContext(), confirmTransaction);
                ConfirmTransactionClickListener clickListener = new ConfirmTransactionClickListener() {
                    @Override
                    public void onPayClicked() {
                        DIMain.getEventSender().sendAction(AnalyticConstant.CHARGE, AnalyticConstant.BUY_CHARGE_ACTION);

                        mChargeViewModel.buyCharge();
                    }

                    @Override
                    public void onCancelClicked() {
                        mConfirmTransactionDialog.dismiss();
                    }
                };

                mConfirmTransactionDialog.setClickListener(clickListener);
                mConfirmTransactionDialog.show();
            }
        });

        setupLoadingView(abResources);
    }

    private Observer<BuyChargeResult> mBuyChargeResultObserver = new Observer<BuyChargeResult>() {
        @Override
        public void onChanged(BuyChargeResult buyChargeResult) {
            if (mConfirmTransactionDialog != null) {
                mConfirmTransactionDialog.dismiss();
            }

            if (buyChargeResult.getStatus() == 0) {
                openFragment(ReceiptFragment.newInstance(buyChargeResult.getReceipt(mChargeType, mTransactionType, mChargeTypeId)), NavigationViewType.NAVIGATION_VIEW_TYPE_HAS_TOOLBAR);

                mUserInfoViewModel.invalidateCredit();
                mUserInfoViewModel.getCredit();
            } else {
                Toast.makeText(getContext(), buyChargeResult.getDescription(), Toast.LENGTH_LONG).show();
            }

            if (mChargeTransactionViewModel != null) {
                mChargeTransactionViewModel.invalidateChargeTransaction();
                mChargeTransactionViewModel.loadMore();
            }
        }
    };

    private void setupLoadingView(IABResources abResources) {
        mNoInternetDescription = abResources.getString(R.string.loadingstateview_text_network_unavailable);
        mLoadingChargeDataDescription = abResources.getString(R.string.loading_message);
        mFailedLoadingChargeDataDescription = abResources.getString(R.string.buycharge_loadingstateview_text_problem_loading_chargedata);

        mLoadingStateView.setRetryListener(new LoadingStateView.RetryListener() {
            @Override
            public void onRetryButtonClicked() {
                mChargeViewModel.loadChargeData();
            }
        });

        if (!isNetworkConnected()) {
            onViewStateChanged(CHARGEPAGE_VIEWSTATE_NO_INTERNET);
            mEmptyView.setEmptyText(R.string.loadingstateview_text_network_unavailable);
            //Todo: register networkStateChanged
        } else {
//            //Todo: unregister networkStateChanged
            mChargeViewModel.loadChargeData();
        }
        registerObserverForLoadingChargeData();
        registerObserverForChargeDataResult();
        registerObserveForChargeSelectionState();

        registerObserverForChargeChoice();
    }

    private void registerObserverForChargeChoice() {
        mChargeViewModel.getSelectedChargeChoice().observe(this, new Observer<IChargeChoice>() {
            @Override
            public void onChanged(IChargeChoice chargeChoice) {
                mButtonConfirm.setChargeChoice(chargeChoice);

                mButtonConfirmContainer.setVisibility(chargeChoice != null ? View.VISIBLE : View.GONE);

                if (chargeChoice != null) {
                    mChargeTypeId = chargeChoice.getProductType();

                    switch (mChargeTypeId) {
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

                if (isLoadingChargeData) {
                    onViewStateChanged(CHARGEPAGE_VIEWSTATE_SHOW_LOADING);

                    mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_LOADING,
                            mLoadingChargeDataDescription,
                            true);
                } else {
                    onViewStateChanged(CHARGEPAGE_VIEWSTATE_FINISH_LOADING);
                }

                mChargeViewModel.setLoadingChargeDataConsumed();
            }
        });
    }

    private void registerObserverForChargeDataResult() {
        mChargeViewModel.getChargeDataLive().observe(this, new Observer<ChargeData>() {
            @Override
            public void onChanged(ChargeData chargeData) {
                if (chargeData == null) {
                    return;
                }

                String errorString = chargeData.errorMessage;

                if (errorString != null) {
                    onViewStateChanged(CHARGEPAGE_VIEWSTATE_SHOW_ERROR);
                    mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_ERROR, errorString, true);
                    return;
                } else {
                    onViewStateChanged(CHARGEPAGE_VIEWSTATE_FINISH_LOADING);
                }

                //success condition
                onViewStateChanged(CHARGEPAGE_VIEWSTATE_SELECTING_CHARGEOPTIONS);

                mContainerLevelOptions.removeAllViews();

                mChargeViewModel.setChargeDataConsumed();
            }
        });
    }

    private void registerObserveForChargeSelectionState() {
        mChargeViewModel.getChargeSelectionState().observe(this, new Observer<List<ChargeSelectionState.ChargeLevelInfo>>() {
            @Override
            public void onChanged(List<ChargeSelectionState.ChargeLevelInfo> chargeLevelsInfo) {
                updateViewState();
            }
        });
    }

    private LinearLayout.LayoutParams getOperatorsLevelLayoutParams() {
        LinearLayout.LayoutParams lp = getDefaultLayoutParams();

        IABResources abResources = DIMain.getABResources();
        int operatorsViewExtraBottomMargin = abResources.getDimenPixelSize(R.dimen.buycharge_chargelevelview_margin_top);

        lp.bottomMargin = operatorsViewExtraBottomMargin;

        return lp;
    }

    private LinearLayout.LayoutParams getDefaultLayoutParams() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        IABResources abResources = DIMain.getABResources();

        lp.topMargin = abResources.getDimenPixelSize(R.dimen.buycharge_chargelevelview_margin_top);
        lp.leftMargin = abResources.getDimenPixelSize(R.dimen.buycharge_chargelevelview_margin_left);
        lp.rightMargin = abResources.getDimenPixelSize(R.dimen.buycharge_chargelevelview_margin_right);

        return lp;
    }

    public static LinearLayout.LayoutParams getTabChildViewLayoutParams() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        IABResources abResources = DIMain.getABResources();

        int mTabViewPaddingTop = abResources.getDimenPixelSize(R.dimen.chargeoptionstabview_contentpadding_top);
        int mTabIconHeight = abResources.getDimenPixelSize(R.dimen.chargeoptionstabview_tabicon_height);
        int mTabViewPaddingBottom = abResources.getDimenPixelSize(R.dimen.chargeoptionstabview_contentpadding_bottom);

        int height = mTabViewPaddingTop + mTabIconHeight + mTabViewPaddingBottom;

        lp.topMargin = -(height / 2);
        lp.leftMargin = abResources.getDimenPixelSize(R.dimen.buycharge_chargelevelview_margin_left);
        lp.rightMargin = abResources.getDimenPixelSize(R.dimen.buycharge_chargelevelview_margin_right);

        return lp;
    }

    private void updateViewState() {
        List<ChargeSelectionState.ChargeLevelInfo> chargeLevelsInfo = mChargeViewModel.getChargeSelectionState().getValue();

        while (mContainerLevelOptions.getChildCount() > chargeLevelsInfo.size() + 1) {
            View view = mContainerLevelOptions.getChildAt(mContainerLevelOptions.getChildCount() - 1);

            mViewTypes.remove(view);

            mContainerLevelOptions.removeView(view);
        }

        int mismatchIndex = findMismatchIndex(chargeLevelsInfo);

        if (mismatchIndex != -1) {
            for (int i = mismatchIndex; i < mContainerLevelOptions.getChildCount(); i++) {
                mViewTypes.remove(mContainerLevelOptions.getChildAt(i));
            }

            mContainerLevelOptions.removeViews(mismatchIndex, mContainerLevelOptions.getChildCount() - mismatchIndex);
        }

        while (mContainerLevelOptions.getChildCount() <= chargeLevelsInfo.size()) {
            if (mContainerLevelOptions.getChildCount() < chargeLevelsInfo.size()) {
                int currentLevel = mContainerLevelOptions.getChildCount();

                View chargeLevelView = createViewForChargeLevelInfo(chargeLevelsInfo, currentLevel, false, mChargeViewModel.getContactInfo());

                if (currentLevel > 0 && mOperatorAnimationIsInProgress) {
                    chargeLevelView.setVisibility(GONE);
                }

                mContainerLevelOptions.addView(chargeLevelView);
            } else if (!addViewForNextLevel(chargeLevelsInfo)) {
                break;
            }
        }

        int level = chargeLevelsInfo.size() - 1;

        if (level >= 0) {
            List<?> options = getLevelOptions(chargeLevelsInfo, level);

            int viewType = getViewType(options);

//            if (viewType == IChargeOptionViewType.VIEWTYPE_OPERATOR) {
//                mOperatorAnimationIsInProgress = true;
//            }

            if (mContainerLevelOptions.getChildCount() < chargeLevelsInfo.size()) {
                View chargeLevelView = createViewForChargeLevelInfo(chargeLevelsInfo, level, true, mChargeViewModel.getContactInfo());

                if (level > 0 && mOperatorAnimationIsInProgress) {
                    chargeLevelView.setVisibility(GONE);
                }

                mContainerLevelOptions.addView(chargeLevelView);
            } else {
                setSelectedOption(level, mContainerLevelOptions.getChildAt(level), viewType, options, chargeLevelsInfo.get(level).chargeOptionIndex, true, mChargeViewModel.getContactInfo());
            }
        }

        updateViewForNextLevel(chargeLevelsInfo);
    }

    private int findMismatchIndex(List<ChargeSelectionState.ChargeLevelInfo> chargeLevelsInfo) {
        for (int i = 0; i < mContainerLevelOptions.getChildCount(); i++) {
            View view = mContainerLevelOptions.getChildAt(i);

            int viewType = mViewTypes.get(view);

            List options = getLevelOptions(chargeLevelsInfo, i);
            int targetViewType = getViewType(options);

            if (viewType != targetViewType) {
                return i;
            }
        }

        return -1;
    }

    private View createViewForChargeLevelInfo(List<ChargeSelectionState.ChargeLevelInfo> chargeLevelsInfo, int level, Object... args) {
        List<?> options = getLevelOptions(chargeLevelsInfo, level);

        int viewType = getViewType(options);

        int selectedIndex = chargeLevelsInfo.get(level).chargeOptionIndex;

        View view = createViewForLevel(level, chargeLevelsInfo, options, viewType);

        setLevelLayoutParams(view, chargeLevelsInfo, level);

        setSelectedOption(level, view, viewType, options, selectedIndex, args);

        return view;
    }

    private void setLevelLayoutParams(View view, List<ChargeSelectionState.ChargeLevelInfo> chargeLevelsInfo, int level) {
        int prevLevelViewType = getPrevLevelViewType(chargeLevelsInfo, level);

        if (prevLevelViewType == IChargeOptionViewType.VIEWTYPE_TAB) {
            view.setLayoutParams(getTabChildViewLayoutParams());
            int paddingTop = -((LinearLayout.LayoutParams) view.getLayoutParams()).topMargin + DIMain.getABResources().getDimenPixelSize(R.dimen.chargeoptionstabview_children_margintop);
            view.setPadding(view.getPaddingLeft(), paddingTop, view.getPaddingRight(), view.getPaddingBottom());
        }
//        else if (//prevLevelViewType == IChargeOptionViewType.VIEWTYPE_IRANCELL_SPECIALOFFER_CHOICE ||
//                prevLevelViewType == IChargeOptionViewType.VIEWTYPE_IRANCELL_SPECIALOFFER_LOADING ||
//                prevLevelViewType == IChargeOptionViewType.VIEWTYPE_IRANCELL_SPECIALOFFER_EMPTY ||
//                prevLevelViewType == IChargeOptionViewType.VIEWTYPE_IRANCELL_SPECIALOFFER_FAILED) {
//            //
//        }
        else {
            view.setLayoutParams(getDefaultLayoutParams());
        }
    }

    private void setSelectedOption(int level, View view, int viewType, List options, int selectedIndex, Object... args) {
        getChargeOptionManager(level, viewType).setSelectedOption(view, options, selectedIndex, args);
    }

    private int getPrevLevelViewType(List<ChargeSelectionState.ChargeLevelInfo> chargeLevelsInfo, int level) {
        if (level == 0) {//operator level
            return -1;
        }

        List<?> prevLevelOptions = getLevelOptions(chargeLevelsInfo, level - 1);

        return getViewType(prevLevelOptions);
    }

    private int getViewType(List options) {
        Object selectedOption = options.get(0);

        return ((IChargeOptionViewType) selectedOption).getViewType();
    }

    private List<?> getLevelOptions(List<ChargeSelectionState.ChargeLevelInfo> chargeLevelsInfo, int level) {
        if (level == 0) {//operatorLevel
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

    private ChargeOptionManager.ChargeOptionsCallback mChargeOptionsCallback = new ChargeOptionManager.ChargeOptionsCallback() {

        @Override
        public void onUserPhoneBookClicked() {

            startActivityForResult(EwaysContactActivity.getIntent(getContext()), REQUEST_CODE_SELECT_CONTACT);
        }

        @Override
        public void onMobilePhoneBookClicked() {

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_CONTACTS},Constants.READ_CONTACT_PERMISSION);
            }else {
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
//            mOperatorAnimationIsInProgress = false;
//
//            final int childCount = mContainerLevelOptions.getChildCount();
//
//            for (int i = 0; i < childCount; i++) {
//                mContainerLevelOptions.getChildAt(i).setVisibility(View.VISIBLE);
//            }
        }

        @Override
        public void onChargeOptionSelected(int level, int selectedIndex, Object option) {
            mChargeViewModel.onChargeOptionSelected(level, selectedIndex, option);
            mButtonConfirmContainer.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAddUserInputChoiceClicked(long amount) {
            mChargeViewModel.addAndSelectChargeChoice(amount);
        }

        @Override
        public void onChangeUserInputChoice(long amount) {

        }

        @Override
        public void onRemoveUserInputChoiceClicked(long amount) {
            mChargeViewModel.removeChargeChoice(amount);
        }

        @Override
        public void onTopInquiriesVisibility(boolean visibility) {

        }

    };

    private View createViewForLevel(int level, List<ChargeSelectionState.ChargeLevelInfo> selectionState, List<?> options, int viewType) {
        View view = getChargeOptionManager(level, viewType).createChargeOption(getActivity(), level, selectionState, options, mChargeOptionsCallback);

        mViewTypes.put(view, viewType);

        return view;
    }

    private ChargeOptionManager getChargeOptionManager(int level, int viewType) {
        int key = level * 1000 + viewType;

        ChargeOptionManager optionManager = mChargeOptionManagers.get(key);

        if (optionManager == null) {
            optionManager = createChargeOptionManager(level, viewType);
            mChargeOptionManagers.put(key, optionManager);
        } else if (level >= SERVICES_TYPE_CHILD_LEVEL) {
            optionManager = createChargeOptionManager(level, viewType);
            mChargeOptionManagers.put(key, optionManager);
        }

        return optionManager;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_buy_charge_b;
    }

    private ChargeOptionManager createChargeOptionManager(int level, int viewType) {
        if (level >= SERVICES_TYPE_CHILD_LEVEL && viewType != IChargeOptionViewType.VIEWTYPE_IRANCELL_SPECIALOFFER_FAILED && viewType != IChargeOptionViewType.VIEWTYPE_IRANCELL_SPECIALOFFER_EMPTY) {
            return new SpinnerDialogChargeOptionManager(viewType);
        }

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

    private boolean addViewForNextLevel(List<ChargeSelectionState.ChargeLevelInfo> chargeLevelsInfo) {
        if (chargeLevelsInfo.isEmpty()) {
            View view = createViewForLevel(0, chargeLevelsInfo, null, IChargeOptionViewType.VIEWTYPE_OPERATOR);
            view.setLayoutParams(getOperatorsLevelLayoutParams());

            mContainerLevelOptions.addView(view);
            return true;
        }

        int lastSelectedLevelIndex = chargeLevelsInfo.size() - 1;
        ChargeSelectionState.ChargeLevelInfo lastChargeLevelInfo = chargeLevelsInfo.get(lastSelectedLevelIndex);

        Object lastSelectedOption = lastChargeLevelInfo.selectedOption;

        if (!(lastSelectedOption instanceof IChargeChoice)) {
            List<?> options = getLevelOptions(chargeLevelsInfo, lastSelectedLevelIndex + 1);

            int levelViewType = getViewType(options);

            View view = createViewForLevel(lastSelectedLevelIndex + 1, chargeLevelsInfo, options, levelViewType);

            setLevelLayoutParams(view, chargeLevelsInfo, lastSelectedLevelIndex + 1);

            if (mOperatorAnimationIsInProgress) {
                view.setVisibility(GONE);
            }

            mContainerLevelOptions.addView(view);

            return true;
        }

        return false;
    }

    private void updateViewForNextLevel(List<ChargeSelectionState.ChargeLevelInfo> chargeLevelsInfo) {
        if (chargeLevelsInfo.isEmpty()) {
            getChargeOptionManager(0, IChargeOptionViewType.VIEWTYPE_OPERATOR).setSelectedOption(mContainerLevelOptions.getChildAt(0), null, -1, true, mChargeViewModel.getContactInfo());
            return;
        }

        int lastSelectedLevelIndex = chargeLevelsInfo.size() - 1;

        ChargeSelectionState.ChargeLevelInfo lastChargeLevelInfo = chargeLevelsInfo.get(lastSelectedLevelIndex);

        Object lastSelectedOption = lastChargeLevelInfo.selectedOption;

        if (!(lastSelectedOption instanceof IChargeChoice)) {
            List<?> options = getLevelOptions(chargeLevelsInfo, lastSelectedLevelIndex + 1);

            int levelViewType = getViewType(options);

            View view = mContainerLevelOptions.getChildAt(mContainerLevelOptions.getChildCount() - 1);

            getChargeOptionManager(lastSelectedLevelIndex + 1, levelViewType).setSelectedOption(view, options, -1);
        }
    }

    private void onViewStateChanged(int chargePageViewState) {
        switch (chargePageViewState) {
            case CHARGEPAGE_VIEWSTATE_NO_INTERNET:
                mEmptyView.setVisibility(View.VISIBLE);

                mLoadingStateView.setVisibility(GONE);
                mContainerLevelOptions.setVisibility(GONE);
                break;
            case CHARGEPAGE_VIEWSTATE_SHOW_ERROR:
                mLoadingStateView.setVisibility(View.VISIBLE);

                mEmptyView.setVisibility(GONE);
                mContainerLevelOptions.setVisibility(GONE);
                break;
            case CHARGEPAGE_VIEWSTATE_SHOW_LOADING:
                mLoadingStateView.setVisibility(View.VISIBLE);

                mEmptyView.setVisibility(GONE);
                mContainerLevelOptions.setVisibility(GONE);
                break;
            case CHARGEPAGE_VIEWSTATE_SELECTING_CHARGEOPTIONS:
                mContainerLevelOptions.setVisibility(View.VISIBLE);

                mEmptyView.setVisibility(GONE);
                mLoadingStateView.setVisibility(GONE);
                break;
            case CHARGEPAGE_VIEWSTATE_FINISH_LOADING:
                mEmptyView.setVisibility(GONE);
                mLoadingStateView.setVisibility(GONE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SELECT_CONTACT) {

            if (resultCode == RESULT_OK) {

                UserPhoneBook userPhoneBook = data.getParcelableExtra(EwaysContactFragment.EXTRA_USERPHONEBOOK);

                mChargeViewModel.onContactInfoChanged(userPhoneBook);

            }
        }else{
            if (resultCode == RESULT_OK) {

                UserPhoneBook userPhoneBook = data.getParcelableExtra(MobilePhoneBookActivity.EXTRA_MOBILERPHONEBOOK);

                mChargeViewModel.onContactInfoChanged(userPhoneBook);

            }
        }
    }

    @Override
    protected void getDataFromServer() {
        boolean isInRestoringMode = mChargeViewModel.hasData();
        if (isInRestoringMode) {
            return;
        }

        mChargeViewModel.loadChargeData();
    }

}
