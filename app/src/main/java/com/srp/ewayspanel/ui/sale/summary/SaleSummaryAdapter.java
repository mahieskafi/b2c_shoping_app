package com.srp.ewayspanel.ui.sale.summary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.srp.ewayspanel.R;
import com.srp.eways.di.DIMain;
import com.srp.ewayspanel.model.sale.BuySummaryItem;
import com.srp.eways.util.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by Eskafi on 1/12/2020.
 */
public class SaleSummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER_VIEW_TYPE = 0;
    private static final int ITEM_VIEW_TYPE = 1;

    private List<BuySummaryItem> mData;
    private Context mContext;

    public SaleSummaryAdapter(@NotNull Context context) {
        mData = new ArrayList<>();

        mContext = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == HEADER_VIEW_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_sale_summary_header, parent, false);
            viewHolder = new SaleSummaryHeaderViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sale_summary, parent, false);
            viewHolder = new SaleSummaryViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position != 0) {
            ((SaleSummaryViewHolder) holder).bind(getItemAtPosition(position - 1), position - 1);
        } else {
            ((SaleSummaryHeaderViewHolder) holder).bind();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_VIEW_TYPE;
        }
        return ITEM_VIEW_TYPE;
    }


    @Override
    public int getItemCount() {
        return getData().size() + 1;
    }

    public BuySummaryItem getItemAtPosition(int position) {
        return getData().get(position);
    }

    public List<BuySummaryItem> getData() {
        return mData;
    }

    public void setNewData(List<BuySummaryItem> newData) {
        mData = new ArrayList<>();

        mData.addAll(newData);
    }

    public class SaleSummaryViewHolder extends RecyclerView.ViewHolder {

        private BuySummaryItem mBuySummaryItem;
        private AppCompatTextView mProductNameText;
        private AppCompatTextView mCountText;
        private AppCompatTextView mAmountText;
        private LinearLayout mItemViewBackground;

        public SaleSummaryViewHolder(@NonNull View itemView) {
            super(itemView);

            mProductNameText = itemView.findViewById(R.id.tv_name);
            mCountText = itemView.findViewById(R.id.tv_count);
            mAmountText = itemView.findViewById(R.id.tv_amount);
            mItemViewBackground = itemView.findViewById(R.id.linear_buy_summary_item);
        }


        private void bind(BuySummaryItem buySummaryItem, int position) {
            mBuySummaryItem = buySummaryItem;

            IABResources AB = DIMain.getABResources();

            mProductNameText.setText(mBuySummaryItem.getGroups());
            mCountText.setText(Utils.toPersianNumber(mBuySummaryItem.getTotalRequestQuantity()));
            mAmountText.setText(Utils.toPersianPriceNumber(mBuySummaryItem.getTotalPaymentAmount()));

            if (position % 2 != 0) {
                mItemViewBackground.setBackgroundColor(AB.getColor(R.color.sale_summary_list_item_background_color));
            } else {
                mItemViewBackground.setBackgroundColor(AB.getColor(R.color.sale_summary_list_background_color));
            }
        }

    }

    public class SaleSummaryHeaderViewHolder extends RecyclerView.ViewHolder {
        public SaleSummaryHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        private void bind() {

        }
    }
}
