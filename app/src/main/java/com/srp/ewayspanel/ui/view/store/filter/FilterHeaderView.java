package com.srp.ewayspanel.ui.view.store.filter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;

import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.eways.ui.charge.buy.RadioOptionModel;
import com.srp.eways.ui.view.NColumnRadioGroup;
import com.srp.eways.ui.view.SearchView;
import com.yashoid.sequencelayout.SequenceLayout;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by Eskafi on 11/10/2019.
 */
public class FilterHeaderView extends SequenceLayout {

    private SearchView mSearchView;
    private AppCompatTextView mAvailabilityTitle;
    private TwoColumnFilterRadioGroup mRadioGroup;
    private AppCompatTextView mCostTitle;
    private AppCompatTextView mCostCurrencyTitle;
    private FilterCostListView mFilterCostListView;

    private boolean mIsAvailability = false;
    private String mSearchValue = "";
    private Long mMinPrice = null;
    private Long mMaxPrice = null;

    public FilterHeaderView(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public FilterHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public FilterHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {

        setClipToPadding(false);
        setClipChildren(false);

        setFocusable(true);
        setFocusableInTouchMode(true);

        IABResources abResources = DI.getABResources();

        LayoutInflater.from(context).inflate(R.layout.view_filter_header, this, true);

        mSearchView = findViewById(R.id.search_view);
        mAvailabilityTitle = findViewById(R.id.text_title_availability);
        mRadioGroup = findViewById(R.id.radio_group);
        mCostTitle = findViewById(R.id.text_title_cost);
        mCostCurrencyTitle = findViewById(R.id.text_title_cost_value);
        mFilterCostListView = findViewById(R.id.filter_cost_list_view);

        addSequences(R.xml.sequences_filter_header);

        mSearchView.setHint(abResources.getString(R.string.filter_header_search_hint));
        mSearchView.setDonKeyboard(true);
        ViewCompat.setElevation(mSearchView, abResources.getDimen(R.dimen.filter_header_search_elevation));

        mSearchView.setSearchViewListener(new SearchView.SearchViewListener() {
            @Override
            public void onSearchTextChanged(String searchText) {
                mSearchValue = searchText;
            }
        });

        mAvailabilityTitle.setTextColor(abResources.getColor(R.color.filter_header_title_availability_text_color));
        mAvailabilityTitle.setTypeface(ResourcesCompat.getFont(context, R.font.iran_yekan_bold));
        mAvailabilityTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.filter_header_title_availability_text_size));
        mAvailabilityTitle.setText(abResources.getString(R.string.filter_header_title_availability_text));

        mRadioGroup.setData(getRadioOptionList());

        mRadioGroup.setOnItemSelectedListener(new NColumnRadioGroup.RadioGroupListener() {

            @Override
            public void onItemSelected(int index, RadioOptionModel data) {
                if ((Boolean) data.option) {
                    mIsAvailability = true;
                } else {
                    mIsAvailability = false;
                }
            }

            @Override
            public void onItemRemoved(int index, RadioOptionModel removedData) {
            }

        });

        mCostTitle.setTextColor(abResources.getColor(R.color.filter_header_title_cost_title_text_color));
        mCostTitle.setTypeface(ResourcesCompat.getFont(context, R.font.iran_yekan_bold));
        mCostTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.filter_header_title_cost_text_size));
        mCostTitle.setText(abResources.getString(R.string.filter_header_title_cost));

        mCostCurrencyTitle.setTextColor(abResources.getColor(R.color.filter_header_title_cost_title_text_color));
        mCostCurrencyTitle.setTypeface(ResourcesCompat.getFont(context, R.font.iran_yekan));
        mCostCurrencyTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, abResources.getDimenPixelSize(R.dimen.filter_header_title_cost_currency_text_size));
        mCostCurrencyTitle.setText(abResources.getString(R.string.filter_header_title_cost_currency));

        mFilterCostListView.setData(getCostList());

        mFilterCostListView.setListener(new FilterCostListView.ItemClickListener() {
            @Override
            public void onItemClicked(FilterCostListView.CostItem costItem) {
                if (costItem.isSelected()) {
                    mMinPrice = costItem.getMin();
                    mMaxPrice = costItem.getMax();
                } else {
                    mMinPrice = null;
                    mMaxPrice = null;
                }
            }
        });
    }

    public List<RadioOptionModel> getRadioOptionList() {
        IABResources abResources = DI.getABResources();

        List<RadioOptionModel> list = new ArrayList<>();
        list.add(new RadioOptionModel(abResources.getString(R.string.filter_header_title_availability_option1), false, false));
        list.add(new RadioOptionModel(abResources.getString(R.string.filter_header_title_availability_option2), false, true));

        return list;
    }

    private List<FilterCostListView.CostItem> getCostList() {

        IABResources abResources = DI.getABResources();

        List<FilterCostListView.CostItem> costItems = new ArrayList<>();
        costItems.add(new FilterCostListView.CostItem(1000000, 500000,
                abResources.getString(R.string.one_million), abResources.getString(R.string.five_hundred_thousand), false));
        costItems.add(new FilterCostListView.CostItem(500000, 100000,
                abResources.getString(R.string.five_hundred_thousand), abResources.getString(R.string.one_hundred_thousand), false));
        costItems.add(new FilterCostListView.CostItem(100000, 1000,
                abResources.getString(R.string.one_hundred_thousand), abResources.getString(R.string.one_thousand), false));
        costItems.add(new FilterCostListView.CostItem(0, 50000000,
                abResources.getString(R.string.upper), abResources.getString(R.string.fifty_million), false));
        costItems.add(new FilterCostListView.CostItem(50000000, 10000000,
                abResources.getString(R.string.fifty_million), abResources.getString(R.string.ten_million), false));
        costItems.add(new FilterCostListView.CostItem(10000000, 1000000,
                abResources.getString(R.string.ten_million), abResources.getString(R.string.one_million), false));

        return costItems;
    }

    public boolean IsAvailability() {
        return mIsAvailability;
    }

    public String getSearchValue() {
        return mSearchValue;
    }

    public Long getMinPrice() {
        return mMinPrice;
    }

    public Long getMaxPrice() {
        return mMaxPrice;
    }

    public void setSearchText(String searchText) {
        mSearchValue = searchText;

        mSearchView.setText(mSearchValue);
    }

    public void setIsAvailability(boolean isAvailability) {
        mIsAvailability = isAvailability;

        if (mIsAvailability) {
            mRadioGroup.setSelectedRadioButton(1);
        } else {
            mRadioGroup.setSelectedRadioButton(0);
        }
    }

    public void setMinPrice(long minPrice) {

        List<FilterCostListView.CostItem> costItems = getCostList();

        for (int i = 0; i < costItems.size(); i++) {
            if (minPrice == costItems.get(i).getMin()) {
                mFilterCostListView.setSelectedItem(i);
                return;
            }
        }

    }

    public void cancelAllFilter() {
        mSearchValue = "";
        mMinPrice = null;
        mMaxPrice = null;
        mIsAvailability = false;

        mSearchView.setText("");
        mFilterCostListView.setData(getCostList());
        mRadioGroup.setData(getRadioOptionList());
    }
}