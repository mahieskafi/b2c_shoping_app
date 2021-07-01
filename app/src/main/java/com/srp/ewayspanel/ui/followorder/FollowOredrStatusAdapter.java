package com.srp.ewayspanel.ui.followorder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.srp.ewayspanel.R;
import com.srp.ewayspanel.model.followOrder.FollowOredrStatusModel;

import java.util.List;

public class FollowOredrStatusAdapter
        extends RecyclerView.Adapter<FollowOredrStatusAdapter.ViewHolder> {

    private List<FollowOredrStatusModel> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    FollowOredrStatusAdapter(Context context, List<FollowOredrStatusModel> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.follow_order_status_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mStatusTextView.setText(mData.get(position).getmText());
        holder.isClick=(mData.get(position).getmIsClick());
        
        if (holder.isClick){
            holder.mStatusTextView.setTextColor(Color.parseColor("#1ebf39"));
            holder.mStatusTextView.setBackgroundResource(R.drawable.follow_order_button_background_enable);
        }else {
            holder.mStatusTextView.setTextColor(Color.parseColor("#979797"));
            holder.mStatusTextView.setBackgroundResource(R.drawable.follow_order_button_disable_background);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    FollowOredrStatusModel getItem(int id) {
        return mData.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mStatusTextView;
        Boolean isClick;

        ViewHolder(View itemView) {
            super(itemView);
            mStatusTextView = itemView.findViewById(R.id.textview11);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

}