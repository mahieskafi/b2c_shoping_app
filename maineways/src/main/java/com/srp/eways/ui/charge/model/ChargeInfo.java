package com.srp.eways.ui.charge.model;

import com.srp.eways.model.charge.result.TopupChargeItem;

import java.util.ArrayList;
import java.util.List;

public class ChargeInfo implements IChargeInfo {

    public static final int NO_OPERATOR_INDEX = -1;

    private List<Operator> mOperators;

    public ChargeInfo(List<TopupChargeItem> items) {
        if (items == null) {
            mOperators = new ArrayList<>();

            return;
        }

        mOperators = new ArrayList<>(items.size());

        for (TopupChargeItem item: items) {
            if (!item.isDisabled()) {
                mOperators.add(new Operator(this, item));
            }
        }
    }

    public List<Operator> getOperators() {
        return mOperators;
    }

    @Override
    public IOperator getOperator(String mobileNumber) {
        if (mobileNumber == null) {
            return null;
        }

        Operator operator = null;
        int longestMatch = 0;

        for (Operator op: mOperators) {
            int matchCount = op.getMatchWithNumber(mobileNumber);

            if (matchCount > longestMatch) {
                longestMatch = matchCount;

                operator = op;
            }
        }

        return operator;
    }

    @Override
    public int getOperatorIndex(String mobileNumber) {
        if (mobileNumber == null) {
            return NO_OPERATOR_INDEX;
        }

        int operatorIndex = NO_OPERATOR_INDEX;
        int longestMatch = 0;

        for (int i = 0; i < mOperators.size(); ++i) {
            Operator operator = mOperators.get(i);

            int matchCount = operator.getMatchWithNumber(mobileNumber);

            if (matchCount > longestMatch) {
                longestMatch = matchCount;

                operatorIndex = i;
            }
        }

        return operatorIndex;
    }

}
