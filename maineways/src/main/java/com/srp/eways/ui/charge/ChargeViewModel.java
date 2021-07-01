package com.srp.eways.ui.charge;

import android.text.TextUtils;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.srp.eways.base.BaseViewModel;
import com.srp.eways.di.DIMainCommon;
import com.srp.eways.model.banner.BannerResponse;
import com.srp.eways.model.charge.result.BuyCashChargeResult;
import com.srp.eways.model.deposit.BankListResponse;
import com.srp.eways.network.BaseCallBackWrapper;
import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.network.NetworkResponseCodes;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.charge.BuyChargeResult;
import com.srp.eways.model.charge.IrancellSpecialOffersResult;
import com.srp.eways.model.charge.request.BuyChargeRequest;
import com.srp.eways.model.charge.result.topinquiry.TopInquiriesResult;
import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.repository.banner.BannerRepository;
import com.srp.eways.repository.charge.ChargeRepository;
import com.srp.eways.ui.charge.model.ChargeData;
import com.srp.eways.ui.charge.model.ChargeInfo;
import com.srp.eways.ui.charge.model.IChargeChoice;
import com.srp.eways.ui.charge.model.IChargeOption;
import com.srp.eways.ui.charge.model.IChargeOptionViewType;
import com.srp.eways.ui.charge.model.IOperator;
import com.srp.eways.ui.charge.model.NoSpecialOfferChoice;
import com.srp.eways.ui.charge.model.Operator;
import com.srp.eways.ui.charge.model.SpecialOfferLoadFailedChoice;
import com.srp.eways.ui.charge.model.SpecialOfferOption;
import com.srp.eways.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.srp.eways.di.DIMainCommon.getContext;


public class ChargeViewModel extends BaseViewModel {


    public static final int ERROR_PHONENUMBER_EMPTY = -1001;
    public static final int ERROR_PHONENUMBER_INVALID = -1002;
    public static final int NOT_ENOUGH_CREDIT = 9997;
    public static final int REDIRECTED = 80001;

    public static final int ERROR_CHARGECHOICE_NOT_SELECTED = -1003;

    private static ChargeViewModel sINSTANCE;

    private ChargeRepository mChargeRepo;

    private MutableLiveData<Boolean> mIsLoadingTopInquiries = new MutableLiveData<>();
    private MutableLiveData<TopInquiriesResult> mTopInquiries = new MutableLiveData<>();

    private MutableLiveData<Boolean> mIsLoadingChargeData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIssecondState = new MutableLiveData<>();

    private MutableLiveData<ChargeData> mChargeDataLive = new MutableLiveData<>();
    private ChargeData mChargeData;

    private ChargeSelectionState mChargeSelectionState;
    private MutableLiveData<List<ChargeSelectionState.ChargeLevelInfo>> mChargeSelectionStateLive = new MutableLiveData<>();

    private UserPhoneBook mContactInfo;
    private String mPhoneNumber;

    private MutableLiveData<IOperator> mOperator = new MutableLiveData<>();
    private MutableLiveData<List<IOperator>> mTransportOptions = new MutableLiveData<>();

    private IChargeOption mLastSelectedOption;

    private MutableLiveData<IChargeChoice> mSelectedChargeChoiceLive = new MutableLiveData<>();
    private IChargeChoice mSelectedChargeChoice;

    private MutableLiveData<Boolean> mIsBuyingCharge = new MutableLiveData<>();

    private MutableLiveData<BuyChargeResult> mBuyChargeResultLive = new MutableLiveData<>();
    private MutableLiveData<BuyCashChargeResult> mBuyCashChargeResultLive = new MutableLiveData<>();
    private BuyChargeResult mBuyChargeResult;

    private boolean mIsLoadingChargeChoices = false;
    private List<IChargeChoice> mSpecialOfferChoices = null;

    private MutableLiveData<Boolean> mHasNewChargeLiveData = new MutableLiveData<>();

    private MutableLiveData<BankListResponse> mBankListResponseLive = new MutableLiveData<>();

    private MutableLiveData<String> mRepeatTransactionNumberLive = new MutableLiveData<>();
    private MutableLiveData<Integer> mChangeTabBarLive = new MutableLiveData<>();

    private MutableLiveData<String> mDefaultPhoneNumber = new MutableLiveData<>();

    private int mGetWay = 0;

    BannerRepository mBannerRepo;
    private MutableLiveData<BannerResponse> mBannerResponseLive = new MutableLiveData<>();
    private MutableLiveData<Boolean> mBannerLoadingLive = new MutableLiveData<>();
    private MutableLiveData<String> mBannerError = new MutableLiveData<>();

    public static ChargeViewModel getInstance() {
        if (sINSTANCE == null) {
            sINSTANCE = new ChargeViewModel();
        }
        return sINSTANCE;
    }

    public ChargeViewModel() {
        super(null);

        mChargeRepo = DIMain.getChargeRepo();
        mBannerRepo = DIMain.getBannerRepo();
        mChargeSelectionState = new ChargeSelectionState();
    }

    public void getBannerFromServer(int bannerType) {

        mBannerLoadingLive.setValue(true);

        mBannerRepo.getBannerList(bannerType, new BaseCallBackWrapper<BannerResponse>(this) {
            @Override
            public void onError(int errorCode, String errorMessage) {
                mBannerLoadingLive.setValue(false);
                mBannerError.setValue(errorMessage);
            }

            @Override
            public void onSuccess(BannerResponse responseBody) {
                mBannerLoadingLive.setValue(false);
                mBannerError.setValue(null);

                mBannerResponseLive.setValue(responseBody);
            }
        });
    }

    public MutableLiveData<BannerResponse> getBannerLive() {
        return mBannerResponseLive;
    }

    public void loadTopInquiries() {

//        boolean isLoading = mIsLoadingTopInquiries.getValue() == null ? false : mIsLoadingTopInquiries.getValue();
//        if (isLoading) {
//            return;
//        }

        mIsLoadingTopInquiries.setValue(true);

        mChargeRepo.getTopInquiries(new BaseCallBackWrapper<TopInquiriesResult>(this) {
            @Override
            public void onError(int errorCode, String errorMessage) {
                mIsLoadingTopInquiries.setValue(false);

                mTopInquiries.setValue(null);
                //todo
            }

            @Override
            public void onSuccess(TopInquiriesResult responseBody) {
                mIsLoadingTopInquiries.setValue(false);

                mTopInquiries.setValue(responseBody);

                if (responseBody.getStatus() == NetworkResponseCodes.SUCCESS) {//success condition
                    mChargeSelectionStateLive.setValue(mChargeSelectionState.getChargeLevelSelectionInfo());
                }
            }
        });
    }

    public void loadChargeData() {
        boolean isLoading = mIsLoadingChargeData.getValue() == null ? false : mIsLoadingChargeData.getValue();
        if (isLoading) {
            return;
        }

        mIsLoadingChargeData.setValue(true);

        mChargeRepo.getChargeData(new OnChargeDataReadyListener() {
            @Override
            public void onChargeDataReady(ChargeData chargeData) {
                mIsLoadingChargeData.setValue(false);

                mChargeData = chargeData;

                mChargeDataLive.setValue(mChargeData);

                if (chargeData.mStatus == NetworkResponseCodes.SUCCESS) {//success condition
                    mChargeSelectionStateLive.setValue(mChargeSelectionState.getChargeLevelSelectionInfo());
                }
            }
        });
    }

    public void onContactInfoChanged(UserPhoneBook contactItem) {
        mSpecialOfferChoices = null;
        mIsLoadingChargeChoices = false;

        mContactInfo = contactItem;

        mPhoneNumber = mContactInfo == null ? null : mContactInfo.getCellPhone();

//        if (mContactInfo == null) {
//            mPhoneNumber = null;
//        } else {
//            mPhoneNumber = Utils.creatStandardPhoneInput(mContactInfo.getCellPhone());
//        }

        if (mPhoneNumber != null) {
            if (mPhoneNumber.length() > 0)
                mIssecondState.setValue(true);
        } else {
            mIssecondState.setValue(false);
        }

        IOperator selectedOperator = mChargeData.mChargeInfo.getOperator(mPhoneNumber);
        int operatorIndex = mChargeData.mChargeInfo.getOperatorIndex(mPhoneNumber);

        onOperatorChanged(selectedOperator, operatorIndex);
    }

    public void onOperatorChanged(IOperator selectedOperator) {
        if (selectedOperator == null) {
            onOperatorChanged(null, ChargeInfo.NO_OPERATOR_INDEX);

            return;
        }

        List<Operator> operators = mChargeData.mChargeInfo.getOperators();

        for (int i = 0; i < operators.size(); ++i) {
            if (selectedOperator.equals(operators.get(i))) {
                onOperatorChanged(selectedOperator, i);
            }
        }
    }

    private void onOperatorChanged(IOperator selectedOperator, int operatorIndex) {
        mOperator.setValue(selectedOperator);
        mTransportOptions.setValue(selectedOperator != null ? selectedOperator.getTransportableOperators() : null);

        mLastSelectedOption = null;
        mSelectedChargeChoice = null;
//        mChargeSelectionState.onOperatorSelected(operatorIndex, selectedOperator);
//
//        mSelectedChargeChoiceLive.setValue(mSelectedChargeChoice);

        if (operatorIndex != ChargeInfo.NO_OPERATOR_INDEX) {
            onChargeOptionSelected(0, operatorIndex, mOperator.getValue());
        } else {
            mChargeSelectionState.onOperatorSelected(ChargeInfo.NO_OPERATOR_INDEX, null);

            mChargeSelectionStateLive.setValue(mChargeSelectionState.getChargeLevelSelectionInfo());
            mSelectedChargeChoiceLive.setValue(mSelectedChargeChoice);
        }
    }

    public void onChargeOptionSelected(int level, int selectedOptionIndex, Object selectedChargeOption) {
        mChargeSelectionState.onLevelOptionSelected(level, selectedOptionIndex, selectedChargeOption);

        boolean shouldCheckNextLevel;
        if (selectedChargeOption instanceof IOperator) {
            mLastSelectedOption = null;
            mSelectedChargeChoice = null;

            shouldCheckNextLevel = true;
        } else if (selectedChargeOption instanceof IChargeOption) {
            mLastSelectedOption = (IChargeOption) selectedChargeOption;
            mSelectedChargeChoice = null;

            shouldCheckNextLevel = true;
        } else {
            mSelectedChargeChoice = (IChargeChoice) selectedChargeOption;

            shouldCheckNextLevel = false;
        }

        mSelectedChargeChoiceLive.setValue(mSelectedChargeChoice);
        mChargeSelectionStateLive.setValue(mChargeSelectionState.getChargeLevelSelectionInfo());

        if (shouldCheckNextLevel) {
            verifyNextLevelHasOneOption();
        }
    }

    public void onChargeOptionValueChanged(Object selectedChargeOption) {

        if (selectedChargeOption instanceof IChargeChoice) {
            mSelectedChargeChoice = (IChargeChoice) selectedChargeOption;
        }

        mSelectedChargeChoiceLive.setValue(mSelectedChargeChoice);
//        mChargeSelectionStateLive.setValue(mChargeSelectionState.getChargeLevelSelectionInfo());

    }

    private boolean verifyNextLevelHasOneOption() {
        List nextLevelOptions;

        if (mLastSelectedOption == null) {
            nextLevelOptions = mOperator.getValue().getChargeOptions();
        } else if (mSelectedChargeChoice == null) {
            nextLevelOptions = mLastSelectedOption.hasChargeOptions() ? mLastSelectedOption.getChargeOptions() : mLastSelectedOption.getChargeChoices();
        } else {
            nextLevelOptions = new ArrayList();
        }

        if (nextLevelOptions.size() == 1) {
            IChargeOptionViewType option = (IChargeOptionViewType) nextLevelOptions.get(0);

            if (option.getViewType() == IChargeOptionViewType.VIEWTYPE_IRANCELL_SPECIALOFFER_LOADING) {
                if (mIsLoadingChargeChoices) {
                    return false;
                } else if (mSpecialOfferChoices != null) {
                    ((SpecialOfferOption) option).setChoices(mSpecialOfferChoices);
                } else {
                    mIsLoadingChargeChoices = true;

                    int productId = mOperator.getValue().getIrancellSpecialOfferProductId();

                    loadSpecialOfferChoices(new SpecialOfferResultCallback(mPhoneNumber, productId));

                    return false;
                }
            } else if (option.getViewType() == IChargeOptionViewType.VIEWTYPE_IRANCELL_SPECIALOFFER_FAILED || option.getViewType() == IChargeOptionViewType.VIEWTYPE_IRANCELL_SPECIALOFFER_EMPTY) {
                return false;
            }

            onChargeOptionSelected(mChargeSelectionState.getLevelCount(), 0, nextLevelOptions.get(0));
            return true;
        }

        return false;
    }

    private void loadSpecialOfferChoices(CallBackWrapper<IrancellSpecialOffersResult> callback) {
        mChargeRepo.getIrancellSpecialOffers(mPhoneNumber, callback);
    }

    private List<IChargeChoice> createSpecialOfferChoicesFromResult(IrancellSpecialOffersResult responseBody, int productId) {
        List<IChargeChoice> choices = new ArrayList<>();

        choices.addAll(responseBody.getItems(productId));

        if (choices.isEmpty()) {
            choices.add(new NoSpecialOfferChoice());
        }

        return choices;
    }

    private void updateLastSelectedOption(ChargeSelectionState state) {
        IChargeOption lastSelectedOption = null;

        int stateLevelCount = state.getLevelCount();
        if (stateLevelCount == 0) {
            lastSelectedOption = null;
        } else {
            for (int i = 0; i < stateLevelCount; ++i) {
                int levelSelectedIndex = state.getSelectionIndexAtLevel(i);

                if (i == 0) {
                    //case 0: when only operator is selected
                    lastSelectedOption = null;
                } else if (i == 1) {
                    lastSelectedOption = mOperator.getValue().getChargeOptions().get(levelSelectedIndex);
                } else if (i < stateLevelCount - 1) {
                    lastSelectedOption = lastSelectedOption.getChargeOptions().get(levelSelectedIndex);
                } else {//i == levelInfoList.size - 1: lastItem
                    if (lastSelectedOption.hasChargeOptions()) {
                        lastSelectedOption = lastSelectedOption.getChargeOptions().get(levelSelectedIndex);
                    } else {
                        // Last level is a selected chargeChoice: so mLastSelectedOption will remain the same.
                    }
                }
            }
        }

        mLastSelectedOption = lastSelectedOption;
    }

    public void buyCharge() {
        BuyChargeRequest chargeRequest = createBuyChargeRequest();
        if (chargeRequest == null) {
            return;
        }

        mChargeRepo.buyCharge(chargeRequest, new BaseCallBackWrapper<BuyChargeResult>(this) {

            @Override
            public void onSuccess(BuyChargeResult responseBody) {
                mIsBuyingCharge.setValue(false);

                mBuyChargeResult = responseBody;

                mBuyChargeResultLive.setValue(mBuyChargeResult);

                if (responseBody.getStatus() != NOT_ENOUGH_CREDIT) {
                    mHasNewChargeLiveData.setValue(true);
                }
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                mIsBuyingCharge.setValue(false);

                mBuyChargeResult = new BuyChargeResult();
                mBuyChargeResult.setStatus(errorCode);
                mBuyChargeResult.setDescription(errorMessage);

                mBuyChargeResultLive.setValue(mBuyChargeResult);

                if (errorCode != NOT_ENOUGH_CREDIT) {
                    mHasNewChargeLiveData.setValue(true);
                }
            }

        });
    }

    public void buyCashCharge() {

        BuyChargeRequest chargeRequest = createBuyChargeRequest();
        if (chargeRequest == null) {
            return;
        }

        mChargeRepo.buyCashCharge(chargeRequest, new BaseCallBackWrapper<BuyCashChargeResult>(this) {

            @Override
            public void onSuccess(BuyCashChargeResult responseBody) {
                mIsBuyingCharge.setValue(false);

//                mBuyChargeResult = responseBody;

                mBuyCashChargeResultLive.setValue(responseBody);

//                mHasNewChargeLiveData.setValue(true);
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                mIsBuyingCharge.setValue(false);

                mBuyChargeResult = new BuyChargeResult();
                mBuyChargeResult.setStatus(errorCode);
                mBuyChargeResult.setDescription(errorMessage);

                mBuyChargeResultLive.setValue(mBuyChargeResult);

                mHasNewChargeLiveData.setValue(true);
            }

        });
    }

    private BuyChargeRequest createBuyChargeRequest() {
        if (!isOkToBuyCharge()) {
            return null;
        }

        mIsBuyingCharge.setValue(true);

        BuyChargeRequest chargeRequest = new BuyChargeRequest(mSelectedChargeChoice.getProductId(), mSelectedChargeChoice.getPackageId(), mSelectedChargeChoice.getAmount(), mSelectedChargeChoice.getPaidAmount(), mSelectedChargeChoice.getProductType(), mPhoneNumber, mSelectedChargeChoice.getName(), mGetWay);

        return chargeRequest;
    }

    private boolean isOkToBuyCharge() {
        if (mIsBuyingCharge.getValue() != null && mIsBuyingCharge.getValue()) {
            return false;
        }

        if (TextUtils.isEmpty(mPhoneNumber)) {
            mBuyChargeResult = new BuyChargeResult();
            mBuyChargeResult.setStatus(ERROR_PHONENUMBER_EMPTY);

            mBuyChargeResultLive.setValue(mBuyChargeResult);

            return false;
        } else if (!Utils.isPhoneNumberValid(mPhoneNumber)) {
            mBuyChargeResult = new BuyChargeResult();
            mBuyChargeResult.setStatus(ERROR_PHONENUMBER_INVALID);

            mBuyChargeResultLive.setValue(mBuyChargeResult);

            return false;
        } else if (mSelectedChargeChoice == null) {
            mBuyChargeResult = new BuyChargeResult();
            mBuyChargeResult.setStatus(ERROR_CHARGECHOICE_NOT_SELECTED);

            mBuyChargeResultLive.setValue(mBuyChargeResult);

            return false;
        }

        return true;
    }

    public void setTopInquiries(TopInquiriesResult topInquiries) {
        mTopInquiries.setValue(topInquiries);
    }

    public void setLoadingTopInquiriesConsumed() {
        mIsLoadingTopInquiries.setValue(null);
    }

    public MutableLiveData<Boolean> isLoadingTopInquiries() {
        return mIsLoadingTopInquiries;
    }

    public MutableLiveData<TopInquiriesResult> getTopInquiries() {
        return mTopInquiries;
    }

    public void setChargeDataConsumed() {
        mChargeDataLive.setValue(null);
    }

    public void setChargeData(ChargeData chargeData) {
        mChargeData = chargeData;

        mChargeDataLive.setValue(chargeData);

        if (chargeData.mStatus == NetworkResponseCodes.SUCCESS) {//success condition
            mChargeSelectionStateLive.setValue(mChargeSelectionState.getChargeLevelSelectionInfo());
        }
    }

    public void setLoadingChargeDataConsumed() {
        mIsLoadingChargeData.setValue(null);
    }

    public void addAndSelectChargeChoice(long amount) {
        if (!mLastSelectedOption.supportsUserInput()) {
            throw new RuntimeException("Can not add choice to a selectedOption that doesn't support userInput");
        }

        IChargeChoice chargeChoice = mLastSelectedOption.addChoice(amount);//update cachedChoices
        if (chargeChoice != null) {
            mChargeRepo.addChoice(mLastSelectedOption, chargeChoice);//update repoChoices

            int level = mChargeSelectionState.getLastLevelSelectionInfo().selectedOption instanceof IChargeChoice ? mChargeSelectionState.getLevelCount() - 1 : mChargeSelectionState.getLevelCount();
            onChargeOptionSelected(level, 0, chargeChoice);
        } else {
            // choice is already added
            // Do What ?
        }
    }

    public void changeChargeChoice(long amount) {
        if (!mLastSelectedOption.supportsUserInput()) {
            throw new RuntimeException("Can not add choice to a selectedOption that doesn't support userInput");
        }

        IChargeChoice chargeChoice = mLastSelectedOption.createChoice(amount);//update cachedChoices
        if (chargeChoice != null) {
            onChargeOptionValueChanged(chargeChoice);
        }
    }

    public void removeChargeChoice(long amount) {
        if (!mLastSelectedOption.supportsUserInput()) {
            throw new RuntimeException("can not remove choice from a selectedOption that doesn't support userInput");
        }

        IChargeChoice removedChargeChoice = mLastSelectedOption.removeChoice(amount);//update cachedChoices
        mChargeRepo.removeChoice(mLastSelectedOption, removedChargeChoice);//update repoChoices

        boolean isChoiceSelected = mSelectedChargeChoice != null;
        if (isChoiceSelected) {
            int prevLevel = mChargeSelectionState.getLevelCount() - 2;
            int prevLevelSelectedIndex = mChargeSelectionState.getChargeLevelSelectionInfo().get(prevLevel).chargeOptionIndex;
            Object prevLevelSelectedOption = mChargeSelectionState.getChargeLevelSelectionInfo().get(prevLevel).selectedOption;

            onChargeOptionSelected(prevLevel, prevLevelSelectedIndex, prevLevelSelectedOption);
        } else {//choices are just visible to user but non of them are selected
            mChargeSelectionStateLive.setValue(mChargeSelectionState.getChargeLevelSelectionInfo());//Todo
        }
    }

    public IChargeOption getLastChargeOption() {
        return mLastSelectedOption;
    }

    public void onBuyChargeResultConsumed() {
        mBuyChargeResultLive.setValue(null);
    }

    public LiveData<IOperator> getOperator() {
        return mOperator;
    }

    public LiveData<List<IOperator>> getTransportOptions() {
        return mTransportOptions;
    }

    public LiveData<List<ChargeSelectionState.ChargeLevelInfo>> getChargeSelectionState() {
        return mChargeSelectionStateLive;
    }

    public LiveData<Boolean> isLoadingChargeData() {
        return mIsLoadingChargeData;
    }

    public LiveData<Boolean> isBuyingCharge() {
        return mIsBuyingCharge;
    }

    public LiveData<ChargeData> getChargeDataLive() {
        return mChargeDataLive;
    }

    public ChargeData getChargeData() {
        return mChargeData;
    }

    public MutableLiveData<BuyChargeResult> getBuyChargeResultLive() {
        return mBuyChargeResultLive;
    }

    public MutableLiveData<BuyCashChargeResult> getBuyCashChargeResultLive() {
        return mBuyCashChargeResultLive;
    }

    public void consumeBuyCashChargeResultLive() {
        mBuyCashChargeResultLive.setValue(null);
    }

    public MutableLiveData<Boolean> getHasNewChargeLiveData() {
        return mHasNewChargeLiveData;
    }

    public void consumeHasNewChargeLiveData() {
        mHasNewChargeLiveData.setValue(false);
    }

    public LiveData<IChargeChoice> getSelectedChargeChoice() {
        return mSelectedChargeChoiceLive;
    }

    private class SpecialOfferResultCallback implements CallBackWrapper<IrancellSpecialOffersResult> {

        private String mPhoneNumber;

        private int mProductId;

        protected SpecialOfferResultCallback(String phoneNumber, int productId) {
            mPhoneNumber = phoneNumber;

            mProductId = productId;
        }

        @Override
        public void onSuccess(IrancellSpecialOffersResult responseBody) {
            if (!TextUtils.equals(ChargeViewModel.this.mPhoneNumber, mPhoneNumber)) {
                return;
            }

            mIsLoadingChargeChoices = false;

            mSpecialOfferChoices = createSpecialOfferChoicesFromResult(responseBody, mProductId);

            SpecialOfferOption option = verifySpecialOfferOptionParentSelected();

            if (option != null) {
                option.setChoices(mSpecialOfferChoices);

                onChargeOptionSelected(mChargeSelectionState.getLevelCount(), 0, option);
            }
        }

        @Override
        public void onError(int errorCode, String errorMessage) {
            if (errorCode == NetworkResponseCodes.ERROR_AUTHORIZATION_FAILED) {
                setIsNeededToLogin(true);
                return;

            }

            if (!TextUtils.equals(ChargeViewModel.this.mPhoneNumber, mPhoneNumber)) {
                return;
            }

            mIsLoadingChargeChoices = false;

            SpecialOfferOption option = verifySpecialOfferOptionParentSelected();

            if (option != null) {
                option.setChoices(Arrays.asList((IChargeChoice) new SpecialOfferLoadFailedChoice()));

                onChargeOptionSelected(mChargeSelectionState.getLevelCount(), 0, option);
            }
        }
    }

    private SpecialOfferOption verifySpecialOfferOptionParentSelected() {
        if (mLastSelectedOption == null) {
            return null;
        }

        List<IChargeOption> options = mLastSelectedOption.getChargeOptions();

        if (options == null || options.size() != 1) {
            return null;
        }

        IChargeOption option = options.get(0);

        if (option instanceof SpecialOfferOption) {
            return (SpecialOfferOption) option;
        }

        return null;
    }

    public void getBankList() {
        setLoading(true);

        mChargeRepo.getBankList(new BaseCallBackWrapper<BankListResponse>(this) {


            @Override
            public void onError(int errorCode, String errorMessage) {
                setLoading(false);

                BankListResponse mBankListResponse = new BankListResponse(errorCode, errorMessage, null);

                mBankListResponseLive.setValue(mBankListResponse);
            }

            @Override
            public void onSuccess(BankListResponse responseBody) {
                setLoading(false);

                mBankListResponseLive.setValue(responseBody);
            }

        });
    }

    public MutableLiveData<BankListResponse> getBankListLiveData() {
        return mBankListResponseLive;
    }

    public void consumeBankListLiveData() {
        mBankListResponseLive.setValue(null);
    }

    public void setGetway(int getway) {
        mGetWay = getway;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setRepeatTransactionPhoneNumber(String phoneNumber) {
        mRepeatTransactionNumberLive.setValue(phoneNumber);
    }

    public MutableLiveData<String> getRepeatTransactionPhoneNumberLive() {
        return mRepeatTransactionNumberLive;
    }

    public void consumeRepeatTransactionPhoneNumber() {
        mRepeatTransactionNumberLive.setValue(null);
    }

    public void setChangeTabBar(Integer tab) {
        mChangeTabBarLive.setValue(tab);
    }

    public MutableLiveData<Integer> getChangeTabBarLive() {
        return mChangeTabBarLive;
    }

    public MutableLiveData<Boolean> getmIssecondState() {
        return mIssecondState;
    }

    public void consumeChangeTabBar() {
        mChangeTabBarLive.setValue(null);
    }

    public UserPhoneBook getContactInfo() {
        return mContactInfo;
    }

    public boolean hasData() {
        return mChargeData != null && mTopInquiries.getValue() != null;
    }

    @Override
    public void makeInstanceNull() {
        sINSTANCE = null;
    }

    public void setDefaultPhoneNumber(String phoneNumber) {
        mDefaultPhoneNumber.setValue(phoneNumber);
    }

    public void consumeDefaultNumber() {
        mDefaultPhoneNumber.setValue(null);
    }

    public MutableLiveData<String> getDefaultPhoneNumberLiveData() {
        return mDefaultPhoneNumber;
    }
}
