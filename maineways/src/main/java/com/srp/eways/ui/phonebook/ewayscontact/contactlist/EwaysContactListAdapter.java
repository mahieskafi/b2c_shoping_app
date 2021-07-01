package com.srp.eways.ui.phonebook.ewayscontact.contactlist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.srp.eways.R;
import com.srp.eways.base.BaseActivity;
import com.srp.eways.base.BaseRecyclerAdapter2;
import com.srp.eways.base.BaseViewHolder;
import com.srp.eways.model.phonebook.UserPhoneBook;
import com.srp.eways.ui.view.phonebook.EwaysContactItemView;

import org.jetbrains.annotations.NotNull;

public class EwaysContactListAdapter extends BaseRecyclerAdapter2<UserPhoneBook, BaseViewHolder> {

    public final static int VIEW_TYPE_CONTACT_ITEM = 0;
    public final static int VIEW_TYPE_SEARCHITEM = 3;

    public interface UserPhoneBookClickListener {

        void onPhoneBookItemClicked(UserPhoneBook userPhoneBook);

        void onPhoneBookItemRemoved(UserPhoneBook userPhoneBook);

        void onPhoneBookItemEdited(UserPhoneBook userPhoneBook);

        void onPhoneBookItemShowMore(boolean isShowMore, UserPhoneBook userPhoneBook);

    }

    private UserPhoneBookClickListener mListener;
    private int mViewType;

    public EwaysContactListAdapter(@NotNull Context context, @NotNull RetryClickListener retryListener, UserPhoneBookClickListener listener) {
        super(context, retryListener);

        mListener = listener;
        mViewType = VIEW_TYPE_CONTACT_ITEM;
    }

    @NotNull
    @Override
    public BaseViewHolder onCreateViewHolder2(@NotNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CONTACT_ITEM) {
            View itemView = new EwaysContactItemView(parent.getContext());
            itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new EwaysContactViewHolder(itemView, mListener);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_eways_serach_contact_view, parent, false);
            return new EwaysSearchContactViewHolder(itemView, mListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (position < getData().size()) {
            holder.onBind(getData().get(position));
        } else if (position == getItemCount()) {
            UserPhoneBook item = (UserPhoneBook) new Object();
            holder.onBind(item);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mViewType;
    }

    public void setViewType(int viewType) {
        mViewType = viewType;
    }
}
