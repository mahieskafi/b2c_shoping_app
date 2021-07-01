package com.srp.eways.ui.phonebook.mobilecontact;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.srp.eways.base.BaseRecyclerAdapter;
import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.ui.charge.buy.ContactItemView;

import org.jetbrains.annotations.NotNull;

public class MobileContactListAdapter  extends BaseRecyclerAdapter<UserPhoneBook, MobileContactListAdapter.PhoneBookItemViewHolder> {


    public interface MobilePhoneBookClickListener {

        void onPhoneBookItemClicked(UserPhoneBook userPhoneBook);

    }

    private MobilePhoneBookClickListener mListener;

    public MobileContactListAdapter(@NotNull Context context, RetryClickListener retryClickListener, MobilePhoneBookClickListener listener) {
        super(context, retryClickListener, false);

        mListener = listener;
    }

    @NotNull
    @Override
    public PhoneBookItemViewHolder onCreateViewHolder2(@NotNull ViewGroup parent, int viewType) {
        View itemView = new ContactItemView(parent.getContext());
        itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return new PhoneBookItemViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder2(@NotNull RecyclerView.ViewHolder holder, int position) {
        ((PhoneBookItemViewHolder) holder).bind(getItemAtPosition(position));
    }

    public UserPhoneBook getItemAtPosition(int position) {
        return getData().get(position);
    }

    public static class PhoneBookItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ContactItemView mContactItemView;

        private UserPhoneBook mData;

        private MobilePhoneBookClickListener mListener;

        public PhoneBookItemViewHolder(@NonNull View itemView, MobilePhoneBookClickListener listener) {
            super(itemView);

            mContactItemView = (ContactItemView) itemView;

            mListener = listener;

            itemView.setOnClickListener(this);
        }

        private void bind(UserPhoneBook data) {
            mData = data;

            mContactItemView.setData(new UserPhoneBook(mData.getFirstName() + " " + mData.getLastName(), mData.getCellPhone()));
        }

        @Override
        public void onClick(View v) {
            mListener.onPhoneBookItemClicked(mData);
        }
    }
}
