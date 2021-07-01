package com.srp.eways.ui.bill.archive;

import android.content.Context;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.srp.eways.base.BaseRecyclerAdapter2;
import com.srp.eways.base.BaseViewHolder;
import com.srp.eways.model.bill.archivedList.BillTemp;
import com.srp.eways.ui.bill.archive.billitemview.BillItemView;


public class BillArchiveAdapter extends BaseRecyclerAdapter2<BillTemp, BaseViewHolder> {

    public interface BillArchivedItemListener {
        void onBillClicked(BillTemp billTable);

        void onBillRemoved(int id);

        void onBillSelected(boolean isChecked, BillTemp billTable);

        void onBillShowMore(boolean isShowMore, BillTemp billTable);
    }

    private BillArchivedItemListener mBillArchivedItemListener;

    public BillArchiveAdapter(Context mContext, BillArchivedItemListener billArchivedItemListener, RetryClickListener mRetryClickListener) {
        super(mContext, mRetryClickListener);

        mBillArchivedItemListener = billArchivedItemListener;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (position < getData().size()) {
            holder.onBind(getData().get(position));
        } else if (position == getItemCount()) {
            BillTemp item = (BillTemp) new Object();
            holder.onBind(item);
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new BillArchiveListViewHolder(new BillItemView(parent.getContext()), mBillArchivedItemListener);
    }

}
