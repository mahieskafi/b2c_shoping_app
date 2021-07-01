package com.srp.ewayspanel.ui.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.srp.eways.base.BaseRecyclerListener;
import com.srp.eways.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eskafi on 7/22/2019.
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    private List<T> mItems;
    private BaseRecyclerListener mListener;
    private LayoutInflater mLayoutInflater;

    public BaseRecyclerViewAdapter(Context context, BaseRecyclerListener mListener) {

        mListener = mListener;
        mItems = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public BaseRecyclerViewAdapter(Context context) {

        mItems = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public abstract BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        if (mItems.size() <= position) {

            return;
        }

        T item = mItems.get(position);
        holder.onBind(item);
    }

    @Override
    public int getItemCount() {

        return mItems != null ? mItems.size() : 0;
    }

    public void setItems(List<T> mItems, boolean notifyChanges) throws IllegalArgumentException {

        if (mItems == null) {

            throw new IllegalArgumentException("Cannot set `null` item to the Recycler adapter");
        }
        mItems.clear();
        mItems.addAll(mItems);

        if (notifyChanges) {

            notifyDataSetChanged();
        }
    }

    public void updateItems(List<T> newItems) {

        setItems(newItems, false);
    }

    public void updateItems(List<T> newItems, DiffUtil.Callback diffCallback) {

        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffCallback, false);
        setItems(newItems, false);
        result.dispatchUpdatesTo(this);
    }

    public List<T> getItems() {

        return mItems;
    }

    public void setItems(List<T> mItems) {

        setItems(mItems, true);
    }

    public T getItem(int position) {

        return mItems.get(position);
    }

    public void add(T item) {

        if (item == null) {

            throw new IllegalArgumentException("Cannot add null item to the Recycler adapter");
        }
        mItems.add(item);
        notifyItemInserted(mItems.size() - 1);
    }

    public void addToBeginning(T item) {

        if (item == null) {

            throw new IllegalArgumentException("Cannot add null item to the Recycler adapter");
        }
        mItems.add(0, item);
        notifyItemInserted(0);
    }

    public void addAll(List<T> mItems) {

        if (mItems == null) {

            throw new IllegalArgumentException("Cannot add `null` mItems to the Recycler adapter");
        }
        this.mItems.addAll(mItems);
        notifyItemRangeInserted(this.mItems.size() - mItems.size(), mItems.size());
    }

    public void clear() {

        mItems.clear();
        notifyDataSetChanged();
    }

    public void remove(T item) {

        int position = mItems.indexOf(item);

        if (position > -1) {

            mItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isEmpty() {

        return getItemCount() == 0;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {

        super.setHasStableIds(hasStableIds);
    }

    public BaseRecyclerListener getListener() {

        return mListener;
    }

    public void setListener(BaseRecyclerListener mListener) {

        this.mListener = mListener;
    }

    @NonNull
    protected View inflate(@LayoutRes final int layout, @Nullable final ViewGroup parent, final boolean attachToRoot) {

        return mLayoutInflater.inflate(layout, parent, attachToRoot);
    }

    @NonNull
    protected View inflate(@LayoutRes final int layout, final @Nullable ViewGroup parent) {

        return inflate(layout, parent, false);
    }
}
