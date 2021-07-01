package com.srp.eways.ui.deposit.increasedeposit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.srp.eways.R;
import com.srp.eways.util.Utils;

/**
 * Created by Eskafi on 9/2/2019.
 */
public class AmountAdapter extends RecyclerView.Adapter<AmountAdapter.ViewHolder> {

    private String[] mData;
    private LayoutInflater mInflater;
    private ItemClickListener itemClickListener;

    AmountAdapter(Context context, String[] data, ItemClickListener listener) {
        mInflater = LayoutInflater.from(context);
        mData = data;
        itemClickListener = listener;
    }

    @Override
    @NonNull
    public AmountAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_amount, parent, false);
        return new AmountAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmountAdapter.ViewHolder holder, int position) {
        holder.myTextView.setText(Utils.toPersianPriceNumber(mData[position]));
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.txt_amount);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
