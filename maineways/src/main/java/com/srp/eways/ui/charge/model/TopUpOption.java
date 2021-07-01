package com.srp.eways.ui.charge.model;

import com.srp.eways.R;
import com.srp.eways.model.charge.result.ITopupTypeItem;
import com.srp.eways.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TopUpOption implements IChargeOption {

    private ITopupTypeItem mItem;

    private IOperator mOperator;

    private List<IChargeChoice> mChoices;

    private LinkedHashMap<String, String> mSortedFixedChoices;

    private IUserInputChoice mUserInputChoice;

    private boolean mIsIrancell = false;

    protected TopUpOption(IOperator operator, ITopupTypeItem item, boolean isIrancell) {
        mItem = item;

        mOperator = operator;

        mSortedFixedChoices = new LinkedHashMap<>();

        mIsIrancell = isIrancell;
    }

    @Override
    public String getName() {
        return mItem.getPName();
    }

    @Override
    public IOperator getOperator() {
        return mOperator;
    }

    @Override
    public int getProductTypeIconResId() {
        return 0;
    }

    @Override
    public int getOptionTypeHintIconResId() {
        return R.drawable.ic_topupoption;
    }

    @Override
    public int getOptionTypeTitleIconResId() {
        return R.drawable.ic_topupoption;
    }

    @Override
    public int getOptionTypeHintResId() {
        return R.string.topupoption_type_hint;
    }

    @Override
    public int getOptionTypeTitleResId() {
        return R.string.topupoption_type_title;
    }

    @Override
    public boolean hasChargeOptions() {
        return false;
    }

    @Override
    public List<IChargeOption> getChargeOptions() {
        return null;
    }

    @Override
    public List<IChargeChoice> getChargeChoices() {
//        if (mChoices != null && !mIsUserInputChoiceAdded) {
////            updateUserInputChoices();
//
//            return mChoices;
//        }

//        mIsUserInputChoiceAdded = false;

        HashMap<String, String> amountList = mItem.getAmountList();
        mChoices = new ArrayList<>(amountList.size());

        List<IChargeChoice> userInputChoices = getUserInputChoices();

        if (userInputChoices != null) {
            mChoices.addAll(0, userInputChoices);
        }

        sortAmountList();

        for (Map.Entry<String, String> entry : mSortedFixedChoices.entrySet()) {
            long amount = Long.parseLong(entry.getKey());

            int topupFixedChoiceViewType = supportsUserInput() ? VIEWTYPE_RADIOBUTTON_ONECOLUMN_ADDABLE : VIEWTYPE_RADIOBUTTON_ONECOLUMN;

            mChoices.add(new TopupFixedChoice(mItem.getProductId(), entry.getValue(), amount, topupFixedChoiceViewType, mItem.getTax(), mItem.getCofficient(), mIsIrancell));
        }

        return mChoices;
    }

    private void sortAmountList() {
        HashMap<String, String> amountList = mItem.getAmountList();
        List<Map.Entry<String, String>> choicesEntriesArray = new ArrayList<>(amountList.entrySet());

        Collections.sort(choicesEntriesArray, mFixedChoiceAmountComparator);

        for (Map.Entry<String, String> entry : choicesEntriesArray) {
            mSortedFixedChoices.put(entry.getKey(), entry.getValue());
        }
    }

    private Comparator<Map.Entry<String, String>> mFixedChoiceAmountComparator = new Comparator<Map.Entry<String, String>>() {
        @Override
        public int compare(Map.Entry<String, String> entry1, Map.Entry<String, String> entry2) {
            long amountLong1 = Long.parseLong(Utils.toEnglishNumber(Utils.removeThousandSeparator(entry1.getKey())));
            long amountLong2 = Long.parseLong(Utils.toEnglishNumber(Utils.removeThousandSeparator(entry2.getKey())));

            if (amountLong1 > amountLong2) {
                return 1;
            } else if (amountLong1 < amountLong2) {
                return -1;
            } else {
                return 0;
            }
        }
    };

//    private void updateUserInputChoices() {
//        List<IChargeChoice> userInputChoices = getUserInputChoices();
//
//        if (userInputChoices == null) {
//            return;
//        }
//
////        if (userInputChoices.size() == 0) {
////            // remove all old userInputChoices. //Todo: I don't know if this case will happen at all!
////            removeAllUserInputChoicesFromChoices();
////            return;
////        }
//
//        // remove old UserInputChoices from ChoiceList
//        Iterator<IChargeChoice> chargeChoicesIterator = mChoices.iterator();
//        while(chargeChoicesIterator.hasNext()) {
//            IChargeChoice chargeChoice = chargeChoicesIterator.next();
//
//            for (int i = 0; i < userInputChoices.size(); ++i) {
//                if (chargeChoice.getAmount() == userInputChoices.get(i).getAmount()) {
//                    chargeChoicesIterator.remove();
//                }
//            }
//        }
//
//        // add all UserInputChoices from ChoiceList (because there might be addition or removal operations to/from userChoiceList while caching!)
//        mChoices.addAll(0, userInputChoices);
//    }
//
//    private void removeAllUserInputChoicesFromChoices() {
//        if (mChoices.size() == mItem.getAmountList().size()) {
//            return;
//        }
//
//        Iterator<IChargeChoice> chargeChoicesIterator = mChoices.iterator();
//        HashMap<String, String> amountList = mItem.getAmountList();
//
//        while(chargeChoicesIterator.hasNext()) {
//            IChargeChoice chargeChoice = chargeChoicesIterator.next();
//
//            boolean isUserInputChoice = true;
//            for (Map.Entry<String, String> entry: amountList.entrySet()) {
//                long amount = Long.parseLong(entry.getKey());
//
//                if (chargeChoice.getAmount() == amount) {
//                    isUserInputChoice = false;
//                }
//            }
//
//            if (isUserInputChoice) {
//                chargeChoicesIterator.remove();
//            }
//        }
//    }

    @Override
    public boolean supportsUserInput() {
        return !mItem.haveFixAmount();
    }

    @Override
    public IChargeChoice addChoice(long amount) {
        return getUserInputChoice().addChoice(amount);
    }

    @Override
    public IChargeChoice createChoice(long amount) {
        return getUserInputChoice().createChoice(amount);
    }

    @Override
    public IChargeChoice removeChoice(long amount) {
        IChargeChoice chargeChoice = null;

        Iterator iterator = mChoices.iterator();
        while (iterator.hasNext()) {
            IChargeChoice choice = (IChargeChoice) iterator.next();
            if (("" + amount).equals(choice.getAmount() + "")) {
                chargeChoice = choice;

                iterator.remove();
            }
        }

        if (getUserInputChoice() != null) {
            getUserInputChoice().removeChoice(amount);
        }

        return chargeChoice;
    }

    @Override
    public IUserInputChoice getUserInputChoice() {
        if (supportsUserInput()) {
            if (mUserInputChoice == null) {
                mUserInputChoice = new TopupUserInputChoice(mItem, mIsIrancell);
            }
            return mUserInputChoice;
        }

        return null;
    }

    @Override
    public List<IChargeChoice> getUserInputChoices() {
        if (supportsUserInput()) {
            if (mUserInputChoice != null) {
                return mUserInputChoice.getUserChoices();
            } else {
                return null;
            }
        }

        return null;
    }

    @Override
    public int getViewType() {
        return VIEWTYPE_RADIOBUTTON_ONECOLUMN;
    }

}
