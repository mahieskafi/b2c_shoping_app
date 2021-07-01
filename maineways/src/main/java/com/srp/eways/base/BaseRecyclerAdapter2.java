package com.srp.eways.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.srp.eways.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eskafi on 11/2/2019.
 */
public abstract class BaseRecyclerAdapter2<T, VH extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {

    public interface RetryClickListener {
        void onClicked();
    }

    public final static int VIEW_TYPE_ITEM = 0;
    public final static int VIEW_TYPE_LOADING_LOAD_MORE = 1;

    private Context mContext;

    private List<T> mData;
    private boolean mHasPaging;
    private RetryClickListener mRetryClickListener;

    private boolean mIsMoreDataLoadingAdded = false;

    public BaseRecyclerAdapter2(Context mContext) {
        this.mContext = mContext;
    }

    public BaseRecyclerAdapter2(Context mContext, List<T> data) {
        this.mContext = mContext;
        mData = new ArrayList<>();
        mData = data;
        mRetryClickListener = null;
        mHasPaging = false;
    }

    public BaseRecyclerAdapter2(Context mContext, RetryClickListener mRetryClickListener) {
        mData = new ArrayList<>();
        this.mContext = mContext;
        this.mRetryClickListener = mRetryClickListener;
    }

    public BaseRecyclerAdapter2(Context mContext, List<T> data, boolean mHasPaging, RetryClickListener mRetryClickListener) {
        this.mContext = mContext;
        mData = new ArrayList<>();
        mData = data;
        this.mHasPaging = mHasPaging;
        this.mRetryClickListener = mRetryClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == getItemCount() - 1 && mIsMoreDataLoadingAdded ? VIEW_TYPE_LOADING_LOAD_MORE : VIEW_TYPE_ITEM);
    }


    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING_LOAD_MORE) {
            return new LoadingViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.item_loading, parent, false));
        }
        return onCreateViewHolder2(parent, viewType);
    }


    public abstract VH onCreateViewHolder2(ViewGroup parent, int viewType);

    public void setNewData(List<T> data) {
        mData = new ArrayList<>();
        if (data != null) {
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void appendData(List<T> data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        if (mIsMoreDataLoadingAdded) {
            removeMoreDataLoading();
        }
        mData.addAll(data);
        notifyItemInserted(mData.size() - 1);
    }

    public List<T> getData() {
        return mData;
    }

    @Override
    public int getItemCount() {
        if (mIsMoreDataLoadingAdded) {
            return mData.size() + 1;
        }
        return mData.size();
    }

    public void addMoreDataLoading() {
        mIsMoreDataLoadingAdded = true;

        notifyDataSetChanged();
    }

    public void removeMoreDataLoading() {
        if (mIsMoreDataLoadingAdded) {
            mIsMoreDataLoadingAdded = false;

            notifyDataSetChanged();
        }
    }

    public void setHasMoreData(boolean hasMore) {
        if (hasMore) {
            addMoreDataLoading();
        } else {
            removeMoreDataLoading();
        }
    }

    public void notifyDataRemovedAt(int position) {
        if (position < mData.size()) {
            mData.remove(mData.get(position));
            notifyItemRemoved(position);
        }
    }

    public void clearData() {
        mData = new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(getData().get(position));
    }

    public class LoadingViewHolder extends BaseViewHolder {
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(Object item) {

        }
    }

}
