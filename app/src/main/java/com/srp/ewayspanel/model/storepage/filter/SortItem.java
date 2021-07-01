package com.srp.ewayspanel.model.storepage.filter;

import com.srp.ewayspanel.R;
import com.srp.ewayspanel.di.DI;
import com.srp.eways.ui.charge.buy.RadioOptionModel;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by Eskafi on 11/12/2019.
 */
public class SortItem {

    private static final int NEWEST = 0;
    private static final int CHEAPEST = 1;
    private static final int BESTSELLING = 2;
    private static final int MOSTSEEN = 3;
    private static final int MOSTEXPENSIVE = 4;

    private List<Item> mSortItems;

    public SortItem() {
        mSortItems = new ArrayList<>();

        IABResources abResources = DI.getABResources();

        mSortItems.add(new Item(CHEAPEST, abResources.getString(R.string.filter_sort_item_price), 5, 2));
        mSortItems.add(new Item(MOSTEXPENSIVE, abResources.getString(R.string.filter_sort_item_most_expensive), 5, 1));
        mSortItems.add(new Item(BESTSELLING, abResources.getString(R.string.filter_sort_item_selling), 1, 1));
        mSortItems.add(new Item(MOSTSEEN, abResources.getString(R.string.filter_sort_item_most_seen), 3, 1));
        mSortItems.add(new Item(NEWEST, abResources.getString(R.string.filter_sort_item_time), 4, 2));
    }

    public List<RadioOptionModel> getRadioList() {
        List<RadioOptionModel> list = new ArrayList<>();
        for (Item sortItem : mSortItems) {
            list.add(new RadioOptionModel(sortItem.getTitle(), false, sortItem));
        }
        return list;
    }

    public int getSelectedPosition(int selectedItemOrder, int selectedItemSort) {
        int position = -1;
        for (int i = 0; i < mSortItems.size(); i++) {
            if (mSortItems.get(i).getOrder() == selectedItemOrder && mSortItems.get(i).getSort() == selectedItemSort) {
                position = i;
            }
        }
        return position;
    }

    public class Item {
        private int mId;

        private String mTitle;

        private int mOrder;

        private int mSort;

        public Item(int id, String title, int order, int sort) {
            mId = id;
            mTitle = title;
            mOrder = order;
            mSort = sort;
        }

        public int getId() {
            return mId;
        }

        public String getTitle() {
            return mTitle;
        }

        public int getOrder() {
            return mOrder;
        }

        public int getSort() {
            return mSort;
        }

    }
}
