package com.srp.eways.base;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.srp.eways.R;
import com.srp.eways.di.DIMain;
import com.srp.eways.network.NetworkResponseCodes;
import com.srp.eways.ui.IContentLoadingStateManager;
import com.srp.eways.ui.navigation.NavigationMemberFragment;
import com.srp.eways.ui.view.EmptyView;
import com.srp.eways.ui.view.LoadingStateView;
import com.srp.eways.util.PaginationScrollListener;

import java.util.ArrayList;

import ir.abmyapp.androidsdk.IABResources;

/**
 * Created by Eskafi on 2/2/2020.
 */
public abstract class BasePageableListFragment<V extends BaseViewModel> extends NavigationMemberFragment<V> implements IContentLoadingStateManager {

    public interface OnRecyclereScrollListener {
        boolean hasMoreItems();

        void loadMoreItems();
    }

    protected RecyclerView mRecyclerView;

    protected EmptyView mEmptyView;
    protected LoadingStateView mLoadingStateView;

    protected String mErrorMessage;

    protected BaseRecyclerAdapter2 mAdapter;

    private OnRecyclereScrollListener mScrollListener;

    public void setLoadingRetryListener(LoadingStateView.RetryListener listener) {
        mLoadingStateView.setRetryListener(listener);

    }

    public void setPaginationScrollListener(final OnRecyclereScrollListener listener) {
        mScrollListener = listener;

        mRecyclerView.addOnScrollListener(new PaginationScrollListener(((LinearLayoutManager) mRecyclerView.getLayoutManager())) {
            @Override
            protected boolean hasMoreItems() {
                if (listener != null) {
                    return listener.hasMoreItems();
                }
                return false;
            }

            @Override
            protected void loadMoreItems() {
                if (listener != null) {
                    listener.loadMoreItems();
                }
            }
        });
    }

    protected void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
    }

    protected void setLoadingState(int loadingState) {
        final IABResources abResources = DIMain.getABResources();

        switch (loadingState) {
            case IContentLoadingStateManager.VIEWSTATE_SHOW_LOADING:
                mLoadingStateView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);

                mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_LOADING, abResources.getString(R.string.loading_message), false);
                break;

            case IContentLoadingStateManager.VIEWSTATE_SHOW_CONTENT:
                mLoadingStateView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);

                break;

            case LoadingStateView.STATE_ERROR:
            case IContentLoadingStateManager.VIEWSTATE_SHOW_ERROR:
                mLoadingStateView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);

                String errorDescription = mErrorMessage != null ? mErrorMessage : abResources.getString(R.string.network_error_undefined);

                mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_ERROR, errorDescription, true);

                break;

            case IContentLoadingStateManager.VIEWSTATE_SHOW_EMPTY:
                mLoadingStateView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                break;

            case IContentLoadingStateManager.VIEWSTATE_LOAD_MORE_ERROR:
                mAdapter.removeMoreDataLoading();
                break;

            case IContentLoadingStateManager.VIEWSTATE_NO_INTERNET_ERROR:
                mLoadingStateView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);

                mLoadingStateView.setStateAndDescription(LoadingStateView.STATE_ERROR, abResources.getString(R.string.network_error_no_internet), false);
                break;
        }
    }

    protected void checkDataStatus(ArrayList<?> data, int status) {
        if (status == NetworkResponseCodes.SUCCESS) {
            if (data != null && data.size() > 0) {
                if (mAdapter.getData() != null && mAdapter.getData().size() > 0) {
                    mAdapter.appendData(data);
                } else {
                    mAdapter.setNewData(data);
                }

                mAdapter.setHasMoreData(mScrollListener.hasMoreItems());

                setLoadingState(IContentLoadingStateManager.VIEWSTATE_SHOW_CONTENT);
            } else if (mAdapter.getData()== null || (mAdapter.getData()!= null && mAdapter.getData().size() == 0)) {
                setLoadingState(IContentLoadingStateManager.VIEWSTATE_SHOW_EMPTY);
            } else {
                setLoadingState(IContentLoadingStateManager.VIEWSTATE_LOAD_MORE_ERROR);
            }
        } else {
            if (mAdapter.getData()== null || (mAdapter.getData()!= null && mAdapter.getData().size() == 0)) {
                setLoadingState(IContentLoadingStateManager.VIEWSTATE_SHOW_ERROR);
            } else {
                setLoadingState(IContentLoadingStateManager.VIEWSTATE_LOAD_MORE_ERROR);
            }
        }
    }
}
