package com.srp.eways.ui.bill.archive;

import android.view.View;

import com.srp.eways.base.BaseViewHolder;
import com.srp.eways.model.bill.archivedList.BillTemp;
import com.srp.eways.ui.bill.ShowMoreClickListener;
import com.srp.eways.ui.bill.archive.billitemview.BillItemView;
import com.srp.eways.util.BillUtil;
import com.srp.eways.util.Utils;

import static com.srp.eways.util.BillUtil.SERVICE_ELECTRICITY;
import static com.srp.eways.util.BillUtil.SERVICE_GAS;
import static com.srp.eways.util.BillUtil.SERVICE_WATER;

public class BillArchiveListViewHolder extends BaseViewHolder<BillTemp, View> {

    private BillArchiveAdapter.BillArchivedItemListener mListener;

    public BillArchiveListViewHolder(View itemView, BillArchiveAdapter.BillArchivedItemListener listener) {
        super(itemView);
        mListener = listener;
    }

    @Override
    public void onBind(final BillTemp item) {

        final BillItemView itemView = (BillItemView) getView();

        itemView.setNumber(item.getBillId());

        if (item.getLogDate() != null) {

            String[] dateTime = item.getLogDate().split(" ");
            String time = (dateTime[1].substring(0, 5));
            String date = dateTime[0];

            itemView.setDate(Utils.toPersianNumber(date));
            itemView.setTime(Utils.toPersianNumber(time));
        }

        itemView.setPrice(item.getPrice());

        String title = BillUtil.getServiceName(item.getBillTypeId());

        switch (item.getBillTypeId()){

            case SERVICE_WATER:
            case SERVICE_ELECTRICITY:
            case SERVICE_GAS:
                title = "قبض " + title;
        }

        itemView.setTitle(title);
        itemView.setIcon(BillUtil.getServiceIcon(item.getBillTypeId()));
        itemView.setIconBackgroundColor(BillUtil.getServiceColor(item.getBillTypeId()));

        itemView.setItemSelected(item.isSelected());
        itemView.setShowMore(item.isShowMore());

        itemView.setPayId(item.getPaymentId());
        itemView.setInquiryId(item.getInquiryNumber());

        itemView.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onBillShowMore(!item.isShowMore(), item);
                }
                itemView.setShowMore(item.isShowMore());
            }
        });

        itemView.setOnDeleteClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onBillRemoved(item.getId());
                }
            }
        });

        itemView.setOnCheckboxClickListener(new BillItemView.OnCheckboxClickListener() {
            @Override
            public void onCheckClicked(boolean isChecked) {
                if (mListener != null) {
                    mListener.onBillSelected(isChecked, item);
                }
            }
        });

        itemView.setOnShowMoreClickListener(new ShowMoreClickListener() {
            @Override
            public void onShowMoreClicked(boolean isShowMore) {
                if (mListener != null) {
                    mListener.onBillShowMore(isShowMore, item);
                }
            }
        });
    }
}
