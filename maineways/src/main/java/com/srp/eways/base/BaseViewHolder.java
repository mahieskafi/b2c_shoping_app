package com.srp.eways.base;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Created by Eskafi on 7/22/2019.
 */
public abstract class BaseViewHolder<T,V extends View> extends RecyclerView.ViewHolder {

    private BaseRecyclerListener mListener;
    private V mView;

    public BaseViewHolder(V itemView) {

        super(itemView);
        mView = itemView;
    }

    public BaseViewHolder(V itemView, BaseRecyclerListener mListener) {

        super(itemView);
        mView = itemView;
        mListener = mListener;
    }

    public V getView() {

        return mView;
    }

    public abstract void onBind(T item);

    public void onBind(T item, List<Object> objects) {

        onBind(item);
    }

    protected BaseRecyclerListener getListener() {

        return mListener;
    }
}
