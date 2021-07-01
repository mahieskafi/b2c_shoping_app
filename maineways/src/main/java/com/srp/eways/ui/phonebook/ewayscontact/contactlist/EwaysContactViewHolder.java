package com.srp.eways.ui.phonebook.ewayscontact.contactlist;

import android.view.View;

import com.srp.eways.base.BaseViewHolder;
import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.ui.bill.ShowMoreClickListener;
import com.srp.eways.ui.view.phonebook.EwaysContactItemView;
import com.srp.eways.util.Utils;

public class EwaysContactViewHolder extends BaseViewHolder<UserPhoneBook, View> {

    private EwaysContactListAdapter.UserPhoneBookClickListener mListener;

    public EwaysContactViewHolder(View itemView, EwaysContactListAdapter.UserPhoneBookClickListener listener) {
        super(itemView);
        mListener = listener;
    }

    @Override
    public void onBind(final UserPhoneBook item) {

        final EwaysContactItemView itemView = (EwaysContactItemView) getView();

        itemView.setPhoneNumber(item.getCellPhone());

        if (item.getCreateDate() != null) {

            String[] dateTime = item.getCreateDate().split(" ");
            String time = (dateTime[1].substring(0, 5));
            String date = dateTime[0];

            itemView.setDate(Utils.toPersianNumber(date));
            itemView.setTime(Utils.toPersianNumber(time));
        }

        itemView.setCredit(item.getDebt());
        itemView.setName(item.getFullName());
        if(item.getDescription()!= null && !item.getDescription().isEmpty()) {
            itemView.setDescription(item.getDescription());
        }else {
            itemView.setDescription("");
        }

        if (item.getShowMore() != null) {
            itemView.setShowMore(item.getShowMore());
        } else {
            itemView.setShowMore(false);
        }

        itemView.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onPhoneBookItemClicked(item);
                }
            }
        });

        itemView.setOnRemoveClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onPhoneBookItemRemoved(item);
                }
            }
        });

        itemView.setOnEditClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onPhoneBookItemEdited(item);
                }
            }
        });

        itemView.setOnShowMoreClickListener(new ShowMoreClickListener() {
            @Override
            public void onShowMoreClicked(boolean isShowMore) {
                if (mListener != null) {
                    mListener.onPhoneBookItemShowMore(isShowMore, item);
                }
            }
        });
    }
}
