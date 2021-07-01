package com.srp.eways.ui.charge.buy;

import android.app.Activity;
import android.view.View;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.charge.ChargeSelectionState;
import com.srp.eways.ui.charge.model.IChargeChoice;
import com.srp.eways.ui.charge.model.IChargeOption;
import com.srp.eways.ui.view.charge.a.ChargeOptionsTabViewA;
import com.srp.eways.ui.view.charge.TabOptionsView;
import com.srp.eways.ui.view.charge.b.ChargeOptionsTabViewB;

import java.util.ArrayList;
import java.util.List;

public class TabOptionManager implements ChargeOptionManager {

    @Override
    public View createChargeOption(Activity activity, final int level, List<ChargeSelectionState.ChargeLevelInfo> selectionState, final List options, final ChargeOptionsCallback callback) {
        final TabOptionsView tabView;

        int mDesignModel = DIMain.getABResources().getInt(R.integer.abtesting_designmodel);
        if (mDesignModel == 0) {//A
            tabView = new ChargeOptionsTabViewA(activity);
        } else {//B
            tabView = new ChargeOptionsTabViewB(activity);
        }

        updateOptions(options, tabView);

        tabView.setOnChargeTabClickListener(new TabOptionsView.ChargeOptionsTabViewListener() {

            @Override
            public void onChargeTabClicked(int tabIndex, TabOptionsView.TabItem tabItem) {
                callback.onChargeOptionSelected(level, tabIndex, tabItem.data);
            }

        });

        return tabView;
    }

    @Override
    public void setSelectedOption(View view, List options, int selectedIndex, Object... args) {
        updateOptions(options, (TabOptionsView) view);

        if (selectedIndex >= 0) {
            ((TabOptionsView) view).setSelectedTab(selectedIndex);
        }
        else {
            if (!options.isEmpty()) {
                ((TabOptionsView) view).setSelectedTabAndNotify(0);
            }
        }
    }

    private void updateOptions(List options, TabOptionsView tabView) {
        int designModel = DIMain.getABResources().getInt(R.integer.abtesting_designmodel);

        int operatorColor = R.color.operator_irancell;

        String[] tabTitles = new String[options.size()];
        int[] tabIcons = new int[options.size()];

        for (int i = 0; i < options.size(); ++i) {
            Object option = options.get(i);

            int iconResId = 0;

            if (option instanceof IChargeOption) {
                String tabTitle = removeOperatorNamesFromTitle(((IChargeOption) option).getName());
                tabTitles[i] = tabTitle;
                iconResId = ((IChargeOption) option).getProductTypeIconResId();

                operatorColor = ((IChargeOption) option).getOperator().getOperatorServiceColor();
            } else {
                tabTitles[i] = ((IChargeChoice)option).getName();
//                iconResId = ((IChargeOption) option).getProductTypeIconResId();//Todo
//                operatorColor = ((IChargeOption) option).getOperator().getOperatorColor();
            }

            tabIcons[i] = iconResId;
        }

        if (designModel == 0) {//A
            tabView.setTabBackgroundColor(DIMain.getABResources().getColor(operatorColor));
        }
        else if (designModel == 1){//B
            tabView.setTabBackgroundColor(DIMain.getABResources().getColor(R.color.taboptionsview_background_b));
        }

//        tabView.setTabTitles(tabTitles);
//        tabView.setTabIcons(tabIcons);

        List<TabOptionsView.TabItem> tabItems = new ArrayList<>();
        for (int i = 0; i < options.size(); ++i) {
            tabItems.add(new TabOptionsView.TabItem(tabTitles[i], tabIcons[i], options.get(i)));
        }
        tabView.setTabItems(tabItems);
    }

    private String removeOperatorNamesFromTitle(String title) {
        title = title.replace("ایرانسل", "");
        title = title.replace("همراه اول", "");
        title = title.replace("رایتل", "");

        return title;
    }

}
