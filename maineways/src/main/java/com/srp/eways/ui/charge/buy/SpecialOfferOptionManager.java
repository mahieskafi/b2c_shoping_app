package com.srp.eways.ui.charge.buy;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.ui.charge.ChargeSelectionState;
import com.srp.eways.ui.charge.buy.b.BuyChargeFragmentB;
import com.srp.eways.ui.charge.model.IChargeOption;
import com.srp.eways.ui.charge.model.IChargeOptionViewType;
import com.srp.eways.ui.charge.model.IOperator;
import com.srp.eways.ui.view.EmptyView;
import com.srp.eways.ui.view.LoadingStateView;

import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

public class SpecialOfferOptionManager implements ChargeOptionManager {

    private int mViewType;

    private RadioOptionManager mChoiceOptionManager;

    public SpecialOfferOptionManager(int viewType) {
        mViewType = viewType;

        mChoiceOptionManager = new RadioOptionManager(1);
    }

    @Override
    public View createChargeOption(Activity activity, int level, List<ChargeSelectionState.ChargeLevelInfo> selectionState, List options, ChargeOptionsCallback callback) {
        switch (mViewType) {
            case IChargeOptionViewType.VIEWTYPE_IRANCELL_SPECIALOFFER_LOADING:
                return createLoadingView(activity);
            case IChargeOptionViewType.VIEWTYPE_IRANCELL_SPECIALOFFER_FAILED:
                return createFailedView(activity, level, selectionState, callback);
            case IChargeOptionViewType.VIEWTYPE_IRANCELL_SPECIALOFFER_EMPTY:
                return createEmptyView(activity);
            case IChargeOptionViewType.VIEWTYPE_IRANCELL_SPECIALOFFER_CHOICE:

                View view = createChoiceView(activity, level, selectionState, options, callback);
                view.setLayoutParams(BuyChargeFragmentB.getTabChildViewLayoutParams());
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
                IABResources abResources = DIMain.getABResources();
                int mTabViewPaddingTop = abResources.getDimenPixelSize(R.dimen.chargeoptionstabview_contentpadding_top);
                int mTabIconHeight = abResources.getDimenPixelSize(R.dimen.chargeoptionstabview_tabicon_height);
                int mTabViewPaddingBottom = abResources.getDimenPixelSize(R.dimen.chargeoptionstabview_contentpadding_bottom);
                layoutParams.topMargin = (int) (- (float)3/4 * (mTabViewPaddingTop + mTabIconHeight + mTabViewPaddingBottom));//Todo: fix margin issue. I don't know why it's not working!
                view.setLayoutParams(layoutParams);

                int paddingTop = (int) ((float)1/4 * (-((LinearLayout.LayoutParams)view.getLayoutParams()).topMargin + DIMain.getABResources().getDimenPixelSize(R.dimen.chargeoptionstabview_children_margintop)));
                view.setPadding(view.getPaddingLeft(), paddingTop, view.getPaddingRight(), view.getPaddingBottom());

                return view;
        }

        throw new RuntimeException("Unsupported viewType: " + mViewType);
    }

    private View createLoadingView(Activity activity) {
        FrameLayout container = new FrameLayout(activity);

        LoadingStateView loadingStateView = new LoadingStateView(activity);
        loadingStateView.setStateAndDescription(LoadingStateView.STATE_LOADING, DIMain.getABResources().getString(R.string.buycharge_loading_specialoffers), false);

        container.addView(loadingStateView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        return container;
    }

    private View createFailedView(Activity activity, final int level, final List<ChargeSelectionState.ChargeLevelInfo> selectionState, final ChargeOptionsCallback callback) {
        LoadingStateView loadingStateView = new LoadingStateView(activity);

        loadingStateView.setStateAndDescription(LoadingStateView.STATE_ERROR, DIMain.getABResources().getString(R.string.buycharge_failed_specialoffers), true);

        loadingStateView.setRetryListener(new LoadingStateView.RetryListener() {

            @Override
            public void onRetryButtonClicked() {
                callback.onChargeOptionSelected(level - 2, getLevelOptions(selectionState, level - 2).size() - 1, selectionState.get(level - 2).selectedOption);
            }

        });

        return loadingStateView;
    }

    private View createEmptyView(Activity activity) {
        EmptyView emptyView = new EmptyView(activity);

        emptyView.setEmptyText(R.string.buycharge_no_specialoffers);

        return emptyView;
    }

    private View createChoiceView(Activity activity, int level, List<ChargeSelectionState.ChargeLevelInfo> selectionState, List options, ChargeOptionsCallback callback) {
        return mChoiceOptionManager.createChargeOption(activity, level, selectionState, options, callback);
    }

    @Override
    public void setSelectedOption(View view, List options, int selectedIndex, Object... args) {
        switch (mViewType) {
            case IChargeOptionViewType.VIEWTYPE_IRANCELL_SPECIALOFFER_LOADING:
                ((ViewGroup) view).getChildAt(0).setVisibility(selectedIndex == -1 ? View.VISIBLE : View.GONE);
                return;
            case IChargeOptionViewType.VIEWTYPE_IRANCELL_SPECIALOFFER_FAILED:
            case IChargeOptionViewType.VIEWTYPE_IRANCELL_SPECIALOFFER_EMPTY:
                // IT SHALL NOT HAPPEN!
                return;
            case IChargeOptionViewType.VIEWTYPE_IRANCELL_SPECIALOFFER_CHOICE:
                mChoiceOptionManager.setSelectedOption(view, options, selectedIndex, args);
                return;
        }

        throw new RuntimeException("Unsupported viewType: " + mViewType);
    }

    private List<?> getLevelOptions(List<ChargeSelectionState.ChargeLevelInfo> chargeLevelsInfo, int level) {
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

}
