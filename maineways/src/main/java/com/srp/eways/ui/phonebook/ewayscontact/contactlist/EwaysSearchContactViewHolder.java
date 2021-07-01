package com.srp.eways.ui.phonebook.ewayscontact.contactlist;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.srp.eways.R;
import com.srp.eways.base.BaseViewHolder;
import com.srp.eways.di.DIMain;
import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.ui.view.phonebook.EwaysContactDetailItemView;

import ir.abmyapp.androidsdk.IABResources;

public class EwaysSearchContactViewHolder extends BaseViewHolder<UserPhoneBook, View> {

    private EwaysContactListAdapter.UserPhoneBookClickListener mListener;

    public EwaysSearchContactViewHolder(View itemView, EwaysContactListAdapter.UserPhoneBookClickListener listener) {
        super(itemView);
        mListener = listener;
    }

    @Override
    public void onBind(final UserPhoneBook item) {

        IABResources resources= DIMain.getABResources();

        final EwaysContactDetailItemView itemView = getView().findViewById(R.id.detail);

        itemView.setPhoneNumber(item.getCellPhone());
        itemView.setName(item.getFirstName() + " " + item.getLastName());
        itemView.setCreditVisibility(View.GONE);
        itemView.setBackground(resources.getDrawable(R.drawable.eways_contact_detail_item_view_unselected_background));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,
                resources.getDimenPixelSize(R.dimen.userphonebook_serach_items_margin_top), 0, 0);
        itemView.setLayoutParams(layoutParams);

        itemView.setItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onPhoneBookItemClicked(item);
                }
            }
        });

        itemView.setRemoveClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onPhoneBookItemRemoved(item);
                }
            }
        });

        itemView.setEditClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onPhoneBookItemEdited(item);
                }
            }
        });

    }
}
