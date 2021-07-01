package com.srp.eways.ui.charge;

import com.srp.eways.ui.charge.model.ChargeInfo;
import com.srp.eways.ui.charge.model.IOperator;

import java.util.ArrayList;
import java.util.List;

public class ChargeSelectionState {

    public static final int DEFAULT_MAX_CHARGE_OPTION_DEPTH = 5;

    public static final int NO_CHARGE_CHOICE_SELECTED = -1;

    public static final int NO_CHARGE_OPTION_SELECTED = -1;

    private List<ChargeLevelInfo> mChargeLevelSelectionInfo;

    public ChargeSelectionState() {
        mChargeLevelSelectionInfo = new ArrayList<>(DEFAULT_MAX_CHARGE_OPTION_DEPTH);
    }

    public void onOperatorSelected(int selectedOperatorIndex, IOperator selectedOperator) {
        if (selectedOperatorIndex == ChargeInfo.NO_OPERATOR_INDEX) {
            mChargeLevelSelectionInfo.clear();

            return;
        }

        onLevelOptionSelected(0, selectedOperatorIndex, selectedOperator);
    }

    public void onLevelOptionSelected(int level, int selectedChargeOptionIndex, Object chargeOption) {
        while (level < mChargeLevelSelectionInfo.size()) {
            mChargeLevelSelectionInfo.remove(level);
        }

        mChargeLevelSelectionInfo.add(new ChargeLevelInfo(level, selectedChargeOptionIndex, chargeOption));
    }

//    public void onChargeChoiceSelected(int selectedChargeChoiceIndex) {
//        if (selectedChargeChoiceIndex == NO_CHARGE_CHOICE_SELECTED) {
//            return;
//        }
//
//        mChargeLevelSelectionInfo.add(new ChargeLevelInfo(mChargeLevelSelectionInfo.size(), selectedChargeChoiceIndex));
//    }

    public int getLevelCount() {
        return mChargeLevelSelectionInfo.size();
    }

    public int getSelectionIndexAtLevel(int level) {
        int selectionIndex;

        ChargeLevelInfo chargeLevelInfo = mChargeLevelSelectionInfo.get(level);

        selectionIndex = chargeLevelInfo == null ? NO_CHARGE_OPTION_SELECTED :chargeLevelInfo.chargeOptionIndex;

        return selectionIndex;
    }

    public ChargeLevelInfo getLastLevelSelectionInfo() {
        return mChargeLevelSelectionInfo.get(getLevelCount() - 1);
    }

    public List<ChargeLevelInfo> getChargeLevelSelectionInfo() {
        return mChargeLevelSelectionInfo;
    }

    public class ChargeLevelInfo {
        public static final int LEVEL_NEXTLEVEL = - 1000;
        public final int level;
        public final int chargeOptionIndex;
        public Object selectedOption;

        public ChargeLevelInfo(int level, int chargeOptionIndex, Object selectedOption) {
            this.level = level;
            this.chargeOptionIndex = chargeOptionIndex;
            this.selectedOption = selectedOption;
        }
    }

}
