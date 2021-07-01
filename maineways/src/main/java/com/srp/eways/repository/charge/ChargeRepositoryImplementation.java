package com.srp.eways.repository.charge;

import android.util.Log;

import com.srp.eways.di.DIMain;
import com.srp.eways.model.charge.BuyChargeResult;
import com.srp.eways.model.charge.ChargeResult;
import com.srp.eways.model.charge.IrancellSpecialOffersResult;
import com.srp.eways.model.charge.request.BuyChargeRequest;
import com.srp.eways.model.charge.result.BuyCashChargeResult;
import com.srp.eways.model.charge.result.topinquiry.TopInquiriesResult;
import com.srp.eways.model.deposit.BankListResponse;
import com.srp.eways.network.CallBackWrapper;
import com.srp.eways.repository.remote.charge.ChargeApiImplementation;
import com.srp.eways.ui.charge.OnChargeDataReadyListener;
import com.srp.eways.ui.charge.model.ChargeData;
import com.srp.eways.ui.charge.model.ChargeInfo;
import com.srp.eways.ui.charge.model.DefaultChargeChoice;
import com.srp.eways.ui.charge.model.IChargeChoice;
import com.srp.eways.ui.charge.model.IChargeOption;
import com.srp.eways.ui.charge.model.IOperator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eramin.
 */
public class ChargeRepositoryImplementation implements ChargeRepository {

    private static final String ENTERED_CHARGEOPTIONS_PREFERENCE_KEY = "enteredChargeOptionsPreferenceKey";

    private static final String CHARGE_DATA_PREFIX = "chargeChoice_";

    private static ChargeRepositoryImplementation instance = null;

    public static ChargeRepositoryImplementation getInstance(){
        if(instance == null)
            instance = new ChargeRepositoryImplementation();

        return instance;
    }

    private ChargeApiImplementation mChargeApiImplementation;

    private ChargeInfo mChargeInfo;

    private ChargeRepositoryImplementation(){

        mChargeApiImplementation = DIMain.getChargeApi();

    }

    @Override
    public void getChargeData(final OnChargeDataReadyListener callBack) {

        mChargeApiImplementation.getChargeData(new CallBackWrapper<ChargeResult>() {
            @Override
            public void onError(int errorCode, String errorMessage) {
                ChargeData chargeData = new ChargeData(null, errorCode, errorMessage);
                callBack.onChargeDataReady(chargeData);
            }

            @Override
            public void onSuccess(ChargeResult responseBody) {
                mChargeInfo = new ChargeInfo(responseBody.getItems());

                addCachedChoicesToChargeInfo();

                ChargeData chargeData = new ChargeData(mChargeInfo, responseBody.getStatus(), null);
                callBack.onChargeDataReady(chargeData);
            }
        });
    }

    private void addCachedChoicesToChargeInfo() {
        List<String> chargeOptionKeyList = DIMain.getPreferenceCache().getStringList(ENTERED_CHARGEOPTIONS_PREFERENCE_KEY);

        if (chargeOptionKeyList == null || chargeOptionKeyList.size() == 0) {
            return;
        }

        for (String chargeOptionKey: chargeOptionKeyList) {
            List<String> choiceStringList = DIMain.getPreferenceCache().getStringList(chargeOptionKey);

            boolean isChoicesAddedToParent = false;
            for (IOperator operator: mChargeInfo.getOperators()) {
                for (IChargeOption chargeOption: operator.getChargeOptions()) {
                    isChoicesAddedToParent = addChoicesToParent(chargeOption, chargeOptionKey, choiceStringList);

                    if (isChoicesAddedToParent) {
                        break;
                    }
                }

                if (isChoicesAddedToParent) {
                    break;
                }
            }
        }
    }

    private boolean addChoicesToParent(IChargeOption chargeOption, String parentChargeOptionKey, List<String> choiceStringList) {
        if (!chargeOption.hasChargeOptions()) {
            if (parentChargeOptionKey.equals(getKeyForChargeOption(chargeOption))) {

                for (String choiceString: choiceStringList) {
                    IChargeChoice chargeChoice = DIMain.getGson().fromJson(choiceString, DefaultChargeChoice.class);

                    if (chargeChoice == null) {
                        // TODO RAMIN THIS SOMETIMES HAPPEN. MUST CHECK WHY????
                        Log.e("RAMIN", "Check here Ramin.");

                        continue;
                    }

                    chargeOption.addChoice(chargeChoice.getAmount());
                }

                return true;
            }

            return false;
        }

        List<? extends IChargeOption> chargeOptions = chargeOption.getChargeOptions();

        for (IChargeOption childOption: chargeOptions) {
            addChoicesToParent(childOption, parentChargeOptionKey, choiceStringList);
        }

        return false;
    }

    @Override
    public void addChoice(IChargeOption parentChargeOption, IChargeChoice chargeChoice) {
        String optionKey = getKeyForChargeOption(parentChargeOption);
        addToKeyList(optionKey);

        List<String> choiceStringList = new ArrayList<>();
        List<String> cachedList = DIMain.getPreferenceCache().getStringList(optionKey);
        if (cachedList != null) {
            choiceStringList.addAll(cachedList);
        }

        String choiceString = chargeChoice.toJsonString();
        if (!choiceStringList.contains(choiceString)) {
            choiceStringList.add(choiceString);

            DIMain.getPreferenceCache().put(optionKey, choiceStringList);
        }
    }

    @Override
    public void removeChoice(IChargeOption parentChargeOption, IChargeChoice chargeChoice) {
        String optionKey = getKeyForChargeOption(parentChargeOption);

        List<String> choiceStringList = new ArrayList<>();
        List<String> cacheList = DIMain.getPreferenceCache().getStringList(optionKey);
        if (cacheList != null) {
            choiceStringList.addAll(cacheList);
        }

        String choiceString = chargeChoice.toJsonString();

        choiceStringList.remove(choiceString);

        if (choiceStringList.size() == 0) {
            removeFromKeyList(optionKey);
        }

        DIMain.getPreferenceCache().put(optionKey, choiceStringList);
    }

    private String getKeyForChargeOption(IChargeOption lastSelectedChargeOption) {
        return CHARGE_DATA_PREFIX + lastSelectedChargeOption.getOperator().getName() + "_" + lastSelectedChargeOption.getName();
    }

    private void addToKeyList(String chargeOptionKey) {
        List<String> keyList = new ArrayList<>();

        List<String> cachedKeyList = DIMain.getPreferenceCache().getStringList(ENTERED_CHARGEOPTIONS_PREFERENCE_KEY);
        if (cachedKeyList != null) {
            keyList.addAll(cachedKeyList);
        }

        if (!keyList.contains(chargeOptionKey)) {
            keyList.add(chargeOptionKey);
        }

        DIMain.getPreferenceCache().put(ENTERED_CHARGEOPTIONS_PREFERENCE_KEY, keyList);
    }

    private void removeFromKeyList(String chargeOptionKey) {
        List<String> keyList = new ArrayList<>();

        List<String> cachedKeyList = DIMain.getPreferenceCache().getStringList(ENTERED_CHARGEOPTIONS_PREFERENCE_KEY);
        if (cachedKeyList != null) {
            keyList.addAll(cachedKeyList);
        }

        keyList.remove(chargeOptionKey);

        DIMain.getPreferenceCache().put(ENTERED_CHARGEOPTIONS_PREFERENCE_KEY, keyList);
    }

    @Override
    public void buyCharge(BuyChargeRequest buyChargeRequest, CallBackWrapper<BuyChargeResult> callBack) {
        mChargeApiImplementation.buyCharge(buyChargeRequest, callBack);
    }

    @Override
    public void buyCashCharge(BuyChargeRequest buyChargeRequest, CallBackWrapper<BuyCashChargeResult> callBack) {
        mChargeApiImplementation.buyCashCharge(buyChargeRequest, callBack);
    }

    @Override
    public void getIrancellSpecialOffers(String phoneNumber, CallBackWrapper<IrancellSpecialOffersResult> callBack) {
        mChargeApiImplementation.getIrancellSpecialOffers(phoneNumber, callBack);
    }

    @Override
    public void getTopInquiries(CallBackWrapper<TopInquiriesResult> callBack) {
        mChargeApiImplementation.getTopInquiries(callBack);
    }

    @Override
    public void getBankList(CallBackWrapper<BankListResponse> callBack) {
        mChargeApiImplementation.getBankList(callBack);
    }

}
