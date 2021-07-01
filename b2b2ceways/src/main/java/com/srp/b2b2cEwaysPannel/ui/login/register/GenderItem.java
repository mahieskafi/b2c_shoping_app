package com.srp.b2b2cEwaysPannel.ui.login.register;

import com.srp.b2b2cEwaysPannel.R;
import com.srp.b2b2cEwaysPannel.di.DI;
import com.srp.eways.ui.charge.buy.RadioOptionModel;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

public class GenderItem {

    private static final int MALE = 0;
    private static final int FEMALE = 1;

    private List<Item> mGenderItems;

    public GenderItem() {
        mGenderItems = new ArrayList<>();

        IABResources abResources = DI.getABResources();

        mGenderItems.add(new Item(MALE, abResources.getString(R.string.login_register_gender_dialog_male)));
        mGenderItems.add(new Item(FEMALE, abResources.getString(R.string.login_register_gender_dialog_female)));
    }

    public List<RadioOptionModel> getRadioList() {
        List<RadioOptionModel> list = new ArrayList<>();
        for (Item genderItem : mGenderItems) {
            list.add(new RadioOptionModel(genderItem.getTitle(), false, genderItem));
        }
        return list;
    }

    public int getGenderId(String title) {
        if (title.isEmpty()) {
            return MALE;
        }
        if (title.equals(mGenderItems.get(MALE).mTitle)) {
            return MALE;
        } else {
            return FEMALE;
        }
    }

    public class Item {
        private int mId;

        private String mTitle;

        public Item(int id, String title) {
            mId = id;
            mTitle = title;
        }

        public int getId() {
            return mId;
        }

        public String getTitle() {
            return mTitle;
        }
    }
}
